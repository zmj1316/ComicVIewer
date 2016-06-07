package tk.zmj1316.comicviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tk.zmj1316.ComicParser.Book;
import tk.zmj1316.ComicParser.Util;

/**
 * Created by Key on 2016/6/6.
 */
public class BookViewCardAdapter extends RecyclerView.Adapter<SimpleCardViewHolder> {
    private Context mContext;
    private List<Book> mList = new ArrayList<>(0);
    private int mCount = 0;
    private LayoutInflater mInflater;
    private final String mURL;
    private int mPageNumber = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BookViewCardAdapter(final Context mContext, String mURL, SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mContext = mContext;
        this.mURL = mURL;
        this.mInflater = LayoutInflater.from(mContext);
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        new AsyncParser().execute(this.mURL);
    }

    class AsyncParser extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            if (mPageNumber == 1) {
                mList = Book.parse(mURL, mPageNumber++);
            } else {
                mList.addAll(Book.parse(mURL, mPageNumber++));
            }
            mCount = mList.size();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mSwipeRefreshLayout.setRefreshing(false);
            notifyDataSetChanged();
        }
    }

    @Override
    public SimpleCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item, parent, false);
        SimpleCardViewHolder simpleCardViewHolder = new SimpleCardViewHolder(v);
        return simpleCardViewHolder;
    }

    @Override
    public void onBindViewHolder(final SimpleCardViewHolder holder, int position) {
        holder.itemTv.setText(mList.get(position).title);
        final String thumb = mList.get(position).Thumb;
        class Download extends AsyncTask<String, Integer, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... params) {
                return Util.downloadBitmapWithReferer(params[0], "");
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);

                if (s != null) {
                    if (holder.itemIv.getTag().equals(thumb)) {
                        holder.itemIv.setImageBitmap(s);
                    } else {
//                        holder.itemIv.setImageDrawable(null);
                    }
                }
            }
        }
        holder.itemIv.setTag(thumb);
        new Download().execute(thumb);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public void refresh() {
        mPageNumber = 1;
        new AsyncParser().execute();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void loadMoreList() {
        new AsyncParser().execute();
        notifyDataSetChanged();
    }

}

class SimpleCardViewHolder extends RecyclerView.ViewHolder {
    public TextView itemTv;
    public ImageView itemIv;

    public SimpleCardViewHolder(View layout) {
        super(layout);
        itemTv = (TextView) layout.findViewById(R.id.item_textview);
        itemIv = (ImageView) layout.findViewById(R.id.item_imageview);
    }
}

