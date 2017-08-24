package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 13.07.17.
 */
@Table(name = "nota", id = "_id")
public class Nota extends Model {
    @Column(name = "value")
    public String value;

    @Column(name = "check_nota")
    public Boolean check;

    public Nota(){
        super();
    }

    public Nota(String value, Boolean check) {
        this.value = value;
        this.check = check;
    }
}
