package hsfweb.controller;

import hsfweb.controller.endpoints.ResponseMapper;
import hsfweb.model.Address;
import hsfweb.model.Contact;
import hsfweb.model.District;
import hsfweb.model.FacilityCategory;
import hsfweb.model.dto.DistanceUtil;
import hsfweb.model.dto.FacilityDTO;
import hsfweb.repository.*;
import hsfweb.service.FacilityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hsfweb.model.dto.DistanceUtil.getDistanceToLocation;
import static java.lang.Double.parseDouble;


/**
 * Created by scott on 30/04/2017.
 */

@RestController
public class FinderEndpoints {
    private static final long FACILITY_SEARCH_LIMIT = 10L;
    private static final long ADDRESS_SEARCH_LIMIT = 10L;

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private FacilityCategoryRepository facilityCategoryRepository;


    @GetMapping(value = {"/api/facility/list", "/api/v1/facility/list"})
    public ResponseEntity<List<Map<String, Object>>> getFacilities(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "facilityType", required = false) String facilityType,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "district", required = false) String districtName) {

        final FacilityCategory facilityCategory;
        final District district;

        if (StringUtils.isNotEmpty(districtName)) {

            final List<District> hpaDistricts = districtRepository.findByName(districtName);

            district = hpaDistricts.isEmpty() ? null : hpaDistricts.get(0);
        } else {

            district = null;
        }

        final List<FacilityDTO> facilities;

        if (StringUtils.isNotEmpty(facilityType) && StringUtils.isEmpty(searchTerm) && district == null) {
            facilities = facilityService.findFacilitiesByFacilityType(facilityType);
        } else if (district != null && StringUtils.isNotEmpty(searchTerm) && StringUtils.isEmpty(facilityType)) {
            facilities = facilityService.findFacilities(searchTerm, district);
        } else if (district == null && StringUtils.isNotEmpty(searchTerm) && StringUtils.isEmpty(facilityType)) {
            facilities = facilityService.findFacilities(searchTerm);
        } else if (district != null && StringUtils.isEmpty(searchTerm) && StringUtils.isEmpty(facilityType)) {
            facilities = facilityService.findFacilities("", district);
        } else {
            facilities = Collections.emptyList();
        }

        return getFacilities(facilities, latitude, longitude);
    }

    @GetMapping(value = {"/api/searchFacility/list", "/api/v1/searchFacility/list"})
    public ResponseEntity<List<Map<String, Object>>> getFacilities(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "district", required = false) String districtName,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude) {


        final District district;

        if (StringUtils.isNotEmpty(districtName)) {

            final List<District> hpaDistricts = districtRepository.findByName(districtName);
            district = hpaDistricts.isEmpty() ? null : hpaDistricts.get(0);

        } else {

            district = null;
        }

        final List<FacilityDTO> facilities =
                facilityService.findFacilities(searchTerm, district);

        return getFacilities(facilities, latitude, longitude);
    }

    public ResponseEntity<List<Map<String, Object>>> getFacilities(List<FacilityDTO> facilities, Double latitude,
                                                                   Double longitude) {

        if (facilities != null) {

            facilities.stream()
                    .forEach(f -> {

                        f.setContactDetail(contactRepository.findContacts(f.getId())
                                .stream()
                                .map(Contact::getContactDetail)
                                .collect(Collectors.joining(",")));
                        f.setAddressDetail(addressRepository.findAddresses(f.getId())
                                .stream()
                                .map(Address::toString)
                                .map(ResponseMapper::addressByRemovingControlChars)
                                .collect(Collectors.joining(System.lineSeparator())));
                    });

        }

        final List<FacilityDTO> facilitiesWithDistanceFromUser;

        if (facilities == null || latitude == null || longitude == null) {

            facilitiesWithDistanceFromUser = Collections.emptyList();

        } else {

            final DistanceUtil.Coordinate currentUserLocation = DistanceUtil.Coordinate.of(latitude, longitude);

            facilitiesWithDistanceFromUser = facilities.stream()
                    .filter(FacilityDTO::getHasCoordinate)
                    .map(f -> {

                        final Double lat = parseDouble(f.getLatitude());
                        final Double lng = parseDouble(f.getLongitude());

                        if (lat != null && lng != null) {

                            final DistanceUtil.Coordinate facilityCoordinate = DistanceUtil.Coordinate.of(lat, lng);
                            final Double facilityDistanceFomUserLocation =
                                    getDistanceToLocation(facilityCoordinate, currentUserLocation);
                            f.setDistanceFromLocation(facilityDistanceFomUserLocation);
                            f.assignDistanceDescription();
                        }

                        return f;
                    })
                    .sorted()
                    .limit(FACILITY_SEARCH_LIMIT)
                    .collect(Collectors.toList());
        }

        // if no facilities can be found by distance e.g no current location, show any random matching ones
        if (facilitiesWithDistanceFromUser.isEmpty()) {

            Collections.shuffle(facilities);

            final List<FacilityDTO> limitedRandomFacilities = facilities.stream()
                    .limit(FACILITY_SEARCH_LIMIT)
                    .collect(Collectors.toList());

            return ResponseMapper.toFacilitiesResponse(limitedRandomFacilities, true);

        } else {

            return ResponseMapper.toFacilitiesResponse(facilitiesWithDistanceFromUser);
        }

    }

    @GetMapping(value = {"/api/facilityType/list", "/api/v1/facilityType/list"})
    public ResponseEntity<List<String>> findFacilityTypes(@RequestParam(value = "category", required = false) String
                                                                  category) {

        final FacilityCategory facilityCategory;
        final List<String> facilityTypes;
        if (StringUtils.isNotEmpty(category)) {
            facilityCategory = facilityCategoryRepository.findByName(category);
            facilityTypes = facilityCategory.getFacilityTypes().stream().collect(Collectors.toList());
        } else {
            facilityTypes = facilityService.findFacilityTypes();
        }

        return ResponseMapper.toStringResponse(facilityTypes);
    }

}
