package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 09.11.2017.
 */

public class DescriptionParfum {

    @SerializedName("id")
    private Long id_parfum;

    @SerializedName("content")
    private String description;

    public Long getId_parfum() {
        return id_parfum;
    }

    public void setId_parfum(Long id_parfum) {
        this.id_parfum = id_parfum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
