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
    public static final String APP_KEY = "z3v5yqkbv8v30";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * IMKit SDK调用第一步 初始化
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
//            System.loadLibrary("imdemo");
        } catch (UnsatisfiedLinkError e) {
//            e.printStackTrace();
        }
    /* RongIM.getInstance().getRongIMClient().setConversationNotificationCycle("00",20,new RongIMClient.SetConversationNotificationCycleCallback() {
    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(ErrorCode errorCode) {

    }
    });
        RongIM.getInstance().getRongIMClient().removeConversationNotificationCycle(new RongIMClient.RemoveConversationNotificationCycleCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });
        RongIM.getInstance().getRongIMClient().getConversationNotificationCycle(new RongIMClient.GetConversationNotificationCycleCallback() {
            @Override
            public void onSuccess(String s, int i) {

            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });*/

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

    }

}
