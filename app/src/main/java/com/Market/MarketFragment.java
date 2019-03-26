package com.Market;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Market.Adapters.MarketAdapter;
import com.Market.Models.BrustBean;
import com.Market.Models.CarouselBean;
import com.Market.Models.PreSellBean;
import com.Tools.APIServices;
import com.Tools.ToastUtils;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.layoutmanger.GroupedGridLayoutManager;
import com.google.gson.Gson;
import com.gstore520.stone.gstore.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanxuwen.DensityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MarketFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;

    @BindView(R.id.recyclerV)
    RecyclerView recyclerV;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private MarketAdapter mAdapter;
    public List<BrustBean> brustDatas;
    public List<PreSellBean> preSellDatas;
    public List<CarouselBean> carouselDatas;
    public Context mContext;

    private final String CAROUSEL_API = "vendor/vendor_round";
    private final String PRESELL_API = "vendor/list_presell";
    private final String BRUST_API = "vendor/list_burst";
    private static final String TAG = "MarketFragment";
    private MarketFragment.StHandler mHandler = new MarketFragment.StHandler(this);
    public MarketFragment() {
        // Required empty public constructor
    }

    public static MarketFragment newInstance(String param1, String param2) {
        MarketFragment fragment = new MarketFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_market, null);
        ButterKnife.bind(this,v);
        mContext = getContext();
        initViews();
        initDatas(0);
        return v;
    }
    public void initDatas(int ifR){
        setCarousel(ifR);
        setPreSell(ifR);
        setBrust(ifR);
    }
    public void initViews(){

        brustDatas = new ArrayList<>();

        preSellDatas = new ArrayList<>();

        carouselDatas = new ArrayList<>();

        toolBarTitle.setOnClickListener(this);

        mAdapter = new MarketAdapter(mContext, brustDatas , preSellDatas,carouselDatas);
        GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(mContext, 2, mAdapter){
            //重写这个方法 改变子项的SpanSize。
            //这个跟重写SpanSizeLookup的getSpanSize方法的使用是一样的。
            @Override
            public int getChildSpanSize(int groupPosition, int childPosition) {

                return super.getChildSpanSize(groupPosition, childPosition);
            }
        };
        //设置RecyclerView布局
        recyclerV.addItemDecoration(new SpaceItemDecoration(30));
        recyclerV.setLayoutManager(gridLayoutManager);
        mAdapter.setOnHeaderClickListener(new MarketAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition) {
                ToastUtils.showShortToast(mContext,"组头：groupPosition = " + groupPosition);
            }
        });
        mAdapter.setOnChildClickListener(new MarketAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                final BrustBean brustItem;
                final PreSellBean preSellItem;
                if(groupPosition == 0){
                    preSellItem = preSellDatas.get(childPosition);
                    Intent intent = new Intent(mContext,GoodDetailActivity.class);
                    intent.putExtra("presell_id", String.valueOf(preSellItem.getId()));
                    startActivity(intent);
                }else{
                    brustItem = brustDatas.get(childPosition);
                    ToastUtils.showShortToast(mContext,"爆款商品ID:id= " + brustItem.getId()+ brustItem.getProduct_name());
                }
            }
        });
        mAdapter.setCarouselCallBack(new MarketAdapter.CarouselCallBack() {
            @Override
            public void clickIdx(int pos) {
                final CarouselBean carouselItem;
                carouselItem = carouselDatas.get(pos);
                ToastUtils.showShortToast(mContext,"轮播图ID:id= "+carouselItem.getId() + carouselItem.getTitle());
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
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int viewType =  parent.getAdapter().getItemViewType(parent.getChildLayoutPosition(view));
            if(viewType==mAdapter.HEADER_BANNER || viewType==mAdapter.HEADER_COMMON ){
                outRect.left = 0;
            }else{
                outRect.left = space;
                if (parent.getChildLayoutPosition(view)%2==1) {
                    outRect.right = 0;
                }else{
                    outRect.right = space;
                }
            }
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

    @Override
    public void onClick(View view) {

    }

    public void setCarousel(final int ifR){
        //ifR------>0:未刷新    1:刷新
        try {
            StringBuilder realPath = new StringBuilder();
            realPath.append(APIServices.API_PATH)
                    .append(CAROUSEL_API)
                    .append("?vendor_id=4");
            APIServices.sendGetRequest(realPath.toString(), null, new Callback() {
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
                    System.out.println(responseStr);
                    Message msg = mHandler.obtainMessage();
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

    public void setPreSell(final int ifR){
        //ifR------>0:未刷新    1:刷新
        try {
            StringBuilder realPath = new StringBuilder();
            realPath.append(APIServices.API_PATH)
                    .append(PRESELL_API)
                    .append("?vendor_id=4");
            APIServices.sendGetRequest(realPath.toString(), null, new Callback() {
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
                    msg.what = 1;
                    msg.obj = responseStr;
                    msg.arg1 = ifR;
                    mHandler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setBrust(final int ifR){
        //ifR------>0:未刷新    1:刷新
        try {
            StringBuilder realPath = new StringBuilder();
            realPath.append(APIServices.API_PATH)
                    .append(BRUST_API)
                    .append("?vendor_id=4");
            APIServices.sendGetRequest(realPath.toString(), null, new Callback() {
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
                    msg.what = 2;
                    msg.obj = responseStr;
                    msg.arg1 = ifR;
                    mHandler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private static class StHandler extends Handler {
        private WeakReference<MarketFragment> mReference;

        public StHandler(MarketFragment fragment) {
            mReference = new WeakReference<MarketFragment>(fragment);
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
                                mReference.get().carouselDatas.removeAll(mReference.get().carouselDatas);
                            }
                            JSONArray data = responseJSON.getJSONArray("data");
                            int len = data.length();
                            Gson gson = new Gson();
                            CarouselBean bean = null;
                            for (int i = 0; i < len; i++) {
                                JSONObject itemJSON = data.getJSONObject(i);
                                bean = gson.fromJson(itemJSON.toString(), CarouselBean.class);
                                mReference.get().carouselDatas.add(bean);
                            }
                            mReference.get().mAdapter.setMDatas(mReference.get().preSellDatas, mReference.get().brustDatas,mReference.get().carouselDatas);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        String responseStr = (String) msg.obj;
                        JSONObject responseJSON = new JSONObject(responseStr);
                        int code = responseJSON.getInt("code");
                        String message = responseJSON.getString("msg");
                        int totalMount = responseJSON.getInt("total");
                        if (code == APIServices.CODE_SUCCESS) {
                            if(msg.arg1 == 1){
                                mReference.get().preSellDatas.removeAll(mReference.get().preSellDatas);
                            }
                            JSONArray data = responseJSON.getJSONArray("data");
                            int len = data.length();
                            Gson gson = new Gson();
                            PreSellBean bean = null;
                            for (int i = 0; i < len; i++) {
                                JSONObject itemJSON = data.getJSONObject(i);
                                bean = gson.fromJson(itemJSON.toString(), PreSellBean.class);
                                mReference.get().preSellDatas.add(bean);
                            }
                            mReference.get().mAdapter.setMDatas(mReference.get().preSellDatas, mReference.get().brustDatas,mReference.get().carouselDatas);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        String responseStr = (String) msg.obj;
                        JSONObject responseJSON = new JSONObject(responseStr);
                        int code = responseJSON.getInt("code");
                        String message = responseJSON.getString("msg");
                        int totalMount = responseJSON.getInt("total");
                        if (code == APIServices.CODE_SUCCESS) {
                            if(msg.arg1 == 1){
                                mReference.get().brustDatas.removeAll(mReference.get().brustDatas);
                                mReference.get().refreshLayout.finishRefresh();
                            }
                            JSONArray data = responseJSON.getJSONArray("data");
                            int len = data.length();
                            Gson gson = new Gson();
                            BrustBean bean = null;
                            for (int i = 0; i < len; i++) {
                                JSONObject itemJSON = data.getJSONObject(i);
                                bean = gson.fromJson(itemJSON.toString(), BrustBean.class);
                                mReference.get().brustDatas.add(bean);
                            }
                            mReference.get().mAdapter.setMDatas(mReference.get().preSellDatas, mReference.get().brustDatas,mReference.get().carouselDatas);
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

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}
