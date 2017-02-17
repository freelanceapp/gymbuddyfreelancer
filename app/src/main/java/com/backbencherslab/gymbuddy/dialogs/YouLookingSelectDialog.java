package com.backbencherslab.gymbuddy.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.backbencherslab.gymbuddy.R;
import com.backbencherslab.gymbuddy.constants.Constants;

public class YouLookingSelectDialog extends DialogFragment implements Constants {

    private int position = 0;

    AlertPositiveListener alertPositiveListener;
    public interface AlertPositiveListener {

        public void onYouLookingSelect(int position);
    }

    public void onAttach(android.app.Activity activity) {

        super.onAttach(activity);

        try {

            alertPositiveListener = (AlertPositiveListener) activity;

        } catch(ClassCastException e){

            // The hosting activity does not implemented the interface AlertPositiveListener
            throw new ClassCastException(activity.toString() + " must implement AlertPositiveListener");
        }
    }

    OnClickListener positiveListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            AlertDialog alert = (AlertDialog)dialog;
            int position = alert.getListView().getCheckedItemPosition();

            alertPositiveListener.onYouLookingSelect(position);
        }
    };

    /** This is a callback method which will be executed
     *  on creating this fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String[] you_looking_strings = new String[] {

                getText(R.string.you_looking_0).toString(),
                getText(R.string.you_looking_1).toString(),
                getText(R.string.you_looking_2).toString(),
                getText(R.string.you_looking_3).toString(),
                getText(R.string.you_looking_4).toString(),
                getText(R.string.you_looking_5).toString(),
                getText(R.string.you_looking_6).toString(),
                getText(R.string.you_looking_7).toString(),

        };

        Bundle bundle = getArguments();

        position = bundle.getInt("position");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        b.setTitle(getText(R.string.account_you_looking));

        b.setSingleChoiceItems(you_looking_strings, position, null);

        b.setPositiveButton(getText(R.string.action_ok), positiveListener);

        b.setNegativeButton(getText(R.string.action_cancel), null);

        AlertDialog d = b.create();

        return d;
    }
}