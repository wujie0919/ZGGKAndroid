package com.zggk.zggkandroid.utils;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;

import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.entity.AccountListEntity;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;

/**
 * 数据获取工具类
 * 
 * @author xushaohan
 * 
 */
public class DataUtils {

	/**
	 * 检查数据是否已下载并保存到本地
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isDataLoaded(Class<?> cls) {
		int count = DBHelperSingleton.getInstance().getCount(cls, null, null);
		if (count == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取本地数据库数据
	 * 
	 * @param cls
	 * @return
	 */
	public static <T> List<T> getDatas(Class<?> cls) {
		return DBHelperSingleton.getInstance().getData(cls, null);
	}

	/**
	 * 获取病害信息
	 */
	public static void getDiseaseType(final Context context) {
		WebServiceUtils.getDiseaseType(new HttpCallBack() {

			@Override
			public void callBack(SoapObject result) {
				// TODO Auto-generated method stub
				if (result != null) {
					DBHelperSingleton.getInstance().deleteData(
							DssTypeEntity.class, null);
					ParseUtils.parseDiseaseTypeData(result);
					context.sendBroadcast(new Intent().setAction("refresh_main_disease"));
				}
			}
		});
	}

	/**
	 * 获取路线信息
	 */
	public static void getRouteList(final Context context) {
		Constant.getInstance().showProgress(context, "数据获取中，请稍后...");
		WebServiceUtils.getRouteData(new HttpCallBack() {

			@Override
			public void callBack(SoapObject result) {
				// TODO Auto-generated method stub
				Constant.getInstance().dismissDialog();
				if (result != null) {
					// log(result.toString());
					DBHelperSingleton.getInstance().deleteData(
							RouteEntity.class, null);
					ParseUtils.parseRouteData(result);
					context.sendBroadcast(new Intent().setAction("refresh_main_disease"));
				} else {
					Constant.showToast(context, "数据获取失败");
				}
			}
		});
	}

	/**
	 * 获取账户信息
	 */
	public static void getAccountInfo() {
		WebServiceUtils.getLoginUserinfo(new HttpCallBack() {

			@Override
			public void callBack(SoapObject result) {
				// TODO Auto-generated method stub
				if (result != null) {
					// log(result.toString());
					DBHelperSingleton.getInstance().deleteData(
							AccountListEntity.class, null);
					ParseUtils.parseAccountData(result);
				}
			}
		});
	}

}
