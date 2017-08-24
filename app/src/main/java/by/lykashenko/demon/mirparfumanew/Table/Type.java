package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 27.06.17.
 */
@Table(name = "type", id= "_id")
public class Type extends Model {

    @Column(name = "value")
    public String value;

    @Column(name = "check_type")
    public Boolean check;

    public Type(String value, Boolean check) {
        this.value = value;
        this.check = check;
    }

    public Type(){
        super();
    }

}
