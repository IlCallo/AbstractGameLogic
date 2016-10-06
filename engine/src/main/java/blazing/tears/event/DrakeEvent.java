package blazing.tears.event;

import blazing.tears.TurnGame;
import blazing.tears.utils.RandomPicker;

public class DrakeEvent extends BaseEvent {
    public DrakeEvent() {
        super("drake", "A drake attacks a BaseZone killing all units and destroying all buildings");
    }

    @Override
    public void runEvent(TurnGame game) {
        // Extract a main.zone and clears it
        RandomPicker.pick(game.getBoard().getZones()).clear();
    }
}
