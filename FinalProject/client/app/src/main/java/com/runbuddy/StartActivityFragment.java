package com.runbuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartActivityFragment extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    TimePicker timePicker;

    TextInputEditText distanceEditText;
    TextInputEditText dateEditText;
    Button submit;
    private RequestQueue _queue;
    private static final String FRAGMENT_TAG = "startActivity";
    private static final String SERVER_ADDRESS = "http://10.0.0.25:8080/";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StartActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartActivityFragment newInstance(String param1, String param2) {
        StartActivityFragment fragment = new StartActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_start_activity, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(v);
            }
        });

        timePicker = (TimePicker)view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String time = hour + ":" + minute;

        submit = (Button) view.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject requestObject = new JSONObject();

                try {
                    requestObject.put("email", LoginActivity.userName);
                    requestObject.put("distance", distanceEditText.toString());
                    requestObject.put("date", dateEditText.toString());
                    requestObject.put("time", time);

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,  SERVER_ADDRESS + "/create_activity",
                            requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(FRAGMENT_TAG, response.toString());
                            Toast.makeText(view.getContext(), "Activity saved! :)", Toast.LENGTH_SHORT).show();
                            Log.i(FRAGMENT_TAG,"activity saved");
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(FRAGMENT_TAG, "Failed to Log in - " + error);
                                    Toast.makeText(view.getContext(), "Sonthing went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                    _queue.add(req);
                }
                catch (JSONException e) {
                    Log.e(FRAGMENT_TAG, "error in submitting activity");
                    Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void openDatePickerDialog(final View v) {
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    switch (v.getId()) {
                        case R.id.dateEditText:
                            ((TextInputEditText) v).setText(selectedDate);
                            break;
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        datePickerDialog.show();
    }
}