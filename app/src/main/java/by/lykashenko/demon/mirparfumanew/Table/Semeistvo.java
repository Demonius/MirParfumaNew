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
    public String value;

    @Column(name = "check_semeistvo")
    public Boolean check;

    public Semeistvo(){
        super();
    }

    public Semeistvo(String value, Boolean check) {
        this.value = value;
        this.check = check;
    }
}
