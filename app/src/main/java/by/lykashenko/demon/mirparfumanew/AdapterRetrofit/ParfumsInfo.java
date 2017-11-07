package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 04.11.2017.
 */

public class ParfumsInfo {


    @SerializedName("tmplvarid")
    private String tmplvarid;

    @SerializedName("contentid")
    private String contentid;

    @SerializedName("value")
    private String valueInfo;

    public String getTmplvarid() {
        return tmplvarid;
    }

    public void setTmplvarid(String tmplvarid) {
        this.tmplvarid = tmplvarid;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(String valueInfo) {
        this.valueInfo = valueInfo;
    }
}
