package tk.zmj1316.ComicParser;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Key on 2016/4/19.
 */
public class Page {
    public String Referer;
    public String ImgURL;
    public String funURL;
    public int PageNum;

    public Page(String referer, String funURL, int pageNum) {
        Referer = referer;
        this.funURL = funURL;
        PageNum = pageNum;
    }

    public String GetImg() {
        if (ImgURL == null) {
            try {
                String js = getByReferer(funURL, Referer);
                Context ctx = Context.enter();
                ctx.setOptimizationLevel(-1);
                Scriptable scope = ctx.initStandardObjects();
                ImgURL = ((NativeArray) ctx.evaluateString(scope, js, null, 0, null)).get(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ImgURL;
    }

    private String getByReferer(String URL, String referer) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("userAgent", Util.UserAgent)
                .addHeader("referer", Referer)
                .build();
        try {
            Response res = okHttpClient.newCall(request).execute();
            return res.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
