package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 13.07.17.
 */
@Table(name = "semeistvo", id = "_id")
public class Semeistvo extends Model {
    @Column(name = "value")
    public String value_for;

    @Column(name = "check_click")
    public Boolean check_click;

    public Semeistvo(){
        super();
    }

    public Semeistvo(String value_for, Boolean check_click) {
        this.value_for = value_for;
        this.check_click = check_click;
    }
}
