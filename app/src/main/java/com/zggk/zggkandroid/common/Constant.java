package com.zggk.zggkandroid.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.activity.Login;

public class Constant {

	private static Constant instance;
	public static final String TAG = "ZGGK";
	public static final int SELECT_IMG_BY_CAMERA = 0;
	public static final int SELECT_IMG_BY_GALLERY = 1;

	// 单例 禁止外部实例
	private Constant() {

	}

	public static Constant getInstance() {
		if (instance == null) {
			instance = new Constant();
		}
		return instance;
	}

	public static void log(String str) {
		Log.d(TAG,
				Thread.currentThread().getStackTrace()[3].getFileName()
						+ " "
						+ Thread.currentThread().getStackTrace()[3]
								.getLineNumber() + "  " + str);
	}

	public static void showToast(Context context, String str) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 70);
		toast.show();
	}

	private ProgressDialog mProDialog;

	public void showProgress(Context context, String str) {
		mProDialog = new ProgressDialog(context);
		mProDialog.setMessage(str);
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.show();
	}

	public void dismissDialog() {
		if (mProDialog != null) {
			mProDialog.dismiss();
		}
	}

	// 弹出选择日期dialog
	public void showDataPickerDialog(Context context, final TextView textView) {
		Calendar calendar = Calendar.getInstance();

		DatePickerDialog datePickerDialog = new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0, int year, int month,
							int dayOfMonth) {
						// TODO Auto-generated method stub
						textView.setText(year + "-" + (month + 1) + "-"
								+ dayOfMonth);

					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	/**
	 * 
	 * @return 返回当前日期 2015-05-19
	 */
	public String getCurDate() {
		Calendar calendar = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.YEAR)).append("-")
				.append(calendar.get(Calendar.MONTH) + 1).append("-")
				.append(calendar.get(Calendar.DAY_OF_MONTH));
		return sb.toString();
	}

	private PopupWindow mPPW_menu;

	/**
	 * 初始化点击菜单显示的PPW
	 * 
	 * @param isMain
	 *            是否在主页面
	 */
	public void initMenuPPW(final Context context, final boolean isMain) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_ppwItem1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_ppwItem2 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_ppwItem1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isMain) {

				}
			}
		});

		tv_ppwItem2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
				intent1.setClass(context, Login.class);
				context.startActivity(intent1);
				// context.finish();
				mPPW_menu.dismiss();
			}
		});

		mPPW_menu = new PopupWindow(layout);
		mPPW_menu.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		// // 控制popupwindow点击屏幕其他地方消失
		mPPW_menu.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片，不能在布局中设置，要通过代码来设置
		mPPW_menu.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

		mPPW_menu.setWidth(LayoutParams.WRAP_CONTENT);
		mPPW_menu.setHeight(LayoutParams.WRAP_CONTENT);

	}

	public void showSelectDialog(Context context, List<String> list, TextView tv) {
		showSelectDialog(context, list, tv, null);
	}

	public void showSelectDialog(Context context, List<String> list,
			OnItemClickListener listener) {
		showSelectDialog(context, list, null, listener);
	}

	/**
	 * 显示选择列表弹框
	 * 
	 * @param context
	 * @param list
	 * @param tv
	 * @param listener
	 */
	public void showSelectDialog(Context context, final List<String> list,
			final TextView tv, final OnItemClickListener listener) {
		final Dialog dialog = new Dialog(context);
		Window window = dialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(context).inflate(R.layout.dlg_select,
				null);
		ListView lv_select = (ListView) view.findViewById(R.id.lv_select);

		lv_select.setAdapter(new CommonAdapter<String>(context, list,
				R.layout.lv_item_text) {

			@Override
			public void convert(ViewHolder helper, String item, int position) {
				// TODO Auto-generated method stub
				TextView tv = helper.getView(R.id.tv_item);
				tv.setText(item);
			}
		});

		lv_select.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (tv != null) {
					tv.setText(list.get(position));
				}
				if (listener != null) {
					listener.onItemClick(parent, view, position, id);
				}

				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.show();
		window.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
		dialog.show();
	}

	/**
	 * MD5加密
	 * 
	 * @param info
	 * @return
	 */
	public static String getMD5(String info) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(info.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			int size = encryption.length;
			for (int i = 0; i < size; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(
							Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return strBuf.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

}
