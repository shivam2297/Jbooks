package com.example.hp.jbooks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Contents extends AppCompatActivity {
    List<subject_categories> contentList;

    String link="";
    private String TAG = "network test";

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    ProgressBar progressBar;

    String HTTP_JSON_URL = "https://jbooks.000webhostapp.com/category_test.php";

    String GET_JSON_FROM_SERVER_NAME = "content";
    String GET_JSON_FROM_SERVER_LINK = "link";

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    View ChildView ;

    int GetItemPosition ;
    int id_subject;
    String category;

    ArrayList<String> ContentNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_subject = extras.getInt("id_subject");
            category = extras.getString("category");
            //The key argument here must match that used in the other activity
        }

        String subject_id = Integer.toString(id_subject);
        String[] details = {subject_id, category};
        new SendPostRequest().execute(details);
        contentList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        progressBar.setVisibility(View.VISIBLE);

        ContentNames = new ArrayList<>();

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("https://jbooks.000webhostapp.com/form_api_test.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("subject_id", strings[0]);
                postDataParams.put("category", strings[1]);

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


            JSON_DATA_WEB_CALL();

            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                GestureDetector gestureDetector = new GestureDetector(Contents.this, new GestureDetector.SimpleOnGestureListener() {

                    @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                        return true;
                    }

                });
                @Override
                public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                    ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                        GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                        Toast.makeText(Contents.this, ContentNames.get(GetItemPosition), Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Contents.this,Category.class);
                        intent.putExtra("link",link);
                        startActivity(intent);
                    }

                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });


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

    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }
    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            subject_categories GetDataAdapter2 = new subject_categories();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setName(json.getString(GET_JSON_FROM_SERVER_NAME));

                ContentNames.add(json.getString(GET_JSON_FROM_SERVER_NAME));
                link=json.getString(GET_JSON_FROM_SERVER_LINK);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            contentList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewCardViewAdapter(contentList, this);

        recyclerView.setAdapter(recyclerViewadapter);

    }


}
