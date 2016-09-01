package agl.impl.event;

import agl.impl.TurnGame;
import agl.impl.utils.RandomPicker;

public class DrakeEvent extends BaseEvent {
    public DrakeEvent() {
        super("drake", "A drake attacks a BaseZone killing all units and destroying all buildings");
    }

    @Override
    public void runEvent(TurnGame game) {
        RandomPicker.pick(game.getBoard().getZones()).clear();
    }
}
