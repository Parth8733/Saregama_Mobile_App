package com.shareware.saregama.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.R;
import com.shareware.saregama.common.Common;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView username,password;
    private ItemClickListener itemClickListener;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        username =(TextView)itemView.findViewById(R.id.userNameTv);
        password=(TextView)itemView.findViewById( R.id.passwordNameTv);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }


    public  void setItemClickListener (ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
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
