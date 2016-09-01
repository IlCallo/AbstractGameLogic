package agl.impl.event;

import agl.impl.TurnGame;

public class DemonsEvent extends BaseEvent {
    public DemonsEvent() {
        super("demons", "Some demons enter this city and wreak havoc");
    }

    @Override
    public void runEvent(TurnGame game) {

    }
}
