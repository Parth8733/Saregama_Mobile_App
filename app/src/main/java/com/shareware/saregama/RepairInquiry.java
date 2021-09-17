package com.shareware.saregama;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.Model.SellerModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RepairInquiry extends AppCompatActivity {

    EditText buyname1,mobnum1,slipnum,modedl,engineer,problem1,receiveditem,sellername,currdate;
    Button submit1;
    FirebaseDatabase database;
    DatabaseReference company;
    String currentdate1;
    SellerModel sellerModel;
    String formattedDate;
    Calendar myCalendar;
    private String finalDate;
    String usernm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_inquiry);

        buyname1 = (EditText)findViewById(R.id.repair_inq_name);
        slipnum = (EditText)findViewById(R.id.repair_inq_slipno);
        mobnum1 = (EditText)findViewById(R.id.repair_inq_mob);
        modedl = (EditText)findViewById(R.id.repair_inq_model);
        engineer = (EditText)findViewById(R.id.repair_inq_eng);
        problem1 = (EditText)findViewById(R.id.repair_inq_prbl);
        receiveditem = (EditText)findViewById(R.id.repair_inq_item);
        sellername = (EditText)findViewById(R.id.repair_inq_sell_name);
        currdate = (EditText)findViewById(R.id.repair_inq_date);
        submit1 = (Button)findViewById(R.id.submit);

        Intent i = getIntent();
        usernm = i.getStringExtra("id");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        formattedDate = df.format(c);
        myCalendar = Calendar.getInstance();
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

        currdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RepairInquiry.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyname1.getText().toString().isEmpty())
                {
                    buyname1.setError("Enter Buyer Name..");
                    return;
                }else if (slipnum.getText().toString().isEmpty())
                {
                    slipnum.setError("Enter Slip Number....");
                    return;
                }else if (mobnum1.getText().toString().isEmpty())
                {
                    mobnum1.setError("Enter Mobile Number..");
                    return;
                }else if (modedl.getText().toString().isEmpty())
                {
                    modedl.setError("Enter Model....");
                    return;
                }else if (engineer.getText().toString().isEmpty())
                {
                    engineer.setError("Enter Engineer Name..");
                    return;
                }else if (problem1.getText().toString().isEmpty())
                {
                    problem1.setError("Enter Problem....");
                    return;
                }else if (receiveditem.getText().toString().isEmpty())
                {
                    receiveditem.setError("Enter ReceiveItem..");
                    return;
                }else if (sellername.getText().toString().isEmpty())
                {
                    sellername.setError("Enter Seller Name..");
                    return;
                }else if (currdate.getText().toString().isEmpty())
                {
                    currdate.setError("Select Date..");
                    return;
                }
                else {
                    database = FirebaseDatabase.getInstance();
                    company = database.getReference("RepairInquiry");
                    sellerModel = new SellerModel(slipnum.getText().toString(),
                            buyname1.getText().toString(),
                            mobnum1.getText().toString(),
                            modedl.getText().toString(),
                            engineer.getText().toString(),
                            problem1.getText().toString(),
                            receiveditem.getText().toString(),
                            sellername.getText().toString(),
                            finalDate,null);
                    if (sellerModel!=null)
                    {
                        company.push().setValue(sellerModel);
                        Toast.makeText(RepairInquiry.this, "Data Entry Successfully !!!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RepairInquiry.this,MainPage.class);
                        i.putExtra("userid",usernm);
                        startActivity(i);
                    }
                    return;
                }
            }
        });
    }
    private void updateLabel() {

        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        currdate.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        finalDate = sdf1.format(myCalendar.getTime());
    }
}
