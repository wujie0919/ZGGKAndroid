package com.zggk.zggkandroid.utils;

import android.content.Context;
import android.content.Intent;

import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.entity.AccountListEntity;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

/**
 * 数据获取工具类
 *
 * @author xushaohan
 */
public class DataUtils {

    /**
     * 检查数据是否已下载并保存到本地
     *
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
        updateDiseaseType(context, null);
    }

    /**
     * 更新病害信息
     *
     * @param context
     * @param callback
     */
    public static void updateDiseaseType(final Context context, final HttpCallBack callback) {
        WebServiceUtils.getDiseaseType(new HttpCallBack() {

            @Override
            public void callBack(SoapObject result) {
                // TODO Auto-generated method stub
                if (callback != null) {
                    callback.callBack(result);
                }
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
        updateRouteList(context, null);
    }

    /**
     * 更新路线信息
     */
    public static void updateRouteList(final Context context, final HttpCallBack callback) {
        Constant.getInstance().showProgress(context, "数据更新中，请稍后...");
        WebServiceUtils.getRouteData(new HttpCallBack() {

            @Override
            public void callBack(SoapObject result) {
                // TODO Auto-generated method stub
                if (callback != null) {
                    callback.callBack(result);
                }
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
        updateAccountInfo(null);
    }

    /**
     * 更新账户信息
     */
    public static void updateAccountInfo(final HttpCallBack callback) {
        WebServiceUtils.getLoginUserinfo(new HttpCallBack() {

            @Override
            public void callBack(SoapObject result) {
                // TODO Auto-generated method stub
                if (callback != null) {
                    callback.callBack(result);
                }

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
