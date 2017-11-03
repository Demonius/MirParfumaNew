package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 31.10.2017.
 */

public class News{
    @SerializedName("id")
    private String id_news;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;


    @SerializedName("date")
    private Long date;

    @SerializedName("uri")
    private String uri;

    @SerializedName("image")
    private String image;

    public String getId_news() {
        return id_news;
    }

    public void setId_news(String id_news) {
        this.id_news = id_news;
    }

    public String getTitle_news() {
        return title;
    }

    public void setTitle_news(String title) {
        this.title = title;
    }

    public String getText_news() {
        return text;
    }

    public void setText_news(String text) {
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImage_news() {
        return image;
    }

    public void setImage_news(String image) {
        this.image = image;
    }



}
