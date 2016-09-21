package com.example.dsxdsxdsx0.cookbook.index;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.cookerys.ShowCookActivity;
import com.example.dsxdsxdsx0.cookbook.info.SortTagInfo;

/**
 * Created by dsxdsxdsx0 on 2016/9/7.
 */
public class CooksListAdapter extends BaseAdapter {

    private Context context;

    private SortTagInfo sortTagInfo;

    public CooksListAdapter(Context context, SortTagInfo sortTagInfo) {
        this.context = context;
        this.sortTagInfo = sortTagInfo;
    }

    @Override
    public int getCount() {
        return sortTagInfo.getResult().size();
    }

    @Override
    public Object getItem(int position) {
        return sortTagInfo.getResult().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListHolder listHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cooks,null);
            listHolder = new ListHolder();
            listHolder.mLetterCooksTv = (TextView) convertView.findViewById(R.id.tvLetter_item_books);
            listHolder.mGridView = (GridView) convertView.findViewById(R.id.show_books_grid_view);

            //给控件设置字体
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),"jianxiyuan.ttf");
            listHolder.mLetterCooksTv.setTypeface(typeface);

            //GridView点击事件
            listHolder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int parentPosition = (int) listHolder.mGridView.getTag();
                    Intent intent = new Intent(context, ShowCookActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", Integer.parseInt(sortTagInfo.getResult().get(parentPosition).getList().get(position).getId()));
                    bundle.putString("title",sortTagInfo.getResult().get(parentPosition).getList().get(position).getName());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            convertView.setTag(listHolder);
        }else {
            listHolder = (ListHolder) convertView.getTag();
        }

        //为控件赋值
        listHolder.mLetterCooksTv.setText(sortTagInfo.getResult().get(position).getName());
        listHolder.mGridView.setAdapter(new CooksGridAdapter(sortTagInfo.getResult().get(position)));
        listHolder.mGridView.setTag(position);

        return convertView;
    }

    class ListHolder{
        TextView mLetterCooksTv;
        GridView mGridView;
    }

    private class CooksGridAdapter extends BaseAdapter{

        private SortTagInfo.Result result;

        public CooksGridAdapter(SortTagInfo.Result result) {
            this.result = result;
        }

        @Override
        public int getCount() {
            return result.getList().size();
        }

        @Override
        public Object getItem(int position) {
            return result.getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridHolder gridHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview,null);
                gridHolder = new GridHolder();
                gridHolder.mItemGridTv = (TextView) convertView.findViewById(R.id.item_grid_txt);

                convertView.setTag(gridHolder);
            }else {
                gridHolder = (GridHolder) convertView.getTag();
            }

            //为TextView赋值
            gridHolder.mItemGridTv.setText(result.getList().get(position).getName());
            return convertView;
        }

        class GridHolder{
            TextView mItemGridTv;
        }
    }


}
