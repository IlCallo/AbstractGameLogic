package agl.core;

public abstract class aglActor {
    private String mId;
    private aglRole mRole;

    public aglActor(String id) {
        mId = id;
    }

    public aglActor(String id, aglRole role) {
        mId = id;
        mRole = role;

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public aglRole getRole() {
        return mRole;
    }

    public void setRole(aglRole role) {
        mRole = role;
    }

    @Override
    public String toString() {
        return "aglActor{" +
                "mId='" + mId + '\'' +
                ", mRole=" + mRole +
                '}';
    }
}
