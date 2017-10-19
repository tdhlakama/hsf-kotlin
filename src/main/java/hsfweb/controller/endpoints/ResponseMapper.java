package hsfweb.controller.endpoints;

import hsfweb.model.Facility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scott on 22/05/2017.
 *
 * The purpose of this class is to ensure that the response sent to the client is not changed directly by changes to
 * the actual model classes e.g addition/removal of fields by mistake
 */
public final class ResponseMapper {

    private ResponseMapper() {}

    private static final String FACILITY_RANDOM_RESULTS_HEADER = "FinderRandomResults";
    private static final Pattern COMMA_PATTERN = Pattern.compile(",", Pattern.LITERAL);
    private static final Pattern ADDRESS_REMOVE_CHARS_PATTERN = Pattern.compile(System.lineSeparator() + "|\r|\\s+");

    public static <T extends BaseModel> ResponseEntity<List<Map<String, Object>>> toBaseModelResponse(List<T> baseModelList) {

        final List<Map<String, Object>> data = new ArrayList<>();
        baseModelList.forEach(o -> {

            Map<String, Object> dataItem = new HashMap<>();

            dataItem.put("id", o.getId());
            dataItem.put("name", o.getName());

            data.add(dataItem);
        });

        return cachedResponse(data);
    }

    public static ResponseEntity<List<String>> toStringResponse(List<String> response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.empty().cachePrivate().mustRevalidate())
                .body(response);
    }

    private static ResponseEntity<List<Map<String, Object>>> cachedResponse(List<Map<String, Object>> response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.empty().cachePrivate().mustRevalidate())
                .body(response);
    }

    private static ResponseEntity<List<Map<String, Object>>> nonCachedResponse(List<Map<String, Object>> response) {

        return ResponseEntity.ok()
                .body(response);
    }

    public static ResponseEntity<List<Map<String, Object>>> toFacilitiesResponse(List<Facility> facilities) {
        return toFacilitiesResponse(facilities, false);
    }

    public static ResponseEntity<List<Map<String, Object>>> toFacilitiesResponse(List<Facility> facilities,
                                                                                 boolean shouldIncludeRandomListHeader) {

        final List<Map<String, Object>> data = new ArrayList<>();
        facilities.forEach(f -> {

            Map<String, Object> dataItem = new HashMap<>();

            dataItem.put("id", f.getId());
            dataItem.put("name", f.getName());
            dataItem.put("latitude", f.getLatitude());
            dataItem.put("longitude", f.getLongitude());
            dataItem.put("distanceFromLocation", f.getDistanceFromLocation());
            dataItem.put("contactDetail", f.getContactDetail());
            dataItem.put("addressDetail", f.getAddressDetail());

            data.add(dataItem);
        });

        if (shouldIncludeRandomListHeader) {

            // Do not cache random results
            return ResponseEntity.ok()
                    .header(FACILITY_RANDOM_RESULTS_HEADER, "true")
                    .body(data);
        } else {

            return cachedResponse(data);
        }
    }

    public static String addressByRemovingControlChars(String address) {

        if (StringUtils.isEmpty(address)) {
            return "";
        }

        return ADDRESS_REMOVE_CHARS_PATTERN.matcher(address).replaceAll(Matcher.quoteReplacement(" "));

    }

    public static Double parseDouble(String stringWithDouble) {

        if (StringUtils.isEmpty(stringWithDouble)) {
            return null;
        }

        try {

            String cleanValue = COMMA_PATTERN.matcher(stringWithDouble).replaceAll(Matcher.quoteReplacement("."));
            Double value = Double.parseDouble(stringWithDouble);

            return value;

        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

