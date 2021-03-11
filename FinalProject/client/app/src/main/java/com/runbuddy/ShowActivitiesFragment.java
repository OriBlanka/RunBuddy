package com.runbuddy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowActivitiesFragment extends Fragment {

    RecyclerView rvGroup;
    ArrayList<String> arrayListGroup;
    LinearLayoutManager layoutManagerGroup;
    GroupAdp adapterGroup;
    private static final String FRAGMENT_TAG = "showActivity";
    private static final String SERVER_ADDRESS = "http://10.0.0.25:8080/";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowActivitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowActivitiesFragment newInstance(String param1, String param2) {
        ShowActivitiesFragment fragment = new ShowActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_activities, container, false);

        rvGroup = view.findViewById(R.id.rvGroup);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,SERVER_ADDRESS + "/all_activities",
                null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response){
                Log.d(FRAGMENT_TAG, response.toString());
                arrayListGroup = new ArrayList<>();
                try{
                    for (int i = 0; i < response.length(); i++) {
                        arrayListGroup.add(response.getJSONObject(i).getString("email"));
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
/*
        arrayListGroup = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            arrayListGroup.add("Group " + i);
        }
*/
        adapterGroup = new GroupAdp(this, arrayListGroup);
        layoutManagerGroup = new LinearLayoutManager(this.getContext());
        rvGroup.setLayoutManager(layoutManagerGroup);
        rvGroup.setAdapter(adapterGroup);

        return view;
    }
}