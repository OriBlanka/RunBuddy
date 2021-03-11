package com.runbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    TextView login;
    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText pass;
    TextInputEditText rePass;
    Button done;

    private RequestQueue _queue;
    private static final String ACTIVITY_TAG = "signUp";
    private static final String SERVER_ADDRESS = "http://10.0.0.25:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (TextInputEditText)findViewById(R.id.firstNameEditText);
        lastName = (TextInputEditText)findViewById(R.id.lastNameEditText);
        email = (TextInputEditText)findViewById(R.id.emailEditText);
        pass = (TextInputEditText)findViewById(R.id.passwordEditText);
        rePass = (TextInputEditText)findViewById(R.id.repasswordEditText);

        login = (TextView) findViewById(R.id.loginTextView);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        done = (Button) findViewById(R.id.doneButton);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!chekFildes()){
                    Toast.makeText(SignUp.this, "Make sure all fields are filled and re-password is match to to the password", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject requestObject = new JSONObject();

                    try {
                        requestObject.put("email", email);
                        requestObject.put("password", pass);
                    }
                    catch (JSONException e) {
                        Log.e(ACTIVITY_TAG, "error in verifying exist user");
                        Toast.makeText(SignUp.this, "Please try again later :)", Toast.LENGTH_SHORT).show();
                    }

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,  SERVER_ADDRESS + email + "/check",
                            requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ACTIVITY_TAG, response.toString());
                            String match = response.toString();

                            if(match.equals("true")){
                                Log.i(ACTIVITY_TAG, "user is found");
                                Toast.makeText(SignUp.this, "The email exist in our system, try another one", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(SignUp.this, "Welcome, " + email, Toast.LENGTH_SHORT).show();
                                //TODO: change the intent name
                                Intent nextIntent = new Intent(SignUp.this, HomePage.class);
                                nextIntent.putExtra("username",email.getText().toString());
                                Log.i(ACTIVITY_TAG,"check "+ email.getText().toString());
                                LoginActivity.userName = email;
                                resetInputs();
                                startActivity(nextIntent);
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(ACTIVITY_TAG, "Failed to sign up - " + error);
                                }
                            });
                    _queue.add(req);
                }
            }
        });
    }

    private boolean chekFildes() {
        if (firstName == null){
            return false;
        }
        if (lastName == null){
            return false;
        }
        if (email == null){
            return false;
        }
        if (pass == null){
            return false;
        }
        if (rePass == null){
            return false;
        }
        if (pass.getText().toString().equals(rePass.getText().toString()) == false){
            return false;
        }
        return true;
    }

    /**********************************************************************
     This method resets the input to their original hints
     ***********************************************************************/
    private void resetInputs(){
        Log.i(ACTIVITY_TAG, "resetting inputs");
        firstName.setText(null);
        firstName.setHint("First name");
        lastName.setText(null);
        lastName.setHint("Last name");
        email.setText(null);
        email.setHint("Email");
        pass.setText(null);
        pass.setHint("Password");
        rePass.setText(null);
        rePass.setHint("Re-password");
    }
}