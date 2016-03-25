package com.zggk.zggkandroid.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.DBHelperSingleton.DataBaseCallBack;
import com.zggk.zggkandroid.common.SharedPres;
import com.zggk.zggkandroid.entity.AccountListEntity;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_server;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;
import com.zggk.zggkandroid.utils.DataUtils;
import com.zggk.zggkandroid.utils.ParseUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends BaseActivity implements OnClickListener {

	private Context mContext = Login.this;
	private Spinner mSpinner;
	private EditText mEt_name, mEt_psw;
	private Button mBtn_login;
	private ImageButton mBtn_addServer;
	private List<Mod_server> mList_servers = new ArrayList<Mod_server>();
	private SpinnerAdapter mSpinnerAdapter = new SpinnerAdapter();
	private String mStr_server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		checkAccountList();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mSpinner = (Spinner) findViewById(R.id.spinner_server);
		mEt_name = (EditText) findViewById(R.id.et_user);
		mEt_psw = (EditText) findViewById(R.id.et_psw);
		mBtn_addServer = (ImageButton) findViewById(R.id.btn_addServer);
		mBtn_login = (Button) findViewById(R.id.btn_login);
		mBtn_addServer.setOnClickListener(this);
		mBtn_login.setOnClickListener(this);

		// 模拟服务器数据
		for (int i = 0; i < 3; i++) {
			Mod_server server = new Mod_server();
			server.setName("服务器-" + i);
			mList_servers.add(server);
		}

		mEt_name.setText(SharedPres.getInstance(mContext).getUserName());
		mEt_psw.setText(SharedPres.getInstance(mContext).getPsw());

		mSpinner.setAdapter(mSpinnerAdapter);
	}

	int i = 2;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			login();
			break;
		case R.id.btn_addServer:
//			if (dialog == null) {
//				addServer();
//			} else {
//				dialog.show();
//			}
			break;
		default:
			break;
		}
	}

	private Dialog dialog;

	private void addServer() {
		dialog = new Dialog(mContext);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dlg_addserver, null);
		final EditText et_name = (EditText) view.findViewById(R.id.et_name);
		final EditText et_address = (EditText) view
				.findViewById(R.id.et_address);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = et_name.getText().toString().trim();
				String address = et_address.getText().toString().trim();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
					Constant.showToast(mContext, "服务器名称和地址不能为空");
					return;
				}
				Mod_server server = new Mod_server();
				server.setName(name);
				server.setAddress(address);
				mList_servers.add(server);
				mSpinnerAdapter.notifyDataSetChanged();
				mSpinner.setSelection(mList_servers.size() - 1);
				Constant.showToast(mContext, "添加成功");
				dialog.dismiss();
				et_name.setText("");
				et_address.setText("");
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.setTitle("添加服务器");
		dialog.show();
	}

	private void login() {
		// super.log(mStr_server);
		final String name = mEt_name.getText().toString().trim();
		final String psw = mEt_psw.getText().toString().trim();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psw)) {
			super.showToast("用户名或密码不能为空");
			return;
		}
		
		String pswMD5 = Constant.getMD5(psw);
		final AccountListEntity account = (AccountListEntity) DBHelperSingleton
				.getInstance().getObject(AccountListEntity.class,
						"LOWER(USER_CODE)='" + name.toLowerCase() + "'");
		if (account == null) {
			showToast("账号不存在");
		} else if (!psw.equals(account.getPASSWORD())
				&& !pswMD5.equals(account.getPASSWORD())) {
			showToast("密码不正确");
		} else {
			//更改账号重新拉取路线信息
			MainApplication.mCurAccounInfo = account;
			
			if (!name.equals(SharedPres.getInstance(mContext).getUserName())) {
				showPreDialog("正在获取基础信息，请稍后...");
				SharedPres.getInstance(mContext).saveUserInfo(name, psw);
				DBHelperSingleton.getInstance().deleteData(RouteEntity.class, null);
				// 避免重复获取数据
				WebServiceUtils.getRouteData(new HttpCallBack() {
					@Override
					public void callBack(SoapObject result) {
						// TODO Auto-generated method stub
						if (result != null) {
							// log(result.toString());
							ParseUtils.parseRouteData(result);
							SharedPres pres=new SharedPres();
							pres.saveRoute(new RouteEntity());
							WebServiceUtils.getDiseaseType(new HttpCallBack() {
								
								@Override
								public void callBack(SoapObject result) {
									// TODO Auto-generated method stub
									if (result!=null) {
										ParseUtils.parseDiseaseTypeData(result);
										dismissPreDialog();
										Intent intent = new Intent();
										intent.setClass(mContext, MainActivity.class);
										startActivity(intent);
										loadData();
									}else {
										dismissPreDialog();
										showToast("网络异常，获取数据失败");
									}
									
								}
							});	
						}else {
							dismissPreDialog();
							showToast("网络异常，获取数据失败");
						}
					}
				});
			}else {
				SharedPres.getInstance(mContext).saveUserInfo(name, psw);
				int num=DBHelperSingleton.getInstance().getCount(RouteEntity.class, "LINE_CODE", null);
				if (num>0) {
					Intent intent = new Intent();
					intent.setClass(mContext, MainActivity.class);
					startActivity(intent);
					loadData();
				}else {
					// 避免重复获取数据
					showPreDialog("正在获取基础信息，请稍后...");
					WebServiceUtils.getRouteData(new HttpCallBack() {
						@Override
						public void callBack(SoapObject result) {
							// TODO Auto-generated method stub
							
							if (result != null) {
								// log(result.toString());
								ParseUtils.parseRouteData(result);
								WebServiceUtils.getDiseaseType(new HttpCallBack() {
									
									@Override
									public void callBack(SoapObject result) {
										// TODO Auto-generated method stub
										if (result!=null) {
											ParseUtils.parseDiseaseTypeData(result);
											dismissPreDialog();
											Intent intent = new Intent();
											intent.setClass(mContext, MainActivity.class);
											startActivity(intent);
											loadData();
										}else {
											dismissPreDialog();
											showToast("网络异常，获取数据失败");
										}
									}
								});
							}else {
								dismissPreDialog();
								showToast("网络异常，获取数据失败");
							}
						}
					});
				}
			}
			
		}
	}
	private class SpinnerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList_servers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList_servers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv_name = new TextView(mContext);
			tv_name.setText(mList_servers.get(position).getName());
			tv_name.setPadding(20, 20, 20, 20);
			return tv_name;
		}
	}

	/**
	 * 检查用户列表
	 */
	private void checkAccountList() {
		if (!isUserRequest) {
			isUserRequest=true;
			int count = DBHelperSingleton.getInstance().getCount(
					AccountListEntity.class, null, null);
			if (count == 0) {
				showPreDialog("数据加载中，请稍后...");
				WebServiceUtils.getLoginUserinfo(new HttpCallBack() {

					@Override
					public void callBack(SoapObject result) {
						// TODO Auto-generated method stub
						isUserRequest=false;
						if (result != null) {
							// log(result.toString());
							parseSoapObject(result);
							
						}else {
							dismissPreDialog();
							showToast("服务器异常，无数据更新");
						}
					}
				});
			}
		}
	}

	/**
	 * 解析SoapObject对象
	 * 
	 * @param result
	 * @return
	 */
	private List<AccountListEntity> parseSoapObject(SoapObject result) {
		List<AccountListEntity> list = new ArrayList<AccountListEntity>();
		int len = result.getPropertyCount();
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
				DBHelperSingleton.getInstance().insertDataWithList(list,true, new DataBaseCallBack() {

					@Override
					public void callBack(Object result) {
						// TODO Auto-generated method stub
						Boolean flg=(Boolean)result;
						if (flg) {
							dismissPreDialog();
							showToast("数据更新完成，请登录");
						}
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private void loadData(){
		int dssCount = DBHelperSingleton.getInstance().getCount(
				DssTypeEntity.class, null, null);
		if (dssCount == 0) {
			DataUtils.getDiseaseType(mContext);
		}
		
		int routeCount = DBHelperSingleton.getInstance().getCount(
				RouteEntity.class, null, null);
		if (routeCount == 0) {
			DataUtils.getRouteList(mContext);
		}
	}

}
