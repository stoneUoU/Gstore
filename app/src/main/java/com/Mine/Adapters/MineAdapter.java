package com.Mine.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.Mine.Models.MineBean;
import com.View.CircleImageView;
import com.bumptech.glide.Glide;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.gstore520.stone.gstore.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class MineAdapter extends GroupedRecyclerViewAdapter implements View.OnClickListener{
    public MineBean mineDatas;
    private static final String TAG = "MineAdapter";

    private LayoutInflater mInflater;
    private Context mContext;
    public Banner mBanner;

    public final int HEADER_VIEW = 0;
    public final int CELL_VIEW = 1;
    public final int FOOTER_VIEW = 2;

    public HeaderVCallBack headerVCallBack;//回调接口类

    private List<Object> cellDatas;

    public interface HeaderVCallBack{
        public void clickIdx(int pos);
    }

    public void setHeaderVCallBack(HeaderVCallBack callBackListener) {
        this.headerVCallBack = callBackListener;
    }

    public MineAdapter(Context context,MineBean mineDatas) {
        super(context);
        this.mContext = context;
        this.mineDatas = mineDatas;
        this.mInflater = LayoutInflater.from(mContext);
        cellDatas = new ArrayList<Object>(){{add(new ArrayList<Object>(){{add(R.mipmap.icon_mima); add("账户密码");add(R.mipmap.mine_icon_go_28_dark);}});add(new ArrayList<Object>(){{add(R.mipmap.icon_kefu); add("在线客服");add(R.mipmap.mine_icon_go_28_dark);}});}};
    }
    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosi) {
        return cellDatas.size();
    }
    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.headerv_for_mine;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.footerv_for_mine;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_for_mine;
    }
    @Override
    public int getHeaderViewType(int groupPosition){
        return HEADER_VIEW;
    }
    @Override
    public int getChildViewType(int groupPosition, int childPosition){
        return CELL_VIEW;
    }
    @Override
    public int getFooterViewType(int groupPosition){
        return FOOTER_VIEW;
    }
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        CircleImageView avaterIMV = (CircleImageView) holder.get(R.id.avaterIMV);
        if(mineDatas != null){
            holder.setText(R.id.nameTv, mineDatas.getNick_name()).setText(R.id.telTv,mineDatas.getTel()).setText(R.id.orderTv,"我的订单").setText(R.id.allTv,"查看全部").setBackgroundColor(R.id.lineV, Color.parseColor("#ff000000"))
                    .setText(R.id.valTv01,mineDatas.getOrder_nums().get(0).toString())
                    .setText(R.id.valTv02,mineDatas.getOrder_nums().get(1).toString())
                    .setText(R.id.valTv03,mineDatas.getOrder_nums().get(2).toString())
                    .setText(R.id.valTv04,mineDatas.getOrder_nums().get(3).toString())
                    .setText(R.id.valTv05,mineDatas.getOrder_nums().get(4).toString());
            Glide.with(mContext)
                    .load("https://pic.shop.znrmny.com/static/ggshop/v5"+mineDatas.getProfile_photo())
                    .into(avaterIMV);
            holder.get(R.id.topRl).setOnClickListener(this);
            holder.get(R.id.midCr).setOnClickListener(this);

            holder.get(R.id.order01RL).setOnClickListener(this);
            holder.get(R.id.order02RL).setOnClickListener(this);
            holder.get(R.id.order03RL).setOnClickListener(this);
            holder.get(R.id.order04RL).setOnClickListener(this);
            holder.get(R.id.order05RL).setOnClickListener(this);
        }
    }
    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        holder.setText(R.id.exitBtn, "退出登录");
        holder.get(R.id.exitBtn).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.topRl:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(0);
                }
                break;
            case R.id.midCr:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(1);
                }
                break;
            case R.id.order01RL:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(2);
                }
            case R.id.order02RL:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(3);
                }
            case R.id.order03RL:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(4);
                }
            case R.id.order04RL:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(5);
                }
            case R.id.order05RL:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(6);
                }
            case R.id.exitBtn:
                if(headerVCallBack!=null) {
                    headerVCallBack.clickIdx(7);
                }
                break;
        }
    }
    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        List<Object> listStr = (List<Object>) cellDatas.get(childPosition);
        if (childPosition == 0) {
            holder.setBackgroundColor(R.id.lineV, Color.parseColor("#ff000000"));
        }
        holder.setText(R.id.nameTv,listStr.get(1).toString()).setImageResource(R.id.leftIMV, (int)listStr.get(0)).setImageResource(R.id.rightIMV, (int)listStr.get(2));
    }
    public void setMDatas(MineBean mineDatas) {
        this.mineDatas = mineDatas;
        Log.d(TAG, "setMDatas: "+mineDatas);
        notifyDataSetChanged();
    }
}

