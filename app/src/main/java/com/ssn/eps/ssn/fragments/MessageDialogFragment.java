package com.ssn.eps.ssn.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.activities.FragmentsCommunicationInterface;

/**
 * Created by Lluís on 23/11/2015.
 */
public class MessageDialogFragment extends DialogFragment {

    private boolean finishAtTheEnd = false;
    private int eventId = Integer.MIN_VALUE;
    private Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String message = (String) getArguments().getSerializable("message");
        String title = (String) getArguments().getSerializable("title");
        int positiveButtonTextId = getArguments().containsKey("positiveButtonTextId") ? (Integer) getArguments().getSerializable("positiveButtonTextId") : R.string.ok;
        boolean cancellButton = getArguments().containsKey("cancellButton") ? getArguments().getBoolean("cancellButton") : false;

        eventId = getArguments().containsKey("eventId") ? getArguments().getInt("eventId") : Integer.MIN_VALUE;
        finishAtTheEnd = getArguments().containsKey("finish") ? getArguments().getBoolean("finish") : false;

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(positiveButtonTextId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(eventId > 0 && activity != null && activity instanceof FragmentsCommunicationInterface){
                            ((FragmentsCommunicationInterface) activity).goToEventById(eventId);
                        }
                        if(finishAtTheEnd && activity != null){
                            activity.finish();
                        }
                    }
                });

        if(cancellButton){
            builder.setNegativeButton(R.string.close,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

}
