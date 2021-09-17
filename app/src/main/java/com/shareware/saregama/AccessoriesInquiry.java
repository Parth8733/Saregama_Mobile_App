package com.shareware.saregama;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shareware.saregama.Model.SellerModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccessoriesInquiry extends AppCompatActivity {

    EditText buyername,bmobileno,brequirement,sellname;
    Button inquirysubmit;
    SellerModel sellerModel;
    FirebaseDatabase database;
    DatabaseReference company;
    String currentdate,usernm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_inquiry);

        buyername = (EditText)findViewById(R.id.acc_inq_name);
        bmobileno = (EditText)findViewById(R.id.acc_inq_mob);
        brequirement = (EditText)findViewById(R.id.acc_inq_req);
        sellname = (EditText)findViewById(R.id.acc_inq_sell_name);
        inquirysubmit = (Button)findViewById(R.id.finalpayment);

        Intent i = getIntent();
        usernm = i.getStringExtra("id");
        inquirysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyername.getText().toString().isEmpty())
                {
                    buyername.setError("Enter Buyer Name :");
                    return;
                }else if (bmobileno.getText().toString().isEmpty())
                {
                    bmobileno.setError("Enter Mobile Number :");
                    return;
                }else if (brequirement.getText().toString().isEmpty())
                {
                    brequirement.setError("Enter Requirements :");
                    return;
                }else if (sellname.getText().toString().isEmpty())
                {
                    sellname.setError("Enter Seller Name :");
                    return;
                }
                else {
                    Date currdate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    currentdate = dateFormat.format(currdate);
                    database = FirebaseDatabase.getInstance();
                    company = database.getReference("AccessoriesInquiry");
                    sellerModel = new SellerModel(buyername.getText().toString(),
                            bmobileno.getText().toString(),
                            currentdate,
                            brequirement.getText().toString(),
                            sellname.getText().toString());
                    if (sellerModel!=null)
                    {
                        company.push().setValue(sellerModel);
                        Toast.makeText(AccessoriesInquiry.this, "Data Entry Successfully !!!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AccessoriesInquiry.this,MainPage.class);
                        i.putExtra("userid",usernm);
                        startActivity(i);
                    }
                }
            }
        });
    }
}
