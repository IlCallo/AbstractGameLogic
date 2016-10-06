package blazing.tears.event;

import blazing.tears.TurnGame;

public class FogEvent extends BaseEvent {
    public FogEvent() {
        super("fog", "Fog invade the city");
    }

    @Override
    public void runEvent(TurnGame game) {
        game.activeFog();
    }
}
