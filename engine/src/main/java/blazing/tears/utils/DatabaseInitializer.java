package blazing.tears.utils;

import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

import static blazing.tears.constant.GameStatus.INACTIVE;

public class DatabaseInitializer {
    private static final int TEAM_NUM = 5;
    private static final int TEAM_UNIT_NUM = 5;
    private static final List<String> TEAM_NAMES = Arrays.asList("Crotali di Satana", "Teemonomicon", "World Wide Derp",
            "Pussycats", "Blblblbonna");
    private static final List<String> TEAM_COLORS = Arrays.asList("red", "white", "yellow", "green", "purple");

    public static void setup(DatabaseReference ref) {
        // Clean the database
        ref.setValue(null);
        // Game status is now inactive
        ref.child("game").setValue(INACTIVE);

        // Creates teams, units for every team and add them to firebase
        for (int i = 0; i < TEAM_NUM; i++) {
            DatabaseReference teamRef = ref.child("team").push();
            teamRef.child("name").setValue(TEAM_NAMES.get(i));
            teamRef.child("color").setValue(TEAM_COLORS.get(i));

            // Creates units for every team and set them as team members
            for (int j = 0; j < TEAM_UNIT_NUM; j++) {
                DatabaseReference unitRef = ref.child("unit").push();
                unitRef.child("email").setValue("dummy" + i + "-" + j + "@test.com");
                unitRef.child("team").setValue(teamRef.getKey());

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            // Custom accounts to test the app
            if (i == 1) {
                DatabaseReference unitRef = ref.child("unit").push();
                unitRef.child("email").setValue("o.o_callo_o.o@hotmail.it");
                unitRef.child("team").setValue(teamRef.getKey());

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            if (i == 2) {
                DatabaseReference unitRef = ref.child("unit").push();
                unitRef.child("email").setValue("fbonnab@gmail.com");
                unitRef.child("team").setValue(teamRef.getKey());

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            if (i == 3) {
                DatabaseReference unitRef = ref.child("unit").push();
                unitRef.child("email").setValue("pcalloc@gmail.com");
                unitRef.child("team").setValue(teamRef.getKey());

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }

            if (i == 4) {
                DatabaseReference unitRef = ref.child("unit").push();
                unitRef.child("email").setValue("callo92thebest@yahoo.it");
                unitRef.child("team").setValue(teamRef.getKey());

                teamRef.child("members/" + unitRef.getKey()).setValue(true);
            }
        }
    }
}
