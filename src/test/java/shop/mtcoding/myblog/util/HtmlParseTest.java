package shop.mtcoding.myblog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class HtmlParseTest {

    @Test
    public void jsoup_test1() throws Exception{
        System.out.println("================================");
        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
        System.out.println(doc);
        System.out.println("================================");
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.attr("title"));
            System.out.println(headline.absUrl("href"));
        }
    }

    @Test
    public void jsoup_test2(){
        String html = "<p>1</p><p><img src=\"data:image/png;base64,iVBORw0KG\"></p>";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("img");
        if(els.size() == 0){
            // 임시 사진 제공해주기
            // 디비 thumnail -> /images/profile.jfif
        }else{
            Element el = els.get(0);
            String img = el.attr("src");
            System.out.println(img);
            // 디비 thumnail -> img
        }
    }

}