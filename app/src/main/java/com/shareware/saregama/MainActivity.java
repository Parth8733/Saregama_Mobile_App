package com.shareware.saregama;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.shareware.saregama.Model.Brand;
import com.shareware.saregama.ViewHolder.BrandViewHolder;
import com.shareware.saregama.common.Common;

import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference categories;
    DatabaseReference modelList;
    FirebaseRecyclerAdapter<Brand, BrandViewHolder> adapter;
    FirebaseRecyclerOptions options;
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
    private TextView nmn;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        categories=database.getReference("Brand");

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
            fab.setEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        nmn = (TextView)view.findViewById(R.id.textViewName);
        nmn.setText(username);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadMenu() {

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

        options = new FirebaseRecyclerOptions.Builder<Brand>()
                .setQuery(categories, new SnapshotParser<Brand>() {
                    @NonNull
                    @Override
                    public Brand parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Brand(snapshot.child("name").getValue().toString(),
                                snapshot.child("image").getValue().toString());
                    }
                })
                .build();

        adapter= new FirebaseRecyclerAdapter<Brand, BrandViewHolder>(options) {
            @NonNull
            @Override
            public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.brands_list_design, viewGroup, false);

                return new BrandViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BrandViewHolder viewHolder, int position, @NonNull final Brand model) {

                Glide.with(MainActivity.this).load(model.getImage()).into(viewHolder.imageView);
                progressDialog.dismiss();
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(MainActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,ModelList.class);
                        intent.putExtra("Brand",adapter.getRef(position).getKey());
                        intent.putExtra("brand",model.getName());
                        intent.putExtra("activity","abc");
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
                    Toast.makeText(MainActivity.this, "New Catergory "+newCategory.getName() +" was added", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, ""+e.getMessage() ,Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, ""+e.getMessage() ,Toast.LENGTH_SHORT).show();
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
     @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(MainActivity.this,MainPage.class);
            i.putExtra("userid",username);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_logout){
            editor.remove("id");
            editor.remove("password");
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this,InquiryViewForm.class);
            intent.putExtra("id",username);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this,ViewSellerForm.class);
            intent.putExtra("id",username);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow && username.equals("Admin")) {
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
            intent.putExtra("id",username);
            startActivity(intent);
        }
        else if (id ==R.id.nav_2ndInquiry){
            //Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,secondInquiryForm.class);
            intent.putExtra("id",username);
            startActivity(intent);
        }
        else if(id==R.id.nav_2ndPurchase){
   //         Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,secondMobilePurchase.class);
            intent.putExtra("id",username);
            startActivity(intent);

        }
        else if(id==R.id.nav_2ndSeller){
           // Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MainActivity.this,secondMobileSeller.class);
            intent.putExtra("id",username);
            startActivity(intent);

        }
        else if(id==R.id.nav_accessaryinquiry){
            Intent intent = new Intent(MainActivity.this,Accessary_admin.class);
            intent.putExtra("id",username);
            startActivity(intent);

            //Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();

        }
        else if(id==R.id.nav_repairinginquiry){
            Intent intent = new Intent(MainActivity.this,Repairing_admin.class);
            intent.putExtra("id",username);
            startActivity(intent);

            // Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();

        }
        else if(id==R.id.nav_upcomingMobile && username.equals("Admin")){
           // Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,upComingMobileAdmin.class);
            intent.putExtra("id",username);
            startActivity(intent);
        }
        else if(id==R.id.nav_upcomingMobileForm){
         //   Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,upComingMobileForm.class);
            intent.putExtra("id",username);
            startActivity(intent);
        }
        else if(id==R.id.nav_usersList && username.equals("Admin")){
            //Toast.makeText(this, "Coming Soon!!!!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(MainActivity.this,UserListActivity.class);
            startActivity(i);

        }
        else if(id == R.id.nav_dataentry){
            Intent intent = new Intent(MainActivity.this,DataEntryAdmin.class);
            intent.putExtra("id",username);
            startActivity(intent);
        }

        else {
            Toast.makeText(this, "You are not Admin", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
