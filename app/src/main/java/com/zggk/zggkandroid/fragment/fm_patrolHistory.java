package com.zggk.zggkandroid.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.R;
import com.zggk.zggkandroid.activity.Detail_routineInspection;
import com.zggk.zggkandroid.activity.MainActivity;
import com.zggk.zggkandroid.common.CommonAdapter;
import com.zggk.zggkandroid.common.Constant;
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
 * Created by xsh on 2016/3/23.
 */
public class fm_patrolHistory extends Fragment implements View.OnClickListener, MainActivity.GetListCallBack {

    private Context mContext;
    private View mView;
    private TextView mTv_title;
    private ListView mLv_disease;
    private EditText mEt_search;
    private Button mBtn_delete, mBtn_upload;
    private LvAdapter mLvAdapter;
    private List<Mod_disease> mList_data;

    private boolean checkedAll = false;// 全选
    private List<Integer> mList_checked = new ArrayList<Integer>();// 选择项
    // private List<Integer> mList_uploaded = new ArrayList<Integer>();// 已上传项
    private boolean isUpload;

    private boolean isSearch;

    private Mod_disease mDisease;// 主单信息、点击录入用回到录入页面时回传用

    private int mType = 0;

    private Boolean deleteTag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_disease_list, null);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        MainActivity.setCallBack(this);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void initView() {
        // TODO Auto-generated method stub
        if (mList_data == null) {
            mList_data = new ArrayList<Mod_disease>();
        }
        mView.findViewById(R.id.layout_title).setVisibility(View.GONE);
        mTv_title = (TextView) mView.findViewById(R.id.tv_title);
        mTv_title.setText("病害列表");
        mLv_disease = (ListView) mView.findViewById(R.id.lv_disease);
        mEt_search = (EditText) mView.findViewById(R.id.et_search);
        mBtn_delete = (Button) mView.findViewById(R.id.btn_delete);
        mBtn_upload = (Button) mView.findViewById(R.id.btn_upload);

        mBtn_delete.setOnClickListener(this);
        mBtn_upload.setOnClickListener(this);
        mView.findViewById(R.id.iv_search).setOnClickListener(this);


        mLv_disease.setOnItemClickListener(new LvItemClickListener());

        mLvAdapter = new LvAdapter(mContext, mList_data,
                R.layout.lv_item_diseaselist);
        mLv_disease.setAdapter(mLvAdapter);

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_delete:
                if (mList_checked.size() == 0) {
                    Constant.showToast(mContext, "请选中需要删除的项目后再删除");
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
                    Constant.showToast(mContext, "请选中需要上传的项目后再上传");
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
    public void listCallBack() {
        String sql = "SELECT * FROM (SELECT * FROM Mod_disease ORDER BY id DESC) ORDER BY uploaded DESC";
        mList_data = DBHelperSingleton.getInstance().getData(sql,
                Mod_disease.class);
        if (mList_data == null) {
            mList_data = new ArrayList<Mod_disease>();
        }
        mLvAdapter = new LvAdapter(mContext, mList_data,
                R.layout.lv_item_diseaselist);
        mLv_disease.setAdapter(mLvAdapter);
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
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

    private class LvItemClickListener implements AdapterView.OnItemClickListener {

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
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void uploadDmDinspRecord(final List<DmDinsp> list, final List<DmDinspRecord> records, final List<DssImage> dissIdArray, SendDataSuccessCallBack callBack) {
        if (list.size() > 0 && records.size() > 0) {
//				WebServiceUtils.sendRiChangBingHai(list, records,
            WebServiceUtils.sendRiChangBingHai(list, records, dissIdArray,
                    new WebServiceUtils.HttpCallBack() {

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
                                Constant.showToast(mContext, "服务器错误，上传失败！");
                            }
                        }
                    });
        } else {
            mProDialog.dismiss();
            Constant.showToast(mContext, "程序异常，上传失败！");
        }
    }

    private void uploadDmFinspRecord(final List<DmFinsp> finspsList, final List<DmFinspRecord> finspRecordsList, final List<DssImage> dissIdArray) {
        if (finspsList.size() > 0 && finspRecordsList.size() > 0) {
            WebServiceUtils.doDmFinsp(finspsList, finspRecordsList,
                    new WebServiceUtils.HttpCallBack() {

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
                                Constant.showToast(mContext, "服务器错误，上传失败！");
                            }
                        }
                    });
        } else {
            mProDialog.dismiss();
            Constant.showToast(mContext, "程序异常，上传失败！");
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
    private void deleteJingchang() {

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

        } else {
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
        sendLocalData();

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
        if (diseasesList != null) {
            if (diseasesList.size() <= 0) {
                Toast.makeText(MainApplication.mContext, "无符合条件数据", Toast.LENGTH_LONG).show();
            }
        }
        return diseasesList;
    }

    public interface SendDataSuccessCallBack {
        public void sendSuccess();
    }

    private void sendLocalData() {
        if (mList_checked.size() > 0) {
            final List<DmFinsp> finspsList = new ArrayList<DmFinsp>();
            final List<DmDinsp> list = new ArrayList<DmDinsp>();
            final List<DmFinspRecord> finspRecordsList = new ArrayList<DmFinspRecord>();
            final List<DssImage> dissIdArray = new ArrayList();
            final List<DmDinspRecord> records = new ArrayList<DmDinspRecord>();
            final List<DssImage> finspDiss = new ArrayList<DssImage>();
            final List<DssImage> recordDiss = new ArrayList<DssImage>();
            for (Integer iterable : mList_checked) {
                Mod_disease disease = (Mod_disease) mList_data.get(iterable);
                if (disease.getType() == 0) {
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
                                        DssImage image = new DssImage();
                                        image.setDssId(dinspRecord.getDssId());
                                        image.setFileName(path.replace("/", ""));
                                        image.setFilePath(path);
                                        dissIdArray.add(image);
                                        recordDiss.add(image);
                                    }
                                }
                            }
                        }
                    }
                } else {
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
                                    DssImage image = new DssImage();
                                    image.setDssId(finspRecord.getDssId());
                                    image.setFileName(path.replace("/", ""));
                                    image.setFilePath(path);
                                    dissIdArray.add(image);
                                    finspDiss.add(image);
                                }
                            }
                        }
                    }
                }

            }
            if (dissIdArray.size() > 0) {

                int i = 0;
                final Lock lock = new ReentrantLock();
                for (DssImage dssImage : dissIdArray) {
                    lock.lock();
                    i = i + 1;
                    final int num = i;
                    RequestParams params = new RequestParams(MainApplication.ImageUrl);
                    params.addBodyParameter("dssId", dssImage.getDssId());
                    File file = new File(dssImage.getFilePath());
                    if (file.exists() && file.length() > 0) {
                        params.addBodyParameter(dssImage.getFileName(), file);
                        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                lock.unlock();
                                if (result.equals("1")){
                                    int count=num;
                                    if (count == dissIdArray.size()) {
                                        if (list.size()>0 && records.size()>0){
                                            uploadDmDinspRecord(list, records, recordDiss, new SendDataSuccessCallBack() {
                                                @Override
                                                public void sendSuccess() {

                                                }
                                            });
                                        }
                                        if (finspsList.size()>0 && finspRecordsList.size()>0){
                                            uploadDmFinspRecord(finspsList, finspRecordsList, finspDiss);
                                        }

                                    }else{
                                        Toast.makeText(x.app(), "上传失败，请保持网络畅通并重新上传，如果数据量大请分批上传！", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(x.app(), "上传失败，请保持网络畅通并重新上传，如果数据量大请分批上传！", Toast.LENGTH_LONG).show();
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

                            }
                        });
                    }
                }
            } else {
                uploadDmDinspRecord(list, records, recordDiss, new SendDataSuccessCallBack() {
                    @Override
                    public void sendSuccess() {
                        uploadDmFinspRecord(finspsList, finspRecordsList, finspDiss);
                    }
                });
            }

        }
    }
    /**
     * 全选
     */
    public void checkAll() {
        if (checkedAll) {
            mList_checked.clear();
        } else {
            for (int i = 0; i < mList_data.size(); i++) {
                mList_checked.add(i);
            }
        }
        mLvAdapter.notifyDataSetChanged();

        checkedAll = !checkedAll;
    }


}
