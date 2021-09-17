package com.shareware.saregama.ViewHolder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.R;
import com.shareware.saregama.ViewSellerForm;
import com.shareware.saregama.common.Common;

import java.util.ArrayList;


public class MyInquiryAdapter extends RecyclerView.Adapter<MyInquiryAdapter.ViewHolder>{


    Context context;
    ArrayList<InquiryModel> listInquiryModel;
    ArrayList<String> listInquiryKey;
    public MyInquiryAdapter(Context context, ArrayList<InquiryModel> listInquiryModel, ArrayList<String> listInquiryKey) {
        this.context = context;
        this.listInquiryModel = listInquiryModel;
        this.listInquiryKey = listInquiryKey;
        //Toast.makeText(context, String.valueOf(listInquiryModel.size())+" " +String.valueOf(listInquiryKey.size()), Toast.LENGTH_SHORT).show();
    }



    @NonNull
    @Override
    public MyInquiryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.inquiry_list_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInquiryAdapter.ViewHolder viewHolder, final int i) {

        final InquiryModel inquiryModel = listInquiryModel.get(i);

        viewHolder.buyerNameTv.setText(inquiryModel.getBuyerName());
        viewHolder.buyerNumberTv.setText(inquiryModel.getMobileNo());
        viewHolder.modelNameTv.setText(inquiryModel.getModelName());
        viewHolder.sellerNameTv.setText(inquiryModel.getSellerName());

    }

    @Override
    public int getItemCount() {
        return listInquiryModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView buyerNameTv, buyerNumberTv, modelNameTv, sellerNameTv;
        public CardView cardView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            buyerNameTv = view.findViewById(R.id.buyerNameTv);
            buyerNumberTv = view.findViewById(R.id.buyerNumberTv);
            modelNameTv = view.findViewById(R.id.modelNameTv);
            sellerNameTv = view.findViewById(R.id.sellerNameTv);

        }



    }
}
