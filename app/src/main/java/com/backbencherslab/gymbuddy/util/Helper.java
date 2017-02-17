package com.backbencherslab.gymbuddy.util;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.backbencherslab.gymbuddy.R;

public class Helper extends Application {

    private Activity activity;

    public Helper(Activity activity) {

        this.activity = activity;
    }

    public Helper() {

    }

    public boolean isValidEmail(String email) {

    	if (TextUtils.isEmpty(email)) {

    		return false;

    	} else {

    		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    	}
    }
    
    public boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }
    
    public boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public String getRelationshipStatus(int mRelationship) {

        switch (mRelationship) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.relationship_status_1);
            }

            case 2: {

                return activity.getString(R.string.relationship_status_2);
            }

            case 3: {

                return activity.getString(R.string.relationship_status_3);
            }

            case 4: {

                return activity.getString(R.string.relationship_status_4);
            }


            default: {

                break;
            }
        }

        return "-";
    }

    public String getPoliticalViews(int mPolitical) {

        switch (mPolitical) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.political_views_1);
            }

            case 2: {

                return activity.getString(R.string.political_views_2);
            }

            case 3: {

                return activity.getString(R.string.political_views_3);
            }

            case 4: {

                return activity.getString(R.string.political_views_4);
            }

            case 5: {

                return activity.getString(R.string.political_views_5);
            }

            case 6: {

                return activity.getString(R.string.political_views_6);
            }

            case 7: {

                return activity.getString(R.string.political_views_7);
            }


            default: {

                break;
            }
        }

        return "-";
    }

    public String getWorldView(int mWorld) {

        switch (mWorld) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.world_view_1);
            }

            case 2: {

                return activity.getString(R.string.world_view_2);
            }

            case 3: {

                return activity.getString(R.string.world_view_3);
            }

            case 4: {

                return activity.getString(R.string.world_view_4);
            }

            case 5: {

                return activity.getString(R.string.world_view_5);
            }

            case 6: {

                return activity.getString(R.string.world_view_6);
            }

            case 7: {

                return activity.getString(R.string.world_view_7);
            }

            case 8: {

                return activity.getString(R.string.world_view_8);
            }

            case 9: {

                return activity.getString(R.string.world_view_9);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getPersonalPriority(int mPriority) {

        switch (mPriority) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.personal_priority_1);
            }

            case 2: {

                return activity.getString(R.string.personal_priority_2);
            }

            case 3: {

                return activity.getString(R.string.personal_priority_3);
            }

            case 4: {

                return activity.getString(R.string.personal_priority_4);
            }

            case 5: {

                return activity.getString(R.string.personal_priority_5);
            }

            case 6: {

                return activity.getString(R.string.personal_priority_6);
            }

            case 7: {

                return activity.getString(R.string.personal_priority_7);
            }

            case 8: {

                return activity.getString(R.string.personal_priority_8);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getImportantInOthers(int mImportant) {

        switch (mImportant) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.important_in_others_1);
            }

            case 2: {

                return activity.getString(R.string.important_in_others_2);
            }

            case 3: {

                return activity.getString(R.string.important_in_others_3);
            }

            case 4: {

                return activity.getString(R.string.important_in_others_4);
            }

            case 5: {

                return activity.getString(R.string.important_in_others_5);
            }

            case 6: {

                return activity.getString(R.string.important_in_others_6);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getSmokingViews(int mSmoking) {

        switch (mSmoking) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.smoking_views_1);
            }

            case 2: {

                return activity.getString(R.string.smoking_views_2);
            }

            case 3: {

                return activity.getString(R.string.smoking_views_3);
            }

            case 4: {

                return activity.getString(R.string.smoking_views_4);
            }

            case 5: {

                return activity.getString(R.string.smoking_views_5);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getAlcoholViews(int mAlcohol) {

        switch (mAlcohol) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.alcohol_views_1);
            }

            case 2: {

                return activity.getString(R.string.alcohol_views_2);
            }

            case 3: {

                return activity.getString(R.string.alcohol_views_3);
            }

            case 4: {

                return activity.getString(R.string.alcohol_views_4);
            }

            case 5: {

                return activity.getString(R.string.alcohol_views_5);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getLooking(int mLooking) {

        switch (mLooking) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.you_looking_1);
            }

            case 2: {

                return activity.getString(R.string.you_looking_2);
            }

            case 3: {

                return activity.getString(R.string.you_looking_3);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getGenderLike(int mLike) {

        switch (mLike) {

            case 0: {

                return "-";
            }

            case 1: {

                return activity.getString(R.string.profile_like_1);
            }

            case 2: {

                return activity.getString(R.string.profile_like_2);
            }

            default: {

                break;
            }
        }

        return "-";
    }
}
