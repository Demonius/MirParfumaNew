package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceLoadBrendu;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 03.04.2017.
 */

public class BrendList {


 public interface OnLoadBrendList {
  void onLoadBrendList(ArrayList<Brendu> brendu);
 }

 private OnLoadBrendList brendList;

 public void registerOnLoadBrendList(OnLoadBrendList callback) {
  this.brendList = callback;
 }


 private Context context;


 public BrendList(Context context) {
  this.context = context;
 }


 public void load(String sql_string_limit) {


  Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(MainActivity.URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build();

  InterfaceLoadBrendu loadBrendu = retrofit.create(InterfaceLoadBrendu.class);
  Call<ArrayList<Brendu>> call = loadBrendu.load(sql_string_limit);
call.enqueue(new Callback<ArrayList<Brendu>>() {
 @Override
 public void onResponse(Call<ArrayList<Brendu>> call, Response<ArrayList<Brendu>> response) {
  if (response!=null){
   ArrayList<Brendu> brendu = response.body();
//   Log.i(MainActivity.LOG_TAG,"Количество брендов => "+brendu.size());
   brendList.onLoadBrendList(brendu);

  }else{
   Log.e(MainActivity.LOG_TAG,"ошибка ответа: код => "+response.code()+" ,тело ошибки => "+response.message());
  }
 }

 @Override
 public void onFailure(Call<ArrayList<Brendu>> call, Throwable t) {
Log.e(MainActivity.LOG_TAG,"ошибка азпроса => "+t.getMessage());
 }
});


 }


}
