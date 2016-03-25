package com.zggk.zggkandroid.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.SpUtils;

/**
 * Created by Aaron on 16/3/18.
 */
public class SetLengthActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mBtn_back;
    private EditText mEd_Length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_length);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void initView(){

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("病害前后距离");
        mBtn_back = (ImageButton) findViewById(R.id.btn_back);
        mBtn_back.setOnClickListener(this);
        mEd_Length=(EditText)findViewById(R.id.ed_lengthText);
        mEd_Length.setText(MainApplication.length);
    }

    private void setLength(){
        String value=mEd_Length.getText().toString();
        if (!TextUtils.isEmpty(value)){
            if(!value.equals(MainApplication.length)){
                MainApplication.length=value;
                SpUtils.putString("length",value);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
        setLength();
    }
}
