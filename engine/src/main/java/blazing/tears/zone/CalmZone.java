package blazing.tears.zone;

import blazing.tears.utils.RandomPicker;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CalmZone extends BaseZone {

    public CalmZone(int id, int cost) {
        super(id, cost, "Calm a nearby main.zone");
    }

    @Override
    protected void powerUp() {
        ArrayList<BaseZone> chaoticNearZones = new ArrayList<>();

        // If this main.zone is chaotic, calm it down
        if (isChaotic()) {
            setChaotic(false);
        } else {
            // Get all chaotic zones near this one
            chaoticNearZones.addAll(getNearZones().stream().filter(BaseZone::isChaotic).collect(Collectors.toList()));

            // If there is at last one of them, extract a main.zone and calm it
            if (chaoticNearZones.size() != 0) {
                RandomPicker.pick(chaoticNearZones).setChaotic(false);
            }
        }
    }
}
