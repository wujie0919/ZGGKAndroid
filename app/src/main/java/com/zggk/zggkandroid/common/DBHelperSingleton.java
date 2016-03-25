package com.zggk.zggkandroid.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zggk.zggkandroid.MainApplication;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class DBHelperSingleton {
	private static DBHelperSingleton dbHelper = null; // 静态私用成员，没有初始化
	private SQLiteDatabase mDB = null;
	private DataBaseHelper mBaseHelper = null;
	private static String PRIMARYKEY = "table_tid"; // 主键名称，为了不与类中自己定义的id产生冲突，请保持在类中不会出现此字段
	private static String DBASE_NAME = "ZGGK_DB";
	public static final int DBASE_VERSION = 1;
	// 含有3个线程的线程池
	private static final ExecutorService executorService = Executors
				.newFixedThreadPool(3);
	
	public interface DataBaseCallBack {
		public void callBack(Object result);
	}
	// 私有构造函数
	private DBHelperSingleton(Context context) {
		mBaseHelper = new DataBaseHelper(context, DBASE_NAME, null,
				DBASE_VERSION);
	}

	public synchronized static DBHelperSingleton getInstance() {
		if (dbHelper == null) {
			new MainApplication();
			dbHelper = new DBHelperSingleton(MainApplication.mContext);
		}
		return dbHelper;
	}

	/**
	 * 创建数据库表 主键为空的时候，默认生成主键，主键名PRIMARYKEY：table_tid
	 * 
	 * @param cClass
	 * @param primaryKey
	 * @return false 建表失败 true 建表成功
	 */
	public Boolean CreateTable(Class<?> cClass, String primaryKey) {
		// 当主键为空的时候
		String sql = getCreateTalbeSql(cClass, primaryKey);
		return dbQuery(sql);
	}

	/**
	 * 批量插入数据
	 * @param <E>
	 * @param object
	 * @param callBack
	 */
	public <E> void insertDataWithList(Object object,Boolean isReplace,
			final DataBaseCallBack callBack) {
		if (object.getClass()==ArrayList.class) {
			@SuppressWarnings("unchecked")
			List<E> objList=(List<E>)object;
			for (Iterator<E> iterator = objList.iterator(); iterator.hasNext();) {
				Object obj = (Object) iterator.next();
				if (isReplace) {
					insertOrReplaceData(obj);
				}else {
					insertData(obj);
				}
				
			}
			// 用于子线程与主线程通信的Handler
			final Handler mHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					// 将返回值回调到callBack的参数中
					callBack.callBack(msg.obj);
				}

			};

			// 开启线程去访问WebService
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					// 将获取的消息利用Handler发送到主线程
					mHandler.sendMessage(mHandler.obtainMessage(0,
							true));
				}
			});
		}else {
			callBack.callBack(false);
		}
	}
	
	/**
	 * 根据sql语句创建表
	 * 
	 * @param sql
	 * @return false 建表失败 true 建表成功
	 */
	public Boolean createTableWithSql(String sql) {
		return dbQuery(sql);
	}

	/**
	 * 插入数据
	 * 
	 * @param cls
	 */
	public void insertData(Object obj) {
		try {
			mDB = mBaseHelper.getWritableDatabase();
			ContentValues initialValues = new ContentValues();
			Field[] property_names = obj.getClass().getDeclaredFields();
			for (Field field : property_names) {
				field.setAccessible(true);
				String name = field.getName();
				if (name.equals(PRIMARYKEY)
						|| field.get(obj) == null
						|| field.getType().getSimpleName()
								.equals("RouteEntity")) {
					continue;
				}

				// field.get(obj).toString() 获取字段值
				initialValues.put(name, field.get(obj).toString());
			}
			mDB.insert(obj.getClass().getSimpleName(), null, initialValues);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.close();
		}

	}
	
	

	/**
	 * 插入或更新数据，适用于主键不是自增长的表
	 * 
	 * @param obj
	 */
	public void insertOrReplaceData(Object obj) {
		try {
			mDB = mBaseHelper.getWritableDatabase();
			Field[] property_names = obj.getClass().getDeclaredFields();
			StringBuffer buffer = new StringBuffer();
			StringBuffer buffer2 = new StringBuffer();
			buffer2.append(")VALUES(");
			buffer.append("INSERT OR REPLACE INTO " + getTableName(obj) + "(");
			for (Field field : property_names) {
				field.setAccessible(true);
				String name = field.getName();
				if (name.equals(PRIMARYKEY)
						|| field.get(obj) == null
						|| field.getType().getSimpleName()
								.equals("RouteEntity")) {
					continue;
				}
				buffer.append(name + ",");
				buffer2.append("'" + field.get(obj).toString() + "',");
			}
			String sql = "";
			if (buffer.length() > 0) {
				sql = buffer.substring(0, buffer.length() - 1);
			}
			if (buffer2.length() > 0) {
				sql = sql + buffer2.substring(0, buffer2.length() - 1) + ")";
			}
			dbQuery(sql);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.close();
		}
	}

	/**
	 * 自己写sql语句插入数据
	 * 
	 * @param sql
	 * @return
	 */
	public Boolean insertDataWithSql(String sql) {
		return dbQuery(sql);
	}

	/**
	 * 删除数据，如果object为null，则删除所有数据
	 * 
	 * @param cls
	 * @param conditions
	 *            例如“id=1” 类似这种条件
	 * @return
	 */
	public Boolean deleteData(Class<?> cls, String conditions) {

		String sql = "";
		if (TextUtils.isEmpty(conditions)) {
			sql = "DELETE FROM " + cls.getSimpleName();
		} else {
			sql = "DELETE FROM " + cls.getSimpleName() + " WHERE " + conditions;
		}
		return dbQuery(sql);
	}

	/**
	 * 自己写的删除sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public Boolean deleteDataWithSql(String sql) {
		return dbQuery(sql);
	}

	public <T> List<T> getData(String sql,Class<?> entity){
		List<T> objects = new ArrayList<T>();
		mDB = mBaseHelper.getWritableDatabase();
		Cursor cursor=mDB.rawQuery(sql,null);
		try {
			while (cursor.moveToNext()) {
				@SuppressWarnings("unchecked")
				T obj = (T) entity.newInstance();
				getEntity(cursor, obj);
				objects.add(obj);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			mDB.close();
		}
		return objects;
	}
	/**
	 * 查询所有数据
	 * 
	 * @param cls
	 * @return
	 */
	public <T> List<T> getData(Class<?> entity, String order) {
		List<T> objects = new ArrayList<T>();

		mDB = mBaseHelper.getWritableDatabase();
		Cursor cursor = mDB.query(entity.getSimpleName(), null, null, null,
				null, null, order);
		try {
			while (cursor.moveToNext()) {
				@SuppressWarnings("unchecked")
				T obj = (T) entity.newInstance();
				getEntity(cursor, obj);
				objects.add(obj);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			mDB.close();
		}
		return objects;
	}
	
	/**
	 * 获取多条记录，where条件必须含有
	 * 
	 * @param cls
	 * @param where
	 * @return
	 */
	public <T> List<T> getDatas(Class<?> cls, String where) {
		if ("".equals(where) || null == where) {
			return null;
		}
		List<T> objects = new ArrayList<T>();
		try {
			String sql = "SELECT * FROM " + cls.getSimpleName() + " WHERE "
					+ where;
			Constant.log(sql);
			mDB = mBaseHelper.getReadableDatabase();
			Cursor cursor = mDB.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				@SuppressWarnings("unchecked")
				T object = (T) cls.newInstance();
				getEntity(cursor, object);
				objects.add(object);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.close();
		}
		return objects;
	}

	public boolean haseData(Class<?> table, String condition) {
		mDB = mBaseHelper.getWritableDatabase();
		Cursor cursor = mDB.query(table.getSimpleName(), null, null, null,
				null, null, condition);
		if (cursor.getCount() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 多条件查询，没有的可以用null
	 * 
	 * @param cls
	 * @param columns
	 *            列名称,用逗号隔开
	 * @param selection
	 *            条件字句，相当于where
	 * @param selectionArgs
	 *            条件字句，参数，用逗号隔开
	 * @param groupBy
	 *            分组列
	 * @param having
	 *            分组条件
	 * @param orderBy
	 *            排序列
	 * @param limit
	 *            分页查询限制
	 * @return
	 */
	public <T> List<T> getDataWithConditions(Class<?> entity, String columns,
			String selection, String selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		List<T> objects = new ArrayList<T>();
		try {
			mDB = mBaseHelper.getWritableDatabase();
			String[] clusArgs = null;
			if (columns != null && !"".equals(columns)) {
				clusArgs = columns.split(",");
			}
			String[] selArgs = null;
			if (selectionArgs != null && !"".equals(selectionArgs)) {
				selArgs = selectionArgs.split(",");
			}
			Cursor cursor = mDB.query(entity.getSimpleName(), clusArgs,
					selection, selArgs, groupBy, having, orderBy);
			while (cursor.moveToNext()) {
				@SuppressWarnings("unchecked")
				T obj = (T) entity.newInstance();
				objects.add(getEntity(cursor, obj));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.close();
		}
		return objects;
	}

	/**
	 * 聚合函数count
	 *
	 * @param column
	 * @return
	 */
	public int getCount(Class<?> cls, String column, String selection) {
		int res = 0;
		StringBuffer sql = new StringBuffer();
		if (column != null && !"".equals(column)) {
			sql.append("SELECT COUNT(" + column + ") FROM "
					+ cls.getSimpleName());
		} else {
			sql.append("SELECT COUNT(*) FROM " + cls.getSimpleName());
		}

		if (selection != null && !"".equals(selection)) {
			sql.append(" WHERE " + selection);
		}
		mDB = mBaseHelper.getWritableDatabase();
		try {
			Cursor mCount = mDB.rawQuery(sql.toString(), null);
			while (mCount.moveToNext()) {
				res = mCount.getInt(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.close();
		}

		return res;
	}

	/**
	 * 聚合函数SUM，column必须不能为空
	 * 
	 * @param cls
	 * @param column
	 * @param selection
	 * @return
	 */
	public int getSum(Class<?> cls, String column, String selection) {
		int res = 0;
		StringBuffer sql = new StringBuffer();
		if (column != null && !"".equals(column)) {
			sql.append("SELECT SUM(" + column + ") FROM " + cls.getSimpleName());
		} else {
			return res;
		}
		if (selection != null && !"".equals(selection)) {
			sql.append(" WHERE " + selection);
		}
		mDB = mBaseHelper.getWritableDatabase();
		try {
			Cursor mCount = mDB.rawQuery(sql.toString(), null);
			mCount.moveToFirst();
			res = mCount.getInt(0);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			mDB.close();
		}

		return res;
	}

	/**
	 * 获取某一个int类型的参数，
	 * 
	 * @param cls
	 * @param column
	 * @param selection
	 * @return
	 */
	public String getIntColumn(Class<?> cls, String column, String selection) {
		String res = "0";
		StringBuffer sql = new StringBuffer();
		if (column != null && !"".equals(column)) {
			sql.append("SELECT " + column + " FROM " + cls.getSimpleName());
		} else {
			return res;
		}
		if (selection != null && !"".equals(selection)) {
			sql.append(" WHERE " + selection);
		}
		mDB = mBaseHelper.getWritableDatabase();
		try {
			Cursor mCount = mDB.rawQuery(sql.toString(), null);
			while (mCount.moveToNext()) {
				res = mCount.getString(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			mDB.close();
		}
		return res;
	}

	/**
	 * 查询最大的数值
	 * 
	 * @param column
	 * @param cls
	 * @param where
	 * @return
	 */
	public String getMaxData(String column, Class<?> cls, String where) {
		String sql = "";
		String resultString = null;
		if (null == column || "".equals(column)) {
			return resultString;
		}
		if (null == where || "".equals(where)) {
			sql = "SELECT MAX(" + column + ") AS MAXCOLUMN FROM "
					+ cls.getSimpleName();
		} else {
			sql = "SELECT MAX(" + column + ") AS MAXCOLUMN FROM "
					+ cls.getSimpleName() + " WHERE " + where;
		}
		mDB = mBaseHelper.getReadableDatabase();
		Cursor cursor = mDB.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			resultString = cursor.getString(cursor.getColumnIndex("MAXCOLUMN"));
		}
		mDB.close();
		return resultString;
	}

	/**
	 * 更新数据
	 * 
	 * @param cls
	 * @param values
	 */
	public void updateData(Class<?> cls, ContentValues values,
			String whereClause) {
		mDB = mBaseHelper.getWritableDatabase();
		try {
			mDB.update(cls.getSimpleName(), values, whereClause, null);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			mDB.close();
		}
	}
	/**
	 * 此处提供复杂sql语句查询，返回多少条
	 * @param sql
	 * @return
	 */
	public int getSizeCount(String sql) {
		int num=0;
		if (sql!=null || !"".equals(sql)) {
			try {
				mDB = mBaseHelper.getReadableDatabase();
				Cursor cursor = mDB.rawQuery(sql, null);
				num=cursor.getCount();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				mDB.close();
			}
		}
		return num;
	}

	/**
	 * 获取单条记录，where条件必须含有
	 * 
	 * @param cls
	 * @param where
	 * @return
	 */
	public Object getObject(Class<?> cls, String where) {
		if ("".equals(where) || null == where) {
			return null;
		}
		try {
			Object object = new Object();
			object = cls.newInstance();
			String sql = "SELECT * FROM " + cls.getSimpleName() + " WHERE "
					+ where;
			Constant.log(sql);
			mDB = mBaseHelper.getReadableDatabase();
			Cursor cursor = mDB.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				
				return getEntity(cursor, object);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.close();
		}
		return null;
	}

	/************************************** 下面是私有方法和私有类，不会涉及到开发用 **********************************/
	/**
	 * 将数据库记录转换为对象
	 * 
	 * @param cursor
	 * @param entity
	 * @return
	 */
	private <T> T getEntity(Cursor cursor, T entity) {
		try {
			Class<?> entity_class = entity.getClass();

			Field[] fs = entity_class.getDeclaredFields();
			for (Field f : fs) {
				int index = cursor.getColumnIndex(f.getName());
				if (index >= 0) {
					Method set = getSetMethod(entity_class, f);
					if (set != null) {
						String value = cursor.getString(index) + "";
						if (cursor.isNull(index)) {
							value = null;
						}
						Class<?> type = f.getType();
						if (type == String.class) {
							set.invoke(entity, value);
						} else if (type == int.class || type == Integer.class) {
							set.invoke(entity, value == null ? (Integer) null
									: Integer.parseInt(value));
						} else if (type == float.class || type == Float.class) {
							set.invoke(entity, value == null ? (Float) null
									: Float.parseFloat(value));
						} else if (type == long.class || type == Long.class) {
							set.invoke(entity, value == null ? (Long) null
									: Long.parseLong(value));
						} else if (type == Date.class) {
							set.invoke(entity, value == null ? (Date) null
									: stringToDateTime(value));
						} else if (type.toString().equals("boolean")) {
							if (value.equals("1") || value.equals("0")) {
								set.invoke(entity, value.equals("1") ? true
										: false);
							}else {
								set.invoke(entity, value == null ? (Boolean) null
										: Boolean.parseBoolean(value));
							}
							
						} else {
							set.invoke(entity, value);
						}
					}
				}
			}
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unused")
	private String datetimeToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (d != null) {
			return sdf.format(d);
		}
		return null;
	}

	private Date stringToDateTime(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (s != null) {
			try {
				return sdf.parse(s);
			} catch (Exception e) {
				// Log.e(tag,"解析时间错误: "+s,e);
			}
		}
		return null;
	}

	/**
	 * 获取get方法
	 * 
	 * @param entity_class
	 * @param f
	 * @return
	 */
	@SuppressWarnings("unused")
	private Method getGetMethod(Class<?> entity_class, Field f) {
		Method method = null;
		Locale locale = Locale.getDefault();
		try {
			Method[] methods = entity_class.getDeclaredMethods();
			String fn = f.getName().toLowerCase(locale);

			for (Method m : methods) {
				String string = m.getName().toLowerCase(locale);
				if (string.startsWith("set")) {
					continue;
				}
				if (string.endsWith(fn) && string.startsWith("get")) {
					method = m;
					break;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return method;
	}

	/**
	 * 获取set方法
	 * 
	 * @param entity_class
	 * @param f
	 * @return
	 */
	private Method getSetMethod(Class<?> entity_class, Field f) {
		Method method = null;
		Locale locale = Locale.getDefault();
		try {
			Method[] methods = entity_class.getDeclaredMethods();
			String fn = f.getName().toLowerCase(locale);
			if (fn.equals("serialversionuid")) {
				return method;
			}
			fn = "set" + fn;
			for (Method m : methods) {
				String string = m.getName().toLowerCase(locale);
				if (string.startsWith("get")) {
					continue;
				}
				if (string.equals(fn)) {
					method = m;
					break;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return method;
	}

	/**
	 * 获取创建表的sql
	 * 
	 * @param cls
	 * @param primaryKey
	 * @return
	 */
	private String getCreateTalbeSql(Class<?> cls, String primaryKey) {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE if not exists " + cls.getSimpleName() + "(");
		try {
			Field[] property_names = cls.getDeclaredFields();
			if (primaryKey == null || "".equals(primaryKey))
				sql.append("ycb_tid INTEGER PRIMARY KEY AUTOINCREMENT,");
			else
				sql.append(primaryKey + " PRIMARY KEY NOT NULL,");
			for (Field field : property_names) {
				String fieldType = field.getType().getSimpleName();
				String name = field.getName();
				if (name.equals(primaryKey) || name.equals(PRIMARYKEY)
						|| fieldType.contains("RouteEntity"))
					continue;
				sql.append(name);
				if ("String".equals(fieldType)) {
					sql.append(" TEXT,");
				} else if ("Integer".equals(fieldType)
						|| "int".equals(fieldType)) {
					sql.append(" INTEGER,");
				} else if ("Long".equalsIgnoreCase(fieldType)) {
					sql.append(" LONG,");
				} else if ("Boolean".equalsIgnoreCase(fieldType)) {
					sql.append(" BOOL,");
				} else if (" Date".equals(fieldType)) {
					sql.append(" DATETIME,");
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		String string = "";
		if (sql.length() > 0 && sql.toString().endsWith(",")) {
			string = sql.substring(0, sql.length() - 1);
			string = string + ")";
		}
		return string;
	}

	private String getTableName(Object entity) {
		String name = entity.getClass().getSimpleName();
		return name;
	}

	/**
	 * 私有方法，执行sql语句，并返回执行结果，含事务
	 * 
	 * @param sql
	 * @return false 失败 true 失败
	 */
	private Boolean dbQuery(String sql) {
		Boolean rs = false;

		try {
			if ("".equals(sql) && sql == null) {
				return rs;
			}
			mDB = mBaseHelper.getWritableDatabase();
			mDB.beginTransaction();
			mDB.execSQL(sql);
			mDB.setTransactionSuccessful();
			rs = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			mDB.endTransaction();
			mDB.close();
		}
		return rs;
	}

	private class DataBaseHelper extends SQLiteOpenHelper {

		public DataBaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}
}
