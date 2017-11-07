package by.lykashenko.demon.mirparfumanew.Table;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumsInfo;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetParfumInfoForId;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by User on 04.11.2017.
 */
@Table(name = "parfums", id = "_id")
public class Parfums extends Model {

    @Column(name = "tmplavarid")
    public String tmplvarid;

    @Column(name = "contentid")
    public String contentid;

    @Column(name = "valueinfo")
    public String infoparfum;

    public Parfums() {
        super();
    }

    public Parfums(String tmplvarid, String contentid, String infoparfum) {
        this.tmplvarid = tmplvarid;
        this.contentid = contentid;
        this.infoparfum = infoparfum;
    }

    public static void setParfums(ArrayList<ParfumsInfo> listData){
        ActiveAndroid.beginTransaction();
        try {

            for (ParfumsInfo parfumInfo: listData) {
                Parfums parfum = new Parfums();
                parfum.tmplvarid = parfumInfo.getTmplvarid();
                parfum.contentid = parfumInfo.getContentid();
                parfum.infoparfum= parfumInfo.getValueInfo();
                parfum.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static HashMap<String,String> getParfumsForId(String id){
        List<Parfums> parfumsInfo;
        HashMap<String,String> parfumsInfos = new HashMap<>();
        ActiveAndroid.beginTransaction();
        try {

            parfumsInfo = new Select().from(Parfums.class).where("contentid = ?",id).execute();

            for (Parfums parfum:parfumsInfo
                 ) {
                parfumsInfos.put(parfum.tmplvarid, parfum.infoparfum);

            }
            Log.d(LOG_TAG,"get parfums info for brend id "+id+" , count list => "+parfumsInfo.size());

            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return parfumsInfos;
    }

    public static Boolean isExists(String id){
        Boolean state;

        ActiveAndroid.beginTransaction();
        try {
            List<Parfums> parfum = new Select().from(Parfums.class).where("contentid = ?",id).execute();
            if (parfum.size()>0) {
                state = true;
                Log.e(LOG_TAG, "data to table Parfums name =>  "+parfum.size());
            }else{
                state=false;
                Log.e(LOG_TAG, "data to table Parfums name =>  "+0);
            }

            ActiveAndroid.setTransactionSuccessful();
        }catch (NullPointerException error){
            Log.e(LOG_TAG,"no data to table Parfums");
            state = false;
        }
        finally {
            ActiveAndroid.endTransaction();
        }
        return state;
    }

//    public static void loadParfumInfoFromServer(String listId){
//
//    }
}
