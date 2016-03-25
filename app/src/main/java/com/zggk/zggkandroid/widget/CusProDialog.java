package com.zggk.zggkandroid.widget;

import com.zggk.zggkandroid.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CusProDialog extends Dialog {

	private Context mContext;

	public CusProDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public CusProDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	private TextView mTv_text;

	private void init() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_loading,
				null);
		mTv_text = (TextView) view.findViewById(R.id.tv_loading);
		this.getWindow().requestFeature(1);
		this.getWindow().setBackgroundDrawable(new BitmapDrawable());
		this.setContentView(view);
	}

	public void show(String str) {
		if (!TextUtils.isEmpty(str)) {
			mTv_text.setVisibility(View.VISIBLE);
			mTv_text.setText(str);
		}
		show();
	}
}
