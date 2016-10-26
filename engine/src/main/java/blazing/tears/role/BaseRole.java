package blazing.tears.role;

import blazing.tears.action.BaseAction;

import java.util.Collections;
import java.util.List;

public abstract class BaseRole {
    private String mName;
    private List<BaseAction> mActions;

    public BaseRole(String name) {
        this(name, Collections.emptyList());
    }

    public BaseRole(String name, List<BaseAction> actions) {
        mName = name;
        mActions = actions;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<BaseAction> getActions() {
        return mActions;
    }

    public void setActions(List<BaseAction> actions) {
        mActions = actions;
    }

    @Override
    public String toString() {
        return "BaseRole{" +
                "mName='" + mName + '\'' +
                ", mActions=" + mActions +
                '}';
    }
}
