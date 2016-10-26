package blazing.tears.zone;

import static blazing.tears.role.Role.UNTOUCHABLE;

public class ImmuneZone extends BaseZone {

    public ImmuneZone(int id, int cost) {
        super(id, cost, "The selected unit cannot be killed by assassins");
    }

    @Override
    protected void powerUp() {
        getController().getRolePool().put(UNTOUCHABLE, 1);
    }
}
