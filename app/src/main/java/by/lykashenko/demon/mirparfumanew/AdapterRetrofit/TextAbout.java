package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 12.04.2017.
 */

public class TextAbout {
    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
