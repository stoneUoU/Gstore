package com.Start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.Tools.APIServices;
import com.Tools.AuthosUtils;
import com.Tools.MD5;
import com.Tools.RexUtils;
import com.Tools.SaveDatasUtils;
import com.Tools.ToastUtils;
import com.View.TimerBtn;
import com.gstore520.stone.gstore.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SmsLoginActivity extends AppCompatActivity implements View.OnClickListener{


    private final String SMS_CODEAPI = "user/send_sms";
    private final String SMS_LOGINAPI = "user/sms_login";
    private final String CODE_LOGINAPI = "user/login";
    public SaveDatasUtils obtainDatasUtils;

    //关闭按钮
    @BindView(R.id.closeIMV)
    ImageView closeIMV;
    //密码登录
    @BindView(R.id.codeLoginBtn)
    Button codeLoginBtn;
    //验证码按钮
    @BindView(R.id.smsBtn)
    TimerBtn smsBtn;
    //密码登录
    @BindView(R.id.smsLoginBtn)
    Button smsLoginBtn;

    @BindView(R.id.user_name)
    EditText user_name;

    @BindView(R.id.sms_code)
    EditText sms_code;

    public Context mContext;

    private static final String TAG = "SmsLoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_login);
        ButterKnife.bind(this);

        mContext = this;

        closeIMV.setOnClickListener(this);
        codeLoginBtn.setOnClickListener(this);
        smsBtn.setOnClickListener(this);
        smsLoginBtn.setOnClickListener(this);

        obtainDatasUtils=new SaveDatasUtils(this, "fileStore");

        initDatas();
    }

    private void initDatas(){
        Intent getIntent = getIntent();
        String mName = getIntent.getStringExtra("status_code");
        Log.d(TAG, "initDatas: "+mName);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.closeIMV:
                finishSelf();
                break;
            case R.id.codeLoginBtn:
                System.out.println("密码登录");
                break;
            case R.id.smsBtn:
                submit_CR(user_name.getText().toString());
                break;
            case R.id.smsLoginBtn:
                codeLogin();
                //submit_LR(user_name.getText().toString(),sms_code.getText().toString());
                break;
        }
    }
    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }
    public void submit_CR(String user_name){
        if(!RexUtils.checkPhone(user_name)){
            ToastUtils.showShortToast(SmsLoginActivity.this, "手机号不合理");
        }else{
            try {
                String realPath = APIServices.API_PATH + SMS_CODEAPI;
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("phone", user_name);
                jsonRequest.put("code_type", "login");
                APIServices.sendPostRequest(realPath, null, jsonRequest, new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("", "onFailure" + e.getMessage());
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        JSONObject responseJSON = null;
                        try {
                            responseJSON = new JSONObject(responseStr);
                            String code = responseJSON.getString("code");
                            if(code.equals("0")){
                                System.out.println(responseStr);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        smsBtn.start();
                                        ToastUtils.showShortToast(SmsLoginActivity.this, "验证码发送成功");
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    public void submit_LR(String user_name, String sms_code){
        if(!RexUtils.checkPhone(user_name)){
            ToastUtils.showShortToast(SmsLoginActivity.this, "手机号不合理");
        }else if(!RexUtils.checkSmsCode(sms_code)){
            ToastUtils.showShortToast(SmsLoginActivity.this, "验证码位数不对");
        }else{
            try {
                String realPath = APIServices.API_PATH + SMS_LOGINAPI;
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("phone", user_name);
                jsonRequest.put("sms_code", sms_code);
                APIServices.sendPostRequest(realPath, null, jsonRequest, new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("", "onFailure" + e.getMessage());
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        JSONObject responseJSON = null;
                        try {
                            responseJSON = new JSONObject(responseStr);
                            String code = responseJSON.getString("code");
                            if(code.equals("0")){
                                System.out.println(responseStr);
                                JSONObject data = responseJSON.getJSONObject("data");
                                final String authos = data.getString("token");
                                obtainDatasUtils.setObject("ifLogin",true);
                                obtainDatasUtils.setObject("authos",authos);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent();
                                        intent.setClass(SmsLoginActivity.this, TabBarActivity.class);//从哪里跳到哪里
                                        SmsLoginActivity.this.startActivity(intent);
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void codeLogin(){
        try {
            String realPath = APIServices.API_PATH + CODE_LOGINAPI;
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("phone", "15717914505");
            jsonRequest.put("password", MD5.md5(MD5.md5(MD5.md5("666666"))));
            APIServices.sendPostRequest(realPath, null, jsonRequest, new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("", "onFailure" + e.getMessage());
                        }
                    });
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    JSONObject responseJSON = null;
                    try {
                        responseJSON = new JSONObject(responseStr);
                        String code = responseJSON.getString("code");
                        if(code.equals("0")){
                            System.out.println(responseStr);
                            JSONObject data = responseJSON.getJSONObject("data");
                            final String authos = data.getString("token");
                            obtainDatasUtils.setObject("ifLogin",true);
                            obtainDatasUtils.setObject("authos",authos);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finishSelf();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return isCustumerBackKey();
        }
        return false;
    }

    private boolean isCustumerBackKey() {
        // 这儿做返回键的控制，如果自己处理返回键逻辑就返回true，如果返回false,代表继续向下传递back事件，由系统去控制
        Intent getIntent = getIntent();
        if(!getIntent.getStringExtra("status_code").equals ("0")){
            //设置返回的数据
            Intent intent = new Intent();
            intent.putExtra("status_code", getIntent.getStringExtra("status_code"));
            setResult(0, intent);
            finish();
            return true;
        }else{
            Intent intent= new Intent(mContext, TabBarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
    }

    private void finishSelf(){
        Intent getIntent = getIntent();
        if(!getIntent.getStringExtra("status_code").equals ("0")){
            //设置返回的数据
            Intent intent = new Intent();
            intent.putExtra("status_code", getIntent.getStringExtra("status_code"));
            setResult(0, intent);
            finish();
        }else{
            AuthosUtils.toTabBarAct(mContext);
        }
    }
}
