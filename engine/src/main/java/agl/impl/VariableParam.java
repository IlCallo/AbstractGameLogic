package agl.impl;

import agl.impl.zone.BaseZone;

public class VariableParam {
    private BaseZone mZone;
    private Unit mUnit;

    public VariableParam(BaseZone zone) {
        mZone = zone;
    }

    public VariableParam(Unit unit) {
        mUnit = unit;
    }

    public BaseZone getZone() {
        return mZone;
    }

    public Unit getUnit() {
        return mUnit;
    }
}
