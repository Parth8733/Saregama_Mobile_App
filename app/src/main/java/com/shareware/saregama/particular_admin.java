package com.shareware.saregama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class particular_admin extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CardView inqcv,sellcv,secpurcv,secinqcv,secsellcv,accinqcv,repinqcv,upinqcv;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_admin);

        preferences=getSharedPreferences("uuid",MODE_PRIVATE);
        editor=preferences.edit();
        Intent intent = getIntent();
        username = intent.getStringExtra("id");
        inqcv = findViewById(R.id.inquirycv);
        sellcv = findViewById(R.id.sellercv);
        secinqcv = findViewById(R.id.secInq);
        secpurcv = findViewById(R.id.secPurchase);
        secsellcv = findViewById(R.id.secSeller);
        accinqcv = findViewById(R.id.accesaryInq);
        repinqcv = findViewById(R.id.repairInq);
        upinqcv = findViewById(R.id.upCommingInq);

        inqcv.setOnClickListener(this);
        sellcv.setOnClickListener(this);
        secpurcv.setOnClickListener(this);
        secsellcv.setOnClickListener(this);
        secinqcv.setOnClickListener(this);
        accinqcv.setOnClickListener(this);
        repinqcv.setOnClickListener(this);
        upinqcv.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(particular_admin.this,MainPage.class);
        i.putExtra("userid",username);
        startActivity(i);
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
            Intent intent = new Intent(particular_admin.this, LoginPage.class);
            startActivity(intent);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.inquirycv :
                Intent i = new Intent(particular_admin.this,InquiryViewForm.class);
                i.putExtra("id",username);
                startActivity(i);
                break;
            case R.id.sellercv :
                Intent j = new Intent(particular_admin.this,ViewSellerForm.class);
                j.putExtra("id",username);
                startActivity(j);
                break;
            case R.id.secInq :
                Intent k = new Intent(particular_admin.this,secondInquiryForm.class);
                k.putExtra("id",username);
                startActivity(k);
                break;
            case R.id.secSeller :
                Intent l = new Intent(particular_admin.this,secondMobileSeller.class);
                l.putExtra("id",username);
                startActivity(l);
                break;
            case R.id.secPurchase :
                Intent m = new Intent(particular_admin.this,secondMobilePurchase.class);
                m.putExtra("id",username);
                startActivity(m);
                break;
            case R.id.repairInq :
                Intent n = new Intent(particular_admin.this,Repairing_admin.class);
                n.putExtra("id",username);
                startActivity(n);
                break;
            case R.id.accesaryInq :
                Intent o = new Intent(particular_admin.this,Accessary_admin.class);
                o.putExtra("id",username);
                startActivity(o);
                break;
            case R.id.upCommingInq :
                Intent p = new Intent(particular_admin.this,upComingMobileForm.class);
                p.putExtra("id",username);
                startActivity(p);
                break;
                default:
                    break;
        }
    }
}
