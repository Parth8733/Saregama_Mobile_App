package com.shareware.saregama.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shareware.saregama.Model.AccessoriesInquiryModel;
import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.R;

import java.util.ArrayList;


public class MyAccessaryAdapter extends RecyclerView.Adapter<MyAccessaryAdapter.ViewHolder> {

    Context context;
    ArrayList<AccessoriesInquiryModel> accessoriesInquiries;
    ArrayList<String> accessoriesKey;

    public MyAccessaryAdapter(Context context, ArrayList<AccessoriesInquiryModel> accessoriesInquiries, ArrayList<String> accessoriesKey) {
        this.context = context;
        this.accessoriesInquiries = accessoriesInquiries;
        this.accessoriesKey = accessoriesKey;
    }

    @NonNull
    @Override
    public MyAccessaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.accessories_list_design, viewGroup, false);
        return new MyAccessaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAccessaryAdapter.ViewHolder viewHolder, int i) {
        final AccessoriesInquiryModel inquiryModel = accessoriesInquiries.get(i);

        viewHolder.buyerNameTv.setText(inquiryModel.getBuyerName());
        viewHolder.buyerNumberTv.setText(inquiryModel.getMobileNo());
        viewHolder.sellerNameTv.setText(inquiryModel.getSellerName());

    }

    @Override
    public int getItemCount() {
        return accessoriesInquiries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView buyerNameTv, buyerNumberTv, sellerNameTv;
        public ViewHolder(View view) {
            super(view);
            buyerNameTv = view.findViewById(R.id.buyerNameTv);
            buyerNumberTv = view.findViewById(R.id.buyerNumberTv);
            sellerNameTv = view.findViewById(R.id.sellerNameTv);

        }
    }
}
