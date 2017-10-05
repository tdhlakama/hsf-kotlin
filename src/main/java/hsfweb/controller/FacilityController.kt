package hsfweb.controller

import hsfweb.controller.endpoints.ResponseMapper
import hsfweb.controller.endpoints.ResponseMapper.parseDouble
import hsfweb.model.District
import hsfweb.model.dto.DistanceUtil
import hsfweb.model.dto.DistanceUtil.getDistanceToLocation
import hsfweb.model.dto.FacilityDTO
import hsfweb.repository.AddressRepository
import hsfweb.repository.ContactRepository
import hsfweb.repository.DistrictRepository
import hsfweb.repository.FacilityCategoryRepository
import hsfweb.service.FacilityService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.streams.toList

@RestController("/")
class FacilityController(val facilityService: FacilityService,
                         val facilityCategoryRepository: FacilityCategoryRepository,
                         val districtRepository: DistrictRepository,
                         val contactRepository: ContactRepository,
                         val addressRepository: AddressRepository) {

    val FACILITY_SEARCH_LIMIT = 10L

    @GetMapping("facilities")
    fun findAll(): Collection<FacilityDTO>
            = facilityService.findFacilities()


    @GetMapping("{name}")
    fun findByName(@PathVariable("name") name: String): ResponseEntity<FacilityDTO> {
        val district: District? = null
        val facilities = facilityService.findFacilities(name, district)
        val facility: FacilityDTO? = facilities?.get(0)
        return ResponseEntity.ok(facility)
    }

    @GetMapping(value = *arrayOf("facility/list", "v1/facility/list"))
    fun getFacilities(
            @RequestParam(value = "searchTerm", required = false) searchTerm: String,
            @RequestParam(value = "facilityType", required = false) facilityType: String,
            @RequestParam(value = "latitude", required = false) latitude: Double,
            @RequestParam(value = "longitude", required = false) longitude: Double,
            @RequestParam(value = "district", required = false) districtName: String): ResponseEntity<List<Map<String, Any>>> {

        var facilities: List<FacilityDTO> = emptyList()

        if (!searchTerm.isNullOrEmpty() && districtName.isNullOrEmpty() && facilityType.isNullOrEmpty()) {
            facilities = facilityService.findFacilities(searchTerm)
        }

        if (districtName.isNullOrEmpty() && searchTerm.isNullOrEmpty() && !facilityType.isNullOrEmpty())
            facilities = facilityService.findFacilitiesByFacilityType(facilityType)

        if (!searchTerm.isNullOrEmpty() && !districtName.isNullOrEmpty()) {
            val districts: List<District>? = districtRepository.findByName(districtName)
            facilities = facilityService.findFacilities(searchTerm, districts?.get(0))
        }

        return getFacilities(facilities, latitude, longitude)
    }

    fun getFacilities(facilities: List<FacilityDTO>, latitude: Double,
                      longitude: Double): ResponseEntity<List<Map<String, Any>>> {

        facilities.forEach {

            it.setContactDetail(contactRepository.findContacts(it.id).map { contact -> contact.contactDetail }.toString())

            it.setAddressDetail(addressRepository.findAddresses(it.id).map { address -> address.toString() }.toString())

        }

        val currentUserLocation = DistanceUtil.Coordinate.of(latitude, longitude)

        val facilitiesWithDistanceFromUser: List<FacilityDTO> = facilities

        facilitiesWithDistanceFromUser.filter { it.hasCoordinate }.toList().forEach {

            val lat = parseDouble(it.latitude)
            val lng = parseDouble(it.longitude)

            if (lat != null && lng != null) {

                val facilityCoordinate = DistanceUtil.Coordinate.of(lat, lng)
                val facilityDistanceFomUserLocation = getDistanceToLocation(facilityCoordinate, currentUserLocation)
                it.distanceFromLocation = facilityDistanceFomUserLocation
                it.assignDistanceDescription()
            }
        }

        facilitiesWithDistanceFromUser.stream().limit(FACILITY_SEARCH_LIMIT)


        // if no facilities can be found by distance e.g no current location, show any random matching ones
        if (facilitiesWithDistanceFromUser.isEmpty()) {

            Collections.shuffle(facilities)

            val limitedRandomFacilities = facilities.stream()
                    .limit(FACILITY_SEARCH_LIMIT).toList()

            return ResponseMapper.toFacilitiesResponse(limitedRandomFacilities, true)

        } else {

            return ResponseMapper.toFacilitiesResponse(facilitiesWithDistanceFromUser)
        }


    }
}