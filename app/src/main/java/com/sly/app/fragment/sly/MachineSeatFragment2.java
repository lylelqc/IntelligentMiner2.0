package com.sly.app.fragment.sly;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.machineSeat.ReleaseOreActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.adapter.MachineSeatRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.MachineSeatDataBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.OnRecyclerViewListener;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.WheelView;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class MachineSeatFragment2 extends BaseFragment implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, MachineSeatRecyclerViewAdapter.OnItemClickListener {
    public static String mContent = "???";
    @BindView(R.id.iv_drop_down)
    ImageView ivDropDown;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.machine_seat_rv)
    RecyclerView machineSeatRv;
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout llCommLayout;
    @BindView(R.id.rl_adress)
    RelativeLayout rlAdress;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_icon_iv)
    ImageView pageStatusIconIv;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;
    @BindView(R.id.refresh_again_tv)
    TextView refreshAgainTv;
    @BindView(R.id.refresh_again_btn)
    CardView refreshAgainBtn;
    Unbinder unbinder;

    private MachineSeatRecyclerViewAdapter mRecyclerViewAdapter;
    private List<MachineSeatDataBean> mResultList = new ArrayList<>();
    private String User, Token, Key, LoginType, SysNumber, FrSystemCode;
    private String ProvinceCode = "";
    private String CityCode = "";
    private String CountryCode = "";
    private String Name, Tell, Address, isDefault, Province, City, County, Region;
    Dialog mBottomSheetDialog;


    /**
     * 下拉刷新
     **/
    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    ICommonRequestPresenter iCommonRequestPresenter;

    IRecyclerViewPresenter iRecyclerViewPresenter;


    public static MachineSeatFragment2 newInstance(String content) {
        MachineSeatFragment2 fragment = new MachineSeatFragment2();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragmeng_machine_seat;
    }

    @Override
    protected void onFirstUserVisible() {
        showPageStatusView("数据加载中...");
        initJsonData();
        initSelectDialog();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        SysNumber = SharedPreferencesUtil.getString(mContext, LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode", "None");
    }


    /**
     * 初始化
     **/
    @Override
    protected void initViewsAndEvents() {
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        swipeRefreshLayout.setOnRefreshListener(this);
        machineSeatRv.addOnScrollListener(new MyScrollListener());

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");

        tvMainTitle.setText("机位信息");
        btnMainBack.setVisibility(View.GONE);
        llRight.setVisibility(View.GONE);
        initRv();
        firstRefresh();
    }


    private void firstRefresh() {

        if (NetUtils.isNetworkConnected(mContext)) {

            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        toRefreshRequest();

                    }
                }, NetWorkCons.Integers.PAGE_LAZY_LOAD_DELAY_TIME_MS);
            }
        } else {
            if (mResultList != null && mResultList.size() > 0) {
                return;
            }
            showRefreshRetry(Contants.NetStatus.NETWORK_MAYBE_DISABLE);
        }
    }

    @Override
    public void clickLoadMoreData() {
        toLoadMoreRequest();
    }

    @Override
    public void toRefreshRequest() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        mCurrentPage = 1;

        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.021");
        map.put("User", User);

        if(LoginType.equals("Miner")){
            map.put("minerSysCode", SysNumber);
        }else{
            map.put("minerSysCode", SysNumber);
        }

        map.put("provinceCode", ProvinceCode);
        map.put("cityCode", CityCode);
        map.put("countryCode", CountryCode);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("机位信息 - " + mapJson);
    }

    @Override
    public void toLoadMoreRequest() {
        if (isRequesting)
            return;

        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            iLoadView.showErrorView(loadMoreView);
            return;
        }

        if (mResultList.size() < NetWorkCons.Request.PAGE_NUMBER) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.021");
        map.put("User", User);

        if(LoginType.equals("Miner")){
            map.put("minerSysCode", SysNumber);
        }else{
            map.put("minerSysCode", SysNumber);
        }
        map.put("provinceCode", ProvinceCode);
        map.put("cityCode", CityCode);
        map.put("countryCode", CountryCode);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("机位信息 - " + mapJson);
    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        Logcat.e("机位信息返回参数 - " + result);
        List<MachineSeatDataBean> resultList = parseResult(result);

        mResultList.clear();
        if (resultList != null && resultList.size() != 0) {
            mResultList.addAll(resultList);
            pageStatusTextTv.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            pageStatusTextTv.setText("暂时木有数据哦！");
            pageStatusTextTv.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }

        refreshListView();
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        Logcat.e("机位信息返回参数 - " + result);
        List<MachineSeatDataBean> resultList = parseResult(result);

        if (resultList.size() == 0) {
            iLoadView.showFinishView(loadMoreView);
        }
        mResultList.addAll(resultList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
//        showToastShort(msg);
        if (NetWorkCons.EventTags.HOMEADV == eventTag) {
            return;
        }
        if (mCurrentPage > 1)
            mCurrentPage--;

        if (eventTag == Contants.HttpStatus.loadmore_data) {
            iLoadView.showErrorView(loadMoreView);
        }
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
//        showToastShort(msg);
        if (NetWorkCons.EventTags.HOMEADV == eventTag) {
            return;
        }
        if (mCurrentPage > 1)
            mCurrentPage--;

        if (eventTag == Contants.HttpStatus.loadmore_data) {
            iLoadView.showErrorView(loadMoreView);
        }
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        if (NetWorkCons.EventTags.HOMEADV != eventTag && !status) {
            isRequesting = status;
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            return;
        }
        ProvinceCode = "";
        CityCode = "";
        CountryCode = "";
        tvArea.setText("请选择地址");
        toRefreshRequest();
    }

    public void refreshListView() {

        MachineSeatRecyclerViewAdapter mIntermediary = new MachineSeatRecyclerViewAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        machineSeatRv.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(machineSeatRv, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        machineSeatRv.setLayoutManager(layoutManager);
        mIntermediary.setOnItemClickListener(this);
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<MachineSeatDataBean> parseResult(String result) {
//        数据解析
        JsonHelper<MachineSeatDataBean> dataParser = new JsonHelper<MachineSeatDataBean>(
                MachineSeatDataBean.class);
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void onItemClick(View view, int position) {
        final String id = mResultList.get(position).getMine79_ID();
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clear_history_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.textView5)).setText("确定删除矿位吗？");
        dialog.findViewById(R.id.tv_dialog_detail).setVisibility(View.GONE);
        dialog.show();

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_action);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirm_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                toRequsetInfo(id);
            }
        });
    }


    public class MyScrollListener extends OnRecyclerViewListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            toLoadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }

        @Override
        public void getScrollY(int y) {

        }
    }


    private void initRv() {
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        SysNumber = SharedPreferencesUtil.getString(mContext, LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode", "None");

        if (LoginType.equals("None")) {
            llRight.setVisibility(View.GONE);
            tvMainRight.setText("");
        } else {
            llRight.setVisibility(View.VISIBLE);
            tvMainRight.setText("发布机位");
        }
//        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
//        machineSeatRv.setLayoutManager(mLayoutManager);
//        machineSeatRv.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerViewAdapter = new MachineSeatRecyclerViewAdapter(getContext(), mResultList, "价格：");
//        machineSeatRv.setAdapter(mRecyclerViewAdapter);
    }

    @OnClick({R.id.rl_adress, R.id.ll_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_adress:
                if (mProvinceDatas == null || mProvinceDatas.size() == 0) {
                    return;
                }
                showSelectAddDialog();
                break;
            case R.id.ll_right:
                if (StringUtils.isEmpty(Token) || StringUtils.isEmpty(User)) {
                    ToastUtils.showToast("请先登录");
                    CommonUtil2.goActivity(mContext, LoginActivity.class);
                } else { //if(!LoginType.equals("Miner"))
                    checkRzStatus();
                }
                break;
        }
    }

    public void showSelectAddDialog() {

        mBottomSheetDialog.show();
    }

    private void initSelectDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_select_address, null);

        provinceEt = (WheelView) view.findViewById(R.id.select_province_et);
        cityEt = (WheelView) view.findViewById(R.id.select_city_et);
        areaEt = (WheelView) view.findViewById(R.id.select_area_et);

        view.findViewById(R.id.select_enter_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultProvinceValue = provinceEt.getSelectedText();
                mDefaultCityValue = cityEt.getSelectedText();
                mDefaultAreaValue = areaEt.getSelectedText();
                tvArea.setText(mDefaultProvinceValue);
                ProvinceCode = mDefaultProvinceIdValue;
//                CityCode = mDefaultCityIdValue;
//                CountryCode = mDefaultAreaIdValue;
                toRefreshRequest();
                mBottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.select_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        provinceEt.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mHandler.removeMessages(0);
                Message message = new Message();
                message.what = 0;
                message.arg1 = id;
                mHandler.sendMessageDelayed(message, 100);

            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        cityEt.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mHandler.removeMessages(1);
                Message message = new Message();
                message.what = 1;
                message.arg1 = id;
                mHandler.sendMessageDelayed(message, 100);

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        areaEt.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mHandler.removeMessages(2);
                Message message = new Message();
                message.what = 2;
                message.arg1 = id;
                mHandler.sendMessageDelayed(message, 100);

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        provinceEt.setData(mProvinceValues);

        provinceEt.setDefault(0);
        Message message = new Message();
        message.arg1 = 0;
        message.what = 0;
        mHandler.sendMessageDelayed(message, 100);


        mBottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    WheelView provinceEt;

    WheelView cityEt;

    WheelView areaEt;
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONArray mJsonObj;

    /**
     * 所有省
     */
    private ArrayList<Map<String, String>> mProvinceDatas;
    private ArrayList<String> mProvinceValues = new ArrayList<>();
    /**
     * key - 省 value - 市s
     */
    private Map<String, ArrayList<Map<String, String>>> mCitisDatasMap = new HashMap<>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, ArrayList<Map<String, String>>> mAreaDatasMap = new HashMap<>();

    private boolean isFirst = true;

    private String mDefaultProvinceValue = "", mDefaultCityValue = "", mDefaultAreaValue = "";
    private String mDefaultProvinceIdValue = "", mDefaultCityIdValue = "", mDefaultAreaIdValue = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                if (!StringUtils.isEmpty(provinceEt.getSelectedText())) {

                    String areaId = mProvinceDatas.get(msg.arg1).get("area_id");
                    mDefaultProvinceIdValue = areaId;
                    ArrayList<Map<String, String>> list = mCitisDatasMap.get(areaId);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (list == null || list.size() == 0) {
                        cityEt.resetData(arrayList);
                    } else {
                        for (Map<String, String> map : list) {
                            arrayList.add(map.get("area_name"));
                        }
                        cityEt.resetData(arrayList);
                    }

//                    if (isFirst) {
//                        if (arrayList.contains(mDefaultCityValue)) {
//                            cityEt.setDefault(list.indexOf(mDefaultCityValue));
//                        } else {
//                            cityEt.setDefault(0);
//                        }
//                    }
                    cityEt.setDefault(0);
                }

                if (!CommonUtils.isBlank(cityEt.getSelectedText())) {
                    String areaId = mProvinceDatas.get(msg.arg1).get("area_id");
                    String areaId2 = mCitisDatasMap.get(areaId).get(cityEt.getSelected()).get("area_id");
                    mDefaultCityIdValue = areaId2;

                    ArrayList<Map<String, String>> list = mAreaDatasMap.get(areaId2);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (list == null || list.size() == 0) {
                        areaEt.resetData(arrayList);
                    } else {
                        for (Map<String, String> map : list) {
                            arrayList.add(map.get("area_name"));
                        }
                        areaEt.resetData(arrayList);
                    }

                    if (isFirst) {

                        if (list.contains(mDefaultAreaValue)) {

                            areaEt.setDefault(list.indexOf(mDefaultAreaValue));
                        } else {
                            areaEt.setDefault(2);
                        }
                    }
                }

                isFirst = false;
            } else if (msg.what == 1) {
                if (!CommonUtils.isBlank(cityEt.getSelectedText())) {

                    String areaId2 = mCitisDatasMap.get(mDefaultProvinceIdValue).get(cityEt.getSelected()).get("area_id");
                    mDefaultCityIdValue = areaId2;
                    ArrayList<Map<String, String>> list = mAreaDatasMap.get(areaId2);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (list == null || list.size() == 0) {
                        areaEt.resetData(arrayList);
                    } else {
                        for (Map<String, String> map : list) {
                            arrayList.add(map.get("area_name"));
                        }
                        areaEt.resetData(arrayList);
                    }
                    areaEt.setDefault(0);

//                    if (isFirst) {
//
//                        if (list.contains(mDefaultAreaValue)) {
//
//                            areaEt.setDefault(list.indexOf(mDefaultAreaValue));
//                        } else {
//                            areaEt.setDefault(2);
//                        }
//                    }
                }
            } else if (msg.what == 2) {

                String areaId2 = mCitisDatasMap.get(mDefaultProvinceIdValue).get(cityEt.getSelected()).get("area_id");
                mDefaultCityIdValue = areaId2;
                ArrayList<Map<String, String>> list = mAreaDatasMap.get(areaId2);
                mDefaultAreaIdValue = list.get(msg.arg1).get("area_id");
            }
        }
    };

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();

            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("pcc.json"));

            //文件读入流
            BufferedReader br = new BufferedReader(inputReader);
            //逐行读取test.txt内容，保存在line里
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            inputReader.close();
            mJsonObj = new JSONArray(sb.toString());
            initDatas();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {

            mProvinceDatas = new ArrayList<>();

            for (int i = 0; i < mJsonObj.length(); i++) {
                Map<String, String> map = new HashMap<>();
                JSONObject jsonP = mJsonObj.getJSONObject(i);// 每个省的json对象
                map.put("area_id", jsonP.getString("ProvinceCode"));
                map.put("area_name", jsonP.getString("ProvinceName"));
                mProvinceValues.add(jsonP.getString("ProvinceName"));
                mProvinceDatas.add(map);
                System.out.println(mJsonObj.length() + "____" + jsonP.getString("ProvinceName"));
                JSONArray jsonCs = null;
                try {
                    jsonCs = jsonP.getJSONArray("City");
                } catch (Exception e1) {
                    continue;
                }
                ArrayList<Map<String, String>> mCitiesDatas = new ArrayList<>();
                for (int j = 0; j < jsonCs.length(); j++) {
                    Map<String, String> cmap = new HashMap<>();
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    cmap.put("area_id", jsonCity.getString("CityCode"));
                    cmap.put("area_name", jsonCity.getString("CityName"));
                    mCitiesDatas.add(cmap);

                    JSONArray jsonAreas = null;
                    try {
                        jsonAreas = jsonCity.getJSONArray("Area");
                    } catch (Exception e) {
                        continue;
                    }

                    ArrayList<Map<String, String>> mAreasDatas = new ArrayList<>();// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        Map<String, String> amap = new HashMap<>();
                        JSONObject jsonArea = jsonAreas.getJSONObject(k);
                        amap.put("area_id", jsonArea.getString("AreaCode"));
                        amap.put("area_name", jsonArea.getString("AreaName"));
                        mAreasDatas.add(amap);
                    }
                    mAreaDatasMap.put(jsonCity.getString("CityCode"), mAreasDatas);
                }

                mCitisDatasMap.put(jsonP.getString("ProvinceCode"), mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
        hidePageStatusView();
    }

    private void checkRzStatus() {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("Rounter", "Sly.031");
        map.put("User", User);

        map.put("LoginType", LoginType);
        if (LoginType.equals("Miner")) {
            map.put("sysCode", SysNumber);
        } else {
            map.put("sysCode", SysNumber);
        }

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content) + "\n提交的内容：" + json);
                        JSONObject jsonObject = null;
                        try {
                            ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                            if (returnBean.getStatus().equals("1")) {
                                if (returnBean.getData().equals("true")) {
                                    CommonUtil2.goActivity(mContext, ReleaseOreActivity.class);
                                } else {
                                    ToastUtils.showToast("您还未进行身份认证!");
                                }

                            } else {
                                ToastUtils.showToast(jsonObject.optString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }

    private void toRequsetInfo(String id) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("Rounter", "Sly.044");
        map.put("User", User);

        map.put("LoginType", LoginType);
        if (LoginType.equals("Miner")) {
            map.put("minerSysCode", SysNumber);
        } else {
            map.put("minerSysCode", SysNumber);
        }
        map.put("ID", id);

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
//                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content) + "\n提交的内容：" + json);
                        JSONObject jsonObject = null;
                        try {
                            ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                            if (returnBean.getStatus().equals("0")) {
                                ToastUtils.showToast("删除机位成功");
                                firstRefresh();
                            } else {
                                ToastUtils.showToast("删除机位失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }

}
