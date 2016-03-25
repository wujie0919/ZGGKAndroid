package com.zggk.zggkandroid.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.zggk.zggkandroid.dao.DmFinspRecordDao;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.utils.FileUtils;

/**
 * 路面病害录入界面-路面、路基、设施、绿化
 * 
 * @author xsh
 * 
 */
public class InputDisease_pavement extends BaseActivity implements
		OnClickListener {

	private Context mContext = InputDisease_pavement.this;

	private Button mBtn_save, mBtn_add, mBtn_sub;
	private ImageButton mBtn_back, mBtn_search;
	private TextView mTv_route, mTv_date, mTv_user, mTv_direction, mTv_section;
	private TextView mTv_laneLocation, mTv_diseaseType, mTv_curingSuggest;
	private EditText mEt_landmark1, mEt_landmark2;
	private EditText mEt_long, mEt_width, mEt_deep, mEt_area, mEt_count,
			mEt_volum, mEt_cause;
	private RadioGroup mRg_orientation, mRg_level, mRg_nature;
	private RadioButton mRB_up, mRB_down, mRB_light, mRB_middle, mRB_serious,
			mRB_newDisease, mRB_oldDisease, mRB_againDisease;
	private RelativeLayout mLayout_diseaseType, mLayout_laneLocation,
			mLayout_curingSuggest;

	private GridView mGv_damageImg;
	private GvAdapter mGvAdapter;
	private List<String> mList_data = new ArrayList<String>();

	private String mAction;
	private int mType;
	private Mod_disease mDisease;
	private RouteEntity mRoute;
	private DssTypeEntity mDssType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_disease_pavement);
		mAction = getIntent().getStringExtra("action");
		mType = getIntent().getIntExtra("type", 0);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		switch (mType) {
		case 1:
			tv_title.setText("路面病害");
			break;
		case 2:
			tv_title.setText("路基病害");
			break;
		case 3:
			tv_title.setText("设施病害");
			break;
		case 4:
			tv_title.setText("绿化病害");
			break;

		default:
			break;
		}

		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mBtn_search = (ImageButton) findViewById(R.id.btn_search);
		mBtn_search.setVisibility(View.VISIBLE);
		mBtn_save = (Button) findViewById(R.id.btn_save);
		mBtn_add = (Button) findViewById(R.id.btn_add);
		mBtn_sub = (Button) findViewById(R.id.btn_sub);
		mEt_area = (EditText) findViewById(R.id.et_area);
		mEt_cause = (EditText) findViewById(R.id.et_cause);
		mEt_count = (EditText) findViewById(R.id.et_count);
		mEt_deep = (EditText) findViewById(R.id.et_deep);
		mEt_landmark1 = (EditText) findViewById(R.id.et_landmark1);
		mEt_landmark2 = (EditText) findViewById(R.id.et_landmark2);
		mEt_long = (EditText) findViewById(R.id.et_long);
		mEt_volum = (EditText) findViewById(R.id.et_volume);
		mEt_width = (EditText) findViewById(R.id.et_width);
		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_user = (TextView) findViewById(R.id.tv_user);
		mTv_direction = (TextView) findViewById(R.id.tv_direction);
		mTv_section = (TextView) findViewById(R.id.tv_section);
		mTv_laneLocation = (TextView) findViewById(R.id.tv_laneLocation);
		mTv_diseaseType = (TextView) findViewById(R.id.tv_diseaseType);
		mTv_curingSuggest = (TextView) findViewById(R.id.tv_curingSuggest);
		mLayout_laneLocation = (RelativeLayout) findViewById(R.id.layout_laneLocation);
		mLayout_diseaseType = (RelativeLayout) findViewById(R.id.layout_diseaseType);
		mLayout_curingSuggest = (RelativeLayout) findViewById(R.id.layout_curingSuggest);

		mRg_orientation = (RadioGroup) findViewById(R.id.rg_orientation);
		mRg_level = (RadioGroup) findViewById(R.id.rg_level);
		mRg_nature = (RadioGroup) findViewById(R.id.rg_nature);
		RGCheckedChangedListener RGL = new RGCheckedChangedListener();
		mRg_level.setOnCheckedChangeListener(RGL);
		mRg_nature.setOnCheckedChangeListener(RGL);
		mRg_orientation.setOnCheckedChangeListener(RGL);

		mBtn_back.setOnClickListener(this);
		mBtn_search.setOnClickListener(this);
		mBtn_save.setOnClickListener(this);
		mBtn_add.setOnClickListener(this);
		mBtn_sub.setOnClickListener(this);
		mLayout_curingSuggest.setOnClickListener(this);
		mLayout_diseaseType.setOnClickListener(this);
		mLayout_laneLocation.setOnClickListener(this);

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
			mRoute = (RouteEntity) DBHelperSingleton.getInstance().getObject(
					RouteEntity.class, "LINE_ID='" + id + "'");
			mDssType = (DssTypeEntity) DBHelperSingleton.getInstance()
					.getObject(DssTypeEntity.class, "parentId='" + id + "'");

			initEdit();
		} else {
			Bundle bundle = getIntent().getBundleExtra("data");
			mDisease = (Mod_disease) bundle.getSerializable("data");
			mRoute = mDisease.getRoute();
			mTv_route.setText("路线:" + mRoute.getLINE_ALLNAME());
			mTv_date.setText("日期:" + mDisease.getDate());
			mTv_user.setText("检查人:" + mDisease.getUser());
			mTv_direction.setText("方向:" + mDisease.getDirection());
			mTv_section.setText("路段:" + mDisease.getSection());
		}
	}

	/**
	 * 如果是编辑则需初始化的数据
	 */
	private void initEdit() {
		mRB_up = (RadioButton) findViewById(R.id.rb_up);
		mRB_down = (RadioButton) findViewById(R.id.rb_down);
		mRB_light = (RadioButton) findViewById(R.id.rb_light);
		mRB_middle = (RadioButton) findViewById(R.id.rb_middle);
		mRB_serious = (RadioButton) findViewById(R.id.rb_serious);

		if (mDisease.getOrientation() == 1) {
			mRB_down.setChecked(true);
		} else {
			mRB_up.setChecked(true);
		}

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

		String paths = mDisease.getPath();
		if (!TextUtils.isEmpty(paths)) {
			String[] pathArray = paths.split(",");
			for (String str : pathArray) {
				mList_data.add(str);
			}
			mGvAdapter.notifyDataSetChanged();
		}

		mTv_route.setText("路线:" + mDisease.getRoute());
		mTv_date.setText("日期:" + mDisease.getDate());
		mTv_direction.setText("方向:" + mDisease.getDirection());
		mTv_user.setText("检查人:" + mDisease.getUser());
		mTv_section.setText("路段:" + mDisease.getSection());
		mTv_laneLocation.setText(mDisease.getLaneLocation());
		mTv_diseaseType.setText(mDisease.getDiseaseType());
		mTv_curingSuggest.setText(mDisease.getAdvice());
		mEt_landmark1.setText(mDisease.getLandmarkStart());
		mEt_landmark2.setText(mDisease.getLandmarkEnd());
		mEt_long.setText(mDisease.getLength());
		mEt_width.setText(mDisease.getWidth());
		mEt_deep.setText(mDisease.getDeep());
		mEt_area.setText(mDisease.getArea());
		mEt_count.setText(mDisease.getCount());
		mEt_volum.setText(mDisease.getVolume());
		mEt_cause.setText(mDisease.getCause());
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
			startActivity(intent);
			break;
		case R.id.btn_save:
			save();
			break;
		case R.id.layout_laneLocation:
			Constant.getInstance().showSelectDialog(mContext,
					Data.laneLocation(), mTv_laneLocation);
			break;
		case R.id.layout_curingSuggest:
			Constant.getInstance().showSelectDialog(mContext,
					Data.curingSuggest(), mTv_curingSuggest);
			break;
		case R.id.layout_diseaseType:
			switch (mType) {
			case 1:
				Constant.getInstance().showSelectDialog(mContext,
						Data.pavementDisease(), mTv_diseaseType);
				break;
			case 2:
				Constant.getInstance().showSelectDialog(mContext,
						Data.subgradeDisease(), mTv_diseaseType);
				break;
			case 3:
				Constant.getInstance().showSelectDialog(mContext,
						Data.facilityDisease(), mTv_diseaseType);
				break;
			case 4:
				Constant.getInstance().showSelectDialog(mContext,
						Data.greenDisease(), mTv_diseaseType);
				break;

			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void save() {
		if (mAction.equals("input")) {
//			mDisease.setId(MainApplication.getUUID());
		}
		mDisease.setType(mType);
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
		mDisease.setNature(mNature);
		mDisease.setAdvice(mTv_curingSuggest.getText().toString());
		mDisease.setCause(mEt_cause.getText().toString());
		mDisease.setLaneLocation(mTv_laneLocation.getText().toString());
		mDisease.setLevel(mLevel);

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
		DmFinspRecordDao recordDao=new DmFinspRecordDao();
		recordDao.insertFinspData(mDisease, mDssType);
		Intent intent = new Intent();
		intent.setClass(mContext, DiseaseList.class);
		startActivity(intent);

	}

	private int mOrientation, mNature, mLevel;

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

	private PopupWindow mPPW_menu;

	@SuppressWarnings("deprecation")
	private void initPPW() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_ppwItem1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_ppwItem2 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_ppwItem1.setText("录入路面信息");
		tv_ppwItem2.setText("录入建筑物信息");
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
}
