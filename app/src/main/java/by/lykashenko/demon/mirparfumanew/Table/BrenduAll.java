package by.lykashenko.demon.mirparfumanew.Table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Admin on 04.07.17.
 */
@Table(name = "brenduall", id= "_id")
public class BrenduAll extends Model {

    @Column(name = "brend_id")
    public String brend_id;

    @Column(name = "brend_name")
    public String brend_name;

    @Column(name = "parfum_id")
    public String parfum_id;

    @Column(name = "parfum_name")
    public String parfum_name;



    @Column(name = "sex")
    public String sex;

    @Column(name = "check_podbor")
    public Boolean check_podbor;

    public BrenduAll() {
        super();
    }

    public BrenduAll(String brend_id, String brend_name, String parfum_id, String parfum_name, String sex, Boolean check_podbor) {
        this.brend_id = brend_id;
        this.brend_name = brend_name;
        this.parfum_id = parfum_id;
        this.parfum_name = parfum_name;
        this.sex = sex;
        this.check_podbor = check_podbor;
    }

}
