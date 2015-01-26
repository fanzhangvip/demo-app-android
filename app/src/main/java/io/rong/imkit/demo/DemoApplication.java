package io.rong.imkit.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;

/**
 * Created by zhjchen on 14-3-20.
 */
public class DemoApplication extends Application {

    private static final String IS_FIRST = "is_first";
    DemoContext mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * IMKit SDK调用第一步 初始化
         * 第一个参数，  context上下文
         * 第二个参数，APPKey换成自己的appkey
         * 第三个参数，push消息通知所要打个的action页面
         * 第四个参数，push消息中可以自定义push图标
         */
        RongIM.init(this);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {

            @Override
            public boolean onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo user) {
                Log.d("Begavior", conversationType.getName() + ":" + user.getName());
                Intent in = new Intent(context, UserInfoActivity.class);
                in.putExtra("user_name", user.getName());
                in.putExtra("user_id", user.getUserId());
                context.startActivity(in);
                return false;

            }


            @Override
            public boolean onClickMessage(Context context, RongIMClient.Message message) {

                if (message.getContent() instanceof LocationMessage) {
                    Intent intent = new Intent(context, LocationActivity.class);
                    intent.putExtra("location", message.getContent());
                    context.startActivity(intent);

                }else  if(message.getContent() instanceof RichContentMessage){
                    RichContentMessage  mRichContentMessage = (RichContentMessage) message.getContent();
                    Log.d("Begavior",  "extra:"+mRichContentMessage.getExtra());

                }

                 Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

                return false;
            }
        });

        mContext = DemoContext.getInstance();
        if(mContext != null)
            mContext.init(this);
        else
            throw new RuntimeException("初始化异常");
        try {
//            Class c;
//            c = Class.forName("com.networkbench.agent.impl.NBSAppAgent");
//            Method m = c.getMethod("setLicenseKey", new Class[]{String.class});
//            m.invoke(c, new Object[]{"a546c342ba704acf91b27e9603b6860d"});

        } catch (Exception e) {
            e.printStackTrace();
        }

       try {
//            System.loadLibrary("imdemo");
        } catch (UnsatisfiedLinkError e) {
//            e.printStackTrace();
        }



        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

    }

}
