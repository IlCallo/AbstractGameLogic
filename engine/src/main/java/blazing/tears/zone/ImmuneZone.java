package blazing.tears.zone;

import blazing.tears.role.RoleProvider;

public class ImmuneZone extends BaseZone {

    public ImmuneZone(int id, int cost) {
        super(id, cost, "The selected unit cannot be killed by assassins");
    }

    @Override
    protected void powerUp() {
        getController().getRolePool().put(RoleProvider.UNTOUCHABLE_ROLE, 1);
    }
}
