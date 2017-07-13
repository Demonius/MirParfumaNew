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

    public Country(String value, Boolean check_country) {
        this.value = value;
        this.check_country = check_country;
    }

    @Column(name = "check_country")
    public Boolean check_country;


    public Country(){
        super();
    }


}
