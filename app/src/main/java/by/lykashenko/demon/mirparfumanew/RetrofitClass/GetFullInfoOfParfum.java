package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.content.Context;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Parfum;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceParfumInfo;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by demon on 09.06.2017.
 */

public class GetFullInfoOfParfum {

    public interface LoadInfo{
        void onLoadInfo(ArrayList<Parfum> parfum);
    }

    private  LoadInfo loadInfo;

    public void regestrationLoadInfo(LoadInfo loadInfo){
        this.loadInfo=loadInfo;
    }

    private Context context;
    public GetFullInfoOfParfum(Context context) {
        this.context =context;
    }

    public void load(String sql_query_info_parfum) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceParfumInfo interfaceParfumInfo = retrofit.create(InterfaceParfumInfo.class);
        Call<ArrayList<Parfum>> call = interfaceParfumInfo.load(sql_query_info_parfum);
        call.enqueue(new Callback<ArrayList<Parfum>>() {
            @Override
            public void onResponse(Call<ArrayList<Parfum>> call, Response<ArrayList<Parfum>> response) {
                if (response!=null){
                    loadInfo.onLoadInfo(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Parfum>> call, Throwable t) {

            }
        });

    }
}
