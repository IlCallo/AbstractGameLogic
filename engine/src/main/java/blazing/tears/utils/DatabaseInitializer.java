package blazing.tears.utils;

import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static blazing.tears.constant.GamePhase.CONTROL;
import static blazing.tears.constant.GameStatus.INACTIVE;
import static blazing.tears.role.Role.UNSPECIFIED;

public class DatabaseInitializer {
    private static final Logger LOG = Logger.getLogger(DatabaseInitializer.class.getName());

    private static final int TEAM_NUM = 5;
    private static final int TEAM_UNIT_NUM = 5;
    private static final List<String> TEAM_NAMES = Arrays.asList("Crotali di Satana", "Teemonomicon", "World Wide Derp",
            "Pussycats", "Blblblbonna");
    private static final List<String> TEAM_COLORS = Arrays.asList("red", "white", "yellow", "green", "purple");

    public static void setup(DatabaseReference ref) {
        LOG.fine("Initializing the database");

        // Clean the database
        ref.setValue(null);
        // Game status is now inactive
        ref.child("game/status").setValue(INACTIVE);

        // Default game phase is control
        ref.child("game/phase").setValue(CONTROL);

        // Set dummy timer at 0
        ref.child("game/timer").setValue(0);

        // Set board center and radius
        ref.child("game/board/center/latitude").setValue(44.698403);
        ref.child("game/board/center/longitude").setValue(10.631783);
        ref.child("game/board/radius").setValue(0.825);

        LOG.fine("Creating dummy teams and units");

        // Creates teams, units for every team and add them to firebase
        for (int i = 0; i < TEAM_NUM; i++) {
            DatabaseReference teamRef = ref.child("team").push();
            teamRef.child("name").setValue(TEAM_NAMES.get(i));
            teamRef.child("color").setValue(TEAM_COLORS.get(i));
            teamRef.child("money/loan").setValue(0);
            teamRef.child("money/maximum").setValue(0);

            // Creates units for every team and set them as team members
            for (int j = 0; j < TEAM_UNIT_NUM; j++) {
                DatabaseReference unitRef = ref.child("unit").push();
                unitRef.child("username").setValue("dummy" + i + "-" + j);
                unitRef.child("team").setValue(teamRef.getKey());
                unitRef.child("money").setValue(0);
                unitRef.child("role").setValue(UNSPECIFIED);

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            // Custom accounts to test the app
            if (i == 1) {
                DatabaseReference unitRef = ref.child("unit/7g32vZ6Pw1MHU2Fm0oZ9hbYpmPv1");
                unitRef.child("username").setValue("IlCallo");
                unitRef.child("team").setValue(teamRef.getKey());
                unitRef.child("money").setValue(0);
                unitRef.child("role").setValue(UNSPECIFIED);

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            if (i == 2) {
                DatabaseReference unitRef = ref.child("unit/MyJDDb9WmHPUCuIeHTqbFQbIdJn2");
                unitRef.child("username").setValue("bonna");
                unitRef.child("team").setValue(teamRef.getKey());
                unitRef.child("money").setValue(0);
                unitRef.child("role").setValue(UNSPECIFIED);

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            if (i == 3) {
                DatabaseReference unitRef = ref.child("unit/IpTzc5KKiNYigtPQaEvnIZMZNNS2");
                unitRef.child("username").setValue("WildCalloAppears");
                unitRef.child("team").setValue(teamRef.getKey());
                unitRef.child("money").setValue(0);
                unitRef.child("role").setValue(UNSPECIFIED);

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            if (i == 4) {
                DatabaseReference unitRef = ref.child("unit/hZuNKH7CqhfGZG4VUDZzunHHv3e2");
                unitRef.child("username").setValue("Calloose");
                unitRef.child("team").setValue(teamRef.getKey());
                unitRef.child("money").setValue(0);
                unitRef.child("role").setValue(UNSPECIFIED);

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }
        }
    }
}
