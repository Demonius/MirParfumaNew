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

    @Column(name = "check_click")
    public Boolean check_click;

    public Type(String value, Boolean check_click) {
        this.value = value;
        this.check_click = check_click;
    }

    public Type(){
        super();
    }

}
