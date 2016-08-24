package com.example.dsxdsxdsx0.cookbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dsxdsxdsx0 on 2016/8/16.
 *
 * 创建菜谱数据库
 */
public class CookHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cook.db";

    private static final int VERSION = 1;

    /*
     * 是否添加了收藏 : 0 否；1 是
     */
    public static final String MY_LIKE = "my_like";

    //是否有观看历史
    public static final String HISTORY_LOOK ="history_look";

    //表名
    public static final String TABLE_TEXT = "cooks_info";

    //id
    public static final String ID = "_id";

    //标题
    public static final String TITLE = "title";

    //关于菜的功效
    public static final String TAGS = "tags";

    //菜品的来历
    public static final String IMTRO = "imtro";

    //菜品所需材料
    public static final String INGREDIENTS = "ingredients";

    //原材料
    public static final String BURDEN = "burden";

    //图片
    public static final String ALBUMS = "albums";

    //创建菜谱表
    public static final String CREATE_TEXT_TABLE = "create table " + TABLE_TEXT + "(" + ID + "integer primary key,"
            + TITLE + "," + TAGS + "," + IMTRO + "," + INGREDIENTS + "," + BURDEN + "," + ALBUMS + ","
            + MY_LIKE + "integer default 0," + HISTORY_LOOK + "integer default 0)";

    //创建图片table
    public static final String TABLE_IMGS = "cooks_imgs";

    public static final String IMGS = "imgs";

    public static final String STEP = "step";

    public static final String CREATE_IMGS_TABLE = "create table " + TABLE_IMGS + "("
            + ID + "integer primary key," + IMGS + "," + STEP + ")";

    public CookHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEXT_TABLE);
        db.execSQL(CREATE_IMGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CREATE_TEXT_TABLE,CREATE_IMGS_TABLE");
        onCreate(db);
    }
}
