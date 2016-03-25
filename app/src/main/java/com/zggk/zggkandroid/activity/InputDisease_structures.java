package com.zggk.zggkandroid.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.utils.FileUtils;

/**
 * 结构物病害录入界面-桥梁、涵洞、隧道、边坡
 * 
 * @author xsh
 * 
 */
public class InputDisease_structures extends BaseActivity implements
		OnClickListener {

	private Context mContext = InputDisease_structures.this;
	private ImageButton mBtn_back, mBtn_menu, mBtn_search;
	private Button mBtn_save, mBtn_add, mBtn_sub;
	private Mod_disease mDisease;
	private LinearLayout mLayout_conclusion, mLayout_diseaseInfo;
	private TextView mTv_route, mTv_date, mTv_structureName, mTv_user;
	private RelativeLayout mLayout_project, mLayout_diseaseType,
			mLayout_curingSuggest, mLayout_curingSuggest_conclusion,
			mLayout_component, mLayout_material, mLayout_component2,
			mLayout_parts, mLayout_scale;
	private RadioGroup mRg_orientation, mRg_nature;
	private RadioButton mRB_up, mRB_down, mRB_newDisease, mRB_oldDisease,
			mRB_againDisease;
	private TextView mTv_diseaseType, mTv_curingSuggest,
			mTv_curingSuggest_conclusion, mTv_component, mTv_material,
			mTv_component2, mTv_parts, mTv_scale, mTv_project;
	private EditText mEt_describe, mEt_conclusion, mEt_comment;
	private EditText mEt_landmark1, mEt_landmark2;
	private EditText mEt_long, mEt_width, mEt_deep, mEt_area, mEt_count,
			mEt_volum, mEt_percantage, mEt_angle;
	private EditText mEt_cause;
	private GridView mGv_damageImg;
	private GvAdapter mGvAdapter;
	private List<String> mList_data = new ArrayList<String>();

	private String mAction;
	private int mType;
	private boolean isDisease;

	private DssTypeEntity mDssType;
	private RouteEntity mRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_disease_struture);

		mAction = getIntent().getStringExtra("action");
		mType = getIntent().getIntExtra("type", 5);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mBtn_search = (ImageButton) findViewById(R.id.btn_search);
		mBtn_menu = (ImageButton) findViewById(R.id.btn_menu);
		mBtn_search.setVisibility(View.VISIBLE);
		mBtn_menu.setVisibility(View.VISIBLE);
		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_user = (TextView) findViewById(R.id.tv_user);
		mTv_structureName = (TextView) findViewById(R.id.tv_structureName);
		mLayout_project = (RelativeLayout) findViewById(R.id.layout_project);
		mLayout_curingSuggest = (RelativeLayout) findViewById(R.id.layout_curingSuggest);
		mLayout_curingSuggest_conclusion = (RelativeLayout) findViewById(R.id.layout_curingSuggest_conclusion);
		mEt_describe = (EditText) findViewById(R.id.et_describe);
		mEt_conclusion = (EditText) findViewById(R.id.et_conclusion);
		mEt_comment = (EditText) findViewById(R.id.et_comment);
		mRg_orientation = (RadioGroup) findViewById(R.id.rg_orientation);
		mRg_nature = (RadioGroup) findViewById(R.id.rg_nature);

		mEt_landmark1 = (EditText) findViewById(R.id.et_landmark1);
		mEt_landmark2 = (EditText) findViewById(R.id.et_landmark2);
		mEt_percantage = (EditText) findViewById(R.id.et_percentage);
		mEt_angle = (EditText) findViewById(R.id.et_angle);
		mEt_long = (EditText) findViewById(R.id.et_long);
		mEt_width = (EditText) findViewById(R.id.et_width);
		mEt_deep = (EditText) findViewById(R.id.et_deep);
		mEt_area = (EditText) findViewById(R.id.et_area);
		mEt_count = (EditText) findViewById(R.id.et_count);
		mEt_volum = (EditText) findViewById(R.id.et_volume);
		mEt_cause = (EditText) findViewById(R.id.et_cause);

		RGCheckedChangedListener RGL = new RGCheckedChangedListener();
		mRg_orientation = (RadioGroup) findViewById(R.id.rg_orientation);
		mRg_nature = (RadioGroup) findViewById(R.id.rg_nature);
		mRg_nature.setOnCheckedChangeListener(RGL);
		mRg_orientation.setOnCheckedChangeListener(RGL);

		mLayout_conclusion = (LinearLayout) findViewById(R.id.layout_conclusion);
		mLayout_diseaseInfo = (LinearLayout) findViewById(R.id.layout_diseaseInfo);
		mLayout_component = (RelativeLayout) findViewById(R.id.layout_component);
		mLayout_component2 = (RelativeLayout) findViewById(R.id.layout_component2);
		mLayout_material = (RelativeLayout) findViewById(R.id.layout_material);
		mLayout_diseaseType = (RelativeLayout) findViewById(R.id.layout_diseaseType);
		mLayout_parts = (RelativeLayout) findViewById(R.id.layout_parts);
		mLayout_scale = (RelativeLayout) findViewById(R.id.layout_scale);

		mTv_component = (TextView) findViewById(R.id.tv_component);
		mTv_component2 = (TextView) findViewById(R.id.tv_component2);
		mTv_material = (TextView) findViewById(R.id.tv_material);
		mTv_diseaseType = (TextView) findViewById(R.id.tv_diseaseType);
		mTv_parts = (TextView) findViewById(R.id.tv_parts);
		mTv_scale = (TextView) findViewById(R.id.tv_scale);
		mTv_curingSuggest = (TextView) findViewById(R.id.tv_curingSuggest);
		mTv_curingSuggest_conclusion = (TextView) findViewById(R.id.tv_curingSuggest_conclusion);
		mTv_project = (TextView) findViewById(R.id.tv_project);

		mLayout_component.setOnClickListener(this);
		mLayout_component2.setOnClickListener(this);
		mLayout_curingSuggest.setOnClickListener(this);
		mLayout_curingSuggest_conclusion.setOnClickListener(this);
		mLayout_diseaseType.setOnClickListener(this);
		mLayout_material.setOnClickListener(this);
		mLayout_parts.setOnClickListener(this);
		mLayout_project.setOnClickListener(this);
		mLayout_scale.setOnClickListener(this);

		mBtn_back.setOnClickListener(this);
		mBtn_menu.setOnClickListener(this);
		mBtn_search.setOnClickListener(this);
		mBtn_save = (Button) findViewById(R.id.btn_save);
		mBtn_save.setOnClickListener(this);
		mBtn_add = (Button) findViewById(R.id.btn_add);
		mBtn_sub = (Button) findViewById(R.id.btn_sub);
		mBtn_add.setOnClickListener(this);
		mBtn_sub.setOnClickListener(this);

		mList_data.add("tag");
		mGv_damageImg = (GridView) findViewById(R.id.gv_damageImg);
		mGvAdapter = new GvAdapter(mContext, mList_data,
				R.layout.gv_item_damageimg);
		mGv_damageImg.setAdapter(mGvAdapter);
		mGv_damageImg.setOnItemClickListener(new GvItemClickListener());

		if (mAction.equals("edit")) {
			long id = getIntent().getLongExtra("id", 0);
			mDisease = (Mod_disease) DBHelperSingleton.getInstance().getObject(
					Mod_disease.class, "id='" + id + "'");
			initEdit();
		} else {
			Bundle bundle = getIntent().getBundleExtra("data");
			mDisease = (Mod_disease) bundle.getSerializable("data");
			mRoute = mDisease.getRoute();
			mTv_route.setText("检查路线:" + mRoute.getLINE_ALLNAME());
			mTv_date.setText("检查日期:" + mDisease.getDate());
			mTv_user.setText("检查人:" + mDisease.getUser());

			switch (mType) {
			case 5:
				tv_title.setText("桥梁检查");
				mTv_structureName
						.setText("检查桥梁:" + mDisease.getStructureName());
				break;
			case 6:
				tv_title.setText("涵洞检查");
				mTv_structureName
						.setText("检查涵洞:" + mDisease.getStructureName());
				break;
			case 7:
				tv_title.setText("隧道检查");
				mTv_structureName
						.setText("检查隧道:" + mDisease.getStructureName());
				break;
			case 8:
				tv_title.setText("边坡检查");
				mTv_structureName
						.setText("检查边坡:" + mDisease.getStructureName());
				break;

			default:
				break;
			}
		}

	}

	private void initEdit() {
		mRB_up = (RadioButton) findViewById(R.id.rb_up);
		mRB_down = (RadioButton) findViewById(R.id.rb_down);
		mRB_oldDisease = (RadioButton) findViewById(R.id.rb_oldDisease);
		mRB_newDisease = (RadioButton) findViewById(R.id.rb_newDisease);
		mRB_againDisease = (RadioButton) findViewById(R.id.rb_againDisease);

		if (mDisease.getOrientation() == 1) {
			mRB_down.setChecked(true);
		} else {
			mRB_up.setChecked(true);
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

		int type = mDisease.getType();
		switch (type) {
		case 5:
			mTv_structureName.setText("检查桥梁:" + mDisease.getStructureName());
			break;
		case 6:
			mTv_structureName.setText("检查涵洞:" + mDisease.getStructureName());
			break;
		case 7:
			mTv_structureName.setText("检查隧道:" + mDisease.getStructureName());
			break;
		case 8:
			mTv_structureName.setText("检查边坡:" + mDisease.getStructureName());
			break;

		default:
			break;
		}
		mTv_route.setText("检查路线:" + mDisease.getRoute());
		mTv_date.setText("检查日期:" + mDisease.getDate());
		mTv_user.setText("检查人:" + mDisease.getUser());
		mTv_diseaseType.setText(mDisease.getDiseaseType());
		mTv_curingSuggest.setText(mDisease.getAdvice());
		mTv_component.setText(mDisease.getComponent());
		mTv_component2.setText(mDisease.getComponent2());
		mTv_curingSuggest_conclusion.setText(mDisease.getAdvice());
		mTv_material.setText(mDisease.getMaterial());
		mTv_parts.setText(mDisease.getParts());
		mTv_scale.setText(mDisease.getScale());
		mEt_landmark1.setText(mDisease.getLandmarkStart());
		mEt_landmark2.setText(mDisease.getLandmarkEnd());
		mEt_long.setText(mDisease.getLength());
		mEt_width.setText(mDisease.getWidth());
		mEt_deep.setText(mDisease.getDeep());
		mEt_area.setText(mDisease.getArea());
		mEt_count.setText(mDisease.getCount());
		mEt_volum.setText(mDisease.getVolume());
		mEt_angle.setTag(mDisease.getAngle());
		mEt_percantage.setText(mDisease.getPercantage());
		mEt_cause.setText(mDisease.getCause());
		mEt_describe.setText(mDisease.getDescribe());
		mEt_conclusion.setText(mDisease.getConclusion());
		mEt_comment.setText(mDisease.getComment());
		mTv_project.setText(mDisease.getProject());

		String paths = mDisease.getPath();
		if (!TextUtils.isEmpty(paths)) {
			String[] pathArray = paths.split(",");
			for (String str : pathArray) {
				mList_data.add(str);
			}
			mGvAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		List<String> list_data = null;
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_save:
			save();
			break;
		case R.id.btn_menu:
			if (mPPW_menu == null) {
				initPpw();
			}
			mPPW_menu.showAsDropDown(v);
			break;
		case R.id.btn_search:
			Intent intent = new Intent();
			intent.setClass(mContext, ConclusionList.class);
			startActivity(intent);
			break;
		case R.id.layout_curingSuggest_conclusion:
			Constant.getInstance().showSelectDialog(mContext,
					Data.curingSuggest(), mTv_curingSuggest_conclusion);
			break;
		case R.id.layout_curingSuggest:
			Constant.getInstance().showSelectDialog(mContext,
					Data.curingSuggest(), mTv_curingSuggest);
			break;
		case R.id.layout_component:
			switch (mType) {
			case 5:
				list_data = Data.component_brige();
				break;
			case 6:
				list_data = Data.component_culvert();
				break;
			case 7:
				list_data = Data.component_tunnel();
				break;
			case 8:
				list_data = Data.component_side();
				break;

			default:
				break;
			}
			Constant.getInstance().showSelectDialog(mContext, list_data,
					mTv_component);
			break;

		case R.id.layout_component2:
			switch (mType) {
			case 5:
				list_data = Data.component2_brige();
				break;
			case 6:
				list_data = Data.component2_culvert();
				break;
			case 7:
				list_data = Data.component2_tunnel();
				break;
			case 8:
				list_data = Data.component2_side();
				break;

			default:
				break;
			}
			Constant.getInstance().showSelectDialog(mContext, list_data,
					mTv_component2);
			break;
		case R.id.layout_material:
			switch (mType) {
			case 5:
				list_data = Data.material_bridge();
				break;
			case 6:
				list_data = Data.material_culvert();
				break;
			case 7:
				list_data = Data.material_tunnel();
				break;
			case 8:
				list_data = Data.material_side();
				break;

			default:
				break;
			}
			Constant.getInstance().showSelectDialog(mContext, list_data,
					mTv_material);
			break;
		case R.id.layout_parts:
			switch (mType) {
			case 5:
				list_data = Data.parts_bridge();
				break;
			case 6:
				list_data = Data.parts_culvert();
				break;
			case 7:
				list_data = Data.parts_tunnel();
				break;
			case 8:
				list_data = Data.parts_side();
				break;

			default:
				break;
			}
			Constant.getInstance().showSelectDialog(mContext, list_data,
					mTv_parts);
			break;
		case R.id.layout_scale:
			Constant.getInstance().showSelectDialog(mContext, Data.scale(),
					mTv_scale);
			break;

		case R.id.layout_diseaseType:
			switch (mType) {
			case 5:
				list_data = Data.bridgeDisease();
				break;
			case 6:
				list_data = Data.culvertDisease();
				break;
			case 7:
				list_data = Data.tunnelDisease();
				break;
			case 8:
				list_data = Data.sideDisease();
				break;

			default:
				break;
			}
			Constant.getInstance().showSelectDialog(mContext, list_data,
					mTv_diseaseType);
			break;
		case R.id.layout_project:
			Constant.getInstance().showSelectDialog(mContext, Data.project(),
					mTv_project);
			break;

		default:
			break;
		}
	}

	private void save() {
		if (mAction.equals("input")) {
			mDisease.setId(MainApplication.getUUID());
		}
		mDisease.setDisease(isDisease);
		mDisease.setProject(mTv_project.getText().toString());
		mDisease.setDescribe(mEt_describe.getText().toString().trim());
		mDisease.setConclusion(mEt_conclusion.getText().toString().trim());
		mDisease.setComment(mEt_comment.getText().toString().trim());
		mDisease.setComponent(mTv_component.getText().toString());
		mDisease.setComponent2(mTv_component2.getText().toString());
		mDisease.setMaterial(mTv_material.getText().toString());
		mDisease.setParts(mTv_parts.getText().toString());
		mDisease.setOrientation(mOrientation);
		mDisease.setLandmarkStart(mEt_landmark1.getText().toString());
		mDisease.setLandmarkEnd(mEt_landmark2.getText().toString());
		mDisease.setDiseaseType(mTv_diseaseType.getText().toString());
		mDisease.setLength(mEt_long.getText().toString());
		mDisease.setWidth(mEt_width.getText().toString());
		mDisease.setDeep(mEt_deep.getText().toString());
		mDisease.setArea(mEt_area.getText().toString());
		mDisease.setCount(mEt_count.getText().toString());
		mDisease.setVolume(mEt_volum.getText().toString());
		mDisease.setPercantage(mEt_percantage.getText().toString().trim());
		mDisease.setAngle(mEt_angle.getText().toString().trim());
		mDisease.setNature(mNature);
		mDisease.setAdvice(mTv_curingSuggest.getText().toString());
		mDisease.setCause(mEt_cause.getText().toString());

		mList_data.remove("tag");
		StringBuffer sb = new StringBuffer();
		for (String str : mList_data) {
			sb.append(str).append(",");
		}
		if (!TextUtils.isEmpty(sb)) {
			sb = sb.deleteCharAt(sb.length() - 1);
			mDisease.setPath(sb.toString());
		}

		DBHelperSingleton.getInstance().insertOrReplaceData(mDisease);

		Intent intent = new Intent();
		if (isDisease) {
			intent.setClass(mContext, DiseaseList.class);
		} else {
			intent.setClass(mContext, ConclusionList.class);
		}
		startActivity(intent);
	}

	private PopupWindow mPPW_menu;

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void initPpw() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_item1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_item2 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_item1.setText("检查结论");
		tv_item2.setText("病害信息");
		tv_item1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isDisease = false;
				mLayout_diseaseInfo.setVisibility(View.GONE);
				mLayout_conclusion.setVisibility(View.VISIBLE);
				mPPW_menu.dismiss();
			}
		});
		tv_item2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isDisease = true;
				mLayout_conclusion.setVisibility(View.GONE);
				mLayout_diseaseInfo.setVisibility(View.VISIBLE);
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

	private int mNature, mOrientation;

	private class RGCheckedChangedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (group.getId()) {
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
					break;
				case R.id.rb_down:
					mOrientation = 1;
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

	private long time;

	private class DialogItemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
			switch (v.getId()) {
			case R.id.tv_camera:
				Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定拍照后保存图片的路径
				String path = FileUtils.getTempPath();
				time = System.nanoTime();
				File file = new File(path, time + ".jpg");
				Uri mOutPutFileUri = Uri.fromFile(file);
				// 设置MediaStore.EXTRA_OUTPUT属性后相机不会在onActivityForResult返回图片，只能主动到自己设定到相机路径获取图片
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
				startActivityForResult(intent1, Constant.SELECT_IMG_BY_CAMERA);
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
				mList_data.add(tempPath);
				mGvAdapter.notifyDataSetChanged();
			}
			break;
		case Constant.SELECT_IMG_BY_GALLERY:
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			Cursor cursor = mContext.getContentResolver().query(uri, null,
					null, null, null);
			cursor.moveToFirst();// 很重要、不限把游标移动到第一位报错
			int pathIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			String tempPath1 = cursor.getString(pathIndex);
			cursor.close();
			mList_data.add(tempPath1);
			mGvAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}

}
