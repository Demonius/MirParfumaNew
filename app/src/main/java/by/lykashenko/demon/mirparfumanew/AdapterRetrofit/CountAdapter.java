package by.lykashenko.demon.mirparfumanew.AdapterRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 16.02.2017.
 */

public class CountAdapter {

 @SerializedName("count(1)")
 private String count;


 public String getCount() {
  return count;
 }

 public void setCount(String count) {
  this.count = count;
 }
}
