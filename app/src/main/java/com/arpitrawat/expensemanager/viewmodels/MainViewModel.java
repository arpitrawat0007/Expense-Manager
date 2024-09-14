package com.arpitrawat.expensemanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.arpitrawat.expensemanager.models.Transaction;
import com.arpitrawat.expensemanager.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    Realm realm;
    Calendar calendar;


    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
    }

    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income = 0;
        double expense = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;

        if (Constants.SELECTED_TAB == Constants.DAILY) {

            newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).findAll();

            income = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type", Constants.INCOME).sum("amount").doubleValue();

            expense = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type", Constants.EXPENSE).sum("amount").doubleValue();

            total = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).sum("amount").doubleValue();


        }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            income = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).equalTo("type", Constants.INCOME).sum("amount").doubleValue();

            expense = realm.where(Transaction.class).greaterThanOrEqualTo("date",startTime).lessThan("date", endTime).equalTo("type", Constants.EXPENSE).sum("amount").doubleValue();

            total = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).sum("amount").doubleValue();

            newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date",endTime).findAll();

        }

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);

    }

    public void addTransactions(Transaction transaction){
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(transaction);

        realm.commitTransaction();
    }

    public void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }

    void setupDatabase(){

        realm = Realm.getDefaultInstance();
    }


}
