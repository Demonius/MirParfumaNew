package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceLoadBrendu;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.Table.BrenduAll;
import by.lykashenko.demon.mirparfumanew.Table.BrenduCount;
import by.lykashenko.demon.mirparfumanew.Table.Country;
import by.lykashenko.demon.mirparfumanew.Table.ForTable;
import by.lykashenko.demon.mirparfumanew.Table.Type;
import by.lykashenko.demon.mirparfumanew.Table.Year;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;
import static by.lykashenko.demon.mirparfumanew.StartActivity.name_receiver;

/**
 * Created by demon on 03.04.2017.
 */

public class BrendList {

    public interface OnLoadBrendList {
        void onLoadBrendList(ArrayList<Brendu> brendu);

        void onProgress(Integer i, Integer count);
    }

    private OnLoadBrendList brendList;

    public void registerOnLoadBrendList(OnLoadBrendList callback) {
        this.brendList = callback;
    }

    private Context context;

    public BrendList(Context context) {
        this.context = context;
    }

    public void load(String sql_string_limit, final Integer id_load) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceLoadBrendu loadBrendu = retrofit.create(InterfaceLoadBrendu.class);
        Call<ArrayList<Brendu>> call = loadBrendu.load(sql_string_limit);
        call.enqueue(new Callback<ArrayList<Brendu>>() {
            @Override
            public void onResponse(Call<ArrayList<Brendu>> call, Response<ArrayList<Brendu>> response) {
                if (response != null) {

                    ActiveAndroid.beginTransaction();
                    switch (id_load) {
                        case 1:

                            if ((new Select().from(BrenduAll.class).executeSingle()) != null) {
                                new Delete().from(BrenduAll.class).execute();
                            }
                            ArrayList<Brendu> brendu = response.body();
                            SaveBrendu saveBrendu = new SaveBrendu();
                            saveBrendu.execute(brendu);
                            // sendMessage();
                            break;

                        case 2:
                            if ((new Select().from(Type.class).executeSingle()) != null) {
                                new Delete().from(Type.class).execute();
                            }
                            for (int i = 0; i < response.body().size(); i++) {
                                Type type = new Type(response.body().get(i).getValue(), true);
                                type.save();
                            }
                            sendMessage();
                            break;

                        case 3:
                            if ((new Select().from(Year.class).executeSingle()) != null) {
                                new Delete().from(Year.class).execute();
                            }
                            for (int i = 0; i < response.body().size(); i++) {
                                Year year_min_max = new Year(response.body().get(i).getValue());
                                year_min_max.save();
                            }
                            sendMessage();
                            break;

                        case 4:

                            sendMessage();
                            break;

                        case 5:

                            sendMessage();
                            break;

                        case 6:
                            if ((new Select().from(Country.class).executeSingle()) != null) {
                                new Delete().from(Country.class).execute();
                            }
                            for (int i = 0; i < response.body().size(); i++) {
                                Country country = new Country(response.body().get(i).getValue(),true);
                                country.save();
                            }
                            sendMessage();
                            break;

                        case 7:
                            ArrayList<Brendu> brendu1 = response.body();
                            brendList.onLoadBrendList(brendu1);
                            break;

                        case 8:
                            if ((new Select().from(ForTable.class).executeSingle()) != null) {
                                new Delete().from(ForTable.class).execute();
                            }
                            for (int i = 0; i < response.body().size(); i++) {
                                ForTable forTableTable = new ForTable(response.body().get(i).getValue(),true);
                                forTableTable.save();
                            }
                            sendMessage();
                            break;

                    }
                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();

                } else {
                    Log.e(LOG_TAG, "ошибка ответа: код => " + response.code() + " ,тело ошибки => " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Brendu>> call, Throwable t) {
                Log.e(LOG_TAG, "ошибка запроса => " + t.getMessage());
            }
        });


    }

    private class SaveBrendu extends AsyncTask<ArrayList<Brendu>, Integer, Integer> {


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            sendMessage();
            SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("state", 1);
            editor.commit();
        }

        @Override
        protected Integer doInBackground(ArrayList<Brendu>... params) {
            for (int i = 0; i < params[0].size(); i++) {
                Brendu brend = params[0].get(i);
                BrenduAll brenduAll = new BrenduAll(brend.getBrend_id(), brend.getBrend_name(), brend.getParfum_id(), brend.getValue(), brend.getSex(), true);
                brenduAll.save();
                publishProgress(i, params[0].size());
            }
            int count = 1;
            for (int i = 0; i < params[0].size() - 1; i++) {
                if (params[0].get(i).getBrend_id().equals(params[0].get(i + 1).getBrend_id())) {
//                    Log.d(LOG_TAG,"равенство: "+params[0].get(i).getBrend_id()+" <=> "+params[0].get(i + 1).getBrend_id());
                    count++;
                    if (i == (params[0].size() - 1)) {
                        BrenduCount brenduCount = new BrenduCount();
                        brenduCount.id_brend = params[0].get(i).getBrend_id();
                        brenduCount.name = params[0].get(i).getBrend_name();
                        brenduCount.count = count;
                        brenduCount.check = true;
                        brenduCount.save();
//                        Log.e(LOG_TAG, "count => "+count);
                    }
                } else {
                    if ((params[0].size() - i) == 2) {
                        BrenduCount brenduCount = new BrenduCount();
                        brenduCount.id_brend = params[0].get(i + 1).getBrend_id();
                        brenduCount.name = params[0].get(i + 1).getBrend_name();
                        brenduCount.count = 1;
                        brenduCount.check = true;
                        brenduCount.save();
//                        Log.e(LOG_TAG, "count => "+1);
                        i = params[0].size();
                    }
                    BrenduCount brenduCount = new BrenduCount();
                    brenduCount.id_brend = params[0].get(i).getBrend_id();
                    brenduCount.name = params[0].get(i).getBrend_name();
                    brenduCount.count = count;
                    brenduCount.check = true;
                    brenduCount.save();
//                    Log.e(LOG_TAG, "count => "+count);
                    count = 1;
                }
            }
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            brendList.onProgress(values[0], values[1]);
        }
    }

    private void sendMessage() {
        Intent serviceStartedIntent = new Intent(name_receiver);
        serviceStartedIntent.putExtra("count", 1);
        LocalBroadcastManager.getInstance(context).sendBroadcast(serviceStartedIntent);
    }


}
