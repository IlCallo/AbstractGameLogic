package blazing.tears.event;

import blazing.tears.TurnGame;

interface Event {
    void runEvent(TurnGame game);
}
