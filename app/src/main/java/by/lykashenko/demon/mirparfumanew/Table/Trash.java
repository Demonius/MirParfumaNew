package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by demon on 13.06.2017.
 */
@Table(name = "trash", id="_id")
public class Trash extends Model {


    @Column(name = "id_parfum")
    public String id_parfum;

    @Column(name="name_parfum")
    public String name_parfum;

    @Column(name = "image_parfum")
    public String image_parfum;

    @Column(name = "ratting_parfum")
    public String ratting_parfum;

    @Column(name="cenaFor")
    public String cena_for;

    @Column(name = "cenaParfum")
    public String cena_parfum;

    @Column(name = "count_parfum")
    public Integer count_parfum;

    public Trash(){
        super();
    }

    public Trash(String id_parfum, String name_parfum, String image_parfum, String ratting_parfum, String cena_for, String cena_parfum, Integer count_parfum) {
        this.id_parfum = id_parfum;
        this.name_parfum = name_parfum;
        this.image_parfum = image_parfum;
        this.ratting_parfum = ratting_parfum;
        this.cena_for = cena_for;
        this.cena_parfum = cena_parfum;
        this.count_parfum = count_parfum;
    }
}
