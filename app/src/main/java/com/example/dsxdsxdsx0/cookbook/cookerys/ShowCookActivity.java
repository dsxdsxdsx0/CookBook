package com.example.dsxdsxdsx0.cookbook.cookerys;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.adapter.ShowCookAdapter;
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

    private ShowCookersInfo info;
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
                    ShowCookersInfo showCookersInfo = gson.fromJson(responseInfo.result.toString(), ShowCookersInfo.class);
                    if (NetworkUtil.isWrong(ShowCookActivity.this, showCookersInfo.getError_code(), Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG))) {
                        for (int i = 0, length = showCookersInfo.getResult().getData().size(); i < length; i++) {
                            showCookersInfo.getResult().getData().add(showCookersInfo.getResult().getData().get(i));
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
                Log.i("TAG",s);
            }
        });

    }

    private void fillingFragList() {
        if (info == null) {

        }

    }


    @Override
    public void initAfter() {

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
