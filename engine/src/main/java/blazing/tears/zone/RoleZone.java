package blazing.tears.zone;

import blazing.tears.role.RoleProvider;

import java.util.Map;

public class RoleZone extends BaseZone {

    public RoleZone(int id, int cost) {
        super(id, cost, "Gain a bonus main.role in your main.role pool");
    }

    @Override
    protected void powerUp() {
        Map<String, Integer> rolePool = getController().getRolePool();
        rolePool.put(RoleProvider.COP_ROLE, 3);
        rolePool.put(RoleProvider.ASSASSIN_ROLE, 3);
        rolePool.put(RoleProvider.BUILDER_ROLE, 3);
    }
}
