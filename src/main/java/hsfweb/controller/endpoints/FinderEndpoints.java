//package hsfweb.controller.endpoints;
//
//import hsfweb.model.*;
//import hsfweb.model.dto.DistanceUtil;
//import hsfweb.model.dto.FacilityDTO;
//import hsfweb.repository.AddressRepository;
//import hsfweb.repository.ContactRepository;
//import hsfweb.repository.DistrictRepository;
//import hsfweb.repository.FacilityRepository;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static hsfweb.model.dto.DistanceUtil.getDistanceToLocation;
//import static java.lang.Double.parseDouble;
//
//
///**
// * Created by scott on 30/04/2017.
// */
//
//@RestController
//@RequestMapping(value = "/api", method = RequestMethod.GET)
//public class FinderEndpoints {
//
//    private static final long FACILITY_SEARCH_LIMIT = 10L;
//    private static final long ADDRESS_SEARCH_LIMIT = 10L;
//
//    @Autowired
//    private ContactRepository contactRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private FacilityRepository facilityRepository;
//    @Autowired
//    private DistrictRepository districtRepository;
//
//
//    @GetMapping(value = {"facility/list", "v1/facility/list"})
//    public ResponseEntity<List<Map<String, Object>>> getFacilities(
//            @RequestParam(value = "searchTerm", required = false) String searchTerm,
//            @RequestParam(value = "category", required = false) String category,
//            @RequestParam(value = "facilityType", required = false) String facilityType,
//            @RequestParam(value = "latitude", required = false) Double latitude,
//            @RequestParam(value = "longitude", required = false) Double longitude,
//            @RequestParam(value = "district", required = false) String district) {
//
//        final FacilityCategory facilityCategory;
//
////        if (StringUtils.isNotEmpty(category)) {
////            facilityCategory = facilityCategoryDao.findByName(category);
////        } else {
////            facilityCategory = null;
////        }
//
//        final Optional<District> hpaDistrict;
//
//        if (StringUtils.isNotEmpty(district)) {
//
//            final List<District> hpaDistricts = districtRepository.findByName(district);
//            hpaDistrict = hpaDistricts.isEmpty() ? Optional.empty() : Optional.of(hpaDistricts.get(0));
//
//        } else {
//            hpaDistrict = Optional.empty();
//        }
//
//        final List<FacilityDTO> facilities = hpaFacilityDao.findFacilities(Optional.ofNullable(facilityType), sector,
//                Optional.ofNullable(facilityCategory), hpaDistrict);
//
//        return getFacilities(facilities, latitude, longitude);
//    }
//
//    @GetMapping(value = {"searchFacility/list", "v1/searchFacility/list"})
//    public ResponseEntity<List<Map<String, Object>>> getFacilities(
//            @RequestParam(value = "searchTerm", required = false) String searchTerm,
//            @RequestParam(value = "district", required = false) String district,
//            @RequestParam(value = "latitude", required = false) Double latitude,
//            @RequestParam(value = "longitude", required = false) Double longitude) {
//
//
//        final Optional<District> hpaDistrict;
//
//        if (StringUtils.isNotEmpty(district)) {
//
//            final List<District> hpaDistricts = hpaDistrictManager.findByName(district);
//            hpaDistrict = hpaDistricts.isEmpty() ? Optional.empty() : Optional.of(hpaDistricts.get(0));
//
//        } else {
//
//            hpaDistrict = Optional.empty();
//        }
//
//        final List<FacilityDTO> facilities =
//                hpaFacilityDao.findFacilities(Optional.ofNullable(searchTerm), hpaDistrict);
//
//        return getFacilities(facilities, latitude, longitude);
//    }
//
//    public ResponseEntity<List<Map<String, Object>>> getFacilities(List<FacilityDTO> facilities, Double latitude,
//                                                                   Double longitude) {
//
//        if (facilities != null) {
//
//            facilities.stream()
//                    .forEach(f -> {
//
//                        f.setContactDetail(hpaFacilityManager.findContactByHpaFacilityId(f.getId())
//                                .stream()
//                                .map(HpaFacilityContact::getName)
//                                .collect(Collectors.joining(",")));
//                        f.setAddressDetail(hpaFacilityManager.findAddressByHpaFacilityId(f.getId())
//                                .stream()
//                                .map(HpaFacilityAddress::getName)
//                                .map(ResponseMapper::addressByRemovingControlChars)
//                                .collect(Collectors.joining(System.lineSeparator())));
//                    });
//
//        }
//
//        final List<FacilityDTO> facilitiesWithDistanceFromUser;
//
//        if (facilities == null || latitude == null || longitude == null) {
//
//            facilitiesWithDistanceFromUser = Collections.emptyList();
//
//        } else {
//
//            final DistanceUtil.Coordinate currentUserLocation = DistanceUtil.Coordinate.of(latitude, longitude);
//
//            facilitiesWithDistanceFromUser = facilities.stream()
//                    .filter(FacilityDTO::getHasCoordinate)
//                    .map(f -> {
//
//                        final Double lat = parseDouble(f.getLatitude());
//                        final Double lng = parseDouble(f.getLongitude());
//
//                        if (lat != null && lng != null) {
//
//                            final DistanceUtil.Coordinate facilityCoordinate = DistanceUtil.Coordinate.of(lat, lng);
//                            final Double facilityDistanceFomUserLocation =
//                                    getDistanceToLocation(facilityCoordinate, currentUserLocation);
//                            f.setDistanceFromLocation(facilityDistanceFomUserLocation);
//                            f.assignDistanceDescription();
//                        }
//
//                        return f;
//                    })
//                    .sorted()
//                    .limit(FACILITY_SEARCH_LIMIT)
//                    .collect(Collectors.toList());
//        }
//
//        // if no facilities can be found by distance e.g no current location, show any random matching ones
//        if (facilitiesWithDistanceFromUser.isEmpty()) {
//
//            Collections.shuffle(facilities);
//
//            final List<FacilityDTO> limitedRandomFacilities = facilities.stream()
//                    .limit(FACILITY_SEARCH_LIMIT)
//                    .collect(Collectors.toList());
//
//            return ResponseMapper.toFacilitiesResponse(limitedRandomFacilities, true);
//
//        } else {
//
//            return ResponseMapper.toFacilitiesResponse(facilitiesWithDistanceFromUser);
//        }
//
//    }
//
//    @GetMapping(value = {"facilityType/list", "v1/facilityType/list"})
//    public ResponseEntity<List<String>> findFacilityTypes(@RequestParam(value = "category", required = false) String
//                                                                  category) {
//
//        final FacilityCategory facilityCategory;
//        final List<String> facilityTypes;
//        if (StringUtils.isNotEmpty(category)) {
//            facilityCategory = facilityCategoryDao.findByName(category);
//            facilityTypes = hpaFacilityDao.findFacilityTypes(facilityCategory);
//        } else {
//            facilityTypes = hpaFacilityDao.findHPAFacilityTypes();
//        }
//
//        return ResponseMapper.toStringResponse(facilityTypes);
//    }
//
//    @GetMapping(value = {"facilityCategory/list", "v1/facilityCategory/list"})
//    public ResponseEntity<List<Map<String, Object>>> getFacilityCategories() {
//
//        List<FacilityCategory> facilityCategories = facilityCategoryDao.findAll();
//        return ResponseMapper.toBaseModelResponse(facilityCategories);
//    }
//
//    @GetMapping(value = {"facilities"})
//    public ResponseEntity<List<Map<String, Object>>> getAllFacilities() {
//
//        final List<FacilityDTO> facilities = hpaFacilityDao.findFacilities(Optional.ofNullable(null), Optional.ofNullable(null),
//                Optional.ofNullable(null), null);
//
//        if (facilities != null) {
//
//            facilities.stream()
//                    .forEach(f -> {
//
//                        f.setContactDetail(hpaFacilityManager.findContactByHpaFacilityId(f.getId())
//                                .stream()
//                                .map(HpaFacilityContact::getName)
//                                .collect(Collectors.joining(",")));
//                        f.setAddressDetail(hpaFacilityManager.findAddressByHpaFacilityId(f.getId())
//                                .stream()
//                                .map(HpaFacilityAddress::getName)
//                                .map(ResponseMapper::addressByRemovingControlChars)
//                                .collect(Collectors.joining(System.lineSeparator())));
//                    });
//
//        }
//
//        return ResponseMapper.toFacilitiesResponse(facilities);
//    }
//}
