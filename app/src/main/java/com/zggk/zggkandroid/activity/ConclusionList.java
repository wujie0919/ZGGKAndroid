package com.zggk.zggkandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.Mod_disease;

/**
 * 检查结论列表界面
 * 
 * @author xsh
 * 
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class ConclusionList extends BaseActivity implements OnClickListener {

	private Context mContext = ConclusionList.this;
	private ImageButton mBtn_back, mBtn_menu;
	private TextView mTv_title;
	private ListView mLv_conclusion;
	private EditText mEt_search;
	private Button mBtn_delete, mBtn_upload;
	private LvAdapter mLvAdapter;
	private List<Mod_disease> mList_data;

	private PopupWindow mPPW_menu;
	private boolean checkedAll = false;// 全选
	private List<Integer> mList_checked = new ArrayList<Integer>();// 选择项
	private List<Integer> mList_uploaded = new ArrayList<Integer>();// 已上传项
	private boolean isUpload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conclusionlist);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mTv_title.setText("结论列表");
		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mBtn_menu = (ImageButton) findViewById(R.id.btn_menu);
		mLv_conclusion = (ListView) findViewById(R.id.lv_conclusion);
		mEt_search = (EditText) findViewById(R.id.et_search);
		mBtn_delete = (Button) findViewById(R.id.btn_delete);
		mBtn_upload = (Button) findViewById(R.id.btn_upload);

		mTv_title.setOnClickListener(this);
		mBtn_back.setOnClickListener(this);
		mBtn_menu.setOnClickListener(this);
		mBtn_delete.setOnClickListener(this);
		mBtn_upload.setOnClickListener(this);

		mEt_search.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String str = mEt_search.getText().toString().trim();
				if (!hasFocus && TextUtils.isEmpty(str)) {
					mEt_search.setGravity(Gravity.CENTER);
				} else {
					mEt_search.setGravity(Gravity.LEFT
							| Gravity.CENTER_VERTICAL);
				}
			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mList_data = DBHelperSingleton.getInstance()
				.getDataWithConditions(Mod_disease.class, null,
						"isDisease = 'false'", null, null, null, "id desc ",
						null);
		if (mList_data == null) {
			mList_data = new ArrayList<Mod_disease>();
		}
		mLv_conclusion.setOnItemClickListener(new LvItemClickListener());
		mLvAdapter = new LvAdapter(mContext, mList_data,
				R.layout.lv_item_conclusion);
		mLv_conclusion.setAdapter(mLvAdapter);

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
		case R.id.btn_delete:
			if (mList_checked.size() == 0) {
				super.showToast("请选中需要删除的项目后再删除");
				return;
			}
			isUpload = false;
			if (mDialog == null) {
				initDialog();
			} else {
				mDialog.show();
			}
			mTv_dlgContent.setText("确定要删除选中项吗？");
			break;
		case R.id.btn_upload:
			if (mList_checked.size() == 0) {
				super.showToast("请选中需要上传的项目后再上传");
				return;
			}
			isUpload = true;
			if (mDialog == null) {
				initDialog();
			} else {
				mDialog.show();
			}
			mTv_dlgContent.setText("确定要上传选中项吗？");
			break;
		case R.id.tv_ppwItem0:
			for (int i = 0; i < mList_data.size(); i++) {
				mList_checked.add(i);
			}
			mList_checked.removeAll(mList_uploaded);
			mLvAdapter.notifyDataSetChanged();
			mPPW_menu.dismiss();

			checkedAll = !checkedAll;
			if (checkedAll) {
				mTv_ppwItem0.setText("取消全选");
			} else {
				mTv_ppwItem0.setText("全选");
			}

			break;
		case R.id.tv_ppwItem1:
			finish();
			break;
		case R.id.btn_negative:
			mDialog.dismiss();
			break;
		case R.id.btn_positive:
			mDialog.dismiss();

			if (isUpload) {
				upload();
			} else {
				delete();
			}
			break;
		default:
			break;
		}
	}

	private class LvAdapter extends CommonAdapter<Mod_disease> {
		public LvAdapter(Context context, List<Mod_disease> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, Mod_disease item,
				final int position) {
			// TODO Auto-generated method stub
			CheckBox ckb_choose = helper.getView(R.id.ckb_chose);
			ImageView iv_img = helper.getView(R.id.iv_img);
			TextView tv_uploadStatus = helper.getView(R.id.tv_uploadStatus);
			TextView tv_item1 = helper.getView(R.id.tv_item1);
			TextView tv_item2 = helper.getView(R.id.tv_item2);

			tv_item2.setText(item.getDate());
			int level = item.getLevel();
			String str_level = null;
			switch (level) {
			case 0:
				str_level = "良好";
				break;
			case 1:
				str_level = "损坏";
				break;
			case 2:
				str_level = "严重损坏";
				break;

			default:
				break;
			}
			tv_item1.setText(item.getStructureName() + "  " + item.getParts()
					+ "  " + str_level);

			if (item.isUploaded()) {
				if (!mList_uploaded.contains(position)) {
					mList_uploaded.add(position);
				}
				ckb_choose.setEnabled(false);
				tv_uploadStatus.setText("已上传");
			} else {
				ckb_choose.setEnabled(true);
				tv_uploadStatus.setText("未上传");
				if (mList_checked.contains(position)) {
					ckb_choose.setChecked(true);
				} else {
					ckb_choose.setChecked(false);
				}
			}

			String paths = item.getPath();
			if (!TextUtils.isEmpty(paths)) {
				String path = paths.split(",")[0];
				MainApplication.setImgByPath(path, iv_img);
			} else {
				iv_img.setImageResource(R.drawable.ic_default);
			}

			ckb_choose
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								if (!mList_checked.contains(position)) {
									mList_checked.add(position);
								}
							} else {
								mList_checked.remove((Object) position);
							}
						}
					});
		}
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

	private TextView mTv_ppwItem0;

	@SuppressWarnings("deprecation")
	private void initPPW() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_threebtn, null);
		mTv_ppwItem0 = (TextView) layout.findViewById(R.id.tv_ppwItem0);
		TextView tv_ppwItem1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_ppwItem2 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_ppwItem2.setVisibility(View.GONE);
		tv_ppwItem1.setText("录入");
		mTv_ppwItem0.setOnClickListener(this);
		tv_ppwItem1.setOnClickListener(this);
		mPPW_menu = new PopupWindow(layout);
		mPPW_menu.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		// // 控制popupwindow点击屏幕其他地方消失
		mPPW_menu.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片，不能在布局中设置，要通过代码来设置
		mPPW_menu.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

		mPPW_menu.setWidth(LayoutParams.WRAP_CONTENT);
		mPPW_menu.setHeight(LayoutParams.WRAP_CONTENT);

	}

	private Dialog mDialog;
	private TextView mTv_dlgContent;

	private void initDialog() {
		mDialog = new Dialog(mContext);
		Window window = mDialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_common,
				null);
		Button btn_positive = (Button) view.findViewById(R.id.btn_positive);
		Button btn_negative = (Button) view.findViewById(R.id.btn_negative);
		mTv_dlgContent = (TextView) view.findViewById(R.id.tv_content);

		btn_negative.setOnClickListener(this);
		btn_positive.setOnClickListener(this);

		mDialog.setContentView(view);
		mDialog.show();
		window.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
	}

	private ProgressDialog mProDialog;

	private void delete() {
		List<Mod_disease> list_d = new ArrayList<Mod_disease>();
		for (int pos : mList_checked) {
			Mod_disease d = mList_data.get(pos);
			DBHelperSingleton.getInstance().deleteData(
					Mod_disease.class, "id='" + d.getId() + "'");
			list_d.add(d);
		}

		mList_data.removeAll(list_d);

		mList_checked.clear();
		mLvAdapter.notifyDataSetChanged();

	}

	private void upload() {
		if (mProDialog == null) {
			mProDialog = new ProgressDialog(mContext);
			mProDialog.setTitle("上传中...");
			mProDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProDialog.setIndeterminate(false);
			mProDialog.setCanceledOnTouchOutside(false);
			mProDialog.setMax(100);
		}
		mProDialog.show();

		mHandler.post(run);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int progress = msg.arg1;
			mProDialog.setProgress(progress);
			if (progress == 100) {
				mProDialog.dismiss();
				Constant.showToast(mContext, "上传完毕");
				for (int pos : mList_checked) {
					Mod_disease d = mList_data.get(pos);
					d.setUploaded(true);
					mList_uploaded.add(pos);

					DBHelperSingleton.getInstance()
							.insertOrReplaceData(d);
				}
				mList_checked.clear();
				mLvAdapter.notifyDataSetChanged();
			}
			mHandler.post(run);
		}
	};

	private Runnable run = new Runnable() {
		int i = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			i += 10;
			// 得到消息对象
			Message msg = mHandler.obtainMessage();
			msg.arg1 = i;
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (i < 101) {
				mHandler.sendMessage(msg);
			}

			if (i == 100) {
				mHandler.removeCallbacks(run);
			}
		}
	};

}
