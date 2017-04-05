package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

/**
 * Created by demon on 05.04.2017.
 */

import com.google.gson.annotations.SerializedName;

public class NewParfum {

 @SerializedName("contentid")
 private String contentid;
 @SerializedName("pagetitle")
 private String pagetitle;
 @SerializedName("image")
 private String image;

 public String getContentid() {
  return contentid;
 }

 public void setContentid(String contentid) {
  this.contentid = contentid;
 }

 public String getPagetitle() {
  return pagetitle;
 }

 public void setPagetitle(String pagetitle) {
  this.pagetitle = pagetitle;
 }

 public String getImage() {
  return image;
 }

 public void setImage(String image) {
  this.image = image;
 }

}
