package com.shareware.saregama;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.List;
import com.shareware.saregama.Model.SellerModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MobileSale extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText uname,address,mobileno,price,iemino,billdate,sellername;
    Spinner brandspinner,modelspinner,colorspinner;
    CheckBox charger,headphone;
    Button salesubmit;
    SellerModel sellerModel;
    FirebaseDatabase database;
    DatabaseReference company;
    ArrayList<String> mobilebrandlist;
    ArrayList<String> mobilemodellist;
    ArrayList<String> mobilecolorlist;
    String formattedDate;
    Calendar myCalendar;
    String mobilebrandname,mobilemodelname,mobilecolorname,mobilebrandlistname,mobilemodellistname,mobilecoloelistname,mobileprice,chargertxt,headphonetxt;
    private String finalDate;
    String usernm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_sale);

        uname = (EditText)findViewById(R.id.mobile_sale_name);
        address = (EditText)findViewById(R.id.mobile_sale_add);
        mobileno = (EditText)findViewById(R.id.mobile_sale_mob);
        price = (EditText)findViewById(R.id.inq_sell_price);
        iemino = (EditText)findViewById(R.id.mobile_sale_iemi);
        billdate = (EditText)findViewById(R.id.inq_billdate);
        brandspinner = (Spinner)findViewById(R.id.mobile_sale_brand);
        modelspinner = (Spinner)findViewById(R.id.mobile_sale_model);
        colorspinner = (Spinner)findViewById(R.id.mobile_sale_color);
        charger = (CheckBox)findViewById(R.id.charger_check);
        headphone = (CheckBox)findViewById(R.id.headphone_check);
        salesubmit = (Button)findViewById(R.id.finalpayment);
        sellername = (EditText)findViewById(R.id.mobile_sale_sellername);
        modelspinner.setOnItemSelectedListener(this);
        Intent i = getIntent();
        usernm = i.getStringExtra("id");
        chargertxt = "No";
        headphonetxt = "No";
        charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (charger.isChecked())
                {
                    chargertxt = "Yes";
                }else {
                    chargertxt = "No";
                }
            }
        });
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (headphone.isChecked())
                {
                    headphonetxt = "Yes";
                }else {
                    headphonetxt = "No";
                }
            }
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        formattedDate = df.format(c);
        myCalendar = Calendar.getInstance();

        mobilebrandlist= new ArrayList<>();
        if (mobilebrandlist != null && mobilebrandlist.size()!=0) {
            mobilebrandlist.add("Select Brand:");
            ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(MobileSale.this, android.R.layout.simple_spinner_item, mobilebrandlist);
            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brandspinner.setAdapter(spinnerAdapter1);
        }

        mobilemodellist = new ArrayList<>();
        if (mobilemodellist != null && mobilemodellist.size()!=0) {
            mobilemodellist.add("Select Model:");
            ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(MobileSale.this, android.R.layout.simple_spinner_item, mobilemodellist);
            spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brandspinner.setAdapter(spinnerAdapter2);
        }

        mobilecolorlist = new ArrayList<>();
        if (mobilecolorlist != null && mobilecolorlist.size()!=0) {
            mobilecolorlist.add("Select Color:");
            ArrayAdapter<String> spinnerAdapter3 = new ArrayAdapter<>(MobileSale.this, android.R.layout.simple_spinner_item, mobilecolorlist);
            spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorspinner.setAdapter(spinnerAdapter3);
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        billdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MobileSale.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        colorspinner.setEnabled(false);
        colorspinner.setClickable(false);
        modelspinner.setEnabled(false);
        modelspinner.setClickable(false);

        database = FirebaseDatabase.getInstance();
        company = database.getReference("MobilePurchase");
        company.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mobilebrandlist.clear();
                mobilemodellist.clear();
                mobilecolorlist.clear();
                price.setText("");

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    mobilebrandlistname = dataSnapshot1.child("companyName").getValue().toString();
                    mobilebrandlist.add(mobilebrandlistname);

                }
                Set<String> unique = new HashSet<>();
                unique.addAll(mobilebrandlist);
                mobilebrandlist.clear();
                mobilebrandlist.addAll(unique);
                Collections.sort(mobilebrandlist);
                mobilebrandlist.add(0,"Select Brand:");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MobileSale.this,android.R.layout.simple_spinner_item, mobilebrandlist);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandspinner.setAdapter(spinnerAdapter);
                brandspinner.setSelection(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        brandspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mobilemodelname = parent.getItemAtPosition(position).toString();
                //Toast.makeText(MobileSale.this, mobilemodellistname, Toast.LENGTH_LONG).show();
                company = database.getReference("MobilePurchase");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mobilemodellist.clear();
                        mobilecolorlist.clear();
                        price.setText("");

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            String check2 = dataSnapshot1.child("companyName").getValue().toString();
                            if (check2.equals(mobilemodelname))
                            {
                                mobilemodellistname = dataSnapshot1.child("modelName").getValue().toString();
                                mobilemodellist.add(mobilemodellistname);
                            }
                        }
                        Set<String> unique = new HashSet<>();
                        unique.addAll(mobilemodellist);
                        mobilemodellist.clear();
                        mobilemodellist.addAll(unique);
                        Collections.sort(mobilemodellist);
                        mobilemodellist.add(0,"Select Model:");
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MobileSale.this,android.R.layout.simple_spinner_item, mobilemodellist);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        modelspinner.setAdapter(spinnerAdapter);
                        modelspinner.setEnabled(true);
                        modelspinner.setClickable(true);

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
        colorspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mobilecolorname = adapterView.getItemAtPosition(i).toString();
                company = database.getReference("MobilePurchase");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            String check = dataSnapshot1.child("modelName").getValue().toString();
                            String checkk = dataSnapshot1.child("color").getValue().toString();
                            if (check.equals(mobilebrandname) && checkk.equals(mobilecolorname)) {
                                mobileprice = dataSnapshot1.child("mobilePrice").getValue().toString();
                                price.setText(mobileprice);
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

        salesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uname.getText().toString().isEmpty())
                {
                    uname.setError("Enter Buyer Name ");
                }else if (address.getText().toString().isEmpty())
                {
                    address.setError("Enter Buyer Address ");
                }else if (mobileno.getText().toString().isEmpty())
                {
                    mobileno.setError("Enter Mobile Number ");
                }else if (mobilemodelname.equals("Select Brand:"))
                {
                    Toast.makeText(MobileSale.this, "Select Brand ", Toast.LENGTH_LONG).show();
                }else if (mobilebrandname.equals("Select Model:"))
                {
                    Toast.makeText(MobileSale.this, "Select Model ", Toast.LENGTH_LONG).show();
                }else if (mobilecolorname.equals("Select Color:"))
                {
                    Toast.makeText(MobileSale.this, "Select Color ", Toast.LENGTH_LONG).show();

                }else if (iemino.getText().toString().isEmpty())
                {
                    iemino.setError("Enter IEMI Number ");
                }else if (billdate.getText().toString().isEmpty())
                {
                    billdate.setError("Select Bill Date ");
                }
                else {
                    database = FirebaseDatabase.getInstance();
                    company = database.getReference("MobileSale");
                    sellerModel = new SellerModel(address.getText().toString(),
                            uname.getText().toString(),
                            mobilecolorname,mobilemodelname,
                            mobileno.getText().toString(),
                            price.getText().toString(),
                            mobilebrandname,
                            iemino.getText().toString(),finalDate,chargertxt,headphonetxt,
                            sellername.getText().toString(),null);
                    if (sellerModel!=null)
                    {
                        company.push().setValue(sellerModel);
                        Toast.makeText(MobileSale.this, "Data Entry Successfully !!!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MobileSale.this,MainPage.class);
                        i.putExtra("userid",usernm);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mobilebrandname = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, mobilebrandname, Toast.LENGTH_LONG).show();
        if (mobilebrandname.equals("Select Brand: "))
        {
            Toast.makeText(this, "Please Select Brand !!", Toast.LENGTH_SHORT).show();
        }else {
            company = database.getReference("MobilePurchase");
            company.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mobilecolorlist.clear();

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        String check = dataSnapshot1.child("modelName").getValue().toString();
                        if (check.equals(mobilebrandname)) {
                            mobilecoloelistname = dataSnapshot1.child("color").getValue().toString();
                            mobilecolorlist.add(mobilecoloelistname);

                        }
                    }
                    Set<String> unique = new HashSet<>();
                    unique.addAll(mobilecolorlist);
                    mobilecolorlist.clear();
                    mobilecolorlist.addAll(unique);
                    Collections.sort(mobilecolorlist);
                    mobilecolorlist.add(0,"Select Color:");
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MobileSale.this,android.R.layout.simple_spinner_item, mobilecolorlist);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    colorspinner.setAdapter(spinnerAdapter);
                    colorspinner.setEnabled(true);
                    colorspinner.setClickable(true);
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
    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        billdate.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        finalDate = sdf1.format(myCalendar.getTime());
    }
}
