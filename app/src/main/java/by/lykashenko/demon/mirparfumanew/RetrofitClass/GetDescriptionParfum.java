package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.DescriptionParfum;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceGetParfumDescription;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by User on 09.11.2017.
 */

public class GetDescriptionParfum {



    public interface LoadedDescriptionParfum{
        void onLoadedDescriptionParfum(ArrayList<DescriptionParfum> description);
    }

    private LoadedDescriptionParfum loadedDescriptionParfum;

    public void regestrationLoadedDescription(LoadedDescriptionParfum loadedDescriptionParfum){
        this.loadedDescriptionParfum=loadedDescriptionParfum;
    }

    public void load(String sql) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceGetParfumDescription interfaceGetParfumDescription = retrofit.create(InterfaceGetParfumDescription.class);
        Call<ArrayList<DescriptionParfum>> call = interfaceGetParfumDescription.getDescripton(sql);
        call
                .enqueue(new Callback<ArrayList<DescriptionParfum>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DescriptionParfum>> call, Response<ArrayList<DescriptionParfum>> response) {
                                        if (response.code()==200) loadedDescriptionParfum.onLoadedDescriptionParfum(response.body());
                else Log.e(LOG_TAG,"error answer description parfum, code answer =>> "+response.code()+", message =>> "+response.message());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DescriptionParfum>> call, Throwable t) {
                        Log.e(LOG_TAG,"error answer description parfum, message answer =>> "+t.getMessage());
                    }
                });
//        call.enqueue(new Callback<DescriptionParfum>() {
//            @Override
//            public void onResponse(Call<DescriptionParfum> call, Response<DescriptionParfum> response) {
//                if (response.code()==200) loadedDescriptionParfum.onLoadedDescriptionParfum(response.body());
//                else Log.e(LOG_TAG,"error answer description parfum, code answer =>> "+response.code()+", message =>> "+response.message());
//            }/
//
//            @Override
//            public void onFailure(Call<DescriptionParfum> call, Throwable t) {
//                Log.e(LOG_TAG,"error answer description parfum, message answer =>> "+t.getMessage());
//            }
//        });
    }

}
