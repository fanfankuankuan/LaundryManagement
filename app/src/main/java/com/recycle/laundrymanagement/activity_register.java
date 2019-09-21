package com.recycle.laundrymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class activity_register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText EmailRegister;
    private EditText PasswordRegister;
    private EditText PhoneRegister;
    private Button SubmitRegister;

    protected DatabaseReference database;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailRegister = (EditText) findViewById(R.id.emailregister);
        PasswordRegister = (EditText) findViewById(R.id.passwordregister);
        PhoneRegister = (EditText) findViewById(R.id.phoneregister);
        SubmitRegister = (Button) findViewById(R.id.register_submit);

        database = FirebaseDatabase.getInstance().getReference();

        //Submit Button Listener
        SubmitRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String useremail = EmailRegister.getText().toString().replace(".",",");
                final String userpassword = PasswordRegister.getText().toString();
                final String userphonenum = PhoneRegister.getText().toString();
                final Boolean useradmin = false;



                final Context context = getApplicationContext();
                //Log.v("EditText",EmailRegister.getText().toString());
                //Log.v("EditText",PasswordRegister.getText().toString());
                //Log.v("EditText",PhoneRegister.getText().toString());

                database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        if (dataSnapshot.hasChild(useremail)){
                            //Log.v("EditText",EmailRegister.getText().toString());
                            Toast.makeText(context,"This email has already existed, please change one", Toast.LENGTH_LONG).show();
                        }
                        else if(!useremail.contains("@")){
                            Toast.makeText(context,"This email is invalid, please input a new one", Toast.LENGTH_LONG).show();
                        }
                        else if(!userphonenum.matches("^\\d+$")){
                            Toast.makeText(context,"This phone number is invalid,please change one",Toast.LENGTH_LONG).show();
                        }
                        else if(dataSnapshot.hasChild(userphonenum)){
                            Toast.makeText(context,"This phone number has already existed, please change one", Toast.LENGTH_LONG).show();
                        }
                        else if (!useremail.isEmpty() && !userpassword.isEmpty() && !userphonenum.isEmpty()){
                            final User user = new User();

                            user.setUser_email(useremail);
                            user.setUser_phonenum(userphonenum);
                            user.setUser_password(Utils.md5Encryption(userpassword));
                            user.setUser_admin(useradmin);

                            database.child("user").child(user.getUser_email()).setValue(user);
                            //Log.v("EditText",EmailRegister.getText().toString());
                            Toast.makeText(context,"user has successfully registered. Wait for Transfer",Toast.LENGTH_LONG).show();


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Config.useremail = useremail;
                                    startActivity(new Intent(activity_register.this,WashingMachineOverview.class));
                                    overridePendingTransition(R.anim.fadein, R.anim.fadein);
                                }
                            }, 3000);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


                //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Email, Phone number and Password");
                //myRef.setValue(EmailRegister.getText().toString() +  PasswordRegister.getText().toString() + PhoneRegister.getText().toString());
            }
        });
    }

    private void goToLogin(){


    }

    @Override
    public void onBackPressed(){
        //Disable going back to the MainActivity
        super.onBackPressed();
        Intent BackLogin = new Intent(activity_register.this,activity_login.class);
        startActivity(BackLogin);
        finish();

    }
}
