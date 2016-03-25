package com.zggk.zggkandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.activity.Login;
import com.zggk.zggkandroid.activity.SetLengthActivity;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.utils.DataUtils;

import java.util.Timer;
import java.util.TimerTask;

public class fm_manage extends Fragment implements OnClickListener {

	private View mView;
	private TableRow mLayout_updateVer, mLayout_settingParams,
			mLayout_updateDisease, mLayout_updateBase, mLayout_updateGIS;
	private Context mContext;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fm_manage, null);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mContext=getActivity();
		mLayout_settingParams = (TableRow) mView
				.findViewById(R.id.layout_settingParams);
		mLayout_updateBase = (TableRow) mView
				.findViewById(R.id.layout_updateBase);
		mLayout_updateDisease = (TableRow) mView
				.findViewById(R.id.layout_updateDisease);
		Button btnExit=(Button)mView.findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(this);
		mLayout_updateGIS = (TableRow) mView
				.findViewById(R.id.layout_updateGIS);
		mLayout_updateVer = (TableRow) mView
				.findViewById(R.id.layout_updateVer);

		mLayout_settingParams.setOnClickListener(this);
		mLayout_updateBase.setOnClickListener(this);
		mLayout_updateDisease.setOnClickListener(this);
		mLayout_updateGIS.setOnClickListener(this);
		mLayout_updateVer.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.layout_updateVer:

			break;
		case R.id.layout_settingParams:

			intent.setClass(mContext, SetLengthActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_updateBase:
			updateBaseData();
			break;
		case R.id.btn_exit:
			intent.setClass(mContext, Login.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		// if (v.getId() == R.id.layout_settingParams) {
		// Constant.showToast(getActivity(), "参数设置");
		// } else if (v.getId() == R.id.layout_updateBase) {
		// Constant.showToast(getActivity(), "基础设置");
		// } else {
		// Constant.getInstance().showProgress(getActivity(), "更新中...");
		// update(v.getId());
		// }
	}


	/**
	 * 更新基础数据
	 */
	private void updateBaseData() {
		DataUtils.getDiseaseType(getActivity());
		DataUtils.getRouteList(getActivity());
		DataUtils.getAccountInfo();
	}

	private Timer mTimer;
	private Handler mHandler = new Handler();

	private void update(final int id) {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO
						Constant.getInstance().dismissDialog();
						switch (id) {
						case R.id.layout_updateVer:
							Constant.showToast(getActivity(), "您已经是最新版本");
							break;
						case R.id.layout_settingParams:
							Constant.showToast(getActivity(), "参数设置");
							break;
						case R.id.layout_updateDisease:
							Constant.showToast(getActivity(), "更新病害信息成功");
							break;
						case R.id.layout_updateBase:
							Constant.showToast(getActivity(), "更新基础设置");
							break;
						case R.id.layout_updateGIS:
							Constant.showToast(getActivity(), "更新GIS数据成功");
							break;

						default:
							break;
						}

						mTimer.cancel();
						mHandler.removeCallbacksAndMessages(null);
					}
				});
			}
		}, 1000, 1000);
	}
}
