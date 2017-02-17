package com.backbencherslab.gymbuddy.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.backbencherslab.gymbuddy.app.App;

public class Wallet {
    private static final String PREFS = "com.fortumo.FortumoDemo.PREFS";
    private static final String CREDITS = "virtualcredits";

    public static int addCredits(Context context, int amount) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int currentCredits = prefs.getInt(CREDITS, 0);

        SharedPreferences.Editor editor = prefs.edit();
        currentCredits += amount;
        editor.putInt(CREDITS, currentCredits);
        editor.commit();

        App.getInstance().setBalance(App.getInstance().getBalance() + amount);

        App.getInstance().payment(amount);

        return currentCredits;
    }

    public static int getCredits(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(CREDITS, 0);
    }
}
