package com.zggk.zggkandroid.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.activity.Detail_routineInspection;
import com.zggk.zggkandroid.activity.MainActivity;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;
import com.zggk.zggkandroid.interfaces.Interfaces;
import com.zggk.zggkandroid.interfaces.Interfaces.OnLocationListener;
import com.zggk.zggkandroid.service.LocationSvc;
import com.zggk.zggkandroid.utils.ParseUtils;
import com.zggk.zggkandroid.widget.CusProDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressLint("InflateParams")
public class fm_disease extends Fragment implements OnLocationListener,
		OnClickListener,MainActivity.SetLengthCallBack {

	private Context mContext;
	private View mView;
	private ListView mLv_disease;
	private List<Mod_disease> mList_data;
	private EditText mET_search;
	private LvAdapter mAdapter;

	private ProgressBar mProgress_loading;

	private CusProDialog mProDialog;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MainActivity.setSetLengthCallBack(this);
		mView = inflater.inflate(R.layout.fm_disease, null);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();

		mProDialog = new CusProDialog(mContext);

		initView();

		Interfaces.getInstance().setLocationListenr(this);
		mContext.startService(new Intent(mContext, LocationSvc.class));

		mReceiver = new BroadReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("refresh_main_disease");
		getActivity().registerReceiver(mReceiver, filter);
	}

	private BroadReceiver mReceiver;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(mReceiver);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mLv_disease = (ListView) mView.findViewById(R.id.lv_disease);
		mLv_disease.setOnItemClickListener(new LvItemClickListener());
		mET_search = (EditText) mView.findViewById(R.id.et_search);

		mView.findViewById(R.id.iv_addSearchCondition).setOnClickListener(this);

		mProgress_loading = (ProgressBar) mView
				.findViewById(R.id.progress_loading);
		mList_data = new ArrayList<Mod_disease>();
		mAdapter = new LvAdapter(mContext, mList_data,
				R.layout.lv_item_diseaselist);
		mLv_disease.setAdapter(mAdapter);

		mLv_disease.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (mLv_disease.getLastVisiblePosition() == totalItemCount - 1
						&& mHasMoreData) {
					mProgress_loading.setVisibility(View.VISIBLE);
					mPage = mList_data.size() / 10;
					String keyword = mET_search.getText().toString().trim();
					getData(keyword);
				}
			}
		});

		mView.findViewById(R.id.btn_search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mProDialog.show();
						mPage = 0;
						mHasMoreData = true;
						String keyword = mET_search.getText().toString().trim();
						getData(keyword);
					}
				});

	}

	private int mPage = 0;
	private String mLat = null, mLot = null;

	private boolean mIsLoading, mHasMoreData = true;

	private void getData(String keyword) {
		if (mIsLoading) {
			return;
		}
		mIsLoading = true;
		LinkedHashMap<String, String> paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("page", mPage + "");
		paramsMap.put("num", "10");
		paramsMap.put("longitude", mLot);
		paramsMap.put("latitude", mLat);
		paramsMap.put("usercode", MainApplication.getCurUserinfo()
				.getUSER_CODE());
		paramsMap.put("keyword", keyword);
		// 在下面继续添加参数
		paramsMap.put("lineID", lineID);
		paramsMap.put("startStake", startStake);
		paramsMap.put("endStake", endStake);
		paramsMap.put("startDate", startDate);
		paramsMap.put("endDate", endDate);
		paramsMap.put("dssType", dssType);
		paramsMap.put("facilityType", facilityType);
		paramsMap.put("len",MainApplication.length);
		WebServiceUtils.regularCheckDiseaseList(paramsMap, new HttpCallBack() {

			@Override
			public void callBack(SoapObject result) {
				// TODO Auto-generated method stub
				mIsLoading = false;
				mProgress_loading.setVisibility(View.GONE);
				mProDialog.dismiss();
				if (result != null) {
					List<Mod_disease> list = ParseUtils
							.parseDssListData(result);
					if (mPage == 0) {
						mList_data.clear();
						mList_data.addAll(list);
						mLv_disease.setAdapter(mAdapter);
					} else {
						mList_data.addAll(list);
						mAdapter.notifyDataSetChanged();
					}

					if (list.size() >= 0 && list.size() < 10) {
						mHasMoreData = false;
					}
				} else {
					mHasMoreData = false;
					Constant.showToast(mContext, "获取定检病害失败");
				}
			}
		});
	}

	@Override
	public void setLength() {
		mET_search.setHint("请输入关键字搜索前后"+MainApplication.length+"米病害信息");
	}

	private class LvAdapter extends CommonAdapter<Mod_disease> {

		public LvAdapter(Context context, List<Mod_disease> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, Mod_disease item, int position) {
			// TODO Auto-generated method stub
			CheckBox ckb_choose = helper.getView(R.id.ckb_chose);
			ckb_choose.setVisibility(View.GONE);
			TextView tv_uploadStatus = helper.getView(R.id.tv_uploadStatus);
			tv_uploadStatus.setVisibility(View.GONE);

			ImageView iv_img = helper.getView(R.id.iv_img);
			TextView tv_routeName = helper.getView(R.id.tv_routeName);
			TextView tv_item1 = helper.getView(R.id.tv_item1);
			TextView tv_item2 = helper.getView(R.id.tv_item2);
			TextView tv_item3 = helper.getView(R.id.tv_item3);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

				}
			}).start();
			RouteEntity route = (RouteEntity) DBHelperSingleton.getInstance()
					.getObject(RouteEntity.class,
							"LINE_ID='" + item.getLineID() + "'");
			if (route != null) {
				tv_routeName.setText(route.getLINE_ALLNAME());
			}

			DssTypeEntity dss = (DssTypeEntity) DBHelperSingleton.getInstance()
					.getObject(DssTypeEntity.class,
							"DSS_TYPE='" + item.getDSS_TYPE() + "'");
			if (dss == null) {
				dss = new DssTypeEntity();
			}

			StringBuffer item1 = new StringBuffer();
			// int orientation = item.getOrientation();
			// item1.append(orientation == 0 ? "上行 " : "下行 ")
			item1.append(item.getLaneLocation() + " K")
					.append(item.getLandmarkStart()).append("+")
					.append(item.getLandmarkEnd());
			tv_item1.setText(item1.toString());

			tv_item3.setText(item.getDate());
			// int type = item.getType();
			int level = item.getLevel();
			String str_level = null;
			switch (level) {
			case 0:
				str_level = "轻";
				break;
			case 1:
				str_level = "中";
				break;
			case 2:
				str_level = "重";
				break;

			default:
				break;
			}

			tv_item2.setText(dss.getDSS_TYPE_NAME() + " " + str_level);

			// if (item.isUploaded()) {
			// if (!mList_uploaded.contains(position)) {
			// mList_uploaded.add(position);
			// }
			// ckb_choose.setEnabled(false);
			// tv_uploadStatus.setText("已上传");
			// } else {
			// ckb_choose.setEnabled(true);
			// tv_uploadStatus.setText("未上传");
			// if (mList_checked.contains(position)) {
			// ckb_choose.setChecked(true);
			// } else {
			// ckb_choose.setChecked(false);
			// }
			// }

			String paths = item.getPath();
			if (!TextUtils.isEmpty(paths)) {
				String path = paths.split(",")[0];
				MainApplication.setImgByPath(path, iv_img);
			} else {
				iv_img.setImageResource(R.drawable.ic_default);
			}
		}
	}

	private class LvItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(mContext, Detail_routineInspection.class);
			intent.putExtra("data", mList_data.get(position));
			startActivity(intent);
		}
	}

	@Override
	public void onLocation(Location location) {
		// TODO Auto-generated method stub
		// Constants.log("定位信息---》" + location.toString());

		mLat = location.getLatitude() + "";
		mLot = location.getLongitude() + "";

		getData("");
		// Geocoder geocoder = new Geocoder(mContext);
		// try {
		// double localCity;
		// List<Address> fromLocation = geocoder.getFromLocation(lat, lot, 1);
		// for (int i = 0; i < fromLocation.size(); i++) {
		// Address address = fromLocation.get(i);
		// localCity = address.getLatitude();
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private Dialog mDialog;
	private Window mDlgWindow;
	private TextView mTv_line, mTv_facilityType;
	private List<RouteEntity> mList_line;
	private List<String> mList_facilityType;

	private RouteEntity mRoute;
	private String lineID, startStake, endStake, startDate, endDate, dssType,
			facilityType;
	private TextView mTv_startDate, mTv_endDate,mTv_zhuangHao;

	private void showSearchDailog() {
		if (mDialog == null) {
			mDialog = new Dialog(mContext);
			mDlgWindow = mDialog.getWindow();
			mDlgWindow.requestFeature(1);

			View view = LayoutInflater.from(mContext).inflate(
					R.layout.dlg_search, null);
			view.findViewById(R.id.layout_line).setOnClickListener(this);
			view.findViewById(R.id.layout_facilityType)
					.setOnClickListener(this);
			Button btn_positive = (Button) view.findViewById(R.id.btn_ok);
			Button btn_negative = (Button) view.findViewById(R.id.btn_cancel);

			final EditText et_startStake = (EditText) view
					.findViewById(R.id.et_startStake);
			final EditText et_endStake = (EditText) view
					.findViewById(R.id.et_endStake);
			final EditText et_dssType = (EditText) view
					.findViewById(R.id.et_dssType);
			mTv_line = (TextView) view.findViewById(R.id.tv_line);
			mTv_startDate = (TextView) view.findViewById(R.id.tv_startDate);
			mTv_endDate = (TextView) view.findViewById(R.id.tv_endDate);
			mTv_startDate.setOnClickListener(this);
			mTv_endDate.setOnClickListener(this);
			mTv_facilityType = (TextView) view
					.findViewById(R.id.tv_facilityType);
			mTv_zhuangHao=(TextView)view.findViewById(R.id.tx_zhuangHao);

			btn_negative.setOnClickListener(this);
			btn_positive.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mRoute != null) {
						lineID = mRoute.getLINE_ID();
					}
					startStake = et_startStake.getText().toString();
					endStake = et_endStake.getText().toString();
					startDate = mTv_startDate.getText().toString();
					endDate = mTv_endDate.getText().toString();

					dssType = et_dssType.getText().toString();
					facilityType = mTv_facilityType.getText().toString();
					mDialog.dismiss();

					mProDialog.show();
					mPage = 0;
					mHasMoreData = true;
					String keyword = mET_search.getText().toString().trim();
					getData(keyword);

				}
			});

			mDialog.setContentView(view);

			mList_line = DBHelperSingleton.getInstance().getData(
					RouteEntity.class, null);
			mList_facilityType = Arrays.asList(getResources().getStringArray(
					R.array.sheshiType));
		}
		mDialog.show();
		mDlgWindow.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_addSearchCondition:
			showSearchDailog();
			break;
		case R.id.btn_cancel:
			mDialog.dismiss();
			break;
		case R.id.layout_line:
			if (mList_line == null) {
				mList_line = DBHelperSingleton.getInstance().getData(
						RouteEntity.class, null);
			}
			showRouteListDialog();
			break;
		case R.id.layout_facilityType:
			Constant.getInstance().showSelectDialog(mContext,
					mList_facilityType, mTv_facilityType, null);
			break;
		case R.id.tv_startDate:
			Constant.getInstance()
					.showDataPickerDialog(mContext, mTv_startDate);
			break;
		case R.id.tv_endDate:
			Constant.getInstance().showDataPickerDialog(mContext, mTv_endDate);
			break;

		default:
			break;
		}
	}

	private void showRouteListDialog() {
		final Dialog dialog = new Dialog(mContext);
		Window window = dialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_select,
				null);
		ListView lv_select = (ListView) view.findViewById(R.id.lv_select);

		lv_select.setAdapter(new CommonAdapter<RouteEntity>(mContext,
				mList_line, R.layout.lv_item_text) {

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
				mRoute = mList_line.get(position);
				mTv_line.setText(mRoute.getLINE_ALLNAME());
				mTv_zhuangHao.setText(getString(R.string.stake_range,
						mRoute.getSTARTSTAKE(), mRoute.getENDSTAKE()));
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.show();
		window.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
		dialog.show();

	}

	private class BroadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mAdapter.notifyDataSetChanged();
		}
	}

}
