package agl.utils;

import agl.impl.Board;
import agl.impl.TurnGame;
import agl.impl.zone.BaseZone;
import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardFactory {
    private static final Logger LOG = Logger.getLogger(TurnGame.class.getName());

    private static double EARTH_RADIUS = 6371.0;

    /**
     * @param center   A GeoPosition object which will be the center of the board
     * @param diameter Diameter of the board, in km
     * @return The Board instance
     */
    public static Board createBoard(GeoLocation center, double diameter, List<BaseZone> zones) {
        LOG.log(Level.FINE, "Board will be centered on {0} and will have a diameter of {1} km", new Object[]{center, diameter});

        // Allocate GeoPosition arrays for the zones points
        GeoLocation[][] coordinates = new GeoLocation[9][];
        coordinates[0] = new GeoLocation[8];
        coordinates[1] = new GeoLocation[8];
        coordinates[2] = new GeoLocation[8];
        coordinates[3] = new GeoLocation[8];
        coordinates[4] = new GeoLocation[8];
        coordinates[5] = new GeoLocation[12];
        coordinates[6] = new GeoLocation[12];
        coordinates[7] = new GeoLocation[12];
        coordinates[8] = new GeoLocation[12];

        // Zones delimiters:
        // central area, 8 points, 45° between each all distant 1/10D from center
        // next 4 areas: 8 points each, 3 on little arc with 45° between each distant 1/10D from center,
        //                              5 on large arc 22.5° between each distant 3/10 from center
        // next 4 areas: 12 points each, 5 on little arc with 22.5° between each distant 3/10D from center,
        //                              7 on large arc 15° between each arc distant 1/2D from center

        // calc points of first area and little arcs of first ring
        for (int a = 0, i = 0; a != 360; a += 45, i++) {
            int currentZoneNum = a / 90 + 1;
            // TODO there must be a way to relate this to the 360° repeat, probably via trigonometric functions
            int precedingZoneNum = (currentZoneNum == 1) ? 4 : currentZoneNum - 1;

            // We must put the denominator as a double or the result of 1/10 will be always 0
            GeoLocation vertex = calcDest(center, 1 / 10.0 * diameter, a);

            // save vertex for the central area
            coordinates[0][i] = vertex;

            // if division result is zero, the point is between two areas
            if (a % 90 == 0) {
                coordinates[currentZoneNum][0] = vertex;
                coordinates[precedingZoneNum][2] = vertex;
            } else {
                // else, it's 45° and we are in a midpoint of a little arc
                coordinates[currentZoneNum][1] = vertex;
            }
        }

        LOG.fine("First round of coordinates has been calculated");

        // calc points of large arc of first ring and little arc of second ring
        // must be done in reverse order to get a closed shape figure
        for (float a = 360; a != 0; a -= 22.5) {
            // (a - 1) is done because otherwise the currentZoneNumber would have gone to 5. We must switch to trigonometry
            int currentZoneNum = (int) (a - 1) / 90 + 1;
            // TODO there must be a way to relate this to the 360° repeat, probably via trigonometric functions
            int precedingZoneNum = (currentZoneNum == 4) ? 1 : currentZoneNum + 1;

            int extCurrentZoneNum = currentZoneNum + 4;
            int extPrecedingZoneNum = precedingZoneNum + 4;

            //LOG.log( Level.FINER, "currentZone: {0}, precedingZone: {1}, extCurrentZone: {2}, extPrecedingZone: {3}, angle: {4}",
            // new Object[]{currentZoneNum, precedingZoneNum, extCurrentZoneNum, extPrecedingZoneNum, a});

            // We must put the denominator as a double or the result of 3/10 will be always 0
            GeoLocation vertex = calcDest(center, 3 / 10.0 * diameter, a);

            // degree of shift inside a single zone of first ring large arc
            float relativeShift = a % 90;

            // if division result is zero, the point is between two zones of first ring
            if (relativeShift == 0) {
                // point shared between two areas of the first ring:
                // first of the large arc for the current zone
                // last of the large arc for the preceding zone
                coordinates[currentZoneNum][3] = vertex;
                coordinates[precedingZoneNum][7] = vertex;

                // midpoint of little arc of zone on second ring
                coordinates[extPrecedingZoneNum][2] = vertex;
            } else {
                // calculate the vertex index relative to the large arc of the zone in first ring
                int index = (int) (relativeShift / 22.5);
                // saves it in the right index
                coordinates[currentZoneNum][index + 3] = vertex;

                // add vertex to little arc of zones on the second ring
                switch (index) {
                    case 1:
                        coordinates[extPrecedingZoneNum][3] = vertex;
                        break;
                    case 2:
                        coordinates[extPrecedingZoneNum][4] = vertex;
                        coordinates[extCurrentZoneNum][0] = vertex;
                        break;
                    case 3:
                        coordinates[extCurrentZoneNum][1] = vertex;
                        break;
                }
            }
        }

        LOG.fine("Second round of coordinates has been calculated");

        // calc points of large arc of second ring
        // the range should actually be [-45,315] otherwise the borders of the zones won't be aligned with previous calc,
        // but we keep the [0,360] and shift -45° only when we must find the vertex position because it's more comfortable
        for (int a = 0, i = 0; a != 360; a += 15, i++) {
            int currentZoneNum = (a < 315) ? (a + 45) / 90 + 5 : 5;
            // TODO there must be a way to relate this to the 360° repeat, probably via trigonometric functions
            int precedingZoneNum = (currentZoneNum == 5) ? 8 : currentZoneNum - 1;

            //LOG.log( Level.FINER, "currentZone: {0}, precedingZone: {1}, angle: {2}", new Object[]{currentZoneNum, precedingZoneNum, a});

            // we shift 45° back to start aligned with the zones border
            GeoLocation vertex = calcDest(center, diameter / 2, a - 45);

            int relativeShift = a % 90;

            // if division result is zero, the point is between two zones of second ring
            if (relativeShift == 0) {
                // point shared between two areas of the second ring:
                // first of the large arc for the current zone
                // last of the large arc for the preceding zone
                coordinates[currentZoneNum][5] = vertex;
                coordinates[precedingZoneNum][11] = vertex;

            } else {
                // calculate the vertex index relative to the large arc of the zone in second ring
                int index = relativeShift / 15;
                // saves it in the right index
                coordinates[currentZoneNum][index + 5] = vertex;
            }
        }

        LOG.fine("Third and last round of coordinates has been calculated");

        // Translate array of points to list of points and add them to zones
        for (int i = 0; i < 9; i++) {
            ArrayList<GeoLocation> perimeter = new ArrayList<>(Arrays.asList(coordinates[0]));
            zones.get(i).setPerimeter(perimeter);
        }

        // TODO find a way to optimize this
        // Add near zones
        zones.get(0).setNearZones(new ArrayList<>(Arrays.asList(zones.get(1), zones.get(2), zones.get(3), zones.get(4))));
        zones.get(1).setNearZones(new ArrayList<>(Arrays.asList(zones.get(0), zones.get(2), zones.get(4), zones.get(8), zones.get(5))));
        zones.get(2).setNearZones(new ArrayList<>(Arrays.asList(zones.get(0), zones.get(1), zones.get(3), zones.get(5), zones.get(6))));
        zones.get(3).setNearZones(new ArrayList<>(Arrays.asList(zones.get(0), zones.get(2), zones.get(4), zones.get(6), zones.get(7))));
        zones.get(4).setNearZones(new ArrayList<>(Arrays.asList(zones.get(0), zones.get(1), zones.get(3), zones.get(7), zones.get(8))));
        zones.get(5).setNearZones(new ArrayList<>(Arrays.asList(zones.get(1), zones.get(2), zones.get(8), zones.get(6))));
        zones.get(6).setNearZones(new ArrayList<>(Arrays.asList(zones.get(2), zones.get(3), zones.get(5), zones.get(7))));
        zones.get(7).setNearZones(new ArrayList<>(Arrays.asList(zones.get(3), zones.get(4), zones.get(6), zones.get(8))));
        zones.get(8).setNearZones(new ArrayList<>(Arrays.asList(zones.get(4), zones.get(1), zones.get(7), zones.get(5))));

        // create
        return new Board(zones);
    }

    private static GeoLocation calcDest(GeoLocation start, double distance, double bearing) {
        double d = distance / EARTH_RADIUS;
        double b = Math.toRadians(bearing);
        double lat1 = Math.toRadians(start.latitude);
        double lon1 = Math.toRadians(start.longitude);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(b));

        double a = Math.atan2(Math.sin(b) * Math.sin(d) * Math.cos(lat1), Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));
        double lon2 = lon1 + a;
        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        return new GeoLocation(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }
}
