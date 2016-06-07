package tk.zmj1316.ComicParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Key on 2016/4/12.
 */
public class Chapter {
    public String id;
    public String ch_str;
    public int page_count;

    public Chapter(String ch_str) {
        this.ch_str = ch_str;
        this.page_count = Get_Ch_Range(ch_str);
        this.id = Extract_Ch_id(ch_str);
    }

    /**
     * @param ch_str exp: "/ch2-229633/"
     * @return chapter id
     */
    public static int Get_Ch_Range(String ch_str) {
        try {
            Document doc = Jsoup.connect(Util.Domain + ch_str)
                    .userAgent(Util.UserAgent)
                    .timeout(3000)
                    .get();
            return Integer.parseInt(doc.getElementsByClass("view_bt").get(0).getElementsByTag("span").get(1).ownText());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param ch_str exp: "/ch2-229633/"
     * @return ch_id
     */
    public static String Extract_Ch_id(String ch_str) {
        try {
            final String Ch_Pattern = "/.*(\\d{6}).*/";
            Pattern r = Pattern.compile(Ch_Pattern);
            Matcher m = r.matcher(ch_str);
            if (m.find()) {
                return m.group(1);
            } else throw new Exception("Chapter ID extracting failed");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Gen_URL(String ch_str, int pageNum) {
        if (pageNum == 1) {
            return Util.Domain + ch_str;
        } else {
            return Util.Domain + ch_str.substring(0, ch_str.length() - 1) + String.format(Locale.US, "-p%d/", pageNum);
        }
    }
}
