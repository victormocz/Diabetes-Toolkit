package model;

/**
 * Created by victorm on 4/13/16.
 */
public class Bookinfo {
    private String title;
    private int type;
    private static final String pdfurl = "https://docs.google.com/gview?embedded=true&url=http://www.auburn.edu/~czm0062/pdf/";
    private static final String htmlurl = "http://www.auburn.edu/~czm0062/pdf/";

    public Bookinfo(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        if (this.type == 0){
            return Bookinfo.pdfurl + title + ".pdf";
        } else {
            return Bookinfo.htmlurl + title + ".html";
        }
    }

    public int getType() {
        return type;
    }
}
