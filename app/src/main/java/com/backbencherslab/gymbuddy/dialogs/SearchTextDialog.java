package com.backbencherslab.gymbuddy.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.backbencherslab.gymbuddy.R;

/**
 * Created by Paul Curtis on 3/22/2017.
 */

public class SearchTextDialog extends DialogFragment {
    private String searchText;

    /**
     * Declaring the interface, to invoke a callback function in the implementing activity class
     */
    SearchTextDialog.SearchTextPositiveListener searchTextPositiveListener;

    /**
     * An interface to be implemented in the hosting activity for "OK" button click listener
     */
    public interface SearchTextPositiveListener {
        void onCloseSearchTextDialog(String searchText);
    }

    /**
     * This is a callback method executed when this fragment is attached to an activity.
     * This function ensures that, the hosting activity implements the interface AlertPositiveListener
     */
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try {
            searchTextPositiveListener = (SearchTextDialog.SearchTextPositiveListener) activity;
        } catch (ClassCastException e) {
            // The hosting activity does not implemented the interface AlertPositiveListener
            throw new ClassCastException(activity.toString() + " must implement AlertPositiveListener");
        }
    }

    /**
     * This is the OK button listener for the alert dialog,
     * which in turn invokes the method onPositiveClick(position)
     * of the hosting activity which is supposed to implement it
     */
    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //TODO grab text from the search box.
            setSearchText("blah");
            searchTextPositiveListener.onCloseSearchTextDialog(searchText);
        }
    };

    /**
     * This is the OK button listener for the alert dialog,
     * which in turn invokes the method onPositiveClick(position)
     * of the hosting activity which is supposed to implement it
     */
    DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    /**
     * This is a callback method which will be executed
     * on creating this fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** Getting the arguments passed to this fragment */
        Bundle bundle = getArguments();

        searchText = bundle.getString("searchText");

        /** Creating a builder for the alert dialog window */
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        /** Setting a title for the window */
        b.setTitle(getText(R.string.hint_search_text));

        LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_search_text, null);

        b.setView(view);

        /** Setting a positive button and its listener */

        b.setPositiveButton(getText(R.string.action_ok), positiveListener);

        b.setNegativeButton(getText(R.string.action_cancel), negativeListener);

        b.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return true;
            }
        });

        /** Creating the alert dialog window using the builder class */
        final AlertDialog d = b.create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                final DialogInterface dlg = dialog;

                final Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        searchTextPositiveListener.onCloseSearchTextDialog(getSearchText());
                    }
                });

                Button p = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                p.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });

        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);

        /** Return the alert dialog window */
        return d;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
