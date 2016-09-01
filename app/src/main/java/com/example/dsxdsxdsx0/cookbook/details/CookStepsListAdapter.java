package com.example.dsxdsxdsx0.cookbook.details;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.info.ShowCookersInfo;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/8/31.
 */
public class CookStepsListAdapter extends BaseAdapter {

    private Context context;

    private List<ShowCookersInfo.Result.Data.Steps> stepList;
    private BitmapUtils bitmapUtils;


    public CookStepsListAdapter(List<ShowCookersInfo.Result.Data.Steps> stepList, Context context) {
        this.context = context;
        this.stepList = stepList;
        this.bitmapUtils = new BitmapUtils(context, Environment.getExternalStorageDirectory().getPath()+ "/cooks/img");
    }

    @Override
    public int getCount() {
        return stepList.size();
    }

    @Override
    public ShowCookersInfo.Result.Data.Steps getItem(int position) {
        return stepList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cook_steps,null);
            holder.mStepNumTv = (TextView) convertView.findViewById(R.id.step_num);
            holder.mStepPicIv = (ImageView) convertView.findViewById(R.id.img);
            holder.mStepIntroTv = (TextView) convertView.findViewById(R.id.intro_tv);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //为控件赋值
        ShowCookersInfo.Result.Data.Steps cookStep = getItem(position);
        holder.mStepNumTv.setText(position + 1 + "");
        bitmapUtils.display(holder.mStepPicIv, cookStep.getImg());
        holder.mStepIntroTv.setText(cookStep.getStep().substring(2));

        return convertView;
    }

    class ViewHolder{
        TextView mStepNumTv;
        ImageView mStepPicIv;
        TextView mStepIntroTv;
    }
}
