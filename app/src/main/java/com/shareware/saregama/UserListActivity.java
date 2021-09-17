package com.shareware.saregama;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.Model.User;
import com.shareware.saregama.ViewHolder.UserViewHolder;
import com.shareware.saregama.common.Common;

public class UserListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference sellerViewList;

    TextView userName,password;

    FirebaseRecyclerAdapter<User,UserViewHolder>adapter;
    private EditText etdUsername,etdPassword;

    FloatingActionButton floatingActionButton;
    private EditText etdName,etdAddPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        database = FirebaseDatabase.getInstance();
        sellerViewList = database.getReference("User");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_userlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.addUserButton_float);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        if (Common.isConnectedToInternet(getBaseContext())) {
            // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
            loadUserList();
        } else {
            Toast.makeText(UserListActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void addUser() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserListActivity.this);
        alertDialog.setTitle("Add New Company");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater layoutInflater =this.getLayoutInflater();
        View add_menu_layout=layoutInflater.inflate(R.layout.add_new_user_layout,null);

        etdName = add_menu_layout.findViewById(R.id.edtName);
        etdAddPassword = add_menu_layout.findViewById(R.id.edtaddPassword);

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_menu_slideshow);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                User newCategory = new User(etdName.getText().toString(), etdAddPassword.getText().toString());
                sellerViewList.push().setValue(newCategory);
                Toast.makeText(UserListActivity.this, "New User was added", Toast.LENGTH_SHORT).show();
                // Snackbar.make(drawer,"New Category "+newCategory.getName() +" was added",Snackbar.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

    }

    private void loadUserList() {

        adapter = new FirebaseRecyclerAdapter<User,UserViewHolder>(
                User.class,
                R.layout.user_list_design,
                UserViewHolder.class,sellerViewList){
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, final User model, int position) {
               viewHolder.username.setText("User Name: "+model.getName());
               viewHolder.password.setText("Password: " +model.getPassword());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //    Toast.makeText(ViewSellerForm.this, model.getMobileNo(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UserListActivity.this);
        mLayoutManager.setReverseLayout(true);

        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);

    }

    private void showUpdateDialog(final String key, final User item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserListActivity.this);
        alertDialog.setTitle("Update Details");
        alertDialog.setMessage("Check Full information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.update_userlist_design_layout, null);
        etdUsername = add_menu_layout.findViewById(R.id.etduserName);
        etdPassword = add_menu_layout.findViewById(R.id.etdPassword);

        etdUsername.setText(item.getName());
        etdPassword.setText(item.getPassword());

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_menu_slideshow);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                item.setName(etdUsername.getText().toString());
                item.setPassword(etdPassword.getText().toString());


                sellerViewList.child(key).setValue(item);
                Toast.makeText(UserListActivity.this, "Values Updated", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }

    private void deleteCategory(String key) {
        sellerViewList.child(key).removeValue();
        Toast.makeText(this, "Form Deleted!!!", Toast.LENGTH_SHORT).show();
    }
}
