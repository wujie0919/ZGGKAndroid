package com.zggk.zggkandroid.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.Toast;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.DmDinsp;
import com.zggk.zggkandroid.entity.DmDinspRecord;
import com.zggk.zggkandroid.entity.DmFinsp;
import com.zggk.zggkandroid.entity.DmFinspRecord;
import com.zggk.zggkandroid.entity.DssImage;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;

import org.ksoap2.serialization.SoapObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 病害列表界面
 * 
 * @author xsh
 * 
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class DiseaseList extends BaseActivity implements OnClickListener {

	private Context mContext = DiseaseList.this;
	private ImageButton mBtn_back, mBtn_menu;
	private TextView mTv_title;
	private ListView mLv_disease;
	private EditText mEt_search;
	private Button mBtn_delete, mBtn_upload;
	private LvAdapter mLvAdapter;
	private List<Mod_disease> mList_data;

	private PopupWindow mPPW_menu;
	private boolean checkedAll = false;// 全选
	private List<Integer> mList_checked = new ArrayList<Integer>();// 选择项
	// private List<Integer> mList_uploaded = new ArrayList<Integer>();// 已上传项
	private boolean isUpload;

	private boolean isSearch;

	private Mod_disease mDisease;// 主单信息、点击录入用回到录入页面时回传用

	private int mType;

	private Boolean deleteTag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);

		isSearch = getIntent().getBooleanExtra("search", false);
		mType = getIntent().getIntExtra("type", 0);
		initView();
		mDisease = (Mod_disease) getIntent().getSerializableExtra("data");
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mTv_title.setText("病害列表");
		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mBtn_menu = (ImageButton) findViewById(R.id.btn_menu);
		mBtn_menu.setVisibility(View.VISIBLE);
		mLv_disease = (ListView) findViewById(R.id.lv_disease);
		mEt_search = (EditText) findViewById(R.id.et_search);
		mBtn_delete = (Button) findViewById(R.id.btn_delete);
		mBtn_upload = (Button) findViewById(R.id.btn_upload);

		mBtn_back.setOnClickListener(this);
		mBtn_menu.setOnClickListener(this);
		mBtn_delete.setOnClickListener(this);
		mBtn_upload.setOnClickListener(this);
		findViewById(R.id.iv_search).setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mList_data = DBHelperSingleton.getInstance().getDatas(
				Mod_disease.class, "type=" + mType + " ORDER BY id DESC");
		if (mList_data == null) {
			mList_data = new ArrayList<Mod_disease>();
		}
		mLv_disease.setOnItemClickListener(new LvItemClickListener());
		mLvAdapter = new LvAdapter(mContext, mList_data,
				R.layout.lv_item_diseaselist);
		mLv_disease.setAdapter(mLvAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			back();
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
				initAlertDialog();
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
				initAlertDialog();
			} else {
				mDialog.show();
			}

			mTv_dlgContent.setText("您是否确认要上传数据");
			break;
		case R.id.tv_ppwItem0:
			if (checkedAll) {
				mList_checked.clear();
			} else {
				for (int i = 0; i < mList_data.size(); i++) {
					mList_checked.add(i);
				}
			}
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
			Intent intent = new Intent();
			intent.setClass(mContext, InputRoutineInspection.class);
			intent.putExtra("action", "input");
			intent.putExtra("fromList", "fromList");
			Bundle b = new Bundle();
			b.putSerializable("data", mDisease);
			intent.putExtra("data", b);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_negative:
			mDialog.dismiss();
			if (!isUpload) {
				deleteTag = false;
				
				switch (mType) {
				case 0:
					deleteRichang();
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					deleteJingchang();
					break;
				default:
					break;
				}
			}
			break;
		case R.id.btn_positive:
			mDialog.dismiss();
			if (isUpload) {
				upload();
			} else {
				deleteTag = true;
				switch (mType) {
				case 0:
					deleteRichang();
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					deleteJingchang();
					break;
				default:
					break;
				}
			}
			break;
		case R.id.iv_search:
			String keyword = mEt_search.getText().toString().trim();
			mList_data = getDataWithKeyWord(keyword);
			mLvAdapter = new LvAdapter(mContext, mList_data,
					R.layout.lv_item_diseaselist);
			mLv_disease.setAdapter(mLvAdapter);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void back() {
		if (!isSearch) {
			Intent intent = new Intent();
			intent.setClass(mContext, MainActivity.class);
			startActivity(intent);
		}
		finish();
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
			TextView tv_routeName = helper.getView(R.id.tv_routeName);
			TextView tv_item1 = helper.getView(R.id.tv_item1);
			TextView tv_item2 = helper.getView(R.id.tv_item2);
			TextView tv_item3 = helper.getView(R.id.tv_item3);

			RouteEntity route = (RouteEntity) DBHelperSingleton.getInstance()
					.getObject(RouteEntity.class,
							"LINE_ID='" + item.getLineID() + "'");
			if (route != null) {
				tv_routeName.setText(route.getLINE_ALLNAME());
			}

			StringBuffer item1 = new StringBuffer();
			// int orientation = item.getOrientation();
			// item1.append(orientation == 0 ? "上行 " : "下行 ")
			item1.append(item.getLaneLocation() + " K")
					.append(item.getLandmarkStart()).append("+")
					.append(item.getLandmarkEnd());
			tv_item1.setText(item1.toString());

			tv_item3.setText(item.getDate());
			int type = item.getType();
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

			String strType = null;
			switch (type) {
			case 0:
			case 1:
				strType = "路面";
				break;
			case 2:
				strType = "路基";
				break;
			case 3:
				strType = "设施";
				break;
			case 4:
				strType = "绿化";
				break;

			default:
				break;
			}

			tv_item2.setText(item.getDiseaseType() + " " + str_level);

			if (item.isUploaded()) {
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

			if (mList_checked.contains(position)) {
				ckb_choose.setChecked(true);
			} else {
				ckb_choose.setChecked(false);
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
			Mod_disease d = mList_data.get(position);
			Intent intent = new Intent();
			intent.putExtra("id", d.getId());
			intent.putExtra("position", position);
			// switch (d.getType()) {
			// case 0:
			// intent.setClass(mContext, Detail_routineInspection.class);
			// break;
			// case 1:
			// case 2:
			// case 3:
			// case 4:
			// intent.setClass(mContext, Detail_pavement.class);
			// break;
			// case 5:
			// case 6:
			// case 7:
			// case 8:
			// intent.setClass(mContext, Detail_structure.class);
			// break;
			// default:
			// break;
			// }
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
		mTv_ppwItem0.setText("全选");
		TextView tv_ppwItem3 = (TextView) layout.findViewById(R.id.tv_ppwItem2);
		tv_ppwItem3.setVisibility(View.GONE);
		TextView tv_ppwItem1 = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		if (getIntent().getBooleanExtra("showInputBtn", false)) {
			tv_ppwItem1.setText("录入");
			tv_ppwItem1.setOnClickListener(this);
		} else {
			tv_ppwItem1.setVisibility(View.GONE);
		}
		mTv_ppwItem0.setOnClickListener(this);
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

	private void initAlertDialog() {
		mDialog = new Dialog(mContext);
		Window window = mDialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_common,
				null);
		Button btn_positive = (Button) view.findViewById(R.id.btn_positive);
		Button btn_negative = (Button) view.findViewById(R.id.btn_negative);
		btn_positive.setText("是");
		btn_negative.setText("否");
		mTv_dlgContent = (TextView) view.findViewById(R.id.tv_content);

		btn_negative.setOnClickListener(this);
		btn_positive.setOnClickListener(this);

		mDialog.setContentView(view);
		mDialog.show();
		window.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 上传数据到服务器
	 */
	private void sendData() {
		if (mList_checked.size() > 0) {
			final List<DmDinsp> list = new ArrayList<DmDinsp>();
			final List<DmDinspRecord> records = new ArrayList<DmDinspRecord>();
			final List<DssImage> dissIdArray = new ArrayList();
			for (Integer iterable : mList_checked) {
				if (iterable < mList_data.size()) {
					Mod_disease disease = (Mod_disease) mList_data
							.get(iterable);
					DmDinspRecord dinspRecord = (DmDinspRecord) DBHelperSingleton
							.getInstance().getObject(DmDinspRecord.class,
									"bId='" + disease.getId() + "'");
					if (dinspRecord != null) {
						records.add(dinspRecord);

						DmDinsp dinsp = (DmDinsp) DBHelperSingleton
								.getInstance().getObject(
										DmDinsp.class,
										"dinspId='" + dinspRecord.getDinspId()
												+ "'");
						if (dinsp != null) {
							Boolean flg = hasObj(list, dinsp);
							if (flg) {
								continue;
							}
							list.add(dinsp);

							String paths = disease.getPath();
							if (!TextUtils.isEmpty(paths)) {
								String[] url = paths.split(",");
								if (url.length > 0) {
									for (String path : url) {
										DssImage image=new DssImage();
										image.setDssId(dinspRecord.getDssId());
										image.setFileName(path.replace("/", ""));
										image.setFilePath(path);
										dissIdArray.add(image);
									}
								}
							}
						}
					}

				}
			}
			if (dissIdArray.size()>0){
				int i=0;
				final Lock lock = new ReentrantLock();
				for (DssImage dssImage:dissIdArray){
					lock.lock();
					i=i+1;
					final int  num=i;
					RequestParams params=new RequestParams(MainApplication.ImageUrl);
					params.addBodyParameter("dssId",dssImage.getDssId());
					File file=new File(dssImage.getFilePath());
					if (file.exists() && file.length()>0){
						params.addBodyParameter(dssImage.getFileName(),file);
						x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
							@Override
							public void onSuccess(String result) {
								lock.unlock();
								if (num == dissIdArray.size()) {
									uploadDmDinspRecord(list,records,dissIdArray);
								}
							}

							@Override
							public void onError(Throwable ex, boolean isOnCallback) {
								lock.unlock();
								mProDialog.dismiss();
								Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
							}

							@Override
							public void onCancelled(CancelledException cex) {
								lock.unlock();
								Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
							}

							@Override
							public void onFinished() {
								Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}else{
				uploadDmDinspRecord(list,records,dissIdArray);
			}

		}
	}

	private void uploadDmDinspRecord(final List<DmDinsp> list,final List<DmDinspRecord> records,final List<DssImage> dissIdArray){
		if (list.size() > 0 && records.size() > 0) {
//				WebServiceUtils.sendRiChangBingHai(list, records,
			WebServiceUtils.sendRiChangBingHai(list, records, dissIdArray,
					new HttpCallBack() {

						@Override
						public void callBack(SoapObject result) {
							// TODO Auto-generated method stub

							if (result != null) {
								mProDialog.dismiss();
								if (mDialog == null) {
									initAlertDialog();
								}
								mDialog.show();
								mTv_dlgContent.setText("上传成功，是否删除客户端数据");
								isUpload = false;
							} else {
								mProDialog.dismiss();
								showToast("服务器错误，上传失败！");
							}
						}
					});
		} else {
			mProDialog.dismiss();
			showToast("程序异常，上传失败！");
		}
	}

	/**
	 * 上传经常检查数据
	 */
	private void sendOtherData() {
		if (mList_checked.size() > 0) {
			final List<DmFinsp> finspsList = new ArrayList<DmFinsp>();
			final List<DmFinspRecord> finspRecordsList = new ArrayList<DmFinspRecord>();
			final List<DssImage> dissIdArray = new ArrayList();
			for (Integer iterable : mList_checked) {
				Mod_disease disease = (Mod_disease) mList_data.get(iterable);
				DmFinspRecord finspRecord = (DmFinspRecord) DBHelperSingleton
						.getInstance().getObject(
								DmFinspRecord.class,
								"bId='" + disease.getId()
										+ "' AND bId IS NOT NULL");
				if (finspRecord != null) {
					finspRecordsList.add(finspRecord);
					DmFinsp finsp = (DmFinsp) DBHelperSingleton.getInstance()
							.getObject(
									DmFinsp.class,
									"finspId='" + finspRecord.getFinspId()
											+ "'");
					if (finsp != null) {
						Boolean flg = hasObj(finspsList, finsp);
						if (flg) {
							continue;
						}
						finspsList.add(finsp);
					}
					String paths = disease.getPath();
					if (!TextUtils.isEmpty(paths)) {
						String[] url = paths.split(",");
						if (url.length > 0) {
							for (String path : url) {
								DssImage image=new DssImage();
								image.setDssId(finspRecord.getDssId());
								image.setFileName(path.replace("/", ""));
								image.setFilePath(path);
								dissIdArray.add(image);
							}
						}
					}
				}
			}
			if (dissIdArray.size()>0){
				int i=0;
				final Lock lock = new ReentrantLock();
				for (DssImage dssImage:dissIdArray){
					lock.lock();
					i=i+1;
					final int  num=i;
					RequestParams params=new RequestParams(MainApplication.ImageUrl);
					params.addBodyParameter("dssId",dssImage.getDssId());
					File file=new File(dssImage.getFilePath());
					if (file.exists() && file.length()>0){
						params.addBodyParameter(dssImage.getFileName(),file);
						x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
							@Override
							public void onSuccess(String result) {
								lock.unlock();
								if (num == dissIdArray.size()) {
									uploadDmFinspRecord(finspsList, finspRecordsList, dissIdArray);
								}
								Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
							}

							@Override
							public void onError(Throwable ex, boolean isOnCallback) {
								lock.unlock();
								mProDialog.dismiss();
								Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
							}

							@Override
							public void onCancelled(CancelledException cex) {
								lock.unlock();
								Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
							}

							@Override
							public void onFinished() {
								Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}else{
				uploadDmFinspRecord(finspsList, finspRecordsList, dissIdArray);
			}

		}
	}

	private void uploadDmFinspRecord(final List<DmFinsp> finspsList,final List<DmFinspRecord> finspRecordsList,final List<DssImage> dissIdArray){
		if (finspsList.size() > 0 && finspRecordsList.size() > 0) {
			WebServiceUtils.doDmFinsp(finspsList, finspRecordsList,
					new HttpCallBack() {

						@Override
						public void callBack(SoapObject result) {
							// TODO Auto-generated method stub
							mProDialog.dismiss();
							if (result != null) {
								for (int j = 0; j < result
										.getPropertyCount(); j++) {
									for (Integer iterable : mList_checked) {
										Mod_disease disease = (Mod_disease) mList_data
												.get(iterable);
										DBHelperSingleton
												.getInstance()
												.deleteData(
														DmFinspRecord.class,
														"bId='"
																+ disease
																.getId()
																+ "'");
									}
								}

								if (mDialog == null) {
									initAlertDialog();
								}
								mDialog.show();
								mTv_dlgContent.setText("上传成功，是否删除客户端数据");
								isUpload = false;
							} else {
								showToast("服务器错误，上传失败！");
							}
						}
					});
		} else {
			mProDialog.dismiss();
			showToast("程序异常，上传失败！");
		}
	}
	/**
	 * 删除或者不删除日常巡查的单子
	 */
	private void deleteRichang() {
		if (deleteTag) {
			for (Integer iterable : mList_checked) {
				if (iterable < mList_data.size()) {
					Mod_disease disease = (Mod_disease) mList_data
							.get(iterable);
					DBHelperSingleton.getInstance().deleteData(
							DmDinspRecord.class,
							"bId='" + disease.getId() + "'");
				}
			}
			delete();
			
		} else {
			for (Integer iterable : mList_checked) {
				if (iterable < mList_data.size()) {
					Mod_disease disease = (Mod_disease) mList_data
							.get(iterable);
					disease.setUploaded(true);
					DBHelperSingleton.getInstance().deleteData(
							DmDinspRecord.class,
							"bId='" + disease.getId() + "'");
					ContentValues vlues = new ContentValues();
					vlues.put("uploaded", true);
					DBHelperSingleton.getInstance().updateData(
							Mod_disease.class, vlues,
							"id='" + disease.getId() + "'");
				}
			}
			mList_data = DBHelperSingleton.getInstance().getDatas(
					Mod_disease.class, "type=" + mType + " ORDER BY id DESC");

		}
		mList_checked.clear();
		mLvAdapter.notifyDataSetChanged();
	}
	/**
	 * 删除经常检查数据
	 */
	private void deleteJingchang(){
		
		if (deleteTag) {
			for (Integer iterable : mList_checked) {
				Mod_disease disease = (Mod_disease) mList_data
						.get(iterable);
				DBHelperSingleton
						.getInstance()
						.deleteData(
								DmFinspRecord.class,
								"bId='"
										+ disease
												.getId()
										+ "'");
			}
			delete();
			
		}else {
			for (Integer iterable : mList_checked) {
				if (iterable < mList_data.size()) {
					Mod_disease disease = (Mod_disease) mList_data
							.get(iterable);
					disease.setUploaded(true);
					DBHelperSingleton.getInstance().deleteData(
							DmFinspRecord.class,
							"bId='" + disease.getId() + "'");
					ContentValues vlues = new ContentValues();
					vlues.put("uploaded", true);
					DBHelperSingleton.getInstance().updateData(
							Mod_disease.class, vlues,
							"id='" + disease.getId() + "'");
				}
			}
			mList_data = DBHelperSingleton.getInstance().getDatas(
					Mod_disease.class, "type=" + mType + " ORDER BY id DESC");

		}
		mList_checked.clear();
		mLvAdapter.notifyDataSetChanged();
	}

	/**
	 * 判断是否已经存在Dmdinsp主单
	 * 
	 * @param list
	 * @param dinsp
	 * @return
	 */
	private Boolean hasObj(List<DmDinsp> list, DmDinsp dinsp) {
		for (DmDinsp dmDinsp : list) {
			if (dmDinsp.getDinspId().equals(dinsp.getDinspId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断DmFinsp主单是否已经存在
	 * 
	 * @param dmfinspList
	 * @param dmFinsp
	 * @return
	 */
	private Boolean hasObj(List<DmFinsp> dmfinspList, DmFinsp dmFinsp) {
		for (DmFinsp finsp : dmfinspList) {
			if (finsp.getFinspId().equals(dmFinsp.getFinspId())) {
				return true;
			}
		}
		return false;
	}

	private ProgressDialog mProDialog;

	private void delete() {
		List<Mod_disease> list_d = new ArrayList<Mod_disease>();
		for (int pos : mList_checked) {
			Mod_disease d = mList_data.get(pos);
			DBHelperSingleton.getInstance().deleteData(Mod_disease.class,
					"id='" + d.getId() + "'");
			list_d.add(d);
		}

		mList_data.removeAll(list_d);

//		mList_checked.clear();
//		mLvAdapter.notifyDataSetChanged();
	}

	private void upload() {
		if (mProDialog == null) {
			mProDialog = new ProgressDialog(mContext);
			mProDialog.setTitle("上传中...");
			mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProDialog.setIndeterminate(false);
			mProDialog.setCanceledOnTouchOutside(false);
			mProDialog.setMax(100);

		}
		int sendType = 0;
		if (mDisease == null) {
			sendType = mType;
		} else {
			sendType = mDisease.getType();
		}
		switch (mType) {
		case 0:
			sendData();
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			sendOtherData();
			break;
		default:
			break;
		}

		mProDialog.show();

		// mHandler.post(run);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int progress = msg.arg1;
			mProDialog.setProgress(progress);
			if (progress == 100) {

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

	/**
	 * 根据关键字模糊查询数据
	 * 
	 * @param keyWord
	 * @return
	 */
	private List<Mod_disease> getDataWithKeyWord(String keyWord) {
		List<Mod_disease> diseasesList = new ArrayList<Mod_disease>();
		diseasesList = DBHelperSingleton.getInstance().getDatas(
				Mod_disease.class,
				"type='" + mType + "' AND (" + "date LIKE '%" + keyWord
						+ "%' OR diseaseType LIKE '%" + keyWord
						+ "%' OR laneLocation LIKE '%" + keyWord
						+ "%' OR landmarkStart LIKE '%" + keyWord
						+ "%' OR landmarkEnd LIKE '%" + keyWord
						+ "%' OR lineName LIKE '%" + keyWord + "%')");
		if (diseasesList!=null){
			if (diseasesList.size()<=0){
				Toast.makeText(MainApplication.mContext, "无符合条件数据", Toast.LENGTH_LONG).show();
			}
		}
		return diseasesList;
	}
	private void uploadImage(final List<DssImage> arrayList){

	}
}
