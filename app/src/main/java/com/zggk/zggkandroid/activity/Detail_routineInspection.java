package com.zggk.zggkandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;

public class Detail_routineInspection extends BaseActivity implements
		OnClickListener {

	private Context mContext = Detail_routineInspection.this;
	private ImageButton mBtn_back, mBtn_search;
	private TextView mTv_route, mTv_date, mTv_type, mTv_range;
	private TextView mTv_orientation, mTv_problemType, mTv_laneLocation,
			mTv_landmark;
	private TextView mTv_diseaseType, mTv_count, mTv_long, mTv_width, mTv_deep,
			mTv_level, mTv_nature, mTv_curingSuggest, mTv_cause, mTv_area,
			mTv_volume,mTv_locationDesc;
	private LinearLayout mLL_long, mLL_width, mLL_deep, mLL_area, mLL_volume,
			mLL_count;
	private Button mBtn_delete, mBtn_edit;
	private GridView mGv_damageImg;
	private List<String> mList_data = new ArrayList<String>();
	private Mod_disease mDisease;
	private RouteEntity mRoute;
	private DssTypeEntity mDssType;
	private String id;

	private LinearLayout mLayout_routine, mLayout_pavement;
	private TextView mTv_route_pavement, mTv_data_pavement, mTv_user_pavement,
			mTv_direction_pavement, mTv_section_pavement;

	private LinearLayout mLL_user, mLL_bottomMenu;

	private boolean showEdit;//

	private LinearLayout mLL_clearGone;// 保洁时隐藏的布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_routine_inspection);

		id = getIntent().getStringExtra("id");
		if (!TextUtils.isEmpty(id)) {
			mDisease = (Mod_disease) DBHelperSingleton.getInstance().getObject(
					Mod_disease.class, "id='" + id + "'");

			mRoute = (RouteEntity) DBHelperSingleton.getInstance()
					.getObject(RouteEntity.class,
							"LINE_ID='" + mDisease.getLineID() + "'");
			mDisease.setRoute(mRoute);
			mDssType = (DssTypeEntity) DBHelperSingleton.getInstance()
					.getObject(DssTypeEntity.class,
							"DSS_TYPE='" + mDisease.getDSS_TYPE() + "'");
			showEdit = true;
		} else {
			mDisease = (Mod_disease) getIntent().getSerializableExtra("data");
			if (mDisease == null) {
				showToast("数据错误");
				finish();
				return;
			}
			mRoute = (RouteEntity) DBHelperSingleton.getInstance()
					.getObject(RouteEntity.class,
							"LINE_ID='" + mDisease.getLineID() + "'");
			mDssType = (DssTypeEntity) DBHelperSingleton.getInstance()
					.getObject(DssTypeEntity.class,
							"DSS_TYPE='" + mDisease.getDSS_TYPE() + "'");
		}
		initView();

		setUI();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("病害详细");
		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		// mBtn_menu = (ImageButton) findViewById(R.id.btn_menu);
		mBtn_search = (ImageButton) findViewById(R.id.btn_search);
		// mBtn_menu.setVisibility(View.VISIBLE);
		mBtn_search.setVisibility(View.GONE);

		mLL_clearGone = (LinearLayout) findViewById(R.id.layout_clearGone);
		mLL_long = (LinearLayout) findViewById(R.id.ll_long);
		mLL_width = (LinearLayout) findViewById(R.id.ll_width);
		mLL_deep = (LinearLayout) findViewById(R.id.ll_deep);
		mLL_area = (LinearLayout) findViewById(R.id.ll_area);
		mLL_volume = (LinearLayout) findViewById(R.id.ll_volume);
		mLL_count = (LinearLayout) findViewById(R.id.ll_count);
		mTv_area = (TextView) findViewById(R.id.tv_area);
		mTv_volume = (TextView) findViewById(R.id.tv_volume);
		mTv_locationDesc = (TextView) findViewById(R.id.tv_locationDesc);

		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_type = (TextView) findViewById(R.id.tv_type);
		mTv_range = (TextView) findViewById(R.id.tv_routeRange);
		mTv_orientation = (TextView) findViewById(R.id.tv_orientation);
		mTv_problemType = (TextView) findViewById(R.id.tv_problemType);
		mTv_laneLocation = (TextView) findViewById(R.id.tv_laneLocation);
		mTv_landmark = (TextView) findViewById(R.id.tv_landmark);
		mTv_diseaseType = (TextView) findViewById(R.id.tv_diseaseType);
		mTv_count = (TextView) findViewById(R.id.tv_count);
		mTv_long = (TextView) findViewById(R.id.tv_long);
		mTv_width = (TextView) findViewById(R.id.tv_width);
		mTv_deep = (TextView) findViewById(R.id.tv_deep);
		mTv_level = (TextView) findViewById(R.id.tv_level);
		mTv_nature = (TextView) findViewById(R.id.tv_nature);
		mTv_curingSuggest = (TextView) findViewById(R.id.tv_curingSuggest);
		mTv_cause = (TextView) findViewById(R.id.tv_cause);

		mBtn_delete = (Button) findViewById(R.id.btn_delete);
		mBtn_edit = (Button) findViewById(R.id.btn_edit);
		mGv_damageImg = (GridView) findViewById(R.id.gv_damageImg);

		mBtn_delete.setOnClickListener(this);
		mBtn_edit.setOnClickListener(this);
		mBtn_back.setOnClickListener(this);
		// mBtn_menu.setOnClickListener(this);
		mBtn_search.setOnClickListener(this);

		mLayout_pavement = (LinearLayout) findViewById(R.id.layout_pavement);
		mLayout_routine = (LinearLayout) findViewById(R.id.layout_routine);
		mTv_data_pavement = (TextView) findViewById(R.id.tv_data_pavement);
		mTv_route_pavement = (TextView) findViewById(R.id.tv_route_pavement);
		mTv_user_pavement = (TextView) findViewById(R.id.tv_user_pavement);
		mTv_section_pavement = (TextView) findViewById(R.id.tv_section_pavement);
		mTv_direction_pavement = (TextView) findViewById(R.id.tv_direction_pavement);

		mLL_user = (LinearLayout) findViewById(R.id.ll_user);
		mLL_bottomMenu = (LinearLayout) findViewById(R.id.layout_bottom);
		if (showEdit) {
			mLL_user.setVisibility(View.VISIBLE);
			mLL_bottomMenu.setVisibility(View.VISIBLE);
		}

	}

	private void setUI() {
		int orientation = mDisease.getOrientation();
		if (orientation == 0) {
			mTv_range.setText(mRoute.getSTARTSTAKE() + "~"
					+ mRoute.getENDSTAKE());
			mTv_orientation.setText("上行");
		} else {
			mTv_range.setText(mRoute.getDOWN_START_STAKE_NUM() + "~"
					+ mRoute.getDOWN_END_STAKE_NUM());
			mTv_orientation.setText("下行");
		}

		mTv_laneLocation.setText(mDisease.getLaneLocation());
		mTv_landmark.setText("K" + mDisease.getLandmarkStart() + "+"
				+ mDisease.getLandmarkEnd());

		int problemType = mDisease.getProblemType();
		mTv_problemType.setText(problemType == 0 ? "病害" : "保洁");
		if (problemType == 1) {
			mLL_clearGone.setVisibility(View.GONE);
		}

		mTv_curingSuggest.setText(mDisease.getAdvice());
		mTv_cause.setText(mDisease.getCause());

		if (mDssType != null) {
			mTv_diseaseType.setText(mDssType.getDSS_TYPE_NAME());
		}

		String sheshiType = mDisease.getFacilityCat();
		if ("SNLM".equals(sheshiType)) {
			mTv_type.setText("设施类型:水泥路面");
		} else if ("JA".equals(sheshiType)) {
			mTv_type.setText("设施类型:沿线设施");
		} else if ("LJ".equals(sheshiType)) {
			mTv_type.setText("设施类型:路基");
		} else if ("LH".equals(sheshiType)) {
			mTv_type.setText("设施类型:绿化");
		} else {
			mTv_type.setText("设施类型:沥青路面");
		}

		// switch (type) {
		// case 0:
		// mLayout_routine.setVisibility(View.VISIBLE);
		// mTv_route.setText("路线：" + mRoute.getLINE_ALLNAME());
		// mTv_date.setText("日期：" + mDisease.getDate());
		//
		// break;
		//
		// default:
		mLayout_pavement.setVisibility(View.VISIBLE);
		mTv_data_pavement.setText(mDisease.getDate());
		mTv_route_pavement.setText(mRoute.getLINE_ALLNAME());
		mTv_user_pavement.setText(mDisease.getUser());
		mTv_direction_pavement.setText(mDisease.getDirection());
		mTv_section_pavement.setText(mDisease.getSection());
		mTv_locationDesc.setText(mDisease.getLocationDesc());
		// break;
		// }

		int level = mDisease.getLevel();
		switch (level) {
		case 0:
			mTv_level.setText("轻");
			break;
		case 1:
			mTv_level.setText("中");
			break;
		case 2:
			mTv_level.setText("重");
			break;

		default:
			break;
		}

		int nature = mDisease.getNature();
		switch (nature) {
		case 0:
			mTv_nature.setText("新病害");
			break;
		case 1:
			mTv_nature.setText("旧病害");
			break;
		case 2:
			mTv_nature.setText("修复后损坏");
			break;

		default:
			break;
		}

		String paths = mDisease.getPath();
		if (!TextUtils.isEmpty(paths)) {
			String[] pathArray = paths.split(",");
			for (String string : pathArray) {
				mList_data.add(string);
			}
		}

		mGv_damageImg.setAdapter(new CommonAdapter<String>(mContext,
				mList_data, R.layout.gv_item_damageimg) {

			@Override
			public void convert(ViewHolder helper, String item, int position) {
				// TODO Auto-generated method stub
				ImageView iv = helper.getView(R.id.iv_damageImg);
				MainApplication.setImgByPath(item, iv);
			}
		});

		if (mDssType != null) {
			setInputUI();
		}
	}

	/**
	 * 设置输入长、宽、深等 UI 显示/隐藏
	 */
	private void setInputUI() {
		int dss_A = mDssType.getHAVE_DSS_A();
		int dss_D = mDssType.getHAVE_DSS_D();
		int dss_L = mDssType.getHAVE_DSS_L();
		int dss_N = mDssType.getHAVE_DSS_N();
		int dss_V = mDssType.getHAVE_DSS_V();
		int dss_W = mDssType.getHAVE_DSS_W();

		if (dss_A == 0) {
			mLL_area.setVisibility(View.GONE);
		} else {
			mLL_area.setVisibility(View.VISIBLE);
			mTv_area.setText(mDisease.getArea() + mDssType.getDSS_A_UNIT());
		}

		if (dss_D == 0) {
			mLL_deep.setVisibility(View.GONE);
		} else {
			mLL_deep.setVisibility(View.VISIBLE);
			mTv_deep.setText(mDisease.getDeep() + mDssType.getDSS_D_UNIT());
		}

		if (dss_L == 0) {
			mLL_long.setVisibility(View.GONE);
		} else {
			mLL_long.setVisibility(View.VISIBLE);
			mTv_long.setText(mDisease.getLength() + mDssType.getDSS_L_UNIT());
		}

		if (dss_N == 0) {
			mLL_count.setVisibility(View.GONE);
		} else {
			mLL_count.setVisibility(View.VISIBLE);
			mTv_count.setText(mDisease.getCount() + mDssType.getDSS_N_UNIT());
		}

		if (dss_V == 0) {
			mLL_volume.setVisibility(View.GONE);
		} else {
			mLL_volume.setVisibility(View.VISIBLE);
			mTv_volume.setText(mDisease.getVolume() + mDssType.getDSS_V_UNIT());
		}

		if (dss_W == 0) {
			mLL_width.setVisibility(View.GONE);
		} else {
			mLL_width.setVisibility(View.VISIBLE);
			mTv_width.setText(mDisease.getWidth() + mDssType.getDSS_W_UNIT());
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_delete:
			DBHelperSingleton.getInstance().deleteData(Mod_disease.class,
					"id='" + id + "'");
			// for (Mod_disease d : MainApplication.mList_disease) {
			// if (d.getId() == id) {
			// mList_data.remove(d);
			// break;
			// }
			// }
			super.showToast("删除成功");
			finish();
			break;
		case R.id.btn_edit:
			Intent intent = new Intent();
			// switch (mDisease.getType()) {
			// case 0:
			// intent.setClass(mContext, InputRoutineInspection.class);
			// break;
			// case 1:
			// case 2:
			// case 3:
			// case 4:
			// intent.setClass(mContext, InputDisease_pavement.class);
			// break;
			// case 5:
			// case 6:
			// case 7:
			// case 8:
			// intent.setClass(mContext, InputDisease_structures.class);
			// break;
			// default:
			// intent.setClass(mContext, InputRoutineInspection.class);
			// break;
			// }
			intent.setClass(mContext, InputRoutineInspection.class);
			intent.putExtra("id", id);
			intent.putExtra("position", getIntent().getIntExtra("position", 0));
			intent.putExtra("action", "edit");
			startActivity(intent);
			break;
		case R.id.btn_search:
			Intent i = new Intent();
			switch (mDisease.getType()) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				i.setClass(mContext, DiseaseList.class);
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				i.setClass(mContext, ConclusionList.class);
				break;
			default:
				break;
			}
			startActivity(i);
			break;

		case R.id.btn_menu:
			if (mPPW_menu == null) {
				initPPW();
			}
			mPPW_menu.showAsDropDown(v);
			break;

		case R.id.tv_ppwItem1:
			finish();
			break;

		default:
			break;
		}
	}

	private PopupWindow mPPW_menu;

	@SuppressWarnings("deprecation")
	private void initPPW() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_ppwItem1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_ppwItem2 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_ppwItem1.setOnClickListener(this);
		tv_ppwItem2.setVisibility(View.GONE);
		mPPW_menu = new PopupWindow(layout);
		mPPW_menu.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		// // 控制popupwindow点击屏幕其他地方消失
		mPPW_menu.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片，不能在布局中设置，要通过代码来设置
		mPPW_menu.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

		mPPW_menu.setWidth(LayoutParams.WRAP_CONTENT);
		mPPW_menu.setHeight(LayoutParams.WRAP_CONTENT);

	}
}
