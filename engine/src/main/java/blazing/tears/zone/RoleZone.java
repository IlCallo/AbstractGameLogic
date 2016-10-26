package blazing.tears.zone;

import blazing.tears.role.Role;

import java.util.Map;

import static blazing.tears.role.Role.*;

public class RoleZone extends BaseZone {

    public RoleZone(int id, int cost) {
        super(id, cost, "Gain a bonus main.role in your main.role pool");
    }

    @Override
    protected void powerUp() {
        Map<Role, Integer> rolePool = getController().getRolePool();
        rolePool.put(COP, 3);
        rolePool.put(ASSASSIN, 3);
        rolePool.put(BUILDER, 3);
    }
}
