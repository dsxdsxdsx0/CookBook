package com.example.dsxdsxdsx0.cookbook.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dsxdsxdsx0.cookbook.info.ShowCookersInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/8/16.
 *
 * 数据库管理器
 */
public class CooksDbManager {

    private Context mContext;

    private SQLiteDatabase db;

    private ShowCookersInfo.Result.Data data;

    private static CooksDbManager cooksDbManager;

    public CooksDbManager(Context context) {
        this.mContext = context;
        CookHelper cookHelper = new CookHelper(mContext);
        db = cookHelper.getWritableDatabase();
    }

    public static CooksDbManager getCooksDbManager(Context context){
        if(cooksDbManager == null){
            cooksDbManager = new CooksDbManager(context);
        }
        return cooksDbManager;
    }

    /**
     * 添加数据
     */
    public void insertData(ShowCookersInfo.Result.Data data){
        Cursor cursor = db.rawQuery("select * from " + CookHelper.TABLE_TEXT + " where " + CookHelper.ID + "=" + data.getId(),null);
        if(cursor.getCount() == 1){
            cursor.close();
            db.execSQL("update " + CookHelper.TABLE_TEXT + " set " + CookHelper.HISTORY_LOOK + " = " + "1"
                    + " where " + CookHelper.ID + "=" + data.getId());
            return;
        }
        cursor.close();
        String textSql = "insert into " + CookHelper.TABLE_TEXT +"(" + CookHelper.ID + "," + CookHelper.TITLE + ","
                + CookHelper.TAGS + "," + "," + CookHelper.IMTRO + "," + CookHelper.INGREDIENTS + "," + CookHelper.BURDEN + "," +
                CookHelper.ALBUMS + "," + CookHelper.HISTORY_LOOK + ")" +
                "values('" + data.getId() + "','" + data.getTitle() + "','" + data.getTags() + "','" + data.getImtro() + "','" + data.getIngredients()
                + "','" + data.getBurden() + "','" + data.getAlbums().get(0) + "',1)";
        db.execSQL(textSql);

        for (int i =0;i<data.getStep().size();i++){
            String imgSql = "insert into " + CookHelper.TABLE_IMGS + "(" + CookHelper.IMGS + "," + CookHelper.STEP + ")"
                    + "values('" + data.getStep().get(i).getImg() + "','" + data.getStep().get(i).getStep() + "')";
            db.execSQL(imgSql);
        }
    }

    /**
     *  删除数据
     * @param data
     */
    public void deleteData(ShowCookersInfo.Result.Data data){

        if (data == null) {
            /**
             * SQLiteDatabase的rawQuery() 用于执行select语句
             * 使用例子如下：
             * SQLiteDatabase db = null;
             * Cursor cursor = db.rawQuery(“select * from person”, null);
             */
            Cursor cursor = db.rawQuery("select * from " + CookHelper.TABLE_TEXT,null);
            while (cursor.moveToNext()){

                //cursor.getColumnIndex(String columnName)返回指定列的名称，如果不存在返回-1
                int historyLook = cursor.getColumnIndex(CookHelper.HISTORY_LOOK);//删除浏览历史的数据
                //cusor.getInt(int columnIndex) : 返回指定列数的value作为一个int值
                if(cursor.getInt(historyLook) == 1){//有历史数据
                    int myLike = cursor.getColumnIndex(CookHelper.MY_LIKE);//获取我的收藏的value值，0是无收藏，1是有收藏
                    if(cursor.getInt(myLike) == 1){//有收藏数据
                        //更新表，将表中数据根据id值设置历史记录为0
                        String sql = "update " + CookHelper.TABLE_TEXT + " set " + CookHelper.HISTORY_LOOK + " = 0"
                                + " where " + CookHelper.ID + "=" + cursor.getInt(cursor.getColumnIndex(CookHelper.ID));
                        db.execSQL(sql);

//                        continue;
                    }
                    //执行删除操作
                    String txtSql = "delete from " + CookHelper.TABLE_TEXT + " where " + CookHelper.ID + "="
                            + cursor.getInt(cursor.getColumnIndex(CookHelper.ID));
                    db.execSQL(txtSql);

                    String imgSql = "delete from " + CookHelper.TABLE_IMGS + " where " + CookHelper.ID + "="
                            + cursor.getInt(cursor.getColumnIndex(CookHelper.ID));
                    db.execSQL(imgSql);
                }
                cursor.close();
            }
        }else {
            String txtSql = "delete from " + CookHelper.TABLE_TEXT + " where " + CookHelper.ID + " = "
                    + data.getId();
            db.execSQL(txtSql);

            String imgSql = "delete from " + CookHelper.TABLE_IMGS + " where " + CookHelper.ID + " = "
                    + data.getId();
            db.execSQL(imgSql);
        }
    }


    /**
     *  更改数据
     * @param data
     * @param isLike 是否收藏
     */
    public void updateData(ShowCookersInfo.Result.Data data,boolean isLike){
        String txtSql = "update " + CookHelper.TABLE_TEXT + " set " + CookHelper.MY_LIKE +
                "='" + (isLike ? 1 : 0) + "' where " + CookHelper.ID + "=" + data.getId();
        db.execSQL(txtSql);
    }

    /**
     * 获取数据
     * @param isHistory
     * @param isLike
     * @return
     */
    public ShowCookersInfo getData(boolean isHistory,boolean isLike){

        ShowCookersInfo showCookersInfo = new ShowCookersInfo();
        String txtSql = "";
        if (isHistory) {
            txtSql = "select * from " + CookHelper.TABLE_TEXT + " where " + CookHelper.HISTORY_LOOK + " = " + 1;
        }else {
            txtSql = "select * from " + CookHelper.TABLE_TEXT + " where " + CookHelper.MY_LIKE + " = " + (isLike ? 1 : 0);
        }

        Cursor cursor = db.rawQuery(txtSql,null);
        ShowCookersInfo.Result result = new ShowCookersInfo.Result();
        showCookersInfo.setResult(result);

        List<ShowCookersInfo.Result.Data> datas = new ArrayList<>();
        result.setData(datas);

        ShowCookersInfo.Result.Data data = null;

        while (cursor.moveToNext()){
            data = new ShowCookersInfo.Result.Data();
            data.setId(cursor.getString(cursor.getColumnIndex(CookHelper.ID)));
            data.setTitle(cursor.getString(cursor.getColumnIndex(CookHelper.TITLE)));
            data.setTags(cursor.getString(cursor.getColumnIndex(CookHelper.TAGS)));
            data.setImtro(cursor.getString(cursor.getColumnIndex(CookHelper.IMTRO)));
            data.setBurden(cursor.getString(cursor.getColumnIndex(CookHelper.BURDEN)));
            data.setIngredients(cursor.getString(cursor.getColumnIndex(CookHelper.INGREDIENTS)));

            List<String> album = new ArrayList<>();
            data.setAlbums(album);
            album.add(cursor.getString(cursor.getColumnIndex(CookHelper.ALBUMS)));

            //获取图片
            List<ShowCookersInfo.Result.Data.Step> stepList = new ArrayList<>();
            data.setStep(stepList);
            ShowCookersInfo.Result.Data.Step step = null;

            String imgSql = "select * from " + CookHelper.TABLE_IMGS + " where " + CookHelper.ID + " = "
                    + cursor.getInt(cursor.getColumnIndex(CookHelper.ID));
            Cursor imgCursor = db.rawQuery(imgSql,null);
            while (imgCursor.moveToNext()){
                step = new ShowCookersInfo.Result.Data.Step();
                step.setImg(imgCursor.getString(imgCursor.getColumnIndex(CookHelper.IMGS)));
                step.setStep(imgCursor.getString(imgCursor.getColumnIndex(CookHelper.STEP)));
                stepList.add(step);
            }
            datas.add(data);
            imgCursor.close();
        }
        cursor.close();
        return showCookersInfo;
    }

    /**
     * 当前id的菜谱是否是添加了收藏
     * @param id
     * @return
     */
    public boolean isLikeNowCook(String id){

        boolean isLike;
        Cursor cursor = db.rawQuery("select " + CookHelper.MY_LIKE + " from " + CookHelper.TABLE_TEXT +
                " where " + CookHelper.ID + " = " + id , null);
        cursor.moveToNext();
        isLike = cursor.getInt(0) == 1;
        cursor.close();
        return isLike;
    }

    public ShowCookersInfo.Result.Data getData() {
        return data;
    }

    public void setData(ShowCookersInfo.Result.Data data) {
        this.data = data;
    }
}
