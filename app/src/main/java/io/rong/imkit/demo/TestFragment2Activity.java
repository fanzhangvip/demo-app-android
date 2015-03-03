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
//                sendMessage();
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        Button button = (Button) inflater.inflate(R.layout.ui_action_btn, mActionBar, false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("","-----------------------0000000000000------------");
            }
        });



        mActionBar.addView(button);

    }


    private void sendMessage() {

        String content = "新华网莫斯科10月14日电 国务院总理李克强14日出席第三届莫斯科国际创新发展论坛并发表题为《以创新实现共同发展包容发展》的演讲。演讲全文如下：创新是人类发展进步的不熄引擎。当今世界正处于大变革、大调整之中，迫切要求更大范围、更深层次的创新。实现这样的创新，墨守成规不行，单打独斗也不行，需要开放、合作与分享。6年前，面对国际金融危机，国际社会同舟共济，避免了危机向纵深蔓延。随着经济全球化、社会信息化的深入推进，更需要各国携起手来，在合作创新中实现知识的倍增、价值的倍增，解决发展的难题，促进共同繁荣。这正是开放式创新的意义所在。";
        String title = "新华网莫斯科10月14日电,李克强在第三届莫斯科国际创新发展论坛上的演讲";
        String url = "http://img2.cache.netease.com/photo/0003/2014-10-15/900x600_A8J6CVA400AJ0003.jpg";
        final RichContentMessage imageTextMessage = new RichContentMessage(title, content, url);
        imageTextMessage.setExtra("可以存放的网址，商品编号或URI,在点击消息时你可以取到进入你的商品页面");


        RongIM.getInstance().sendMessage(RongIMClient.ConversationType.PRIVATE, DemoContext.getInstance().getCurrentUser().getUserId(), imageTextMessage, new RongIMClient.SendMessageCallback() {

                    @Override
                    public void onSuccess(int messageId) {

                    }

                    @Override
                    public void onError(int messageId, ErrorCode errorCode) {

                    }

                    @Override
                    public void onProgress(int messageId, int percent) {

                    }
                }
        );

    }
}
