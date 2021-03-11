package com.runbuddy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder> {

    private Fragment activity;
    ArrayList<String> arrayListGroup;

    private static final String FRAGMENT_TAG = "showActivity-GroupAdp";
    private static final String SERVER_ADDRESS = "http://10.0.0.25:8080/";

    public GroupAdp(Fragment activity, ArrayList<String> arrayListGroup) {
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group, parent, false);
        return new GroupAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(arrayListGroup.get(position));

        ArrayList<String> arrayListActivity = new ArrayList<>();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,SERVER_ADDRESS + "/all_activities",
                null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response){
                Log.d(FRAGMENT_TAG, response.toString());
                arrayListGroup = new ArrayList<>();
                try{
                    for (int i = 0; i < response.length(); i++) {
                        arrayListGroup.add(response.getJSONObject(i).getString("distance"));
                        arrayListGroup.add(response.getJSONObject(i).getString("time:"));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Log.d(FRAGMENT_TAG, "error - " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(FRAGMENT_TAG, "Encountered error - " + error);
            }
        });

        ActivityAdp adapterActivity = new ActivityAdp(arrayListActivity);
        LinearLayoutManager LayoutManagerActivity = new LinearLayoutManager(activity.getContext());
        holder.rvActivity.setLayoutManager(LayoutManagerActivity);
        holder.rvActivity.setAdapter(adapterActivity);
    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        RecyclerView rvActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            rvActivity = itemView.findViewById(R.id.rvActivity);
        }
    }
}
