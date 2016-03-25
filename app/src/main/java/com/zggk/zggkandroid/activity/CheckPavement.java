package com.zggk.zggkandroid.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.common.SharedPres;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.DmFinsp;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.Pavement;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.http.WebServiceUtils;
import com.zggk.zggkandroid.http.WebServiceUtils.HttpCallBack;
import com.zggk.zggkandroid.interfaces.Interfaces;
import com.zggk.zggkandroid.utils.DataUtils;
import com.zggk.zggkandroid.utils.ParseUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

/**
 * 路面经常检查
 * 
 * @author xsh
 * 
 */
@SuppressLint("InflateParams")
public class CheckPavement extends BaseActivity implements OnClickListener {

	private Context mContext = CheckPavement.this;
	private EditText mEt_user, mEt_section, mEt_zongPing, mEt_direction,
			mEt_comment;
	private TextView mTv_date, mTv_route, mTv_maneUnits, mTv_curingUnits;
	private RelativeLayout mLayout_route, mLayout_date;
	private Button mBtn_ok;

	private PopupWindow mPPW_menu;
	private ImageButton mBtn_back;

	private List<RouteEntity> mList_route;
	private RouteEntity mRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_pavement);

		initView();

		boolean loaded = DataUtils.isDataLoaded(RouteEntity.class);
		if (loaded) {
			mList_route = DataUtils.getDatas(RouteEntity.class);
		} else {
			getRouteList();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		int type = getIntent().getIntExtra("type", 1);
		String title="";
		switch (type){
			case 1:
				title="路面";
				break;
			case 2:
				title="路基";
				break;
			case 3:
				title="设施";
				break;
			case 4:
				title="绿化";
				break;
		}
		tv_title.setText(title+"检查基本信息");
		ImageButton btn_search = (ImageButton) findViewById(R.id.btn_search);
		btn_search.setVisibility(View.VISIBLE);
		btn_search.setOnClickListener(this);

		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mLayout_route = (RelativeLayout) findViewById(R.id.layout_route);
		mLayout_date = (RelativeLayout) findViewById(R.id.layout_date);
		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_maneUnits = (TextView) findViewById(R.id.tv_manageUnits);
		mTv_curingUnits = (TextView) findViewById(R.id.tv_curingUnits);

		mEt_user = (EditText) findViewById(R.id.et_user);
		mEt_section = (EditText) findViewById(R.id.et_section);
		mEt_comment = (EditText) findViewById(R.id.et_comment);
		mEt_direction = (EditText) findViewById(R.id.et_direction);
		mEt_zongPing = (EditText) findViewById(R.id.et_zongPing);
		mBtn_ok = (Button) findViewById(R.id.btn_ok);

		mBtn_ok.setOnClickListener(this);
		mBtn_back.setOnClickListener(this);
		mLayout_date.setOnClickListener(this);
		mLayout_route.setOnClickListener(this);

		mTv_date.setText(Constant.getInstance().getCurDate());

		String user = MainApplication.mCurAccounInfo.getUSER_NAME();
		mEt_user.setText(user);

		mRoute = SharedPres.getInstance(mContext).getRoute();
		if (mRoute != null) {
			mTv_route.setText(mRoute.getLINE_ALLNAME());
		}

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
			intent.putExtra("type", getIntent().getIntExtra("type", 0));
			startActivity(intent);
			break;
		case R.id.layout_route:
			if (mList_route != null && mList_route.size() != 0) {
				showRouteListDialog();
			} else {
				getRouteList();
			}
			break;
		case R.id.layout_date:
			Constant.getInstance().showDataPickerDialog(mContext, mTv_date);
			break;
		// case R.id.btn_menu:
		// if (mPPW_menu == null) {
		// initPpw();
		// }
		// mPPW_menu.showAsDropDown(v);
		// break;
		case R.id.btn_ok:
			if (mRoute == null) {
				showToast("请选择巡查线路");
			} else {
				enterInput();
			}
			break;
		default:
			break;
		}
	}

	private void enterInput() {
		String date = mTv_date.getText().toString();
		String user = mEt_user.getText().toString().trim();
		String direction = mEt_direction.getText().toString().trim();
		String section = mEt_section.getText().toString().trim();
		String zongpin = mEt_zongPing.getText().toString().trim();
		String comment = mEt_comment.getText().toString().trim();
		String manageUnits = mTv_maneUnits.getText().toString();
		String curingUnits = mTv_curingUnits.getText().toString();

		int type = getIntent().getIntExtra("type", 1);
		String staticId = "";

		// （路面：LM，路基：LJ，沿线设施：JA,绿化：LH）
		String types = "";
		switch (type) {
		case 1:
			types = "LM";
			break;
		case 2:
			types = "LJ";
			break;
		case 3:
			types = "JA";
			break;
		case 4:
			types = "LH";
			break;
		default:
			break;
		}

		DmFinsp dmFinsp = new DmFinsp();
		Pavement pavement = (Pavement) DBHelperSingleton.getInstance()
				.getObject(
						Pavement.class,
						"lineCode='" + mRoute.getLINE_ID() + "' AND DateTime='"
								+ date + "' AND type='" + types + "'");
		staticId = String.valueOf(System.currentTimeMillis());
		if (pavement != null) {
			dmFinsp.setFinspId(pavement.getpId());
		} else {
			pavement = new Pavement();
			dmFinsp.setFinspId(MainApplication.getUUID());
			pavement.setpId(dmFinsp.getFinspId());
			pavement.setDateTime(date);
			pavement.setLineCode(mRoute.getLINE_ID());
			pavement.setOrg_id(MainApplication.getCurUserinfo().getORG_ID());
			pavement.setUser_code(MainApplication.getCurUserinfo()
					.getUSER_CODE());

			pavement.setType(types);
			DBHelperSingleton.getInstance().insertData(pavement);
		}
		dmFinsp.setFacilityCat(types);
		dmFinsp.setLineCode(mRoute.getLINE_ID());
		dmFinsp.setCreateUserId(MainApplication.getCurUserinfo().getUSER_CODE());
		dmFinsp.setInspDate(date);
		dmFinsp.setMntnOrgNm(MainApplication.getCurUserinfo().getORG_NAME());
		dmFinsp.setSearchDept(MainApplication.getCurUserinfo().getUNT_NAME());
		dmFinsp.setMntOrgId(MainApplication.getCurUserinfo().getORG_ID());
		dmFinsp.setInspPerson(MainApplication.getCurUserinfo().getUSER_NAME());
		dmFinsp.setLineDirect(direction);
		dmFinsp.setRoadSection(section);
		dmFinsp.setRemark(comment);
		DBHelperSingleton.getInstance().insertOrReplaceData(dmFinsp);
		Mod_disease d = new Mod_disease();
		d.setType(type);
		d.setRoute(mRoute);
		d.setLineID(mRoute.getLINE_ID());
		d.setDate(date);
		d.setUser(user);
		d.setDirection(direction);
		d.setSection(section);
		d.setZongpin(zongpin);
		d.setComment(comment);
		d.setManeUnits(manageUnits);
		d.setCuringUnits(curingUnits);
		if (mRoute != null) {
			d.setRoute(mRoute);
		}
		d.setId(staticId);
		d.setZhuId(dmFinsp.getFinspId());
		Intent intent = new Intent();
		intent.setClass(mContext, InputRoutineInspection.class);
		intent.putExtra("action", "input");
		intent.putExtra("type", type);
		Bundle b = new Bundle();
		b.putSerializable("data", d);
		intent.putExtra("data", b);
		startActivity(intent);
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void initPpw() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ppw_menu, null);
		TextView tv_setting = (TextView) layout.findViewById(R.id.tv_ppwItem1);
		TextView tv_exitLogin = (TextView) layout
				.findViewById(R.id.tv_ppwItem2);
		tv_exitLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, Login.class);
				startActivity(intent);
				finish();
			}
		});
		tv_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Interfaces.getInstance().startChangePage(4);
				mPPW_menu.dismiss();
				finish();
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

	private void showRouteListDialog() {
		final Dialog dialog = new Dialog(mContext);
		Window window = dialog.getWindow();
		window.requestFeature(1);

		View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_select,
				null);
		ListView lv_select = (ListView) view.findViewById(R.id.lv_select);

		lv_select.setAdapter(new CommonAdapter<RouteEntity>(mContext,
				mList_route, R.layout.lv_item_text) {

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
				mRoute = mList_route.get(position);
				mTv_route.setText(mRoute.getLINE_ALLNAME());
				SharedPres.getInstance(mContext).saveRoute(mRoute);
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.show();
		window.setLayout((int) (MainApplication.mScreenW * 0.9f),
				LayoutParams.WRAP_CONTENT);
		dialog.show();

	}

	private void getRouteList() {
		showPreDialog("正在获取线路列表，请稍后...");
		// 避免重复获取数据
		if (!isRequest) {
			isRequest = true;
			WebServiceUtils.getRouteData(new HttpCallBack() {

				@Override
				public void callBack(SoapObject result) {
					// TODO Auto-generated method stub
					dismissPreDialog();
					isRequest = false;
					if (result != null) {
						// log(result.toString());
						mList_route = ParseUtils.parseRouteData(result);
					}
				}
			});
		}

	}

}
