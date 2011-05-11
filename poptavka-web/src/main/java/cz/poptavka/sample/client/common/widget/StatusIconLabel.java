package cz.poptavka.sample.client.common.widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class StatusIconLabel extends SimpleIconLabel {

    public enum State {
        INFO_16, ACCEPT_16, ERROR_16, INFO_24, ACCEPT_24, ERROR_24, LOAD_24, LOAD_32
    }

    public StatusIconLabel(String description, State state) {
        super(description);
        setState(state);
    }

    public StatusIconLabel(State state) {
        super();
        setState(state);
    }

    public StatusIconLabel() {
        super();
    }

    public void setState(State state) {
        switch (state) {
            case INFO_16:
                image.setResource(StyleResource.INSTANCE.images().infoIcon16());
                break;
            case ACCEPT_16:
                image.setResource(StyleResource.INSTANCE.images().acceptIcon16());
                break;
            case ERROR_16:
                image.setResource(StyleResource.INSTANCE.images().errorIcon16());
                break;
            case INFO_24:
                image.setResource(StyleResource.INSTANCE.images().infoIcon24());
                break;
            case ACCEPT_24:
                image.setResource(StyleResource.INSTANCE.images().acceptIcon24());
                break;
            case ERROR_24:
                image.setResource(StyleResource.INSTANCE.images().errorIcon24());
                break;
            case LOAD_24:
                image.setResource(StyleResource.INSTANCE.images().loadIcon24());
                break;
            case LOAD_32:
                image.setResource(StyleResource.INSTANCE.images().loadIcon32());
                break;
            default:
                break;
        }
    }

    public void setTexts(String message, String description) {
        setMessage(message);
        setDescription(description);
    }

    public void setStyleState(String style, State state) {
        setState(state);
        setStyle(style);
    }

    public void setStateWithDescription(State state, String description) {
        setState(state);
        setDescription(description);
    }

    public void setPassedSmall(boolean passed) {
        if (passed) {
            setState(State.ACCEPT_16);
        } else {
            setState(State.ERROR_16);
        }
    }

    public void setPassedMedium(boolean passed) {
        if (passed) {
            setState(State.ACCEPT_24);
        } else {
            setState(State.ERROR_24);
        }
    }

    public void setStyle(String style) {
        wrapper.setStyleName(style);
    }

}
