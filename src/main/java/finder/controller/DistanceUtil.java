package finder.controller;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * Created by tdhla on 4/27/2017.
 */
public class DistanceUtil {

    public static Double getDistanceToLocation(Coordinate from, Coordinate to) {

        if (from == null || from.longitude == null || from.latitude == null || to == null || to.longitude == null ||
                to.longitude == null) {
            return null;
        }

        LatLng userPoint = new LatLng(from.latitude, from.longitude);
        LatLng pointOfInterest = new LatLng(to.latitude, to.longitude);

        return LatLngTool.distance(pointOfInterest, userPoint, LengthUnit.METER);

    }

    public static String formatNumber(Double d) {

        NumberFormat numberFormat = new DecimalFormat("#.0");
        String value = numberFormat.format(d);
        return value;
    }

    public static class Coordinate {

        private Double latitude;
        private Double longitude;

        private Coordinate() {
        }

        public static Coordinate of(Double latitude, Double longitude) {

            Objects.requireNonNull(latitude, "Latitude is required");
            Objects.requireNonNull(longitude, "Longitude is required");

            final Coordinate coordinate = new Coordinate();
            coordinate.latitude = latitude;
            coordinate.longitude = longitude;

            return coordinate;
        }
    }


}
