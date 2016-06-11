package tk.zmj1316.comicviewer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tk.zmj1316.ComicParser.Chapter;
import tk.zmj1316.ComicParser.Page;
import tk.zmj1316.ComicParser.Util;

public class GalleryActivity extends AppCompatActivity {
    public static String GALLERY_CHSTR = "GALLERY_CHSTR";
    public static String GALLERY_PAGECOUNT = "GALLERY_PAGECOUNT";

    private String mChstr;
    private int mPageCount;
    private int mLoadedPage = 1;
    private int mReadingPage = 1;
    private ImageView mImageView;
    private List<Bitmap> mBitmapList;
    private Button mPrev;
    private Button mNext;
    private TextView mText;

    class AsyncImageLoader extends AsyncTask<String, Integer, String> {
        private Bitmap mBitmap;

        @Override
        protected String doInBackground(String... params) {
            String referer = Chapter.Gen_URL(mChstr, mLoadedPage);
            String imageFun = referer +
                    String.format(Locale.US, "imagefun.ashx?cid=%s&page=%d&key=&maxcount=10",
                            Chapter.Extract_Ch_id(mChstr), mLoadedPage);
            String imgURL = new Page(referer, imageFun, 1).GetImg();
            mBitmap = Util.downloadBitmapWithReferer(imgURL, referer);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mBitmapList.add(mBitmap);
            if (mBitmapList.size() == 1) {
                mImageView.setImageBitmap(mBitmap);
                mText.setText(String.format(Locale.CHINA, "%d/%d", mReadingPage, mPageCount));
            }
            if (mBitmapList.size() - 2 > mReadingPage) {
                mNext.setEnabled(true);
            } else
                mNext.setEnabled(false);
            if (mLoadedPage != mPageCount) {
                mLoadedPage++;
                new AsyncImageLoader().execute();
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        mChstr = (String) intent.getExtras().get(GALLERY_CHSTR);
        mPageCount = (int) intent.getExtras().get(GALLERY_PAGECOUNT);
        mImageView = (ImageView) findViewById(R.id.gallery_imageview);
        mBitmapList = new ArrayList<>();
        mText = (TextView) findViewById(R.id.page_text);
        mPrev = (Button) findViewById(R.id.gallery_prev);
        mPrev.setEnabled(false);
        mNext = (Button) findViewById(R.id.gallery_next);
        mNext.setEnabled(false);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(1);
            }
        });
        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(0);
            }
        });
        new AsyncImageLoader().execute();
    }


    private void changePage(int dir) {
        if (dir > 0 && mReadingPage < mBitmapList.size() - 2) { // Next page
            mImageView.setImageBitmap(mBitmapList.get(++mReadingPage));
        } else if (mReadingPage > 1) {
            mImageView.setImageBitmap(mBitmapList.get(--mReadingPage));
        }
        if (mReadingPage > 1) {
            mPrev.setEnabled(true);
        }
        if (mReadingPage < mBitmapList.size() - 2) {
            mNext.setEnabled(true);
        }
        mText.setText(String.format(Locale.CHINA, "%d/%d", mReadingPage, mPageCount));

    }
}
