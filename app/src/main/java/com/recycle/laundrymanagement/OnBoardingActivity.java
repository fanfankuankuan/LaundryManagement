package com.recycle.laundrymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnBoardingActivity extends AppCompatActivity {

    private TextView onboard_email;
    private TextView onboard_phone;
    private TextView onboard_admin;
    protected DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        onboard_email = (TextView) findViewById(R.id.onboardemail);
        onboard_phone = (TextView) findViewById(R.id.onboardphone);
        onboard_admin = (TextView) findViewById(R.id.onboardadmin);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                if (dataSnapshot.hasChild(Config.useremail.replace(".",","))){
                    onboard_email.setText(Config.useremail);
                    onboard_phone.setText(dataSnapshot.child(Config.useremail.replace(".",",")).child("user_phonenum").getValue().toString());
                    if (!Config.adminFlag) {
                        onboard_admin.setText("false");
                    }
                    else{
                        onboard_admin.setText("true");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //final Context context = getApplicationContext();
        //Toast.makeText(context, Config.useremail + Config.adminFlag, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        //Disable going back to the MainActivity
        super.onBackPressed();
        Intent BackLogin = new Intent(OnBoardingActivity.this,activity_login.class);
        startActivity(BackLogin);
        finish();

    }

}
