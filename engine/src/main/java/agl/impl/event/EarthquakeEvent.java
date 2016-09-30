package agl.impl.event;

import agl.impl.TurnGame;
import agl.impl.utils.RandomPicker;
import agl.impl.zone.BaseZone;

import java.util.List;

public class EarthquakeEvent extends BaseEvent {
    public EarthquakeEvent() {
        super("earthquake", "An earthquake strikes two zones");
    }

    @Override
    public void runEvent(TurnGame game) {
        List<BaseZone> zones = game.getBoard().getZones();

        // Extract a zone
        BaseZone z1 = RandomPicker.pick(zones);
        z1.getBuildings().forEach(z1::removeBuilding);

        // Extract another zone different from the first one
        BaseZone z2;
        do {
            z2 = RandomPicker.pick(zones);
        } while (z1 == z2);
        z2.getBuildings().forEach(z2::removeBuilding);
    }
}
