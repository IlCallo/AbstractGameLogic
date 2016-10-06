package blazing.tears.actor;

import blazing.tears.role.BaseRole;

public abstract class BaseActor {
    private String mId;
    private BaseRole mRole;

    public BaseActor(String id) {
        mId = id;
    }

    public BaseActor(String id, BaseRole role) {
        mId = id;
        mRole = role;

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

    @Override
    public String toString() {
        return "BaseActor{" +
                "mId='" + mId + '\'' +
                ", mRole=" + mRole +
                '}';
    }
}
