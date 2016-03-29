package com.zggk.zggkandroid.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.Data;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.dao.DmDinspRecordDao;
import com.zggk.zggkandroid.dao.DmFinspRecordDao;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.utils.DataUtils;
import com.zggk.zggkandroid.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 录入病害界面
 * 
 * @author xsh
 * 
 */
public class InputRoutineInspection extends BaseActivity implements
		OnClickListener {

	private Context mContext = InputRoutineInspection.this;
	private ImageButton mBtn_back, mBtn_menu, mBtn_search;
	private TextView mTv_title, mTv_route, mTv_date, mTv_type;
	private LinearLayout mLayout_input1, mLayout_input2, mLayout_input3;
	private RelativeLayout mLayout_laneLocation, mLayout_level,
			mLayout_diseaseType, mLayout_curingSuggest, mLayout_structure,
			mLayout_component, mLayout_material, mLayout_component2,
			mLayout_parts, mLayout_scale;
	private RadioGroup mRg_problemType, mRg_orientation, mRg_level, mRg_nature;
	private RadioButton mRB_disease, mRB_clearing, mRB_up, mRB_down, mRB_light,
			mRB_middle, mRB_serious, mRB_newDisease, mRB_oldDisease,
			mRB_againDisease;
	private TextView mTv_laneLocation, mTv_diseaseType, mTv_curingSuggest,
			mTv_structure, mTv_component, mTv_material, mTv_component2,
			mTv_parts, mTv_scale, mTv_stake;
	private EditText mEt_landmark1, mEt_landmark2;
	private EditText mEt_lenght, mEt_width, mEt_deep, mEt_area, mEt_count,
			mEt_volum, mEt_percantage, mEt_angle;
	private EditText mEt_cause, mEt_locationDesc;
	private GridView mGv_damageImg;
	private GvAdapter mGvAdapter;
	private List<String> mList_photo = new ArrayList<String>();

	private Button mBtn_save,mBtn_GetZhuangHao;

	private boolean isInit = false;

	private PopupWindow mPPW_menu;

	private Intent mIntent;
	private String mAction;
	private Mod_disease mDisease;

	private int mProblemType, mOrientation, mLevel, mNature;
	private boolean isPavement = true;

	private List<String> mList_sheshiType;

	/**
	 * 设施类型--0=沥青路面，1=水泥路面，2=路基，3=沿线设施，4=绿化
	 */
	private int mSheshiType;

	private List<String> mList_laneLocation;
	private String mLaneLocation;// 车道位置、编码
	private DssTypeEntity mDssType;
	private RouteEntity mRoute;

	private LinearLayout mLL_lenght, mLL_width, mLL_deep, mLL_area, mLL_count,
			mLL_volume;
	private TextView mTv_lenghtUnit, mTv_widthUnit, mTv_deepUnit, mTv_areaUnit,
			mTv_countUnit, mTv_volumeUnit;
	private TextView mTv_lenghtTag, mTv_widthTag, mTv_deepTag, mTv_areaTag,
			mTv_countTag, mTv_volumeTag;

	private LinearLayout mLL_goneWithClean;

	private Mod_disease mDSS_main;// 主单传来的数据
	private int mType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_routine_inspection);

		mIntent = getIntent();
		mAction = mIntent.getStringExtra("action");

		initView1();

		mList_sheshiType = Arrays.asList(getResources().getStringArray(
				R.array.sheshiType));

		boolean loaded = DataUtils.isDataLoaded(DssTypeEntity.class);
		if (!loaded) {
			DataUtils.getDiseaseType(mContext);
		}
	}

	/**
	 * 初始化录入路面时的界面
	 */
	private void initView1() {
		// TODO Auto-generated method stub
		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mBtn_search = (ImageButton) findViewById(R.id.btn_search);
		mBtn_search.setVisibility(View.VISIBLE);
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_type = (TextView) findViewById(R.id.tv_type);

		mLayout_laneLocation = (RelativeLayout) findViewById(R.id.layout_laneLocation);
		mLayout_level = (RelativeLayout) findViewById(R.id.layout_level);
		mLayout_diseaseType = (RelativeLayout) findViewById(R.id.layout_diseaseType);
		mLayout_curingSuggest = (RelativeLayout) findViewById(R.id.layout_curingSuggest);

		mTv_laneLocation = (TextView) findViewById(R.id.tv_laneLocation);
		mTv_diseaseType = (TextView) findViewById(R.id.tv_diseaseType);
		mTv_curingSuggest = (TextView) findViewById(R.id.tv_curingSuggest);
		mTv_stake = (TextView) findViewById(R.id.tv_stake);

		mLL_goneWithClean = (LinearLayout) findViewById(R.id.ll_goneWithClean);
		mLL_lenght = (LinearLayout) findViewById(R.id.ll_lenght);
		mLL_width = (LinearLayout) findViewById(R.id.ll_width);
		mLL_deep = (LinearLayout) findViewById(R.id.ll_deep);
		mLL_area = (LinearLayout) findViewById(R.id.ll_area);
		mLL_count = (LinearLayout) findViewById(R.id.ll_count);
		mLL_volume = (LinearLayout) findViewById(R.id.ll_volume);
		mTv_lenghtUnit = (TextView) findViewById(R.id.tv_lenghtUnit);
		mTv_widthUnit = (TextView) findViewById(R.id.tv_widthUnit);
		mTv_deepUnit = (TextView) findViewById(R.id.tv_deepUnit);
		mTv_areaUnit = (TextView) findViewById(R.id.tv_areaUnit);
		mTv_countUnit = (TextView) findViewById(R.id.tv_countUnit);
		mTv_volumeUnit = (TextView) findViewById(R.id.tv_volumeUnit);

		mTv_lenghtTag = (TextView) findViewById(R.id.tv_lenghtTag);
		mTv_widthTag = (TextView) findViewById(R.id.tv_widthTag);
		mTv_deepTag = (TextView) findViewById(R.id.tv_deepTag);
		mTv_areaTag = (TextView) findViewById(R.id.tv_areaTag);
		mTv_countTag = (TextView) findViewById(R.id.tv_countTag);
		mTv_volumeTag = (TextView) findViewById(R.id.tv_volumeTag);

		mEt_landmark1 = (EditText) findViewById(R.id.et_landmark1);
		mEt_landmark2 = (EditText) findViewById(R.id.et_landmark2);
		mEt_lenght = (EditText) findViewById(R.id.et_long);
		mEt_width = (EditText) findViewById(R.id.et_width);
		mEt_deep = (EditText) findViewById(R.id.et_deep);
		mEt_area = (EditText) findViewById(R.id.et_area);
		mEt_count = (EditText) findViewById(R.id.et_count);
		mEt_volum = (EditText) findViewById(R.id.et_volume);
		mEt_cause = (EditText) findViewById(R.id.et_cause);
		mEt_locationDesc = (EditText) findViewById(R.id.et_locationDesc);

		mRB_disease = (RadioButton) findViewById(R.id.rb_disease);
		mRB_clearing = (RadioButton) findViewById(R.id.rb_cleaning);
		mRg_problemType = (RadioGroup) findViewById(R.id.rg_problemType);
		mRg_orientation = (RadioGroup) findViewById(R.id.rg_orientation);
		mRg_level = (RadioGroup) findViewById(R.id.rg_level);
		mRg_nature = (RadioGroup) findViewById(R.id.rg_nature);
		mRB_up = (RadioButton) findViewById(R.id.rb_up);
		mRB_down = (RadioButton) findViewById(R.id.rb_down);

		RGCheckedChangedListener RGL = new RGCheckedChangedListener();
		mRg_level.setOnCheckedChangeListener(RGL);
		mRg_nature.setOnCheckedChangeListener(RGL);
		mRg_orientation.setOnCheckedChangeListener(RGL);
		mRg_problemType.setOnCheckedChangeListener(RGL);

		mBtn_save = (Button) findViewById(R.id.btn_save);
//		mBtn_add = (Button) findViewById(R.id.btn_add);
//		mBtn_sub = (Button) findViewById(R.id.btn_sub);
		mBtn_save.setOnClickListener(this);
//		mBtn_add.setOnClickListener(this);
//		mBtn_sub.setOnClickListener(this);
		mBtn_GetZhuangHao=(Button)findViewById(R.id.btn_getzh);

		mBtn_back.setOnClickListener(this);
		mBtn_search.setOnClickListener(this);
		mLayout_laneLocation.setOnClickListener(this);
		mLayout_diseaseType.setOnClickListener(this);
		mLayout_curingSuggest.setOnClickListener(this);
		mTv_type.setOnClickListener(this);
		mBtn_GetZhuangHao.setOnClickListener(this);

		EditWatcher watcher = new EditWatcher();
		mEt_lenght.addTextChangedListener(watcher);
		mEt_width.addTextChangedListener(watcher);

		mList_photo.add("tag");
		mGv_damageImg = (GridView) findViewById(R.id.gv_damageImg);
		mGvAdapter = new GvAdapter(mContext, mList_photo,
				R.layout.gv_item_damageimg);
		mGv_damageImg.setAdapter(mGvAdapter);
		mGv_damageImg.setOnItemClickListener(new GvItemClickListener());

		if (mAction.equals("input")) {
			Bundle b = getIntent().getBundleExtra("data");
			mDisease = (Mod_disease) b.getSerializable("data");
			if (mDisease==null) mDisease=new Mod_disease();
			mRoute = mDisease.getRoute();
			mType = mDisease.getType();
			String fromList = mIntent.getStringExtra("fromList");
			if (fromList != null) {
				if (fromList.equals("fromList")) {
					Mod_disease disease = new Mod_disease();
					disease.setZhuId(mDisease.getZhuId());
					disease.setDate(mDisease.getDate());
					disease.setUser(mDisease.getUser());
					disease.setRoute(mDisease.getRoute());
					disease.setId(String.valueOf(System.currentTimeMillis()));
					disease.setCarNum(mDisease.getCarNum());
					disease.setCuringUnits(mDisease.getCuringUnits());
					disease.setInspTimeIntvlEnd(mDisease.getInspTimeIntvlEnd());
					disease.setInspTimeIntvlStart(mDisease
							.getInspTimeIntvlStart());
					disease.setLineID(mDisease.getLineID());
					disease.setLineName(mDisease.getLineName());
					disease.setMileage(mDisease.getMileage());
					disease.setWeather(mDisease.getWeather());
					disease.setSection(mDisease.getSection());
					mDisease = null;
					mDisease = disease;
				}
			}
			mDSS_main = (Mod_disease) b.getSerializable("data");

			mBtn_menu = (ImageButton) findViewById(R.id.btn_menu);
			mBtn_menu.setVisibility(View.GONE);
			mBtn_menu.setOnClickListener(this);
			mTv_date.setText("日期:" + mDisease.getDate());

			mTv_route.setText(mRoute.getLINE_ALLNAME());
			mTv_stake.setText(getString(R.string.stake_range,
					mRoute.getSTARTSTAKE(), mRoute.getENDSTAKE()));
		} else {
			String id = getIntent().getStringExtra("id");
			mDisease = (Mod_disease) DBHelperSingleton.getInstance().getObject(
					Mod_disease.class, "id='" + id + "'");
			if (mDisease==null) mDisease=new Mod_disease();
			mRoute = (RouteEntity) DBHelperSingleton.getInstance()
					.getObject(RouteEntity.class,
							"LINE_ID='" + mDisease.getLineID() + "'");
			if (mRoute==null) mRoute=new RouteEntity();
			mDisease.setRoute(mRoute);
			mDssType = (DssTypeEntity) DBHelperSingleton.getInstance()
					.getObject(DssTypeEntity.class,
							"DSS_TYPE='" + mDisease.getDSS_TYPE() + "'");
			mType=mDisease.getType();
			initEdit();
		}
		mSheshiType = mType;
		switch (mType) {
		case 0:
			mTv_title.setText("日常巡查");
			mTv_type.setClickable(true);
			mTv_type.setBackgroundResource(R.drawable.sp_et_normal);
			break;
		case 1:
			mTv_title.setText("路面经常检查");
			mTv_type.setText("路面");
			mTv_type.setClickable(false);
			mRB_clearing.setVisibility(View.GONE);
			break;
		case 2:
			mTv_title.setText("路基经常检查");
			mTv_type.setText("路基");
			mTv_type.setClickable(false);
			mLayout_laneLocation.setVisibility(View.GONE);
			mRB_clearing.setVisibility(View.GONE);
			break;
		case 3:
			mTv_title.setText("设施经常检查");
			mTv_type.setText("沿线设施");
			mTv_type.setClickable(false);
			mLayout_laneLocation.setVisibility(View.GONE);
			mRB_clearing.setVisibility(View.GONE);
			break;
		case 4:
			mTv_title.setText("绿化经常检查");
			mTv_type.setText("绿化");
			mTv_type.setClickable(false);
			mLayout_laneLocation.setVisibility(View.GONE);
			mRB_clearing.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化录入的不是路面病害时的界面
	 */
	private void initView2() {
		mLayout_input1 = (LinearLayout) findViewById(R.id.layout_input1);
		mLayout_input2 = (LinearLayout) findViewById(R.id.layout_input2);
		mLayout_input3 = (LinearLayout) findViewById(R.id.layout_input3);

		mLayout_structure = (RelativeLayout) findViewById(R.id.layout_structure);
		mLayout_component = (RelativeLayout) findViewById(R.id.layout_component);
		mLayout_component2 = (RelativeLayout) findViewById(R.id.layout_component2);
		mLayout_material = (RelativeLayout) findViewById(R.id.layout_material);
		mLayout_parts = (RelativeLayout) findViewById(R.id.layout_parts);
		mLayout_scale = (RelativeLayout) findViewById(R.id.layout_scale);

		mLayout_scale.setOnClickListener(this);
		mLayout_structure.setOnClickListener(this);
		mLayout_component.setOnClickListener(this);
		mLayout_component2.setOnClickListener(this);
		mLayout_material.setOnClickListener(this);
		mLayout_parts.setOnClickListener(this);

		mTv_structure = (TextView) findViewById(R.id.tv_structureName);
		mTv_component = (TextView) findViewById(R.id.tv_component);
		mTv_component2 = (TextView) findViewById(R.id.tv_component2);
		mTv_material = (TextView) findViewById(R.id.tv_material);
		mTv_parts = (TextView) findViewById(R.id.tv_parts);
		mTv_scale = (TextView) findViewById(R.id.tv_scale);

		mEt_percantage = (EditText) findViewById(R.id.et_percentage);
		mEt_angle = (EditText) findViewById(R.id.et_angle);

		isInit = true;
	}

	/**
	 * 以编辑状态进入页面时额外初始化的数据
	 */
	private void initEdit() {
		isPavement = TextUtils.isEmpty(mDisease.getStructureName());
		if (!isPavement) {
			showStructureView();
			mTv_structure.setText(mDisease.getStructureName());
			mTv_component.setText(mDisease.getComponent());
			mTv_component2.setText(mDisease.getComponent2());
			mTv_material.setText(mDisease.getMaterial());
			mTv_parts.setText(mDisease.getParts());
			mTv_scale.setText(mDisease.getScale());
			mEt_angle.setText(mDisease.getAngle());
			mEt_percantage.setText(mDisease.getPercantage());
		} else {
			int type = mDisease.getType();
			switch (type) {
			case 0:
				mTv_type.setText("沥青路面");
				break;
			case 1:
				mTv_type.setText("水泥路面");
				break;
			case 2:
				mTv_type.setText("路基");
				break;
			case 3:
				mTv_type.setText("设施");
				break;
			case 4:
				mTv_type.setText("绿化");
				break;

			default:
				break;
			}
			mTv_laneLocation.setText(mDisease.getLaneLocation());

			mRB_light = (RadioButton) findViewById(R.id.rb_light);
			mRB_middle = (RadioButton) findViewById(R.id.rb_middle);
			mRB_serious = (RadioButton) findViewById(R.id.rb_serious);
			int level = mDisease.getLevel();
			switch (level) {
			case 1:
				mRB_middle.setChecked(true);
				break;
			case 2:
				mRB_serious.setChecked(true);
				break;
			default:
				mRB_light.setChecked(true);
				break;
			}
		}

		mRB_disease = (RadioButton) findViewById(R.id.rb_disease);
		mRB_clearing = (RadioButton) findViewById(R.id.rb_cleaning);
		mRB_oldDisease = (RadioButton) findViewById(R.id.rb_oldDisease);
		mRB_newDisease = (RadioButton) findViewById(R.id.rb_newDisease);
		mRB_againDisease = (RadioButton) findViewById(R.id.rb_againDisease);

		if (mDisease.getProblemType() == 1) {
			mRB_clearing.setChecked(true);
			mLayout_diseaseType.setVisibility(View.GONE);
			mLayout_curingSuggest.setVisibility(View.GONE);
			mLL_count.setVisibility(View.GONE);
			mLL_goneWithClean.setVisibility(View.GONE);
		} else {
			mRB_disease.setChecked(true);
		}

		if (mDisease.getOrientation() == 1) {
			mRB_down.setChecked(true);
			mTv_stake.setText(getString(R.string.stake_range,
					mRoute.getDOWN_START_STAKE_NUM(),
					mRoute.getDOWN_END_STAKE_NUM()));
		} else {
			mRB_up.setChecked(true);
			mTv_stake.setText(getString(R.string.stake_range,
					mRoute.getSTARTSTAKE(), mRoute.getENDSTAKE()));
		}

		int nature = mDisease.getNature();
		switch (nature) {
		case 1:
			mRB_oldDisease.setChecked(true);
			break;
		case 2:
			mRB_againDisease.setChecked(true);
			break;
		default:
			mRB_newDisease.setChecked(true);
			break;
		}

		mTv_route.setText("路线:" + mRoute.getLINE_ALLNAME());
		mTv_date.setText("日期:" + mDisease.getDate());
		mTv_diseaseType.setText(mDisease.getDiseaseType());
		mTv_curingSuggest.setText(mDisease.getAdvice());
		mEt_landmark1.setText(mDisease.getLandmarkStart());
		mEt_landmark2.setText(mDisease.getLandmarkEnd());
		mEt_lenght.setText(mDisease.getLength());
		mEt_width.setText(mDisease.getWidth());
		mEt_deep.setText(mDisease.getDeep());
		mEt_area.setText(mDisease.getArea());
		mEt_count.setText(mDisease.getCount());
		mEt_volum.setText(mDisease.getVolume());
		mEt_cause.setText(mDisease.getCause());

		String paths = mDisease.getPath();
		if (!TextUtils.isEmpty(paths)) {
			String[] pathArray = paths.split(",");
			for (String str : pathArray) {
				mList_photo.add(str);
			}
			mGvAdapter.notifyDataSetChanged();
		}

		setInputUI();
	}

	/**
	 * 显示路面视图
	 */
	private void showPavementView() {
		if (!isInit) {
			initView2();
		}
		mLayout_input1.setVisibility(View.GONE);
		mLayout_input2.setVisibility(View.GONE);
		mLayout_input3.setVisibility(View.GONE);
		mLayout_scale.setVisibility(View.GONE);

		mLayout_laneLocation.setVisibility(View.VISIBLE);
		mLayout_level.setVisibility(View.VISIBLE);

	}

	/**
	 * 显示建筑物视图
	 */
	private void showStructureView() {
		if (!isInit) {
			initView2();
		}
		mLayout_input1.setVisibility(View.VISIBLE);
		mLayout_input2.setVisibility(View.VISIBLE);
		mLayout_input3.setVisibility(View.VISIBLE);
		mLayout_scale.setVisibility(View.VISIBLE);

		mLayout_laneLocation.setVisibility(View.GONE);
		mLayout_level.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_menu:
			if (mPPW_menu == null) {
				initPPW();
			}
			mPPW_menu.showAsDropDown(v);
			break;
		case R.id.btn_search:
			Intent intent = new Intent();
			intent.setClass(mContext, DiseaseList.class);
			intent.putExtra("search", true);
			intent.putExtra("showInputBtn", true);
			startActivity(intent);
			break;
		case R.id.tv_ppwItem1:
			showPavementView();
			mPPW_menu.dismiss();
			break;
		case R.id.tv_ppwItem2:
			showStructureView();
			mPPW_menu.dismiss();
			break;
		case R.id.btn_save:
			save();
			break;
		case R.id.layout_laneLocation:
			if (mRB_up.isChecked()) {
				mList_laneLocation = Arrays.asList(getResources()
						.getStringArray(R.array.laneLocation_up));
			} else {
				mList_laneLocation = Arrays.asList(getResources()
						.getStringArray(R.array.laneLocation_down));
			}
			Constant.getInstance().showSelectDialog(mContext,
					mList_laneLocation, mTv_laneLocation,
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							String str = mList_laneLocation.get(position);
							mLaneLocation = str.charAt(str.length() - 1) + "";
							// showToast("编码--" + mLaneLocation);
						}
					});
			break;
		case R.id.layout_diseaseType:
			changeDssTypeData();
			if (mList_dssType != null) {
				showDssTypeDialog();
			} else {
				showToast("病害数据获取中...");
				DataUtils.getDiseaseType(mContext);
			}
			break;
		case R.id.layout_curingSuggest:
			Constant.getInstance().showSelectDialog(mContext,
					Data.curingSuggest(), mTv_curingSuggest);
			break;
		case R.id.layout_structure:
			Constant.getInstance().showSelectDialog(mContext, Data.structure(),
					mTv_structure);
			break;
		case R.id.layout_scale:
			Constant.getInstance().showSelectDialog(mContext, Data.scale(),
					mTv_scale);
			break;
		case R.id.layout_component:
		case R.id.layout_component2:
		case R.id.layout_material:
		case R.id.layout_parts:
			showSelect(v.getId());
			break;
		case R.id.tv_type:
			Constant.getInstance().showSelectDialog(mContext, mList_sheshiType,
					mTv_type, new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							mSheshiType = position;
							mTv_diseaseType.setText("");
							mDssType = null;

							if (position == 0 || position == 1) {
								mLayout_laneLocation
										.setVisibility(View.VISIBLE);
							} else {
								mLayout_laneLocation.setVisibility(View.GONE);
							}
							// if (position == 0 || position == 1) {
							// showPavementView();
							// } else {
							// showStructureView();
							// }
						}
					});
			break;
//		case R.id.btn_add:
//			String stakeEnd = mEt_landmark2.getText().toString();
//			if (!TextUtils.isEmpty(stakeEnd)) {
//				int i = Integer.valueOf(stakeEnd);
//				i += 10;
//				mEt_landmark2.setText(i + "");
//			}
//			break;
//		case R.id.btn_sub:
//			String stakeEnd1 = mEt_landmark2.getText().toString();
//			if (!TextUtils.isEmpty(stakeEnd1)) {
//				int i = Integer.valueOf(stakeEnd1);
//				i -= 10;
//				mEt_landmark2.setText(i + "");
//			}
//
//			break;
		case R.id.btn_getzh:
				break;
		default:
			break;
		}
	}

	private void showDssTypeDialog() {
		final Dialog dialog = new Dialog(mContext);
		Window window = dialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_select,
				null);
		ListView lv_select = (ListView) view.findViewById(R.id.lv_select);

		lv_select.setAdapter(new CommonAdapter<DssTypeEntity>(mContext,
				mList_dssType, R.layout.lv_item_text) {

			@Override
			public void convert(ViewHolder helper, DssTypeEntity item,
					int position) {
				// TODO Auto-generated method stub
				TextView tv = helper.getView(R.id.tv_item);
				tv.setText(item.getDSS_TYPE_NAME());
			}
		});

		lv_select.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mDssType = mList_dssType.get(position);
				mTv_diseaseType.setText(mList_dssType.get(position)
						.getDSS_TYPE_NAME());
				setInputUI();
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
			mTv_areaUnit.setText(mDssType.getDSS_A_UNIT());
		}

		if (dss_D == 0) {
			mLL_deep.setVisibility(View.GONE);
		} else {
			mLL_deep.setVisibility(View.VISIBLE);
			mTv_deepUnit.setText(mDssType.getDSS_D_UNIT());
		}

		if (dss_L == 0) {
			mLL_lenght.setVisibility(View.GONE);
		} else {
			mLL_lenght.setVisibility(View.VISIBLE);
			mTv_lenghtUnit.setText(mDssType.getDSS_L_UNIT());
		}

		if (dss_N == 0) {
			mLL_count.setVisibility(View.GONE);
		} else {
			mLL_count.setVisibility(View.VISIBLE);
			mTv_countUnit.setText(mDssType.getDSS_N_UNIT());
		}

		if (dss_V == 0) {
			mLL_volume.setVisibility(View.GONE);
		} else {
			mLL_volume.setVisibility(View.VISIBLE);
			mTv_volumeUnit.setText(mDssType.getDSS_V_UNIT());
		}

		if (dss_W == 0) {
			mLL_width.setVisibility(View.GONE);
		} else {
			mLL_width.setVisibility(View.VISIBLE);
			mTv_widthUnit.setText(mDssType.getDSS_W_UNIT());
		}

		mTv_areaTag.setText("面积：");
		mTv_lenghtTag.setText("长：");
		mTv_widthTag.setText("宽：");
		mTv_deepTag.setText("深：");
		mTv_countTag.setText("数量：");
		mTv_volumeTag.setText("体积：");

		String colom = mDssType.getHAVE_DSS_COLOM();
		if ("DSS_A".equals(colom)) {
			mTv_areaTag.setText("*面积：");
		} else if ("DSS_L".equals(colom)) {
			mTv_lenghtTag.setText("*长：");
		} else if ("DSS_W".equals(colom)) {
			mTv_widthTag.setText("*宽：");
		} else if ("DSS_D".equals(colom)) {
			mTv_deepTag.setText("*深：");
		} else if ("DSS_N".equals(colom)) {
			mTv_countTag.setText("*数量：");
		} else if ("DSS_V".equals(colom)) {
			mTv_countTag.setText("*体积：");
		}

	}

	private List<DssTypeEntity> mList_dssType;

	private void changeDssTypeData() {
		switch (mSheshiType) {
		case 0:
			mList_dssType = DBHelperSingleton.getInstance()
					.getDataWithConditions(DssTypeEntity.class, null,
							"STRUCTCOMPCODE=?", "LMLQ", null, null, null, null);
			// mList_dssType =
			// DBHelperSingleton.getInstance().getDatas(DssTypeEntity.class,
			// "DSS_TYPE LIKE 'LMLQ%'");
			break;
		case 1:
			mList_dssType = DBHelperSingleton.getInstance()
					.getDataWithConditions(DssTypeEntity.class, null,
							"STRUCTCOMPCODE=?", "LMSN", null, null, null, null);
			// mList_dssType =
			// DBHelperSingleton.getInstance().getDatas(DssTypeEntity.class,
			// "DSS_TYPE LIKE 'LMSN%'");
			break;
		case 2:
			mList_dssType = DBHelperSingleton.getInstance()
					.getDataWithConditions(DssTypeEntity.class, null,
							"STRUCTCOMPCODE=?", "LJ", null, null, null, null);
			// mList_dssType =
			// DBHelperSingleton.getInstance().getDatas(DssTypeEntity.class,
			// "DSS_TYPE LIKE 'LJ%'");
			break;
		case 3:
			mList_dssType = DBHelperSingleton.getInstance()
					.getDataWithConditions(DssTypeEntity.class, null,
							"STRUCTCOMPCODE=?", "JA", null, null, null, null);
			// mList_dssType =
			// DBHelperSingleton.getInstance().getDatas(DssTypeEntity.class,
			// "DSS_TYPE LIKE 'JA%'");
			break;
		case 4:
			mList_dssType = DBHelperSingleton.getInstance()
					.getDataWithConditions(DssTypeEntity.class, null,
							"STRUCTCOMPCODE=?", "LH", null, null, null, null);
			// mList_dssType =
			// DBHelperSingleton.getInstance().getDatas(DssTypeEntity.class,
			// "DSS_TYPE LIKE 'LH%'");
			break;

		default:
			break;
		}
	}

	private void showSelect(int id) {
		String structure = mTv_structure.getText().toString();
		int type = 0;
		if (structure.equals("桥梁")) {
			type = 0;
		} else if (structure.equals("涵洞")) {
			type = 1;
		} else if (structure.equals("隧道")) {
			type = 2;
		} else if (structure.equals("边坡")) {
			type = 3;
		}
		List<String> list_data = null;
		TextView tv = null;
		switch (id) {
		case R.id.layout_component:
			tv = mTv_component;
			switch (type) {
			case 0:
				list_data = Data.component_brige();
				break;
			case 1:
				list_data = Data.component_culvert();
				break;
			case 2:
				list_data = Data.component_tunnel();
				break;
			case 3:
				list_data = Data.component_side();
				break;
			default:
				break;
			}
			break;
		case R.id.layout_component2:
			tv = mTv_component2;
			switch (type) {
			case 0:
				list_data = Data.component2_brige();
				break;
			case 1:
				list_data = Data.component2_culvert();
				break;
			case 2:
				list_data = Data.component2_tunnel();
				break;
			case 3:
				list_data = Data.component2_side();
				break;
			default:
				break;
			}
			break;
		case R.id.layout_material:
			tv = mTv_material;
			switch (type) {
			case 0:
				list_data = Data.material_bridge();
				break;
			case 1:
				list_data = Data.material_culvert();
				break;
			case 2:
				list_data = Data.material_tunnel();
				break;
			case 3:
				list_data = Data.material_side();
				break;
			default:
				break;
			}
			break;
		case R.id.layout_parts:
			tv = mTv_parts;
			switch (type) {
			case 0:
				list_data = Data.parts_bridge();
				break;
			case 1:
				list_data = Data.parts_culvert();
				break;
			case 2:
				list_data = Data.parts_tunnel();
				break;
			case 3:
				list_data = Data.parts_side();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		Constant.getInstance().showSelectDialog(mContext, list_data, tv);
	}

	private void save() {
		String Landmark1 = mEt_landmark1.getText().toString();
		String Landmark2 = mEt_landmark2.getText().toString();
		if (Landmark1 == null || "".equals(Landmark1)) {
			showToast("请输入桩号");
			return;
		}
		if (TextUtils.isEmpty(Landmark2)) {
			Landmark2 = "000";
		} else if (Landmark2.length() == 1) {
			Landmark2 = "00" + Landmark2;
		} else if (Landmark2.length() == 2) {
			Landmark2 = "0" + Landmark2;
		}
		float landmark = Float.valueOf(Landmark1 + "." + Landmark2);
		float routeStart = 0.0f;
		float routeEnd = 0.0f;
		if (mRB_up.isChecked()) {
			if (!TextUtils.isEmpty(mRoute.getSTARTSTAKE())) {
				routeStart = Float.valueOf(mRoute.getSTARTSTAKE());
			}
			if (!TextUtils.isEmpty(mRoute.getENDSTAKE())) {
				routeEnd = Float.valueOf(mRoute.getENDSTAKE());
			}
			if (landmark < routeStart || landmark > routeEnd) {
				showToast("请输入规定范围内桩号");
				return;
			}
		} else {
			if (!TextUtils.isEmpty(mRoute.getDOWN_START_STAKE_NUM())) {
				routeStart = Float.valueOf(mRoute.getDOWN_START_STAKE_NUM());
			}
			if (!TextUtils.isEmpty(mRoute.getDOWN_END_STAKE_NUM())) {
				routeEnd = Float.valueOf(mRoute.getDOWN_END_STAKE_NUM());
			}
			if (landmark > routeStart || landmark < routeEnd) {
				showToast("请输入规定范围内桩号");
				return;
			}
		}
		if (mRB_disease.isChecked()) {
			if (mDssType == null) {
				showToast("请选择病害类型");
				return;
			}
			String colom = mDssType.getHAVE_DSS_COLOM();
			String value = null;
			if ("DSS_A".equals(colom)) {
				value = mEt_area.getText().toString().trim();
				if (TextUtils.isEmpty(value)) {
					showToast("请输入面积");
					return;
				}
			} else if ("DSS_L".equals(colom)) {
				value = mEt_lenght.getText().toString().trim();
				if (TextUtils.isEmpty(value)) {
					showToast("请输入长度");
					return;
				}
			} else if ("DSS_W".equals(colom)) {
				value = mEt_width.getText().toString().trim();
				if (TextUtils.isEmpty(value)) {
					showToast("请输入宽度");
					return;
				}
			} else if ("DSS_D".equals(colom)) {
				value = mEt_deep.getText().toString().trim();
				if (TextUtils.isEmpty(value)) {
					showToast("请输入深度");
					return;
				}
			} else if ("DSS_N".equals(colom)) {
				value = mEt_count.getText().toString().trim();
				if (TextUtils.isEmpty(value)) {
					showToast("请输入数量");
					return;
				}
			} else if ("DSS_V".equals(colom)) {
				value = mEt_volum.getText().toString().trim();
				if (TextUtils.isEmpty(value)) {
					showToast("请输入体积");
					return;
				}
			}
		}

		if (mAction.equals("input")) {
			// mDisease.setId(MainApplication.getUUID());
			// mDisease.getRoute().setParentId(mDisease.getId() + "");
			// DBHelperSingleton.getInstance().insertOrReplaceData(
			// mDisease.getRoute());
		}
		mDisease.setType(mSheshiType);

		mDisease.setProblemType(mProblemType);
		mDisease.setOrientation(mOrientation);
		mDisease.setLandmarkStart(Landmark1);
		mDisease.setLandmarkEnd(Landmark2);
		mDisease.setDiseaseType(mTv_diseaseType.getText().toString());
		mDisease.setLength(mEt_lenght.getText().toString());
		mDisease.setWidth(mEt_width.getText().toString());
		mDisease.setDeep(mEt_deep.getText().toString());
		mDisease.setArea(mEt_area.getText().toString());
		mDisease.setCount(mEt_count.getText().toString());
		mDisease.setVolume(mEt_volum.getText().toString());
		mDisease.setNature(mNature);
		mDisease.setAdvice(mTv_curingSuggest.getText().toString());
		mDisease.setCause(mEt_cause.getText().toString());
		mDisease.setLocationDesc(mEt_locationDesc.getText().toString().trim());

		mList_photo.remove("tag");
		StringBuffer sb = new StringBuffer();
		for (String str : mList_photo) {
			sb.append(str).append(",");
		}
		if (!TextUtils.isEmpty(sb)) {
			sb = sb.deleteCharAt(sb.length() - 1);
			mDisease.setPath(sb.toString());
		}

		if (isPavement) {
			mDisease.setLaneLocation(mTv_laneLocation.getText().toString());
			mDisease.setLevel(mLevel);
		} else {
			mDisease.setAngle(mEt_angle.getText().toString());
			mDisease.setPercantage(mEt_percantage.getText().toString());
			mDisease.setBridge(mTv_structure.getText().toString());
			mDisease.setComponent(mTv_component.getText().toString());
			mDisease.setComponent2(mTv_component2.getText().toString());
			mDisease.setMaterial(mTv_material.getText().toString());
			mDisease.setParts(mTv_parts.getText().toString());
			mDisease.setScale(mTv_scale.getText().toString());
		}

		// if (mAction.equals("input")) {
		// MainApplication.mList_disease.add(0, mDisease);
		// } else {
		// int position = getIntent().getIntExtra("position", 0);
		// MainApplication.mList_disease.set(position, mDisease);
		// }

		if (mDssType != null) {
			mDssType.setParentId(mDisease.getId() + "");
			// DBHelperSingleton.getInstance().insertData(mDssType);
			mDisease.setDSS_TYPE(mDssType.getDSS_TYPE());
		}
		switch (mType) {
		case 0:
			// 日常巡查功能
			// 设施类型--0=沥青路面，1=水泥路面，2=路基，3=沿线设施，4=绿化
			if ((mDisease.getType() == 0 || mDisease.getType() == 1)
					&& (TextUtils.isEmpty(mDisease.getLaneLocation()))) {
				showToast("请输入车道");
				return;
			}
			switch (mSheshiType) {
			case 0:
				mDisease.setFacilityCat("LQLM");
				break;
			case 1:
				mDisease.setFacilityCat("LMSN");
				break;
			case 2:
				mDisease.setFacilityCat("LJ");
				break;
			case 3:
				mDisease.setFacilityCat("JA");
				break;
			case 4:
				mDisease.setFacilityCat("LH");
				break;
			default:
				break;
			}

			DmDinspRecordDao recordDao = new DmDinspRecordDao();
			recordDao.insertRiChang(mDisease, mDssType);
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			mDisease.setType(mType);
			DmFinspRecordDao frecordDao = new DmFinspRecordDao();
			frecordDao.insertFinspData(mDisease, mDssType);
			break;
		default:
			break;
		}
		DBHelperSingleton.getInstance().insertOrReplaceData(mDisease);
		Intent intent = new Intent();
		intent.setClass(mContext, DiseaseList.class);
		intent.putExtra("data", mDSS_main);
		intent.putExtra("type", mDisease.getType());
		intent.putExtra("showInputBtn", true);
		startActivity(intent);
		finish();
	}

	private class GvAdapter extends CommonAdapter<String> {
		public GvAdapter(Context context, List<String> mDatas, int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, String item, int position) {
			// TODO Auto-generated method stub
			if (!item.equals("tag")) {
				ImageView iv = helper.getView(R.id.iv_damageImg);
				MainApplication.setImgByPath(item, iv);
			}
		}
	}

	private class GvItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (position == 0) {
				if (mDialog == null) {
					initSeleteDLG();
				} else {
					mDialog.show();
				}
			}
		}

	}

	private class RGCheckedChangedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (group.getId()) {
			case R.id.rg_level:
				switch (checkedId) {
				case R.id.rb_light:
					mLevel = 0;
					break;
				case R.id.rb_middle:
					mLevel = 1;
					break;
				case R.id.rb_serious:
					mLevel = 2;
					break;
				default:
					break;
				}
				break;
			case R.id.rg_nature:
				switch (checkedId) {
				case R.id.rb_newDisease:
					mNature = 0;
					break;
				case R.id.rb_oldDisease:
					mNature = 1;
					break;
				case R.id.rb_againDisease:
					mNature = 2;
					break;

				default:
					break;
				}
				break;
			case R.id.rg_orientation:
				switch (checkedId) {
				case R.id.rb_up:
					mOrientation = 0;
					mLaneLocation = "";
					mTv_laneLocation.setText("");
					mTv_stake.setText(getString(R.string.stake_range,
							mRoute.getSTARTSTAKE(), mRoute.getENDSTAKE()));
					break;
				case R.id.rb_down:
					mOrientation = 1;
					mLaneLocation = "";
					mTv_laneLocation.setText("");
					mTv_stake.setText(getString(R.string.stake_range,
							mRoute.getDOWN_START_STAKE_NUM(),
							mRoute.getDOWN_END_STAKE_NUM()));
					break;
				default:
					break;
				}
				break;
			case R.id.rg_problemType:
				switch (checkedId) {
				case R.id.rb_disease:
					mProblemType = 0;
					mLayout_diseaseType.setVisibility(View.VISIBLE);
					mLayout_curingSuggest.setVisibility(View.VISIBLE);
					mLL_count.setVisibility(View.VISIBLE);
					mLL_goneWithClean.setVisibility(View.VISIBLE);
					break;
				case R.id.rb_cleaning:
					mProblemType = 1;
					mLayout_diseaseType.setVisibility(View.GONE);
					mLayout_curingSuggest.setVisibility(View.GONE);
					mLL_count.setVisibility(View.GONE);
					mLL_goneWithClean.setVisibility(View.GONE);
					break;
				default:
					break;
				}
				break;

			default:
				break;
			}
		}

	}

	@SuppressWarnings("deprecation")
	private void initPPW() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_ppwItem1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_ppwItem2 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_ppwItem1.setText("路面巡查");
		tv_ppwItem2.setText("结构物巡查");
		tv_ppwItem1.setOnClickListener(this);
		tv_ppwItem2.setOnClickListener(this);
		mPPW_menu = new PopupWindow(layout);
		mPPW_menu.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		// // 控制popupwindow点击屏幕其他地方消失
		mPPW_menu.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片，不能在布局中设置，要通过代码来设置
		mPPW_menu.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

		mPPW_menu.setWidth(LayoutParams.WRAP_CONTENT);
		mPPW_menu.setHeight(LayoutParams.WRAP_CONTENT);

	}

	private Dialog mDialog;

	/**
	 * 初始化选择照片弹框
	 */
	@SuppressWarnings("deprecation")
	private void initSeleteDLG() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dlg_select_img, null);
		TextView tv_item1 = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_item2 = (TextView) view.findViewById(R.id.tv_gallery);
		DialogItemClickListener listener = new DialogItemClickListener();
		tv_item1.setOnClickListener(listener);
		tv_item2.setOnClickListener(listener);
		mDialog = new Dialog(mContext);
		Window window = mDialog.getWindow();
		window.requestFeature(1);
		window.setGravity(Gravity.BOTTOM);
		mDialog.setContentView(view);
		mDialog.show();
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	private long time;

	private class DialogItemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
			Intent intent;
			switch (v.getId()) {
			case R.id.tv_camera:
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定拍照后保存图片的路径
				String path = FileUtils.getTempPath();
				time = System.currentTimeMillis();
				File file = new File(path, time + ".jpg");
				Uri mOutPutFileUri = Uri.fromFile(file);
				// 设置MediaStore.EXTRA_OUTPUT属性后相机不会在onActivityForResult返回图片，只能主动到自己设定到相机路径获取图片
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
				startActivityForResult(intent, Constant.SELECT_IMG_BY_CAMERA);
				break;

			case R.id.tv_gallery:
				Intent intent2 = new Intent(Intent.ACTION_PICK, null);
				intent2.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				intent2.putExtra("return-data", true);
				startActivityForResult(intent2, Constant.SELECT_IMG_BY_GALLERY);
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.SELECT_IMG_BY_CAMERA:
			String tempPath = FileUtils.getTempPath() + time + ".jpg";
			File tempFile = new File(tempPath);
			if (tempFile.exists()) {
				mList_photo.add(tempPath);
				mGvAdapter.notifyDataSetChanged();
			}
			break;
		case Constant.SELECT_IMG_BY_GALLERY:
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			if (uri.getScheme().toString().compareTo("content") == 0) {
				Cursor cursor = mContext.getContentResolver().query(uri, null,
						null, null, null);
				cursor.moveToFirst();// 很重要、不限把游标移动到第一位报错
				int pathIndex = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				String tempPath1 = cursor.getString(pathIndex);
				cursor.close();
				mList_photo.add(tempPath1);
			} else if (uri.getScheme().toString().compareTo("file") == 0) {
				String path = uri.toString().replace("file://", "");
				// if (!path.startsWith("/mnt")) {
				// path += "/mnt";
				// }
				mList_photo.add(path);
			}
			mGvAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}

	private class EditWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String w = mEt_width.getText().toString();
			String l = mEt_lenght.getText().toString();
			if (!TextUtils.isEmpty(l) && !TextUtils.isEmpty(w)) {
				try {
					float width = Float.valueOf(w);
					float lenght = Float.valueOf(l);
					float area = width * lenght;
					mEt_area.setText(area + "");
				} catch (Exception e) {
					// TODO: handle exception
					mEt_area.setText("");
				}

			} else {
				mEt_area.setText("");
			}
		}

	}

}
