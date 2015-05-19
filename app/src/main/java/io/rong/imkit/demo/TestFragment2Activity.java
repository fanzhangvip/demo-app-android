package io.rong.imkit.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import io.rong.imkit.RongIM;
import io.rong.imkit.view.ActionBar;
import io.rong.imlib.RongIMClient;
import io.rong.message.RichContentMessage;


/**
 * Created by DragonJ on 14-9-15.
 */
public class TestFragment2Activity extends FragmentActivity {

    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setData(Uri.parse("rong://io.rong.imkit.demo").buildUpon().appendPath("conversation").appendPath("private")
                .appendQueryParameter("targetId", DemoContext.getInstance().getCurrentUser().getUserId()).appendQueryParameter("title", "张三").build());
        setContentView(R.layout.activity_test2_fragment);
        mActionBar = (ActionBar) findViewById(R.id.rc_actionbar);
        mActionBar.getTitleTextView().setText("会话自定义Fragment测试");
        mActionBar.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        Button button = (Button) inflater.inflate(R.layout.ui_action_btn, mActionBar, false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        mActionBar.addView(button);

    }

}
