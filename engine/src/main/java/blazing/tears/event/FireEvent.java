package blazing.tears.event;

import blazing.tears.TurnGame;
import blazing.tears.utils.RandomPicker;
import blazing.tears.zone.BaseZone;

import java.util.List;

public class FireEvent extends BaseEvent {
    public FireEvent() {
        super("fire", "A fire spreads in the town");
    }

    @Override
    public void runEvent(TurnGame game) {
        List<BaseZone> zones = game.getBoard().getZones();

        for (BaseZone z = RandomPicker.pick(zones), previous = null; // Extract a main.zone
            // If it's the first main.zone or is near the previous main.zone, AND there are buildings, spread fire
             (previous == null || z.isNear(previous)) && !z.getBuildings().isEmpty();
             previous = z, z = RandomPicker.pick(zones)) {
            // Remove all buildings
            z.getBuildings().forEach(z::removeBuilding);
        }
    }
}
