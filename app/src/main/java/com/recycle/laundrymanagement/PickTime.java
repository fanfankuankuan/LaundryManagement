package com.recycle.laundrymanagement;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class PickTime extends DialogFragment {
    int timeSelected = -1;
    private PickTimeListener callbackListener;
    public interface PickTimeListener {
        public void onPositiveClick();
        public void onNegativeClick();
    }

    public void setListener(PickTimeListener listener) {
        callbackListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Pick a Time Duration")
                .setSingleChoiceItems(R.array.timeDuration, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timeSelected = which;
                            }
                        })
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        timeSelected = -1;
                        callbackListener.onNegativeClick();
                    }
                }).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callbackListener.onPositiveClick();
                    }
                });
            }
        });
        return dialog;
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            callbackListener = (PickTimeListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Adapter must implement NoticeDialogListener");
//        }
//    }
//    @Override
//    public void onActivityCreated (Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//    }
}
