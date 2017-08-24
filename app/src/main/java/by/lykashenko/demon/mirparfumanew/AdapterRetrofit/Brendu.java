package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

/**
 * Created by demon on 03.04.2017.
 */

import com.google.gson.annotations.SerializedName;

public class Brendu {

 @SerializedName("brend_id")
 private String brend_id;
 @SerializedName("brend_name")
 private String brend_name;
 @SerializedName("parfum_id")
 private String parfum_id;
 @SerializedName("sex")
 private String sex;
@SerializedName("value")
 private String value;

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }

 public String getBrend_id() {
  return brend_id;
 }

 public void setBrend_id(String brend_id) {
  this.brend_id = brend_id;
 }

 public String getBrend_name() {
  return brend_name;
 }

 public void setBrend_name(String brend_name) {
  this.brend_name = brend_name;
 }

 public String getParfum_id() {
  return parfum_id;
 }

 public void setParfum_id(String parfum_id) {
  this.parfum_id = parfum_id;
 }

 public String getSex() {
  return sex;
 }

 public void setSex(String sex) {
  this.sex = sex;
 }
}