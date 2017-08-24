package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 19.06.17.
 */
@Table(name = "brenducount", id = "_id")
public class BrenduCount extends Model {

    @Column(name = "id_brend")
    public String id_brend;

    @Column(name = "name_brend")
    public String name;

    @Column(name = "count")
    public Integer count;

    @Column(name = "checkedPodbor")
    public Boolean check;

    public BrenduCount() {
        super();
    }

    public BrenduCount(String id_brend, String name, Integer count, Boolean check) {
        this.id_brend = id_brend;
        this.name = name;
        this.count = count;
        this.check = check;
    }
}
