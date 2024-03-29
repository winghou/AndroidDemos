package com.benio.demoproject.span;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.TextView;

import com.benio.demoproject.R;

public class SpanActivity extends AppCompatActivity {

    private TextView mTextView;
    private static final String CONTENT = "12b3,456,789###"/*"#1ab,456,789,asd####f,qwer"*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        mTextView = (TextView) findViewById(R.id.text);
    }

    public void showSuperscriptSpan(View view) {
        String superscript = "7";
        int start = 7;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(CONTENT);
        stringBuilder.replace(start, start + superscript.length(), superscript);
        Parcel parcel = Parcel.obtain();
        parcel.writeString(superscript);
        stringBuilder.setSpan(new SuperscriptSpan(parcel), start, start + superscript.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        parcel.recycle();
        mTextView.setText(stringBuilder);
    }

    public void showForegroundColorSpan(View view) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(CONTENT);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextView.setText(stringBuilder);
    }

    public void showBackgroundColorSpan(View view) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(CONTENT);
        stringBuilder.setSpan(new BackgroundColorSpan(Color.BLUE), 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextView.setText(stringBuilder);
    }

    public void showRoundedBackgroundSpan(View view) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(CONTENT);
        stringBuilder.setSpan(new RoundedBackgroundSpan(Color.BLUE, 10, 10, 5), 0, CONTENT.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextView.setText(stringBuilder);
    }

    public void showSpanUtil(View view) {
        // Create an italic "hello, " a red "world",
        // and bold the entire sequence.
        CharSequence text = SpanUtil.bold(SpanUtil.italic("hello"),
                SpanUtil.color(Color.RED, "world"));
        mTextView.setText(text);
    }
}
