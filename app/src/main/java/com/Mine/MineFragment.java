package com.Mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Market.MarketFragment;
import com.Market.Models.BrustBean;
import com.Market.Models.CarouselBean;
import com.Market.Models.PreSellBean;
import com.Mine.Adapters.MineAdapter;
import com.Mine.Models.MineBean;
import com.Start.SmsLoginActivity;
import com.Tools.APIServices;
import com.Tools.SaveDatasUtils;
import com.Tools.ToastUtils;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.google.gson.Gson;
import com.gstore520.stone.gstore.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MineFragment extends Fragment {

    @BindView(R.id.recyclerV)
    RecyclerView recyclerV;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private MineAdapter mAdapter;
    public MineBean mineDatas;
    public Context mContext;

    private final String MINE_API = "user/detail";
    private static final String TAG = "MineFragment";
    private MineFragment.StHandler mHandler = new MineFragment.StHandler(this);
    public SaveDatasUtils obtainDatasUtils;

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this,v);
        mContext = getContext();
        obtainDatasUtils=new SaveDatasUtils(mContext, "fileStore");
        initViews();
        initDatas(0);
        return v;
    }
    public void initDatas(int ifR){
        setMineInfo(ifR);
    }
    public void initViews(){

        mAdapter = new MineAdapter(mContext, mineDatas);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerV.setLayoutManager(linearLayoutManager);

        mAdapter.setOnChildClickListener(new MineAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                ToastUtils.showShortToast(mContext,"子项：posi = " + childPosition);
            }
        });
        mAdapter.setHeaderVCallBack(new MineAdapter.HeaderVCallBack() {
            @Override
            public void clickIdx(int pos) {
                switch (pos){


                    case 0:
                        ToastUtils.showShortToast(mContext,"进入个人中心");
                        break;
                    case 1:
                        ToastUtils.showShortToast(mContext,"进入我的订单");
                        break;
                    default:
                        obtainDatasUtils.setObject("ifLogin",false);
                        obtainDatasUtils.setObject("authos","");
                        Intent intent = new Intent(mContext,SmsLoginActivity.class);
                        intent.putExtra("status_code", "4");
                        startActivityForResult(intent, 0);
                        ToastUtils.showShortToast(mContext,"注销成功");
                        break;
                }
            }
        });
        recyclerV.setAdapter(mAdapter);
        //设置 Header 为 贝塞尔雷达 样式
        //refreshLayout.setRefreshHeader(new BezierRadarHeader(mContext).setEnableHorizontalDrag(true));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initDatas(1);
                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
    }

    public void setMineInfo(final int ifR){
        //ifR------>0:未刷新    1:刷新
        try {
            StringBuilder realPath = new StringBuilder();
            realPath.append(APIServices.API_PATH)
                    .append(MINE_API)
                    .append("?vendor_id=4");
            String authos = String.valueOf(obtainDatasUtils.getObject("authos"));
            System.out.println(obtainDatasUtils.getObject("authos"));
            APIServices.sendGetRequest(realPath.toString(), authos, new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        Log.e("", "onFailure" + e.getMessage());
                        if(ifR == 1){
                            refreshLayout.finishRefresh();
                        }
                        }
                    });
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    Message msg = mHandler.obtainMessage();
                    Log.d(TAG, "handleMessage: "+responseStr);
                    msg.what = 0;
                    msg.obj = responseStr;
                    msg.arg1 = ifR;
                    mHandler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private static class StHandler extends Handler {
        private WeakReference<MineFragment> mReference;

        public StHandler(MineFragment fragment) {
            mReference = new WeakReference<MineFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        String responseStr = (String) msg.obj;
                        JSONObject responseJSON = new JSONObject(responseStr);
                        int code = responseJSON.getInt("code");
                        String message = responseJSON.getString("msg");
                        int totalMount = responseJSON.getInt("total");
                        if (code == APIServices.CODE_SUCCESS) {
                            if(msg.arg1 == 1){
                                mReference.get().refreshLayout.finishRefresh();
                            }
                            Gson gson = new Gson();
                            MineBean bean = null;
                            //data of type org.json.JSONObject cannot be converted to JSONArray
                            bean = gson.fromJson(responseJSON.getJSONObject("data").toString(), MineBean.class);
                            mReference.get().mineDatas = bean;
                            mReference.get().mAdapter.setMDatas(mReference.get().mineDatas);
                        }else if(code == APIServices.CODE_TOKEN_EXPIRED){
                            ToastUtils.showShortToast(mReference.get().mContext,message );
                            Intent intent = new Intent(mReference.get().mContext,SmsLoginActivity.class);
                            intent.putExtra("status_code", "0");
                            mReference.get().obtainDatasUtils.setObject("ifLogin",false);
                            mReference.get().obtainDatasUtils.setObject("authos","");
                            mReference.get().mContext.startActivity(intent);
                        }else{
                            Log.d(TAG, String.valueOf(code));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
