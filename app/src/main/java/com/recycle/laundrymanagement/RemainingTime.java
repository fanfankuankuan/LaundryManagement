package com.recycle.laundrymanagement;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemainingTime extends DialogFragment {
    TextView messageView;
    int machineNo;
    public RemainingTime() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.fragment_remaining_time, null);
        messageView = (TextView)customView.findViewById(R.id.rmn_time);
        builder.setTitle("Remaining Time of machine " + machineNo + ": ")
                .setView(customView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        return alert;
    }

}
