package com.backbencherslab.gymbuddy;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.backbencherslab.gymbuddy.adapter.PeopleListAdapter;
import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.constants.Constants;
import com.backbencherslab.gymbuddy.dialogs.SearchSettingsDialog;
import com.backbencherslab.gymbuddy.dialogs.SearchTextDialog;
import com.backbencherslab.gymbuddy.model.Profile;
import com.backbencherslab.gymbuddy.util.CustomRequest;
import com.backbencherslab.gymbuddy.util.Helper;

public class SearchFragment extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {
    private static final String STATE_LIST = "State Adapter Data";

    SearchView searchView = null;

    ListView mListView;
    TextView mMessage;
    ImageView mSplash;

    FloatingActionButton filterFAB;
    FloatingActionButton searchTextFAB;

    LinearLayout mHeaderContainer;

    SwipeRefreshLayout mItemsContainer;

    private ArrayList<Profile> itemsList;
    private PeopleListAdapter itemsAdapter;

    public String queryText, currentQuery, oldQuery;

    public int itemCount;
    private int userId = 0;

    private int search_gender = -1, search_online = -1, search_age_from = 18, search_age_to = 110, preload_gender = -1;
    private int search_workout_type = -1;
    private int search_fitness_goals = -1;
    private String search_text = "";

    private int itemId = 0;
    private int arrayLength = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean restore = false;
    private Boolean preload = true;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        if (savedInstanceState != null) {
            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new PeopleListAdapter(getActivity(), itemsList);
            currentQuery = queryText = savedInstanceState.getString("queryText");
            restore = savedInstanceState.getBoolean("restore");
            preload = savedInstanceState.getBoolean("preload");
            itemId = savedInstanceState.getInt("itemId");
            userId = savedInstanceState.getInt("userId");
            itemCount = savedInstanceState.getInt("itemCount");
            search_gender = savedInstanceState.getInt("search_gender");
            preload_gender = savedInstanceState.getInt("preload_gender");
        } else {
            itemsList = new ArrayList<Profile>();
            itemsAdapter = new PeopleListAdapter(getActivity(), itemsList);

            currentQuery = queryText = "";

            restore = false;
            preload = true;
            itemId = 0;
            userId = 0;
            itemCount = 0;
            search_gender = -1;
            preload_gender = -1;
        }

        mHeaderContainer = (LinearLayout) rootView.findViewById(R.id.container_header);

        filterFAB = (FloatingActionButton) rootView.findViewById(R.id.filterFAB);
        searchTextFAB = (FloatingActionButton) rootView.findViewById(R.id.searchTextFAB);

        mItemsContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mMessage = (TextView) rootView.findViewById(R.id.message);
        mSplash = (ImageView) rootView.findViewById(R.id.splash);

        mListView = (ListView) rootView.findViewById(R.id.listView);

        mListView.setAdapter(itemsAdapter);

        if (itemsAdapter.getCount() == 0) {
            showMessage(getText(R.string.label_empty_list).toString());
        } else {
            hideMessage();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Profile profile = (Profile) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", profile.getId());
                startActivity(intent);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                if ((lastInScreen == totalItemCount) && !(loadingMore) && (viewMore) && !(mItemsContainer.isRefreshing())) {

                    if (preload) {
                        loadingMore = true;
                        preload();
                    } else {
                        currentQuery = getCurrentQuery();

                        if (currentQuery.equals(oldQuery)) {
                            loadingMore = true;
                            search2();
                        }
                    }
                }
            }
        });

        if (queryText.length() == 0) {

            if (mListView.getAdapter().getCount() == 0) {
                showMessage(getString(R.string.label_search_start_screen_msg));
            } else {
            /*   if (preload) {
                    mHeaderText.setVisibility(View.GONE);
                } else {
                    mHeaderText.setVisibility(View.VISIBLE);
                    mHeaderText.setText(getText(R.string.label_search_results) + " " + Integer.toString(itemCount));
                }
*/
                hideMessage();
            }

        } else {

            if (mListView.getAdapter().getCount() == 0) {
                showMessage(getString(R.string.label_search_results_error));
            } else {
                hideMessage();
            }
        }

        filterFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Getting the fragment manager */
                android.app.FragmentManager fm = getActivity().getFragmentManager();

                /** Instantiating the DialogFragment class */
                SearchSettingsDialog alert = new SearchSettingsDialog();

                /** Creating a bundle object to store the selected item's index */
                Bundle b = new Bundle();

                /** Storing the selected item's index in the bundle object */
                b.putInt("searchGender", search_gender);
                b.putInt("searchOnline", search_online);
                b.putInt("searchAgeFrom", search_age_from);
                b.putInt("searchAgeTo", search_age_to);
                b.putInt("searchWorkoutType", search_workout_type);
                b.putInt("searchFitnessGoals", search_fitness_goals);

                /** Setting the bundle object to the dialog fragment object */
                alert.setArguments(b);

                /** Creating the dialog fragment object, which will in turn open the alert dialog window */
                alert.show(fm, "alert_dialog_search_settings");

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        searchTextFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Getting the fragment manager */
                android.app.FragmentManager fm = getActivity().getFragmentManager();

                /** Instantiating the DialogFragment class */
                SearchTextDialog alert = new SearchTextDialog();

                /** Creating a bundle object to store the selected item's index */
                Bundle b = new Bundle();

                /** Storing the selected item's index in the bundle object */
                b.putString("searchText", search_text);

                /** Setting the bundle object to the dialog fragment object */
                alert.setArguments(b);

                /** Creating the dialog fragment object, which will in turn open the alert dialog window */

                alert.show(fm, "alert_dialog_search_text");

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        if (!restore) {
            if (preload) {
                preload();
            }
        }

        return rootView;
    }

    public void onCloseSettingsDialog(int searchGender, int searchOnline, int searchAgeFrom, int searchAgeTo, String searchWorkoutType, String searchFitnessGoals) {
        if (searchAgeFrom < 0) {
            searchAgeFrom = 13;
        }

        if (searchAgeTo < searchAgeFrom) {
            searchAgeTo = 110;
        }

        search_gender = searchGender;
        search_online = searchOnline;
        search_age_from = searchAgeFrom;
        search_age_to = searchAgeTo;
        search_workout_type = Helper.getWorkoutTypeInt(searchWorkoutType);
        search_fitness_goals = Helper.getFitnessGoalsInt(searchFitnessGoals);

        String q = getCurrentQuery();

        if (preload) {
            itemId = 0;
            preload();
        } else {
            if (q.length() > 0) {
                searchStart();
            }
        }
    }

    public void onCloseSearchTextDialog(String searchText) {
        this.search_text = searchText;
        Toast.makeText(getActivity(), "the search text: " + search_text, Toast.LENGTH_LONG);
    }

    @Override
    public void onRefresh() {
        currentQuery = queryText;
        currentQuery = currentQuery.trim();
        if (App.getInstance().isConnected() && currentQuery.length() != 0) {
            userId = 0;
            search2();
        } else {
            mItemsContainer.setRefreshing(false);
        }
    }

    public String getCurrentQuery() {
        String searchText = searchView.getQuery().toString();
        searchText = searchText.trim();
        return searchText;
    }

    public void searchStart() {
        preload = false;
        currentQuery = getCurrentQuery();
        if (App.getInstance().isConnected()) {
            userId = 0;
            search2();
        } else {
            Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("queryText", queryText);
        outState.putBoolean("restore", true);
        outState.putBoolean("preload", preload);
        outState.putInt("itemId", itemId);
        outState.putInt("userId", userId);
        outState.putInt("itemCount", itemCount);
        outState.putInt("search_gender", search_gender);
        outState.putInt("preload_gender", preload_gender);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.options_menu_main_search);


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        }

        if (searchView != null) {
            searchView.setQuery(queryText, false);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setIconified(false);

            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchAutoComplete.setHint(getText(R.string.placeholder_search));
            searchAutoComplete.setHintTextColor(getResources().getColor(R.color.white));

            searchView.clearFocus();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {

                    queryText = newText;

                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

                    queryText = query;
                    searchStart();

                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

//    public void search() {
//        mItemsContainer.setRefreshing(true);
//
//        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_APP_SEARCH, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (!loadingMore) {
//                                itemsList.clear();
//                            }
//
//                            arrayLength = 0;
//
//                            if (!response.getBoolean("error")) {
//
//                                itemCount = response.getInt("itemCount");
//                                oldQuery = response.getString("query");
//                                userId = response.getInt("userId");
//
//                                if (response.has("users")) {
//
//                                    JSONArray usersArray = response.getJSONArray("users");
//
//                                    arrayLength = usersArray.length();
//
//                                    if (arrayLength > 0) {
//
//                                        for (int i = 0; i < usersArray.length(); i++) {
//
//                                            JSONObject profileObj = (JSONObject) usersArray.get(i);
//
//                                            Profile profile = new Profile(profileObj);
//
//                                            itemsList.add(profile);
//                                        }
//                                    }
//                                }
//                            }
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//
//                        } finally {
//
//                            loadingComplete();
//
//                            Log.e("response", response.toString());
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                loadingComplete();
//                Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("accountId", Long.toString(App.getInstance().getId()));
//                params.put("accessToken", App.getInstance().getAccessToken());
//                params.put("query", currentQuery);
//                params.put("userId", Integer.toString(userId));
//                params.put("gender", Integer.toString(search_gender));
//                params.put("online", Integer.toString(search_online));
//                params.put("ageFrom", Integer.toString(search_age_from));
//                params.put("ageTo", Integer.toString(search_age_to));
//                return params;
//            }
//        };
//
//        App.getInstance().addToRequestQueue(jsonReq);
//    }

    public void search2() {
        mItemsContainer.setRefreshing(true);
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_APP_SEARCH2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!loadingMore) {
                                itemsList.clear();
                            }
                            arrayLength = 0;
                            if (!response.getBoolean("error")) {
                                itemCount = response.getInt("itemCount");
                                oldQuery = response.getString("query");
                                userId = response.getInt("userId");
                                if (response.has("users")) {
                                    JSONArray usersArray = response.getJSONArray("users");
                                    arrayLength = usersArray.length();
                                    if (arrayLength > 0) {
                                        for (int i = 0; i < usersArray.length(); i++) {
                                            JSONObject profileObj = (JSONObject) usersArray.get(i);
                                            Profile profile = new Profile(profileObj);
                                            itemsList.add(profile);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            loadingComplete();
                            Log.e("response", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingComplete();
                Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("query", currentQuery);
                params.put("userId", Integer.toString(userId));
                params.put("gender", Integer.toString(search_gender));
                params.put("online", Integer.toString(search_online));
                params.put("ageFrom", Integer.toString(search_age_from));
                params.put("ageTo", Integer.toString(search_age_to));
                params.put("workoutType", Integer.toString(search_workout_type));
                params.put("fitnessGoals", Integer.toString(search_fitness_goals));
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void preload() {
        if (preload) {
            mItemsContainer.setRefreshing(true);

            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_APP_SEARCH_PRELOAD2, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!loadingMore) {
                                    itemsList.clear();
                                }
                                arrayLength = 0;
                                if (!response.getBoolean("error")) {
                                    itemId = response.getInt("itemId");
                                    if (response.has("items")) {
                                        JSONArray usersArray = response.getJSONArray("items");
                                        arrayLength = usersArray.length();
                                        if (arrayLength > 0) {
                                            for (int i = 0; i < usersArray.length(); i++) {
                                                JSONObject profileObj = (JSONObject) usersArray.get(i);
                                                Profile profile = new Profile(profileObj);
                                                itemsList.add(profile);
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                loadingComplete();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    loadingComplete();
                    Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", Long.toString(App.getInstance().getId()));
                    params.put("accessToken", App.getInstance().getAccessToken());
                    params.put("itemId", Integer.toString(itemId));
                    params.put("gender", Integer.toString(search_gender));
                    params.put("online", Integer.toString(search_online));
                    params.put("ageFrom", Integer.toString(search_age_from));
                    params.put("ageTo", Integer.toString(search_age_to));
                    params.put("workoutType", Integer.toString(search_workout_type));
                    params.put("fitnessGoals", Integer.toString(search_fitness_goals));
                    return params;
                }
            };
            App.getInstance().addToRequestQueue(jsonReq);
        }
    }

    public void loadingComplete() {
        if (arrayLength == LIST_ITEMS) {
            viewMore = true;
        } else {
            viewMore = false;
        }

        itemsAdapter.notifyDataSetChanged();

        loadingMore = false;

        mItemsContainer.setRefreshing(false);

        if (mListView.getAdapter().getCount() == 0) {
            showMessage(getString(R.string.label_search_results_error));
        } else {
            hideMessage();
            if (preload) {
                //mHeaderText.setVisibility(View.GONE);
            } else {
                //mHeaderText.setVisibility(View.VISIBLE);
                //mHeaderText.setText(getText(R.string.label_search_results) + " " + Integer.toString(itemCount));
            }
        }
    }

    public void showMessage(String message) {
        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
        mSplash.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {
        mMessage.setVisibility(View.GONE);
        mSplash.setVisibility(View.GONE);
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