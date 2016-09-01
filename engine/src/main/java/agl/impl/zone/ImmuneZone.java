package agl.impl.zone;

import agl.impl.VariableParam;

public class ImmuneZone extends BaseZone {

    public ImmuneZone(int id, int cost) {
        super(id, cost, "The selected unit cannot be killed by assassins");
    }

    @Override
    public void usePower(VariableParam param) {
        param.getUnit().setImmune(true);
    }
}
