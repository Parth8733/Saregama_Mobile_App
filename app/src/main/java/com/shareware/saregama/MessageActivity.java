package com.shareware.saregama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.Model.SecondMobPurchase;
import com.shareware.saregama.Model.SellerModel;
import com.shareware.saregama.Model.User;
import com.shareware.saregama.common.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageActivity extends AppCompatActivity {
    CheckBox inquiryCheckBox,sellerCheckBox,secinquiryCheckBox,secsellerCheckBox,secpurchaseCheckBox,upcomingCheckBox,dataCheckBox;
    EditText messageEtd;
    ImageView sendButton;
    RadioGroup LangradioGrp;
    RadioButton engradio,gujradio;

    ArrayList<String> Allnumbers;
    ArrayList<String> inquirynumbers,datanumbers;
    ArrayList<String> sellernumbers,secInquiryNumbers,secSellerNumbers,secPuchaseNumbers,upComingNumbers;

    FirebaseDatabase database;
    DatabaseReference inquiryViewList,sellerViewList,secInquiryViewList,secPurchaseViewList,secSellerViewList,upInquiryViewList,dataViewList;
    private InquiryModel dataInquiry;
    private SellerModel dataSeller;
    private SecondMobPurchase dataSecondPurchase;
    User dataentry;



    String LOAD_URL="https://fkmppdeveloper.000webhostapp.com/msg.php";
    String strOfInts="",msgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        LangradioGrp = (RadioGroup)findViewById(R.id.langRadiogrp);
        engradio = (RadioButton)findViewById(R.id.langEng);
        gujradio = (RadioButton)findViewById(R.id.langGuj);
        database = FirebaseDatabase.getInstance();
        inquiryViewList = database.getReference("Inquiry");
        sellerViewList = database.getReference("Seller");
        secInquiryViewList = database.getReference("MobileInquiry");
        secSellerViewList =database.getReference("MobileSale");
        secPurchaseViewList =database.getReference("MobilePurchase");
        upInquiryViewList = database.getReference("UpMobileInquiry");
        dataViewList = database.getReference("DataEntry");


        Allnumbers = new ArrayList<>();
        inquirynumbers = new ArrayList<>();
        sellernumbers = new ArrayList<>();
        secInquiryNumbers = new ArrayList<>();
        secSellerNumbers = new ArrayList<>();
        secPuchaseNumbers = new ArrayList<>();
        upComingNumbers = new ArrayList<>();
        datanumbers = new ArrayList<>();

        inquiryCheckBox =findViewById(R.id.inquirycheckbox);
        sellerCheckBox = findViewById(R.id.sellercheckbox);
        secinquiryCheckBox = findViewById(R.id.secondInquirycheckbox);
        secsellerCheckBox = findViewById(R.id.secondsellercheckbox);
        secpurchaseCheckBox = findViewById(R.id.secondpurchasecheckbox);
        upcomingCheckBox = findViewById(R.id.upcomingcheckbox);
        dataCheckBox = findViewById(R.id.olddatacheckbox);
        messageEtd = findViewById(R.id.messageText);
        sendButton = findViewById(R.id.messageSendBtn);
        LangradioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.langEng :
                        msgType = "english";
                        break;
                    case R.id.langGuj :
                        msgType = "gujarati";
                        break;
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //  Toast.makeText(MessageActivity.this,strOfInts , Toast.LENGTH_SHORT).show();
                if(messageEtd.getText().toString().isEmpty()){
                    messageEtd.setError("Type Your Message First!!!");
                }else if (msgType=="")
                {
                    Toast.makeText(MessageActivity.this, "Please Select any Language !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Allnumbers.addAll(inquirynumbers);
                    Allnumbers.addAll(sellernumbers);
                    Allnumbers.addAll(secInquiryNumbers);
                    Allnumbers.addAll(secPuchaseNumbers);
                    Allnumbers.addAll(secSellerNumbers);
                    Allnumbers.addAll(upComingNumbers);
                    Allnumbers.addAll(datanumbers);
                    Set<String> unique = new HashSet<>();
                    unique.addAll(Allnumbers);
                    Allnumbers.clear();
                    Allnumbers.addAll(unique);
                    String[] namesArr = (String[])Allnumbers.toArray(new String[Allnumbers.size()]);
                    strOfInts = Arrays.toString(namesArr).replaceAll("\\[|\\]||\\s", "");

                    Toast.makeText(MessageActivity.this, "Total Numbers "+String.valueOf(namesArr.length), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MessageActivity.this, msgType, Toast.LENGTH_SHORT).show();
                    sendDataVolley(Allnumbers);
                }
            }
        });

        dataCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    dataGetNumbers();
                }else {
                    Toast.makeText(MessageActivity.this, String.valueOf(datanumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    datanumbers.clear();
                }
            }
        });

        inquiryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                {
                    inquiryGetNumbers();

                }
                else {
                    Toast.makeText(MessageActivity.this, String.valueOf(inquirynumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    inquirynumbers.clear();

                }

            }
        });
        sellerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    sellerGetNumbers();
                }
                else {
                    Toast.makeText(MessageActivity.this, String.valueOf(sellernumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    sellernumbers.clear();
                }
            }
        });
        secinquiryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    secInquiryGetNumbers();
                }
                else {
                    Toast.makeText(MessageActivity.this, String.valueOf(secInquiryNumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    secInquiryNumbers.clear();
                }
            }
        });
        secsellerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    secSellerGetNumbers();
                }
                else {
                    Toast.makeText(MessageActivity.this, String.valueOf(secSellerNumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    secSellerNumbers.clear();
                }
            }
        });
        secpurchaseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    secPurchaseGetNumbers();
                }
                else {
                    Toast.makeText(MessageActivity.this, String.valueOf(secPuchaseNumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    secPuchaseNumbers.clear();
                }
            }
        });
        upcomingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    upInquiryGetNumbers();
                }
                else {
                    Toast.makeText(MessageActivity.this, String.valueOf(upComingNumbers.size())+" Numbers Removed", Toast.LENGTH_SHORT).show();
                    upComingNumbers.clear();
                }
            }
        });
    }

    private void dataGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        dataViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot seller : dataSnapshot.getChildren()){
                    dataentry = seller.getValue(User.class);
                    datanumbers.add(dataentry.getNumber());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(datanumbers);
                datanumbers.clear();
                datanumbers.addAll(unique);
                Toast.makeText(MessageActivity.this, String.valueOf(datanumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void secPurchaseGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        secPurchaseViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot seller : dataSnapshot.getChildren()){
                    dataSecondPurchase = seller.getValue(SecondMobPurchase.class);
                    secPuchaseNumbers.add(dataSecondPurchase.getMobileNo());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(secPuchaseNumbers);
                secPuchaseNumbers.clear();
                secPuchaseNumbers.addAll(unique);
                Toast.makeText(MessageActivity.this, String.valueOf(secPuchaseNumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void upInquiryGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        upInquiryViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot seller : dataSnapshot.getChildren()){
                    dataInquiry = seller.getValue(InquiryModel.class);
                    upComingNumbers.add(dataInquiry.getMobileNo());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(upComingNumbers);
                upComingNumbers.clear();
                upComingNumbers.addAll(unique);
                Toast.makeText(MessageActivity.this, String.valueOf(upComingNumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void secSellerGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        secSellerViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot seller : dataSnapshot.getChildren()){
                    dataSecondPurchase = seller.getValue(SecondMobPurchase.class);
                    secSellerNumbers.add(dataSecondPurchase.getMobileNo());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(secSellerNumbers);
                secSellerNumbers.clear();
                secSellerNumbers.addAll(unique);
                Toast.makeText(MessageActivity.this, String.valueOf(secSellerNumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void secInquiryGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        secInquiryViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot seller : dataSnapshot.getChildren()){
                    dataInquiry = seller.getValue(InquiryModel.class);
                    secInquiryNumbers.add(dataInquiry.getMobileNo());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(secInquiryNumbers);
                secInquiryNumbers.clear();
                secInquiryNumbers.addAll(unique);
                Toast.makeText(MessageActivity.this, String.valueOf(secInquiryNumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendDataVolley(ArrayList<String> allnumbers) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,LOAD_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject obj= new JSONObject(response);
                        if(obj.getInt("success")==1) {
                            Intent intent = new Intent(MessageActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(MessageActivity.this,"Successfully Sent", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(MessageActivity.this,"Error in sending message", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MessageActivity.this, "Failed" +e, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }


                }
            },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MessageActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
            )
            {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("numbers",strOfInts);
                    params.put("message",messageEtd.getText().toString());
                    params.put("type",msgType);
                    return params;
                }
            };

            RequestQueue request = Volley.newRequestQueue(this);
            request.add(stringRequest);



}
    private void sellerGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sellerViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot seller : dataSnapshot.getChildren()){
                    dataSeller = seller.getValue(SellerModel.class);
                    sellernumbers.add(dataSeller.getMobileNo());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(sellernumbers);
                sellernumbers.clear();
                sellernumbers.addAll(unique);
                Toast.makeText(MessageActivity.this, String.valueOf(sellernumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inquiryGetNumbers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        inquiryViewList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot inquiryData : dataSnapshot.getChildren()) {
                    dataInquiry = inquiryData.getValue(InquiryModel.class);
                    inquirynumbers.add(dataInquiry.getMobileNo());
                }
                Set<String> unique = new HashSet<>();
                unique.addAll(inquirynumbers);
                inquirynumbers.clear();
                inquirynumbers.addAll(unique);
               // Toast.makeText(MessageActivity.this, String.valueOf(inquirynumbers.toString()), Toast.LENGTH_LONG).show();
                Toast.makeText(MessageActivity.this, String.valueOf(inquirynumbers.size())+" Numbers Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
