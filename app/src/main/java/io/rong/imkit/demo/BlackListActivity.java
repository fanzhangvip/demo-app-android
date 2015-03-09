package io.rong.imkit.demo;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.demo.ui.WinToast;
import io.rong.imkit.utils.Util;
import io.rong.imkit.view.ActionBar;
import io.rong.imkit.view.SelectDialog;
import io.rong.imlib.RongIMClient;

/**
 * Created by bob on 15-1-7.
 */
public class BlackListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private String TAG = "BlackListActivity";
    ActionBar mActionBar;
    private ListView mBlackList;
    private BlackListAdapter mBlackListAdapter;
    private List<RongIMClient.UserInfo> mUserInfoList = null;

    @Override
    protected int setContentViewResId() {
        return R.layout.black_list;
    }

    @Override
    protected void initView() {
        mActionBar = (ActionBar) findViewById(android.R.id.custom);
        mBlackList = (ListView) findViewById(R.id.black_list);
        mActionBar.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActionBar.getTitleTextView().setText("黑名单列表");
    }

    @Override
    protected void initData() {

        if (RongIM.getInstance() != null && Util.getNetWorkType(this) != -1) {


            RongIM.getInstance().getBlacklist(
                    new RongIM.GetBlacklistCallback() {

                        @Override
                        public void onError(ErrorCode errorCode) {

                            Log.e(TAG,
                                    "-------getBlacklist onError--------:"
                                            + errorCode.getMessage());
                        }

                        @Override
                        public void onSuccess(String[] userIds) {
                            Log.e(TAG,
                                    "-------getBlacklist onSuccess--------:");
                            if (DemoContext.getInstance() != null) {
                                mUserInfoList = DemoContext.getInstance().getUserInfoByIds(userIds);
                                mBlackListAdapter = new BlackListAdapter(
                                        BlackListActivity.this, mUserInfoList);
                                mBlackList.setAdapter(mBlackListAdapter);
                                mBlackListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        } else {
            WinToast.toast(this, R.string.network_not);
        }
        mBlackList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final String userId = mUserInfoList.get(position).getUserId();
        String titleName = mUserInfoList.get(position).getName();
        final SelectDialog selectDialog = new SelectDialog(this);
        selectDialog.setTitle(titleName, true);
        selectDialog.setFristLineContent("delete_from_balck");
        selectDialog.setSecondLineContent("cancle_black");
        if (Util.getNetWorkType(BlackListActivity.this) != -1) {
            selectDialog.setOnDialogItemViewListener(new SelectDialog.OnDialogItemViewListener() {
                @Override
                public void OnDialogItemViewClick(View view, int position) {
                    if (position == 0) {
                        if (RongIM.getInstance() != null) {
                            RongIM.getInstance().removeFromBlacklist(userId,
                                    new RongIM.OperationCallback() {

                                        @Override
                                        public void onError(ErrorCode errorCode) {
                                            Log.e(TAG,
                                                    "------- removeFromBlacklist onError-------:"
                                                            + errorCode);
                                        }

                                        @Override
                                        public void onSuccess() {
                                            Log.e(TAG, "-------被移除黑名单-------:");
                                            initData();
                                        }
                                    });
                        }
                    } else if (position == 1) {

                    }
                    selectDialog.dismiss();
                }
            });
            selectDialog.show();
        } else {
            WinToast.toast(this, R.string.network_not);
        }
    }
}
