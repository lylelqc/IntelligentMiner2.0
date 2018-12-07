package com.sly.app.activity.point;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.ShopCartAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.GoodsInfo;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.StoreInfo;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/9/13 14:17
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 * <p/> ExpandableListView
 * 想实现ChildView的点击事件，实现接口onChildClick
 * 第一，在适配器里isChildSelectable 必须返回true.
 * 第二，在ChildView布局child_item_layout.xml最外层的layout设置个属性 android:descendantFocusability="blocksDescendants"
 * beforeDescendants：viewgroup会优先其子类控件而获取到焦点
 * afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
 * blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点
 * ExpandableListView   http://blog.csdn.net/sinat_29962405/article/details/51265080
 */
public class MPSCActivity extends BaseActivity implements ShopCartAdapter.CheckInterface, ShopCartAdapter.ModifyCountInterface {

    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.elv_list)
    ExpandableListView mElvList;
    @BindView(R.id.cb_all)
    CheckBox mCbAll;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tvPostPrice)
    TextView mTvPostPrice;
    @BindView(R.id.tv_buy_submit)
    TextView mTvGoToPay;
    @BindView(R.id.id_ll_normal_all_state)
    LinearLayout mIdLlNormalAllState;
    @BindView(R.id.tv_save)
    TextView mTvSave;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.id_ll_editing_all_state)
    LinearLayout mIdLlEditingAllState;
    @BindView(R.id.id_rl_foot)
    RelativeLayout mIdRlFoot;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;
    @BindView(R.id.rl_layout)
    RelativeLayout mRlLayout;
    @BindView(R.id.ll_layout_empty)
    LinearLayout mLayoutEmpty;
    private int flag = 0;
    private int totalCount = 0;// 购买的商品总数量
    private double totalPrice = 0.00;// 购买的商品总价
    private String MemberCode, Token;
    private boolean proInitialize;//初始化选中
    private GoodsBean mGoodsBean;
    private ShopCartAdapter mAdapter;
    private List<GoodsInfo> products;
    private List<StoreInfo> groups = new ArrayList<StoreInfo>();// 组元素数据列表
    private Map<String, List<GoodsInfo>> children = new HashMap<String, List<GoodsInfo>>();// 子元素数据列表

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        setAllS(MemberCode, Token);
        GetCartList(MemberCode, Token);

    }

    @Override
    protected void initView() {
        mBtnTitleText.setText("积分商城购物车");
        mTvTitleRight.setText("编辑");
        mTvTitleRight.setTextColor(Color.WHITE);
        mElvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Logcat.i("--setOnChildClickListener----" + "group " + groupPosition + " child " + childPosition);
//                GetComIDInfo(products.get(childPosition).getComID());
                return false;
            }
        });
        mElvList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                Logcat.i("--setOnGroupClickListener----" + "group " + groupPosition);
                return false;
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_m_s_cart;
    }


    @Override
    protected void setListener() {
    }

    @OnClick({R.id.btn_title_back, R.id.tv_title_right, R.id.tv_delete, R.id.tv_save, R.id.tv_buy_submit, R.id.cb_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                if (flag == 0) {
                    mIdLlNormalAllState.setVisibility(View.GONE);
                    mIdLlEditingAllState.setVisibility(View.VISIBLE);
                    mTvTitleRight.setText("完成");
                    mAdapter.setupEditingAll(true);//
                } else if (flag == 1) {
                    mIdLlNormalAllState.setVisibility(View.VISIBLE);
                    mIdLlEditingAllState.setVisibility(View.GONE);
                    mTvTitleRight.setText("编辑");
                    mAdapter.setupEditingAll(false);
                }
                flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                break;
            case R.id.tv_delete:
                if (totalCount == 0) {
                    ToastUtils.showToast("请选择要删除的商品");
                } else {
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.clear_history_alert);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ((TextView) dialog.findViewById(R.id.textView5)).setText("确认删除该商品？");
                    ((TextView) dialog.findViewById(R.id.tv_dialog_detail)).setVisibility(View.GONE);
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
                            doDelete();
                            ToastUtils.showToast("删除的方法");
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.tv_save:
                ToastUtils.showToast("移到收藏");
                break;
            case R.id.tv_buy_submit:
                Logcat.i("------初始化选中是否完成："+proInitialize);
                if (proInitialize = true) {
                    ArrayList<GoodsInfo> buyList = new ArrayList<>();
                    for (GoodsInfo bean : products) {
                        if (bean.isChoosed() == true) {
                            buyList.add(bean);
                        }
                    }
                    if (buyList.size() != 0) {
                        doSelectS(buyList);
                    } else {
                        ToastUtils.showToast("请选择要购买的商品");
                    }
                }else {
                    setAllS(MemberCode, Token);
                }
                break;
            case R.id.cb_all:
                doCheckAll();
                break;
        }
    }

    private void GetCartList(String memberCode, String token) {
        initProgressDialog("加载中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("mallType", "JF");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.CART_COM_LIST, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                NetLogCat.W(NetWorkConstant.CART_COM_LIST, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    try {
                        JSONArray jsonArray = new JSONArray(mReturnBean.getData());
                        if (jsonArray.length() > 0) {
                            products = new ArrayList<GoodsInfo>();
                            groups.add(new StoreInfo("1", "地球村商城", false, false, 1));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                products.add(
                                        new GoodsInfo(
                                                object.optString("CartCode"),
                                                object.optString("ComID"),
                                                object.optString("ComTitle"),
                                                object.getDouble("Price"),
                                                object.getInt("Quantity"),
                                                object.optString("ComPicUrl"),
                                                object.optString("OptionIDCombin"),
                                                object.optString("OptionIDCombinRemark"),
                                                object.optString("Fanxuan"),
                                                object.optString("mallType"),
                                                object.optString("ClassCode"),
                                                object.optString("AddTime"),
                                                false
                                        ));
                            }
                            children.put("1", products);
                            mAdapter = new ShopCartAdapter(groups, children, MPSCActivity.this);
                            mElvList.setAdapter(mAdapter);
                            mAdapter.setCheckInterface(MPSCActivity.this);// 关键步骤1,设置复选框接口
                            mAdapter.setModifyCountInterface(MPSCActivity.this);// 关键步骤2,设置数量增减接口
                            mAdapter.notifyDataSetChanged();
                            if (mAdapter != null && mAdapter.getGroupCount() > 0) {
                                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                                    mElvList.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                                }
                            }
                        } else {
                            mTvTitleRight.setVisibility(View.GONE);
                            mRlLayout.setVisibility(View.GONE);
                            mLayoutEmpty.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }


    private void GetComIDInfo(String comID) {
        initProgressDialog("加载中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("ComID", comID);
        map.put("MemberCode", "");
        map.put("Token", "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_DETAIL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkConstant.COM_DETAIL, json, statusCode, content);
                dismissProgressDialog();
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    mGoodsBean = JSON.parseObject(mReturnBean.getData(), GoodsBean.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("ComBean", mGoodsBean);
                 //   startActivityWithExtras(PointComDetailActivity.class, mBundle);
                }
                return false;
            }
        });
    }

    private void doSelectS(ArrayList<GoodsInfo> buyList) {
        List<String> deleteList = new ArrayList<String>();
        String selectCartCode = null;
        for (Iterator<String> iterator = children.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            List<GoodsInfo> mGoodsInfo = children.get(key);
            for (int i = 0; i < mGoodsInfo.size(); i++) {
                if (mGoodsInfo.get(i).isChoosed() == true) {
                    deleteList.add(mGoodsInfo.get(i).getCartCode());
                    if (CommonUtils.isBlank(selectCartCode)) {
                        selectCartCode = "'" + mGoodsInfo.get(i).getCartCode() + "'";
                    } else {
                        selectCartCode = selectCartCode + "," + "'" + mGoodsInfo.get(i).getCartCode() + "'";
                    }
                }
            }
        }
        NetSelectS(selectCartCode, buyList);
    }

    private void NetSelectS(String CartCode, final ArrayList<GoodsInfo> buyList) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("CartCode", CartCode);
        final String json = JSON.toJSONString(map);
        HttpClient.postJson(NetWorkConstant.CART_SELECT, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.CART_SELECT, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelableArrayList("buy", buyList);
                    mBundle.putDouble("TotalPrice", Double.valueOf(mTvTotalPrice.getText().toString().substring(1, mTvTotalPrice.getText().toString().length())));
                    Logcat.i(buyList.size()+"||||"+Double.valueOf(mTvTotalPrice.getText().toString().substring(1, mTvTotalPrice.getText().toString().length())));
                    startActivityWithExtras(PointMallAffirmOrder.class, mBundle);
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });
    }

    /**
     * 初始化未选中
     */
    private void setAllS(String memberCode, final String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.CART_All_SELECT_S, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.CART_All_SELECT_S, json, statusCode, content);
                ReturnBean mReturn = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturn.getStatus().equals("1")) {
                    proInitialize = true;
                } else {
                    Logcat.i("初始化选中：" + mReturn.getMsg());
                }
                return false;
            }
        });
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     * 3.分两步删除 1 服务器的 2 本地的
     */

    private void doDelete() {
        List<String> deleteList = new ArrayList<String>();
        String delCartCode = null;
        for (Iterator<String> iterator = children.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            List<GoodsInfo> mGoodsInfo = children.get(key);
            for (int i = 0; i < mGoodsInfo.size(); i++) {
                if (mGoodsInfo.get(i).isChoosed() == true) {
                    deleteList.add(mGoodsInfo.get(i).getCartCode());
                    if (CommonUtils.isBlank(delCartCode)) {
                        delCartCode = "'" + mGoodsInfo.get(i).getCartCode() + "'";
                    } else {
                        delCartCode = delCartCode + "," + "'" + mGoodsInfo.get(i).getCartCode() + "'";
                    }
                }
            }
        }
        Logcat.i("----------delCartCode---------" + delCartCode);
        NetDeleteCarGoods(delCartCode);
    }

    /**
     * 删除成功之后在删除 本地的
     *
     * @param CartCode 购物车ID
     */
    private void NetDeleteCarGoods(String CartCode) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("CartCode", CartCode);
        final String json = JSON.toJSONString(map);
        HttpClient.postJson(NetWorkConstant.CART_DElET_ALL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.W(NetWorkConstant.CART_DElET_ALL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                    List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>();// 待删除的组元素列表
                    for (int i = 0; i < groups.size(); i++) {
                        StoreInfo group = groups.get(i);
                        if (group.isChoosed()) {
                            toBeDeleteGroups.add(group);
                        }
                        List<GoodsInfo> toBeDeleteProducts = new ArrayList<GoodsInfo>();// 待删除的子元素列表
                        List<GoodsInfo> childs = children.get(group.getId());
                        for (int j = 0; j < childs.size(); j++) {
                            if (childs.get(j).isChoosed()) {
                                toBeDeleteProducts.add(childs.get(j));
                            }
                        }
                        childs.removeAll(toBeDeleteProducts);
                    }
                    groups.removeAll(toBeDeleteGroups);
                    setCartNum();  //删除之后刷新数据
                    mAdapter.notifyDataSetChanged();

                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }

            }
        });
    }

    /**
     * 设置购物车产品数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(mCbAll.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (GoodsInfo goodsInfo : childs) {
                count += 1;
            }
        }
        //购物车已清空
        if (count == 0) {
            clearCart();
        } else {
            mBtnTitleText.setText("购物车" + "(" + count + ")");
        }

    }

    /**
     * 购物车数量为零的时候
     */
    private void clearCart() {
        mBtnTitleText.setText("购物车" + "(" + 0 + ")");
        mRlLayout.setVisibility(View.GONE);
        mTvTitleRight.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        groups.clear();
        totalPrice = 0;
        totalCount = 0;
        children.clear();
    }

    //选择店铺下的
    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck()) {
            mCbAll.setChecked(true);
        } else {
            mCbAll.setChecked(false);
        }
        mAdapter.notifyDataSetChanged();
        calculate();
    }


    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }
        if (isAllCheck()) {
            mCbAll.setChecked(true);// 全选
        } else {
            mCbAll.setChecked(false);// 反选
        }
        //  setSelectS(products.get(childPosition).getComID(), products.get(childPosition).getCartCode());
        mAdapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 加
     */
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, String num, boolean isChecked) {
       /* GoodsInfo product = (GoodsInfo) mAdapter.getChild(groupPosition, childPosition);
        int currentCount = Integer.valueOf(num);
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        mAdapter.notifyDataSetChanged();
        calculate();*/
        ToastUtils.showToast("该模块待更新");
    }

    /**
     * 减
     */
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, String num, boolean isChecked) {
      /*  GoodsInfo product = (GoodsInfo) mAdapter.getChild(groupPosition,
                childPosition);
        int currentCount = Integer.valueOf(num);
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        mAdapter.notifyDataSetChanged();
        calculate();*/
        ToastUtils.showToast("该模块待更新");

    }


    /**
     * 子类左滑删除 By 预留
     */
    @Override
    public void childDelete(int groupPosition, int childPosition) {

    }

    /**
     * 统计操作
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    totalPrice += product.getPrice() * product.getCount();
                }
            }
        }

        String price = String.format(Locale.getDefault(), "%.2f", totalPrice);
        mTvTotalPrice.setText("￥" + price);
        mTvGoToPay.setText("结算(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if (totalCount == 0) {
            setCartNum();
        } else {
            mBtnTitleText.setText("购物车" + "(" + totalCount + ")");
        }
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(mCbAll.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childBeans = children.get(group.getId());
            for (int j = 0; j < childBeans.size(); j++) {
                childBeans.get(j).setChoosed(mCbAll.isChecked());
            }
        }
        mAdapter.notifyDataSetChanged();
        //   setAllS(MemberCode, Token);
        calculate();
    }

    private boolean isAllCheck() {
        for (int i = 0; i < groups.size(); i++) {
            List<GoodsInfo> childs = children.get(groups.get(i).getId());
            for (GoodsInfo info : childs) {
                if (!info.isChoosed())
                    return false;
            }
        }
        return true;
    }
}
