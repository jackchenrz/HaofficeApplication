
package com.publish.haoffice.api.downmanger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.publish.haoffice.R;

public class DownloadingAdapter extends ArrayAdapter<DownloadTask> {

    private static final String TAG = "DownloadItemAdapter";

    private LayoutInflater mLayoutInflater;

    private List<DownloadTask> mTaskList;

    private Context mContext;

    public DownloadingAdapter(Context context, int textViewResourceId, List<DownloadTask> taskList) {
        super(context, textViewResourceId, taskList);
        mTaskList = taskList;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public int getCount() {
        return mTaskList.size();
    }

    public DownloadTask getItem(int position) {
        return mTaskList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        final DownloadTask task = mTaskList.get(position);
        //if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.download_list_item,null);
            holder = new ViewHolder();

            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mSize = (TextView) convertView.findViewById(R.id.size);
            holder.item_cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            holder.mStatusText = (TextView) convertView.findViewById(R.id.state);
            holder.mStateImageView = (ImageView) convertView.findViewById(R.id.ic_state);
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progress);
            holder.item_cb.setVisibility(View.GONE);
            holder.mProgressBar.setMax(100);
        // convertView.setTag(holder);
        // } else {
        // holder = (ViewHolder) convertView.getTag();
        // }

        holder.mTitle.setText(task.getTitle());
        holder.mSize.setText(formatSize(task.getFinishedSize(), task.getTotalSize()));

//        if(URLUtil.isHttpUrl(task.getThumbnail())){
//            holder.mThumbnail.setImageBitmap(getBitMapFromUrl(task.getThumbnail()));
//        } else if (URLUtil.isFileUrl(task.getThumbnail())){
//            holder.mThumbnail.setImageBitmap(BitmapFactory.decodeFile(task.getThumbnail().substring(8)));
//        } else if (URLUtil.isAssetUrl(task.getThumbnail())){
//            holder.mThumbnail.setImageBitmap(getBitmapFromAsset(task.getThumbnail().substring(22)));
//        }
        // ImageUtil.loadImage(holder.mIcon, task.getThumbnail());

        if (task.getPercent() > 0) {
            holder.mProgressBar.setProgress(task.getPercent());
        }

        switch (mTaskList.get(position).getDownloadState()) {

            case PAUSE:
                holder.mStatusText.setText(mContext.getResources().getString(R.string.download_paused));
                holder.mStateImageView.setImageResource(R.drawable.player_pause_selected);
                holder.mProgressBar.setIndeterminate(false);
                break;
            case FAILED:
                holder.mStatusText.setText(mContext.getResources().getString(R.string.download_failed));
                holder.mStateImageView.setImageResource(R.drawable.player_pause_selected);
                holder.mProgressBar.setIndeterminate(false);
                break;
            case DOWNLOADING:
                holder.mStatusText.setText(mContext.getResources().getString(R.string.download_downloading));
                holder.mStateImageView.setImageResource(R.drawable.tab_download_focus);
                holder.mProgressBar.setIndeterminate(false);
                break;
            case FINISHED:
                holder.mProgressBar.setProgress(100);
                holder.mProgressBar.setIndeterminate(false);
                holder.mStatusText.setText(mContext.getResources().getString(R.string.download_finished));
                holder.mStateImageView.setImageResource(R.drawable.tab_download_focus);
                break;
            case INITIALIZE:
                holder.mProgressBar.setIndeterminate(false);
                holder.mStatusText.setText(mContext.getResources().getString(R.string.download_initial));
                holder.mStateImageView.setImageResource(R.drawable.tab_download_focus);
                break;
            default:
                break;
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.listview_even_bg));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.listview_odd_bg));
        }

        return convertView;
    }

    private String formatSize(long finishedSize, long totalSize) {
        StringBuilder sb = new StringBuilder(50);

        float finished = ((float) finishedSize) / 1024 / 1024;
        if (finished < 1) {
            sb.append(String.format("%1$.2f K / ", ((float) finishedSize) / 1024));
        } else {
            sb.append((String.format("%1$.2f M / ", finished)));
        }

        float total = ((float) totalSize) / 1024 / 1024;
        if (total < 1) {
            sb.append(String.format("%1$.2f K ", ((float) totalSize) / 1024));
        } else {
            sb.append(String.format("%1$.2f M ", total));
        }
        return sb.toString();
    }

    static class ViewHolder {

        public TextView mTitle;

        public TextView mStatusText;

        public TextView mSize;

        public ProgressBar mProgressBar;

        public ImageView mStateImageView;

        public CheckBox item_cb;

    }

    public static Bitmap getBitMapFromUrl(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap getBitmapFromAsset(String fileName) {
        Bitmap image = null;
        try {
            AssetManager am = mContext.getAssets();
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {

        }
        return image;
    }
}
