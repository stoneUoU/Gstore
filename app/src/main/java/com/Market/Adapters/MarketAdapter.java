package com.Market.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Market.Models.BrustBean;
import com.Market.Models.CarouselBean;
import com.Market.Models.PreSellBean;
import com.Tools.FormatTools;
import com.View.RoundImageView;
import com.bumptech.glide.Glide;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.gstore520.stone.gstore.R;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketAdapter extends GroupedRecyclerViewAdapter implements OnBannerListener{
    public List<BrustBean> brustDatas;
    public List<PreSellBean> preSellDatas;
    public List<CarouselBean> carouselDatas;
    private static final String TAG = "MarketAdapter";

    private LayoutInflater mInflater;
    private Context mContext;
    public Banner mBanner;
    public final int ITEM_PRESELL = 0;
    public final int ITEM_BRUST = 1;

    public final int HEADER_BANNER = 2;
    public final int HEADER_COMMON = 3;

    private List<String> bannerList;
    private List<String> bannerTitles;

    public CarouselCallBack carouselCallBack;//回调接口类

    public interface CarouselCallBack{
        public void clickIdx(int pos);
    }

    public void setCarouselCallBack(CarouselCallBack callBackListener) {
        this.carouselCallBack = callBackListener;
    }

    public MarketAdapter(Context context,List<BrustBean> brustDatas,List<PreSellBean> preSellDatas,List<CarouselBean> carouselDatas) {
        super(context);
        this.mContext = context;
        this.brustDatas = brustDatas;
        this.preSellDatas = preSellDatas;
        this.carouselDatas = carouselDatas;
        this.mInflater = LayoutInflater.from(mContext);
        initDatas();
    }

    private void initDatas(){
        bannerList = new ArrayList<>();
        bannerTitles = new ArrayList<>();
    }
    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosi) {
        if(groupPosi == 0){
            return preSellDatas.size();
        }else {
            return brustDatas.size();
        }
    }
    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        if(viewType == HEADER_BANNER){
            return R.layout.headerv_for_banner_market;
        }else{
            return R.layout.headerv_for_market;
        }
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.footerv_for_market;
    }

    @Override
    public int getChildLayout(int viewType) {
        if(viewType == ITEM_PRESELL){
            return R.layout.item_for_presell_market;
        }else{
            return R.layout.item_for_brust_market;
        }
    }
    @Override
    public int getHeaderViewType(int groupPosition){
        if(groupPosition == 0){
            return HEADER_BANNER;
        }else{
            return HEADER_COMMON;
        }
    }
    @Override
    public int getChildViewType(int groupPosition, int childPosition){
        if(groupPosition == 0){
            return ITEM_PRESELL;
        }else{
            return ITEM_BRUST;
        }
    }
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        if(groupPosition == 0){
            mBanner = holder.get(R.id.mBanner);
            //设置banner样式
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图片加载器
            mBanner.setImageLoader(new GlideImageLoader());
            //设置指示器位置（当banner模式中有指示器时）
            mBanner.setIndicatorGravity(BannerConfig.RIGHT);
            mBanner.setOnBannerListener(this);
            mBanner.update(bannerList,bannerTitles);
            holder.setText(R.id.headerTv, "预售清单");
        }else{
            holder.setText(R.id.headerTv, "当日爆款");
        }
    }
    @Override
    public void OnBannerClick(int position) {
        if(carouselCallBack!=null) {
            carouselCallBack.clickIdx(position);
        }
    }
    //图片加载：
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
            //用fresco加载图片简单用法，记得要写下面的createImageView方法
            Uri uri = Uri.parse((String) path);
            imageView.setImageURI(uri);
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        holder.setText(R.id.emptyTV, "我是尾视图（headerView）");
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        final BrustBean brustItem;
        final PreSellBean preSellItem;
        if(groupPosition == 0){
            preSellItem = preSellDatas.get(childPosition);
            //FormatTools
            holder.setText(R.id.nameTv, preSellItem.getProduct_name()).setText(R.id.endTimeTv, preSellItem.getEnd_time()).setText(R.id.priceTv, "￥ "+ FormatTools.formatInt(preSellItem.getReal_price()));
            ImageView IMV = (RoundImageView) holder.get(R.id.imageView);
            Glide.with(mContext)
                    .load("https://pic.shop.znrmny.com/static/ggshop/v5"+preSellItem.getPic_path())
                    .into(IMV);
        }else{
            brustItem = brustDatas.get(childPosition);
            holder.setText(R.id.nameTv, brustItem.getProduct_name()).setText(R.id.priceTv, "￥ "+FormatTools.formatInt(brustItem.getReal_price()));
            ImageView IMV = (RoundImageView) holder.get(R.id.imageView);
            Glide.with(mContext)
                    .load("https://pic.shop.znrmny.com/static/ggshop/v5"+brustItem.getPic_path())
                    .into(IMV);
        }
    }
    public void setMDatas(List<PreSellBean> preSellDatas,List<BrustBean> brustDatas,List<CarouselBean> carouselDatas) {
        this.carouselDatas = carouselDatas;
        this.brustDatas = brustDatas;
        this.preSellDatas = preSellDatas;
        notifyDataSetChanged();
        bannerList.removeAll(bannerList);
        bannerTitles.removeAll(bannerTitles);
        for(int i=0;i<carouselDatas.size();i++){
            CarouselBean carouselBean = carouselDatas.get(i);
            bannerList.add("https://pic.shop.znrmny.com/static/ggshop/v5"+carouselBean.getPic_path());
        }
        for(int i=0;i<carouselDatas.size();i++){
            CarouselBean carouselBean = carouselDatas.get(i);
            bannerTitles.add(carouselBean.getTitle());
        }
    }
}

