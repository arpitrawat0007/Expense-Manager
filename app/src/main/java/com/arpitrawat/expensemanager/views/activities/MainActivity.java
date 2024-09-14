package com.arpitrawat.expensemanager.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arpitrawat.expensemanager.adapters.TransactionsAdapter;
import com.arpitrawat.expensemanager.models.Transaction;
import com.arpitrawat.expensemanager.utils.Constants;
import com.arpitrawat.expensemanager.utils.Helper;
import com.arpitrawat.expensemanager.viewmodels.MainViewModel;
import com.arpitrawat.expensemanager.views.fragments.AddTransactionFragment;
import com.arpitrawat.expensemanager.R;
import com.arpitrawat.expensemanager.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    /*
    * 0 = Daily
    * 1 = Monthly
    * 2 = Calendar
    * 3 = Summary
    * 4 = Notes
    */

    Calendar calendar;


    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        setSupportActionBar(binding.Toolbar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.perviousDateBtn.setOnClickListener(c->{
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.FloatingActionButton.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));

        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
                binding.transactionsList.setAdapter(transactionsAdapter);
                if(transactions.size() > 0) {
                    binding.nodata.setVisibility(View.GONE);
                }else {
                    binding.nodata.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")){
                   Constants.SELECTED_TAB = 1;
                    updateDate();
                }else if (tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
            binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));

            }
        });

        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));

            }
        });
        viewModel.getTransactions(calendar);

    }

    public void getTransactions(){
        viewModel.getTransactions(calendar);
    }


    void updateDate(){

        if (Constants.SELECTED_TAB == Constants.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}