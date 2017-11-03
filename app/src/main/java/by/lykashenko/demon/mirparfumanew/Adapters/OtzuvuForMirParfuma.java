package by.lykashenko.demon.mirparfumanew.Adapters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 01.11.2017.
 */

public class OtzuvuForMirParfuma {

    @SerializedName("id")
    private Long id_otzuva;

    @SerializedName("text")
    private String text_otzuva;

    @SerializedName("name")
    private String name_client;

    @SerializedName("createdon")
    private String create_date;

    @SerializedName("createdby")
    private Integer create_by;

    @SerializedName("rating")
    private Integer rating;

    public Long getId_otzuva() {
        return id_otzuva;
    }

    public void setId_otzuva(Long id_otzuva) {
        this.id_otzuva = id_otzuva;
    }

    public String getText_otzuva() {
        return text_otzuva;
    }

    public void setText_otzuva(String text_otzuva) {
        this.text_otzuva = text_otzuva;
    }

    public String getName_client() {
        return name_client;
    }

    public void setName_client(String name_client) {
        this.name_client = name_client;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Integer getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Integer create_by) {
        this.create_by = create_by;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
