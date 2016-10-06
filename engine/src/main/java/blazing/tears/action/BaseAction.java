package blazing.tears.action;

public abstract class BaseAction {
    private String mName;
    private String mDescription;

    public BaseAction(String name, String description) {
        mName = name;
        mDescription = description;
    }

    public abstract void doAction();

    @Override
    public String toString() {
        return "BaseAction{" +
                "mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }
}
