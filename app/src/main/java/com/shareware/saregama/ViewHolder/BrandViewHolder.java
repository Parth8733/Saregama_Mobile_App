package com.shareware.saregama.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;


import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.R;
import com.shareware.saregama.common.Common;


/**
 * Created by SID on 2018-03-29.
 */

public class BrandViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,View.OnCreateContextMenuListener {

//    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public BrandViewHolder(View itemView) {
        super(itemView);
       // txtMenuName =(TextView)itemView.findViewById(R.id.menu_name);
        imageView =(ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }
    public  void setItemClickListener (ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View view)   {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
