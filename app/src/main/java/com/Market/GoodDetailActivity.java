package com.Market;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.Start.SmsLoginActivity;
import com.Tools.SaveDatasUtils;
import com.Tools.ToastUtils;
import com.gstore520.stone.gstore.R;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.backBtn)
    Button backBtn;
    public Context mContext;
    public LayoutInflater mInflater;
    private static final String TAG = "GoodDetailActivity";
    private SaveDatasUtils obtainDatasUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        View v = mInflater.inflate(R.layout.activity_good_detail, null);
        obtainDatasUtils=new SaveDatasUtils(mContext, "fileStore");
        setContentView(v);
        ButterKnife.bind(this);
        initView();
        initDatas();
    }
    private void initDatas(){
        Intent getIntent = getIntent();
        String sellID = getIntent.getStringExtra("presell_id");
        Log.d(TAG, "initDatas: "+sellID);
    }
    private void initView(){
        btn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                if(!Boolean.valueOf(String.valueOf(obtainDatasUtils.getObject("ifLogin")))){
                    Intent intent = new Intent(mContext,SmsLoginActivity.class);
                    intent.putExtra("status_code", "1");
                    startActivityForResult(intent, 0);
                }else{
                    Intent getIntent = getIntent();
                    String sellID = getIntent.getStringExtra("presell_id");
                    Intent intent = new Intent(mContext,FirmOrderActivity.class);
                    intent.putExtra("presell_id", sellID);
                    startActivity(intent);
                }
                break;
            case R.id.backBtn:
                finish();
                break;

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            String str = data.getStringExtra("status_code");
            Log.d(TAG, "onActivityResult: "+str);
        }
    }
}
