package agl.impl.zone;

import agl.impl.VariableParam;

public class MultitaskingZone extends BaseZone {

    public MultitaskingZone(int id, int cost) {
        super(id, cost, "The selected unit can pick both the actions for his role");
    }

    @Override
    public void usePower(VariableParam param) {
        param.getUnit().setMultitasking(true);
    }
}
