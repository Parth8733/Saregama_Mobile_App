package com.shareware.saregama;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.shareware.saregama.common.Common;

public class MainPage extends AppCompatActivity {

    ImageView sell,inquiry,secondsell,secondpurc,accinquiry,repairiquiry,secondinq,upcomingproud,olddata;
    Button adminbtn;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        sell = (ImageView)findViewById(R.id.sell_img);
        inquiry = (ImageView)findViewById(R.id.inquiry_img);
        secondsell = (ImageView)findViewById(R.id.secondmobile_img);
        secondpurc = (ImageView)findViewById(R.id.secondmobile2_img);
        accinquiry = (ImageView)findViewById(R.id.accessories_inquiry_img);
        repairiquiry = (ImageView)findViewById(R.id.repair_inquiry_img);
        secondinq = (ImageView)findViewById(R.id.secondmobileinq_img);
        upcomingproud = (ImageView)findViewById(R.id.upcoming_img);
        olddata = findViewById(R.id.data_img);
        Intent i = getIntent();
        username = i.getStringExtra("userid");
        adminbtn = (Button)findViewById(R.id.adminbtn);
        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals(Common.ADMIN)) {
                    Intent intent = new Intent(MainPage.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainPage.this, particular_admin.class);
                    intent.putExtra("id",username);
                    startActivity(intent);
                }
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this,Seller.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });

        olddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this,DataEntry.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });

        inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this,Inquiry.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
        secondsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainPage.this,MobileSale.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
        secondpurc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainPage.this,MobilePurchase.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
        accinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainPage.this,AccessoriesInquiry.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
        repairiquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this,RepairInquiry.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
        secondinq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this,SecondMobileInquiry.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
        upcomingproud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this,UpcomingMobile.class);
                i.putExtra("id",username);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
