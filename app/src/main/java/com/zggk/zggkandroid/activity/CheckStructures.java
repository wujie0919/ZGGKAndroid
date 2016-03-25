package com.zggk.zggkandroid.activity;

import java.util.List;

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
import com.zggk.zggkandroid.common.Data;
import com.zggk.zggkandroid.common.SharedPres;
import com.zggk.zggkandroid.common.ViewHolder;
import com.zggk.zggkandroid.entity.Mod_disease;
import com.zggk.zggkandroid.entity.RouteEntity;
import com.zggk.zggkandroid.interfaces.Interfaces;
import com.zggk.zggkandroid.utils.DataUtils;

/**
 * 结构物经常检查
 * 
 * @author xsh
 * 
 */
@SuppressLint("InflateParams")
public class CheckStructures extends BaseActivity implements OnClickListener {

	private Context mContext = CheckStructures.this;
	private EditText mEt_user, mEt_zongPing, mEt_comment;
	private TextView mTv_date, mTv_route, mTv_maneUnits, mTv_curingUnits,
			mTv_bridge, mTv_checkItem;
	private RelativeLayout mLayout_route, mLayout_date, mLayout_structure;
	private Button mBtn_ok;

	private PopupWindow mPPW_menu;
	private ImageButton mBtn_back;

	private int mType;

	private List<RouteEntity> mList_route;
	private RouteEntity mRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_structure);

		mType = getIntent().getIntExtra("type", 5);

		initView();

		boolean loaded = DataUtils.isDataLoaded(RouteEntity.class);
		if (loaded) {
			mList_route = DataUtils.getDatas(RouteEntity.class);
		} else {
			DataUtils.getRouteList(mContext);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("检查基本信息");
		mBtn_back = (ImageButton) findViewById(R.id.btn_back);
		mLayout_route = (RelativeLayout) findViewById(R.id.layout_route);
		mLayout_date = (RelativeLayout) findViewById(R.id.layout_date);
		mLayout_structure = (RelativeLayout) findViewById(R.id.layout_structure);
		mTv_route = (TextView) findViewById(R.id.tv_route);
		mTv_date = (TextView) findViewById(R.id.tv_date);
		mTv_bridge = (TextView) findViewById(R.id.tv_structureName);
		mTv_checkItem = (TextView) findViewById(R.id.tv_checkItem);
		mTv_maneUnits = (TextView) findViewById(R.id.tv_manageUnits);
		mTv_curingUnits = (TextView) findViewById(R.id.tv_curingUnits);

		mEt_user = (EditText) findViewById(R.id.et_user);
		mEt_comment = (EditText) findViewById(R.id.et_comment);
		mEt_zongPing = (EditText) findViewById(R.id.et_zongPing);
		mBtn_ok = (Button) findViewById(R.id.btn_ok);

		mBtn_ok.setOnClickListener(this);
		mBtn_back.setOnClickListener(this);
		mLayout_date.setOnClickListener(this);
		mLayout_route.setOnClickListener(this);
		mLayout_structure.setOnClickListener(this);

		mTv_date.setText(Constant.getInstance().getCurDate());
		mEt_user.setText(SharedPres.getInstance(mContext).getUserName());

		mRoute = SharedPres.getInstance(mContext).getRoute();
		if (mRoute != null) {
			mTv_route.setText(mRoute.getLINE_ALLNAME());
		}

		switch (mType) {
		case 5:
			mTv_checkItem.setText("检查桥梁");
			break;
		case 6:
			mTv_checkItem.setText("检查涵洞");
			break;
		case 7:
			mTv_checkItem.setText("检查隧道");
			break;
		case 8:
			mTv_checkItem.setText("检查边坡");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.layout_route:
			if (mList_route != null) {
				showRouteListDialog();
			} else {
				mList_route = DataUtils.getDatas(RouteEntity.class);
				if (mList_route != null) {
					showRouteListDialog();
				}
			}
			break;
		case R.id.layout_date:
			Constant.getInstance().showDataPickerDialog(mContext, mTv_date);
			break;
		case R.id.layout_structure:
			List<String> list_data = null;
			switch (mType) {
			case 5:
				list_data = Data.bridge();
				break;
			case 6:
				list_data = Data.culvert();
				break;
			case 7:
				list_data = Data.tunnel();
				break;
			case 8:
				list_data = Data.side();
				break;

			default:
				break;
			}
			Constant.getInstance().showSelectDialog(mContext, list_data,
					mTv_bridge);
			break;
		// case R.id.btn_menu:
		// if (mPPW_menu == null) {
		// initPpw();
		// }
		// mPPW_menu.showAsDropDown(v);
		// break;
		case R.id.btn_ok:
			enterInput();
			break;
		default:
			break;
		}
	}

	private void enterInput() {
		String date = mTv_date.getText().toString();
		String user = mEt_user.getText().toString().trim();
		String bridge = mTv_bridge.getText().toString();
		String zongpin = mEt_zongPing.getText().toString().trim();
		String comment = mEt_comment.getText().toString().trim();
		String manageUnits = mTv_maneUnits.getText().toString();
		String curingUnits = mTv_curingUnits.getText().toString();

		Mod_disease d = new Mod_disease();
		d.setType(mType);
		d.setRoute(mRoute);
		d.setLineName(mRoute.getLINE_ALLNAME());
		d.setLineID(mRoute.getLINE_ID());
		d.setDate(date);
		d.setUser(user);
		d.setStructureName(bridge);
		d.setZongpin(zongpin);
		d.setComment(comment);
		d.setManeUnits(manageUnits);
		d.setCuringUnits(curingUnits);

		Intent intent = new Intent();
		intent.setClass(mContext, InputDisease_structures.class);
		intent.putExtra("type", mType);
		intent.putExtra("action", "input");
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

}
