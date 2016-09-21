package com.example.dsxdsxdsx0.cookbook.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.MainActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dsxdsxdsx0 on 2016/9/13.
 *
 * 登录
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.edit_note_bar)
    private Toolbar toolbar;

    @ViewInject(R.id.edittext_user)
    private EditText mUserNameEt;//账号

    @ViewInject(R.id.edittext_pw)
    private EditText mUserPwdEt;//密码

    @ViewInject(R.id.icon_show_pwd)
    private ImageView mIsShowPwdIv;//点击是否显示密码

    @ViewInject(R.id.btn_go_login)
    private Button mLoginBtn;//登录

    @ViewInject(R.id.btn_register)
    private Button mRegistBtn;//注册

    private ProgressDialog progressDialog;

    private static final int FINISH = 1;

    private boolean isShowPwd = false;

    @Override
    public void initBefore() {
        //Toolbar设置icon，标题和点击事件
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp1);
        toolbar.setTitle("登录");

        //设置Toolbar为activity的ActionBar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("信息提示");
        progressDialog.setMessage("正在加载中，请稍后...");
        progressDialog.setCancelable(true);

    }

    @Override
    public void init() {
        //登录
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new LoginThread()).start();//开启登录的子线程
                progressDialog.show();//显示进度条
            }
        });

        //注册
        mRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        //是否显示密码
        mIsShowPwdIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowPwd){//如果不显示的话
                    mIsShowPwdIv.setImageResource(R.mipmap.icon_no_show_pwd);
                    //设置EditText文本为隐藏的
                    mUserPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    //设置EditText文本为可见的
                    mUserPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIsShowPwdIv.setImageResource(R.mipmap.icon_show_pwd_normal);
                }

                isShowPwd = !isShowPwd;
                mUserPwdEt.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mUserPwdEt.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
    }

    @Override
    public void initAfter() {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FINISH) {
                final String name = mUserNameEt.getText().toString();
                String pwd = mUserPwdEt.getText().toString();

                //Bnob设置账号和密码
                final BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(name);
                bmobUser.setPassword(pwd);

                bmobUser.login(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            //如果成功
                            if(bmobUser.getUsername().equals(name)){
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user_name",name);
                                startActivity(intent);
                                progressDialog.dismiss();
                                overridePendingTransition(R.anim.new_to_right,R.anim.old_to_right);
                            }else {
                                Toast.makeText(LoginActivity.this,"用户未验证",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,AlertDialog.THEME_HOLO_DARK);
                            builder.setTitle("信息提示");
                            builder.setIcon(R.mipmap.app_icon);
                            builder.setMessage("账号或密码输入错误");
                            builder.setPositiveButton("确定", null);
                            builder.show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }
    };

    //登录子线程
    class LoginThread implements Runnable{

        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = FINISH;
            try {
                Thread.sleep(1000);//缓冲1s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendMessage(message);//发送并处理
        }
    }

}
