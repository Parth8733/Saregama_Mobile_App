package com.shareware.saregama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.shareware.saregama.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {

    EditText userid,password;
    Button go;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    String userUid,username,userpassword;
    FirebaseDatabase database;
    DatabaseReference login;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        userid = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.phone_number_edt);
        go = (Button)findViewById(R.id.start_auth_button);
        database = FirebaseDatabase.getInstance();
        login = database.getReference("User");
        preferences=getSharedPreferences("uuid",MODE_PRIVATE);
        editor=preferences.edit();
        userUid = preferences.getString("id","");

        if (!userUid.isEmpty()){
            pd = new ProgressDialog(LoginPage.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();

            login.orderByChild("name").equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()==null){
                        pd.dismiss();
                        Toast.makeText(LoginPage.this, "User Not find", Toast.LENGTH_SHORT).show();
                        userid.setError("Enter Correct User Name");
                    }
                    else {
                        pd.dismiss();
                    Intent i = new Intent(LoginPage.this,MainPage.class);
                    i.putExtra("userid",userUid);
                    startActivity(i);

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(LoginPage.this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                logindemo();

            }
        });
    }

    private void logindemo()
    {
        login.orderByChild("name").equalTo(userid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    pd.dismiss();
                    Toast.makeText(LoginPage.this, "User Not find", Toast.LENGTH_SHORT).show();
                    userid.setError("Enter Correct User Name");
                }
                else {
                    pd.dismiss();
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        User user = d.getValue(User.class);
                        if (!(password.getText().toString().equals(user.getPassword()))){
                            password.setError("Enter Correct Password");
                        }
                        else {
                            editor.putString("id",user.getName()).commit();
                            editor.putString("password",user.getPassword()).commit();
                            Intent i = new Intent(LoginPage.this,MainPage.class);
                            i.putExtra("userid",user.getName());
                            startActivity(i);

                         //   Toast.makeText(LoginPage.this, String.valueOf(user.getPassword()), Toast.LENGTH_LONG).show();
                        }
                    }

                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
