package com.ssn.eps.ssn.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ssn.eps.ssn.R;

/**
 * Created by Lluís on 23/11/2015.
 */
public class MessageDialogFragment extends DialogFragment {

    private boolean finishAtTheEnd = false;
    private Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String message = (String) getArguments().getSerializable("message");
        String title = (String) getArguments().getSerializable("title");
        int positiveButtonTextId = getArguments().containsKey("positiveButtonText") ? (Integer) getArguments().getSerializable("positiveButtonText") : R.string.ok;

        finishAtTheEnd = getArguments().containsKey("finish") ? getArguments().getBoolean("finish") : false;

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(positiveButtonTextId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(finishAtTheEnd && activity != null){
                            //Activity parent = (Activity) getActivity();
                            //parent.finish();
                            activity.finish();
                        }
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

}
