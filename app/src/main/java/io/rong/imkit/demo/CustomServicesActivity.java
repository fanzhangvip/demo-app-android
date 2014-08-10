package io.rong.imkit.demo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sea_monster.core.exception.BaseException;
import com.sea_monster.core.network.AbstractHttpRequest;
import com.sea_monster.core.network.ApiCallback;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imkit.demo.model.CustomerService;
import io.rong.imkit.demo.ui.LoadingDialog;
import io.rong.imkit.veiw.ActionBar;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhjchen on 14-8-5.
 */
public class CustomServicesActivity extends BaseActivity implements Handler.Callback,AdapterView.OnItemClickListener {

    private final static int HANDLE_GET_CUSTOM_SERVICE_LIST_SUCCESS = 1001;
    private final static int HANDLE_GET_CUSTOM_SERVICE_LIST_FAILURE = 1002;

    private ListView mListView;
    private ActionBar actionBar;
    private CustomServiceAdapter mCustomServiceAdapter;
    private Handler mHandler;
    LoadingDialog mLoadingDialog;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_custom_service;
    }

    @Override
    protected void initView() {
        mListView = getViewById(android.R.id.list);
        actionBar = getViewById(R.id.action_bar);
    }

    @Override
    protected void initData() {

        mHandler=new Handler(this);

        mCustomServiceAdapter = new CustomServiceAdapter(this);
        mListView.setAdapter(mCustomServiceAdapter);
        mListView.setOnItemClickListener(this);

        actionBar.getTitleTextView().setText(R.string.custom_service_people_list);
        actionBar.setOnBackClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();

        DemoContext.getInstance().getDemoApi().getCustomServiceList(DemoApplication.APP_KEY, new ApiCallback<ArrayList<CustomerService>>() {
            @Override
            public void onComplete(AbstractHttpRequest<ArrayList<CustomerService>> arrayListAbstractHttpRequest, ArrayList<CustomerService> customerServices) {
                mHandler.obtainMessage(HANDLE_GET_CUSTOM_SERVICE_LIST_SUCCESS, customerServices).sendToTarget();
            }

            @Override
            public void onFailure(AbstractHttpRequest<ArrayList<CustomerService>> arrayListAbstractHttpRequest, BaseException e) {
                e.printStackTrace();
                mHandler.obtainMessage(HANDLE_GET_CUSTOM_SERVICE_LIST_FAILURE, e).sendToTarget();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case HANDLE_GET_CUSTOM_SERVICE_LIST_SUCCESS:

                ArrayList<CustomerService> customerServices = (ArrayList<CustomerService>) msg.obj;
                mCustomServiceAdapter.addData(customerServices);
                mCustomServiceAdapter.notifyDataSetChanged();

                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }

                break;
            case HANDLE_GET_CUSTOM_SERVICE_LIST_FAILURE:
                Toast.makeText(this, R.string.custom_service_get_list_failure, Toast.LENGTH_SHORT).show();

                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                break;
            default:
                break;
        }


        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CustomerService customerService=mCustomServiceAdapter.getItem(position);

        if(customerService==null ||customerService.getUserId()==null){
            return;
        }

        /**
         * 打开客服聊天页面
         *
         * API详见 http://docs.rongcloud.cn/android.html
         */
        RongIM.getInstance().startCustomerServiceChat(this, customerService.getUserId(), customerService.getGlobalNick(), new RongIM.OnConversationStartedListener() {
            @Override
            public void onCreated(RongIMClient.ConversationType conversationType, String targetId) {
                Log.d("FunctioanListActivity", "----startCustomerServiceChat----onCreated--------");

            }

            @Override
            public void onDestroyed() {
                Log.d("FunctioanListActivity", "----startCustomerServiceChat----onDestroyed--------");
            }

            @Override
            public void onClickUserPortrait(RongIMClient.UserInfo user) {
                Log.d("FunctioanListActivity", "----startCustomerServiceChat----onClickUserPortrait--------");
            }

            @Override
            public void onClickMessage(RongIMClient.Message message) {
                Log.d("FunctioanListActivity", "----startCustomerServiceChat----onClickMessage--------");
            }
        });
    }
}
