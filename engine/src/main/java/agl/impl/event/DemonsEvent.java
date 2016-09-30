package agl.impl.event;

import agl.impl.TurnGame;
import agl.impl.utils.RandomPicker;
import agl.impl.zone.BaseZone;

import java.util.List;

public class DemonsEvent extends BaseEvent {
    public DemonsEvent() {
        super("demons", "Some demons enter the city and wreak havoc");
    }

    @Override
    public void runEvent(TurnGame game) {
        List<BaseZone> zones = game.getBoard().getZones();

        // Add a demon in 5 areas (more demons per area may be added)
        for (int i = 0; i < 5; i++) {
            RandomPicker.pick(zones).addDemon();
        }
    }
}
