package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 16.02.2017.
 */

public class CountAdapter {

 @SerializedName("count(1)")
 private Integer count;


 public Integer getCount() {
  return count;
 }

 public void setCount(Integer count) {
  this.count = count;
 }
}
