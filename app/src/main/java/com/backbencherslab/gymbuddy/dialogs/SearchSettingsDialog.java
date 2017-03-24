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
    Spinner ageTo, ageFrom, workoutType, fitnessGoals, workoutTime, workoutDistance;

    private int searchGender, searchOnline, searchAgeFrom, searchAgeTo;
    //NEW FILTER FIELDS
    private int searchWorkoutType, searchFitnessGoals, searchWorkoutTime, searchWorkoutDistance;

    /**
     * Declaring the interface, to invoke a callback function in the implementing activity class
     */
    AlertPositiveListener alertPositiveListener;

    /**
     * An interface to be implemented in the hosting activity for "OK" button click listener
     */
    public interface AlertPositiveListener {
        void onCloseSettingsDialog(int searchGender, int searchOnline, int searchAgeFrom, int searchAgeTo, int searchWorkoutType, int searchFitnessGoals, int searchWorkoutTime, int searchWorkoutDistance);
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
            alertPositiveListener.onCloseSettingsDialog(searchGender, searchOnline, searchAgeFrom, searchAgeTo, searchWorkoutType, searchFitnessGoals, searchWorkoutTime, searchWorkoutDistance);
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
            //DO NOTHING BUT CLOSE.
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
        searchWorkoutType = bundle.getInt("searchWorkoutType");
        searchFitnessGoals = bundle.getInt("searchFitnessGoals");
        searchWorkoutTime = bundle.getInt("searchWorkoutTime");
        searchWorkoutDistance = bundle.getInt("searchWorkoutDistance");

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

        // NEW FILTERS
        workoutType = (Spinner) view.findViewById(R.id.dropdown_filter_by_workout_type);
        fitnessGoals = (Spinner) view.findViewById(R.id.dropdown_filter_by_fitness_goals);
        workoutTime = (Spinner) view.findViewById(R.id.dropdown_filter_by_workout_time);
        workoutDistance = (Spinner) view.findViewById(R.id.dropdown_filter_by_distance);

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

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
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

        /** Workout Type Filter */
        workoutType = (Spinner) view.findViewById(R.id.dropdown_filter_by_workout_type);

        ArrayAdapter<String> workoutTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        workoutTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutType.setAdapter(workoutTypeSpinnerAdapter);
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
        workoutType.setSelection(0);

        /** Fitness Goals Filter */
        fitnessGoals = (Spinner) view.findViewById(R.id.dropdown_filter_by_fitness_goals);

        ArrayAdapter<String> fitnessGoalsSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        fitnessGoalsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitnessGoals.setAdapter(fitnessGoalsSpinnerAdapter);
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
        fitnessGoals.setSelection(0);

        /** Workout Time Filter */
        workoutTime = (Spinner) view.findViewById(R.id.dropdown_filter_by_workout_time);

        ArrayAdapter<String> workoutTimeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        workoutTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTime.setAdapter(workoutTimeSpinnerAdapter);
        workoutTimeSpinnerAdapter.add(getResources().getString(R.string.relationship_status_0));
        workoutTimeSpinnerAdapter.add(getResources().getString(R.string.relationship_status_1));
        workoutTimeSpinnerAdapter.add(getResources().getString(R.string.relationship_status_2));
        workoutTimeSpinnerAdapter.add(getResources().getString(R.string.relationship_status_3));
        workoutTimeSpinnerAdapter.add(getResources().getString(R.string.relationship_status_4));
        workoutTimeSpinnerAdapter.notifyDataSetChanged();
        workoutTime.setSelection(0);

        /** Distance Filter */
        workoutDistance = (Spinner) view.findViewById(R.id.dropdown_filter_by_distance);

        ArrayAdapter<String> distanceSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        distanceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutDistance.setAdapter(distanceSpinnerAdapter);
        distanceSpinnerAdapter.add(getResources().getString(R.string.dialog_nearby_0));
        distanceSpinnerAdapter.add(getResources().getString(R.string.dialog_nearby_1));
        distanceSpinnerAdapter.add(getResources().getString(R.string.dialog_nearby_2));
        distanceSpinnerAdapter.add(getResources().getString(R.string.dialog_nearby_3));
        distanceSpinnerAdapter.add(getResources().getString(R.string.dialog_nearby_4));
        distanceSpinnerAdapter.notifyDataSetChanged();
        workoutDistance.setSelection(0);

        setGender(searchGender);
        setOnline(searchOnline);
        setAgeFrom(searchAgeFrom);
        setAgeTo(searchAgeTo);
        setWorkoutType(searchWorkoutType);
        setFitnessGoals(searchFitnessGoals);
        setWorkoutTime(searchWorkoutTime);
        setWorkoutDistance(searchWorkoutDistance);

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
                        alertPositiveListener.onCloseSettingsDialog(getGender(), getOnline(), getAgeFrom(), getAgeTo(), getWorkoutType(), getFitnessGoals(), getWorkoutTime(), getWorkoutDistance());
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
        int age;
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

    public int getWorkoutType() {
        int workoutTypeInt = -1;
        switch (workoutType.getSelectedItemPosition()) {
            case 0: {
                workoutTypeInt = 0;
                break;
            }
            case 1: {
                workoutTypeInt = 1;
                break;
            }
            case 2: {
                workoutTypeInt = 2;
                break;
            }
            case 3: {
                workoutTypeInt = 3;
                break;
            }
            case 4: {
                workoutTypeInt = 4;
                break;
            }
            case 5: {
                workoutTypeInt = 5;
                break;
            }
            case 6: {
                workoutTypeInt = 6;
                break;
            }
            case 7: {
                workoutTypeInt = 7;
                break;
            }
            case 8: {
                workoutTypeInt = 8;
                break;
            }
            case 9: {
                workoutTypeInt = 9;
                break;
            }
            default: {
                workoutTypeInt = 0;
                break;
            }
        }
        return workoutTypeInt;
    }

    public void setWorkoutType(int searchWorkoutType) {
        this.searchWorkoutType = searchWorkoutType;
    }

    public int getFitnessGoals() {
        int fitnessGoalsInt;
        switch (fitnessGoals.getSelectedItemPosition()) {
            case 0: {
                fitnessGoalsInt = 0;
                break;
            }
            case 1: {
                fitnessGoalsInt = 1;
                break;
            }
            case 2: {
                fitnessGoalsInt = 2;
                break;
            }
            case 3: {
                fitnessGoalsInt = 3;
                break;
            }
            case 4: {
                fitnessGoalsInt = 4;
                break;
            }
            case 5: {
                fitnessGoalsInt = 5;
                break;
            }
            case 6: {
                fitnessGoalsInt = 6;
                break;
            }
            case 7: {
                fitnessGoalsInt = 7;
                break;
            }
            case 8: {
                fitnessGoalsInt = 8;
                break;
            }
            case 9: {
                fitnessGoalsInt = 9;
                break;
            }
            case 10: {
                fitnessGoalsInt = 10;
                break;
            }
            default: {
                fitnessGoalsInt = 0;
                break;
            }
        }
        return fitnessGoalsInt;
    }

    public void setFitnessGoals(int searchFitnessGoals) {
        this.searchFitnessGoals = searchFitnessGoals;
    }

    public int getWorkoutTime() {
        int workoutTimeInt;
        switch (workoutTime.getSelectedItemPosition()) {
            case 0: {
                workoutTimeInt = 0;
                break;
            }
            case 1: {
                workoutTimeInt = 1;
                break;
            }
            case 2: {
                workoutTimeInt = 2;
                break;
            }
            case 3: {
                workoutTimeInt = 3;
                break;
            }
            case 4: {
                workoutTimeInt = 4;
                break;
            }
            default: {
                workoutTimeInt = 0;
            }
        }
        return workoutTimeInt;
    }

    public void setWorkoutTime(int searchWorkoutTime) {
        //TODO THIS METHOD NEEDS TO BE USED TO SET THE SELECTION UPON CREATION OF EACH SETTINGS DIALOG
        this.searchWorkoutTime = searchWorkoutTime;
    }

    public int getWorkoutDistance() {
        int workoutDistanceInt;
        switch (workoutDistance.getSelectedItemPosition()) {
            case 0: {
                workoutDistanceInt = 0;
                break;
            }
            case 1: {
                workoutDistanceInt = 1;
                break;
            }
            case 2: {
                workoutDistanceInt = 2;
                break;
            }
            case 3: {
                workoutDistanceInt = 3;
                break;
            }
            case 4: {
                workoutDistanceInt = 4;
                break;
            }
            default: {
                workoutDistanceInt = 0;
            }
        }
        return workoutDistanceInt;
    }

    public void setWorkoutDistance(int searchWorkoutDistance) {
        this.searchWorkoutDistance = searchWorkoutDistance;
    }
}