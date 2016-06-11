package tk.zmj1316.comicviewer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tk.zmj1316.ComicParser.Book;
import tk.zmj1316.ComicParser.Chapter;

/**
 * Created by Key on 2016/6/10.
 */
public class BookContentAdapter extends RecyclerView.Adapter<SimpleTextViewHolder> {

    private Book mBook;
    private String mURL;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Chapter> mList;
    private int mCount;

    class AsyncBookLoader extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            mBook = new Book(mURL);
            if (mBook.mAllChapters != null) {
                mList = mBook.mAllChapters;
            }
            else
                mList = mBook.mNewChapters;
            mCount = mList.size();

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            notifyItemRangeChanged(0,mCount);
        }
    }

    public BookContentAdapter(final Context mContext, String mURL) {
        this.mContext = mContext;
        this.mURL = mURL;
        this.mInflater = LayoutInflater.from(mContext);
        new AsyncBookLoader().execute();
    }

    @Override
    public SimpleTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.content_book_item, parent, false);
        SimpleTextViewHolder holder = new SimpleTextViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(final SimpleTextViewHolder holder, final int position) {
        holder.textView.setText(mList.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.textView.setText(mList.get(position).ch_str);
                Chapter chapter = mList.get(position);
                Intent intent = new Intent(mContext, GalleryActivity.class);
                intent.putExtra(GalleryActivity.GALLERY_CHSTR, chapter.ch_str);
                intent.putExtra(GalleryActivity.GALLERY_PAGECOUNT, chapter.page_count);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}

class SimpleTextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public SimpleTextViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.book_content_item_text);
    }
}
