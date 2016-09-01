package com.example.dsxdsxdsx0.cookbook.cookerys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.adapter.ShowCookAdapter;
import com.example.dsxdsxdsx0.cookbook.db.CooksDbManager;
import com.example.dsxdsxdsx0.cookbook.details.CookDetailsActivity;
import com.example.dsxdsxdsx0.cookbook.info.ShowCookersInfo;
import com.example.dsxdsxdsx0.cookbook.util.NetworkUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/8/18.
 *
 * 显示菜谱列表Activity
 *
 */

@ContentView(R.layout.activity_cookery)
public class ShowCookActivity extends BaseActivity {

    private static ShowCookersInfo info;
    private ShowCookersInfo tabInfo2;
    private ShowCookersInfo tabInfo3;

    @ViewInject(R.id.linear_bar)
    private LinearLayout mLinearBar;

    @ViewInject(R.id.edit_note_bar)
    private Toolbar mToolbar;

    @ViewInject(R.id.tablayout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.show_detail_viewpager)
    private ViewPager viewPager;

    private List<String> titles;

    private List<ShowCookFragment> fragmentList;

    private ShowCookAdapter showCookAdapter;

    private int cookId;

    private int pn;//页数

    private String searchKey;//搜索关键字

    private String title;//标题

    private int tabCursor;//tab页导航标题

    private CookItemAdapter cookItemAdapter;//ListView的item适配器

    @Override
    public void initBefore() {

        titles = new ArrayList<>();
        titles.add("全部菜谱");
        titles.add("最近浏览");
        titles.add("我的收藏");

        fragmentList = new ArrayList<>();
        fragmentList.add(new ShowCookFragment());
        fragmentList.add(new ShowCookFragment());
        fragmentList.add(new ShowCookFragment());

        getBeforeBundle();

        //Toolbar进行设置icon,标题,点击事件
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp1);

        mToolbar.setTitle(title);

        //设置Toolbar为activity的ActionBar
        setSupportActionBar(mToolbar);//v7jar才能使用

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCookActivity.this.finish();
                overridePendingTransition(R.anim.new_to_right,R.anim.old_to_right);
            }
        });

    }

    //通过Bundle获得传值
    private void getBeforeBundle(){
        Bundle bundle = getIntent().getExtras();
        tabCursor = bundle.getInt("tab_cursor", 0);
        title = bundle.getString("title");
        searchKey = bundle.getString("search_key");
        cookId = bundle.getInt("id",0);
        if(tabCursor > 1){
            cookId = 1;
        }
    }

    @Override
    public void init() {
        showCookAdapter = new ShowCookAdapter(getSupportFragmentManager(),fragmentList,titles);
        viewPager.setAdapter(showCookAdapter);

        //从PagerAdapter的getPagerTitle(int)方法中获取tab页的标题
        mTabLayout.setTabsFromPagerAdapter(showCookAdapter);
        //在Tab页中添加ViewPager
        mTabLayout.setupWithViewPager(viewPager);

    }

    /**
     * 从网络中获取数据
     * @param isLoad 是否加载
     */
    public void getDataFromNet(final boolean isLoad){

        if (isLoad) {
            pn += 10;
        }else {
            pn = 0;
        }

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configDefaultHttpCacheExpiry(5000);
        httpUtils.send(HttpRequest.HttpMethod.GET, NetworkUtil.getUrl(cookId, searchKey, pn, 10), new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Gson gson = new Gson();
                if (isLoad) {
                    ShowCookersInfo loadInfo = gson.fromJson(responseInfo.result.toString(), ShowCookersInfo.class);
                    if (NetworkUtil.isWrong(ShowCookActivity.this, loadInfo.getError_code(), Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG))) {
                        for (int i = 0, length = loadInfo.getResult().getData().size(); i < length; i++) {
                            info.getResult().getData().add(loadInfo.getResult().getData().get(i));
                            cookItemAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    info = gson.fromJson(responseInfo.result.toString(), ShowCookersInfo.class);
                    if (NetworkUtil.isWrong(getApplicationContext(), info.getError_code(), Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT))) {
                        fillingFragList();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("TAG", s);
            }
        });

    }

    private void fillingFragList() {
        if (info == null) {
            return;
        }
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (fragmentList.get(0).getmCooksLv() == null) {
                    return;
                }
                cookItemAdapter = new CookItemAdapter(info);
                fragmentList.get(0).setFlag1();
                fragmentList.get(0).getmCooksLv().setAdapter(cookItemAdapter);
                fragmentList.get(0).getmCooksLv().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //根据列表id所对应的数据库数值设置item显示
                        CooksDbManager.getCooksDBManager(ShowCookActivity.this).setData(info.getResult().getData().get((int) id));
                        //根据id插入数据
                        CooksDbManager.getCooksDBManager(ShowCookActivity.this).insertData(info.getResult().getData().get((int)id));

                        Intent intent = new Intent(ShowCookActivity.this,CookDetailsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };

        if (fragmentList.get(0).getmCooksLv() == null) {
            handler.postDelayed(runnable,300);
        }else {
            handler.post(runnable);
        }

    }


    @Override
    public void initAfter() {
        getDataFromNet(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        fillingFragList();
                        break;
                    case 1:
                        ShowCookFragment fra = fragmentList.get(1);
                        fra.setFlag2();
                        tabInfo2 = CooksDbManager.getCooksDBManager(ShowCookActivity.this).getData(true,false);
                        fra.getmCooksLv().setAdapter(new CookItemAdapter(tabInfo2));
                        fra.getmCooksLv().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                CooksDbManager.getCooksDBManager(ShowCookActivity.this).setData(tabInfo2.getResult().getData().get((int) id));
                                CooksDbManager.getCooksDBManager(ShowCookActivity.this).insertData(tabInfo2.getResult().getData().get((int) id));

                                //跳转到菜谱详情页
                                Intent intent = new Intent();

                            }
                        });
                        break;

                    case 2:
                        ShowCookFragment fra2 = fragmentList.get(2);
                        fra2.setFlag3();
                        tabInfo3 = CooksDbManager.getCooksDBManager(ShowCookActivity.this).getData(true,false);
                        fra2.getmCooksLv().setAdapter(new CookItemAdapter(tabInfo3));
                        fra2.getmCooksLv().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                CooksDbManager.getCooksDBManager(ShowCookActivity.this).setData(tabInfo3.getResult().getData().get((int)id));
                                CooksDbManager.getCooksDBManager(ShowCookActivity.this).setData(tabInfo3.getResult().getData().get((int)id));

                                //跳转到菜谱详情页
                                Intent intent = new Intent();


                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(tabCursor >= 1){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   viewPager.setCurrentItem(tabCursor,true);
                }
            },500);
        }


    }



    /**
     * 当最近浏览数据被清除时调用，刷新ListView
     */
    public void updateData(){
        ShowCookFragment showCookFragment = fragmentList.get(1);
        ShowCookersInfo info = CooksDbManager.getCooksDBManager(ShowCookActivity.this).getData(true,false);
        showCookFragment.getmCooksLv().setAdapter(new CookItemAdapter(info));
    }



    public class CookItemAdapter extends BaseAdapter{

        private ShowCookersInfo info;
        private BitmapUtils utils = new BitmapUtils(ShowCookActivity.this, Environment.getExternalStorageDirectory().getPath() + "//img");

        public CookItemAdapter(ShowCookersInfo info) {
            this.info = info;
        }

        @Override
        public int getCount() {
            if (info.getResult() == null) {
                return 0;
            }
            return info.getResult().getData().size();
        }

        @Override
        public Object getItem(int position) {
            return info.getResult().getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ShowCookActivity.this).inflate(R.layout.item_cook_list, null);
                holder = new ViewHolder();
                holder.cook_name = (TextView) convertView.findViewById(R.id.cook_name);
                holder.cook_tags = (TextView) convertView.findViewById(R.id.cook_tags);
                holder.cook_ingredients = (TextView) convertView.findViewById(R.id.cook_ingredients);
                holder.cook_burden = (TextView) convertView.findViewById(R.id.cook_burden);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cook_name.setText(info.getResult().getData().get(position).getTitle());
            holder.cook_tags.setText(info.getResult().getData().get(position).getTags());
            holder.cook_ingredients.setText(info.getResult().getData().get(position).getIngredients());
            holder.cook_burden.setText(info.getResult().getData().get(position).getBurden());
            utils.display(holder.img, info.getResult().getData().get(position).getAlbums().get(0));

            return convertView;
        }

        class ViewHolder{
            TextView cook_name;
            TextView cook_tags;
            TextView cook_ingredients;
            TextView cook_burden;
            ImageView img;

        }
    }






}
