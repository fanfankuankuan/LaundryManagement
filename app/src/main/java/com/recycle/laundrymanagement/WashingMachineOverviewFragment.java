package com.recycle.laundrymanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WashingMachineOverviewFragment extends Fragment {
    private OverviewAdapter mOverviewAdapter;
    private RecyclerView mRecyclerView;
    private DatabaseReference mdatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mdatabase.child("status").child("1").setValue("available");
        mdatabase.child("status").child("2").setValue("unavailable");
        mdatabase.child("status").child("3").setValue("finish");
        mdatabase.child("status").child("4").setValue("overtime");


        View rootView = inflater.inflate(R.layout.washing_machine_overview_layout, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        View mView = rootView.findViewById(R.id.washing_machine);



        final List<WashingMachine> listItems = new ArrayList<> ();
        for (int i = 0; i < 15; i++) {
            WashingMachine wm = new WashingMachine();
            wm.setDrawable_label("available");
            wm.setWashing_machine_id(i);
            //wm.setUser_email(Config.useremail);
            wm.setStatus_id(1);
            wm.setStart_time(0);
            wm.setEnd_time(0);
            wm.setDrawable_id(R.drawable.washing_machine);
            mdatabase.child("washing_machine").child(String.valueOf(wm.getWashing_machine_id())).setValue(wm);
            listItems.add(wm);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mOverviewAdapter = new OverviewAdapter(getContext(), listItems, WashingMachineOverviewFragment.this);
        mRecyclerView.setAdapter(mOverviewAdapter);


//        mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mdatabase.child("washing_machine").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        listItems.get(1).setDrawable_label("kkk");
//                        if() {
//
//                        }
//                        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//                        mOverviewAdapter = new OverviewAdapter(getContext(), listItems);
//                        mRecyclerView.setAdapter(mOverviewAdapter);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild(username)) {
//                            Toast.makeText(getContext(), "username is already registered, please change one",
//                                    Toast.LENGTH_LONG).show();
//                        } else if (!username.isEmpty() && !password.isEmpty()) {
//                            final User user = new User();
//                            user.setUser_account(username);
//                            user.setUser_password(Utils.md5Encryption(password));
//                            user.setUser_timestamp(System.currentTimeMillis());
//                            database.child("user").child(user.getUser_account()).setValue(user);
//                            Toast.makeText(getContext(), "user has successfully registered",
//                                    Toast.LENGTH_LONG).show();
//                            goToLogin();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });



//        final List<WashingMachine> listItems = new ArrayList<WashingMachine> () {
//            {
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//                add(new WashingMachine("Ready for usage!", R.drawable.washing_machine));
//            }
//        };





//        mdatabase.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                WashingMachine wm = mutableData.getValue(WashingMachine.class);
//                if (wm == null) {
//                    return Transaction.success(mutableData);
//                }
//                wm.setDrawable_id(3);
//                wm.setDrawable_label("Ready for Usage");
//                wm.setUser_email("565600@gmail.com");
//                wm.setStatus_id(1);
//                wm.setStart_time(System.currentTimeMillis());
//                wm.setEnd_time(System.currentTimeMillis() + 15000.0);
//                // Set value and report transaction success
//                mutableData.child("washing_machine").child(String.valueOf(wm.getDrawable_id())).setValue(wm);
//                return Transaction.success(mutableData);
//            }

//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b,
//                                   DataSnapshot dataSnapshot) {
//                // Transaction completed
//                Log.d("wmfragment", "postTransaction:onComplete:" + databaseError);
//            }
//        });

        return rootView;
    }
}
