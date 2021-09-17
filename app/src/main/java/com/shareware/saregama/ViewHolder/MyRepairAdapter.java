package com.shareware.saregama.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.Model.RepairInquiryModel;
import com.shareware.saregama.R;

import java.util.ArrayList;

public class MyRepairAdapter extends RecyclerView.Adapter<MyRepairAdapter.ViewHolder> {

    Context context;
    ArrayList<RepairInquiryModel> repairInquiryModelArrayList;
    ArrayList<String> repairInquiryKey;

    public MyRepairAdapter(Context context, ArrayList<RepairInquiryModel> repairInquiryModelArrayList, ArrayList<String> repairInquiryKey) {
        this.context = context;
        this.repairInquiryModelArrayList = repairInquiryModelArrayList;
        this.repairInquiryKey = repairInquiryKey;
    }

    @NonNull
    @Override
    public MyRepairAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.inquiry_list_design, viewGroup, false);
        return new MyRepairAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRepairAdapter.ViewHolder viewHolder, int i) {

        final RepairInquiryModel inquiryModel = repairInquiryModelArrayList.get(i);

        viewHolder.buyerNameTv.setText(inquiryModel.getBuyerName());
        viewHolder.buyerNumberTv.setText(inquiryModel.getMobileNo());
        viewHolder.modelNameTv.setText(inquiryModel.getModelName());
        viewHolder.sellerNameTv.setText(inquiryModel.getSellerName());

    }

    @Override
    public int getItemCount() {
        return repairInquiryModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView buyerNameTv, buyerNumberTv, modelNameTv, sellerNameTv;

        public ViewHolder(View view) {
            super(view);
            buyerNameTv = view.findViewById(R.id.buyerNameTv);
            buyerNumberTv = view.findViewById(R.id.buyerNumberTv);
            modelNameTv = view.findViewById(R.id.modelNameTv);
            sellerNameTv = view.findViewById(R.id.sellerNameTv);


        }
    }
}
