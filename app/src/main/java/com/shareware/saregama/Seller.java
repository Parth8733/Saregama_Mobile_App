package com.shareware.saregama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.dropdownview.DropDownView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shareware.saregama.Model.SellerModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Seller extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText buyname,buyadd,mobnum,mobprice,mobserialno,sellername,quantity;
    Spinner brand,compname,mobcolor;
    Button submit;
    FirebaseDatabase database;
    DatabaseReference company,company1;
    String modelname,brandname,check1,mobilecolor,price,brandnamelist,modelnamelist,colorslist,currentdate,quantities;
    ArrayList<String> brandlist;
    ArrayList<String> modellist;
    ArrayList<String> colorlist;
    SellerModel sellerModel;
    String usernm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        brand = (Spinner)findViewById(R.id.buy_mob_brand);
        submit = (Button)findViewById(R.id.submit);
        buyname = (EditText)findViewById(R.id.buy_name);
        buyadd = (EditText)findViewById(R.id.buy_add);
        mobnum = (EditText)findViewById(R.id.buy_mob);
        compname = (Spinner) findViewById(R.id.buy_mob_company);
        mobcolor = (Spinner) findViewById(R.id.buy_mob_color);
        mobprice = (EditText)findViewById(R.id.buy_mob_price);
        mobserialno = (EditText)findViewById(R.id.buy_mob_serialnumb);
        sellername = (EditText)findViewById(R.id.sell_name);
        quantity = (EditText)findViewById(R.id.buy_mob_quantity);
        brand.setOnItemSelectedListener(this);
        mobprice.setEnabled(false);

        Intent i = getIntent();
        usernm = i.getStringExtra("id");
        brandlist = new ArrayList<>();
        if (brandlist!=null && brandlist.size() !=0) {
            brandlist.add("Select Brand:");
            ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(Seller.this, android.R.layout.simple_spinner_item, brandlist);
            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            compname.setAdapter(spinnerAdapter1);
        }

        modellist = new ArrayList<>();
        if (modellist!= null && modellist.size() !=0) {
            modellist.add("Select Model:");
            ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(Seller.this, android.R.layout.simple_spinner_item, modellist);
            spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brand.setAdapter(spinnerAdapter2);
        }

        colorlist = new ArrayList<>();
        if (colorlist != null && colorlist.size()!=0) {
            colorlist.add("Select Color:");
            ArrayAdapter<String> spinnerAdapter3 = new ArrayAdapter<>(Seller.this, android.R.layout.simple_spinner_item, colorlist);
            spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mobcolor.setAdapter(spinnerAdapter3);
        }

        mobcolor.setEnabled(false);
        mobcolor.setClickable(false);
        brand.setEnabled(false);
        brand.setClickable(false);

        database = FirebaseDatabase.getInstance();
        company = database.getReference("Brand");
        company.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                brandlist.clear();
                modellist.clear();
                colorlist.clear();
                mobprice.setText("");
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    brandnamelist=dataSnapshot1.child("name").getValue().toString();
                    brandlist.add(brandnamelist);

                }

                Collections.sort(brandlist);
                brandlist.add(0,"Select Brand:");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Seller.this,android.R.layout.simple_spinner_item, brandlist);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                compname.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        compname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                modelname = parent.getItemAtPosition(position).toString();
                company = database.getReference("Model");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modellist.clear();
                        colorlist.clear();
                        mobprice.setText("");

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            check1 = dataSnapshot1.child("brand").getValue().toString();
                            if (check1.equals(modelname))
                            {
                                modelnamelist = dataSnapshot1.child("name").getValue().toString();
                                modellist.add(modelnamelist);

                            }
                        }
                        Set<String> unique = new HashSet<>();
                        unique.addAll(modellist);
                        modellist.clear();
                        modellist.addAll(unique);
                        modellist.add(0,"Select Model:");
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Seller.this,android.R.layout.simple_spinner_item, modellist);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        brand.setAdapter(spinnerAdapter);
                        brand.setEnabled(true);
                        brand.setClickable(true);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        mobcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mobilecolor = adapterView.getItemAtPosition(i).toString();
                company = database.getReference("Model");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            String check = dataSnapshot1.child("name").getValue().toString();
                            String checkk = dataSnapshot1.child("color").getValue().toString();
                            if (check.equals(brandname) && checkk.equals(mobilecolor)) {
                                price = dataSnapshot1.child("price").getValue().toString();
                                    mobprice.setText(price);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyname.getText().toString().isEmpty())
                {
                    buyname.setError("Enter Buyer Name ");
                }else if (buyadd.getText().toString().isEmpty())
                {
                    buyadd.setError("Enter Buyer Address ");
                }else if (mobnum.getText().toString().isEmpty())
                {
                    mobnum.setError("Enter Mobile Number ");
                }else if (brandnamelist.equals("Select Brand:"))
                {
                    Toast.makeText(Seller.this, "Select Brand ", Toast.LENGTH_LONG).show();
                }else if (modelnamelist.equals("Select Model:"))
                {
                    Toast.makeText(Seller.this, "Select Model ", Toast.LENGTH_LONG).show();
                }else if (colorslist.equals("Select Color:"))
                {
                    Toast.makeText(Seller.this, "Select Color ", Toast.LENGTH_LONG).show();
                }else if (mobserialno.getText().toString().isEmpty())
                {
                    mobserialno.setError("Enter Mobile Serial Number ");
                }else if (sellername.getText().toString().isEmpty())
                {
                    sellername.setError("Enter Seller Name");
                }else
                {
                    database = FirebaseDatabase.getInstance();
                    company = database.getReference("Seller");
                    Date currdate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    currentdate = dateFormat.format(currdate);
                    sellerModel = new SellerModel(buyadd.getText().toString(),
                                                    buyname.getText().toString(),
                                                    mobilecolor,modelname,
                                                    mobnum.getText().toString(),
                                                    mobprice.getText().toString(),
                                                    mobserialno.getText().toString(),
                                                    brandname,
                                                    sellername.getText().toString(),currentdate,
                                                    quantity.getText().toString(),null);
                    if (sellerModel!=null)
                    {
                        company.push().setValue(sellerModel);
                        Toast.makeText(Seller.this, "Data Entry Successfully !!!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Seller.this,MainPage.class);
                        i.putExtra("userid",usernm);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        brandname = adapterView.getItemAtPosition(i).toString();
        if (brandname.equals("Select Brand: "))
        {
            Toast.makeText(this, "Please Select Brand !!", Toast.LENGTH_SHORT).show();
        }else {
            company = database.getReference("Model");
            company.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    colorlist.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        String check = dataSnapshot1.child("name").getValue().toString();
                        if (check.equals(brandname)) {
                            colorslist = dataSnapshot1.child("color").getValue().toString();
                            colorlist.add(colorslist);

                        }
                    }
                    Collections.sort(colorlist);
                    colorlist.add(0,"Select Color:");
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Seller.this,android.R.layout.simple_spinner_item, colorlist);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mobcolor.setAdapter(spinnerAdapter);
                    mobcolor.setEnabled(true);
                    mobcolor.setClickable(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
