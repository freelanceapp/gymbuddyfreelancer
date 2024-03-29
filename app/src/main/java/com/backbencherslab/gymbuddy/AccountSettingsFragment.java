package com.backbencherslab.gymbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.constants.Constants;
import com.backbencherslab.gymbuddy.dialogs.AlcoholViewsSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.GenderSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.ImportantInOthersSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.PersonalPrioritySelectDialog;
import com.backbencherslab.gymbuddy.dialogs.PoliticalViewsSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.RelationshipStatusSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.SmokingViewsSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.WorldViewSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.YouLikeSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.YouLookingSelectDialog;
import com.backbencherslab.gymbuddy.util.CustomRequest;

public class AccountSettingsFragment extends Fragment implements Constants {

    public static final int RESULT_OK = -1;

    private ProgressDialog pDialog;

    private String fullname, location, facebookPage, instagramPage, bio;

    private int sex, year, month, day;
    private int relationshipStatus, politicalViews, worldView, personalPriority, importantInOthers, viewsOnSmoking, viewsOnAlcohol, youLooking, youLike, allowShowMyBirthday;

    EditText mFullname, mLocation, mFacebookPage, mInstagramPage, mBio;
    Button mBirth, mGender, mRelationshipStatus, mWorldView, mPoliticalViews, mPersonalPriority, mImportantInOthers, mSmokingViews, mAlcoholViews, mYouLooking, mYouLike;

    CheckBox mAllowShowDatebirth;

    private Boolean loading = false;
    DatabaseHelper helpher;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);

        initpDialog();

        Intent i = getActivity().getIntent();
        fullname = i.getStringExtra("fullname");
        location = i.getStringExtra("location");
        facebookPage = i.getStringExtra("facebookPage");
        instagramPage = i.getStringExtra("instagramPage");
        bio = i.getStringExtra("bio");

        sex = i.getIntExtra("sex", 0);

        year = i.getIntExtra("year", 0);
        month = i.getIntExtra("month", 0);
        day = i.getIntExtra("day", 0);

        relationshipStatus = i.getIntExtra("relationshipStatus", 0);
        politicalViews = i.getIntExtra("politicalViews", 0);
        worldView = i.getIntExtra("worldView", 0);
        personalPriority = i.getIntExtra("personalPriority", 0);
        importantInOthers = i.getIntExtra("importantInOthers", 0);
        viewsOnSmoking = i.getIntExtra("viewsOnSmoking", 0);
        viewsOnAlcohol = i.getIntExtra("viewsOnAlcohol", 0);
        youLooking = i.getIntExtra("youLooking", 0);
        youLike = i.getIntExtra("youLike", 0);

        allowShowMyBirthday = i.getIntExtra("allowShowMyBirthday", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_settings, container, false);

        if (loading) {

            showpDialog();
        }

        mFullname = (EditText) rootView.findViewById(R.id.fullname);
        mLocation = (EditText) rootView.findViewById(R.id.location);
        mFacebookPage = (EditText) rootView.findViewById(R.id.facebookPage);
        mInstagramPage = (EditText) rootView.findViewById(R.id.instagramPage);
        mBio = (EditText) rootView.findViewById(R.id.bio);

        mBirth = (Button) rootView.findViewById(R.id.selectBirth);
        mGender = (Button) rootView.findViewById(R.id.selectGender);
        mRelationshipStatus = (Button) rootView.findViewById(R.id.selectRelationshipStatus);
        mPoliticalViews = (Button) rootView.findViewById(R.id.selectPoliticalViews);
        mWorldView = (Button) rootView.findViewById(R.id.selectWorldView);
        mPersonalPriority = (Button) rootView.findViewById(R.id.selectPersonalPriority);
        mImportantInOthers = (Button) rootView.findViewById(R.id.selectImportantInOthers);
        mSmokingViews = (Button) rootView.findViewById(R.id.selectSmokingViews);
        mAlcoholViews = (Button) rootView.findViewById(R.id.selectAlcoholViews);
        mYouLooking = (Button) rootView.findViewById(R.id.selectYouLooking);
        mYouLike = (Button) rootView.findViewById(R.id.selectYouLike);

        mAllowShowDatebirth = (CheckBox) rootView.findViewById(R.id.allowShowDatebirth);

        mBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
                dpd.getDatePicker().setMaxDate(new Date().getTime());

                dpd.show();
            }
        });

        mGender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectGender(sex);
            }
        });

        mRelationshipStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectRelationshipStatus(relationshipStatus);
            }
        });

        mPoliticalViews.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectPoliticalViews(politicalViews);
            }
        });

        mWorldView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectWorldView(worldView);
            }
        });

        mPersonalPriority.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectPersonalPriority(personalPriority);
            }
        });

        mImportantInOthers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectImportantInOthers(importantInOthers);
            }
        });

        mSmokingViews.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectSmokingViews(viewsOnSmoking);
            }
        });

        mAlcoholViews.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectAlcoholViews(viewsOnAlcohol);
            }
        });

        mYouLooking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectYouLooking(youLooking);
            }
        });

        mYouLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectYouLike(youLike);
            }
        });


        mFullname.setText(fullname);
        mLocation.setText(location);
        mFacebookPage.setText(facebookPage);
        mInstagramPage.setText(instagramPage);
        mBio.setText(bio);

        getGender(sex);
        getRelationshipStatus(relationshipStatus);
        getPoliticalViews(politicalViews);
        getWorldView(worldView);
        getPersonalPriority(personalPriority);
        getImportantInOthers(importantInOthers);
        getSmokingViews(viewsOnSmoking);
        getAlcoholViews(viewsOnAlcohol);
        getYouLooking(youLooking);
        getYouLike(youLike);

        int mMonth1 = month + 1;

        mBirth.setText(getString(R.string.action_select_birth) + ": " + new StringBuilder().append(day).append("/").append(mMonth1).append("/").append(year));

        checkAllowShowBirthday(allowShowMyBirthday);

        return rootView;
    }

    public void checkAllowShowBirthday(int value) {

        if (value == 1) {

            mAllowShowDatebirth.setChecked(true);
            allowShowMyBirthday = 1;

        } else {

            mAllowShowDatebirth.setChecked(false);
            allowShowMyBirthday = 0;
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {

            year = mYear;
            month = monthOfYear;
            day = dayOfMonth;

            int mMonth1 = month + 1;

            mBirth.setText(getString(R.string.action_select_birth) + ": " + new StringBuilder().append(day).append("/").append(mMonth1).append("/").append(year));

        }

    };

    public void selectGender(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        GenderSelectDialog alert = new GenderSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_gender");
    }

    public void getGender(int mSex) {

        sex = mSex;

        if (mSex == 0) {

            mGender.setText(getString(R.string.label_sex_male));
            mGender.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);


        } else {

            mGender.setText(getString(R.string.label_sex_female));
            mGender.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);

        }
    }

    public void selectRelationshipStatus(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        RelationshipStatusSelectDialog alert = new RelationshipStatusSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_relationship_status");
    }

    public void getRelationshipStatus(int mRelationship) {

        relationshipStatus = mRelationship;

        switch (mRelationship) {

            case 0: {

                mRelationshipStatus.setText(getString(R.string.Workout_time) + ": " + getString(R.string.relationship_status_0));


                break;
            }

            case 1: {
                mRelationshipStatus.setText(getString(R.string.Workout_time) + ": " + getString(R.string.relationship_status_1));
                ;
                mRelationshipStatus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.morning), null, null, null);
                break;
            }

            case 2: {
                Drawable icon = this.getResources().getDrawable(R.drawable.afternoon);
                mRelationshipStatus.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                mRelationshipStatus.setText(getString(R.string.Workout_time) + ": " + getString(R.string.relationship_status_2));
                ;

                break;
            }

            case 3: {
                Drawable icon = this.getResources().getDrawable(R.drawable.evening);
                mRelationshipStatus.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

                mRelationshipStatus.setText(getString(R.string.Workout_time) + ": " + getString(R.string.relationship_status_3));
                ;

                break;
            }

            case 4: {
                Drawable icon = this.getResources().getDrawable(R.drawable.night);
                mRelationshipStatus.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

                mRelationshipStatus.setText(getString(R.string.Workout_time) + ": " + getString(R.string.relationship_status_4));
                ;

                break;
            }

            default: {

                break;
            }
        }
    }
    public void selectPoliticalViews(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        PoliticalViewsSelectDialog alert = new PoliticalViewsSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_political_views");
    }

    public void getPoliticalViews(int mPolitical) {

        politicalViews = mPolitical;

        switch (mPolitical) {

            case 0: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_0));

                break;
            }

            case 1: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_1));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.mon), null, null, null);

                break;
            }

            case 2: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_2));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.tue), null, null, null);

                break;
            }

            case 3: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_3));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.wed), null, null, null);

                break;
            }

            case 4: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_4));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.thu), null, null, null);

                break;
            }

            case 5: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_5));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.fri), null, null, null);

                break;
            }

            case 6: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_6));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sat), null, null, null);

                break;
            }

            case 7: {

                mPoliticalViews.setText(getString(R.string.account_political_views) + ": " + getString(R.string.political_views_7));
                mPoliticalViews.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sun), null, null, null);

                break;
            }


            default: {

                break;
            }
        }
    }

    public void selectWorldView(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();
        WorldViewSelectDialog alert = new WorldViewSelectDialog();
        Bundle b  = new Bundle();
        b.putInt("position", position);
        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_world_view");
    }

    public void getWorldView(int mWorld) {

        worldView = mWorld;

        switch (mWorld) {

            case 0: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_0));

                break;
            }

            case 1: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_1));

                break;
            }

            case 2: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_2));

                break;
            }

            case 3: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_3));

                break;
            }

            case 4: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_4));

                break;
            }

            case 5: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_5));

                break;
            }

            case 6: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_6));

                break;
            }

            case 7: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_7));

                break;
            }

            case 8: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_8));

                break;
            }

            case 9: {

                mWorldView.setText(getString(R.string.account_world_view) + ": " + getString(R.string.world_view_9));

                break;
            }

            default: {

                break;
            }
        }
    }

    public void selectPersonalPriority(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        PersonalPrioritySelectDialog alert = new PersonalPrioritySelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_personal_priority");
    }

    public void getPersonalPriority(int mPriority) {

        personalPriority = mPriority;

        switch (mPriority) {

            case 0: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_0));

                break;
            }

            case 1: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_1));

                break;
            }

            case 2: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_2));

                break;
            }

            case 3: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_3));

                break;
            }

            case 4: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_4));

                break;
            }

            case 5: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_5));

                break;
            }

            case 6: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_6));

                break;
            }

            case 7: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_7));

                break;
            }

            case 8: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_8));

                break;
            }
            case 9: {

                mPersonalPriority.setText(getString(R.string.workouttype) + ": " + getString(R.string.personal_priority_9));

                break;
            }

            default: {

                break;
            }
        }
    }

    public void selectImportantInOthers(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        ImportantInOthersSelectDialog alert = new ImportantInOthersSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_important_in_others");
    }

    public void getImportantInOthers(int mImportant) {

        importantInOthers = mImportant;

        switch (mImportant) {

            case 0: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_0));

                break;
            }

            case 1: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_1));

                break;
            }

            case 2: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_2));

                break;
            }

            case 3: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_3));

                break;
            }

            case 4: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_4));

                break;
            }

            case 5: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_5));

                break;
            }

            case 6: {

                mImportantInOthers.setText(getString(R.string.account_you_looking) + ": " + getString(R.string.important_in_others_6));

                break;
            }

            default: {

                break;
            }
        }
    }

    public void selectSmokingViews(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        SmokingViewsSelectDialog alert = new SmokingViewsSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_smoking_views");
    }

    public void getSmokingViews(int mSmoking) {

        viewsOnSmoking = mSmoking;

        switch (mSmoking) {

            case 0: {

                mSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + getString(R.string.smoking_views_0));

                break;
            }

            case 1: {

                mSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + getString(R.string.smoking_views_1));

                break;
            }

            case 2: {

                mSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + getString(R.string.smoking_views_2));

                break;
            }

            case 3: {

                mSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + getString(R.string.smoking_views_3));

                break;
            }

            case 4: {

                mSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + getString(R.string.smoking_views_4));

                break;
            }

            case 5: {

                mSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + getString(R.string.smoking_views_5));

                break;
            }

            default: {

                break;
            }
        }
    }

    public void selectAlcoholViews(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        AlcoholViewsSelectDialog alert = new AlcoholViewsSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_alcohol_views");
    }

    public void getAlcoholViews(int mAlcohol) {

        viewsOnAlcohol = mAlcohol;

        switch (mAlcohol) {

            case 0: {

                mAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + getString(R.string.alcohol_views_0));

                break;
            }

            case 1: {

                mAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + getString(R.string.alcohol_views_1));

                break;
            }

            case 2: {

                mAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + getString(R.string.alcohol_views_2));

                break;
            }

            case 3: {

                mAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + getString(R.string.alcohol_views_3));

                break;
            }

            case 4: {

                mAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + getString(R.string.alcohol_views_4));

                break;
            }

            case 5: {

                mAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + getString(R.string.alcohol_views_5));

                break;
            }

            default: {

                break;
            }
        }
    }

    public void selectYouLooking(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        YouLookingSelectDialog alert = new YouLookingSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_you_looking");
    }

    public void getYouLooking(int mLooking) {

        youLooking = mLooking;

        switch (mLooking) {

            case 0: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_0));

                break;
            }

            case 1: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_1));

                break;
            }

            case 2: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_2));

                break;
            }

            case 3: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_3));

                break;
            }

            case 4: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_4));

                break;
            }
            case 5: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_5));

                break;
            }
            case 6: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_6));

                break;
            }
            case 7: {

                mYouLooking.setText(getString(R.string.account_you_looking_dialog) + ": " + getString(R.string.you_looking_7));

                break;
            }


            default: {

                break;
            }
        }
    }

    public void selectYouLike(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        YouLikeSelectDialog alert = new YouLikeSelectDialog();

        Bundle b  = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_you_like");
    }

    public void getYouLike(int mLike) {

        youLike = mLike;

        switch (mLike) {

            case 0: {

                mYouLike.setText(getString(R.string.account_you_like_dialog) + ": " + getString(R.string.you_like_0));

                break;
            }

            case 1: {

                mYouLike.setText(getString(R.string.account_you_like_dialog) + ": " + getString(R.string.you_like_1));

                break;
            }

            case 2: {

                mYouLike.setText(getString(R.string.account_you_like_dialog) + ": " + getString(R.string.you_like_2));

                break;
            }

            default: {

                break;
            }
        }
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save: {

                fullname = mFullname.getText().toString();
                location = mLocation.getText().toString();
                facebookPage = mFacebookPage.getText().toString();
                instagramPage = mInstagramPage.getText().toString();
                bio = mBio.getText().toString();

                if (mAllowShowDatebirth.isChecked()) {

                    allowShowMyBirthday = 1;

                } else {

                    allowShowMyBirthday = 0;
                }

                saveSettings();

                return true;
            }

            default: {

                break;
            }
        }

        return false;
    }

    public void saveSettings() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_SAVE_SETTINGS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.has("error")) {

                                if (!response.getBoolean("error")) {

                                    fullname = response.getString("fullname");
                                    location = response.getString("location");
                                    facebookPage = response.getString("fb_page");
                                    instagramPage = response.getString("instagram_page");
                                    bio = response.getString("status");
                                    String name = location;
                                    String address = "";
                                    Double branch = App.getInstance().getLat();
                                    Double email = App.getInstance().getLng();

                                    helpher = new DatabaseHelper(getActivity());
                                    helpher.insertIntoDB(name,address,branch,email);

                                    Toast.makeText(getActivity(), getText(R.string.msg_settings_saved), Toast.LENGTH_SHORT).show();

                                    App.getInstance().setFullname(fullname);

                                    Intent i = new Intent();
                                    i.putExtra("fullname", fullname);
                                    i.putExtra("location", location);
                                    i.putExtra("facebookPage", facebookPage);
                                    i.putExtra("instagramPage", instagramPage);
                                    i.putExtra("bio", bio);

                                    i.putExtra("sex", sex);

                                    i.putExtra("year", year);
                                    i.putExtra("month", month);
                                    i.putExtra("day", day);

                                    i.putExtra("relationshipStatus", relationshipStatus);
                                    i.putExtra("politicalViews", politicalViews);
                                    i.putExtra("worldView", worldView);
                                    i.putExtra("personalPriority", personalPriority);
                                    i.putExtra("importantInOthers", importantInOthers);
                                    i.putExtra("viewsOnSmoking", viewsOnSmoking);
                                    i.putExtra("viewsOnAlcohol", viewsOnAlcohol);
                                    i.putExtra("youLooking", youLooking);
                                    i.putExtra("youLike", youLike);
                                    i.putExtra("allowShowMyBirthday", allowShowMyBirthday);

                                    getActivity().setResult(RESULT_OK, i);

                                    getActivity().finish();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("fullname", fullname);
                params.put("location", location);
                params.put("facebookPage", facebookPage);
                params.put("instagramPage", instagramPage);
                params.put("bio", bio);
                params.put("sex", Integer.toString(sex));
                params.put("year", Integer.toString(year));
                params.put("month", Integer.toString(month));
                params.put("day", Integer.toString(day));

                params.put("iStatus", Integer.toString(relationshipStatus));
                params.put("politicalViews", Integer.toString(politicalViews));
                params.put("worldViews", Integer.toString(worldView));
                params.put("personalPriority", Integer.toString(personalPriority));
                params.put("importantInOthers", Integer.toString(importantInOthers));
                params.put("smokingViews", Integer.toString(viewsOnSmoking));
                params.put("alcoholViews", Integer.toString(viewsOnAlcohol));
                params.put("lookingViews", Integer.toString(youLooking));
                params.put("interestedViews", Integer.toString(youLike));

                params.put("allowShowMyBirthday", Integer.toString(allowShowMyBirthday));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}