package empapp;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
@Alternative
public class StubFacesContextProvider implements FacesContextProvider {

    private Map<String, Object> flashAttribute = new HashMap<>();

    @Override
    public void setFlashAttribute(String key, Object value) {
        flashAttribute.put(key, value);
    }

    public Map<String, Object> getFlashAttribute() {
        return flashAttribute;
    }
}
