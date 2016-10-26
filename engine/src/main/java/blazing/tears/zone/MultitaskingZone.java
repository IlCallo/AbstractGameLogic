package blazing.tears.zone;

import static blazing.tears.role.Role.MULTITASKING;

public class MultitaskingZone extends BaseZone {

    public MultitaskingZone(int id, int cost) {
        super(id, cost, "The selected unit can pick both the actions for his main.role");
    }

    @Override
    protected void powerUp() {
        getController().getRolePool().put(MULTITASKING, 1);
    }
}
