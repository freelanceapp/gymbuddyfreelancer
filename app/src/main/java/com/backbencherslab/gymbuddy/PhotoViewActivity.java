package com.backbencherslab.gymbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.common.ActivityBase;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends ActivityBase {

    private static final String TAG = "photo_view_activity";

    Toolbar toolbar;

    ImageView photoView;
    LinearLayout mContentScreen;
    RelativeLayout mLoadingScreen;
    PhotoViewAttacher mAttacher;
    ImageLoader imageLoader;

    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Intent i = getIntent();

        imgUrl = i.getStringExtra("imgUrl");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        mContentScreen = (LinearLayout) findViewById(R.id.PhotoViewContentScreen);
        mLoadingScreen = (RelativeLayout) findViewById(R.id.PhotoViewLoadingScreen);

        photoView = (ImageView) findViewById(R.id.photoImageView);

        getSupportActionBar().setTitle("Progress Photos");

        showLoadingScreen();

        imageLoader = App.getInstance().getImageLoader();

        imageLoader.get(imgUrl, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean isImmediate) {

                photoView.setImageBitmap(imageContainer.getBitmap());
                mAttacher = new PhotoViewAttacher(photoView);

                showContentScreen();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.share: {

                ShareDialog shareDialog;
                FacebookSdk.sdkInitialize(getApplicationContext());
                shareDialog = new ShareDialog(this);

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Gym Buddy - Progress Picture")
                        .setContentDescription(App.getInstance().getFullname())
                        .setContentUrl(Uri.parse(imgUrl))
                        .build();

                shareDialog.show(linkContent);

                return true;
            }

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void showLoadingScreen() {

        mContentScreen.setVisibility(View.GONE);
        mLoadingScreen.setVisibility(View.VISIBLE);
    }

    public void showContentScreen() {

        mLoadingScreen.setVisibility(View.GONE);
        mContentScreen.setVisibility(View.VISIBLE);
    }
}
