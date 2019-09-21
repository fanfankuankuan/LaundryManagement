package com.recycle.laundrymanagement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class activity_login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    protected DatabaseReference database;

    private EditText EmailLogin;
    private EditText PasswordLogin;
    private Button SubmitButton;
    private TextView RegisterLink;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        EmailLogin = (EditText) findViewById(R.id.emaillogin);
        PasswordLogin = (EditText) findViewById(R.id.passwordlogin);
        SubmitButton = (Button) findViewById(R.id.submit);
        RegisterLink = (TextView) findViewById(R.id.registerlink);

        EmailLogin.setText("");
        PasswordLogin.setText("");

        database = FirebaseDatabase.getInstance().getReference();

        //Submit Button Listener
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String useremail = EmailLogin.getText().toString().replace(".",",");
                final String userpassword = Utils.md5Encryption(PasswordLogin.getText().toString());
                final Boolean useradmin = true;

                final Context context = getApplicationContext();



                database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                        if (dataSnapshot.hasChild(useremail) && (userpassword.equals(dataSnapshot.child(useremail).child("user_password").getValue()))){
                            Config.useremail = useremail;
                            if (useradmin == dataSnapshot.child(useremail).child("user_admin").getValue()){
                                Config.adminFlag = true;
                                FirebaseMessaging.getInstance().subscribeToTopic("admin");
                            }
                            startActivity(new Intent(activity_login.this,WashingMachineOverview.class));
                            overridePendingTransition(R.anim.fadein, R.anim.fadein);
                        }
                        else{
                            Toast.makeText(context, "Wrong Email or Password. Please Try again!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


                //Log.v("EditText",EmailLogin.getText().toString());
                //Log.v("EditText",PasswordLogin.getText().toString());

            }
        });

        //RegisterLink Listener
        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_login.this, activity_register.class));
                //overridePendingTransition(R.anim.fadein, R.anim.fadein);
            }
        });



    }

    @Override
    public void onBackPressed(){
        //Disable going back to the MainActivity
        moveTaskToBack(true);
    }



}
