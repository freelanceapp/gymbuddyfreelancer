package com.backbencherslab.gymbuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.constants.Constants;
import com.backbencherslab.gymbuddy.util.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public static final int RESULT_OK = -1;

  static   List<DatabaseModel> dbList;
    static  Context context;
static    DatabaseHelper helpher;
  static   int position;
    final    String location = "";

    RecyclerAdapter(Context context, List<DatabaseModel> dbList ){

        this.dbList = new ArrayList<DatabaseModel>();
        this.context = context;
        this.dbList = dbList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(dbList.get(position).getName());
        holder.address.setText(dbList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {

        public TextView name,address;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = (TextView) itemLayoutView
                    .findViewById(R.id.rvname);
            address = (TextView)itemLayoutView.findViewById(R.id.rvemail);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }



        @Override
        public void onClick(View v) {


            helpher = new DatabaseHelper(context);
            dbList= new ArrayList<DatabaseModel>();
            dbList = helpher.getDataFromDB();


            if(dbList.size()>0) {


                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(
                        "geo:" + dbList.get(getAdapterPosition()).getBranch() +
                                "," + dbList.get(getAdapterPosition()).getEmail() +
                                "?q=" + dbList.get(getAdapterPosition()).getBranch() +
                                "," + dbList.get(getAdapterPosition()).getEmail() +
                                "(" + dbList.get(getAdapterPosition()).getName() + ")"));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to remove this gym")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                            helpher = new DatabaseHelper(context);
                            dbList= new ArrayList<DatabaseModel>();
                            dbList = helpher.getDataFromDB();

                            if(dbList.size()>0) {
                                        helpher.deleteARow(dbList.get(getAdapterPosition()).getName());
                                        saveSettings();
                                        Intent intent1 = new Intent(context, SecondActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent1);
                                }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        public void saveSettings() {


            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constants.METHOD_ACCOUNT_SAVE_SETTINGS, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                if (response.has("error")) {
                                  String location = "";

                                    if (!response.getBoolean("error")) {
                                        location = response.getString("location");

                                        Toast.makeText(context, context.getText(R.string.msg_settings_saved), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent();
                                        i.putExtra("location", location);


                                    }
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();

                            } finally {


                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    String location = "";

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", Long.toString(App.getInstance().getId()));
                    params.put("accessToken", App.getInstance().getAccessToken());
                    params.put("location", location);

                    return params;
                }
            };

            App.getInstance().addToRequestQueue(jsonReq);
        }


    }

}