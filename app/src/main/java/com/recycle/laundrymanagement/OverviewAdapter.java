package com.recycle.laundrymanagement;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WashingMachine> mMachines;
    private LayoutInflater mInflater;
    private WashingMachineOverviewFragment homeFragment;


    private DatabaseReference mdatabase;

    private OnClickListener mClickListener;


    public interface OnClickListener{
        public void setItem(String item);
    }

    public void setClickListener(OnClickListener callback) {
        mClickListener = callback;
    }


    public OverviewAdapter(Context context, List<WashingMachine> mMachines, WashingMachineOverviewFragment homeFragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mMachines = mMachines;
        this.homeFragment = homeFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.washing_maching_item, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        mdatabase = FirebaseDatabase.getInstance().getReference();
        final ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.mTextView.setText(mMachines.get(position).getDrawable_label());
        viewHolder.mImageView.setImageResource(mMachines.get(position).getDrawable_id());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mClickListener != null) {
                    mClickListener.setItem(mMachines.get(position).getDrawable_label());
                }
                mdatabase.child("/washing_machine/" + position).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot feature_snapShot: dataSnapshot.getChildren()) {
                            if(feature_snapShot.getKey().equals("status_id")) {
                                final String status_id_key = feature_snapShot.getKey();
                                String status_id_value = feature_snapShot.getValue().toString();
                                if(status_id_value.equals("1")) {
                                    final PickTime timePicker = new PickTime();
                                    timePicker.setListener(new PickTime.PickTimeListener(){
                                        @Override
                                        public void onPositiveClick() {

                                            FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(position));

                                            int mode = timePicker.timeSelected;
                                            if (mode != -1) {
                                                final String status_id_value_change = "2";
                                                long start_time = System.currentTimeMillis();
                                                long end_time = System.currentTimeMillis();
                                                if (mode == 0) {
                                                    end_time += 5000;
                                                } else if (mode == 1) {
                                                    end_time += 10000;
                                                } else if (mode == 2) {
                                                    end_time += 15000;
                                                }
                                                mMachines.get(position).setStatus_id(Integer.parseInt(status_id_value_change));
                                                mMachines.get(position).setStart_time(start_time);
                                                mMachines.get(position).setEnd_time(end_time);
                                                mdatabase.child("/washing_machine/" + position + "/" + status_id_key).setValue(status_id_value_change);
                                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                                                Date format_start = new Date(start_time);
                                                Date format_end = new Date(end_time);
                                                mdatabase.child("/washing_machine/" + position + "/user_email").setValue(Config.useremail);
                                                mMachines.get(position).setUser_email(Config.useremail);
                                                mdatabase.child("/washing_machine/" + position + "/start_time").setValue(sdf.format(format_start));
                                                mdatabase.child("/washing_machine/" + position + "/end_time").setValue(sdf.format(format_end));
                                                mdatabase.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot status_snapShot: dataSnapshot.getChildren()) {
                                                            if(status_snapShot.getKey().equals(status_id_value_change)) {
                                                                String status_value = status_snapShot.getValue().toString();
                                                                mMachines.get(position).setDrawable_label(status_value);
                                                                mdatabase.child("/washing_machine/" + position + "/drawable_label").setValue(status_value);
                                                                viewHolder.mTextView.setText(status_value);
                                                                viewHolder.mImageView.setImageResource(R.drawable.washing_machine_unavailable);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                timePicker.dismiss();

                                            } else {
                                                Toast.makeText(timePicker.getContext(), "Please choose a time", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onNegativeClick() {

                                        }

                                    });
                                    timePicker.show(homeFragment.getFragmentManager(), "timepk");
                                } else if(status_id_value.equals("3") || status_id_value.equals("4")) {

                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(String.valueOf(position));

                                    final String status_id_value_change = "1";
                                    long start_time = 0;
                                    long end_time = 0;
                                    mMachines.get(position).setStatus_id(Integer.parseInt(status_id_value_change));
                                    mMachines.get(position).setStart_time(start_time);
                                    mMachines.get(position).setEnd_time(end_time);
                                    mdatabase.child("/washing_machine/" + position + "/" + status_id_key).setValue(status_id_value_change);
                                    mdatabase.child("/washing_machine/" + position + "/start_time").setValue(start_time);
                                    mdatabase.child("/washing_machine/" + position + "/end_time").setValue(end_time);
                                    mdatabase.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot status_snapShot: dataSnapshot.getChildren()) {
                                                if(status_snapShot.getKey().equals(status_id_value_change)) {
                                                    String status_key = status_snapShot.getKey();
                                                    String status_value = status_snapShot.getValue().toString();
                                                    mMachines.get(position).setDrawable_label(status_value);
                                                    mdatabase.child("/washing_machine/" + position + "/drawable_label").setValue(status_value);
                                                    viewHolder.mTextView.setText(status_value);
                                                    viewHolder.mImageView.setImageResource(R.drawable.washing_machine);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else if(status_id_value.equals("2")) {
                                    long end_time = mMachines.get(position).getEnd_time();
//                                    String end_time_str = "";
//                                    for(DataSnapshot feature_snapShot_1: dataSnapshot.getChildren()) {
//                                        if (feature_snapShot_1.getKey().equals("end_time")) {
//                                            end_time_str =  feature_snapShot_1.getValue().toString();
//                                        }
//                                    }
                                    final DialogFragment remainingTime = new RemainingTime();
                                    ((RemainingTime) remainingTime).machineNo = position;
                                    remainingTime.show(homeFragment.getFragmentManager(), "washer");
                                    long timeRem = end_time - System.currentTimeMillis();
                                    new CountDownTimer(timeRem, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            if( ((RemainingTime) remainingTime).messageView != null) {
                                                ((RemainingTime) remainingTime).messageView.setText("CountDonwTimer:" + millisUntilFinished / 1000);
                                            }
                                        }

                                        public void onFinish() {
                                            if( ((RemainingTime) remainingTime).messageView != null) {
                                                ((RemainingTime) remainingTime).messageView.setText("done!");
                                            }
                                        }
                                    }.start();

                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        Timer timer = new Timer();
        TimerTask timerTask;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mdatabase.child("/washing_machine/" + position).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long end_time = mMachines.get(position).getEnd_time();
                        long curr_time = System.currentTimeMillis();
                        for(DataSnapshot feature_snapShot: dataSnapshot.getChildren()) {
                            if(feature_snapShot.getKey().equals("status_id")) {
                                final String status_id_key = feature_snapShot.getKey();
                                String status_id_value = feature_snapShot.getValue().toString();
                                if(status_id_value.equals("2") && curr_time >= end_time) {
                                    final String status_id_value_change = "3";
                                    mMachines.get(position).setStatus_id(Integer.parseInt(status_id_value_change));
                                    mdatabase.child("/washing_machine/" + position + "/" + status_id_key).setValue(status_id_value_change);
                                    mdatabase.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot status_snapShot: dataSnapshot.getChildren()) {
                                                if(status_snapShot.getKey().equals(status_id_value_change)) {
                                                    String status_value = status_snapShot.getValue().toString();
                                                    mMachines.get(position).setDrawable_label(status_value);
                                                    mdatabase.child("/washing_machine/" + position + "/drawable_label").setValue(status_value);
                                                    viewHolder.mTextView.setText(status_value);
                                                    viewHolder.mImageView.setImageResource(R.drawable.washing_machine_finished);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }else if(status_id_value.equals("3") && curr_time >= end_time + 10000) {
                                    final String status_id_value_change = "4";
                                    mMachines.get(position).setStatus_id(Integer.parseInt(status_id_value_change));
                                    mdatabase.child("/washing_machine/" + position + "/" + status_id_key).setValue(status_id_value_change);
                                    mdatabase.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot status_snapShot: dataSnapshot.getChildren()) {
                                                if(status_snapShot.getKey().equals(status_id_value_change)) {
                                                    String status_value = status_snapShot.getValue().toString();
                                                    mMachines.get(position).setDrawable_label(status_value);
                                                    mdatabase.child("/washing_machine/" + position + "/drawable_label").setValue(status_value);
                                                    viewHolder.mTextView.setText(status_value);
                                                    viewHolder.mImageView.setImageResource(R.drawable.washing_machine_overtime);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public int getItemCount() {
        return mMachines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.status_text);
            mImageView = (ImageView) itemView.findViewById(R.id.washing_machine_img);

        }
    }
}
