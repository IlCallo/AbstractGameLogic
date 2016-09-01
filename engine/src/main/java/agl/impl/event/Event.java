package agl.impl.event;

import agl.impl.TurnGame;

interface Event {
    void runEvent(TurnGame game);
}
