package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 01.07.17.
 */
@Table(name = "parfumlist", id = "_id")
public class ListParfum extends Model {

    @Column(name = "idParfum")
    public String idParfum;

    @Column(name = "name")
    public String name;

    @Column (name = "imageLink")
    public String imageLink;

    @Column(name = "ratting")
    public Integer retting;

    @Column(name="cenaFor")
    public String cena_for;

    @Column(name = "cenaParfum")
    public String cena_parfum;

    public ListParfum(){
        super();
    }

    public ListParfum(String idParfum, String name, String imageLink, Integer retting, String cena_for, String cena_parfum) {
        this.idParfum = idParfum;
        this.name = name;
        this.imageLink = imageLink;
        this.retting = retting;
        this.cena_for = cena_for;
        this.cena_parfum = cena_parfum;
    }
}
