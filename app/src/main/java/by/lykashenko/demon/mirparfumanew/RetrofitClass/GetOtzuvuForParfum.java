package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.Adapters.Otzuvu;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceGetOtzuvu;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 03.07.17.
 */

public class GetOtzuvuForParfum {

    public interface GetOtzuvuList{
        void onGetOtzuvuList(ArrayList<Otzuvu> otzuvu);
    }

    private GetOtzuvuList getOtzuvuList;

    public void registerGetOtzuvuList(GetOtzuvuList getOtzuvuList){
        this.getOtzuvuList=getOtzuvuList;
    }

    public void load(String sql_get_otzuvu) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceGetOtzuvu getOtzuvu = retrofit.create(InterfaceGetOtzuvu.class);
        Call<ArrayList<Otzuvu>> call = getOtzuvu.getOtzuvu(sql_get_otzuvu);
        call.enqueue(new Callback<ArrayList<Otzuvu>>() {
            @Override
            public void onResponse(Call<ArrayList<Otzuvu>> call, Response<ArrayList<Otzuvu>> response) {
                if (response!=null){
                    ArrayList<Otzuvu> list = response.body();
                    getOtzuvuList.onGetOtzuvuList(list);

                } else {
                    Log.e(LOG_TAG,"error answer getOtzuvuForParfum: code answer => "+response.code()+"| message => "+response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Otzuvu>> call, Throwable t) {
                Log.e(LOG_TAG,"error connection getOtzuvuForParfum: error message => "+t.getMessage());
            }
        });
    }
}
