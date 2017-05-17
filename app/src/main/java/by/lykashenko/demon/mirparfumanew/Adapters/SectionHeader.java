package by.lykashenko.demon.mirparfumanew.Adapters;

import com.intrusoft.sectionedrecyclerview.Section;

import java.util.List;

/**
 * Created by demon on 27.04.2017.
 */

public class SectionHeader implements Section<Child> {

    List<Child> childList;
    String sectinText;

    @Override
    public List<Child> getChildItems() {
        return childList;
    }

    public String getSectinText() {
        return sectinText;
    }

    public SectionHeader(List<Child> childList, String sectinText){
        this.childList=childList;
        this.sectinText=sectinText;
    }
}
