package com.example.hp.jbooks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity {
    TextInputEditText login;
    TextInputLayout loginInputLayout;
    private String TAG = "network test";
    FloatingActionButton signin_btn;
    TextView signup;
    private Boolean loggedIn = true;
    SharedPreferences mpreference;
    public static final String mypreference = "mypref";
    public static final String UserId = "userId";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLoggedIn())
            startActivity(new Intent(Login.this, Mainfile.class));
        setContentView(R.layout.activity_login);
        login = (TextInputEditText) findViewById(R.id.login);
        loginInputLayout = (TextInputLayout) findViewById(R.id.loginTextLayout);
        signin_btn = (FloatingActionButton) findViewById(R.id.signin_btn);
        signup = (TextView) findViewById(R.id.signup);

        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(login.getText().toString().trim()).matches()) {
                    loginInputLayout.setError(getString(R.string.err_msg_email));
                    if (login.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                } else
                    loginInputLayout.setErrorEnabled(false);
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean allvalid = true;
                if (login.getText().toString().matches("")) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Fill The Email ID", Toast.LENGTH_SHORT).show();
                }
                if (allvalid && isOnline()) {
                    new SendPostRequest().execute(login.getText().toString());
                    //if else checking
                } else {
                    try {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });

                        alertDialog.show();
                    } catch (Exception e) {
                        Log.d(TAG, "Show Dialog: " + e.getMessage());
                    }
                }
                /*Intent intent=new Intent(Login.this,Mainfile.class);
                startActivity(intent);*/
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isLoggedIn() {
        mpreference = this.getSharedPreferences(getResources().getString(R.string.sharedPreferenceKey), Context.MODE_PRIVATE);
        if (!mpreference.contains(getResources().getString(R.string.preferenceLoggedInKey)) || !mpreference.getBoolean(getResources().getString(R.string.preferenceLoggedInKey), false))
            loggedIn = false;
        else
            loggedIn = true;
        return loggedIn;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //Login.super.onBackPressed();
                        moveTaskToBack(true);
                        Login.this.finish();
                    }
                }).create().show();
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("https://jbooks.000webhostapp.com/isactive.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", strings[0]);
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                startActivity(new Intent(Login.this, Login.class));
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                int code = (int) jsonObj.get("code");
                String message = (String) jsonObj.get("message");
                if (code == 1) {
                    String name = (String) jsonObj.get("name");
                    Toast.makeText(getApplicationContext(), "Successfully login By : " + name,
                            Toast.LENGTH_LONG).show();
                    mpreference = getSharedPreferences(getResources().getString(R.string.sharedPreferenceKey), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mpreference.edit();
                    editor.putString(Name, name);
                    editor.putString(Email, login.getText().toString());
                    editor.putBoolean(getResources().getString(R.string.preferenceLoggedInKey), true);
                    editor.commit();
                    Intent intent = new Intent(Login.this, Mainfile.class);
                    startActivity(intent);
                } else if (code == 0) {
                    Toast.makeText(getApplicationContext(), "Please visit the registered Mail ID and open the link",
                            Toast.LENGTH_LONG).show();
                } else if (code == -1) {
                    Toast.makeText(getApplicationContext(), "Ivalid Email Id Given Sign Up again !!!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid json response",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                startActivity(new Intent(Login.this, Login.class));
            }

            // typecasting obj to JSONObject
            //JSONObject jo = (JSONObject) obj;
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
