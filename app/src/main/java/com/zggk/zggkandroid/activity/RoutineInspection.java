package com.zggk.zggkandroid.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.SharedPres;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.DmDinsp;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.entity.Routine_main;
import com.zggk.zggkandroid.entity.StaticCarEntity;
import com.zggk.zggkandroid.entity.StaticEntity;
import com.zggk.zggkandroid.entity.StaticKmEntity;
import com.zggk.zggkandroid.entity.StaticRoadEntity;
import com.zggk.zggkandroid.entity.StaticWeatherEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;
import com.zggk.zggkandroid.interfaces.Interfaces;
import com.zggk.zggkandroid.utils.DataUtils;
import com.zggk.zggkandroid.utils.ParseUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.Arrays;
import java.util.List;

/**
 * 日常巡查界面
 *
 * @author xsh
 */
public class RoutineInspection extends BaseActivity implements OnClickListener {

	private Context mContext = RoutineInspection.this;
	private EditText mEt_section, mEt_km, mEt_carNum, mEt_weather,
			mEt_patroler;
	private TextView mTv_title, mTv_date, mTv_route, mTv_manageUnits,
			mTv_curingUnits;
	private TextView mTv_inspTimeIntvlStart, mTv_inspTimeIntvlEnd;

	private PopupWindow mPPW_menu;
	private List<RouteEntity> mList_route;
	private RouteEntity mRoute;

	private String[] mTimes;
	private StaticEntity entity;
	private StaticRoadEntity roadEntity;
	private StaticWeatherEntity weatherEntity;
	private StaticKmEntity kmEntity;
	private StaticCarEntity carEntity;
	private Integer timeInteger;
	private ImageButton mbtn_Section, mbtn_Km, mbtn_CarNum, mbtn_weather;
	private List<StaticCarEntity> carList;
	private List<StaticRoadEntity> roadList;
	private List<StaticKmEntity> kmList;
	private List<StaticWeatherEntity> weatherList;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routine_inspection);
		timeInteger = MainApplication.getTime();
		entity = (StaticEntity) DBHelperSingleton.getInstance().getObject(StaticEntity.class, "dayStatus='" + timeInteger + "'");
		roadEntity = new StaticRoadEntity();
		carEntity = new StaticCarEntity();
		kmEntity = new StaticKmEntity();
		weatherEntity = new StaticWeatherEntity();
		initView();

		boolean loaded = DataUtils.isDataLoaded(RouteEntity.class);
		if (loaded) {
			mList_route = DataUtils.getDatas(RouteEntity.class);
		} else {
			getRouteList();
		}

		mTimes = getResources().getStringArray(R.array.array_times);

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mTv_title.setText("日常巡查");
		ImageButton btn_search = (ImageButton) findViewById(R.id.btn_search);
		btn_search.setVisibility(View.VISIBLE);
		btn_search.setOnClickListener(this);

		ImageView imageView = (ImageView) findViewById(R.id.image_data);
		imageView.setVisibility(View.GONE);

		mbtn_CarNum = (ImageButton) findViewById(R.id.btn_selectCarNum);
		mbtn_CarNum.setOnClickListener(this);
		mbtn_Km = (ImageButton) findViewById(R.id.btn_selectPatrolKm);
		mbtn_Km.setOnClickListener(this);
		mbtn_Section = (ImageButton) findViewById(R.id.btn_selectSection);
		mbtn_Section.setOnClickListener(this);
		mbtn_weather = (ImageButton) findViewById(R.id.btn_selectWeather);
		mbtn_weather.setOnClickListener(this);

		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_inspTimeIntvlStart = (TextView) findViewById(R.id.inspTimeIntvlStart);
		mTv_inspTimeIntvlEnd = (TextView) findViewById(R.id.inspTimeIntvlEnd);
		mEt_patroler = (EditText) findViewById(R.id.et_patroler);
		mTv_manageUnits = (TextView) findViewById(R.id.tv_manageUnits);
		mTv_curingUnits = (TextView) findViewById(R.id.tv_curingUnits);

		mEt_section = (EditText) findViewById(R.id.et_patrolSection);
		mEt_km = (EditText) findViewById(R.id.et_patrolKm);
		mEt_carNum = (EditText) findViewById(R.id.et_patrolCarNum);
		mEt_weather = (EditText) findViewById(R.id.et_weather);

		mTv_inspTimeIntvlStart.setOnClickListener(this);
		mTv_inspTimeIntvlEnd.setOnClickListener(this);
		findViewById(R.id.layout_route).setOnClickListener(this);
		findViewById(R.id.layout_date).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);

		mTv_date.setText(Constant.getInstance().getCurDate());
		mTv_manageUnits.setText(MainApplication.mCurAccounInfo.getORG_NAME());
		mTv_curingUnits.setText(MainApplication.mCurAccounInfo.getUNT_NAME());

		String user = MainApplication.mCurAccounInfo.getUSER_NAME();
		mEt_patroler.setText(user);
		if (!TextUtils.isEmpty(user)) {
			mEt_patroler.setSelection(user.length());
		}

		mRoute = SharedPres.getInstance(mContext).getRoute();
		if (mRoute != null) {
			mTv_route.setText(mRoute.getLINE_ALLNAME());
		}
		if (entity != null) {
			mEt_carNum.setText(entity.getCarNum());
			mEt_weather.setText(entity.getWeather());
			mEt_section.setText(entity.getRoad());
			mEt_km.setText(entity.getMileage());
		}
//		Calendar c = Calendar.getInstance();
		if (timeInteger == 2) {
			mTv_inspTimeIntvlStart.setText("14:00");
			mTv_inspTimeIntvlEnd.setText("17:00");
		} else if (timeInteger == 3) {
			mTv_inspTimeIntvlStart.setText("18:00");
			mTv_inspTimeIntvlEnd.setText("23:00");
		} else {
			mTv_inspTimeIntvlStart.setText("8:00");
			mTv_inspTimeIntvlEnd.setText("12:00");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_search:
				Intent intent = new Intent();
				intent.setClass(mContext, DiseaseList.class);
				intent.putExtra("search", true);
				intent.putExtra("type", getIntent().getIntExtra("type", 0));
				startActivity(intent);
				break;
			case R.id.layout_route:
				if (mList_route != null && mList_route.size() != 0) {
					showRouteListDialog();
				} else {
					getRouteList();
				}
				break;
			case R.id.layout_date:
				// Constant.getInstance().showDataPickerDialog(mContext, mTv_date);
				break;
			// case R.id.btn_menu:
			// if (mPPW_menu == null) {
			// initPpw();
			// }
			// mPPW_menu.showAsDropDown(v);
			// break;
			case R.id.btn_ok:
				if (mRoute == null) {
					showToast("请选择巡查线路");
				} else {
					enter();
				}
				break;
			case R.id.inspTimeIntvlStart:
				Constant.getInstance().showSelectDialog(mContext,
						Arrays.asList(mTimes), mTv_inspTimeIntvlStart,
						new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
													View view, int position, long id) {
								// TODO Auto-generated method stub

							}
						});
				break;
			case R.id.inspTimeIntvlEnd:
				Constant.getInstance().showSelectDialog(mContext,
						Arrays.asList(mTimes), mTv_inspTimeIntvlEnd,
						new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
													View view, int position, long id) {
								// TODO Auto-generated method stub

							}
						});
				break;
			case R.id.btn_selectCarNum:
				showInfoSelect(3);
				break;
			case R.id.btn_selectPatrolKm:
				showInfoSelect(2);
				break;
			case R.id.btn_selectSection:
				showInfoSelect(1);
				break;
			case R.id.btn_selectWeather:
				showInfoSelect(4);
				break;
			default:
				break;
		}
	}

	private void showInfoSelect(final int type) {
		String where = "type=1";
		kmList = DBHelperSingleton.getInstance().getData(StaticKmEntity.class, where);
		roadList = DBHelperSingleton.getInstance().getData(StaticRoadEntity.class, where);
		carList = DBHelperSingleton.getInstance().getData(StaticCarEntity.class, where);
		weatherList = DBHelperSingleton.getInstance().getData(StaticWeatherEntity.class, where);

		final Dialog dialog = new Dialog(mContext);
		Window window = dialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_select,
				null);
		ListView lv_select = (ListView) view.findViewById(R.id.lv_select);

		switch (type) {
			case 1:
				lv_select.setAdapter(new CommonAdapter<StaticRoadEntity>(mContext,
						roadList, R.layout.lv_item_text) {

					@Override
					public void convert(ViewHolder helper, StaticRoadEntity item, int position) {
						TextView tv = helper.getView(R.id.tv_item);
						tv.setText(item.getRoad());
					}
				});
				break;
			case 2:
				lv_select.setAdapter(new CommonAdapter<StaticKmEntity>(mContext,
						kmList, R.layout.lv_item_text) {

					@Override
					public void convert(ViewHolder helper, StaticKmEntity item, int position) {
						TextView tv = helper.getView(R.id.tv_item);
						tv.setText(item.getKm());
					}
				});
				break;
			case 3:
				lv_select.setAdapter(new CommonAdapter<StaticCarEntity>(mContext,
						carList, R.layout.lv_item_text) {

					@Override
					public void convert(ViewHolder helper, StaticCarEntity item, int position) {
						TextView tv = helper.getView(R.id.tv_item);
						tv.setText(item.getCarNum());
					}
				});
				break;
			case 4:
				lv_select.setAdapter(new CommonAdapter<StaticWeatherEntity>(mContext,
						weatherList, R.layout.lv_item_text) {

					@Override
					public void convert(ViewHolder helper, StaticWeatherEntity item, int position) {
						TextView tv = helper.getView(R.id.tv_item);
						tv.setText(item.getWeather());
					}
				});
				break;
		}
		lv_select.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				switch (type){
					case 1:
						mEt_section.setText(roadList.get(position).getRoad());
						break;
					case 2:
						mEt_km.setText(kmList.get(position).getKm());
						break;
					case 3:
						mEt_carNum.setText(carList.get(position).getCarNum());
						break;
					case 4:
						mEt_weather.setText(weatherList.get(position).getWeather());
						break;
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

	/*
	选择路线
	 */
	private void showRouteListDialog() {
		final Dialog dialog = new Dialog(mContext);
		Window window = dialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_select,
				null);
		ListView lv_select = (ListView) view.findViewById(R.id.lv_select);

		lv_select.setAdapter(new CommonAdapter<RouteEntity>(mContext,
				mList_route, R.layout.lv_item_text) {

			@Override
			public void convert(ViewHolder helper, RouteEntity item,
								int position) {
				// TODO Auto-generated method stub
				TextView tv = helper.getView(R.id.tv_item);
				tv.setText(item.getLINE_ALLNAME());
			}
		});

		lv_select.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				mRoute = mList_route.get(position);
				mTv_route.setText(mRoute.getLINE_ALLNAME());
				SharedPres.getInstance(mContext).saveRoute(mRoute);
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.show();
		window.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
		dialog.show();

	}

	private void enter() {
		String date = mTv_date.getText().toString();
		String section = mEt_section.getText().toString().trim();//366
		String carNum = mEt_carNum.getText().toString().trim();//644
		String mileage = mEt_km.getText().toString().trim();//255
		String weather = mEt_weather.getText().toString().trim();//晴
		String maneUnits = mTv_manageUnits.getText().toString();
		String curingUnits = mTv_curingUnits.getText().toString();
		String inspTimeIntvlStart = mTv_inspTimeIntvlStart.getText().toString();
		String inspTimeIntvlEnd = mTv_inspTimeIntvlEnd.getText().toString();
		String user = mEt_patroler.getText().toString().trim();
		String staticId = "";
		DmDinsp dmDinsp = new DmDinsp();
		staticId = String.valueOf(System.currentTimeMillis());
		int type = 1;
		if (entity == null) {
			entity = new StaticEntity();
		}
		roadEntity.setType(type);
		carEntity.setType(type);
		kmEntity.setType(type);
		weatherEntity.setType(type);
		carEntity.setCarNum(carNum);
		roadEntity.setRoad(section);
		kmEntity.setKm(mileage);
		weatherEntity.setWeather(weather);
		if (DBHelperSingleton.getInstance().getCount(StaticCarEntity.class,"carNum","type=1 AND carNum='"+carNum+"'")<=0) {
			DBHelperSingleton.getInstance().insertOrReplaceData(carEntity);
		}
		if (DBHelperSingleton.getInstance().getCount(StaticRoadEntity.class,"road","type=1 AND road='"+section+"'")<=0){
			DBHelperSingleton.getInstance().insertOrReplaceData(roadEntity);
		}
		if (DBHelperSingleton.getInstance().getCount(StaticKmEntity.class,"km","type=1 AND km='"+mileage+"'")<=0){
			DBHelperSingleton.getInstance().insertOrReplaceData(kmEntity);
		}
		if (DBHelperSingleton.getInstance().getCount(StaticWeatherEntity.class,"weather","type=1 AND weather='"+weather+"'")<=0){
			DBHelperSingleton.getInstance().insertOrReplaceData(weatherEntity);
		}


		entity.setCarNum(carNum);
		entity.setDayStatus(String.valueOf(timeInteger));
		entity.setMileage(mileage);
		entity.setRoad(section);
		entity.setWeather(weather);
		DBHelperSingleton.getInstance().insertOrReplaceData(entity);
		// 先判断是否已经有主表，如果没有创建主表，如果有则不用创建
		Routine_main routine_main = (Routine_main) DBHelperSingleton
				.getInstance().getObject(
						Routine_main.class,
						"lineCode='" + mRoute.getLINE_ID() + "' AND DateTime='"
								+ date + "'");
		if (routine_main != null) {
			dmDinsp.setDinspId(routine_main.getrId());
		} else {
			routine_main = new Routine_main();
			dmDinsp.setDinspId(MainApplication.getUUID());
			routine_main.setrId(dmDinsp.getDinspId());
			routine_main.setDateTime(date);
			routine_main.setLineCode(mRoute.getLINE_ID());
			routine_main
					.setOrg_id(MainApplication.getCurUserinfo().getORG_ID());
			routine_main.setUser_code(MainApplication.getCurUserinfo()
					.getUSER_CODE());
			DBHelperSingleton.getInstance().insertData(routine_main);
		}
		dmDinsp.setbId(staticId);
		dmDinsp.setLineCode(mRoute.getLINE_ID());
		dmDinsp.setCreateTime(date);
		dmDinsp.setInspDate(date);
		dmDinsp.setMntOrgId(MainApplication.getCurUserinfo().getORG_ID());
		dmDinsp.setMntnOrgNm(MainApplication.getCurUserinfo().getORG_NAME());
		dmDinsp.setCreateUserId(MainApplication.getCurUserinfo().getUSER_CODE());
		dmDinsp.setSearchDept(MainApplication.getCurUserinfo().getORG_NAME());

		if (timeInteger == 1) {
			// 早上
			dmDinsp.setInspCarM(carNum);
			dmDinsp.setInspTimeIntvlM(String.valueOf(timeInteger));
			dmDinsp.setInspTimeIntvlStartM(inspTimeIntvlStart);
			dmDinsp.setInspTimeIntvlEndM(inspTimeIntvlEnd);
			dmDinsp.setInspScopeM(section);
			dmDinsp.setWeatherM(weather);
			dmDinsp.setInspDistanceM(mileage);
			dmDinsp.setInspPersonM(user);
		} else if (timeInteger == 2) {
			// 下午
			dmDinsp.setInspCarA(carNum);
			dmDinsp.setInspTimeIntvlA(String.valueOf(timeInteger));
			dmDinsp.setInspTimeIntvlStartA(inspTimeIntvlStart);
			dmDinsp.setInspTimeIntvlEndA(inspTimeIntvlEnd);
			dmDinsp.setInspScopeA(section);
			dmDinsp.setWeatherA(weather);
			dmDinsp.setInspDistanceA(mileage);
			dmDinsp.setInspPersonA(user);
		} else {
			// 晚上
			dmDinsp.setInspCarN(carNum);
			dmDinsp.setInspTimeIntvlN(String.valueOf(timeInteger));
			dmDinsp.setInspTimeIntvlStartN(inspTimeIntvlStart);
			dmDinsp.setInspTimeIntvlEndN(inspTimeIntvlEnd);
			dmDinsp.setInspScopeN(section);
			dmDinsp.setWeatherN(weather);
			dmDinsp.setInspDistanceN(mileage);
			dmDinsp.setInspPersonN(user);
		}
		DBHelperSingleton.getInstance().insertOrReplaceData(dmDinsp);

		Mod_disease disease = new Mod_disease();
		disease.setType(0);
		disease.setId(staticId);
		disease.setZhuId(dmDinsp.getDinspId());
		disease.setDate(date);
		disease.setLineID(mRoute.getLINE_ID());
		disease.setLineName(mRoute.getLINE_ALLNAME());
		disease.setRoute(mRoute);
		disease.setSection(section);
		disease.setCarNum(carNum);
		disease.setMileage(mileage);
		disease.setWeather(weather);
		disease.setManeUnits(maneUnits);
		disease.setCuringUnits(curingUnits);
		disease.setUser(user);
		disease.setInspTimeIntvlStart(inspTimeIntvlStart);
		disease.setInspTimeIntvlEnd(inspTimeIntvlEnd);

		Intent intent = new Intent();
		intent.setClass(mContext, InputRoutineInspection.class);
		intent.putExtra("action", "input");
		Bundle b = new Bundle();
		b.putSerializable("data", disease);
		intent.putExtra("data", b);
		startActivity(intent);

	}

	@SuppressWarnings("deprecation")
	private void initPpw() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_setting = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_exitLogin = (TextView) layout
				.findViewById(R.id.tv_ppwItem2);
		tv_exitLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, Login.class);
				startActivity(intent);
				finish();
			}
		});
		tv_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Interfaces.getInstance().startChangePage(4);
				mPPW_menu.dismiss();
				finish();
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

	private void getRouteList() {
		showPreDialog("正在获取线路列表，请稍后...");
		// 避免重复获取数据
		if (!isRequest) {
			isRequest = true;
			WebServiceUtils.getRouteData(new HttpCallBack() {

				@Override
				public void callBack(SoapObject result) {
					// TODO Auto-generated method stub
					dismissPreDialog();
					isRequest = false;
					if (result != null) {
						// log(result.toString());
						mList_route = ParseUtils.parseRouteData(result);
					}
				}
			});
		}

	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.connect();
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"RoutineInspection Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app deep link URI is correct.
//				Uri.parse("android-app://com.zggk.zggkandroid.activity/http/host/path")
//		);
//		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"RoutineInspection Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app deep link URI is correct.
//				Uri.parse("android-app://com.zggk.zggkandroid.activity/http/host/path")
//		);
//		AppIndex.AppIndexApi.end(client, viewAction);
//		client.disconnect();
	}
}
