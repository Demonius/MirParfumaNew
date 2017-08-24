package by.lykashenko.demon.mirparfumanew.Adapters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 09.06.2017.
 */

public class CenaParfum {
    @SerializedName("MIGX_id")
    private Integer mIGXId;
    @SerializedName("title")
    private String title;
    @SerializedName("price")
    private String price;
    @SerializedName("oldprice")
    private String oldprice;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }



    public Integer getmIGXId() {
        return mIGXId;
    }

    public void setmIGXId(Integer mIGXId) {
        this.mIGXId = mIGXId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
