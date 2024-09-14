package com.arpitrawat.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arpitrawat.expensemanager.R;
import com.arpitrawat.expensemanager.databinding.RowAccountBinding;
import com.arpitrawat.expensemanager.models.Account;

import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>{

    Context context;
    ArrayList<Account> accountArrayList;
    public AccountsAdapter(Context context, ArrayList<Account> accountArrayList, AccountsClickListner accountsClickListner){
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.accountsClickListner = accountsClickListner;
    }

    public interface AccountsClickListner{
        void onAccountSelected(Account account);
    }

    AccountsClickListner accountsClickListner;

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        Account account = accountArrayList.get(position);
        holder.binding.accountName.setText(account.getAccountName());
        holder.itemView.setOnClickListener(c->{
            accountsClickListner.onAccountSelected(account);
        });
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public class AccountsViewHolder extends RecyclerView.ViewHolder{

        RowAccountBinding binding;
        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountBinding.bind(itemView);
        }
    }
}
