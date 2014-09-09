package io.rong.imkit.demo;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imlib.RongIMClient;

/**
 * Created by zhjchen on 14-4-16.
 */

public class GroupListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private GroupListAdapter mGroupListAdapter;

    @Override
    protected int setContentViewResId() {
        return R.layout.rc_activity_group_list;
    }

    @Override
    protected void initView() {
        mListView = getViewById(android.R.id.list);
    }

    @Override
    protected void initData() {

        HashMap<String, RongIMClient.Group> groupMap = DemoContext.getInstance().getGroupMap();

        ArrayList<RongIMClient.Group> groups=new ArrayList<RongIMClient.Group>();

        for (String groupId : groupMap.keySet()) {
            groups.add(groupMap.get(groupId));
        }

        mGroupListAdapter = new GroupListAdapter(this, groups);
        mListView.setAdapter(mGroupListAdapter);
        mGroupListAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        RongIMClient.Group group=mGroupListAdapter.getItem(position);


        Uri uri = Uri.parse("rong://" + this.getPackageName()).buildUpon()
                .appendPath("conversation").appendPath(RongIMClient.ConversationType.GROUP.getName().toLowerCase())
                .appendQueryParameter("targetId", group.getId()).appendQueryParameter("title", group.getName()).build();


        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }
}
