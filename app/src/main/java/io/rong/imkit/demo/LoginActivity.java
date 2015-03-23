package io.rong.imkit.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sea_monster.core.exception.BaseException;
import com.sea_monster.core.exception.InternalException;
import com.sea_monster.core.network.AbstractHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.demo.model.User;
import io.rong.imkit.demo.ui.LoadingDialog;
import io.rong.imkit.demo.ui.WinToast;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.UserInfo;

public class LoginActivity extends BaseApiActivity implements OnClickListener, Callback {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private Button mRegisterBtn;
    private Button mLoginBtn;

    private String mDeviceId;
    private LoadingDialog mDialog;

    private TextView mVersionTextView;
    private TextView mBuildTextView;

    private Handler mHandler;
    private static final int HANDLER_LOGIN_SUCCESS = 1;
    private static final int HANDLER_LOGIN_FAILURE = 2;
    private static final int REQUEST_CODE_REGISTER = 2001;

    public static final String INTENT_EMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";

    private AbstractHttpRequest<User> loginHttpRequest;
    private AbstractHttpRequest<ArrayList<User>> getFriendsHttpRequest;

    private boolean mIsLoginSuccess = false;

    private String TOKEN="ABiJiw9kjm5JNKBqOueR1kmcbyeYIrXSDa0nFvL2mH8ZQAacEOnpq4ALGp+0d44QDl1Qq02s3vO1S9Uvmgoceg==";
    public static String mUserID =null;
    @Override
    protected int setContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("SplashScreen"); //统计页面
//        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void initView() {

        mUserNameEditText = getViewById(android.R.id.text1);
        mPasswordEditText = getViewById(android.R.id.text2);
        mRegisterBtn = getViewById(android.R.id.button1);
        mLoginBtn = getViewById(android.R.id.button2);
        mVersionTextView = getViewById(R.id.version_code);
        mBuildTextView = getViewById(R.id.build_code);

    }

    @Override
    protected void initData() {
        if(DemoContext.getInstance()!=null) {
            String email = DemoContext.getInstance().getSharedPreferences().getString(INTENT_EMAIL, "");
            String password = DemoContext.getInstance().getSharedPreferences().getString(INTENT_PASSWORD, "");
            mUserNameEditText.setText(email);
            mPasswordEditText.setText(password);
        }
        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mDeviceId = mTelephonyManager.getDeviceId();

        mDialog = new LoadingDialog(this);
        mHandler = new Handler(this);

        String[] versionInfo = getVersionInfo();
        mBuildTextView.setText(String.format(getResources().getString(R.string.login_version_date), versionInfo[0]));
        mVersionTextView.setText(String.format(getResources().getString(R.string.login_version_code), versionInfo[1]));

        String token = DemoContext.getInstance().getSharedPreferences().getString("LOGIN_TOKEN",null);
        Log.e("LoginActivity", "---------userId token---------:" + token);
//        if(!TextUtils.isEmpty(token)){
//            try {
//            if (mDialog != null && !mDialog.isShowing())
//                mDialog.show();
//                RongIM.connect(token, new ConnectCallback() {
//                    @Override
//                    public void onSuccess(String userId) {
//
//                        Log.e("LoginActivity", "---------userId---------:" + userId);
//
//                        mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS).sendToTarget();
//                        mIsLoginSuccess = true;
//                        mUserID = userId;
//                        RongCloudEvent.getInstance().setOtherListener();
//
//                        if (DemoContext.getInstance() != null) {
//                            User user = new User();
//                            getFriendsHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(user.getCookie(), LoginActivity.this);
//                            DemoContext.getInstance().setCurrentUser(user);
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ErrorCode errorCode) {
//                        Log.e("LoginActivity", "---------errorCode---------:" + errorCode);
//                        mHandler.obtainMessage(HANDLER_LOGIN_FAILURE).sendToTarget();
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {

        //打开注册页面
        if (v == mRegisterBtn) {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REGISTER);
        } else if (v == mLoginBtn) {
            String username = mUserNameEditText.getEditableText().toString();
            String password = mPasswordEditText.getEditableText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                WinToast.toast(LoginActivity.this, R.string.login_erro_is_null);
                return;
            }

            if (mDialog != null && !mDialog.isShowing())
                mDialog.show();

            //发起登录 http请求 (注：非融云SDK接口，是demo接口)
            if(DemoContext.getInstance()!=null)
                loginHttpRequest = DemoContext.getInstance().getDemoApi().login(username, password, mDeviceId, this);
        }

    }

    private void httpLoginSuccess(final User user, boolean isFirst) {

        if (isFirst) {
            Gson gson = new Gson();
            String userJson = gson.toJson(user);

            DemoContext.getInstance().getSharedPreferences().edit().putString("LONGIN_USER", userJson).commit();
        }
        Log.d("LoginActivity", "--------- onSuccess userId getToken----------:" + user.getToken());

        /**
         * IMKit SDK调用第二步
         *
         * 建立与服务器的连接
         *
         * 详见API
         * http://docs.rongcloud.cn/api/android/imkit/index.html
         */
        try {
            if ("io.rong.imkit.demo".equals(getCurProcessName(getApplicationContext()))) {
                RongIM.connect(user.getToken(), new ConnectCallback() {

                    @Override
                    public void onSuccess(String userId) {
                        Log.e("LoginActivity", "--------- onSuccess userId----------:" + userId);

                        mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS).sendToTarget();
                        mIsLoginSuccess = true;
                        mUserID = userId;
                        RongCloudEvent.getInstance().setOtherListener();

                        Editor editor = DemoContext.getInstance().getSharedPreferences().edit();
                        editor.putString("LOGIN_TOKEN", user.getToken());
                        editor.commit();
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
//                            mHandler.obtainMessage(HANDLER_LOGIN_FAILURE).sendToTarget();
                        Log.d("LoginActivity", "---------onError ----------:" + errorCode);
                        mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS).sendToTarget();
                        LoginActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                WinToast.toast(LoginActivity.this, R.string.connect_fail);
                            }
                        });
                    }

                });
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DemoContext.getInstance() != null) {
            Editor editor = DemoContext.getInstance().getSharedPreferences().edit();
            editor.putString(INTENT_PASSWORD, mPasswordEditText.getText().toString());
            editor.putString(INTENT_EMAIL, mUserNameEditText.getText().toString());
            editor.commit();
        }


        //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
        if (DemoContext.getInstance() != null) {
            getFriendsHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(user.getCookie(), this);
            DemoContext.getInstance().setCurrentUser(user);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

        //登录成功  返回数据
        if (loginHttpRequest == request) {

            if (obj instanceof User) {

                final User user = (User) obj;
                httpLoginSuccess(user, true);
            }
            //获取好友列表接口  返回好友数据  (注：非融云SDK接口，是demo接口)
        } else if (getFriendsHttpRequest == request) {

            @SuppressWarnings("unchecked")
            final ArrayList<UserInfo> friends = (ArrayList<UserInfo>) getFriends((ArrayList<User>) obj);
            if(DemoContext.getInstance()!=null)
                DemoContext.getInstance().setFriends(friends);
        }

    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        Log.e("TAG","-------------request:---------");
        if (loginHttpRequest == request) {
            if (e instanceof InternalException) {

                InternalException ie = (InternalException) e;
                Log.e("TAG","-------------getCode:---------"+ie.getCode());
                if (ie.getCode() == 401) {
                    WinToast.toast(this,R.string.login_pass_error);
                }else if(ie.getCode() == 403){
                    WinToast.toast(this, R.string.login_username_error);
                }

                if (mDialog != null)
                    mDialog.dismiss();
            }
        }

    }
    /**
     * 把自己系统中的用户对象转换成融云中的用户对象
     *
     * @param users
     * @return
     */
    public List<UserInfo> getFriends(ArrayList<User> users) {

        if (users == null)
            return null;

        ArrayList<UserInfo> friends = new ArrayList<UserInfo>();

        for (User user : users) {
            UserInfo info = new UserInfo(String.valueOf(user.getId()), user.getUsername(), user.getPortrait());
            friends.add(info);
        }

        return friends;
    }


    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == HANDLER_LOGIN_FAILURE) {
            WinToast.toast(LoginActivity.this, R.string.login_failure);

            if (mDialog != null)
                mDialog.dismiss();

        } else if (msg.what == HANDLER_LOGIN_SUCCESS) {
            WinToast.toast(LoginActivity.this, R.string.login_success);

            if (mDialog != null)
                mDialog.dismiss();

            startActivity(new Intent(this, MainActivity.class));

            initGroupInfo();
        }
        return false;
    }

    private void initGroupInfo() {

        if(DemoContext.getInstance()!=null) {
            HashMap<String, RongIMClient.Group> groupM = DemoContext.getInstance().getGroupMap();

            List<RongIMClient.Group> groups = new ArrayList<RongIMClient.Group>();

            Iterator iterator = groupM.values().iterator();

            while (iterator.hasNext()) {
                groups.add((RongIMClient.Group) iterator.next());
            }

            if (RongIM.getInstance() != null) {
                RongIM.getInstance().syncGroup(groups, new RongIM.OperationCallback() {

                    @Override
                    public void onSuccess() {
                        Log.e("syncGroup", "=============syncGroup====onSuccess===========");
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        Log.e("syncGroup", "=============syncGroup====onError===========" + errorCode);
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mIsLoginSuccess)
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                mUserNameEditText.setText(data.getStringExtra(INTENT_EMAIL));
                mPasswordEditText.setText(data.getStringExtra(INTENT_PASSWORD));
            }
        }
    }


    private String[] getVersionInfo() {
        String[] version = new String[2];

        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version[0] = String.valueOf(packageInfo.versionCode);
            version[1] = packageInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}