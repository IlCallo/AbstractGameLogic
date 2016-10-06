package blazing.tears.event;

import blazing.tears.TurnGame;
import blazing.tears.utils.RandomPicker;
import blazing.tears.zone.BaseZone;

import java.util.List;

public class ExplosionEvent extends BaseEvent {
    public ExplosionEvent() {
        super("explosion", "An explosion destroy buildings in an main.zone");
    }

    @Override
    public void runEvent(TurnGame game) {
        List<BaseZone> zones = game.getBoard().getZones();
        BaseZone z = RandomPicker.pick(zones);

        // Remove all buildings
        z.getBuildings().forEach(z::removeBuilding);
    }
}
