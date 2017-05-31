package by.lykashenko.demon.mirparfumanew.Adapters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 24.05.2017.
 */

public class ParfumNamePrice {
	@SerializedName("contentid")
	private String contentid;

	@SerializedName("value")
	private String value;

	@SerializedName("tmplvarid")
	private Integer tmplvarid;

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getTmplvarid() {
		return tmplvarid;
	}

	public void setTmplvarid(Integer tmplvarid) {
		this.tmplvarid = tmplvarid;
	}
}
