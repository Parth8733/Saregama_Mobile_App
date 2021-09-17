package com.shareware.saregama.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.R;
import com.shareware.saregama.common.Common;


/**
 * Created by SID on 2018-03-23.
 */

public class ModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView mobile_Name;
    public ImageView mobile_imageView;
    public TextView quantity;
    public  TextView mobile_price;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ModelViewHolder(View itemView) {
        super(itemView);
        mobile_Name =(TextView)itemView.findViewById(R.id.mobile_name);
        mobile_price=(TextView)itemView.findViewById(R.id.mobile_price);
        quantity= (TextView)itemView.findViewById(R.id.mobile_quantity);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
