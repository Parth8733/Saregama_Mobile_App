package com.shareware.saregama;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shareware.saregama.Interface.ItemClickListener;
import com.shareware.saregama.Model.Model;
import com.shareware.saregama.ViewHolder.ModelViewHolder;
import com.shareware.saregama.common.Common;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ModelList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference vehicleLists;

    String brandName = "";
    String brand = "";
    FirebaseRecyclerAdapter<Model, ModelViewHolder> adapter;

    FirebaseStorage storage;
    StorageReference storageReference;

    RelativeLayout rootlayout;
    FloatingActionButton fab;

    EditText edtName, edtPrice, edtQuantity, edtColor;
    Button btnUpload, btnSelect;

    String imagebrand,newmob;
    Uri saveUri;
    Model newVehicle;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_list);
        database = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_vehicle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootlayout = (RelativeLayout) findViewById(R.id.rootLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent i = getIntent();
        imagebrand = i.getStringExtra("image");
        brandName = i.getStringExtra("Brand");
        brand = i.getStringExtra("brand");
        newmob = i.getStringExtra("activity");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddModelDialog();
            }
        });
        if (newmob.equals("newmob")){
            vehicleLists = database.getReference("UpMobileModel");
            loadVehicleListNewMob(brandName);
        }
        else {
            vehicleLists = database.getReference("Model");
            loadVehicleList(brandName);
        }
    }

    Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child("brandId");

    FirebaseRecyclerOptions<Model> options =
            new FirebaseRecyclerOptions.Builder<Model>()
                    .setQuery(query, new SnapshotParser<Model>() {
                        @NonNull
                        @Override
                        public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                            return new Model(snapshot.child("Name").getValue().toString(),
                                    snapshot.child("Price").getValue().toString(),
                                    snapshot.child("BrandId").getValue().toString(),
                                    snapshot.child("Brand").getValue().toString(),
                                    snapshot.child("Color").getValue().toString(),
                                    snapshot.child("Quantity").getValue().toString());
                        }
                    })
                    .build();

    private void loadVehicleListNewMob(String brandName) {
        adapter = new FirebaseRecyclerAdapter<Model, ModelViewHolder>(options) {
            @NonNull
            @Override
            public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull ModelViewHolder viewHolder, int position, @NonNull Model model) {

                viewHolder.mobile_Name.setText("Model Name: "+model.getName());
                // Glide.with(ModelList.this).load(model.getImage()).into(viewHolder.mobile_imageView);
                viewHolder.quantity.setText("Color: "+model.getColor());
                viewHolder.mobile_price.setText("Price: "+model.getPrice());

                final Model local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        //  recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }

    private void loadVehicleList(String brandName) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("brandId");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("Name").getValue().toString(),
                                        snapshot.child("Price").getValue().toString(),
                                        snapshot.child("BrandId").getValue().toString(),
                                        snapshot.child("Brand").getValue().toString(),
                                        snapshot.child("Color").getValue().toString(),
                                        snapshot.child("Quantity").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ModelViewHolder>(options) {
            @NonNull
            @Override
            public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull ModelViewHolder viewHolder, int position, @NonNull Model model) {

                viewHolder.mobile_Name.setText("Model Name: "+model.getName());
                // Glide.with(ModelList.this).load(model.getImage()).into(viewHolder.mobile_imageView);
                viewHolder.quantity.setText("Color: "+model.getColor());
                viewHolder.mobile_price.setText("Price: "+model.getPrice());

                final Model local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
     //  recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }

    private void showAddModelDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModelList.this);
        alertDialog.setTitle("Add New Model");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_model_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        edtQuantity = add_menu_layout.findViewById(R.id.edtQuantity);
        edtColor = add_menu_layout.findViewById(R.id.edtColor);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
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
                //  Toast.makeText(VehicleList.this, "btn pressed", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_menu_slideshow);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                uploadImage();
                if (newVehicle != null) {
                    vehicleLists.push().setValue(newVehicle);
                    Toast.makeText(ModelList.this, "New Model " + newVehicle.getName() + " was added", Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST);


    }

    private void uploadImage() {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

                    dialog.dismiss();
                  //  Toast.makeText(ModelList.this, "Uploaded!!", Toast.LENGTH_SHORT).show();

        newVehicle = new Model(edtName.getText().toString(), edtPrice.getText().toString(), brandName, brand, edtColor.getText().toString(), edtQuantity.getText().toString());




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

    private void showUpdateDialog(final String key, final Model item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModelList.this);
        alertDialog.setTitle("Update Mobile Model");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_model_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        edtQuantity = add_menu_layout.findViewById(R.id.edtQuantity);
        edtColor = add_menu_layout.findViewById(R.id.edtColor);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        edtName.setText(item.getName());
        edtPrice.setText(item.getPrice());
        edtColor.setText(item.getColor());
        edtPrice.setText(item.getPrice());
        edtQuantity.setText(item.getQuantity());
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
                item.setPrice(edtPrice.getText().toString());
                item.setColor(edtColor.getText().toString());
                item.setQuantity(edtQuantity.getText().toString());
                vehicleLists.child(key).setValue(item);
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
        vehicleLists.child(key).removeValue();
        Toast.makeText(this, "Item Deleted!!!", Toast.LENGTH_SHORT).show();
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
    private void changeImage(final Model item) {
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
                    Toast.makeText(ModelList.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ModelList.this, ""+e.getMessage() ,Toast.LENGTH_SHORT).show();
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
