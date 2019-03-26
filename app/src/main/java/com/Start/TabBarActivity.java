package com.Start;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.Market.MarketFragment;
import com.Mine.MineFragment;
import com.ShopCart.ShopCartFragment;
import com.Tools.SaveDatasUtils;
import com.Tools.StatusBarTools;
import com.Tools.ToastUtils;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.gstore520.stone.gstore.R;

public class TabBarActivity extends AppCompatActivity {

    //从上一个界面传过来的字典：
    // 定一个值：登录失效是:status_code:0
    // 在主页消息、主页立即购买、商品详情界面登录:status_code:1
    // 进app直接点购物车tab登录:status_code:2
    // 进app直接点我的tab登录:status_code:3
    // 退出登录：status_code:4

    private MarketFragment marketFragment;
    private MineFragment mineFragment;
    private ShopCartFragment shopCartFragment;
    private Fragment[] mFragments;
    private BottomNavigationBar mBottomNavigationBar;
    private TextBadgeItem mTextBadgeItem;
    private ShapeBadgeItem mShapeBadgeItem;
    private int index;//点击的fragment的下标
    private int currentTabIndex=0;//当前的fragment的下标
    public Context mContext;
    public SaveDatasUtils obtainDatasUtils;
    private static final String TAG = "TabBarActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar);
        initView();//实例化View
        initBottomNavigationBar();//BottomNavigationBar控制底部导航栏的实现
        initListener();//点击事件
        //StatusBarTools.setStatusBarColor(this,R.color.colorWhite);
        mContext = this;
        obtainDatasUtils=new SaveDatasUtils(this, "fileStore");
        //obtainDatasUtils.setObject("ifLogin",false);
        //initDatas();
    }

//    private void initDatas(){
//        Intent getIntent = getIntent();
//        String mName = getIntent.getStringExtra("status_code");
//        if(mName != null){
//            Log.d(TAG, "initDatas: "+mName);
//            switch (mName){
//                case "2":
//                    mBottomNavigationBar.selectTab(1);
//                    break;
//                case "3":
//                    mBottomNavigationBar.selectTab(2);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    }

    private void initListener() {
        mBottomNavigationBar //设置lab点击事件
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        switch (position){
                            case 0:
                                index = 0;
                                break;
                            case 1:
                                index = 1;
                                break;
                            case 2:
                                index = 2;
                                break;
                        }
                        if (index == 1) {
                            if(!Boolean.valueOf(String.valueOf(obtainDatasUtils.getObject("ifLogin")))){
                                Intent intent = new Intent(mContext,SmsLoginActivity.class);
                                intent.putExtra("status_code", "2");
                                startActivityForResult(intent, 0);
                                return;
                            }
                        }
                        if (index == 2) {
                            if(!Boolean.valueOf(String.valueOf(obtainDatasUtils.getObject("ifLogin")))){
                                Intent intent = new Intent(mContext,SmsLoginActivity.class);
                                intent.putExtra("status_code", "3");
                                startActivityForResult(intent, 0);
                                return;
                            }
                        }
                        if (currentTabIndex != index) {
                            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                            trx.hide(mFragments[currentTabIndex]);
                            if (!mFragments[index].isAdded()) {
                                trx.add(R.id.tabBar_fremeLayout, mFragments[index]);
                            }
                            trx.show(mFragments[index]).commit();
                        }
                        currentTabIndex = index;
                    }
                    @Override
                    public void onTabUnselected(int position) {

                    }
                    @Override
                    public void onTabReselected(int position) {
                        ToastUtils.showShortToast(mContext, "onTabReselected"+position);
                    }
                });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            String str = data.getStringExtra("status_code");
            if(!Boolean.valueOf(String.valueOf(obtainDatasUtils.getObject("ifLogin")))){
                mBottomNavigationBar.selectTab(0);
            }else{
                if(str != null){
                    switch (str){
                        case "2":
                            mBottomNavigationBar.selectTab(1);
                            break;
                        case "3":
                            mBottomNavigationBar.selectTab(2);
                            break;
                        default:
                            mBottomNavigationBar.selectTab(0);
                            break;
                    }

                }
            }
        }

    }
    private void initBottomNavigationBar() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor("#ff0000");//选中颜色 图标和文字
//                .setInActiveColor("#8e8e8e")//默认未选择颜色
//                .setBarBackgroundColor(R.color.white);//默认背景色

        mBottomNavigationBar
                .addItem(new BottomNavigationItem(getResources().getDrawable(R.mipmap.mine_icon_store_click), "商城")
                        .setInactiveIcon(ContextCompat.getDrawable(TabBarActivity.this, R.mipmap.mine_icon_store_onclick))
                        .setBadgeItem(mShapeBadgeItem))
                .addItem(new BottomNavigationItem(getResources().getDrawable(R.mipmap.icon_shoppingcar_click), "购物车")
                        .setInactiveIcon(ContextCompat.getDrawable(TabBarActivity.this, R.mipmap.icon_shoppingcar_onclick))
                        .setBadgeItem(mTextBadgeItem))
                .addItem(new BottomNavigationItem(getResources().getDrawable(R.mipmap.mine_icon_mine_click), "我的")
                        .setInactiveIcon(ContextCompat.getDrawable(TabBarActivity.this, R.mipmap.mine_icon_mine_onclick)))
                .setFirstSelectedPosition(0)//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成
    }

    private void initView() {

        marketFragment=new MarketFragment();
        shopCartFragment=new ShopCartFragment();
        mineFragment=new MineFragment();

        mBottomNavigationBar= (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        mFragments=new Fragment[]{marketFragment,shopCartFragment,mineFragment};

        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.add(R.id.tabBar_fremeLayout, mFragments[0]);
        trx.show(mFragments[0]).commit();
    }
//    @Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - oldBackTime > 2000) {
//            oldBackTime = System.currentTimeMillis(); Toast.makeText(this, R.string.back, Toast.LENGTH_SHORT).show();
//        } else {
//            finish();
//        }
//    }
}

