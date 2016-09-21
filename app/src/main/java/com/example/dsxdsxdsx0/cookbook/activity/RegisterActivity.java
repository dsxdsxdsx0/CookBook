package com.example.dsxdsxdsx0.cookbook.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.info.UserInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dsxdsxdsx0 on 2016/9/18.
 *
 * 注册页面
 *
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.edit_note_bar)
    private Toolbar toolbar;

    @ViewInject(R.id.edittext_user)
    private EditText mUserNameEt;//用户名

    @ViewInject(R.id.edittext_password)
    private EditText mPwdEt;

    @ViewInject(R.id.edittext_password_again)
    private EditText mPwdAgainEt;//再次输入密码

    @ViewInject(R.id.edittext_vaild_code)
    private EditText mObtainCodeEt;//验证码输入框

    @ViewInject(R.id.btn_achieve_authcode)
    private Button mObtainCodeBtn;//获取验证码

    @ViewInject(R.id.tv_user_agree)
    private TextView mUserAgreeTv;//用户协议

    @ViewInject(R.id.checkbox_agree)
    private CheckBox mCheckBox;//勾选框

    @ViewInject(R.id.register_submit)
    private Button mRegisterBtn;//注册按钮

    private static final int FINISH = 1;

    private ProgressDialog progressDialog;

    private BmobUser userInfo;

    private String pwd;

    private String userName;

    private String pwd2;

    private String authCode;


    @Override
    public void initBefore() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("信息提示");
        progressDialog.setMessage("正在加载中，请稍等...");
        progressDialog.setIcon(R.mipmap.app_icon);
        progressDialog.setCancelable(true);

        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp1);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.new_to_right,R.anim.old_to_right);
            }
        });

    }

    @Override
    public void init() {

        userName = mUserNameEt.getText().toString();
        pwd = mPwdEt.getText().toString();
        pwd2 = mPwdAgainEt.getText().toString();
        authCode = mObtainCodeEt.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(RegisterActivity.this,"获取验证码成功",Toast.LENGTH_LONG).show();
        }

        //请求验证码
        mObtainCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(userName, "", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this,"获取验证码成功",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(RegisterActivity.this,"获取验证码失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        new Thread(new HandThread()).start();//开启子线程
        progressDialog.show();//打开进度加载
    }

    @Override
    public void initAfter() {

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == FINISH){

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(authCode)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this,AlertDialog.THEME_HOLO_DARK);
                    builder.setIcon(R.mipmap.app_icon);
                    builder.setTitle("信息提示");
                    builder.setMessage("用户名或密码不能为空");
                    builder.setPositiveButton("返回", null);
                    builder.show();
                    return;
                }

                userInfo = new BmobUser();
                userInfo.setUsername(userName);
                userInfo.setPassword(pwd);

                //通过获取手机验证码进行注册
                userInfo.signOrLogin(authCode,new SaveListener < Object > () {
                    @Override
                    public void done (Object o, BmobException e){
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    };

    private class HandThread implements Runnable {
        @Override
        public void run() {
            Message msg=Message.obtain();
            msg.what=FINISH;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendMessage(msg);//发送至handler
        }
    }



}
