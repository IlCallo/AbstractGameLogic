package agl.core;

public abstract class aglAction {
    private String mName;
    private String mDescription;

    public aglAction(String name, String description) {
        mName = name;
        mDescription = description;
    }

    public abstract void doAction();

    @Override
    public String toString() {
        return "aglAction{" +
                "mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }
}
