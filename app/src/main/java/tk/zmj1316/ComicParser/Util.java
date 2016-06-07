package tk.zmj1316.ComicParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Key on 2016/6/6.
 */
public class Util {
    public static String UserAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36";
    public static String Domain = "http://www.1kkk.com";
    public static String NewBookUrl = "http://www.1kkk.com/manhua-new/";

    public static Bitmap downloadBitmapWithReferer(String url, String referer) {
        Bitmap bitmap = null;
        try {
            URL murl = new URL(url);
            URLConnection connection = murl.openConnection();
            connection.setUseCaches(true);
            connection.addRequestProperty("userAgent", UserAgent);
            connection.addRequestProperty("referer", referer);
            bitmap = BitmapFactory.decodeStream((InputStream) connection.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
