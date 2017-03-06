package com.backbencherslab.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.common.ActivityBase;
import com.backbencherslab.gymbuddy.dialogs.CoverChooseDialog;
import com.backbencherslab.gymbuddy.dialogs.FriendRequestActionDialog;
import com.backbencherslab.gymbuddy.dialogs.MyPhotoActionDialog;
import com.backbencherslab.gymbuddy.dialogs.PeopleNearbySettingsDialog;
import com.backbencherslab.gymbuddy.dialogs.PhotoChooseDialog;
import com.backbencherslab.gymbuddy.dialogs.PhotoDeleteDialog;
import com.backbencherslab.gymbuddy.dialogs.ProfileBlockDialog;
import com.backbencherslab.gymbuddy.dialogs.ProfileReportDialog;
import com.backbencherslab.gymbuddy.dialogs.SearchSettingsDialog;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

public class MainActivity extends ActivityBase implements FragmentDrawer.FragmentDrawerListener, PhotoChooseDialog.AlertPositiveListener, CoverChooseDialog.AlertPositiveListener, ProfileReportDialog.AlertPositiveListener, ProfileBlockDialog.AlertPositiveListener, PhotoDeleteDialog.AlertPositiveListener, MyPhotoActionDialog.AlertPositiveListener, FriendRequestActionDialog.AlertPositiveListener, SearchSettingsDialog.AlertPositiveListener, PeopleNearbySettingsDialog.AlertPositiveListener {

    Toolbar mToolbar;
    FragmentManager fragmentManager;

    private FragmentDrawer drawerFragment;

    private CharSequence mTitle;

    LinearLayout mContainerAdmob;
    FragmentTransaction fragmentTransaction;

    Fragment fragment;
    Boolean action = false;
    int page = 0;

    private Boolean restore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        if (savedInstanceState != null) {

            //Restore the fragment's instance
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

            restore = savedInstanceState.getBoolean("restore");
            mTitle = savedInstanceState.getString("mTitle");

        } else {

            fragment = new Fragment();

            restore = false;
            mTitle = getString(R.string.app_name);
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mTitle);

        drawerFragment = (FragmentDrawer) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if (!restore) {

            displayView(1);
        }


        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.selectTab(1);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_black_24dp, getString(R.string.tab1)).setActiveColorResource(R.color.dark_blue))
                .addItem(new BottomNavigationItem(R.drawable.ic_map_black_24dp, getString(R.string.tab2)).setActiveColorResource(R.color.dark_blue))
                .addItem(new BottomNavigationItem(R.drawable.ic_person_add_black_24dp, getString(R.string.findfriends)).setActiveColorResource(R.color.dark_blue))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mToolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setTitle(mTitle);

                drawerFragment = (FragmentDrawer) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (position) {
                    case 0:
                        fragment = NotificationsFragment.newInstance();
                        fragmentTransaction.replace(R.id.container_body, fragment).commit();
                        getSupportActionBar().setTitle(R.string.page_5);
                        break;
                    case 1:
                        fragment = com.backbencherslab.gymbuddy.maps.MainActivity.newInstance();
                        fragmentTransaction.replace(R.id.container_body, fragment).commit();
                        getSupportActionBar().setTitle(R.string.tab2);
                        break;
                    case 2:
                        displayView(9);

                        break;
                }}

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void invite() {

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        String appLinkUrl, previewImageUrl;
        appLinkUrl = "https://play.google.com/store/apps/details?id=com.backbencherslab.gymbuddy";
        previewImageUrl = "http://gymbuddy.org/appinvite.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    @Override
    public void onChangeDistance(int position) {

        PeopleNearbyFragment p = (PeopleNearbyFragment) fragment;
        p.onChangeDistance(position);
    }

    @Override
    public void onCloseSettingsDialog(int searchGender, int searchOnline, int searchAgeFrom, int searchAgeTo) {

        SearchFragment p = (SearchFragment) fragment;
        p.onCloseSettingsDialog(searchGender, searchOnline, searchAgeFrom, searchAgeTo);
    }

    @Override
    public void onAcceptRequest(int position) {

        NotificationsFragment p = (NotificationsFragment) fragment;
        p.onAcceptRequest(position);
    }

    @Override
    public void onRejectRequest(int position) {

        NotificationsFragment p = (NotificationsFragment) fragment;
        p.onRejectRequest(position);
    }

    @Override
    public void onPhotoDelete(int position) {

        GalleryFragment p = (GalleryFragment) fragment;
        p.onPhotoDelete(position);
    }

    @Override
    public void onPhotoRemoveDialog(int position) {

        GalleryFragment p = (GalleryFragment) fragment;
        p.onPhotoRemove(position);
    }

    @Override
    public void onPhotoFromGallery() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.photoFromGallery();
    }

    @Override
    public void onPhotoFromCamera() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.photoFromCamera();
    }

    @Override
    public void onCoverFromGallery() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.coverFromGallery();
    }

    @Override
    public void onCoverFromCamera() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.coverFromCamera();
    }

    @Override
    public void onProfileReport(int position) {

        ProfileFragment p = (ProfileFragment) fragment;
        p.onProfileReport(position);
    }

    @Override
    public void onProfileBlock() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.onProfileBlock();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }

    private void displayView(int position) {

        action = false;

        switch (position) {

            case 0: {

                break;
            }

            case 1: {

                page = 1;

                fragment = new ProfileFragment();
                getSupportActionBar().setTitle(R.string.page_1);

                action = true;

                break;
            }

            case 2: {

                page = 2;

                fragment = new GalleryFragment();
                getSupportActionBar().setTitle(R.string.page_2);

                action = true;

                break;
            }

            case 3: {

                page = 3;

                fragment = new FriendsFragment();
                getSupportActionBar().setTitle(R.string.page_3);
                action = true;

                break;
            }

            case 4: {

                page = 4;

                fragment = new DialogsFragment();
                getSupportActionBar().setTitle(R.string.page_4);

                action = true;

                break;
            }

            case 5: {

                page = 5;

                fragment = new NotificationsFragment();
                getSupportActionBar().setTitle(R.string.page_5);

                action = true;

                break;
            }

            case 6: {

                page = 6;
                fragment = new GuestsFragment();
                getSupportActionBar().setTitle(R.string.page_6);

                action = true;

                break;
            }

            case 7: {

                page = 7;

                fragment = new UpgradesFragment();
                getSupportActionBar().setTitle(R.string.page_9);

                action = true;

                break;
            }

            case 8: {

                page = 8;

                fragment = new PeopleNearbyFragment();
                getSupportActionBar().setTitle(R.string.page_10);

                action = true;

                break;
            }

            case 9: {

                page = 9;

                // Upgrade

                fragment = new SearchFragment();
                getSupportActionBar().setTitle("");

                action = true;

                break;
            }
            case 10: {

                page = 10;

                // Upgrade

                invite();
                action = true;

                break;}
            default: {

                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        }

        if (action) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, fragment)
                    .commit();
        }
    }

    public void hideAds() {

        if (App.getInstance().getAdmob() == ADMOB_DISABLED) {

            mContainerAdmob.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home: {

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerFragment.isDrawerOpen()) {

            drawerFragment.closeDrawer();

        } else {

            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {

        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
