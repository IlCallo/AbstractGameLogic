package blazing.tears.group;

import blazing.tears.actor.BaseActor;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGroup {
    private String mId;
    private String mName;
    private ArrayList<BaseActor> mMembers;

    public BaseGroup(String id, String name) {
        mId = id;
        mName = name;

        mMembers = new ArrayList<>();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<BaseActor> getMembers() {
        return mMembers;
    }

    public void addMember(BaseActor actor) {
        mMembers.add(actor);
    }

    public void removeMember(BaseActor actor) {
        mMembers.remove(actor);
    }
}
