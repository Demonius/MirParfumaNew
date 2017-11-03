package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.Adapters.OtzuvuForMirParfuma;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceLoadOtzuvu;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by User on 01.11.2017.
 */

public class GetListOtzuv {



    public interface InterfaceCallbackLoadOtzuvu{
        void CallbackLoadOtzuvu(ArrayList<OtzuvuForMirParfuma> listOtzuvu);
    }

    private String sql_query;
    private InterfaceCallbackLoadOtzuvu interfaceCallbackLoadOtzuvu;

    public void registerCallbackLoadOtzuvu(InterfaceCallbackLoadOtzuvu interfaceCallbackLoadOtzuvu){
        this.interfaceCallbackLoadOtzuvu = interfaceCallbackLoadOtzuvu;
    }

    public GetListOtzuv(String sql_query) {
        this.sql_query=sql_query;
    }

    public void load() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceLoadOtzuvu loadOtzuvu = retrofit.create(InterfaceLoadOtzuvu.class);
        Call<ArrayList<OtzuvuForMirParfuma>> call = loadOtzuvu.load(sql_query);

        call.enqueue(new Callback<ArrayList<OtzuvuForMirParfuma>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<OtzuvuForMirParfuma>> call, @NonNull Response<ArrayList<OtzuvuForMirParfuma>> response) {
                Log.d(LOG_TAG, "load list otzuvu- >> " + response.code());
                interfaceCallbackLoadOtzuvu.CallbackLoadOtzuvu(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<OtzuvuForMirParfuma>> call, Throwable t) {
                Log.e(LOG_TAG, "error response load otzuvu ->> " + t.getMessage());
            }
        });
    }

}
