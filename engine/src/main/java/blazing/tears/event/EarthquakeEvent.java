package blazing.tears.event;

import blazing.tears.TurnGame;
import blazing.tears.utils.RandomPicker;
import blazing.tears.zone.BaseZone;

import java.util.List;

public class EarthquakeEvent extends BaseEvent {
    public EarthquakeEvent() {
        super("earthquake", "An earthquake strikes two zones");
    }

    @Override
    public void runEvent(TurnGame game) {
        List<BaseZone> zones = game.getBoard().getZones();

        // Extract a main.zone
        BaseZone z1 = RandomPicker.pick(zones);
        z1.getBuildings().forEach(z1::removeBuilding);

        // Extract another main.zone different from the first one
        BaseZone z2;
        do {
            z2 = RandomPicker.pick(zones);
        } while (z1 == z2);
        z2.getBuildings().forEach(z2::removeBuilding);
    }
}
