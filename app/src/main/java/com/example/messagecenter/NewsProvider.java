package com.example.messagecenter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import static com.example.messagecenter.MetaData.TableMetaData.CONTENT_TYPE;
import static com.example.messagecenter.MetaData.TableMetaData.CONTENT_TYPE_ITME;
import static com.example.messagecenter.MetaData.TableMetaData.NEW;
import static com.example.messagecenter.MetaData.TableMetaData.NEWS;
import static com.example.messagecenter.MetaData.TableMetaData.uriMatcher;

public class NewsProvider extends ContentProvider {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext(),MetaData.DATABASE_NAME,null,1);

    public NewsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NEWS:
                count = db.delete("news", selection, selectionArgs);
                break;
            case NEW:
                String newsId = uri.getPathSegments().get(1);
                count = db.delete("news","id=?",new String[]{newsId});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
                }
        db.close();
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NEWS:
                return CONTENT_TYPE;
            case NEW:
                return CONTENT_TYPE_ITME;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
                }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //获得一个可写的数据库引用，如果数据库不存在，则根据onCreate的方法里创建；
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long id = 0;
        switch (uriMatcher.match(uri)) {
            case NEWS:
                id = db.insert("news", null, values);    // 返回的是记录的行号，主键为int，实际上就是主键值
                return ContentUris.withAppendedId(uri, id);
                case NEW:
                    id = db.insert("news", null, values);
                    String path = uri.toString();
                    return Uri.parse(path.substring(0, path.lastIndexOf("/"))+id); // 替换掉id
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbOpenHelper = new DBOpenHelper(context, MetaData.DATABASE_NAME, null, MetaData.DATABASE_VERSION);
        db = dbOpenHelper.getWritableDatabase();

        if (db == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case NEWS:
                return db.query("news", projection, selection, selectionArgs, null, null, sortOrder);
            case NEW:
                // 进行解析，返回值为10
                String newsId = uri.getPathSegments().get(1);
                return db.query("news", projection, "id=?", new String[]{newsId}, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
                }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NEWS:
                count = db.update("news", values, selection, selectionArgs);
                break;
            case NEW:
                // 下面的方法用于从URI中解析出id，对这样的路径content://com.ljq.provider.personprovider/person/10
                // 进行解析，返回值为10
                String newsId = uri.getPathSegments().get(1);
                count = db.update("news", values, "id=?", new String[]{newsId});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
                }
        db.close();
        return count;
    }

    /**
     * 建立数据库
     */
    private static class DBOpenHelper extends SQLiteOpenHelper{

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, null, version);
        }

        private static final String DB_CREATE = "create table if not exists news ("
                +"id integer primary key autoincrement,"
                +MetaData.TableMetaData.FIELD_TITLE+" varchar(20),"
                +MetaData.TableMetaData.FIELD_MESSAGE+" text,"
                +MetaData.TableMetaData.FIELD_FLAG+" integer,"
                +MetaData.TableMetaData.FIELD_TIME+" varchar,"
                +MetaData.TableMetaData.FIELD_TYPE+" integer)";

        private static final String DB_DROP = "drop table news";
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            //sqLiteDatabase.execSQL(DB_DROP);
            sqLiteDatabase.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists news");
        }
    }
}
