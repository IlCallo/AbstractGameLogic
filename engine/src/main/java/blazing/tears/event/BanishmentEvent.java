package blazing.tears.event;

import blazing.tears.TurnGame;
import blazing.tears.utils.RandomPicker;

public class BanishmentEvent extends BaseEvent {
    public BanishmentEvent() {
        super("banishment", "The governor of a main.zone is banished from the city, the area is without lead");
    }

    @Override
    public void runEvent(TurnGame game) {
        // Extract a main.zone and disable its power-up
        RandomPicker.pick(game.getBoard().getZones()).setPowerUpEnabled(false);
    }
}
