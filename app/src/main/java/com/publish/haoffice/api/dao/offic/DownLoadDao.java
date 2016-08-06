package com.publish.haoffice.api.dao.offic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.publish.haoffice.api.DBHelper.DBHelper;
import com.publish.haoffice.api.downmanger.DownloadState;
import com.publish.haoffice.api.downmanger.DownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 2016/7/7.
 */
public class DownLoadDao {

    private DBHelper helper;
    /**
     * 表中字段[插入数据库时系统生成的id]
     */
    private static final String FIELD_ID = "_id";

    /**
     * 表中字段[下载url]
     */
    private static final String FIELD_URL = "url";

    /**
     * 表中字段[下载状态]
     */
    private static final String FIELD_DOWNLOAD_STATE = "downloadState";

    /**
     * 表中字段[文件放置路径]
     */
    private static final String FIELD_FILEPATH = "filepath";

    /**
     * 表中字段[文件名]
     */
    private static final String FIELD_FILENAME = "filename";

    private static final String FIELD_TITLE = "title";

    private static final String FIELD_THUMBNAIL = "thumbnail";

    /**
     * 表中字段[已完成文件大小]
     */
    private static final String FIELD_FINISHED_SIZE = "finishedSize";

    /**
     * 表中字段[文件总大小]
     */
    private static final String FIELD_TOTAL_SIZE = "totalSize";

    private DownLoadDao(Context context) {
        helper = new DBHelper(context);
    }

    public static DownLoadDao instance;

    public synchronized static DownLoadDao getInstance(Context context) {
        if (instance == null) {
            instance = new DownLoadDao(context);
        }
        return instance;
    }

    private final String TABLE_NAME = "download";


    /**
     * 存入一条下载任务（直接存入数据库）<BR>
     *
     * @param downloadTask DownloadTask
     */
    public void insert(DownloadTask downloadTask) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(TABLE_NAME, null, getContentValues(downloadTask));
    }


    /**
     * 根据url查询数据库中相应的下载任务<BR>
     *
     * @param url
     * @return DownloadTask
     */
    public DownloadTask query(String url) {
        SQLiteDatabase db = helper.getReadableDatabase();
        DownloadTask dlTask = null;
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                FIELD_URL, FIELD_DOWNLOAD_STATE, FIELD_FILEPATH, FIELD_FILENAME, FIELD_TITLE,
                FIELD_THUMBNAIL, FIELD_FINISHED_SIZE, FIELD_TOTAL_SIZE
        }, FIELD_URL + "=?", new String[] {
                url
        }, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                dlTask = new DownloadTask(cursor.getString(0), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                dlTask.setDownloadState(DownloadState.valueOf(cursor.getString(1)));
                dlTask.setFinishedSize(cursor.getInt(6));
                dlTask.setTotalSize(cursor.getInt(7));
            }
            cursor.close();
        }
        return dlTask;
    }
    /**
     * 查询数据库中所有下载任务集合<BR>
     *
     * @return 下载任务List
     */
    public List<DownloadTask> queryAll() {
        List<DownloadTask> tasks = new ArrayList<DownloadTask>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                FIELD_URL, FIELD_DOWNLOAD_STATE, FIELD_FILEPATH, FIELD_FILENAME, FIELD_TITLE,
                FIELD_THUMBNAIL, FIELD_FINISHED_SIZE, FIELD_TOTAL_SIZE
        }, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DownloadTask dlTask = new DownloadTask(cursor.getString(0), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                dlTask.setDownloadState(DownloadState.valueOf(cursor.getString(1)));
                dlTask.setFinishedSize(cursor.getInt(6));
                dlTask.setTotalSize(cursor.getInt(7));

                tasks.add(dlTask);
            }
            cursor.close();
        }

        return tasks;
    }

    public List<DownloadTask> queryDownloaded() {
        List<DownloadTask> tasks = new ArrayList<DownloadTask>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                FIELD_URL, FIELD_DOWNLOAD_STATE, FIELD_FILEPATH, FIELD_FILENAME, FIELD_TITLE,
                FIELD_THUMBNAIL, FIELD_FINISHED_SIZE, FIELD_TOTAL_SIZE
        }, FIELD_DOWNLOAD_STATE + "='FINISHED'", null, null, null, "_id desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DownloadTask dlTask = new DownloadTask(cursor.getString(0), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                dlTask.setDownloadState(DownloadState.valueOf(cursor.getString(1)));
                dlTask.setFinishedSize(cursor.getInt(6));
                dlTask.setTotalSize(cursor.getInt(7));

                tasks.add(dlTask);
            }
            cursor.close();
        }

        return tasks;
    }

    public  List<DownloadTask> queryUnDownloaded() {
        List<DownloadTask> tasks = new ArrayList<DownloadTask>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                FIELD_URL, FIELD_DOWNLOAD_STATE, FIELD_FILEPATH, FIELD_FILENAME, FIELD_TITLE,
                FIELD_THUMBNAIL, FIELD_FINISHED_SIZE, FIELD_TOTAL_SIZE
        }, FIELD_DOWNLOAD_STATE + "<> 'FINISHED'", null, null, null, "_id desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DownloadTask dlTask = new DownloadTask(cursor.getString(0), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                dlTask.setDownloadState(DownloadState.valueOf(cursor.getString(1)));
                dlTask.setFinishedSize(cursor.getInt(6));
                dlTask.setTotalSize(cursor.getInt(7));

                tasks.add(dlTask);
            }
            cursor.close();
        }

        return tasks;
    }

    /**
     * 更新下载任务<BR>
     *
     * @param downloadTask DownloadTask
     */
    public void update(DownloadTask downloadTask) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.update(TABLE_NAME, getContentValues(downloadTask), FIELD_URL + "=?", new String[] {
                downloadTask.getUrl()
        });
    }

    /**
     * 从数据库中删除一条下载任务<BR>
     *
     * @param downloadTask DownloadTask
     */
    public void delete(DownloadTask downloadTask) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, FIELD_URL + "=?", new String[] {
                downloadTask.getUrl()
        });
    }


    /**
     * 将DownloadTask转化成ContentValues<BR>
     *
     * @param downloadTask DownloadTask
     * @return ContentValues
     */
    private ContentValues getContentValues(DownloadTask downloadTask) {
        ContentValues values = new ContentValues();
        values.put(FIELD_URL, downloadTask.getUrl());
        values.put(FIELD_DOWNLOAD_STATE, downloadTask.getDownloadState().toString());
        values.put(FIELD_FILEPATH, downloadTask.getFilePath());
        values.put(FIELD_FILENAME, downloadTask.getFileName());
        values.put(FIELD_TITLE, downloadTask.getTitle());
        values.put(FIELD_THUMBNAIL, downloadTask.getThumbnail());
        values.put(FIELD_FINISHED_SIZE, downloadTask.getFinishedSize());
        values.put(FIELD_TOTAL_SIZE, downloadTask.getTotalSize());
        return values;
    }
}
