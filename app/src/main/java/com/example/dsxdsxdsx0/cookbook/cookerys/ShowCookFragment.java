package com.example.dsxdsxdsx0.cookbook.cookerys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.db.CooksDbManager;
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



    public void setFlag1() {
        mCooksLv.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout startLabels = mCooksLv.getLoadingLayoutProxy(true,false);
        startLabels.setPullLabel("下拉刷新");
        startLabels.setRefreshingLabel("正在刷新");
        startLabels.setReleaseLabel("放开刷新");

        ILoadingLayout endLabels = mCooksLv.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多");
        endLabels.setRefreshingLabel("正在载入");
        endLabels.setReleaseLabel("放开加载");

        mCooksLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            showCookActivity.getDataFromNet(false);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCooksLv.onRefreshComplete();
                                }
                            });
                        }
                    }).start();
                } else if (refreshView.isFooterShown()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            showCookActivity.getDataFromNet(true);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCooksLv.onRefreshComplete();
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    public void setFlag2(){
        mCooksLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ILoadingLayout startLabels = mCooksLv.getLoadingLayoutProxy(true,false);
        startLabels.setPullLabel("下拉清除最近浏览");
        startLabels.setRefreshingLabel("正在清除");
        startLabels.setReleaseLabel("放开清除最近浏览");

        mCooksLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CooksDbManager.getCooksDBManager(getActivity()).delData(null);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showCookActivity.updateData();
                                    mCooksLv.onRefreshComplete();
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    public void setFlag3(){
        mCooksLv.setMode(PullToRefreshBase.Mode.DISABLED);
    }



    public PullToRefreshListView getmCooksLv(){
        return mCooksLv;
    }
}
