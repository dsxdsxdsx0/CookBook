package com.example.dsxdsxdsx0.cookbook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsxdsxdsx0.cookbook.R;

import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/8/16.
 *
 * 菜单适配器
 */
public class MainMenuAdapter extends BaseAdapter {

    private List<String> list;

    private int[] imgIds = {R.drawable.recipe_lable_4_2, R.drawable.recipe_lable_5_3, R.drawable.recipe_lable_1_1,
            R.drawable.recipe_lable_5_1, R.drawable.recipe_lable_2_3, R.drawable.recipe_lable_4_1,
            R.drawable.recipe_lable_3_2, R.drawable.recipe_lable_3_1, R.drawable.recipe_lable_1_3};

    public MainMenuAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu,null);
            holder = new ViewHolder();
            holder.mMenuIcon = (ImageView) convertView.findViewById(R.id.gridview_item_img);
            holder.mMenuName = (TextView) convertView.findViewById(R.id.gridview_item_tv);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //为控件赋值
        holder.mMenuIcon.setImageResource(imgIds[position]);
        holder.mMenuName.setText(list.get(position));

        return convertView;
    }

    class ViewHolder{
        ImageView mMenuIcon;
        TextView mMenuName;
    }
}
