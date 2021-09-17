package com.shareware.saregama;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shareware.saregama.Model.InquiryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Inquiry extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText buyname1,buyadd1,mobnum1,mobprice1,sellername1,comment1;
    Spinner brand1,compname1,mobcolor1;
    Button submit1;
    FirebaseDatabase database;
    DatabaseReference company;
    String modelname1,brandname1,check2,mobilecolor1,price1,brandnamelist1,modelnamelist1,colorslist1,currentdate1;
    ArrayList<String> brandlist1;
    ArrayList<String> modellist1;
    ArrayList<String> colorlist1;
    InquiryModel sellerModel;
    String usernm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        brand1 = (Spinner)findViewById(R.id.inq_mob_brand);
        submit1 = (Button)findViewById(R.id.finalpayment);
        buyname1 = (EditText)findViewById(R.id.inq_name);
        buyadd1 = (EditText)findViewById(R.id.inq_add);
        mobnum1 = (EditText)findViewById(R.id.inq_mob);
        comment1 = (EditText)findViewById(R.id.inq_comment);
        compname1 = (Spinner) findViewById(R.id.inq_mob_company);
        mobcolor1 = (Spinner) findViewById(R.id.inq_mob_color);
        mobprice1 = (EditText)findViewById(R.id.inq_mob_price);
        sellername1 = (EditText)findViewById(R.id.inq_sell_name);
        brand1.setOnItemSelectedListener(this);

        Intent i = getIntent();
        usernm = i.getStringExtra("id");

        brandlist1= new ArrayList<>();
        if (brandlist1 != null && brandlist1.size()!=0) {
            brandlist1.add("Select Brand:");
            ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(Inquiry.this, android.R.layout.simple_spinner_item, brandlist1);
            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            compname1.setAdapter(spinnerAdapter1);
        }

        modellist1 = new ArrayList<>();
        if (modellist1 != null&& modellist1.size()!=0) {
            modellist1.add("Select Model:");
            ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(Inquiry.this, android.R.layout.simple_spinner_item, modellist1);
            spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brand1.setAdapter(spinnerAdapter2);
        }

        colorlist1 = new ArrayList<>();
        if (colorlist1!=null&&colorlist1.size()!=0) {
            colorlist1.add("Select Color:");
            ArrayAdapter<String> spinnerAdapter3 = new ArrayAdapter<>(Inquiry.this, android.R.layout.simple_spinner_item, colorlist1);
            spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mobcolor1.setAdapter(spinnerAdapter3);
        }

        mobcolor1.setEnabled(false);
        mobcolor1.setClickable(false);
        brand1.setEnabled(false);
        brand1.setClickable(false);

        database = FirebaseDatabase.getInstance();
        company = database.getReference("Brand");
        company.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                brandlist1.clear();
                modellist1.clear();
                colorlist1.clear();
                mobprice1.setText("");
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    brandnamelist1=dataSnapshot1.child("name").getValue().toString();
                    brandlist1.add(brandnamelist1);


                }
                Collections.sort(brandlist1);
                brandlist1.add(0,"Select Brand:");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Inquiry.this,android.R.layout.simple_spinner_item, brandlist1);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                compname1.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        compname1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                modelname1 = parent.getItemAtPosition(position).toString();
                company = database.getReference("Model");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modellist1.clear();
                        colorlist1.clear();
                        mobprice1.setText("");
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            check2 = dataSnapshot1.child("brand").getValue().toString();
                            if (check2.equals(modelname1))
                            {
                                modelnamelist1 = dataSnapshot1.child("name").getValue().toString();
                                modellist1.add(modelnamelist1);

                            }
                        }
                        Set<String> unique = new HashSet<>();
                        unique.addAll(modellist1);
                        modellist1.clear();
                        modellist1.addAll(unique);
                        Collections.sort(modellist1);
                        modellist1.add(0,"Select Model:");
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Inquiry.this,android.R.layout.simple_spinner_item, modellist1);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        brand1.setAdapter(spinnerAdapter);
                        brand1.setEnabled(true);
                        brand1.setClickable(true);

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
        mobcolor1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mobilecolor1 = adapterView.getItemAtPosition(i).toString();
                company = database.getReference("Model");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            String check = dataSnapshot1.child("name").getValue().toString();
                            String checkk = dataSnapshot1.child("color").getValue().toString();
                            if (check.equals(brandname1) && checkk.equals(mobilecolor1)) {
                                price1 = dataSnapshot1.child("price").getValue().toString();
                                mobprice1.setText(price1);
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

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyname1.getText().toString().isEmpty())
                {
                    buyname1.setError("Enter Buyer Name ");
                }else if (buyadd1.getText().toString().isEmpty())
                {
                    buyadd1.setError("Enter Buyer Address ");
                }else if (mobnum1.getText().toString().isEmpty())
                {
                    mobnum1.setError("Enter Mobile Number ");
                }else if (brandnamelist1.equals("Select Brand:"))
                {
                    Toast.makeText(Inquiry.this, "Select Brand ", Toast.LENGTH_LONG).show();
                }else if (modelnamelist1.equals("Select Model:"))
                {
                    Toast.makeText(Inquiry.this, "Select Model ", Toast.LENGTH_LONG).show();
                }else if (colorslist1.equals("Select Color:"))
                {
                    Toast.makeText(Inquiry.this, "Select Color ", Toast.LENGTH_LONG).show();

                }else if (sellername1.getText().toString().isEmpty())
                {
                    sellername1.setError("Enter Seller Name");
                }else
                {
                    database = FirebaseDatabase.getInstance();
                    company = database.getReference("Inquiry");
                    Date currdate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    currentdate1 = dateFormat.format(currdate);
                    sellerModel = new InquiryModel(buyadd1.getText().toString(),
                                                    buyname1.getText().toString(),
                                                    mobilecolor1,modelname1,
                                                    mobnum1.getText().toString(),
                                                    mobprice1.getText().toString(),
                                                    brandname1,
                                                    sellername1.getText().toString(),currentdate1,comment1.getText().toString());
                    if (sellerModel!=null)
                    {
                        company.push().setValue(sellerModel);
                        Toast.makeText(Inquiry.this, "Data Entry Successfully !!!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Inquiry.this,MainPage.class);
                        i.putExtra("userid",usernm);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        brandname1 = adapterView.getItemAtPosition(i).toString();
        if (brandname1.equals("Select Brand: "))
        {
            Toast.makeText(this, "Please Select Brand !!", Toast.LENGTH_SHORT).show();
        }else {
            company = database.getReference("Model");
            company.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    colorlist1.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        String check = dataSnapshot1.child("name").getValue().toString();
                        if (check.equals(brandname1)) {
                            colorslist1 = dataSnapshot1.child("color").getValue().toString();
                            colorlist1.add(colorslist1);

                        }
                    }
                    Set<String> unique = new HashSet<>();
                    unique.addAll(colorlist1);
                    colorlist1.clear();
                    colorlist1.addAll(unique);
                    Collections.sort(colorlist1);
                    colorlist1.add(0,"Select Color:");
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Inquiry.this,android.R.layout.simple_spinner_item, colorlist1);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mobcolor1.setAdapter(spinnerAdapter);
                    mobcolor1.setEnabled(true);
                    mobcolor1.setClickable(true);
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