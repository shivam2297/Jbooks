package com.example.hp.jbooks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Faculty_form extends AppCompatActivity implements AdapterView.OnItemClickListener{
    EditText name,phn_no,email,school_name;
    Spinner subject;
    Button faculty_submit_btn;
    private ProgressDialog pDialog;
    private String TAG = "network test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);
        name = (EditText) findViewById(R.id.name);
        phn_no = (EditText) findViewById(R.id.phn_no);
        email = (EditText) findViewById(R.id.email);
        school_name = (EditText) findViewById(R.id.school_name);
        subject = (Spinner) findViewById(R.id.subject);
        faculty_submit_btn = (Button) findViewById(R.id.faculty_submit_btn);

        faculty_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allvalid=true;
                if (name.getText().toString().matches("") || phn_no.getText().toString().matches("") || email.getText().toString().matches("") || school_name.getText().toString().matches("")) {
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                }
                if(phn_no.getText().toString().length() < 7 || phn_no.getText().toString().length() > 13) {
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "check your Phone Number", Toast.LENGTH_SHORT).show();
                }
                if (!isValidEmail(email.getText())){
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "check your email", Toast.LENGTH_SHORT).show();
                }
                if (subject.getSelectedItemPosition()==0){
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "choose a subject", Toast.LENGTH_SHORT).show();
                }
                if (allvalid && isOnline()){
                    Toast.makeText(getApplicationContext(), "Succesfully login by:"+name.getText().toString(), Toast.LENGTH_SHORT).show();
                    String role_post = "faculty";
                    String name_post = name.getText().toString();
                    String email_post = email.getText().toString();
                    String phnno_post = phn_no.getText().toString();
                    String fathername_post = "----";
                    String fatheremail_post ="----";
                    String stream_post = "----";
                    String yop_post="----";
                    String parentcontact_post = "----";
                    String subject_post=subject.getSelectedItem().toString();
                    String school_post = school_name.getText().toString();

                    String[] details = {role_post, name_post, phnno_post, email_post, fathername_post, parentcontact_post, school_post,yop_post,stream_post,fatheremail_post,subject_post};

                    String[] maildetails = {name_post, phnno_post, email_post};
                    Log.d("test1", details.toString());
                    new SendPostRequest().execute(details);
                }else{
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
                    }
                    catch(Exception e)
                    {
                        Log.d(TAG, "Show Dialog: "+e.getMessage());
                    }
                }
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Subject");
        categories.add("Physics");
        categories.add("Chemistry");
        categories.add("Maths");
        categories.add("Biology");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        subject.setAdapter(dataAdapter);
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Faculty_form.this);
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
            Intent intent = new Intent(Faculty_form.this, Login.class);
            startActivity(intent);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

