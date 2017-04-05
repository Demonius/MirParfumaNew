package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.NewParfum;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceLoadNewParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 05.04.2017.
 */

public class NewParfumList {


 public void load(String sql_string_new) {

  Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(MainActivity.URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build();

  InterfaceLoadNewParfum interfaceLoadNewParfum = retrofit.create(InterfaceLoadNewParfum.class);
  Call<ArrayList<NewParfum>> call = interfaceLoadNewParfum.load(sql_string_new);
  call.enqueue(new Callback<ArrayList<NewParfum>>() {
   @Override
   public void onResponse(Call<ArrayList<NewParfum>> call, Response<ArrayList<NewParfum>> response) {
    if (response!=null){
     ArrayList<NewParfum> newParfums = response.body();
     loadNewParfumList.onLoadNewParfumList(newParfums);
    }else{
     Log.e(MainActivity.LOG_TAG,"ошибка сервера: код ответа => "+response.code()+"; сообщение => "+response.message());
    }
   }

   @Override
   public void onFailure(Call<ArrayList<NewParfum>> call, Throwable throwable) {
Log.e(MainActivity.LOG_TAG,"ошибка загрузки с сервера");
   }
  });

 }

 public interface OnLoadNewParfumList{
  void onLoadNewParfumList(ArrayList<NewParfum> newParfums);
 }

 private OnLoadNewParfumList loadNewParfumList;

 public void registerOnLoadNewparfumList(OnLoadNewParfumList loadNewParfumList){
  this.loadNewParfumList=loadNewParfumList;
 }

 private Context context;

 public NewParfumList(Context context){
  this.context=context;
 }


}
