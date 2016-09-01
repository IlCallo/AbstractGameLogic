package agl.impl.event;

public abstract class BaseEvent implements Event {
    private String mName;
    private String mDescription;

    BaseEvent(String name, String description) {
        mName = name;
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }
}
