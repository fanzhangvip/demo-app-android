package io.rong.imkit.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.rong.imlib.RongIMClient;

public class BlackListAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
    private List<RongIMClient.UserInfo> mUserInfoList;

    public BlackListAdapter(Context context, List<RongIMClient.UserInfo> users) {
        mUserInfoList = users;
        mLayoutInflater = LayoutInflater.from(context);
    }

	@Override
	public int getCount() {
		return mUserInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null || convertView.getTag() == null) {

			convertView = mLayoutInflater.inflate(R.layout.black_list_item,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.itemName = (TextView) convertView
					.findViewById(R.id.item_name);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemName.setText(mUserInfoList.get(position).getName());
		return convertView;
	}

	static class ViewHolder {
		TextView itemName;
	}

}
