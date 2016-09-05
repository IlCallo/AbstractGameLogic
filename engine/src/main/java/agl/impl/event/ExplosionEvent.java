package agl.impl.event;

import agl.impl.TurnGame;
import agl.impl.utils.RandomPicker;
import agl.impl.zone.BaseZone;

import java.util.List;

public class ExplosionEvent extends BaseEvent {
    public ExplosionEvent() {
        super("explosion", "An explosion destroy buildings in an zone");
    }

    @Override
    public void runEvent(TurnGame game) {
        List<BaseZone> zones = game.getBoard().getZones();
        BaseZone z = RandomPicker.pick(zones);

        // Remove all buildings
        z.getBuildings().forEach(z::removeBuilding);
    }
}
