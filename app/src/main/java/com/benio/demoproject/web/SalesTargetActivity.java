package com.benio.demoproject.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.benio.demoproject.R;
import com.benio.demoproject.web.urlrooter.H5Contract;

public class SalesTargetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_target);

        TextView textView = (TextView) findViewById(R.id.tv_sales_target);
        Intent intent = getIntent();
        String orgId = intent.getStringExtra(H5Contract.SalesTarget.KEY_ORG_ID);
        String orgName = intent.getStringExtra(H5Contract.SalesTarget.KEY_ORG_NAME);
        String orgType = intent.getStringExtra(H5Contract.SalesTarget.KEY_ORG_TYPE);
        String date = intent.getStringExtra(H5Contract.SalesTarget.KEY_DATE);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("orgId:").append(orgId).append('\n');
        stringBuilder.append("orgName:").append(orgName).append('\n');
        stringBuilder.append("orgType:").append(orgType).append('\n');
        stringBuilder.append("date:").append(date).append('\n');
        textView.setText(stringBuilder.toString());
    }
}
