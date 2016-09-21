package com.example.dsxdsxdsx0.cookbook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by dsxdsxdsx0 on 2016/9/8.
 *
 * 引导页
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    public static final int MSG_START_ANIM = 1;

    public static final int MSG_GO_ANIM = 2;

    private AlphaAnimation alphaAnimation;//透明度动画

    @ViewInject(R.id.iv_welcome)
    private ImageView mWelcomeIv;//欢迎页图片

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_START_ANIM:
                    mWelcomeIv.startAnimation(alphaAnimation);
                    mWelcomeIv.setVisibility(View.VISIBLE);
                    break;
                case MSG_GO_ANIM:
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    overridePendingTransition(R.anim.new_to_left,R.anim.old_to_left);
                    finish();
                    break;
            }

            return false;
        }
    });

    @Override
    public void initBefore() {

    }

    @Override
    public void init() {
        alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(MSG_GO_ANIM,1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void initAfter() {
        handler.sendEmptyMessageDelayed(MSG_START_ANIM,1000);
    }
}
