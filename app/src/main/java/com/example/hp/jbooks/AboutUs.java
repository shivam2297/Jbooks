package com.example.hp.jbooks;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView credits = (TextView) findViewById(R.id.creditText);
        String text = "Developed By:<br>" +
                "<a href=\"https://www.linkedin.com/in/paras-jain-963a84a2/\">Paras Jain</a><br>" +
                "<a href=\"https://www.linkedin.com/in/shivam-bansal-b9773412b/\">Shivam Bansal</a><br>" +
                "<a href=\"https://www.linkedin.com/in/shivam-bansal-b9763412b/\">Nishi Khera</a>";
        credits.setMovementMethod(LinkMovementMethod.getInstance());
        credits.setText(Html.fromHtml(text));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
