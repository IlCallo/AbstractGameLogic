package agl.impl.event;

import agl.impl.TurnGame;
import agl.impl.utils.RandomPicker;

public class BanishmentEvent extends BaseEvent {
    public BanishmentEvent() {
        super("banishment", "The governor of a zone is banished from the city, the area is without lead");
    }

    @Override
    public void runEvent(TurnGame game) {
        // Extract a zone and disable its power-up
        RandomPicker.pick(game.getBoard().getZones()).setPowerUpEnabled(false);
    }
}
