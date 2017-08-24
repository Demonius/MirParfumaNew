package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 13.07.17.
 */
@Table(name = "for", id = "_id")
public class ForTable extends Model {

    @Column(name = "value")
    public String value;

    @Column(name = "check_for")
    public Boolean check;

    public ForTable(){
        super();
    }

    public ForTable(String value, Boolean check) {
        this.value = value;
        this.check = check;
    }
}
