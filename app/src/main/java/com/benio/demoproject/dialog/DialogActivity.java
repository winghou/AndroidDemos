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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_message_dialog:
                showMessageDialog();
                break;
        }
    }

    private void showMessageDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Title")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("long message, long message, long message, long message")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .setNeutralButton("中立", null)
                .create();
        dialog.show();
    }
}
