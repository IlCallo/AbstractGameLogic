package agl.impl.zone;

import agl.impl.VariableParam;

public class CalmZone extends BaseZone {

    public CalmZone(int id, int cost) {
        super(id, cost, "Calm a nearby zone");
    }

    @Override
    public void usePower(VariableParam param) {
        if (param.getZone().isNear(this)) {
            param.getZone().setChaotic(false);
        }
    }
}
