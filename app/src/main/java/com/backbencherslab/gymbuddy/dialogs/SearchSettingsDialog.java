package com.backbencherslab.gymbuddy.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.backbencherslab.gymbuddy.R;
import com.backbencherslab.gymbuddy.constants.Constants;


public class SearchSettingsDialog extends DialogFragment implements Constants {

    CheckBox genderMaleCheckBox, genderFemaleCheckBox, onlineCheckBox;
    Spinner ageTo, ageFrom, workoutTypeSpinner, fitnessGoalsSpinner, lookingForSpinner;

    private int searchGender, searchOnline, searchAgeFrom, searchAgeTo;
    private String searchWorkoutType, searchFitnessGoals;

    /**
     * Declaring the interface, to invoke a callback function in the implementing activity class
     */
    AlertPositiveListener alertPositiveListener;

    /**
     * An interface to be implemented in the hosting activity for "OK" button click listener
     */
    public interface AlertPositiveListener {

        void onCloseSettingsDialog(int searchGender, int searchOnline, int searchAgeFrom, int searchAgeTo, String workoutType, String fitnessGoals);
    }

    /**
     * This is a callback method executed when this fragment is attached to an activity.
     * This function ensures that, the hosting activity implements the interface AlertPositiveListener
     */
    public void onAttach(android.app.Activity activity) {

        super.onAttach(activity);

        try {

            alertPositiveListener = (AlertPositiveListener) activity;

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
    OnClickListener positiveListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            alertPositiveListener.onCloseSettingsDialog(searchGender, searchOnline, searchAgeFrom, searchAgeTo, searchWorkoutType, searchFitnessGoals);
        }
    };

    /**
     * This is the OK button listener for the alert dialog,
     * which in turn invokes the method onPositiveClick(position)
     * of the hosting activity which is supposed to implement it
     */
    OnClickListener negativeListener = new OnClickListener() {
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

        searchGender = bundle.getInt("searchGender");
        searchOnline = bundle.getInt("searchOnline");
        searchAgeFrom = bundle.getInt("searchAgeFrom");
        searchAgeTo = bundle.getInt("searchAgeTo");
        searchWorkoutType = bundle.getString("searchWorkoutType");
        searchFitnessGoals = bundle.getString("searchFitnessGoals");

        /** Creating a builder for the alert dialog window */
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        /** Setting a title for the window */
        b.setTitle(getText(R.string.label_search_settings_dialog_title));

        LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_search_settings, null);

        b.setView(view);

        genderMaleCheckBox = (CheckBox) view.findViewById(R.id.genderMaleCheckBox);
        genderFemaleCheckBox = (CheckBox) view.findViewById(R.id.genderFemaleCheckBox);
        onlineCheckBox = (CheckBox) view.findViewById(R.id.onlineCheckBox);

        ageFrom = (Spinner) view.findViewById(R.id.ageFrom);
        ageTo = (Spinner) view.findViewById(R.id.ageTo);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageFrom.setAdapter(spinnerAdapter);
        spinnerAdapter.add(getString(R.string.age_item_from_2));
        spinnerAdapter.add(getString(R.string.age_item_from_3));
        spinnerAdapter.add(getString(R.string.age_item_from_4));
        spinnerAdapter.add(getString(R.string.age_item_from_5));
        spinnerAdapter.add(getString(R.string.age_item_from_6));
        spinnerAdapter.add(getString(R.string.age_item_from_7));
        spinnerAdapter.add(getString(R.string.age_item_from_8));
        spinnerAdapter.add(getString(R.string.age_item_from_10));
        spinnerAdapter.add(getString(R.string.age_item_from_11));
        spinnerAdapter.add(getString(R.string.age_item_from_12));
        spinnerAdapter.add(getString(R.string.age_item_from_13));
        spinnerAdapter.notifyDataSetChanged();
        ageFrom.setSelection(0);

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageTo.setAdapter(spinnerAdapter2);
        spinnerAdapter2.add(getString(R.string.age_item_to_2));
        spinnerAdapter2.add(getString(R.string.age_item_to_3));
        spinnerAdapter2.add(getString(R.string.age_item_to_4));
        spinnerAdapter2.add(getString(R.string.age_item_to_5));
        spinnerAdapter2.add(getString(R.string.age_item_to_6));
        spinnerAdapter2.add(getString(R.string.age_item_to_7));
        spinnerAdapter2.add(getString(R.string.age_item_to_8));
        spinnerAdapter2.add(getString(R.string.age_item_to_9));
        spinnerAdapter2.add(getString(R.string.age_item_to_10));
        spinnerAdapter2.add(getString(R.string.age_item_to_11));
        spinnerAdapter2.add(getString(R.string.age_item_to_12));
        spinnerAdapter2.add(getString(R.string.age_item_to_13));
        spinnerAdapter2.add(getString(R.string.age_item_to_14));
        spinnerAdapter2.notifyDataSetChanged();
        ageTo.setSelection(12);

        setGender(searchGender);
        setOnline(searchOnline);
        setAgeFrom(searchAgeFrom);
        setAgeTo(searchAgeTo);

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
                        alertPositiveListener.onCloseSettingsDialog(getGender(), getOnline(), getAgeFrom(), getAgeTo(), getSearchWorkoutType(), getSearchFitnessGoals());
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

        /** Workout Type Filter */
        workoutTypeSpinner = (Spinner) view.findViewById(R.id.dropdown_filter_by_workout_type);

        ArrayAdapter<String> workoutTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        workoutTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeSpinner.setAdapter(workoutTypeSpinnerAdapter);
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_0));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_1));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_2));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_3));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_4));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_5));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_6));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_7));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_8));
        workoutTypeSpinnerAdapter.add(getResources().getString(R.string.world_view_9));
        workoutTypeSpinnerAdapter.notifyDataSetChanged();

        /** Fitness Goals Filter */
        fitnessGoalsSpinner = (Spinner) view.findViewById(R.id.dropdown_filter_by_fitness_goals);

        ArrayAdapter<String> fitnessGoalsSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        fitnessGoalsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitnessGoalsSpinner.setAdapter(fitnessGoalsSpinnerAdapter);
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_0));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_1));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_2));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_3));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_4));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_5));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_6));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_7));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_8));
        fitnessGoalsSpinnerAdapter.add(getResources().getString(R.string.personal_priority_9));
        fitnessGoalsSpinnerAdapter.notifyDataSetChanged();

        /** Looking For Filter */
        lookingForSpinner = (Spinner) view.findViewById(R.id.dropdown_filter_by_looking_for);

        ArrayAdapter<String> lookingForSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        lookingForSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lookingForSpinner.setAdapter(lookingForSpinnerAdapter);
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_0));
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_1));
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_2));
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_3));
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_4));
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_5));
        lookingForSpinnerAdapter.add(getResources().getString(R.string.important_in_others_6));
        lookingForSpinnerAdapter.notifyDataSetChanged();

        /** Return the alert dialog window */
        return d;
    }

    public int getGender() {

        if (genderFemaleCheckBox.isChecked() && genderMaleCheckBox.isChecked()) {

            return -1;
        }

        if (genderMaleCheckBox.isChecked()) {

            return 0;
        }

        if (genderFemaleCheckBox.isChecked()) {

            return 1;
        }

        return -1;
    }

    public void setGender(int gender) {

        switch (gender) {

            case 0: {

                genderMaleCheckBox.setChecked(true);
                genderFemaleCheckBox.setChecked(false);

                break;
            }

            case 1: {

                genderMaleCheckBox.setChecked(false);
                genderFemaleCheckBox.setChecked(true);

                break;
            }

            default: {

                genderMaleCheckBox.setChecked(true);
                genderFemaleCheckBox.setChecked(true);

                break;
            }
        }
    }

    public int getOnline() {

        if (onlineCheckBox.isChecked()) {

            return 0;
        }

        return -1;
    }

    public void setOnline(int online) {

        if (online == -1) {

            onlineCheckBox.setChecked(false);

        } else {

            onlineCheckBox.setChecked(true);
        }
    }

    public void setAgeFrom(int age) {

        switch (age) {

            case 13: {

                ageFrom.setSelection(0);

                break;
            }

            case 18: {

                ageFrom.setSelection(1);

                break;
            }

            case 25: {

                ageFrom.setSelection(2);

                break;
            }

            case 30: {

                ageFrom.setSelection(3);

                break;
            }

            case 35: {

                ageFrom.setSelection(4);

                break;
            }

            case 40: {

                ageFrom.setSelection(5);

                break;
            }

            case 45: {

                ageFrom.setSelection(6);

                break;
            }

            default: {

                ageFrom.setSelection(0);

                break;
            }
        }
    }

    public int getAgeFrom() {
        int age = 13;

        switch (ageFrom.getSelectedItemPosition()) {

            case 0: {

                age = 13;

                break;
            }

            case 1: {

                age = 18;

                break;
            }

            case 2: {

                age = 25;

                break;
            }

            case 3: {

                age = 30;

                break;
            }

            case 4: {

                age = 35;

                break;
            }

            case 5: {

                age = 40;

                break;
            }

            case 6: {

                age = 45;

                break;
            }

            default: {

                age = 13;

                break;
            }
        }

        return age;
    }

    public void setAgeTo(int age) {

        switch (age) {

            case 20: {

                ageTo.setSelection(0);

                break;
            }

            case 27: {

                ageTo.setSelection(1);

                break;
            }

            case 38: {

                ageTo.setSelection(2);

                break;
            }

            case 43: {

                ageTo.setSelection(3);

                break;
            }

            case 50: {

                ageTo.setSelection(4);

                break;
            }

            case 70: {

                ageTo.setSelection(5);

                break;
            }

            default: {

                ageTo.setSelection(6);

                break;
            }
        }
    }

    public int getAgeTo() {

        int age = 110;

        switch (ageTo.getSelectedItemPosition()) {

            case 0: {

                age = 20;

                break;
            }

            case 1: {

                age = 27;

                break;
            }

            case 2: {

                age = 38;

                break;
            }

            case 3: {

                age = 43;

                break;
            }

            case 4: {

                age = 50;

                break;
            }

            case 5: {

                age = 70;

                break;
            }

            default: {

                age = 110;

                break;
            }
        }

        return age;
    }

    public String getSearchWorkoutType() {
        return searchWorkoutType;
    }

    public void setSearchWorkoutType(String searchWorkoutType) {
        this.searchWorkoutType = searchWorkoutType;
    }

    public String getSearchFitnessGoals() {
        return searchFitnessGoals;
    }

    public void setSearchFitnessGoals(String searchFitnessGoals) {
        this.searchFitnessGoals = searchFitnessGoals;
    }
}