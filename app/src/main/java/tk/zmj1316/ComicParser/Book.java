package tk.zmj1316.ComicParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Key on 2016/4/12.
 */
public class Book {
    public String title;
    public String URL;
    public String Thumb;
    public List<Chapter> mAllChapters;
    public List<Chapter> mNewChapters = new ArrayList<Chapter>();

    public int chapterCount = 0;
    public boolean Large = false;

    private Document doc;


    public Book(String URL) {
        this.URL = Util.Domain + URL;

        try {
            doc = Jsoup.connect(this.URL)
                    .userAgent(Util.UserAgent)
                    .timeout(3000)
                    .get();
            Element e = doc.getElementsByClass("sy_k21").get(0);
            title = e.getElementsByTag("h1").get(0).text();
            Element t = doc.getElementsByAttributeValue("class", "sy_k1 z").get(0);
            Thumb = t.getElementsByTag("img").get(0).attr("src");
            parseChapters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseChapters() {
        Elements chapterLists = doc.getElementsByAttributeValue("class", "sy_nr1 cplist_ullg");
        Element newChapterList = chapterLists.get(0);

        for (Element i : newChapterList.getElementsByTag("a")
                ) {
            mNewChapters.add(new Chapter(i.attr("href"), i.text()));
        }
        if (chapterLists.size() > 1) {
            Large = true;
            Element allChapterList = chapterLists.get(1);
            mAllChapters = new ArrayList<>(10);
            for (Element i : allChapterList.getElementsByTag("a")
                    ) {
                mAllChapters.add(new Chapter(i.attr("href"), i.text()));
            }
        }
    }

    static public class BookThumb {
        public final String Thumb;
        public final String URL;
        public final String title;

        public BookThumb(String thumb, String url, String title) {
            this.Thumb = thumb;
            URL = url;
            this.title = title;
        }
    }

    static public List<BookThumb> parse(String url, int pageNumber) {
        List<BookThumb> result = new ArrayList<>(24);
        try {
            Document doc = Jsoup.connect(url + pageNumber + "/")
                    .userAgent(Util.UserAgent)
                    .timeout(3000)
                    .get();
            Element main = doc.getElementsByClass("in_01").get(0);
            for (Element i : main.getElementsByTag("li")
                    ) {
                String href = i.getElementsByTag("a").get(0).attr("href");
                String thumb = i.getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
                String title = i.getElementsByTag("a").text();
                int firstMark = title.indexOf("[");
                if (firstMark > 0)
                    title = title.substring(0, firstMark - 1);
                result.add(new BookThumb(thumb, href, title));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
