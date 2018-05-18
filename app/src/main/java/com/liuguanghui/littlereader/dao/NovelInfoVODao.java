package com.liuguanghui.littlereader.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liuguanghui.littlereader.pojo.NovelVO;
import com.liuguanghui.littlereader.util.DBNovelVOHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 操作black_number表的DAO类
 *
 */
public class NovelInfoVODao {

	private DBNovelVOHelper dbHelper;

	public NovelInfoVODao(Context context) {
		dbHelper = new DBNovelVOHelper(context);
	}
	/**
	 * 添加一条记录
	 * @param novelInfo
	 */
	public void add(NovelVO novelInfo) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行insert insert into black_number (number) values(xxx)
		ContentValues values = new ContentValues();
		values.put("id", novelInfo.getId());
		values.put("netid", novelInfo.getNetid());
		values.put("title", novelInfo.getTitle());
		values.put("imgpath", novelInfo.getImgpath());
		values.put("author", novelInfo.getAuthor());
		values.put("tname", novelInfo.getTname());
		values.put("tid", novelInfo.getTid());
		values.put("netUrl", novelInfo.getNetUrl());
		values.put("introduction", novelInfo.getIntroduction());
		values.put("latestchapterid", novelInfo.getLatestchapterid());
		values.put("latestchaptername", novelInfo.getLatestchaptername());
		values.put("updatetime", novelInfo.getUpdatetime().getTime());
		values.put("status", novelInfo.getStatus());
		//SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("readDate", novelInfo.getReadDate());

		long id = database.insert("tbNovelInfo", null, values);
		Log.i("TAG", "id="+id);
		//3. 关闭
		database.close();
	}

	/**
	 * 根据id删除一条记录
	 */
	public void deleteById(NovelVO novelVO) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行delete delete from black_number where _id=id
		int deleteCount = database.delete("tbNovelInfo", "id=? and netid = ? ",
				new String[]{novelVO.getId()+"", novelVO.getNetid()+""});
		Log.i("TAG", "deleteCount="+deleteCount);
		//3. 关闭
		database.close();
	}

	/**
	 * 更新一条记录
	 * 阅读时间 和 是否置顶
	 */
	public void update(NovelVO novelInfo) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行update update black_number set number=xxx where _id=id
		ContentValues values = new ContentValues();
		if(novelInfo.getReadDate()!= 0 ){
			values.put("readDate", novelInfo.getReadDate());
		}
		values.put("isTop", novelInfo.getIsTop());
	    values.put("latestchapterid", novelInfo.getLatestchapterid());
		values.put("latestchaptername", novelInfo.getLatestchaptername());


		int updateCount = database.update("tbNovelInfo", values ,
				" id="+novelInfo.getId() + " and netid =" +novelInfo.getNetid(), null);
		Log.i("TAG", "updateCount="+updateCount);
		//3. 关闭
		database.close();
	}


	public NovelVO getNovelVO(long netId ,long novelId){
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行query select * from black_number
		Cursor cursor = database.query("tbNovelInfo", null, " id="+novelId + " and netid =" +netId, null, null,
				null, "isTop desc , readDate desc ");
		//3. 从cursor中取出所有数据并封装到List中
		NovelVO novel = null;
		while(cursor.moveToNext()) {
			novel = new NovelVO();
			novel.setId(cursor.getLong(cursor.getColumnIndex("id")));
			novel.setNetid(cursor.getLong(cursor.getColumnIndex("netid")));
			novel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			novel.setTname(cursor.getString(cursor.getColumnIndex("tname")));
			novel.setTid(cursor.getLong(cursor.getColumnIndex("tid")));
			novel.setLatestchapterid(cursor.getLong(cursor.getColumnIndex("latestchapterid")));
			novel.setLatestchaptername(cursor.getString(cursor.getColumnIndex("latestchaptername")));
			novel.setNetUrl(cursor.getString(cursor.getColumnIndex("netUrl")));
			novel.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
			Long l = cursor.getLong(cursor.getColumnIndex("updatetime"));
			Date  d = new Date();
			d.setTime(l);
			novel.setUpdatetime(d);
			novel.setStatus((byte) cursor.getInt(cursor.getColumnIndex("status")));
			novel.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
			novel.setImgpath(cursor.getString(cursor.getColumnIndex("imgpath")));
			novel.setIsTop(cursor.getInt(cursor.getColumnIndex("isTop")));
			novel.setReadDate(cursor.getLong(cursor.getColumnIndex("readDate")));
			break;
		}
		//4. 关闭
		cursor.close();
		database.close();
		return novel;
	}

	/**
	 * 查询所有记录封装成List<BLackNumber>
	 */
	public List<NovelVO> getAll() {

		List<NovelVO> list = new ArrayList<NovelVO>();
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行query select * from black_number
		//String[] cloumns  = {"id" , "novelName", "chapter", "image", "isTop", "readDate", "addBook"};

		Cursor cursor = database.query("tbNovelInfo", null, null, null, null,
				null, "isTop desc , readDate desc ");
		//3. 从cursor中取出所有数据并封装到List中
		while(cursor.moveToNext()) {
			NovelVO novel = new NovelVO();
			novel.setId(cursor.getLong(cursor.getColumnIndex("id")));
			novel.setNetid(cursor.getLong(cursor.getColumnIndex("netid")));
			novel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			novel.setTname(cursor.getString(cursor.getColumnIndex("tname")));
			novel.setTid(cursor.getLong(cursor.getColumnIndex("tid")));
			novel.setLatestchapterid(cursor.getLong(cursor.getColumnIndex("latestchapterid")));
			novel.setLatestchaptername(cursor.getString(cursor.getColumnIndex("latestchaptername")));
			novel.setNetUrl(cursor.getString(cursor.getColumnIndex("netUrl")));
			novel.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
			Long l = cursor.getLong(cursor.getColumnIndex("updatetime"));
			Date  d = new Date();
			d.setTime(l);
			novel.setUpdatetime(d);
			novel.setStatus((byte) cursor.getInt(cursor.getColumnIndex("status")));
			novel.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
			novel.setImgpath(cursor.getString(cursor.getColumnIndex("imgpath")));
			novel.setIsTop(cursor.getInt(cursor.getColumnIndex("isTop")));
			novel.setReadDate(cursor.getLong(cursor.getColumnIndex("readDate")));

			list.add(novel);
		}
		//4. 关闭
		cursor.close();
		database.close();

		return list;
	}
}
