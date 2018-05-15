package com.liuguanghui.littlereader.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.liuguanghui.littlereader.R;

/**
 * 数据库连接帮助类
 * @author liuguanghui
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "novelList.db", null, 1);
	}

	/**
	 * 在数据库文件不存在，
	 * 连接数据库时调用 
	 * 可以初始化数据
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("TAG", "DBHelper onCreate " );
		//建表
		String sql = "create table tbNovelInfo(_id integer primary key autoincrement , " +
				"novelName varchar ," +
				"image int ," +
				"chapter varchar, " +
				"isTop int , " +
				"topDate INTEGER ," +
				"readDate INTEGER ," +
				"addBook int  )	";
		db.execSQL(sql);
		//插入数据
		db.execSQL("insert into tbNovelInfo(image, novelName ,chapter ,isTop ) values ("+R.mipmap.commonnovelimg+",'时空干涉手册','完本感言',0)");
		db.execSQL("insert into tbNovelInfo(image, novelName ,chapter ,isTop ) values ("+R.mipmap.commonnovelimg+",'我是杀毒软件','第123章：极致隐身',0)");
		db.execSQL("insert into tbNovelInfo(image, novelName ,chapter ,isTop ) values ("+R.mipmap.mydz+",'麻衣道者','猴儿酒',0)");
		db.execSQL("insert into tbNovelInfo(image, novelName ,chapter ,isTop ) values ("+R.mipmap.commonnovelimg+",'冥主','第321章：圣人有情',0)");
		db.execSQL("insert into tbNovelInfo(image, novelName ,chapter ,isTop ) values ("+R.mipmap.commonnovelimg+",'氪金魔主','青龙不死身',0)");
		db.execSQL("insert into tbNovelInfo(image, novelName ,chapter ,isTop ) values ("+R.mipmap.hdxsx+",'花都小神仙','大结局',0)");

	}

	/**
	 * 当传入的版本号大于当前版本号时调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("TAG", "DBHelper onUpgrade " );
		
	}

}
