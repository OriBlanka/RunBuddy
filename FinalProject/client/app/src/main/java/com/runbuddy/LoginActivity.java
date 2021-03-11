package com.runbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    String token = "";
    public static TextInputEditText userName;
    TextInputEditText password;
    Button next;
    Button cancel;
    private RequestQueue _queue;
    private static final String ACTIVITY_TAG = "login";
    private static final String SERVER_ADDRESS = "http://10.0.0.25:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (TextInputEditText)findViewById(R.id.userNameEditText);
        password = (TextInputEditText)findViewById(R.id.repasswordEditText);
        next = (Button)findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUserIn(userName.getText().toString(), password.getText().toString());
            }
        });

        cancel = (Button)findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(cancelIntent);
            }
        });
    }

    /**********************************************************************
        This method checks the users login credentials and logs him
        in if he is in the system
    ***********************************************************************/
    private void LogUserIn(String email, String password){
        JSONObject requestObject = new JSONObject();

        try {
            requestObject.put("email", email);
            requestObject.put("password", password);
        }
        catch (JSONException e) {
            resetInputs();
            Log.e(ACTIVITY_TAG, "error in verifying user");
            Toast.makeText(LoginActivity.this, "Please re-enter user and pass", Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SERVER_ADDRESS + email + "/check",
                requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(ACTIVITY_TAG, response.toString());
                String match = response.toString();

                if (match.equals("true")){
                    Log.d(ACTIVITY_TAG, "user is found");
                    Toast.makeText(LoginActivity.this, "Welcome back, " + userName, Toast.LENGTH_SHORT).show();
                    //TODO: change the intent name
                    Intent nextIntent = new Intent(LoginActivity.this, HomePage.class);
                    nextIntent.putExtra("username",userName.getText().toString());
                    Log.i(ACTIVITY_TAG,"check "+ userName.getText().toString());
                    resetInputs();
                    sendToServer();
                    startActivity(nextIntent);
                } else{
                    Log.i(ACTIVITY_TAG, "user isn't found");
                    Toast.makeText(LoginActivity.this, "Wrong username / password", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(ACTIVITY_TAG, "Failed to Log in - " + error);
                    }
                });
        _queue.add(req);
    }


    /**********************************************************************
        This method resets the input to their original hints
    ***********************************************************************/
    private void resetInputs(){
        Log.i(ACTIVITY_TAG, "resetting inputs");
        userName.setText(null);
        userName.setHint("Username");
        password.setText(null);
        password.setHint("Password");
    }


    /**********************************************************************
        Send a stock's name to the server
    ***********************************************************************/
    private void sendToServer(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                Log.d(ACTIVITY_TAG,"user token " + token);
            }
        });
    }
}