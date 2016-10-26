package blazing.tears.actor;

import blazing.tears.action.BaseAction;
import blazing.tears.role.BaseRole;

public abstract class BaseActor {
    private String mId;
    private BaseRole mRole;
    private BaseAction mAction;

    public BaseActor(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public BaseRole getRole() {
        return mRole;
    }

    public void setRole(BaseRole role) {
        mRole = role;
    }

    public BaseAction getAction() {
        return mAction;
    }

    public void setAction(BaseAction action) {
        mAction = action;
    }

    @Override
    public String toString() {
        return "BaseActor{" +
                "mId='" + mId + '\'' +
                ", mRole=" + mRole +
                ", mAction=" + mAction +
                '}';
    }
}
