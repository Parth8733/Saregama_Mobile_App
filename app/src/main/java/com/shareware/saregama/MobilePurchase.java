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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shareware.saregama.Model.SellerModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MobilePurchase extends AppCompatActivity {

    EditText uname,address,mobileno,price,iemino,billdate,sellername,modelspinner,colorspinner;
    Spinner brandspinner;
    CheckBox charger,headphone;
    Button salesubmit;
    Calendar myCalendar;
    SellerModel sellerModel;
    FirebaseDatabase database;
    DatabaseReference company;
    ArrayList<String> mobilebrandlist;
    String formattedDate;
    String usernm,mobilemodelname,mobilebrandlistname,mobilemodellistname,mobilecoloelistname,chargertxt,headphonetxt;
    private String finalDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_purchase);

        uname = (EditText)findViewById(R.id.mobile_purchase_name);
        address = (EditText)findViewById(R.id.mobile_purchase_add);
        mobileno = (EditText)findViewById(R.id.mobile_purchase_mob);
        price = (EditText)findViewById(R.id.mobile_purchase_price);
        iemino = (EditText)findViewById(R.id.mobile_purchase_iemi);
        billdate = (EditText)findViewById(R.id.mobile_purchase_billdate);
        brandspinner = (Spinner)findViewById(R.id.mobile_purchase_brand);
        modelspinner = (EditText) findViewById(R.id.mobile_purchase_model);
        colorspinner = (EditText) findViewById(R.id.mobile_purchase_color);
        charger = (CheckBox)findViewById(R.id.charger_check);
        headphone = (CheckBox)findViewById(R.id.headphone_check);
        salesubmit = (Button)findViewById(R.id.finalpayment);
        sellername = (EditText)findViewById(R.id.mobile_purchase_sellername);
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
                }else
                {
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
            ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(MobilePurchase.this, android.R.layout.simple_spinner_item, mobilebrandlist);
            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brandspinner.setAdapter(spinnerAdapter1);
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
                new DatePickerDialog(MobilePurchase.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        database = FirebaseDatabase.getInstance();
        company = database.getReference("Brand");
        company.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mobilebrandlist.clear();
                price.setText("");
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    mobilebrandlistname = dataSnapshot1.child("name").getValue().toString();
                    mobilebrandlist.add(mobilebrandlistname);

                }
                Collections.sort(mobilebrandlist);
                mobilebrandlist.add(0,"Select Brand:");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MobilePurchase.this,android.R.layout.simple_spinner_item, mobilebrandlist);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandspinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        brandspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mobilemodelname = parent.getItemAtPosition(position).toString();
                company = database.getReference("Model");
                company.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        price.setText("");
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            String check2 = dataSnapshot1.child("brand").getValue().toString();
                            if (check2.equals(mobilemodelname))
                            {
                                mobilemodellistname = dataSnapshot1.child("name").getValue().toString();
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
                }else if (mobilebrandlistname.equals("Select Brand:"))
                {
                    Toast.makeText(MobilePurchase.this, "Select Brand ", Toast.LENGTH_LONG).show();
                }else if (iemino.getText().toString().isEmpty())
                {
                    iemino.setError("Enter IEMI Number ");
                }else if (billdate.getText().toString().isEmpty())
                {
                    billdate.setError("Select Bill Date ");
                }else if (modelspinner.getText().toString().isEmpty())
                {
                    modelspinner.setError("Enter Model Name :");
                }else if (colorspinner.getText().toString().isEmpty())
                {
                    colorspinner.setError("Enter Color :");
                }
                else {
                    database = FirebaseDatabase.getInstance();
                    company = database.getReference("MobilePurchase");
                    Date currdate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String currentdate = dateFormat.format(currdate);
                    sellerModel = new SellerModel(address.getText().toString(),
                            uname.getText().toString(),
                            colorspinner.getText().toString(),
                            mobilemodelname,
                            mobileno.getText().toString(),
                            price.getText().toString(),
                            modelspinner.getText().toString(),
                            iemino.getText().toString(),finalDate,chargertxt,headphonetxt,
                            sellername.getText().toString(),currentdate);
                    if (sellerModel!=null)
                    {
                        company.push().setValue(sellerModel);
                        Toast.makeText(MobilePurchase.this, "Data Entry Successfully !!!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MobilePurchase.this,MainPage.class);
                        i.putExtra("userid",usernm);
                        startActivity(i);
                    }
                }
            }
        });
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