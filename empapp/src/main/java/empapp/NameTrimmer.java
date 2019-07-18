package empapp;

import javax.inject.Named;

@Named
public class NameTrimmer {

    public String trimName(String name) {
        return name.trim();
    }
}
