package agl.impl;

import agl.impl.event.*;
import agl.impl.objective.*;
import agl.impl.utils.RandomPicker;
import agl.impl.zone.*;
import agl.utils.BoardFactory;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TurnGame implements Runnable {
    private static final Logger LOG = Logger.getLogger(TurnGame.class.getName());

    private static final int ZONES_NUM = 9;

    private static final int INITIAL_MONEY = 10;

    private static final int STARTING_ZONE_NUM = 3;

    private static final String OBJECTIVE_CHAOS = "chaos";
    private static final int OBJECTIVE_CHAOS_NUM = 8;
    private static final String OBJECTIVE_CONTROL = "control";
    private static final int OBJECTIVE_CONTROL_NUM = 4;
    private static final String OBJECTIVE_CUTRO = "cutro";
    private static final int OBJECTIVE_CUTRO_NUM = 50;
    private static final String OBJECTIVE_OMNIPRESENT = "omnipresent";
    private static final int OBJECTIVE_OMNIPRESENT_NUM = 9;
    private static final String OBJECTIVE_PEACEFUL = "peaceful";
    private static final int OBJECTIVE_PEACEFUL_NUM = 9;

    private static final String INITIALIZE_PHASE = "initialize";
    private static final String PREPARE_PHASE = "prepare";
    private static final String START_PHASE = "start";
    private static final String PAUSE_PHASE = "pause";
    private static final String RESUME_PHASE = "resume";
    private static final String END_PHASE = "end";

    private int mTurn;
    private Board mBoard;
    private ArrayList<Team> mTeams;
    private ArrayList<Unit> mUnits;

    private ArrayList<BaseEvent> mEvents;
    private double mEventProbability;

    // Game affecting statuses
    private boolean mFog;

    private DatabaseReference mRef;
    private GeoFire mGeo;

    private GeoLocation mCenter;
    private double mDiameter;

    private boolean mTerminate;

    public TurnGame(GeoLocation center, double diameter, DatabaseReference ref, GeoFire geo) {
        mRef = ref;
        mGeo = geo;
        mBoard = null;
        mTeams = new ArrayList<>();
        mUnits = new ArrayList<>();
        mEvents = new ArrayList<>();

        mCenter = center;
        mDiameter = diameter;

        mTerminate = false;
    }

    public static void main(String[] args) {
        // get input stream
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("engine/SeriousGames-6953f1c36618.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LOG.fine("Reading Firebase database token");

        // connect to firebase
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(fis)
                .setDatabaseUrl("https://seriousgames.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);

        LOG.fine("Firebase database access initialized");

        GeoLocation center = new GeoLocation(44.698610, 10.630919); // Reggio Emilia center
        double diameter = 1.6; // Reggio Emilia historical center diameter

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // start new game on his own thread
        (new Thread(new TurnGame(center, diameter, ref, new GeoFire(ref.child("geofire"))))).start();
    }

    public int getTurn() {
        return mTurn;
    }

    public Board getBoard() {
        return mBoard;
    }

    public void setBoard(Board board) {
        mBoard = board;
    }

    public ArrayList<Team> getTeams() {
        return mTeams;
    }

    public void setTeams(ArrayList<Team> teams) {
        mTeams = teams;
    }

    public ArrayList<Unit> getUnits() {
        return mUnits;
    }

    public void setUnits(ArrayList<Unit> units) {
        mUnits = units;
    }

    public void activeFog() {
        mFog = true;
    }

    @Override
    public void run() {
        LOG.log(Level.FINE, "Game started");

        mRef.child("game/status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phase = (String) dataSnapshot.getValue();
                if (phase == null) {
                    mRef.child("game/status").setValue(INITIALIZE_PHASE);
                    return;
                }

                LOG.log(Level.FINE, "Current phase is {0}", phase);

                switch (phase) {
                    case INITIALIZE_PHASE:
                        initialize(mCenter, mDiameter);
                        break;
                    case PREPARE_PHASE:
                        prepare();
                        break;
                    case START_PHASE:
                        start();
                        break;
                    case PAUSE_PHASE:
                        pause();
                        break;
                    case RESUME_PHASE:
                        resume();
                        break;
                    case END_PHASE:
                        end();
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LOG.log(Level.SEVERE, databaseError.toException().toString(), databaseError.toException());
            }
        });

        do {
        } while (!mTerminate);
    }

    private void initialize(GeoLocation center, double diameter) {
        mTurn = 0;
        mEventProbability = 0.25;

        // Initialize general game info
        mRef.child("game/turn").setValue(mTurn);
        mRef.child("game/event/probability").setValue(mEventProbability);

        // Load events
        mEvents.add(new BanishmentEvent());
        mEvents.add(new DemonsEvent());
        mEvents.add(new DrakeEvent());
        mEvents.add(new EarthquakeEvent());
        mEvents.add(new ExplosionEvent());
        mEvents.add(new FireEvent());
        mEvents.add(new FogEvent());
        mEvents.add(new TaxEvent());

        // Clean previous data
        mRef.child("game/event/possible").setValue(null);

        // Load events reference
        for (BaseEvent event : mEvents) {
            mRef.child("game/event/possible").push().setValue(event.getName());
        }

        // Initialize zones and board
        ArrayList<BaseZone> zones = new ArrayList<>();
        zones.add(new MoneyZone(1, 7, 5));
        zones.add(new MoneyZone(2, 7, 5));
        zones.add(new MoneyZone(3, 4, 3));
        zones.add(new MoneyZone(4, 4, 3));
        zones.add(new CalmZone(5, 7));
        zones.add(new RoleZone(6, 4));
        zones.add(new RoleZone(7, 4));
        zones.add(new ImmuneZone(8, 9));
        zones.add(new MultitaskingZone(9, 9));

        mBoard = BoardFactory.createBoard(center, diameter, zones);

        // load zones into db
        mRef.child("zone").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                for (BaseZone z : zones) {
                    MutableData localMutableData = mutableData.child(Integer.toString(z.getId()));
                    localMutableData.child("id").setValue(z.getId());
                    localMutableData.child("cost").setValue(z.getCost());
                    localMutableData.child("description").setValue(z.getDescription());
                    localMutableData.child("chaotic").setValue(false);
                    localMutableData.child("center").setValue(z.getCenter());

                    for (BaseZone z1 : z.getNearZones()) {
                        localMutableData.child("near/" + z1.getId()).setValue(true);
                    }

                    for (int i = 0; i < z.getPerimeter().size(); i++) {
                        localMutableData.child("perimeter/" + i).setValue(z.getPerimeter().get(i));
                    }
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    LOG.log(Level.SEVERE, databaseError.toException().toString(), databaseError.toException());
                }

                // Extract the starting zones
                ArrayList<BaseZone> possibleStartingZones = new ArrayList<>();

                for (int i = 0; i < STARTING_ZONE_NUM; i++) {
                    BaseZone zone = RandomPicker.pick(zones);
                    if (possibleStartingZones.contains(zone)) {
                        i--;
                    } else {
                        possibleStartingZones.add(zone);
                    }

                    LOG.log(Level.FINE, "Starting location n. {0} will be zone {1} which is a {2}",
                            new Object[]{i, zone.getId(), zone.getClass().toString()});
                }

                // Get teams
                mRef.child("team").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Number of units to wait
                        long unitsToWait = 0;

                        // Define objective placeholder
                        ArrayList<String> objectiveList = new ArrayList<>(Arrays.asList(OBJECTIVE_CHAOS, OBJECTIVE_CONTROL,
                                OBJECTIVE_CUTRO, OBJECTIVE_OMNIPRESENT, OBJECTIVE_PEACEFUL));

                        // Cycle all registered teams
                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                            // Get team name and create the instance
                            Team team = new Team(ds1.getKey(), (String) ds1.child("name").getValue());

                            // Cycle all team members
                            for (DataSnapshot ds2 : ds1.child("members").getChildren()) {
                                // Get unit name and create the instance
                                String unitId = ds2.getKey();
                                Unit unit = new Unit(unitId, team);
                                unitsToWait++;

                                // Decide starting point of unit, taking the one with less units inside
                                BaseZone startZone = possibleStartingZones.get(0);
                                for (int i = 1; i < possibleStartingZones.size(); i++) {
                                    if (possibleStartingZones.get(i).getUnits().size() < startZone.getUnits().size()) {
                                        startZone = possibleStartingZones.get(i);
                                    }
                                }
                                startZone.addUnit(unit);
                                mRef.child("unit/" + unitId + "/startPosition").setValue(startZone.getCenter());

                                // Add the unit to the team members
                                team.addMember(unit);
                            }

                            // Choose a random objective
                            String o = RandomPicker.pick(objectiveList);
                            Objective objective = null;

                            switch (o) {
                                case OBJECTIVE_CHAOS:
                                    objective = new ChaosObjective("", OBJECTIVE_CHAOS_NUM);
                                    break;
                                case OBJECTIVE_CONTROL:
                                    objective = new ControlObjective("", OBJECTIVE_CONTROL_NUM, team);
                                    break;
                                case OBJECTIVE_CUTRO:
                                    objective = new CutroObjective("", OBJECTIVE_CUTRO_NUM, team);
                                    break;
                                case OBJECTIVE_OMNIPRESENT:
                                    objective = new OmnipresentObjective("", OBJECTIVE_OMNIPRESENT_NUM, team);
                                    break;
                                case OBJECTIVE_PEACEFUL:
                                    objective = new PeacefulObjective("", OBJECTIVE_PEACEFUL_NUM);
                                    break;
                            }

                            objectiveList.remove(o);

                            // Set the objective
                            team.setObjective(objective);
                            mRef.child("team/" + team.getId() + "/objective").setValue(o);

                            // Set initial money
                            team.earnMoney(INITIAL_MONEY);
                            mRef.child("team/" + team.getId() + "/money").setValue(INITIAL_MONEY);

                            // Add the team to the team lists
                            mTeams.add(team);

                            LOG.log(Level.FINE, "Team {0} initialized with objective => {1}",
                                    new Object[]{team.getName(), team.getObjective().getClass().toString()});
                        }

                        mRef.child("game/unitsToWait").setValue(unitsToWait);

                        mRef.child("game/status").setValue(PREPARE_PHASE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        LOG.log(Level.SEVERE, databaseError.toException().toString(), databaseError.toException());
                    }
                });
            }
        });
    }

    private void prepare() {
        // Add listener for missing units until the game starts
        mRef.child("game/unitsToWait").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((long) dataSnapshot.getValue() == 0) {
                    mRef.child("game/unitsToWait").removeEventListener(this);

                    // TODO Add all Firebase listeners and start the game

                    mRef.child("game/status").setValue(START_PHASE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void start() {
        // DUMMY
        mTerminate = true;

        while (!meetsEndingCriteria()) {
            turn();
        }

        mRef.child("game/status").setValue(END_PHASE);
    }

    private void pause() {

    }

    private void resume() {

    }

    private void end() {

    }

    private boolean meetsEndingCriteria() {
        for (Team team : mTeams) {
            if (team.getObjective().checkVictory(this)) {
                return true;
            }
        }

        return false;
    }

    private void turn() {
        resetConditions();

        leadersTurn();

        unitsTurn();

        // Check if there are still events to fire out
        if (mEvents.size() > 0) {
            // Check if an event take place
            if (ThreadLocalRandom.current().nextDouble() <= mEventProbability) {
                // Reset event possibility to the minimum
                mEventProbability = 0.25;
                // Extract an event, execute it and remove it from the list
                int index = ThreadLocalRandom.current().nextInt(mEvents.size());
                mEvents.get(index).runEvent(this);
                mEvents.remove(index);
            } else {
                // Double the possibility for an event to take place
                mEventProbability *= 2;
            }
        }

        mTurn++;
        mRef.child("game/turn").setValue(mTurn);
    }

    // Reset all turn-based conditions
    private void resetConditions() {
        // Remove fog of war
        mFog = false;

        // remove unit invulnerability and multitasking

    }

    private void leadersTurn() {

    }

    private void unitsTurn() {

    }
}
