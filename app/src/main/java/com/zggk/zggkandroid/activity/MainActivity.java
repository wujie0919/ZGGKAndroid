package com.zggk.zggkandroid.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.common.Constant;
import com.zggk.zggkandroid.common.Data;
import com.zggk.zggkandroid.fragment.fm_disease;
import com.zggk.zggkandroid.fragment.fm_main;
import com.zggk.zggkandroid.fragment.fm_manage;
import com.zggk.zggkandroid.fragment.fm_patrolHistory;
import com.zggk.zggkandroid.interfaces.Interfaces;
import com.zggk.zggkandroid.interfaces.Interfaces.ChangePage;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements ChangePage,
        OnClickListener {

    public Context mContext = MainActivity.this;
    private ViewPager mVp_main;
    private RadioGroup mRg_bottomMenu;
    private RadioButton mRb_main;
    // private RadioButton mRb_report;
    private RadioButton mRb_history;
    private RadioButton mRb_disease;
    // private RadioButton mRb_focusDisease;
    private RadioButton mRb_manage;

    private TextView mTv_title;
    private ImageButton mBtn_search;
    private TextView mTv_checkAll;
    private PopupWindow mPPW_menu;

    private fm_patrolHistory mFM_patrolHistory;

    public static GetListCallBack getCallBack() {
        return callBack;
    }

    public static void setCallBack(GetListCallBack callBack) {
        MainActivity.callBack = callBack;
    }

    private static GetListCallBack callBack;

    public interface GetListCallBack {
        public void listCallBack();
    }

    public static SetLengthCallBack getSetLengthCallBack() {
        return setLengthCallBack;
    }

    public static void setSetLengthCallBack(SetLengthCallBack setLengthCallBack) {
        MainActivity.setLengthCallBack = setLengthCallBack;
    }

    private static SetLengthCallBack setLengthCallBack;

    public interface SetLengthCallBack {
        public void setLength();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        mVp_main = (ViewPager) findViewById(R.id.vp_main);
        mVp_main.setAdapter(new VpAdapter(getSupportFragmentManager()));
        mVp_main.setOnPageChangeListener(new VPPageChangeListener());
        mVp_main.setOffscreenPageLimit(3);

        mRg_bottomMenu = (RadioGroup) findViewById(R.id.rg_bottomMenu);
        mRg_bottomMenu
                .setOnCheckedChangeListener(new RgCheckedChangedListener());
        mRb_main = (RadioButton) findViewById(R.id.rb_main);
        // mRb_report = (RadioButton) findViewById(R.id.rb_report);
        mRb_history = (RadioButton) findViewById(R.id.rb_history);
        mRb_disease = (RadioButton) findViewById(R.id.rb_disease);
        // mRb_focusDisease = (RadioButton) findViewById(R.id.rb_focusDisease);
        mRb_manage = (RadioButton) findViewById(R.id.rb_manage);

        mTv_title = (TextView) findViewById(R.id.tv_title);

        mBtn_search = (ImageButton) findViewById(R.id.btn_search);
        mBtn_search.setOnClickListener(this);
        mTv_checkAll = (TextView) findViewById(R.id.tv_main_checkAll);
        mTv_checkAll.setOnClickListener(this);

        mRb_main.setChecked(true);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Interfaces.getInstance().setChangePage(this);
        Intent intent = getIntent();
        boolean isSetting = intent.getBooleanExtra("setting", false);
        if (isSetting) {
            mVp_main.setCurrentItem(4);
        }
        // 把setting重置为false
        intent.putExtra("setting", false);
    }

    private class RgCheckedChangedListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mBtn_search.setVisibility(View.GONE);
            mTv_checkAll.setVisibility(View.GONE);
            mRb_main.setTextColor(getResources().getColor(R.color.gray_txt));
            // mRb_report.setTextColor(getResources().getColor(R.color.gray_txt));
            mRb_history.setTextColor(getResources().getColor(R.color.gray_txt));
            mRb_disease.setTextColor(getResources().getColor(R.color.gray_txt));
            // mRb_focusDisease.setTextColor(getResources().getColor(
            // R.color.gray_txt));
            mRb_manage.setTextColor(getResources().getColor(R.color.gray_txt));

            mRb_main.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.ic_main_normal, 0, 0);
            // mRb_report.setCompoundDrawablesWithIntrinsicBounds(0,
            // R.drawable.ic_report_normal, 0, 0);
            mRb_disease.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.ic_disease_normal, 0, 0);
            mRb_history.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.ic_history_normal, 0, 0);
            // mRb_focusDisease.setCompoundDrawablesWithIntrinsicBounds(0,
            // R.drawable.ic_focus_disease_normal, 0, 0);
            mRb_manage.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.ic_manage_normal, 0, 0);
            switch (checkedId) {
                case R.id.rb_main:
                    mRb_main.setTextColor(getResources()
                            .getColor(R.color.blue_dark));
                    mRb_main.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.ic_main_press, 0, 0);
                    mTv_title.setText("功能模块");
                    mVp_main.setCurrentItem(0);
                    break;
                case R.id.rb_report:
                    // mRb_report.setTextColor(getResources().getColor(
                    // R.color.blue_dark));
                    // mRb_report.setCompoundDrawablesWithIntrinsicBounds(0,
                    // R.drawable.ic_report_press, 0, 0);
//                    mTv_title.setText("定检报告");
//                    mBtn_search.setVisibility(View.VISIBLE);
//                    mVp_main.setCurrentItem(1);
                    break;
                case R.id.rb_history:
                    mRb_history.setTextColor(getResources().getColor(
                            R.color.blue_dark));
                    mRb_history.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.ic_history_press, 0, 0);
                    mTv_title.setText("巡查记录");
                    mVp_main.setCurrentItem(1);
                    mTv_checkAll.setVisibility(View.VISIBLE);
                    getListData();
                    break;
                case R.id.rb_disease:
                    mRb_disease.setTextColor(getResources().getColor(
                            R.color.blue_dark));
                    mRb_disease.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.ic_disease_press, 0, 0);
                    mTv_title.setText("历史病害");
//				mBtn_search.setVisibility(View.VISIBLE);
                    mVp_main.setCurrentItem(2);
                    if (setLengthCallBack !=null){
                        setLengthCallBack.setLength();
                    }
                    break;
                case R.id.rb_focusDisease:
                    // mRb_focusDisease.setTextColor(getResources().getColor(
                    // R.color.blue_dark));
                    // mRb_focusDisease.setCompoundDrawablesWithIntrinsicBounds(0,
                    // R.drawable.ic_focus_disease_press, 0, 0);
//                    mTv_title.setText("重点病害");
//                    mBtn_search.setVisibility(View.VISIBLE);
//                    mVp_main.setCurrentItem(3);
                    break;
                case R.id.rb_manage:
                    mRb_manage.setTextColor(getResources().getColor(
                            R.color.blue_dark));
                    mRb_manage.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.ic_manage_press, 0, 0);
                    mTv_title.setText("设置");
                    mVp_main.setCurrentItem(3);
                    break;
                default:
                    break;
            }
        }
    }

    private void getListData(){
        if (callBack!=null){
            callBack.listCallBack();
        }
    }

    private class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
            // TODO
        }

        @Override
        public Fragment getItem(int position) {
            // TODO
            switch (position) {
                case 0:
                    return new fm_main();
                case 1:
                    if (mFM_patrolHistory == null) {
                        mFM_patrolHistory = new fm_patrolHistory();
                    }
                    return mFM_patrolHistory;
                case 2:
                    return new fm_disease();
                // case 3:
                // return new fm_focusDisease();
                case 3:
                    return new fm_manage();
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            // TODO
            return 4;
        }
    }

    private class VPPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub

            switch (position) {
                case 0:
                    mRb_main.setChecked(true);
                    break;
                case 1:
                    mRb_history.setChecked(true);
                    break;
                case 2:
                    // mRb_report.setChecked(true);
                    mRb_disease.setChecked(true);
                    break;
                case 3:
                    mRb_manage.setChecked(true);
                    // mRb_disease.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void doChangePage(int position) {
        // TODO Auto-generated method stub
        mVp_main.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_search:
                // Intent intent = new Intent();
                // intent.setClass(mContext, DiseaseList.class);
                // startActivity(intent);
                break;
            case R.id.layout_project:
                Constant.getInstance().showSelectDialog(mContext, Data.project(),
                        mTv_search_project);
                break;
            case R.id.layout_route:
                Constant.getInstance().showSelectDialog(mContext, Data.route(),
                        mTv_search_route);
                break;
            case R.id.btn_cancel:
                mDialog.dismiss();
                break;
            case R.id.btn_ok:
                mDialog.dismiss();
                Constant.getInstance().showProgress(mContext, "");
                dismissDialog();
                break;
            case R.id.tv_main_checkAll:
//                mFM_patrolHistory.checkAll();
                String text = mTv_checkAll.getText().toString();
                if ("全选".equals(text)) {
                    mTv_checkAll.setText("取消全选");
                } else {
                    mTv_checkAll.setText("全选");
                }
                break;
            default:
                break;
        }
    }

    private Timer mTimer;
    private Handler mHandler = new Handler();

    private void dismissDialog() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO
                        Constant.getInstance().dismissDialog();
                    }
                });
            }
        }, 1000, 1000);
    }

    private Dialog mDialog;
    private Window mDialogWindow;
    private TextView mTv_search_year, mTv_search_project, mTv_search_route;

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
}
