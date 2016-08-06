
package com.publish.haoffice.api.downmanger;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.StatuesUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.app.Construct.ConstructMainFragment;
import com.publish.haoffice.app.office.OfficDetailActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DownloadListActivity extends Activity  {

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    public static final String DOWNLOADED = "isDownloaded";

    private static final String TAG = "DownloadListActivity";

    private ListView mDownloadingListView;

    private ListView mDownloadedListView;

    private Context mContext;

    private Button mDownloadedBtn;

    private Button mDownloadingBtn;

    List<DownloadTask> mDownloadinglist;

    List<DownloadTask> mDownloadedlist;

    DownloadingAdapter mDownloadingAdapter;

    DownloadedAdapter mDownloadedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // //开启沉浸式状态栏
        StatuesUtils.openImmerseStatasBarMode(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.download_list);
        ButterKnife.inject(this);
        tv_title.setText("下载管理");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                DownloadListActivity.this.finish();
            }
        });
        mContext = this;

        mDownloadingBtn = (Button) findViewById(R.id.buttonDownloading);
        mDownloadedBtn = (Button) findViewById(R.id.buttonDownloaded);
        mDownloadedBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                toggleView(true);
            }});
        mDownloadingBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                toggleView(false);
            }});

        mDownloadingListView = (ListView) findViewById(R.id.downloadingListView);
        mDownloadedListView = (ListView) findViewById(R.id.downloadedListView);

        toggleView(true);

        mDownloadedlist = DownloadTaskManager.getInstance(mContext).getFinishedDownloadTask();
        mDownloadedAdapter = new DownloadedAdapter(DownloadListActivity.this, 0, mDownloadedlist);

        mDownloadedListView.setAdapter(mDownloadedAdapter);
        mDownloadedListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.d(TAG, "arg2" + arg2 + " mDownloadedlist" + mDownloadedlist.size());
                onDownloadFinishedClick(mDownloadedlist.get(arg2));
            }
        });
        mDownloadedListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
                    final long arg3) {
                new AlertDialog.Builder(mContext)
                        .setItems(
                                new String[] {
                                        mContext.getResources().getString(R.string.download_delete_task),
                                        mContext.getResources().getString(R.string.download_delete_task_file)
                                }, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            Toast.makeText(mContext,
                                                    mContext.getResources().getString(R.string.download_deleted_task_ok),
                                                    Toast.LENGTH_LONG).show();
                                            DownloadTaskManager.getInstance(mContext)
                                                    .deleteDownloadTask(mDownloadedlist.get(arg2));
                                            mDownloadedlist.remove(arg2);
                                            mDownloadedAdapter.notifyDataSetChanged();
                                        } else if (which == 1) {
                                            Toast.makeText(mContext,
                                                    mContext.getResources().getString(R.string.download_deleted_task_file_ok),
                                                    Toast.LENGTH_LONG).show();
                                            DownloadTaskManager.getInstance(mContext)
                                                    .deleteDownloadTask(mDownloadedlist.get(arg2));
                                            DownloadTaskManager.getInstance(mContext)
                                                    .deleteDownloadTaskFile(mDownloadedlist.get(arg2));
                                            mDownloadedlist.remove(arg2);
                                            mDownloadedAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }).create().show();
                return true;
            }
        });

        // downloading list
        mDownloadinglist = DownloadTaskManager.getInstance(mContext).getDownloadingTask();
        if(mDownloadinglist != null && mDownloadinglist.size() != 0){
            mDownloadingBtn.setText("正在下载("+mDownloadinglist.size() +")");
        }

        mDownloadingAdapter = new DownloadingAdapter(DownloadListActivity.this, 0, mDownloadinglist);

        mDownloadingListView.setAdapter(mDownloadingAdapter);


        mDownloadingListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                DownloadTask task = mDownloadinglist.get(arg2);
                switch (task.getDownloadState()) {
                    case PAUSE:
                        Log.i(TAG, "PAUSE continue " + task.getFileName());
                        DownloadTaskManager.getInstance(mContext).continueDownload(task);
                        //addListener(task);
                        break;
                    case FAILED:
                        Log.i(TAG, "FAILED continue " + task.getFileName());
                        DownloadTaskManager.getInstance(mContext).continueDownload(task);
                        //addListener(task);
                        break;
                    case DOWNLOADING:
                        Log.i(TAG, "DOWNLOADING pause " + task.getFileName());
                        DownloadTaskManager.getInstance(mContext).pauseDownload(task);

                        break;
                    case FINISHED:
                        onDownloadFinishedClick(task);
                        break;
                    case INITIALIZE:

                        break;
                    default:
                        break;
                }

            }
        });
        mDownloadingListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
                    final long arg3) {
                new AlertDialog.Builder(mContext)
                        .setItems(
                                new String[] {
                                        mContext.getResources().getString(R.string.download_delete_task),
                                        mContext.getResources().getString(R.string.download_delete_task_file)
                                }, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            Toast.makeText(mContext,
                                                    mContext.getResources().getString(R.string.download_deleted_task_ok),
                                                    Toast.LENGTH_LONG).show();
                                            DownloadTaskManager.getInstance(mContext)
                                                    .deleteDownloadTask(mDownloadinglist.get(arg2));
                                            mDownloadinglist.remove(arg2);
                                            if(mDownloadinglist.size() == 0){
                                                mDownloadingBtn.setText("正在下载");
                                            }else{
                                                mDownloadingBtn.setText("正在下载("+mDownloadinglist.size()+")");
                                            }
                                            mDownloadingAdapter.notifyDataSetChanged();
                                        } else if (which == 1) {
                                            Toast.makeText(mContext,
                                                    mContext.getResources().getString(R.string.download_deleted_task_file_ok),
                                                    Toast.LENGTH_LONG).show();
                                            DownloadTaskManager.getInstance(mContext)
                                                    .deleteDownloadTask(mDownloadinglist.get(arg2));
                                            DownloadTaskManager.getInstance(mContext)
                                                    .deleteDownloadTaskFile(mDownloadinglist.get(arg2));
                                            mDownloadinglist.remove(arg2);
                                            if(mDownloadinglist.size() == 0){
                                                mDownloadingBtn.setText("正在下载");
                                            }else{
                                                mDownloadingBtn.setText("正在下载("+mDownloadinglist.size()+")");
                                            }
                                            mDownloadingAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }).create().show();
                return true;
            }
        });

        for (final DownloadTask task : mDownloadinglist) {
            if (!task.getDownloadState().equals(DownloadState.FINISHED)) {
                Log.d(TAG, "add listener");
                addListener(task);
            }
        }

        //DownloadOperator.check(mContext);
    }


    private int downX;
    private int downY;
    private boolean b;
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                b = super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
//                int moveX1 = (int) ev.getX();
//                int moveY1 = (int) ev.getY();
//                int disX1 = Math.abs(moveX1 - downX);
//                int disY1 = Math.abs(moveY1 - downY);
//                if(disX1 > disY1 && disX1 > 5){
//                    ConstructMainFragment main1 = (ConstructMainFragment) getFragmentManager().findFragmentByTag("MAIN");
//                    main1.fragment_container1.setEnabled(false);
//                }
                b = super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_UP:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = Math.abs(moveX - downX);
                int disY = Math.abs(moveY - downY);
                LogUtils.d("ckj",moveX + "/" + downX);
                if(disX > disY && disX > 5){
                    b = false;
                    if(downX - moveX > 30){
                        toggleView(false);
                        break;
                    }else{
                        toggleView(true);
                        break;
                    }
                }else {
                    b = super.dispatchTouchEvent(ev);
                }
                break;
        }


        return b;
    }

    private void toggleView(boolean isShowDownloaded) {
        if (isShowDownloaded) {
            mDownloadedBtn.setBackgroundResource(R.drawable.download_right_tab);
            mDownloadingBtn.setBackgroundResource(R.drawable.download_left_tab);
            mDownloadedBtn.setTextColor(this.getResources().getColor(R.color.titleText));
            mDownloadingBtn.setTextColor(this.getResources().getColor(R.color.darkblack));
            mDownloadedListView.setVisibility(View.VISIBLE);
            mDownloadingListView.setVisibility(View.GONE);
        } else {
            mDownloadedBtn.setBackgroundResource(R.drawable.download_left_tab);
            mDownloadingBtn.setBackgroundResource(R.drawable.download_right_tab);
            mDownloadingBtn.setTextColor(this.getResources().getColor(R.color.titleText));
            mDownloadedBtn.setTextColor(this.getResources().getColor(R.color.darkblack));
            mDownloadedListView.setVisibility(View.GONE);
            mDownloadingListView.setVisibility(View.VISIBLE);
        }
    }

    class MyDownloadListener implements DownloadListener {
        private DownloadTask task;

        public MyDownloadListener(DownloadTask downloadTask) {
            task = downloadTask;
        }

        @Override
        public void onDownloadFinish(String filepath) {
            Log.d(TAG, "onDownloadFinish");
            task.setDownloadState(DownloadState.FINISHED);
            task.setFinishedSize(task.getFinishedSize());
            task.setPercent(100);
            DownloadListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingAdapter.notifyDataSetChanged();
                    mDownloadedAdapter.add(task);

                    mDownloadingAdapter.remove(task);
                    if(mDownloadingAdapter.getCount() == 0){
                        mDownloadingBtn.setText("正在下载");
                    }else{
                        mDownloadingBtn.setText("正在下载("+mDownloadingAdapter.getCount()+")");
                    }
                    // toggleView(true);
                }
            });

        }

        @Override
        public void onDownloadStart() {
            task.setDownloadState(DownloadState.INITIALIZE);
            DownloadListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onDownloadPause() {
            Log.d(TAG, "onDownloadPause");
            task.setDownloadState(DownloadState.PAUSE);
            DownloadListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onDownloadStop() {
            Log.d(TAG, "onDownloadStop");
            task.setDownloadState(DownloadState.PAUSE);
            DownloadListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onDownloadFail() {
            Log.d(TAG, "onDownloadFail");
            task.setDownloadState(DownloadState.FAILED);
            DownloadListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onDownloadProgress(final int finishedSize, final int totalSize,
                int speed) {
            // Log.d(TAG, "download " + finishedSize);
            task.setDownloadState(DownloadState.DOWNLOADING);
            task.setFinishedSize(finishedSize);
            task.setTotalSize(totalSize);
            task.setPercent(finishedSize*100/totalSize);
            task.setSpeed(speed);

            DownloadListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void addListener(DownloadTask task) {
        DownloadTaskManager.getInstance(mContext).registerListener(task, new MyDownloadListener(task));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        toggleView(intent.getBooleanExtra(DOWNLOADED, false));

        super.onNewIntent(intent);
    }

    /**
     * You can overwrite this method to implement what you want do after download task item is clicked.
     * @param task
     */
    public void onDownloadFinishedClick(DownloadTask task) {
        Log.d(TAG, task.getFilePath() + "/"+ task.getFileName());
        Intent intent = DownloadOpenFile.openFile(task.getFilePath()
                + "/"+ task.getFileName());
        if (null == intent) {
            Toast.makeText(mContext, R.string.download_file_not_exist, Toast.LENGTH_LONG).show();
        } else {
            mContext.startActivity(intent);
        }
    }




}
