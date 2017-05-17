package by.lykashenko.demon.mirparfumanew.Adapters;

/**
 * Created by demon on 27.04.2017.
 */

public class Child {

    String id;
    String name;
    String count;

    public Child(String id, String name, String count){
        this.id=id;
        this.name=name;
        this.count=count;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }
}
