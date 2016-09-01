package agl.impl.zone;

import agl.impl.VariableParam;

public class RoleZone extends BaseZone {

    public RoleZone(int id, int cost) {
        super(id, cost, "Gain a bonus role in your role pool");
    }

    @Override
    public void usePower(VariableParam param) {
    }
}
