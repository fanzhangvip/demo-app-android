package io.rong.imkit.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sea_monster.core.resource.model.Resource;

import java.util.List;

import io.rong.imkit.adapter.BaseUIAdapter;
import io.rong.imkit.demo.common.DemoApi;
import io.rong.imkit.demo.model.CustomerService;
import io.rong.imkit.veiw.AsyncImageView;

/**
 * Created by zhjchen on 14-8-5.
 */
public class CustomServiceAdapter extends BaseUIAdapter<CustomerService> {

    public CustomServiceAdapter(Context context) {
        super(context);
    }

    public CustomServiceAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        AsyncImageView asyncImageView = (AsyncImageView) holder.obtainView(convertView, android.R.id.icon);
        TextView textView = (TextView) holder.obtainView(convertView, android.R.id.text1);
        CustomerService customerService = dataSet.get(position);

        if (asyncImageView.getResource() == null) {
            asyncImageView.setResource(new Resource(DemoApi.CUSTOM_SERVICE_HOST + customerService.getGlobalIcon()));
        }

        textView.setText(customerService.getGlobalNick());

        return convertView;
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_custom_service_list;
    }
}
