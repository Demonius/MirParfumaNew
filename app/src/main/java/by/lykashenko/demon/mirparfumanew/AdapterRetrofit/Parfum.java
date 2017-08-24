package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 09.06.2017.
 */

public class Parfum {

    @SerializedName("tmplvarid")
    private String tmplvarid;
    @SerializedName("value")
    private String value;

    public String getTmplvarid() {
        return tmplvarid;
    }

    public void setTmplvarid(String tmplvarid) {
        this.tmplvarid = tmplvarid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
