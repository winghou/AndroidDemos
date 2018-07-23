package com.benio.demoproject.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.benio.demoproject.R;

/**
 * Created by zhangzhibin on 2018/7/20.
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        findViewById(R.id.btn_message_dialog).setOnClickListener(this);
        findViewById(R.id.btn_custom_dialog).setOnClickListener(this);
        findViewById(R.id.btn_input_dialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_message_dialog:
                showMessageDialog();
                break;
            case R.id.btn_custom_dialog:
                showCustomDialog();
                break;
            case R.id.btn_input_dialog:
                showInputDialog();
                break;
        }
    }

    private void showMessageDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("message")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .show();
    }

    private void showCustomDialog() {
        new AlertDialog.Builder(this)
                .setTitle("custom")
                .setView(R.layout.dialog_fingerprint_authentication)
                .show();
    }

    private void showInputDialog() {
        new AlertDialog.Builder(this)
                .setTitle("input")
                .setView(R.layout.dialog_input)
                .setPositiveButton("确定", null)
                .show();
    }
}
