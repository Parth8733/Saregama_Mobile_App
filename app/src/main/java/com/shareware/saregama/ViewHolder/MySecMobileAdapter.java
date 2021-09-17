package com.shareware.saregama.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shareware.saregama.Model.SecondMobPurchase;
import com.shareware.saregama.R;

import java.util.ArrayList;

public class MySecMobileAdapter extends RecyclerView.Adapter<MySecMobileAdapter.ViewHolder> {

    Context context;
    ArrayList<SecondMobPurchase> secondMobPurchasesList;
    ArrayList<String> secondMobPurchaseKey;

    public MySecMobileAdapter(Context context, ArrayList<SecondMobPurchase> secondMobPurchasesList, ArrayList<String> secondMobPurchaseKey) {
        this.context = context;
        this.secondMobPurchasesList = secondMobPurchasesList;
        this.secondMobPurchaseKey = secondMobPurchaseKey;
    }

    @NonNull
    @Override
    public MySecMobileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate( R.layout.secmobpurchase_list_design, viewGroup, false);
        return new MySecMobileAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MySecMobileAdapter.ViewHolder viewHolder, int i) {

        final SecondMobPurchase secondMobPurchase = secondMobPurchasesList.get(i);

        viewHolder.buyerNameTv.setText(secondMobPurchase.getBuyerName());
        viewHolder.buyerNumberTv.setText(secondMobPurchase.getMobileNo());
        viewHolder.modelNameTv.setText(secondMobPurchase.getModelName());
        viewHolder.sellerNameTv.setText(secondMobPurchase.getSellerName());

    }

    @Override
    public int getItemCount() {
        return secondMobPurchasesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView buyerNameTv, buyerNumberTv, modelNameTv, sellerNameTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerNameTv = itemView.findViewById(R.id.buyerNameTv);
            buyerNumberTv = itemView.findViewById(R.id.buyerNumberTv);
            modelNameTv = itemView.findViewById(R.id.modelNameTv);
            sellerNameTv = itemView.findViewById(R.id.sellerNameTv);

        }
    }
}
