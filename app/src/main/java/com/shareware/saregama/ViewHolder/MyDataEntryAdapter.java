package com.shareware.saregama.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shareware.saregama.DataEntryAdmin;
import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.Model.User;
import com.shareware.saregama.R;

import java.util.ArrayList;

public class MyDataEntryAdapter extends RecyclerView.Adapter<MyDataEntryAdapter.ViewHolder>{
    Context context;
    ArrayList<User> listInquiryModel;
    ArrayList<String> listInquiryKey;
    public MyDataEntryAdapter(Context context, ArrayList<User> listInquiryModel, ArrayList<String> listInquiryKey) {
        this.context = context;
        this.listInquiryModel = listInquiryModel;
        this.listInquiryKey = listInquiryKey;
        //Toast.makeText(context, String.valueOf(listInquiryModel.size())+" " +String.valueOf(listInquiryKey.size()), Toast.LENGTH_SHORT).show();
    }



    @NonNull
    @Override
    public MyDataEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dataentry_list_design, viewGroup, false);
        return new MyDataEntryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataEntryAdapter.ViewHolder viewHolder, final int i) {

        final User inquiryModel = listInquiryModel.get(i);

        viewHolder.buyerNameTv.setText(inquiryModel.getName());
        viewHolder.buyerNumberTv.setText(inquiryModel.getNumber());

    }

    @Override
    public int getItemCount() {
        return listInquiryModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView buyerNameTv, buyerNumberTv;

        public ViewHolder(View view) {
            super(view);
            buyerNameTv = view.findViewById(R.id.buyerNameTv);
            buyerNumberTv = view.findViewById(R.id.buyerNumberTv);
        }



    }
}

