package com.zggk.zggkandroid.utils;

import android.content.Context;
import android.os.Environment;

import com.zggk.zggkandroid.MainApplication;

import java.io.File;

public class FileUtils {

	public static final String ROOT_FOLDER = "ZGGK";
	public static final String SDROOT_PATH = Environment
			.getExternalStorageDirectory().getPath();
	public static String DATAROOT_PATH = null;

	private final static String CACHE_FOLDER = File.separator + ROOT_FOLDER
			+ File.separator + "cache" + File.separator;
	private final static String LOG_FOLDER = File.separator + ROOT_FOLDER
			+ File.separator + "logs" + File.separator;
	private final static String TEMP_FOLDER = File.separator + ROOT_FOLDER
			+ File.separator + "temp" + File.separator;
	private final static String DOWNLOAD_FOLDER = File.separator + ROOT_FOLDER
			+ File.separator + "down" + File.separator;

	/**
	 * 检查SD卡是否挂载
	 * 
	 * @return
	 */
	private static boolean isSDMount() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取根目录
	 * 
	 * @return
	 */
	private static String getRootPath() {
		return isSDMount() ? SDROOT_PATH
				: getDataPath(MainApplication.mContext);
	}

	private static String getDataPath(Context context) {
		return context.getCacheDir().getPath();
	}

	public static String getTempPath() {
		String path = getRootPath() + TEMP_FOLDER;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String getCrashDirectory(){
		String path = getRootPath() + LOG_FOLDER;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

}
