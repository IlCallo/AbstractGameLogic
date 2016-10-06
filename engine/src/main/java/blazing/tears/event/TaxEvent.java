package blazing.tears.event;

import blazing.tears.TurnGame;
import blazing.tears.zone.BaseZone;

public class TaxEvent extends BaseEvent {
    private static int BUILDING_PAYMENT = 3;

    public TaxEvent() {
        super("tax", "Taxes on the buildings");
    }

    @Override
    public void runEvent(TurnGame game) {
        for (BaseZone z : game.getBoard().getZones()) {
            z.getBuildings().stream().filter(b -> !b.getOwner().spendMoney(BUILDING_PAYMENT)).forEach(z::removeBuilding);
        }
    }
}
