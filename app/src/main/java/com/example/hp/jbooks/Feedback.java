package com.example.hp.jbooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Feedback extends AppCompatActivity {

    RatingBar star_rating;
    EditText feedback_text;
    TextView user_name;
    JsonObjectRequest jsonObjectRequest;
    String HTTP_JSON_URL_BASE = "https://jbooks.000webhostapp.com/feedback.php?";
    String HTTP_JSON_URL, userEmail, userName;
    float rating;
    String feedback, responeMessage;
    RequestQueue requestQueue;
    SharedPreferences mpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mpreferences = this.getSharedPreferences(getResources().getString(R.string.sharedPreferenceKey), Context.MODE_PRIVATE);

        userEmail = mpreferences.getString(getResources().getString(R.string.preferenceEmailKey), "");
        userName = mpreferences.getString(getResources().getString(R.string.preferenceNameKey), "");

        user_name = (TextView) findViewById(R.id.user_name);
        star_rating = (RatingBar) findViewById(R.id.star_rating);
        feedback_text = (EditText) findViewById(R.id.feedback_text);

        user_name.setText(userName + " (" + userEmail + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_feedback:
                rating = star_rating.getRating();
                try {
                    feedback = URLEncoder.encode(feedback_text.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HTTP_JSON_URL = HTTP_JSON_URL_BASE + "user_email=" + userEmail + "&"
                        + "star_rating=" + String.valueOf(rating) + "&"
                        + "feedback_text=" + feedback;
                JSON_DATA_WEB_CALL();
                Toast.makeText(this, responeMessage, Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void JSON_DATA_WEB_CALL() {

        jsonObjectRequest = new JsonObjectRequest(HTTP_JSON_URL,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonObjectRequest);
    }


    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONObject object) {
        responeMessage = null;
        try {
            responeMessage = object.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("response", object.toString());

    }
}
