package com.Tools;

import android.content.Context;
import android.content.Intent;

import com.Start.SmsLoginActivity;
import com.Start.TabBarActivity;

public class AuthosUtils {

    public static void missAuthos(Context context,String msg){
        SaveDatasUtils obtainDatasUtils=new SaveDatasUtils(context, "fileStore");
        ToastUtils.showShortToast(context,msg);
        Intent intent = new Intent(context,SmsLoginActivity.class);
        intent.putExtra("status_code", "0");
        obtainDatasUtils.setObject("ifLogin",false);
        obtainDatasUtils.setObject("authos","");
        context.startActivity(intent);
    }

    public static void toTabBarAct(Context context){
        Intent intent= new Intent(context, TabBarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
