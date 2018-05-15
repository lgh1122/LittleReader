package com.liuguanghui.littlereader.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liuguanghui.littlereader.pojo.NovelInfo;
import com.liuguanghui.littlereader.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作black_number表的DAO类
 *
 */
public class NovelInfoDao {

	private DBHelper dbHelper;

	public NovelInfoDao(Context context) {
		dbHelper = new DBHelper(context);
	}
	/**
	 * 添加一条记录
	 * @param novelInfo
	 */
	public void add(NovelInfo novelInfo) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行insert insert into black_number (number) values(xxx)
		ContentValues values = new ContentValues();
		values.put("novelName", novelInfo.getNovelName());
		values.put("chapter", novelInfo.getChapter());
		//SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("readDate", novelInfo.getReadDate());

		long id = database.insert("tbNovelInfo", null, values);
		Log.i("TAG", "id="+id);

		//设置id
		novelInfo.setId((int) id);
		//3. 关闭
		database.close();
	}

	/**
	 * 根据id删除一条记录
	 */
	public void deleteById(int id) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行delete delete from black_number where _id=id
		int deleteCount = database.delete("tbNovelInfo", "_id=?", new String[]{id+""});
		Log.i("TAG", "deleteCount="+deleteCount);
		//3. 关闭
		database.close();
	}

	/**
	 * 更新一条记录
	 */
	public void update(NovelInfo novelInfo) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行update update black_number set number=xxx where _id=id
		ContentValues values = new ContentValues();
		if(novelInfo.getReadDate()!= 0 ){
			values.put("readDate", novelInfo.getReadDate());
		}
		values.put("isTop", novelInfo.getIsTop());
		int updateCount = database.update("tbNovelInfo", values , "_id="+novelInfo.getId(), null);
		Log.i("TAG", "updateCount="+updateCount);
		//3. 关闭
		database.close();
	}


	/**
	 * 查询所有记录封装成List<BLackNumber>
	 */
	public List<NovelInfo> getAll() {

		List<NovelInfo> list = new ArrayList<NovelInfo>();
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行query select * from black_number
		String[] cloumns  = {"_id" , "novelName", "chapter", "image", "isTop", "readDate", "addBook"};

		Cursor cursor = database.query("tbNovelInfo", cloumns, null, null, null,
				null, "isTop desc , readDate desc ");
		//3. 从cursor中取出所有数据并封装到List中
		while(cursor.moveToNext()) {
			NovelInfo novel = new NovelInfo();
			novel.setId(cursor.getInt(0));
			novel.setNovelName(cursor.getString(1));
			novel.setChapter(cursor.getString(2));
			novel.setImage(cursor.getInt(3));
			novel.setIsTop(cursor.getInt(4));
			novel.setReadDate(cursor.getLong(5));
			novel.setAddBook(cursor.getInt(6));
			list.add(novel);
		}
		//4. 关闭
		cursor.close();
		database.close();

		return list;
	}
}
