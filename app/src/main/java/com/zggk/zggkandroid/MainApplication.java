package com.zggk.zggkandroid;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zggk.zggkandroid.common.CrashHandler;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.SpUtils;
import com.zggk.zggkandroid.entity.AccountListEntity;
import com.zggk.zggkandroid.entity.DmDinsp;
import com.zggk.zggkandroid.entity.DmDinspRecord;
import com.zggk.zggkandroid.entity.DmFinsp;
import com.zggk.zggkandroid.entity.DmFinspRecord;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.Pavement;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.entity.Routine_main;
import com.zggk.zggkandroid.entity.StaticCarEntity;
import com.zggk.zggkandroid.entity.StaticEntity;
import com.zggk.zggkandroid.entity.StaticKmEntity;
import com.zggk.zggkandroid.entity.StaticRoadEntity;
import com.zggk.zggkandroid.entity.StaticWeatherEntity;

import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MainApplication extends Application {

	public static Context mContext;
	public static int mScreenW, mScreenH;
	public static List<Mod_disease> mList_disease;
	public static AccountListEntity mCurAccounInfo;
	public static String latitude;
	public static String longitude;
	private static long requestTime = 1000 * 60;
	public static String length;
	public static String ImageUrl;
	public static  String WEB_SERVER_URL;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		CrashHandler.getInstance().init(this);
		x.Ext.init(this);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenW = dm.widthPixels;
		mScreenH = dm.heightPixels;
//		ImageUrl="http://128.21.8.7:7001/mems/phoneservlt";
//		WEB_SERVER_URL= "http://128.21.8.7:7001/mems/xfservices/MyWebService";
		ImageUrl = "http://121.8.234.83:17001/mems/phoneservlt";
		WEB_SERVER_URL = "http://121.8.234.83:17001/mems/xfservices/MyWebService";
		initUIL();

		length= SpUtils.getString("length","");
		if (TextUtils.isEmpty(length)) SpUtils.putString("length","10");

		createTable();
//		deleteData();
		mList_disease = DBHelperSingleton.getInstance().getData(
				Mod_disease.class, "isDisease = 'true'");
//		String[] nameStrings="".split(",");
//		System.out.println(nameStrings);

	}

	public static AccountListEntity getCurUserinfo() {
		return mCurAccounInfo;
	}

	/**
	 * 创建数据库表格
	 */
	private void createTable() {
		DBHelperSingleton.getInstance().CreateTable(Mod_disease.class, "id");
		DBHelperSingleton.getInstance().CreateTable(Routine_main.class, "rId");
		DBHelperSingleton.getInstance().CreateTable(DmDinsp.class, "dinspId");
		DBHelperSingleton.getInstance().CreateTable(DmDinspRecord.class, "bId");
		DBHelperSingleton.getInstance().CreateTable(AccountListEntity.class,
				"USER_CODE");
		DBHelperSingleton.getInstance().CreateTable(RouteEntity.class,
				"LINE_ID");
		DBHelperSingleton.getInstance().CreateTable(DssTypeEntity.class,
				"parentId");
		DBHelperSingleton.getInstance().CreateTable(DmFinsp.class, "finspId");
		DBHelperSingleton.getInstance().CreateTable(DmFinspRecord.class, "bId");
		DBHelperSingleton.getInstance().CreateTable(Pavement.class, "pId");
		DBHelperSingleton.getInstance().CreateTable(StaticEntity.class, "dayStatus");
		DBHelperSingleton.getInstance().CreateTable(StaticCarEntity.class, null);
		DBHelperSingleton.getInstance().CreateTable(StaticKmEntity.class, null);
		DBHelperSingleton.getInstance().CreateTable(StaticRoadEntity.class, null);
		DBHelperSingleton.getInstance().CreateTable(StaticWeatherEntity.class, null);
	}

	private static void deleteData() {
		String sql = "DELETE FROM Routine_main WHERE DateTime NOT IN (SELECT inspDate FROM DmDinsp)";
		Boolean flg = DBHelperSingleton.getInstance().deleteDataWithSql(sql);
		if (flg) {
			System.out.println("delete success");
		}
	}

	private static DisplayImageOptions options_normal;

	/**
	 * 初始化UIL框架
	 */
	private void initUIL() {
		// TODO Auto-generated method stub
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext)
				.memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.build();

		ImageLoader.getInstance().init(config);

		options_normal = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).build();

	}

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 判断上午下午
	 * 
	 * @return
	 */
	public static Integer getTime() {
		long time = System.currentTimeMillis();
		final Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(time);

		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int apm = mCalendar.get(Calendar.AM_PM);
		if (apm == 0) {
			// 早上
			return 1;
		} else if (apm == 1 && hour < 18) {
			// 下午
			return 2;
		} else {
			// 晚上
			return 3;
		}
	}

	/**
	 * 获取时间
	 * 
	 * @return
	 */
	public static String getNowDate() {
		long time = System.currentTimeMillis();
		final Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(time);
		int minute = mCalendar.get(Calendar.MINUTE);
		String minString = String.valueOf(minute);
		if (minute < 10) {
			minString = "0" + minString;
		}
		return mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + minString;
	}

	/**
	 * 显示本地图片
	 * 
	 * @param path
	 * @param iv
	 */
	public static void setImgByPath(String path, final ImageView iv) {
		ImageLoader.getInstance().displayImage("file:/" + path, iv,
				options_normal);
	}

	/**
	 * 显示网络图片
	 * 
	 * @param url
	 * @param iv
	 */
	public static void setImgByUrl(String url, ImageView iv) {
		ImageLoader.getInstance().displayImage(url, iv, options_normal);
	}

	/**
	 * 获取经纬度
	 */
	private static void getLocaltion() {
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = String.valueOf(location.getLatitude());
				longitude = String.valueOf(location.getLongitude());
			}
		} else {
			LocationListener locationListener = new LocationListener() {

				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}

				// Provider被enable时触发此函数，比如GPS被打开
				@Override
				public void onProviderEnabled(String provider) {

				}

				// Provider被disable时触发此函数，比如GPS被关闭
				@Override
				public void onProviderDisabled(String provider) {

				}

				// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {
						latitude = String.valueOf(location.getLatitude());
						longitude = String.valueOf(location.getLongitude());
					}
				}
			};
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, requestTime, 0,
					locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = String.valueOf(location.getLatitude());
				longitude = String.valueOf(location.getLongitude());
			}
		}

	}
	public static byte[] getStringFromFile(String filePath) {  
        byte[] ret = null;  
        try {  
        	File file=new File(filePath);
            FileInputStream in = new FileInputStream(file);  
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);  
            byte[] b = new byte[4096];  
            int n;  
            while ((n = in.read(b)) != -1) {  
                out.write(b, 0, n);  
            }  
            in.close();  
            out.close();  
            ret = out.toByteArray();  
        } catch (IOException e) {  
            // log.error("helper:get bytes from file process error!");  
            e.printStackTrace();  
        }  
//        return Base64.encodeToString(ret,Base64.DEFAULT);
        return ret;
    }  
}
