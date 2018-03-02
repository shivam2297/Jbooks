package com.example.hp.jbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class Category extends AppCompatActivity {
    List<subject_categories> categoryList;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    ProgressBar progressBar;
    String id = "";

    String HTTP_JSON_URL = "https://jbooks.000webhostapp.com/category_test.php?subject=1";

    String GET_JSON_FROM_SERVER_NAME = "category";
    String GET_JSON_FROM_SERVER_ID = "id";

    JsonArrayRequest jsonArrayRequest;

    RequestQueue requestQueue;

    View ChildView;

    int GetItemPosition;
    int id_subject;

    ArrayList<String> CategoryNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_subject = extras.getInt("id_subject");
            //The key argument here must match that used in the other activity
        }

        categoryList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        progressBar.setVisibility(View.VISIBLE);

        CategoryNames = new ArrayList<>();

        JSON_DATA_WEB_CALL();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(Category.this, new GestureDetector.SimpleOnGestureListener() {

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

                    Toast.makeText(Category.this, CategoryNames.get(GetItemPosition), Toast.LENGTH_LONG).show();
                    //Toast.makeText(Category.this, id, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Category.this, Contents.class);
                    intent.putExtra("id_subject", id_subject);
                    intent.putExtra("category", CategoryNames.get(GetItemPosition));
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

            subject_categories GetDataAdapter2 = new subject_categories();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setName(json.getString(GET_JSON_FROM_SERVER_NAME));

                CategoryNames.add(json.getString(GET_JSON_FROM_SERVER_NAME));
                id = json.getString(GET_JSON_FROM_SERVER_ID);
                //Toast.makeText(Category.this,json.getString(GET_JSON_FROM_SERVER_NAME)+" "+id, Toast.LENGTH_LONG).show();


            } catch (JSONException e) {

                e.printStackTrace();
            }
            categoryList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewCardViewAdapter(categoryList, this);

        recyclerView.setAdapter(recyclerViewadapter);

    }
}
