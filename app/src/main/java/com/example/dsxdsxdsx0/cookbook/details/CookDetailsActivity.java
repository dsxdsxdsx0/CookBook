package com.example.dsxdsxdsx0.cookbook.details;

import android.app.AlertDialog;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.db.CooksDbManager;
import com.example.dsxdsxdsx0.cookbook.info.ShowCookersInfo;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.w3c.dom.Text;

/**
 *  菜单详情页
 * Created by dsxdsxdsx0 on 2016/8/30.
 */
@ContentView(R.layout.activity_cook_detail)
public class CookDetailsActivity extends BaseActivity {

    @ViewInject(R.id.header_img)
    private ImageView mHeadImg;

    @ViewInject(R.id.name)
    private TextView mNameTv;

    @ViewInject(R.id.share_tv)
    private TextView mShareTv;//分享

    @ViewInject(R.id.collect_tv_l)
    private CheckedTextView mCollectCTv;//收藏

    @ViewInject(R.id.text_intro)
    private TextView mIntroduceTv;//简介

    @ViewInject(R.id.materials_layout)
    private LinearLayout mMaterialsLayout;//制作步骤布局

    @ViewInject(R.id.listView)
    private ListView mListView;

    @ViewInject(R.id.details_title)
    private Toolbar mDetailsTitleBar;//标题

    private ShowCookersInfo.Result.Data data;

    private BitmapUtils bitmapUtils;//位图

    private CookStepsListAdapter cookStepsListAdapter;

    //点击收藏事件
    @OnClick(R.id.collect_tv_l)
    private void collectClick(View view){
        mCollectCTv.toggle();
        CooksDbManager.getCooksDBManager(CookDetailsActivity.this).updateData(data,mCollectCTv.isChecked());
    }

    //点击分享事件
    @OnClick(R.id.share_tv)
    private void shareClick(View view){

    }




    @Override
    public void initBefore() {
        //从数据库中获取数据
        data = CooksDbManager.getCooksDBManager(this).getData();
        mDetailsTitleBar.setNavigationIcon(R.drawable.ic_chevron_left_24dp1);
        mDetailsTitleBar.setTitle(data.getTitle());
        setSupportActionBar(mDetailsTitleBar);
        mDetailsTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookDetailsActivity.this.finish();
                overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
            }
        });
        bitmapUtils = new BitmapUtils(CookDetailsActivity.this, Environment.getExternalStorageDirectory().getPath() + "/cooks/img");
    }

    private View view;
    private TextView mDialogNum;
    private TextView mDialogIntro;
    private ImageView mDialogImg;

    @Override
    public void init() {
        view = LayoutInflater.from(CookDetailsActivity.this).inflate(R.layout.dialog_detail_layout,null);

        bitmapUtils.display(mHeadImg, data.getAlbums().get(0));
        mNameTv.setText(data.getTitle());
        mIntroduceTv.setText(data.getImtro());
        mCollectCTv.setChecked(CooksDbManager.getCooksDBManager(CookDetailsActivity.this).isLikeNowCook(data.getId()));

        //添加制作方法
        addFood();
        cookStepsListAdapter = new CookStepsListAdapter(data.getStep(),this);
        mListView.setAdapter(cookStepsListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowCookersInfo.Result.Data.Steps step = cookStepsListAdapter.getItem(position);
                ShowAlertDialog("第" + (position+1) + "页",step.getImg(),step.getStep());
            }
        });


    }

    //添加制作方法
    private void addFood() {
        //食材明细
        String ingredients = data.getIngredients();//主食材
        String burden = data.getBurden();//副食材
        String materialStr = ingredients + ";" + burden;
        String []split = materialStr.split(";");

        //每行放两个，计算需要多少行
        int lines = (split.length % 2) == 0 ? (split.length) / 2 : (split.length) / 2 + 1;
        for (int i = 0; i < lines; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_material,null);
            mMaterialsLayout.addView(view);

            String []texts = split[i * 2].split(",");
            TextView tv1 = (TextView) findViewById(R.id.text1);
            tv1.setText(texts[0]);
            TextView tv2 = (TextView) view.findViewById(R.id.text2);
            tv2.setText(texts[1]);

            if(i == lines -1 &&split.length % 2 != 0){
                continue;
            }

            texts = split[i * 2 + 1].split(",");
            TextView tv3 = (TextView) findViewById(R.id.text3);
            tv3.setText(texts[0]);
            TextView tv4 = (TextView) findViewById(R.id.text4);
            tv4.setText(texts[1]);

            texts = null;

        }
    }

    private AlertDialog dialog;

    private void ShowAlertDialog(String num,String imgUrl,String intro){
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).create();
            dialog.show();
            dialog.setContentView(view);

            mDialogNum = (TextView) findViewById(R.id.dialog_number);
            mDialogImg = (ImageView) findViewById(R.id.dialog_img);
            mDialogIntro = (TextView) findViewById(R.id.dialog_tro);
        }

        if (dialog != null) {
            mDialogNum.setText(num);
            mDialogIntro.setText(intro);
            bitmapUtils.display(mDialogImg, imgUrl);
            dialog.show();
        }
    }

    @Override
    public void initAfter() {

    }
}
