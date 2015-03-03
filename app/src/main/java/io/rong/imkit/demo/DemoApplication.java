package io.rong.imkit.demo;

import android.app.Application;

import io.rong.imkit.RongIM;
import io.rong.imkit.demo.message.GroupInvitationNotification;
import io.rong.imlib.AnnotationNotFoundException;

/**
 * Created by zhjchen on 14-3-20.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * IMKit SDK调用第一步 初始化
         * context上下文
         */
        RongIM.init(this);

        /**
         * 融云SDK事件监听处理
         */
        RongCloudEvent.init(this);

        DemoContext.init(this);
        try {
            //注册自定义消息类型
            RongIM.registerMessageType(GroupInvitationNotification.class);

        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

    }

}