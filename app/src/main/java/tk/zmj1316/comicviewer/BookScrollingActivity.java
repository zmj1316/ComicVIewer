package tk.zmj1316.comicviewer;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import tk.zmj1316.ComicParser.Util;


public class BookScrollingActivity extends AppCompatActivity {

    private String mURL;
    private String mThumb;
    private Intent mIntent;
    private String mTitle;
    private RecyclerView mRecycleVIew;
    private ImageView mImageVIew;
    private BookContentAdapter mBookContentAdapter;
    public static final String CHAPTER_URL = "BOOKSCROLLING_CHAPTER_URL";
    public static final String CHAPTER_TITLE = "BOOKSCROLLING_CHAPTER_TITLE";
    public static final String CHAPTER_THUMB = "BOOKSCROLLING_CHAPTER_THUMB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mIntent = getIntent();
        mURL = (String) mIntent.getExtras().get(CHAPTER_URL);
        if (mURL == null) {
            return;
        }
        mTitle = (String) mIntent.getExtras().get(CHAPTER_TITLE);
        mThumb = (String) mIntent.getExtras().get(CHAPTER_THUMB);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecycleVIew = (RecyclerView) findViewById(R.id.book_content_Recview);
        mBookContentAdapter = new BookContentAdapter(this, mURL);
        mRecycleVIew.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecycleVIew.setAdapter(mBookContentAdapter);
        setTitle(mTitle);
        mImageVIew = (ImageView) findViewById(R.id.book_content_image);
        mImageVIew.setImageBitmap(Util.downloadBitmapWithReferer(mThumb,""));
    }
}
