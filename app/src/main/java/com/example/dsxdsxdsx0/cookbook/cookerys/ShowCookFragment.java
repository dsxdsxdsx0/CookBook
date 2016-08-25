package com.example.dsxdsxdsx0.cookbook.cookerys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.pulltorefresh.ILoadingLayout;
import com.example.dsxdsxdsx0.cookbook.pulltorefresh.PullToRefreshBase;
import com.example.dsxdsxdsx0.cookbook.pulltorefresh.PullToRefreshListView;

/**
 * Created by dsxdsxdsx0 on 2016/8/24.
 *
 * 承载逻辑和页面的Fragment
 */
public class ShowCookFragment extends Fragment {

    private PullToRefreshListView mCooksLv;

    private ShowCookActivity showCookActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_view_list,null);
        showCookActivity = (ShowCookActivity) getActivity();
        mCooksLv = (PullToRefreshListView) view.findViewById(R.id.cooks_lv);

        setFlag1();

        return view;
    }



    private void setFlag1() {
        mCooksLv.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout startLabels = mCooksLv.getLoadingLayoutProxy(true,false);
        startLabels.setPullLabel("下拉刷新");
        startLabels.setRefreshingLabel("正在刷新");
        startLabels.setReleaseLabel("放开刷新");

        ILoadingLayout endLabels = mCooksLv.getLoadingLayoutProxy(false,true);
        endLabels.setPullLabel("上拉加载更多");
        endLabels.setRefreshingLabel("正在载入");
        endLabels.setReleaseLabel("放开加载");

        mCooksLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });



    }
}
