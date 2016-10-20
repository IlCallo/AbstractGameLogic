package blazing.tears;

import blazing.tears.actor.Unit;
import blazing.tears.constant.GamePhase;
import blazing.tears.constant.GameStatus;
import blazing.tears.event.*;
import blazing.tears.group.Team;
import blazing.tears.objective.*;
import blazing.tears.role.RoleProvider;
import blazing.tears.utils.BoardFactory;
import blazing.tears.utils.RandomPicker;
import blazing.tears.zone.*;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static blazing.tears.constant.GamePhase.*;
import static blazing.tears.constant.GameStatus.*;


public class TurnGame implements Runnable {
    private static final Logger LOG = Logger.getLogger(TurnGame.class.getName());

    private static final int ZONES_NUM = 9;

    private static final int INITIAL_MONEY = 10;

    private static final int STARTING_ZONE_NUM = 3;

    private static final long ROLE_TIMEOUT = 2 * 1000 * 60;
    private static final long ACTION_TIMEOUT = 2 * 1000 * 60;
    private static final long MONEY_TIMEOUT = 1 * 1000 * 60;
    private static final long TURN_TIMEOUT = 15 * 1000 * 60;

    private static final String CHAOS_OBJECTIVE = "chaos";
    private static final int CHAOS_OBJECTIVE_NUM = 8;
    private static final String CONTROL_OBJECTIVE = "control";
    private static final int CONTROL_OBJECTIVE_NUM = 4;
    private static final String RICH_OBJECTIVE = "rich";
    private static final int RICH_OBJECTIVE_NUM = 50;
    private static final String OMNIPRESENT_OBJECTIVE = "omnipresent";
    private static final int OMNIPRESENT_OBJECTIVE_NUM = 8;
    private static final String PEACEFUL_OBJECTIVE = "peaceful";
    private static final int PEACEFUL_OBJECTIVE_NUM = 8;

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

    private boolean mTerminate;

    public TurnGame(DatabaseReference ref, GeoFire geo) {
        mRef = ref;
        mGeo = geo;
        mBoard = null;
        mTeams = new ArrayList<>();
        mUnits = new ArrayList<>();
        mEvents = new ArrayList<>();

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // start new game on his own thread
        (new Thread(new TurnGame(ref, new GeoFire(ref.child("geofire"))))).start();
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
                GameStatus status = GameStatus.valueOf(dataSnapshot.getValue(String.class));
                if (status == INACTIVE) {
                    mRef.child("game/status").setValue(INITIALIZE);
                    return;
                }

                LOG.log(Level.FINE, "Current status is {0}", status);

                switch (status) {
                    case INITIALIZE:
                        initialize();
                        break;
                    case PREPARE:
                        prepare();
                        break;
                    case START:
                        start(CONTROL);
                        break;
                    case PAUSE:
                        pause();
                        break;
                    case RESUME:
                        resume();
                        break;
                    case END:
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

    private void initialize() {
        // Retrieve board info from Firebase
        mRef.child("game/board").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lat = (double) dataSnapshot.child("center/latitude").getValue();
                double lng = (double) dataSnapshot.child("center/longitude").getValue();
                GeoLocation center = new GeoLocation(lat, lng);

                double radius = (double) dataSnapshot.child("radius").getValue();

                LOG.fine("Center and radius has been retrieved from Firebase");

                mTurn = 0;
                mEventProbability = 0.00;

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

                mBoard = BoardFactory.createBoard(center, radius, zones);

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
                                zone.setChaotic(true);
                                mRef.child("zone/" + zone.getId() + "/chaotic").setValue(true);
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

                                // Define color names
                                ArrayList<String> colorList = new ArrayList<>(Arrays.asList("red", "white",
                                        "yellow", "green", "purple"));

                                // Define objective placeholder
                                ArrayList<String> objectiveList = new ArrayList<>(Arrays.asList(CHAOS_OBJECTIVE, CONTROL_OBJECTIVE,
                                        RICH_OBJECTIVE, OMNIPRESENT_OBJECTIVE, PEACEFUL_OBJECTIVE));

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
                                        mRef.child("unit/" + unitId + "/ready").setValue(false);
                                        mRef.child("unit/" + unitId + "/lastBeat").setValue(null);
                                        mRef.child("unit/" + unitId + "/lastPosition").setValue(null);

                                        // Add the unit to the team members
                                        team.addMember(unit);
                                    }

                                    // Choose a random objective
                                    String o = RandomPicker.pick(objectiveList);
                                    Objective objective = null;

                                    switch (o) {
                                        case CHAOS_OBJECTIVE:
                                            objective = new ChaosObjective("", CHAOS_OBJECTIVE_NUM);
                                            break;
                                        case CONTROL_OBJECTIVE:
                                            objective = new ControlObjective("", CONTROL_OBJECTIVE_NUM, team);
                                            break;
                                        case RICH_OBJECTIVE:
                                            objective = new RichObjective("", RICH_OBJECTIVE_NUM, team);
                                            break;
                                        case OMNIPRESENT_OBJECTIVE:
                                            objective = new OmnipresentObjective("", OMNIPRESENT_OBJECTIVE_NUM, team);
                                            break;
                                        case PEACEFUL_OBJECTIVE:
                                            objective = new PeacefulObjective("", PEACEFUL_OBJECTIVE_NUM);
                                            break;
                                    }

                                    objectiveList.remove(o);

                                    // Set the objective
                                    team.setObjective(objective);
                                    mRef.child("team/" + team.getId() + "/objective").setValue(o);

                                    // Set color
                                    String c = RandomPicker.pick(colorList);
                                    colorList.remove(c);
                                    team.setColor(c);
                                    mRef.child("team/" + team.getId() + "/color").setValue(c);

                                    // Set initial money
                                    team.earnMoney(INITIAL_MONEY);
                                    mRef.child("team/" + team.getId() + "/money").setValue(INITIAL_MONEY);

                                    // Add the team to the team lists
                                    mTeams.add(team);

                                    LOG.log(Level.FINE, "Team {0} initialized with objective => {1}",
                                            new Object[]{team.getName(), team.getObjective().getClass().toString()});
                                }

                                mRef.child("game/unitsToWait").setValue(unitsToWait);

                                mRef.child("game/status").setValue(GameStatus.PREPARE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                LOG.log(Level.SEVERE, databaseError.toException().toString(), databaseError.toException());
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LOG.log(Level.SEVERE, databaseError.toException().toString(), databaseError.toException());
            }
        });
    }

    private void prepare() {
        System.exit(1);

        // Add listener for missing units until the game starts
        mRef.child("game/unitsToWait").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((long) dataSnapshot.getValue() == 0) {
                    mRef.child("game/unitsToWait").removeEventListener(this);

                    // TODO Add all Firebase listeners and start the game

                    mRef.child("game/status").setValue(GameStatus.START);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void start(GamePhase phase) {
        // Special checks if it's control phase
        if (phase == CONTROL) {
            if (meetsEndingCriteria()) {
                mRef.child("game/status").setValue(END);
            } else {
                mTurn++;
                mRef.child("game/turn").setValue(mTurn);

                resetCondition();

                checkEvent();

                activePowerUp();

                // Go to the next phase
                start(ROLE);
            }
        } else {
            // get current expiration time
            long expireTime = System.currentTimeMillis();
            TimerTask tt = null;

            // Set ad-hoc expiration time and timer task for every phase
            switch (phase) {
                case ROLE:
                    expireTime += ROLE_TIMEOUT;
                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            // Checks role conflicts and resolve them
                            chooseRole();
                            // Go to the next phase
                            start(ACTION);
                        }
                    };
                    break;
                case ACTION:
                    expireTime += ACTION_TIMEOUT;
                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            // Checks actions conflicts and resolve them
                            chooseAction();
                            // Go to the next phase
                            start(MONEY);
                        }
                    };
                    break;
                case MONEY:
                    expireTime += MONEY_TIMEOUT;
                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            // Checks money conflicts and resolve them
                            askLoan();
                            // Go to the next phase
                            start(TURN);
                        }
                    };
                    break;
                case TURN:
                    expireTime += TURN_TIMEOUT;
                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            // Checks turn conflicts and resolve them
                            turn();
                            // Go to the next phase
                            start(CONTROL);
                        }
                    };
                    break;
            }

            assert tt != null;
            // Start timer
            new Timer().schedule(tt, expireTime);

            // Set timer value on Firebase
            mRef.child("game/timer").setValue(expireTime);
        }

        // Updates phase
        mRef.child("game/phase").setValue(phase);
    }

    private void pause() {

    }

    private void resume() {

    }

    private void end() {

    }

    private boolean meetsEndingCriteria() {
        // Checks objectives of every team
        for (Team team : mTeams) {
            if (team.getObjective().checkVictory(this)) {
                return true;
            }
        }

        return false;
    }

    // Reset all turn-based conditions
    private void resetCondition() {
        // Remove fog of war
        mFog = false;

        // Remove roles from units (also removes invulnerability and multitasking)
        for (Unit u : getUnits()) {
            u.setRole(null);
        }

        // Reset teams' role pool (remove role pool enhancements)
        for (Team t : getTeams()) {
            t.setRolePool(RoleProvider.getInitialRolePool());
        }

    }

    private void checkEvent() {
        // Check if there are still events to fire out
        if (mEvents.size() > 0) {
            // If the event probability is at 0.00, it's first turn, we set it to a minimum value and finish here
            // No event can be fired in the first turn
            if (mEventProbability == 0.00) {
                mEventProbability = 0.25;
            } else {
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
        }
    }

    private void activePowerUp() {
        // Execute power-up for whom have right to use them
        mBoard.getZones().stream().filter(z -> z.getController() != null).forEach(z -> {
            z.usePowerUp();
        });
    }

    private void chooseRole() {

    }

    private void chooseAction() {
    }

    private void askLoan() {
    }

    private void turn() {
    }
}
