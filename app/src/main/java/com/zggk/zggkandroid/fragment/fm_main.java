package com.zggk.zggkandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.activity.CheckStructures;
import com.zggk.zggkandroid.activity.CheckPavement;
import com.zggk.zggkandroid.activity.RoutineInspection;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.Mod_mainGV_item;

/**
 * 主页界面
 * 
 * @author xsh
 * 
 */
public class fm_main extends Fragment {

	private Context mContext;
	private View mView_main;
	private GridView mGv_main;
	private int num=5;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView_main = inflater.inflate(R.layout.fm_main, null);
		return mView_main;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();

		inflateData();
		initView();
		
	}

	private List<Mod_mainGV_item> mList_gvDatas = new ArrayList<Mod_mainGV_item>();

	private void inflateData() {
		for (int i = 0; i < num; i++) {
			Mod_mainGV_item item = new Mod_mainGV_item();
			switch (i) {
			case 0:
				item.setResource(R.drawable.ic_routine_inspection);
				item.setName("日常巡查");
				break;
			case 1:
				item.setResource(R.drawable.ic_pavement);
				item.setName("路面经常检查");
				break;
			case 2:
				item.setResource(R.drawable.ic_subgrade);
				item.setName("路基经常检查");
				break;
			case 3:
				item.setResource(R.drawable.ic_facility);
				item.setName("设施经常检查");
				break;
			case 4:
				item.setResource(R.drawable.ic_greening);
				item.setName("绿化经常检查");
				break;
//			case 5:
//				item.setResource(R.drawable.ic_bridge);
//				item.setName("桥梁经常检查");
//				break;
//			case 6:
//				item.setResource(R.drawable.ic_culvert);
//				item.setName("涵洞经常检查");
//				break;
//			case 7:
//				item.setResource(R.drawable.ic_tunnel);
//				item.setName("隧道经常检查");
//				break;
//			case 8:
//				item.setResource(R.drawable.ic_side);
//				item.setName("边坡经常检查");
//				break;

			default:
				break;
			}
			mList_gvDatas.add(item);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		mGv_main = (GridView) mView_main.findViewById(R.id.gv_main);
		mGv_main.setOnItemClickListener(new GvItemClickListener());
		mGv_main.setAdapter(new CommonAdapter<Mod_mainGV_item>(mContext,
				mList_gvDatas, R.layout.gv_item_main) {

			@Override
			public void convert(ViewHolder helper, Mod_mainGV_item item,
					int position) {
				// TODO Auto-generated method stub
				helper.setText(R.id.tv_name, item.getName());
				helper.setImageResource(R.id.img_icon, item.getResource());
			}
		});
	}

	private class GvItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (position) {
			case 0:
				intent.setClass(mContext, RoutineInspection.class);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				intent.setClass(mContext, CheckPavement.class);
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				intent.setClass(mContext, CheckStructures.class);
				break;

			default:
				return;
			}
			intent.putExtra("type", position);
			startActivity(intent);
		}

	}

}
