package agl.core;

import java.util.List;

public abstract class aglRole {
    private String mName;
    private List<aglAction> mActions;

    public aglRole(String name) {
        mName = name;
    }

    public aglRole(String name, List<aglAction> actions) {

        mName = name;
        mActions = actions;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<aglAction> getActions() {
        return mActions;
    }

    public void setActions(List<aglAction> actions) {
        mActions = actions;
    }

    @Override
    public String toString() {
        return "aglRole{" +
                "mName='" + mName + '\'' +
                ", mActions=" + mActions +
                '}';
    }
}
