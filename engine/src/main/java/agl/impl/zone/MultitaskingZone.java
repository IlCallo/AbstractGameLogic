package agl.impl.zone;

import agl.impl.role.RoleProvider;

public class MultitaskingZone extends BaseZone {

    public MultitaskingZone(int id, int cost) {
        super(id, cost, "The selected unit can pick both the actions for his role");
    }

    @Override
    protected void powerUp() {
        getController().getRolePool().put(RoleProvider.MULTITASKING_ROLE, 1);
    }
}
