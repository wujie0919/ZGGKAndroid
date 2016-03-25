package com.zggk.zggkandroid.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.activity.Detail_routineInspection;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.interfaces.Interfaces;
import com.zggk.zggkandroid.interfaces.Interfaces.OnLocationListener;
import com.zggk.zggkandroid.service.LocationSvc;

public class fm_focusDisease extends Fragment implements OnLocationListener {

	private Context mContext;
	private View mView;
	private ListView mLv_disease;
	private List<Mod_disease> mList_data;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fm_disease, null);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();

		initView();

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mList_data = DBHelperSingleton.getInstance().getData(Mod_disease.class,
				null);
		if (mList_data == null) {
			mList_data = new ArrayList<Mod_disease>();
		}
		mLv_disease.setAdapter(new CommonAdapter<Mod_disease>(mContext,
				mList_data, R.layout.lv_item_disease) {

			@Override
			public void convert(ViewHolder helper, Mod_disease item,
					int position) {
				// TODO Auto-generated method stub
				DssTypeEntity dssType = (DssTypeEntity) DBHelperSingleton
						.getInstance().getObject(DssTypeEntity.class,
								"parentId='" + item.getId() + "'");

				helper.setText(R.id.tv_diseaseType, dssType.getDSS_TYPE_NAME());
				StringBuffer location = new StringBuffer();
				location.append("位置:")
						// .append(item.getOrientation() == 0 ? "上行-" : "下行-")
						.append(item.getLaneLocation() + "-k")
						.append(item.getLandmarkStart() + "+")
						.append(item.getLandmarkEnd());
				helper.setText(R.id.tv_location, location.toString());

				int level = item.getLevel();
				String strLv = null;
				switch (level) {
				case 0:
					strLv = "轻";
					break;
				case 1:
					strLv = "中";
					break;
				case 2:
					strLv = "重";
					break;
				default:
					break;
				}
				helper.setText(R.id.tv_level, "严重程度:" + strLv);

				StringBuffer source = new StringBuffer();
				source.append("来源:").append(item.getDate()).append("  ");
				int type = item.getType();
				switch (type) {
				case 0:
					source.append("日常巡查");
					break;
				case 1:
					source.append("路面经常检查");
					break;
				case 2:
					source.append("路基经常检查");
					break;
				case 3:
					source.append("设施经常检查");
					break;
				case 4:
					source.append("绿化经常检查");
					break;
				case 5:
					source.append("桥梁经常检查");
					break;
				case 6:
					source.append("涵洞经常检查");
					break;
				case 7:
					source.append("隧道经常检查");
					break;
				case 8:
					source.append("边坡经常检查");
					break;
				default:
					break;
				}
				helper.setText(R.id.tv_source, source.toString());

				String describe = TextUtils.isEmpty(item.getDescribe()) ? "无"
						: item.getDescribe();
				helper.setText(R.id.tv_describe, "描述:" + describe);

				ImageView iv = helper.getView(R.id.iv_1);
				String paths = item.getPath();
				if (!TextUtils.isEmpty(paths)) {
					String path = paths.split(",")[0];
					MainApplication.setImgByPath(path, iv);
				} else {
					iv.setImageResource(R.drawable.ic_default);
				}
			}
		});

	}

	private void initView() {
		// TODO Auto-generated method stub
		mLv_disease = (ListView) mView.findViewById(R.id.lv_disease);
		mLv_disease.setOnItemClickListener(new LvItemClickListener());
	}

	private class LvItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("id", mList_data.get(position).getId());
			intent.setClass(mContext, Detail_routineInspection.class);
			startActivity(intent);
		}
	}

	@Override
	public void onLocation(Location location) {
		// TODO Auto-generated method stub
		// Constants.log("定位信息---》" + location.toString());

		double lat = location.getLatitude();
		double lot = location.getLongitude();
		Geocoder geocoder = new Geocoder(mContext);
		try {
			String localCity = "";
			List<Address> fromLocation = geocoder.getFromLocation(lat, lot, 1);
			for (int i = 0; i < fromLocation.size(); i++) {
				Address address = fromLocation.get(i);
				localCity = address.getLocality();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
