package agl.core;

import java.util.List;

public abstract class aglGroup {
    private String mName;
    private List<aglActor> mMembers;

    public aglGroup(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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
