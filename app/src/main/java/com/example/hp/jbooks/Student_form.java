package com.example.hp.jbooks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Student_form extends AppCompatActivity {

    EditText name, phn_no, email, parent_name, parent_no, parent_email, school_name, role;
    Spinner stream,yop;
    private String TAG = "network test";
    FloatingActionButton parent_submit_btn;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);
        name=(EditText)findViewById(R.id.name);
        phn_no=(EditText)findViewById(R.id.phn_no);
        email=(EditText)findViewById(R.id.email);
        parent_email=(EditText)findViewById(R.id.parent_email);
        parent_name=(EditText)findViewById(R.id.parent_name);
        parent_no=(EditText)findViewById(R.id.parent_no);
        school_name=(EditText)findViewById(R.id.school_name);
        stream=(Spinner) findViewById(R.id.stream);
        yop=(Spinner) findViewById(R.id.yop);
        parent_submit_btn= (FloatingActionButton) findViewById(R.id.parent_submit_btn);

        parent_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean allvalid = true;
                /*if ( parent_email.getText().toString().matches("") || parent_name.getText().toString().matches("") || name.getText().toString().matches("") || phn_no.getText().toString().matches("") || parent_no.getText().toString().matches("") || email.getText().toString().matches("") || school_name.getText().toString().matches("")) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                }*/
                if ((phn_no.getText().toString().length() < 7 || phn_no.getText().toString().length() > 13) || (parent_no.getText().toString().length() < 7 || parent_no.getText().toString().length() > 13)) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "check your Phone Number", Toast.LENGTH_SHORT).show();
                }
                if (!isValidEmail(email.getText()) && !isValidEmail(parent_email.getText())) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "check your email", Toast.LENGTH_SHORT).show();
                }
                if (yop.getSelectedItem().toString().length() != 4) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "check your Year of passing", Toast.LENGTH_SHORT).show();
                }
                if (stream.getSelectedItemPosition() == 0) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "choose a Stream", Toast.LENGTH_SHORT).show();
                }
                if (allvalid && isOnline()) {
                    Toast.makeText(getApplicationContext(), "Succesfully login by:" + name.getText().toString(), Toast.LENGTH_SHORT).show();
                    String role_post = "parent";
                    String name_post = name.getText().toString();
                    String email_post = email.getText().toString();
                    String phnno_post = phn_no.getText().toString();
                    String parentname_post = parent_name.getText().toString();
                    String parentemail_post = parent_email.getText().toString();
                    String stream_post = stream.getSelectedItem().toString();
                    String yop_post = yop.getSelectedItem().toString();
                    String parentcontact_post = parent_no.getText().toString();
                    String school_post = school_name.getText().toString();
                    String subject_post = "----";

                    String[] details = {role_post, name_post, phnno_post, email_post, parentname_post, parentcontact_post, school_post, yop_post, stream_post, parentemail_post, subject_post};
                    Log.d("test1", details.toString());
                    new SendPostRequest().execute(details);

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

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Stream");
        categories.add("Medical");
        categories.add("Non-Medical");
        categories.add("Commerce");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        stream.setAdapter(dataAdapter);

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i <= 2025; i++)
            years.add(Integer.toString(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yop.setAdapter(adapter);

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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Student_form.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("https://jbooks.000webhostapp.com/form_api_test.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("role", strings[0]);
                postDataParams.put("name", strings[1]);
                postDataParams.put("ph_no", strings[2]);
                postDataParams.put("email", strings[3]);
                postDataParams.put("father_name", strings[4]);
                postDataParams.put("parent_contact", strings[5]);
                postDataParams.put("school_name", strings[6]);
                postDataParams.put("yop", strings[7]);
                postDataParams.put("stream", strings[8]);
                postDataParams.put("father_email", strings[9]);
                postDataParams.put("subject", strings[10]);
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
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, result);
            Toast.makeText(getApplicationContext(), "Please Login First",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Student_form.this, Mainfile.class);
            startActivity(intent);
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
