package com.liuguanghui.littlereader.dao;

/**
 * 操作tbNovelSearchHistory表的DAO类
 */
public class SearchHistoryDao {

	/*private DBNovelVOHelper dbHelper;

	public SearchHistoryDao(Context context) {
		dbHelper = new DBNovelVOHelper(context);
	}
	*//**
	 * 添加一条记录
	 * @param searchStr
	 *//*
	public void add(String searchStr) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行insert insert into black_number (number) values(xxx)
		ContentValues values = new ContentValues();
		values.put("id", SystemClock.currentThreadTimeMillis());
		values.put("keyword", searchStr);
		long id = database.insert("tbNovelSearchHistory", null, values);
		Log.i("TAG", "id="+id);
		//3. 关闭
		database.close();
	}

	*//**
	 * 清空搜索历史
	 *//*
	public void deleteAll() {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行delete delete from black_number where _id=id
		int deleteCount = database.delete("tbNovelSearchHistory", null,
				null);
		Log.i("TAG", "deleteAll="+deleteCount);
		//3. 关闭
		database.close();
	}

	*//**
	 * 更新一条记录
	 * 阅读时间 和 是否置顶
	 *//*
	public void update(String str) {
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		//2. 执行update update black_number set number=xxx where _id=id
		ContentValues values = new ContentValues();
		values.put("id", SystemClock.currentThreadTimeMillis());
		int updateCount = database.update("tbNovelSearchHistory", values ,
				  "  keyword ='" +str+"'", null);
		Log.i("TAG", "updateCount="+updateCount);
		//3. 关闭
		database.close();
	}


	*//**
	 * 查询所有记录封装成List<BLackNumber>
	 *//*
	public  String getHistory(String str ) {


		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();

		Cursor cursor = database.query("tbNovelSearchHistory", null, "  keyword ='" +str+"'", null, null,
				null, null);
		//3. 从cursor中取出所有数据并封装到List中
		String keyword = null;
		while(cursor.moveToNext()) {
			keyword = cursor.getString(cursor.getColumnIndex("keyword"));
			break;
		}
		//4. 关闭
		cursor.close();
		database.close();

		return  keyword;
	}

	*//**
	 * 查询所有记录封装成List<BLackNumber>
	 *//*
	public List<String> getAll() {

		List<String> list = new ArrayList<String>();
		//1. 得到连接
		SQLiteDatabase database = dbHelper.getReadableDatabase();

		Cursor cursor = database.query("tbNovelSearchHistory", null, null, null, null,
				null, "id desc  ");
		//3. 从cursor中取出所有数据并封装到List中
		while(cursor.moveToNext()) {
			String keyword = cursor.getString(cursor.getColumnIndex("keyword"));
			list.add(keyword);
			if(list.size() >6 ){
				break;
			}
		}
		//4. 关闭
		cursor.close();
		database.close();

		return list;
	}*/
}
