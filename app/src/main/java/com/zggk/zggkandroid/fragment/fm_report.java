package com.zggk.zggkandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.ViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class fm_report extends Fragment {

	private View mView;
	private Context mContext;
	private ListView mLv_report;
	private LvAdapter mLvAdapter;
	private List<String> mList_data = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fm_report, null);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mLv_report = (ListView) mView.findViewById(R.id.lv_report);

		mList_data.add("sss");
		mList_data.add("sss");
		mList_data.add("sss");
		mLvAdapter = new LvAdapter(mContext, mList_data,
				R.layout.lv_item_report);
		mLv_report.setAdapter(mLvAdapter);
	}

	private class LvAdapter extends CommonAdapter<String> {

		public LvAdapter(Context context, List<String> mDatas, int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, String item, int position) {
			// TODO Auto-generated method stub

		}

	}

}
