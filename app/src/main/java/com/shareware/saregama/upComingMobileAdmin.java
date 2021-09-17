package com.shareware.saregama;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.Model.Brand;
import com.shareware.saregama.ViewHolder.BrandViewHolder;
import com.shareware.saregama.common.Common;

import java.util.UUID;

public class upComingMobileAdmin extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference categories;
    DatabaseReference modelList;
    FirebaseRecyclerAdapter<Brand, BrandViewHolder> adapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    Brand newCategory;
    EditText edtName;
    int counter=0;
    Button btnUpload,btnSelect;
    Uri saveUri;
    private  final  int PICK_IMAGE_REQUEST = 71;
    RecyclerView recycler_menu;
    ProgressDialog progressDialog;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String username;
    FloatingActionButton fab;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_mobile_admin);

        database = FirebaseDatabase.getInstance();
        categories=database.getReference("UpMobileBrand");

        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference("Companies logo/");
        preferences=getSharedPreferences("uuid",MODE_PRIVATE);
        editor=preferences.edit();
        username = preferences.getString("id","");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        recycler_menu= (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(this);

        if (username.equals("Admin")) {
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            loadMenu();
        }
        else
            fab.setVisibility(View.GONE);


    }

    private void loadMenu() {
        adapter= new FirebaseRecyclerAdapter<Brand, BrandViewHolder>(
                Brand.class,
                R.layout.brands_list_design,
                BrandViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(BrandViewHolder viewHolder, final Brand model, int position) {


                Glide.with(upComingMobileAdmin.this).load(model.getImage()).into(viewHolder.imageView);
                progressDialog.dismiss();
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(MainActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(upComingMobileAdmin.this,ModelList.class);
                        intent.putExtra("Brand",adapter.getRef(position).getKey());
                        intent.putExtra("brand",model.getName());
                        intent.putExtra("activity","newmob");
                        //   intent.putExtra("logo",model.getImage());
                        startActivity(intent);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        recycler_menu.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
        else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        categories.child(key).removeValue();
        if(categories.child("brandId").equals(key))
        {
            categories.child("brandId").removeValue();
        }
        Toast.makeText(this, "Item Deleted!!!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final Brand item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(upComingMobileAdmin.this);
        alertDialog.setTitle("Update information");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater layoutInflater =this.getLayoutInflater();
        View add_menu_layout=layoutInflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        edtName.setText(item.getName());
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_add_white_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                item.setName(edtName.getText().toString());
                categories.child(key).setValue(item);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Image Selected ");
        }

    }
    private void showDialog()
    {final AlertDialog.Builder alertDialog = new AlertDialog.Builder(upComingMobileAdmin.this);
        alertDialog.setTitle("Add New Company");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater layoutInflater =this.getLayoutInflater();
        View add_menu_layout=layoutInflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_menu_slideshow);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                if (newCategory!=null)
                {
                    categories.push().setValue(newCategory);
                    Toast.makeText(upComingMobileAdmin.this, "New Catergory "+newCategory.getName() +" was added", Toast.LENGTH_SHORT).show();
                    // Snackbar.make(drawer,"New Category "+newCategory.getName() +" was added",Snackbar.LENGTH_SHORT).show();
                }
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
    private void uploadImage() {


        if (saveUri != null)
        {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFloder = storageReference.child("images/"+imageName);
            imageFloder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(upComingMobileAdmin.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                    imageFloder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            newCategory = new Brand(edtName.getText().toString(),uri.toString());

                        }
                    });

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(upComingMobileAdmin.this, ""+e.getMessage() ,Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Uploaded "+progress+"%");
                        }
                    });
        }
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),PICK_IMAGE_REQUEST);

    }
    private void changeImage(final Brand item) {
        if (saveUri != null)
        {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFloder = storageReference.child("images/"+imageName);
            imageFloder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(upComingMobileAdmin.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                    imageFloder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            item.setImage(uri.toString());
                        }
                    });

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(upComingMobileAdmin.this, ""+e.getMessage() ,Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Uploaded "+progress+"%");
                        }
                    });
        }
    }

}
