package com.backbencherslab.gymbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pkmmte.view.CircularImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.constants.Constants;
import com.backbencherslab.gymbuddy.dialogs.GenderSelectDialog;
import com.backbencherslab.gymbuddy.dialogs.MsgImageChooseDialog;
import com.backbencherslab.gymbuddy.util.CustomRequest;
import com.backbencherslab.gymbuddy.util.Helper;

public class SignupFragment extends Fragment implements Constants {

    CallbackManager callbackManager;

    LoginButton loginButton;

    private ProgressDialog pDialog;

    LinearLayout mActionContainer;
    Button mSelectBirth, mSelectGender;

    EditText signupUsername, signupFullname, signupPassword, signupEmail;
    Button signupJoinHowBtn;
    TextView mLabelTerms, mRegularSignup, mLabelAuthorizationViaFacebook;
    CircularImageView mAddPhoto;

    private String selectedPhotoImg = "";
    private Uri selectedImage;
    private Uri outputFileUri;

    private String username, password, email, language, fullname, photo = "";
    String facebookId = "", facebookName = "", facebookEmail = "";
    private int sex, year = 2000, month = 1, day = 1;

    private Boolean loading = false;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (AccessToken.getCurrentAccessToken() != null) LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();

        Intent i = getActivity().getIntent();
        facebookId = i.getStringExtra("facebookId");
        facebookName = i.getStringExtra("facebookName");
        facebookEmail = i.getStringExtra("facebookEmail");

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        if (loading) {
            showpDialog();
        }

        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends, email");

        if (!FACEBOOK_AUTHORIZATION) {

            loginButton.setVisibility(View.GONE);
        }

        mActionContainer = (LinearLayout) rootView.findViewById(R.id.actionContainer);

        mAddPhoto = (CircularImageView) rootView.findViewById(R.id.signupPhoto);

        signupUsername = (EditText) rootView.findViewById(R.id.signupUsername);
        signupFullname = (EditText) rootView.findViewById(R.id.signupFullname);
        signupPassword = (EditText) rootView.findViewById(R.id.signupPassword);
        signupEmail = (EditText) rootView.findViewById(R.id.signupEmail);

        mSelectGender = (Button) rootView.findViewById(R.id.selectGender);
        mSelectBirth = (Button) rootView.findViewById(R.id.selectBirth);

        mLabelTerms = (TextView) rootView.findViewById(R.id.SignupLabelTerms);

        mLabelTerms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", METHOD_APP_TERMS);
                i.putExtra("title", getText(R.string.signup_label_terms_and_policies));
                startActivity(i);
            }
        });

        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceImage();
            }
        });

        mLabelAuthorizationViaFacebook = (TextView) rootView.findViewById(R.id.labelAuthorizationViaFacebook);

        mRegularSignup = (TextView) rootView.findViewById(R.id.regularSignup);

        mRegularSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                facebookId = "";
                facebookName = "";
                facebookEmail = "";

                loginButton.setVisibility(View.VISIBLE);

                mActionContainer.setVisibility(View.GONE);
            }
        });

        if (facebookId != null && !facebookId.equals("")) {
            loginButton.setVisibility(View.GONE);
            mActionContainer.setVisibility(View.VISIBLE);
        } else {
            mActionContainer.setVisibility(View.GONE);
        }

        if (facebookId == null) {
            facebookId = "";
        }

        if (facebookEmail != null && !facebookEmail.equals("")) {
            signupEmail.setText(facebookEmail);
        }

        signupUsername.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (App.getInstance().isConnected() && checkUsername()) {

                    CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_APP_CHECKUSERNAME, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {

                                        if (response.getBoolean("error")) {

                                            signupUsername.setError(getString(R.string.error_login_taken));
                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", username);

                            return params;
                        }
                    };

                    App.getInstance().addToRequestQueue(jsonReq);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        signupFullname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkFullname();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        signupPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkPassword();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        signupEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkEmail();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        signupJoinHowBtn = (Button) rootView.findViewById(R.id.signupJoinHowBtn);

        signupJoinHowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_signup();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (App.getInstance().isConnected()) {

                            loading = true;

                            showpDialog();

                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {

                                            // Application code

                                            try {

                                                facebookId = object.getString("id");
                                                facebookName = object.getString("name");

                                                if (object.has("email")) {

                                                    facebookEmail = object.getString("email");
                                                }

                                            } catch (Throwable t) {

                                                Log.e("Profile", "Could not parse malformed JSON: \"" + object.toString() + "\"");

                                            } finally {

                                                if (AccessToken.getCurrentAccessToken() != null)
                                                    LoginManager.getInstance().logOut();

                                                Log.d("Profile", object.toString());

                                                if (App.getInstance().isConnected()) {

                                                    if (!facebookId.equals("")) {

                                                        signinByFacebookId();

                                                    } else {

                                                        loading = false;

                                                        hidepDialog();
                                                    }

                                                } else {

                                                    loading = false;

                                                    hidepDialog();
                                                }
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });

        mSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectGender(sex);
            }
        });

        mSelectBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
                dpd.getDatePicker().setMaxDate(new Date().getTime());

                dpd.show();
            }
        });

        int mMonth1 = month + 1;

        mSelectBirth.setText(getString(R.string.action_select_birth) + ": " + new StringBuilder().append(day).append("/").append(mMonth1).append("/").append(year));

        getGender(sex);

        if (selectedPhotoImg != null && selectedPhotoImg.length() > 0) {

            mAddPhoto.setImageURI(Uri.fromFile(new File(selectedPhotoImg)));
        }

        // Inflate the layout for this fragment
        return rootView;
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

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO_IMG && resultCode == getActivity().RESULT_OK && null != data) {

            selectedImage = data.getData();

            selectedPhotoImg = getImageUrlWithAuthority(getActivity(), selectedImage, "photo.jpg");

            try {

                selectedPhotoImg = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                mAddPhoto.setImageURI(null);
                mAddPhoto.setImageURI(Uri.fromFile(new File(selectedPhotoImg)));
            } catch (Exception e) {

                if (selectedPhotoImg != null && selectedPhotoImg.length() > 0) {

                    mAddPhoto.setImageURI(Uri.fromFile(new File(selectedPhotoImg)));
                }

                Log.e("OnSelectPhotoImage", e.getMessage());
            }

        } else if (requestCode == CREATE_PHOTO_IMG && resultCode == getActivity().RESULT_OK) {

            try {

                selectedPhotoImg = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                mAddPhoto.setImageURI(null);
                mAddPhoto.setImageURI(Uri.fromFile(new File(selectedPhotoImg)));
            } catch (Exception ex) {

                mAddPhoto.setImageURI(Uri.fromFile(new File(selectedPhotoImg)));

                Log.v("OnCameraCallBack", ex.getMessage());
            }

        }
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri, String fileName) {
        InputStream is = null;

        if (uri.getAuthority() != null) {

            try {

                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);

                return writeToTempImageAndGetPathUri(context, bmp, fileName).toString();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (is != null) {

                        is.close();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage, String fileName) {
        String file_path = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER;
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);

        try {

            FileOutputStream fos = new FileOutputStream(file);

            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {

            Toast.makeText(inContext, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";
    }

    public void choiceImage() {
        android.app.FragmentManager fm = getActivity().getFragmentManager();

        MsgImageChooseDialog alert = new MsgImageChooseDialog();

        alert.show(fm, "alert_dialog_image_choose");
    }

    public void imageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.label_select_img)), SELECT_PHOTO_IMG);
    }

    public void imageFromCamera() {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), APP_TEMP_FOLDER);

            if (!root.exists()) {

                root.mkdirs();
            }

            File sdImageMainDirectory = new File(root, "photo.jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, CREATE_PHOTO_IMG);
        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectGender(int position) {
        android.app.FragmentManager fm = getActivity().getFragmentManager();

        GenderSelectDialog alert = new GenderSelectDialog();

        Bundle b = new Bundle();
        b.putInt("position", position);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_select_gender");
    }

    public void getGender(int mSex) {
        sex = mSex;

        if (mSex == 0) {
            mSelectGender.setText(getString(R.string.label_sex_male));
        } else {
            mSelectGender.setText(getString(R.string.label_sex_female));
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
            year = mYear;
            month = monthOfYear;
            day = dayOfMonth;

            int mMonth1 = month + 1;

            mSelectBirth.setText(getString(R.string.action_select_birth) + ": " + new StringBuilder().append(day).append("/").append(mMonth1).append("/").append(year));

        }
    };

    public void signinByFacebookId() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_LOGINBYFACEBOOK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (App.getInstance().authorize(response)) {

                            if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

                                App.getInstance().updateGeoLocation();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                if (App.getInstance().getState() == ACCOUNT_STATE_BLOCKED) {

                                    App.getInstance().logout();
                                    Toast.makeText(getActivity(), getText(R.string.msg_account_blocked), Toast.LENGTH_SHORT).show();

                                } else {

                                    App.getInstance().updateGeoLocation();

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }

                        } else {

                            if (facebookId != "") {

                                loginButton.setVisibility(View.GONE);

                                mActionContainer.setVisibility(View.VISIBLE);

                                if (facebookEmail != null && !facebookEmail.equals("")) {

                                    signupEmail.setText(facebookEmail);
                                }
                            }
                        }

                        loading = false;

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("facebookId", facebookId);
                params.put("clientId", CLIENT_ID);
                params.put("gcm_regId", App.getInstance().getGcmToken());

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public Boolean checkUsername() {
        username = signupUsername.getText().toString();

        Helper helper = new Helper();

        if (username.length() == 0) {

            signupUsername.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (username.length() < 5) {

            signupUsername.setError(getString(R.string.error_small_username));

            return false;
        }

        if (!helper.isValidLogin(username)) {

            signupUsername.setError(getString(R.string.error_wrong_format));

            return false;
        }

        signupUsername.setError(null);

        return true;
    }

    public Boolean checkFullname() {
        fullname = signupFullname.getText().toString();

        if (fullname.length() == 0) {

            signupFullname.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() < 2) {

            signupFullname.setError(getString(R.string.error_small_fullname));

            return false;
        }

        signupFullname.setError(null);

        return true;
    }

    public Boolean checkPassword() {
        password = signupPassword.getText().toString();

        Helper helper = new Helper();

        if (password.length() == 0) {

            signupPassword.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            signupPassword.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            signupPassword.setError(getString(R.string.error_wrong_format));

            return false;
        }

        signupPassword.setError(null);

        return true;
    }

    public Boolean checkEmail() {
        email = signupEmail.getText().toString();

        Helper helper = new Helper();

        if (email.length() == 0) {

            signupEmail.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            signupEmail.setError(getString(R.string.error_wrong_format));

            return false;
        }

        signupEmail.setError(null);

        return true;
    }

    public Boolean verifyRegForm() {
        signupUsername.setError(null);
        signupFullname.setError(null);
        signupPassword.setError(null);
        signupEmail.setError(null);

        Helper helper = new Helper();

        if (username.length() == 0) {

            signupUsername.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (username.length() < 5) {

            signupUsername.setError(getString(R.string.error_small_username));

            return false;
        }

        if (!helper.isValidLogin(username)) {

            signupUsername.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (fullname.length() == 0) {

            signupFullname.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() < 2) {

            signupFullname.setError(getString(R.string.error_small_fullname));

            return false;
        }

        if (password.length() == 0) {

            signupPassword.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            signupPassword.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            signupPassword.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (email.length() == 0) {

            signupEmail.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            signupEmail.setError(getString(R.string.error_wrong_format));

            return false;
        }

        return true;
    }

    public void check_signup() {
        username = signupUsername.getText().toString();
        fullname = signupFullname.getText().toString();
        password = signupPassword.getText().toString();
        email = signupEmail.getText().toString();
        language = Locale.getDefault().getLanguage();

        if (verifyRegForm()) {

            if (selectedPhotoImg.length() != 0) {

                loading = true;

                showpDialog();

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "photo.jpg");

                uploadFile(METHOD_ACCOUNT_UPLOADPHOTO, f);

            } else {

                loading = true;

                showpDialog();

                signup();
            }
        }
    }

    public void signup() {
        if (App.getInstance().isConnected()) {

            loading = true;

            showpDialog();

            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_SIGNUP, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");

                            if (App.getInstance().authorize(response)) {

                                Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");

                                App.getInstance().updateGeoLocation();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                switch (App.getInstance().getErrorCode()) {

                                    case 300: {

                                        signupUsername.setError(getString(R.string.error_login_taken));
                                        break;
                                    }

                                    case 301: {

                                        signupEmail.setError(getString(R.string.error_email_taken));
                                        break;
                                    }

                                    default: {

                                        Log.e("Profile", "Could not parse malformed JSON: \"" + response.toString() + "\"");
                                        break;
                                    }
                                }
                            }

                            loading = false;

                            hidepDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                    loading = false;

                    hidepDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("fullname", fullname);
                    params.put("password", password);
                    params.put("photo", photo);
                    params.put("email", email);
                    params.put("language", language);
                    params.put("facebookId", facebookId);
                    params.put("sex", Integer.toString(sex));
                    params.put("year", Integer.toString(year));
                    params.put("month", Integer.toString(month));
                    params.put("day", Integer.toString(day));
                    params.put("clientId", CLIENT_ID);
                    params.put("gcm_regId", App.getInstance().getGcmToken());

                    return params;
                }
            };

            App.getInstance().addToRequestQueue(jsonReq);

        } else {

            Toast.makeText(getActivity(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean uploadFile(String serverURL, File file) {
        final OkHttpClient client = new OkHttpClient();

        try {

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("text/csv"), file))
                    .addFormDataPart("accountId", Long.toString(App.getInstance().getId()))
                    .addFormDataPart("accessToken", App.getInstance().getAccessToken())
                    .build();

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(serverURL)
                    .addHeader("Accept", "application/json;")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(com.squareup.okhttp.Request request, IOException e) {

                    loading = false;

                    hidepDialog();

                    Log.e("failure", request.toString());
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                    String jsonData = response.body().string();

                    Log.e("response", jsonData);

                    try {

                        JSONObject result = new JSONObject(jsonData);

                        if (!result.getBoolean("error")) {

                            photo = result.getString("lowPhotoUrl");
                        }

                        Log.d("My App", response.toString());

                    } catch (Throwable t) {

                        Log.e("My App", "Could not parse malformed JSON: \"" + t.getMessage() + "\"");

                    } finally {

                        signup();
                    }

                }
            });

            return true;

        } catch (Exception ex) {
            // Handle the error

            loading = false;

            hidepDialog();
        }

        return false;
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