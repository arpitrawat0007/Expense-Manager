package com.arpitrawat.expensemanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arpitrawat.expensemanager.R;
import com.arpitrawat.expensemanager.adapters.AccountsAdapter;
import com.arpitrawat.expensemanager.adapters.CategoryAdapter;
import com.arpitrawat.expensemanager.databinding.FragmentAddTransactionBinding;
import com.arpitrawat.expensemanager.databinding.ListDialogBinding;
import com.arpitrawat.expensemanager.models.Account;
import com.arpitrawat.expensemanager.models.Category;
import com.arpitrawat.expensemanager.models.Transaction;
import com.arpitrawat.expensemanager.utils.Constants;
import com.arpitrawat.expensemanager.utils.Helper;
import com.arpitrawat.expensemanager.viewmodels.MainViewModel;
import com.arpitrawat.expensemanager.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {


    public AddTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentAddTransactionBinding.inflate(inflater);

       transaction = new Transaction();

       binding.incomeBtn.setOnClickListener(view -> {
           binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
           binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
           binding.expenseBtn.setTextColor(getContext().getColor(R.color.black));
           binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

           transaction.setType(Constants.INCOME);
       });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.black));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));

            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime()) ;
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(c-> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category("Salary", R.drawable.salary, R.color.category1));
            categories.add(new Category("Bussiness", R.drawable.business, R.color.category2));
            categories.add(new Category("Investment", R.drawable.investment, R.color.category3));
            categories.add(new Category("Loan", R.drawable.loan, R.color.category4));
            categories.add(new Category("Rent", R.drawable.rent, R.color.category5));
            categories.add(new Category("Other", R.drawable.other_money, R.color.category6));


            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                binding.category.setText(category.getCategoryName());
                transaction.setCategory(category.getCategoryName());
                categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);
            categoryDialog.show();

        });

        binding.account.setOnClickListener(c->{
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account>accounts = new ArrayList<>();
            accounts.add(new Account(0,"Cash"));
            accounts.add(new Account(0,"Card"));
            accounts.add(new Account(0,"Bank"));
            accounts.add(new Account(0,"UPI"));
            accounts.add(new Account(0,"Other"));


            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListner() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);
            accountsDialog.show();


        });

        binding.saveTransactionBtn.setOnClickListener(c-> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();

            if (transaction.getType().equals(Constants.EXPENSE)){
                transaction.setAmount(amount*-1);
            }else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            ((MainActivity)getActivity()).viewModel.addTransactions(transaction);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });
            return binding.getRoot();
    }
}