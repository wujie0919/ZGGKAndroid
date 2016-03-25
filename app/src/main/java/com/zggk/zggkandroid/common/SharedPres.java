package com.zggk.zggkandroid.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.zggk.zggkandroid.entity.RouteEntity;

public class SharedPres {

	private static Context mContext;
	private static SharedPres instance;
	private SharedPreferences mSharedPre;

	public static SharedPres getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPres();
		}
		mContext = context;
		return instance;
	}

	public void saveUserInfo(String name, String psw) {
		mSharedPre = mContext.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		Editor editor = mSharedPre.edit();
		editor.putString("userName", name);
		editor.putString("psw", psw);
		editor.commit();
	}

	public String getUserName() {
		mSharedPre = mContext.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		return mSharedPre.getString("userName", "");
	}

	public String getPsw() {
		mSharedPre = mContext.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		return mSharedPre.getString("psw", "");
	}

	public void saveServerInfo(String name, String address) {
		mSharedPre = mContext.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		Editor editor = mSharedPre.edit();
		editor.putString("serverName", name);
		editor.putString("address", address);
		editor.commit();
	}

	public String getServerName() {
		mSharedPre = mContext.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		return mSharedPre.getString("serverName", "");
	}

	public String getServerAddress() {
		mSharedPre = mContext.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		return mSharedPre.getString("address", "");
	}

	/**
	 * 保存默认线路
	 */
	public void saveRoute(RouteEntity entity) {
		mSharedPre = mContext.getSharedPreferences("routeConfigs",
				Context.MODE_PRIVATE);
		Editor editor = mSharedPre.edit();
		editor.putString("lineId", entity.getLINE_ID());
		editor.putString("lineCode", entity.getLINE_CODE());
		editor.putString("lineName", entity.getLINE_ALLNAME());
		editor.putString("lineStartStake", entity.getSTARTSTAKE());
		editor.putString("lineEndStake", entity.getENDSTAKE());
		editor.putString("lineStartStake_down",
				entity.getDOWN_START_STAKE_NUM());
		editor.putString("lineEndStake_down", entity.getDOWN_END_STAKE_NUM());
		editor.commit();
	}

	/**
	 * 获取默认线路
	 */
	public RouteEntity getRoute() {
		mSharedPre = mContext.getSharedPreferences("routeConfigs",
				Context.MODE_PRIVATE);
		String lineId = mSharedPre.getString("lineId", null);
		if (TextUtils.isEmpty(lineId)) {
			return null;
		}

		RouteEntity route = new RouteEntity();
		route.setLINE_ID(lineId);
		route.setLINE_CODE(mSharedPre.getString("lineCode", null));
		route.setLINE_ALLNAME(mSharedPre.getString("lineName", null));
		route.setSTARTSTAKE(mSharedPre.getString("lineStartStake", null));
		route.setENDSTAKE(mSharedPre.getString("lineEndStake", null));
		route.setDOWN_START_STAKE_NUM(mSharedPre.getString(
				"lineStartStake_down", null));
		route.setDOWN_END_STAKE_NUM(mSharedPre.getString("lineEndStake_down",
				null));
		return route;
	}

}
