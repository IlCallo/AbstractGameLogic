package agl.impl.objective;

public abstract class BaseObjective implements Objective {
    private String mDescription;

    public BaseObjective(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }
}
