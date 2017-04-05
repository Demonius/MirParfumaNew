package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

/**
 * Created by demon on 03.04.2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brendu {

 @SerializedName("id")
 private String id;
 @SerializedName("pagetitle")
 private String pagetitle;
 @SerializedName("alias")
 private String alias;
 @SerializedName("value")
 private String value;

 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getPagetitle() {
  return pagetitle;
 }

 public void setPagetitle(String pagetitle) {
  this.pagetitle = pagetitle;
 }

 public String getAlias() {
  return alias;
 }

 public void setAlias(String alias) {
  this.alias = alias;
 }

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }

}