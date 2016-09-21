package com.example.dsxdsxdsx0.cookbook;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dsxdsxdsx0.cookbook.adapter.MainMenuAdapter;
import com.example.dsxdsxdsx0.cookbook.adapter.MyViewPagerAdapter;
import com.example.dsxdsxdsx0.cookbook.application.CookApplication;
import com.example.dsxdsxdsx0.cookbook.cookerys.ShowCookActivity;
import com.example.dsxdsxdsx0.cookbook.index.IndexActivity;
import com.example.dsxdsxdsx0.cookbook.info.UpdateEntity;
import com.example.dsxdsxdsx0.cookbook.util.ProgressDialogUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private Handler mHandler;
    
    //图片
    private ImageView[]imageViews;

    //图片id
    private int []imgIds;

    //小圆点
    private ImageView []tips;

    //搁置小点的容器
    @ViewInject(R.id.im)
    private LinearLayout mTipsLayout;

    //ViewPager轮播
    @ViewInject(R.id.main_viewpager)
    private ViewPager mViewPager;

    //GridView 菜单
    @ViewInject(R.id.main_mygridview)
    private GridView mGridView;

    //搜索输入框
    @ViewInject(R.id.edit_search)
    private EditText mSearchEt;

    @ViewInject(R.id.img_search)
    private ImageView mSearchIv;

    private static boolean viewPageRun = true;

    private List<String> list;

    public static final int AUTO = 198;

    private MyViewPagerAdapter adapter;//轮播图adapter

    private BroadcastReceiver mReceiver;

    private MainMenuAdapter mMenuAdapter;//菜单adapter

    //我的收藏点击事件
    @OnClick(R.id.tv_collect)
    public void onCollectClick(View v){
        Intent intent = new Intent(MainActivity.this,ShowCookActivity.class);
        intent.putExtra("title","我的收藏");
        intent.putExtra("tab_cursor",2);
        startActivity(intent);
        overridePendingTransition(R.anim.new_to_left, R.anim.old_to_left);
    }

    //最近浏览点击事件
    @OnClick(R.id.tv_read)
    public void onScanClick(View v){
        Intent intent = new Intent(MainActivity.this,ShowCookActivity.class);
        intent.putExtra("title","最近浏览");
        intent.putExtra("tab_cursor",1);
        startActivity(intent);
        overridePendingTransition(R.anim.new_to_left,R.anim.old_to_left);
    }

    //全部菜谱点击事件
    @OnClick(R.id.tv_all_food)
    private void onAllFoodClick(View v){
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.new_to_top,R.anim.old_to_top);
    }

    //搜索点击按钮
    @OnClick(R.id.img_search)
    private void onSearchClick(View view){
        String content = mSearchEt.getText().toString();
        if (content == null || content.length() != 0) {
            Intent intent = new Intent(this,ShowCookActivity.class);
            intent.putExtra("title","菜谱搜索");
            intent.putExtra("search_key",content);
            startActivity(intent);
            overridePendingTransition(R.anim.new_to_left,R.anim.old_to_left);
        }
    }

    @OnTouch(R.id.main_viewpager)
    public boolean onTouch(View v,MotionEvent event){

        if(v == mViewPager){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    viewPageRun = false;
                    break;

                case  MotionEvent.ACTION_MOVE:
                    viewPageRun = true;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    viewPageRun = true;
                    break;

            }
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewPageRun = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPageRun = false;
    }

    @Override
    public void initBefore() {
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setStatusHeight();
        }


        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "4efaa4fdb3375a020686ff80dc758c2a");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    @Override
    public void init() {
        imgIds = new int[]{R.mipmap.p25,R.mipmap.p29,R.mipmap.p31};
        imageViews = imgIds.length <= 3 ? new ImageView[imgIds.length * 4] : new ImageView[imgIds.length];
        for(int i = 0;i<imageViews.length;i++){
            imageViews[i] = new ImageView(this);
            //图片填充容器多余的空白
            imageViews[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews[i].setImageResource(imgIds[i % imgIds.length]);
        }
        setPoint();
        adapter = new MyViewPagerAdapter(imageViews);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(imageViews.length * 100);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position % imgIds.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        autoSlidePager();
        addBroadcastReceiver();


    }

    //添加广播，监听更新
    private void addBroadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if("com.jiaji.cookbook.update".equals(action)){
                    UpdateEntity updateEntity = intent.getParcelableExtra("update_entity");
                    showUpdateDialog("更新内容:"+updateEntity.getChangelog()+"\n版   本："+updateEntity.getVersionShort()+
                    "\n大   小:"+String.format("%.2f M",(updateEntity.getBinary().getFsize())/(1000.0 * 1000.0)),updateEntity.getInstallUrl());
                }else if("com.jiaji.cookbook.update_progress".equals(action)){
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setDataAndType(Uri.fromFile(new File(intent.getStringExtra(UpdateService.APK_LOCAL))),"application/vnd.android.package-archive");
                    startActivity(intent1);
                }
            }
        };

        //动态注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jiaji.cookbook.update");
        intentFilter.addAction("com.jiaji.cookbook.update_progress");
        registerReceiver(mReceiver,intentFilter);
    }

    private void showUpdateDialog(String msg, final String url){
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("检查到更新")
                .setIcon(R.drawable.ic_update).setMessage(msg).setNegativeButton("稍等", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateService.start(url);
                        ProgressDialogUtil.show(CookApplication.getContext());
                    }
                }).create();
        dialog.show();
    }

    /**
     * 自动循环ViewPager
     */
    private void autoSlidePager(){
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == AUTO){
                    if(viewPageRun){
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                    mHandler.sendEmptyMessageDelayed(AUTO, 3000);
                }
                return false;
            }
        };
        mHandler = new Handler(callback);
        mHandler.sendEmptyMessageDelayed(AUTO, 3000);
    }


    @Override
    public void initAfter() {
        list = new ArrayList<>();
        list.add("家常菜");
        list.add("快手菜");
        list.add("素菜");
        list.add("创意菜");
        list.add("凉菜");
        list.add("烘培");
        list.add("面食");
        list.add("汤");
        list.add("自制调味料");

        mMenuAdapter= new MainMenuAdapter(list);
        mGridView.setAdapter(mMenuAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到ShowCookActivity
                Intent intent = new Intent(MainActivity.this, ShowCookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",position + 1);
                bundle.putString("title",list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.new_to_left,R.anim.old_to_left);
            }
        });

        initSearch();
    }

    //处理搜索框
    private void initSearch() {

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    mSearchIv.setVisibility(View.GONE);
                }else {
                    mSearchIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //把点点放到图上
    public void setPoint(){
        tips = new ImageView[imgIds.length];
        for (int i = 0;i<tips.length;i++){
            tips[i] = new ImageView(this);
            if(i==0){
                tips[i].setImageResource(R.mipmap.yuanquan_up2);
            }else {
                tips[i].setImageResource(R.mipmap.yuanquan_down2);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            layoutParams.width = 20;
            layoutParams.height = 20;
            layoutParams.leftMargin = 5;
            layoutParams.leftMargin = 5;

            mTipsLayout.addView(tips[i],layoutParams);
        }
    }

    private void setImageBackground(int selectItems){
        for(int i = 0;i<tips.length;i++ ){
            if(i == selectItems){
                tips[i].setImageResource(R.mipmap.yuanquan_up2);
            }else {
                tips[i].setImageResource(R.mipmap.yuanquan_down2);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        ProgressDialogUtil.destory();
    }
}
