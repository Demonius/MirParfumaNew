package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import by.lykashenko.demon.mirparfumanew.Fragments.Favorite;

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
    public Integer ratting_parfum;

    @Column(name="cenaFor")
    public String cena_for;

    @Column(name = "cenaParfum")
    public String cena_parfum;

    public Favorites(){
        super();
    }

    public Favorites(String id_parfum, String name_parfum, String image_parfum, Integer ratting_parfum, String cena_for, String cena_parfum) {
        this.id_parfum = id_parfum;
        this.name_parfum = name_parfum;
        this.image_parfum = image_parfum;
        this.ratting_parfum = ratting_parfum;
        this.cena_for = cena_for;
        this.cena_parfum = cena_parfum;
    }
}
