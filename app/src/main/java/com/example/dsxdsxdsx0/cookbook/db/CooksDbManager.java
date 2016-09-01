package com.example.dsxdsxdsx0.cookbook.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dsxdsxdsx0.cookbook.info.ShowCookersInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaJi on 2015/12/15.
 */
public class CooksDbManager {
    private SQLiteDatabase db;
    private ShowCookersInfo.Result.Data data;
    private static CooksDbManager cooksDBManager;

    private CooksDbManager(Context context) {
        CookHelper helper = new CookHelper(context);
        db = helper.getWritableDatabase();
    }

    //单例模式,数据库管理器初始化
    public static CooksDbManager getCooksDBManager(Context context) {
        if (cooksDBManager == null) {
            cooksDBManager = new CooksDbManager(context);
        }
        return cooksDBManager;
    }

    /**
     * 增添数据
     */
    public void insertData(ShowCookersInfo.Result.Data data) {
        Cursor cursor = db.rawQuery("select * from " + CookHelper.TABLE_TEXT + " where " + CookHelper.ID + "=" + data.getId(),null);
        if (cursor.getCount() == 1) {
            cursor.close();
            db.execSQL("update " + CookHelper.TABLE_TEXT + " set " + CookHelper.HISTORY_LOOK + " = " + 1 + " where " + CookHelper.ID + "=" + data.getId());
            return;
        }
        cursor.close();
        String textSql = "insert into " + CookHelper.TABLE_TEXT + "(" + CookHelper.ID + "," + CookHelper.TITLE + "," +
                CookHelper.TAGS + "," + CookHelper.IMTRO + "," + CookHelper.INGREDIENTS + "," + CookHelper.BURDEN + "," +
                CookHelper.ALBUMS + "," +
                CookHelper.HISTORY_LOOK + ")" +
                " values('" + data.getId() + "','" + data.getTitle() + "','" + data.getTags() + "','" + data.getImtro() + "','" + data.getIngredients()
                + "','" + data.getBurden() + "','" + data.getAlbums().get(0) + "',1)";
        db.execSQL(textSql);
        //
        for (int i = 0, length = data.getStep().size(); i < length; i++) {
            String imgsSql = "insert into " + CookHelper.TABLE_IMGS + "(" + CookHelper.ID + "," + CookHelper.IMG + "," + CookHelper.STEP + ")"
                    + " values('" + data.getId() + "','" +
                    data.getStep().get(i).getImg() + "','" + data.getStep().get(i).getStep() + "')";
            db.execSQL(imgsSql);
        }
    }

    /**
     * 删除数据
     *
     * @param data
     */
    public void delData(ShowCookersInfo.Result.Data data) {
        if (data == null) {//如果没有数据，先通过查询语句遍历数据
            /**
             * SQLiteDatabase的rawQuery() 用于执行select语句
             * 使用例子如下：
             * SQLiteDatabase db = null;
             * Cursor cursor = db.rawQuery(“select * from person”, null);
             */
            Cursor cursor = db.rawQuery("select * from " + CookHelper.TABLE_TEXT, null);
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(CookHelper.HISTORY_LOOK)) == 1)//删除浏览历史的数据
                {
                    if (cursor.getInt(cursor.getColumnIndex(CookHelper.MY_LIKE)) == 1){

                        db.execSQL(" update  "+ CookHelper.TABLE_TEXT+" set "+ CookHelper.HISTORY_LOOK+" =0"
                                +" where "+ CookHelper.ID+" = "+cursor.getInt(cursor.getColumnIndex(CookHelper.ID)));

                        continue;
                    }
                    String textSql = "delete from " + CookHelper.TABLE_TEXT + " where "
                            + CookHelper.ID + "=" + cursor.getInt(cursor.getColumnIndex(CookHelper.ID));
                    db.execSQL(textSql);
                    String imgsSql = "delete from " + CookHelper.TABLE_IMGS + " where "
                            + CookHelper.ID + "=" + cursor.getInt(cursor.getColumnIndex(CookHelper.ID));
                    db.execSQL(imgsSql);
                }
            }
            cursor.close();
        } else {
            String textSql = "delete from " + CookHelper.TABLE_TEXT + " where "
                    + CookHelper.ID + "=" + data.getId();
            db.execSQL(textSql);
            String imgsSql = "delete from " + CookHelper.TABLE_IMGS + " where "
                    + CookHelper.ID + "=" + data.getId();
            db.execSQL(imgsSql);
        }
    }

    /**
     * 修改数据，default 0（不喜欢） default 1 （喜欢）
     *
     * @param data
     * @param isLike 是否收藏
     */
    public void updateData(ShowCookersInfo.Result.Data data, boolean isLike) {
        String textSql = "update " + CookHelper.TABLE_TEXT + " set "
                + CookHelper.MY_LIKE + "='" + (isLike ? 1 : 0) + "' where " + CookHelper.ID + "=" + data.getId();
        db.execSQL(textSql);
    }

    public ShowCookersInfo getData(boolean isHistory, boolean isLike) {
        ShowCookersInfo info = new ShowCookersInfo();
        String textSql;
        if (isHistory) {
            textSql = "select * from " + CookHelper.TABLE_TEXT + " where " + CookHelper.HISTORY_LOOK + " = " + 1;

        } else {
            textSql = "select * from " + CookHelper.TABLE_TEXT + " where " + CookHelper.MY_LIKE + "=" + (isLike ? 1 : 0);
        }

        Cursor textCursor = db.rawQuery(textSql, null);
        ShowCookersInfo.Result result = new ShowCookersInfo.Result();
        info.setResult(result);
        List<ShowCookersInfo.Result.Data> datas = new ArrayList<>();
        result.setData(datas);
        ShowCookersInfo.Result.Data data;
        while (textCursor.moveToNext()) {
            data = new ShowCookersInfo.Result.Data();
            data.setId(textCursor.getString(textCursor.getColumnIndex(CookHelper.ID)));
            data.setTitle(textCursor.getString(textCursor.getColumnIndex(CookHelper.TITLE)));
            data.setTags(textCursor.getString(textCursor.getColumnIndex(CookHelper.TAGS)));
            data.setImtro(textCursor.getString(textCursor.getColumnIndex(CookHelper.IMTRO)));
            data.setIngredients(textCursor.getString(textCursor.getColumnIndex(CookHelper.INGREDIENTS)));
            data.setBurden(textCursor.getString(textCursor.getColumnIndex(CookHelper.BURDEN)));
            List<String> albums = new ArrayList<>();
            data.setAlbums(albums);
            albums.add(textCursor.getString(textCursor.getColumnIndex(CookHelper.ALBUMS)));

            List<ShowCookersInfo.Result.Data.Steps> stepses = new ArrayList<>();
            data.setStep(stepses);
            ShowCookersInfo.Result.Data.Steps steps;
            String imgsSql = "select * from " + CookHelper.TABLE_IMGS + " where " + CookHelper.ID + "="
                    + textCursor.getInt(textCursor.getColumnIndex(CookHelper.ID));
            Cursor imgsCursor = db.rawQuery(imgsSql, null);
            while (imgsCursor.moveToNext()) {
                steps = new ShowCookersInfo.Result.Data.Steps();
                steps.setImg(imgsCursor.getString(imgsCursor.getColumnIndex(CookHelper.IMG)));
                steps.setStep(imgsCursor.getString(imgsCursor.getColumnIndex(CookHelper.STEP)));
                stepses.add(steps);
            }
            datas.add(data);
            imgsCursor.close();
        }
        textCursor.close();
        return info;
    }

    /**
     * 当前id的菜谱是否是添加了收藏
     *
     * @param id
     * @return
     */
    public boolean isLikeNowCook(String id) {
        boolean isLike;
        Cursor cursor = db.rawQuery("select " + CookHelper.MY_LIKE + " from " + CookHelper.TABLE_TEXT
                + " where " + CookHelper.ID + "=" + id, null);
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
