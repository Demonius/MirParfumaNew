package by.lykashenko.demon.mirparfumanew.Table;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;


/**
 * Created by demon on 13.06.2017.
 */
@Table(name = "favorites", id="_id")
public class Favorites extends Model {

    @Column(name = "id_parfum")
    public String id_parfum;

    @Column(name="name_parfum")
    public String name_parfum;

    @Column(name = "image_parfum")
    public String image_parfum;

    @Column(name = "ratting_parfum")
    public Float ratting_parfum;

    @Column(name="cenaFor")
    public String cena_for;

    @Column(name = "cenaParfum")
    public String cena_parfum;

    public Favorites(){
        super();
    }

    public Favorites(String id_parfum, String name_parfum, String image_parfum, Float ratting_parfum, String cena_for, String cena_parfum) {
        this.id_parfum = id_parfum;
        this.name_parfum = name_parfum;
        this.image_parfum = image_parfum;
        this.ratting_parfum = ratting_parfum;
        this.cena_for = cena_for;
        this.cena_parfum = cena_parfum;
    }

    public static Boolean isFavorites(String id){
        Favorites favorite;
        Boolean state = false;
        ActiveAndroid.beginTransaction();
        try{
            favorite = new Select().from(Favorites.class).where("id_parfum = ?",id).executeSingle();
            if (favorite.getId()>0){
                state = true;
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        catch (NullPointerException error){
            state = false;
//            Log.e(LOG_TAG, "no data to table \'Favorites\'");
        }

        finally {
            ActiveAndroid.endTransaction();
        }
        return state;
    }

    public static void deleteFromFavorites(String id) {
        ActiveAndroid.beginTransaction();
        try{
            new Delete().from(Favorites.class).where("id_parfum = ?", id).execute();
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }
    }
}
