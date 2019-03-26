package com.Market;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.Start.SmsLoginActivity;
import com.Tools.APIServices;
import com.Tools.AuthosUtils;
import com.Tools.SaveDatasUtils;
import com.Tools.ToastUtils;
import com.google.gson.Gson;
import com.gstore520.stone.gstore.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirmOrderActivity extends AppCompatActivity {

    public Context mContext;
    public LayoutInflater mInflater;
    private final String MINE_API = "user/detail";
    private static final String TAG = "GoodDetailActivity";
    private SaveDatasUtils obtainDatasUtils;
    private FirmOrderActivity.StHandler mHandler = new FirmOrderActivity.StHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        View v = mInflater.inflate(R.layout.activity_firm_order, null);
        obtainDatasUtils=new SaveDatasUtils(mContext, "fileStore");
        setContentView(v);
        ButterKnife.bind(this);
        initDatas();
    }
    private void initDatas(){
//        Intent getIntent = getIntent();
//        String sellID = getIntent.getStringExtra("presell_id");
//        Log.d(TAG, "initDatas: "+sellID);
        try {
            StringBuilder realPath = new StringBuilder();
            realPath.append(APIServices.API_PATH)
                    .append(MINE_API)
                    .append("?vendor_id=4");
            APIServices.sendGetRequest(realPath.toString(), "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdXRoX2lkIjoiMTU3MTc5MTQ1MDUiLCJsb2dpbl9hdXRoX2lkIjoxMjAsImlkIjo4OSwiYXV0aF90eXBlIjoxLCJ0aW1lc3RhbXAiOjE1MzMyNTY4OTcuODY4NDA4fQ.GBM0BAZlRuqWLiSP-NsE9BeX4CUIWHTbwYmet4dt70E", new Callback() {
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
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = responseStr;
                    mHandler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }


    }
    private static class StHandler extends Handler {
        private WeakReference<FirmOrderActivity> mReference;

        public StHandler(FirmOrderActivity activity) {
            mReference = new WeakReference<FirmOrderActivity>(activity);
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
                            Log.d(TAG, "handleMessage: 查询成功");
                        }else if(code == APIServices.CODE_TOKEN_EXPIRED){
                            AuthosUtils.missAuthos(mReference.get().mContext,message);
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
