package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 27.06.17.
 */
@Table(name = "country", id="_id")
public class Country extends Model {

    @Column(name = "value")
    public String value;

    @Column(name = "check_country")
    public Boolean check;


    public Country(){
        super();
    }

    public Country(String value, Boolean check) {
        this.value = value;
        this.check = check;
    }
}
