package io.rong.imkit.demo;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by zhjchen on 14-7-17.
 */
public class FunctionListAdapter extends BaseAdapter {
    private static final String TAG = "FunctionListAdapter";
    private String[] mNameArray;
    private LayoutInflater mLayoutInflater;
    int numbermessage = 0;

    public FunctionListAdapter(Context context, String[] names) {
        mNameArray = names;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public FunctionListAdapter(Context context, String[] names, int totalNum) {
        mNameArray = names;
        mLayoutInflater = LayoutInflater.from(context);
        numbermessage = totalNum;
    }

    @Override
    public int getCount() {
        if (mNameArray == null)
            return 0;
        return mNameArray.length;
    }

    @Override
    public Object getItem(int position) {
        if (mNameArray == null || mNameArray.length >= position)
            return null;
        return mNameArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null || convertView.getTag() == null) {

            convertView = mLayoutInflater.inflate(R.layout.item_function_list,
                    parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView
                    .findViewById(android.R.id.text1);
            viewHolder.showNumberImg = (TextView) convertView
                    .findViewById(R.id.show_num);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            if (position == 0 && numbermessage > 0&&numbermessage <100) {
                viewHolder.showNumberImg.setText("" + numbermessage);
                viewHolder.showNumberImg.setVisibility(View.VISIBLE);
            } else if (position == 0 && numbermessage > 99) {
                viewHolder.showNumberImg.setVisibility(View.VISIBLE);
                viewHolder.showNumberImg.setText(R.string.no_read_message);
            }else if(position !=0){
                viewHolder.showNumberImg.setVisibility(View.GONE);
            }

        viewHolder.titleTextView.setText(mNameArray[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView showNumberImg;
    }
}
