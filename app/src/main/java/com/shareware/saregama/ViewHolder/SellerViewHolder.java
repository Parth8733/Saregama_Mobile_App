package com.shareware.saregama.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.R;
import com.shareware.saregama.common.Common;


public class SellerViewHolder extends  RecyclerView.ViewHolder implements
        View.OnClickListener,View.OnCreateContextMenuListener{

    private ItemClickListener itemClickListener;

    public TextView buyerNameTv,modelNameTv,sellerNameTv,buyerNumberTv;
    public ImageView mobile_imageView;
    public CardView cardView;
    public SellerViewHolder(View itemView) {
        super(itemView);
        buyerNameTv=(itemView).findViewById(R.id.buyerNameTv);
        modelNameTv =(itemView).findViewById(R.id.modelNameTv);
        sellerNameTv= (itemView).findViewById(R.id.sellerNameTv);
        buyerNumberTv = (itemView).findViewById(R.id.buyerNumberTv);
        cardView =(itemView).findViewById(R.id.cardViewInquiry);
        mobile_imageView =(ImageView)itemView.findViewById(R.id.callbtn);
        mobile_imageView.setOnClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
