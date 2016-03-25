package com.zggk.zggkandroid.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.google.gson.Gson;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.DBHelperSingleton.DataBaseCallBack;
import com.zggk.zggkandroid.entity.AccountListEntity;
import com.zggk.zggkandroid.entity.DssListEntity;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.entity.DssListEntity.FoundDate;

/**
 * 解析工具类
 * 
 * @author xushaohan
 * 
 */
public class ParseUtils {

	public static List<RouteEntity> parseRouteData(SoapObject result) {
		List<RouteEntity> list = new ArrayList<RouteEntity>();
		int len = result.getPropertyCount();
		for (int i = 0; i < len; i++) {
			try {
				JSONObject object = new JSONObject(result.getProperty(i)
						.toString());
				String arrayData = object.getString("data");
				JSONArray array = new JSONArray(arrayData);
				int size = array.length();
				for (int j = 0; j < size; j++) {
					JSONObject json = array.getJSONObject(j);
					Gson gson = new Gson();
					RouteEntity data = gson.fromJson(json.toString(),
							RouteEntity.class);
					// data.setParentId(data.getLINE_ID());
					list.add(data);
				}
				// Gson gson = new Gson();
				// Type type = new
				// TypeToken<List<AccountListEntity>>(){}.getType();
				// list = gson.fromJson(array.toString(), type);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DBHelperSingleton.getInstance().insertDataWithList(list,true, new DataBaseCallBack() {

			@Override
			public void callBack(Object result) {
				// TODO Auto-generated method stub
			}
		});
		return list;
	}

	public static List<DssTypeEntity> parseDiseaseTypeData(SoapObject result) {
		List<DssTypeEntity> list = new ArrayList<DssTypeEntity>();
		int len = result.getPropertyCount();
		for (int i = 0; i < len; i++) {
			try {
				JSONObject object = new JSONObject(result.getProperty(i)
						.toString());
				String arrayData = object.getString("data");
				JSONArray array = new JSONArray(arrayData);
				int size = array.length();
				for (int j = 0; j < size; j++) {
					JSONObject json = array.getJSONObject(j);
					Gson gson = new Gson();
					DssTypeEntity data = gson.fromJson(json.toString(),
							DssTypeEntity.class);
					data.setParentId(data.getDSS_TYPE());
					list.add(data);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DBHelperSingleton.getInstance().insertDataWithList(list,true, new DataBaseCallBack() {

			@Override
			public void callBack(Object result) {
				// TODO Auto-generated method stub
			}
		});
		return list;
	}

	/**
	 * 解析账户信息
	 * 
	 * @param result
	 * @return
	 */
	public static void parseAccountData(SoapObject result) {
		int len = result.getPropertyCount();
		List<AccountListEntity> list = new ArrayList<AccountListEntity>();
		for (int i = 0; i < len; i++) {
			try {
				JSONObject object = new JSONObject(result.getProperty(i)
						.toString());
				JSONArray array = object.getJSONArray("data");
				int size = array.length();
				for (int j = 0; j < size; j++) {
					JSONObject json = array.getJSONObject(j);
					Gson gson = new Gson();
					AccountListEntity account = gson.fromJson(json.toString(),
							AccountListEntity.class);
					list.add(account);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DBHelperSingleton.getInstance().insertDataWithList(list,true, new DataBaseCallBack() {

			@Override
			public void callBack(Object result) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 解析定检病害列表数据
	 * 
	 * @param result
	 * @return
	 */
	public static List<Mod_disease> parseDssListData(SoapObject result) {
		List<Mod_disease> list = new ArrayList<Mod_disease>();
		// List<DssListEntity> list = new ArrayList<DssListEntity>();
		int len = result.getPropertyCount();
		for (int i = 0; i < len; i++) {
			try {
				JSONObject object = new JSONObject(result.getProperty(i)
						.toString());
				String arrayStr = object.getString("data");
				JSONArray outArray = new JSONArray(arrayStr);
				if (outArray.length() == 0) {
					return list;
				}
				JSONArray array = outArray.getJSONObject(0)
						.getJSONArray("data");
				int size = array.length();
				for (int j = 0; j < size; j++) {
					JSONObject json = array.getJSONObject(j);
					Gson gson = new Gson();
					DssListEntity dss = gson.fromJson(json.toString(),
							DssListEntity.class);
					list.add(changeData(dss));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	public static Mod_disease changeData(DssListEntity dss) {
		Mod_disease d = new Mod_disease();
		String level = dss.getDssDegree();
		if (level.contains("轻")) {
			d.setLevel(0);
		} else if (level.contains("重")) {
			d.setLevel(2);
		} else {
			d.setLevel(1);
		}

		String stake = dss.getStake();
		String[] stakes = stake.split("\\.");
		if (stakes.length >= 2) {
			d.setLandmarkStart(stakes[0]);
			d.setLandmarkEnd(stakes[1]);
		}

		FoundDate dateInfo = dss.getFoundDate();
		String date = DateUtils.getDate(dateInfo.getTime());
		d.setDate(date);

		d.setWidth(dss.getDssW());
		d.setLength(dss.getDssL());
		d.setDeep(dss.getDssD());
		d.setArea(dss.getDssA());
		d.setCount(dss.getDssN());
		d.setVolume(dss.getDssV());
		d.setFacilityCat(dss.getFacilityCat());
		d.setLaneLocation(dss.getLane());
		d.setNature(Integer.valueOf(dss.getDssQuality()));
		d.setDSS_TYPE(dss.getDssType());
		d.setLineID(dss.getLineId());
		d.setOrientation(dss.getLineDirect().equals("上行") ? 0 : 1);
		d.setAdvice(dss.getMntnAdvice());

		return d;
	}

}
