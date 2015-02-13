package io.rong.imkit.demo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import io.rong.imkit.RongIM;
import io.rong.imkit.demo.ui.WinToast;
import io.rong.imkit.view.ActionBar;
import io.rong.imlib.RongIMClient;
/**
 * Created by bob on 2015/2/12.
 */
public class DemoNotificationCycleSettingActivity extends BaseActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener {
    private static final  String TAG = DemoNotificationCycleSettingActivity.class.getSimpleName();
    /**
     * 当前状态
     */
    private TextView mStatusTv;
    /**
     * 设置push
     */
    private Button mAddButton;
    private Button mRemoveButton;
    private Button mQueryButton;
    private TimePicker mFromTimePicker;
    private TimePicker mToTimerPicker;
    ActionBar mActionBar;
    String time ;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    mStatusTv.setText(R.string.de_notification_status_no);

                    break;
                case 2:
                    mStatusTv.setText("当前设置push屏蔽开始时间为：" + msg.obj.toString());

                    break;

            }
        }
    };
    @Override
    protected int setContentViewResId() {
        return R.layout.de_notification_setting;
    }

    @Override
    protected void initView() {
        mActionBar = (ActionBar)findViewById(R.id.action_bar);
        mStatusTv = (TextView) findViewById(R.id.de_status);
        mAddButton = (Button) findViewById(R.id.de_add_notification);
        mRemoveButton = (Button) findViewById(R.id.de_remove_notification);
        mQueryButton = (Button) findViewById(R.id.de_quary_notification);
        mFromTimePicker = (TimePicker) findViewById(R.id.de_from_timePicker);
        mToTimerPicker = (TimePicker) findViewById(R.id.de_to_timePicker);
    }

    @Override
    protected void initData() {

        mActionBar.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActionBar.getTitleTextView().setText("push消息设置");
        mFromTimePicker.setIs24HourView(true);
        mToTimerPicker.setIs24HourView(true);
        mAddButton.setOnClickListener(this);
        mRemoveButton.setOnClickListener(this);
        mQueryButton.setOnClickListener(this);
        mFromTimePicker.setOnTimeChangedListener(this);
//        mToTimerPicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        switch (view.getId()){
            case R.id.de_from_timePicker:

                time = hourOfDay+":"+minute +":"+"00";
                mStatusTv.setText("你选择的时间是: " +time);

                break;
            case R.id.de_to_timePicker:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.de_add_notification:

                if(time != null) {
                    RongIM.getInstance().getRongIMClient().setConversationNotificationCycle(time, 5, new RongIMClient.SetConversationNotificationCycleCallback() {
                        @Override
                        public void onSuccess() {
                            Log.e(TAG, "----yb----设置会话通知周期-onSuccess");
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {
                            Log.e(TAG, "----yb----设置会话通知周期-oonError:" + errorCode.getValue());
                        }
                    });
                }else{
                    WinToast.toast(DemoNotificationCycleSettingActivity.this,"请先设置时间");
                }



                break;
            case R.id.de_remove_notification:

                if(RongIM.getInstance() != null)
                RongIM.getInstance().getRongIMClient().removeConversationNotificationCycle(new RongIMClient.RemoveConversationNotificationCycleCallback() {


                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "----yb----移除会话通知周期-onSuccess");
                        Message msg = Message.obtain();
                        msg.what = 1;
                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        Log.e(TAG,"----yb-----移除会话通知周期-oonError:"+errorCode.getValue());
                    }
                });

                break;
            case R.id.de_quary_notification:

                if(RongIM.getInstance() != null)
                RongIM.getInstance().getRongIMClient().getConversationNotificationCycle(new RongIMClient.GetConversationNotificationCycleCallback() {
                    @Override
                    public void onSuccess(String startTime, int spanMins) {
                        Log.e(TAG,"----yb----获取会话通知周期-onSuccess起始时间startTime:"+startTime+",间隔分钟数spanMins:"+spanMins);
                        Message msg = Message.obtain();
                        if("".equals(startTime)){
                            msg.what = 1;
                        }else{
                            msg.what = 2;
                            msg.obj = startTime;
                        }
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        Log.e(TAG,"----yb----获取会话通知周期-oonError:"+errorCode.getValue());
                    }
                });

                break;
        }
    }


}
