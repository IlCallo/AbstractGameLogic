package blazing.tears.zone;

import blazing.tears.role.RoleProvider;

public class MultitaskingZone extends BaseZone {

    public MultitaskingZone(int id, int cost) {
        super(id, cost, "The selected unit can pick both the actions for his main.role");
    }

    @Override
    protected void powerUp() {
        getController().getRolePool().put(RoleProvider.MULTITASKING_ROLE, 1);
    }
}
