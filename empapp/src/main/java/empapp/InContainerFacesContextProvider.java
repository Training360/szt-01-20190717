package empapp;

import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
public class InContainerFacesContextProvider implements FacesContextProvider {

//    public FacesContext getFacesContext() {
//        return FacesContext.getCurrentInstance();
//    }

    @Override
    public void setFlashAttribute(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().put(key, value);
    }
}
