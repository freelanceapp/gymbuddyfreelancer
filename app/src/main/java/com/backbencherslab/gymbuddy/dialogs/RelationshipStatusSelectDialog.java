package com.backbencherslab.gymbuddy.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.backbencherslab.gymbuddy.R;
import com.backbencherslab.gymbuddy.constants.Constants;

public class RelationshipStatusSelectDialog extends DialogFragment implements Constants {

    private int position = 0;

    AlertPositiveListener alertPositiveListener;

    /** An interface to be implemented in the hosting activity for "OK" button click listener */
    public interface AlertPositiveListener {

        public void onRelationshipStatusSelect(int position);
    }

    /** This is a callback method executed when this fragment is attached to an activity.
     *  This function ensures that, the hosting activity implements the interface AlertPositiveListener
     * */
    public void onAttach(android.app.Activity activity) {

        super.onAttach(activity);

        try {

            alertPositiveListener = (AlertPositiveListener) activity;

        } catch(ClassCastException e){

            // The hosting activity does not implemented the interface AlertPositiveListener
            throw new ClassCastException(activity.toString() + " must implement AlertPositiveListener");
        }
    }

    /** This is the OK button listener for the alert dialog,
     *  which in turn invokes the method onPositiveClick(position)
     *  of the hosting activity which is supposed to implement it
     */
    OnClickListener positiveListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            AlertDialog alert = (AlertDialog)dialog;
            int position = alert.getListView().getCheckedItemPosition();

            alertPositiveListener.onRelationshipStatusSelect(position);
        }
    };

    /** This is a callback method which will be executed
     *  on creating this fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String[] relationship_status_strings = new String[] {

                getText(R.string.relationship_status_0).toString(),
                getText(R.string.relationship_status_1).toString(),
                getText(R.string.relationship_status_2).toString(),
                getText(R.string.relationship_status_3).toString(),
                getText(R.string.relationship_status_4).toString(),

        };

        Bundle bundle = getArguments();

        position = bundle.getInt("position");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        b.setTitle(getText(R.string.Workout_time));

        b.setSingleChoiceItems(relationship_status_strings, position, null);

        b.setPositiveButton(getText(R.string.action_ok), positiveListener);

        b.setNegativeButton(getText(R.string.action_cancel), null);

        AlertDialog d = b.create();

        return d;
    }
}