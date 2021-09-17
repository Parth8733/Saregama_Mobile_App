package com.shareware.saregama.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.shareware.saregama.Model.SellerModel;
import com.shareware.saregama.R;

import java.util.ArrayList;

public class MySellerAdapter extends RecyclerView.Adapter<MySellerAdapter.ViewHolder> {
    Context context;
    ArrayList<SellerModel> listSeller;
    ArrayList<String> listSellerKey;

    public MySellerAdapter(Context context, ArrayList<SellerModel> listSeller, ArrayList<String> listSellerKey) {
        this.context = context;
        this.listSeller = listSeller;
        this.listSellerKey = listSellerKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.seller_list_design, viewGroup, false);
        return new ViewHolder(v);
    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        SellerModel seller =listSeller.get(i);
        viewHolder.buyerNameTv.setText(seller.getBuyerName());
        viewHolder.sellerNameTv.setText(seller.getSellerName());
        viewHolder.modelNameTv.setText(seller.getModelName());
        viewHolder.buyerMobileTv.setText(seller.getMobileNo());

    }

    @Override
    public int getItemCount() {
        return listSeller.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView buyerNameTv,modelNameTv,sellerNameTv,buyerMobileTv;

        public ViewHolder(View v) {
        super(v);
        buyerNameTv =v.findViewById(R.id.buyerNameTv);
        modelNameTv = v.findViewById(R.id.modelNameTv);
        sellerNameTv = v.findViewById(R.id.sellerNameTv);
        buyerMobileTv = v.findViewById(R.id.buyerNumberTv);

        }

    }
}
