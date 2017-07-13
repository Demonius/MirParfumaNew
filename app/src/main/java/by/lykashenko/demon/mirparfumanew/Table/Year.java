package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 27.06.17.
 */
@Table(name = "year", id="_id")
public class Year extends Model {

    @Column(name = "value")
    public String value;

    public Year(){
        super();
    }

    public Year(String value) {
        this.value = value;
    }
}
