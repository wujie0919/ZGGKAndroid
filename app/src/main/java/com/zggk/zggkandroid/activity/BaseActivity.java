package com.zggk.zggkandroid.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.entity.AccountListEntity;

public class BaseActivity extends Activity {

    private Context mContext = BaseActivity.this;
    public static final String TAG = "ZGGK";
    public static Boolean isRequest = false;
    public static Boolean isUserRequest = false;
    private Dialog mPreDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (savedInstanceState != null) {
            AccountListEntity userInfo = (AccountListEntity) savedInstanceState.getSerializable("userInfo");
            if (userInfo != null) {
                MainApplication.mCurAccounInfo = userInfo;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("userInfo", MainApplication.mCurAccounInfo);
    }

    public void showToast(String str) {
        Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 50);
        toast.show();
    }

    public void log(String str) {
        Log.d(TAG,
                Thread.currentThread().getStackTrace()[3].getFileName()
                        + " "
                        + Thread.currentThread().getStackTrace()[3]
                        .getLineNumber() + "  " + str);
    }

    @SuppressWarnings("deprecation")
    public Dialog showPreDialog(String str) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_loading, null);
        if (!TextUtils.isEmpty(str)) {
            TextView tv = (TextView) view.findViewById(R.id.tv_loading);
            tv.setVisibility(View.VISIBLE);
            tv.setText(str);
        }
        mPreDialog = new Dialog(mContext);
        mPreDialog.getWindow().requestFeature(1);
        mPreDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        mPreDialog.setContentView(view);
        mPreDialog.show();
        return mPreDialog;
    }

    public void dismissPreDialog() {
        if (mPreDialog != null) {
            mPreDialog.dismiss();
        }
    }
}
