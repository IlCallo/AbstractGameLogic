package agl.core;

import java.util.ArrayList;
import java.util.List;

public abstract class aglGroup {
    private String mId;
    private String mName;
    private ArrayList<aglActor> mMembers;

    public aglGroup(String id, String name) {
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

    public List<aglActor> getMembers() {
        return mMembers;
    }

    public void addMember(aglActor actor) {
        mMembers.add(actor);
    }

    public void removeMember(aglActor actor) {
        mMembers.remove(actor);
    }
}
