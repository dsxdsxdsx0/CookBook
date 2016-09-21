package com.example.dsxdsxdsx0.cookbook.index;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.info.SortTagInfo;
import com.example.dsxdsxdsx0.cookbook.view.IndexView;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dsxdsxdsx0 on 2016/9/5.
 *
 * 全部菜谱索引页
 */
@ContentView(R.layout.index_layout)
public class IndexActivity extends BaseActivity {

    private SortTagInfo sortTagInfo;

    @ViewInject(R.id.linear_bar)
    private LinearLayout mLinearBar;

    @ViewInject(R.id.listview_cooks)
    private ListView mCooksView;

    @ViewInject(R.id.overlay_title_bar)
    private LinearLayout mOverlayTitleBarLayout;//标题栏

    @ViewInject(R.id.iv_back)
    private ImageView mBackIv;//返回键

    @ViewInject(R.id.tv_title)
    private TextView mTitleTv;//标题

    @ViewInject(R.id.show_now_index)
    private TextView mShowNowIndexTv;//显示中间的数字

    @ViewInject(R.id.index_view)
    private IndexView mIndexView;//右侧字符栏

    private RelativeLayout.LayoutParams layoutParams;

    @Override
    public void initBefore() {

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //通过Typeface来设置字体
        Typeface typeface =Typeface.createFromAsset(getAssets(),"jianxiyuan.ttf");
        mTitleTv.setTypeface(typeface);
        mShowNowIndexTv.setTypeface(typeface);

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.iv_back)
    private void backClick(View view){
        finish();
        overridePendingTransition(R.anim.new_to_right,R.anim.old_to_right);
    }

    @Override
    public void init() {
        Gson gson = new Gson();
        try{
            InputStream is =getAssets().open("cooks_classify");
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String len = "";
            while ((len = bufferedReader.readLine())!=null){
                stringBuilder.append(len);
            }
            bufferedReader.close();
            sortTagInfo = gson.fromJson(stringBuilder.toString(),SortTagInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initAfter() {
        mCooksView.setAdapter(new CooksListAdapter(this,sortTagInfo));

        mIndexView.setOnLetterChangeListener(new IndexView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(int selectedIndex) {
                int position = selectedIndex - 1;
                mCooksView.setSelection(position);
                //显示中间显示的字
                mShowNowIndexTv.setText(sortTagInfo.getResult().get(position).getName());
                mShowNowIndexTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClickUp() {
                mShowNowIndexTv.setVisibility(View.GONE);//当放开时设置为不可见
            }
        });

        //监听ListView的滑动
        mCooksView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /**
                 * 在很多时候ListView列表数据不需要全部刷新，只需刷新有数据变化的那一条，
                 * 这时可以用getChildAt(index)获取某个指定position的view，并对该view进行刷新。

                 注意：在ListView中，使用getChildAt(index)的取值，只能是当前可见区域（列表可滚动）的子项！

                 即取值范围在 >= ListView.getFirstVisiblePosition() &&  <= ListView.getLastVisiblePosition();
                 所以如果想获取前部的将会出现返回Null值空指针问题
                 */
                View v = view.getChildAt(0);
                if (v == null) {
                    return;
                }

                int dex = v.getBottom() - mTitleTv.getHeight();
                if(dex <= 0){
                    layoutParams.topMargin = dex;
                }else {
                    layoutParams.topMargin = 0;
                }
                Log.i("TAG","firstVisibleItem = " + firstVisibleItem);

                mTitleTv.setText(sortTagInfo.getResult().get(firstVisibleItem).getName());

                mOverlayTitleBarLayout.setLayoutParams(layoutParams);
                mIndexView.setSelected(firstVisibleItem + 1);
            }
        });
    }
}
