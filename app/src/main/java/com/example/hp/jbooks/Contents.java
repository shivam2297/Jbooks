package com.example.hp.jbooks;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class Contents extends AppCompatActivity {
    List<Subject_Contents> contentList;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    ProgressBar progressBar;

    String HTTP_JSON_URL_BASE = "https://jbooks.000webhostapp.com/content_test.php?";
    String HTTP_JSON_URL;
    String GET_JSON_FROM_SERVER_NAME = "name";
    String GET_JSON_FROM_SERVER_ID = "id";
    String GET_JSON_FROM_SERVER_LINK = "link";
    String GET_JSON_FROM_SERVER_DESCRIPTION = "description";
    JsonArrayRequest jsonArrayRequest;

    RequestQueue requestQueue;

    View ChildView;

    int GetItemPosition;
    int id_subject;
    int id_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contents);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_subject = extras.getInt("id_subject");
            id_category = extras.getInt("category");
            //The key argument here must match that used in the other activity
        }
        HTTP_JSON_URL = HTTP_JSON_URL_BASE + "subject=0&" + "category=0";
        contentList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        progressBar.setVisibility(View.VISIBLE);

        JSON_DATA_WEB_CALL();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(Contents.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    Toast.makeText(Contents.this, contentList.get(GetItemPosition).getName(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(Category.this, id, Toast.LENGTH_LONG).show();
                    String link = contentList.get(GetItemPosition).getLink();
                    Intent intent = new Intent(Contents.this, WebViewpdf.class);
                    intent.putExtra("link", link);
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

    public void JSON_DATA_WEB_CALL() {

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("response", response.toString());
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

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            Subject_Contents GetDataAdapter2 = new Subject_Contents();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setId(json.getString(GET_JSON_FROM_SERVER_ID));
                GetDataAdapter2.setName(json.getString(GET_JSON_FROM_SERVER_NAME));
                GetDataAdapter2.setLink(GET_JSON_FROM_SERVER_LINK);
                //Toast.makeText(Category.this,json.getString(GET_JSON_FROM_SERVER_NAME)+" "+id, Toast.LENGTH_LONG).show();


            } catch (JSONException e) {

                e.printStackTrace();
            }
            contentList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new Content_Adapter(contentList, this);

        recyclerView.setAdapter(recyclerViewadapter);

    }

}
