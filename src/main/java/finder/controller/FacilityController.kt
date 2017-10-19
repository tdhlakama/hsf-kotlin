package finder.controller

import finder.controller.endpoints.ResponseMapper
import finder.model.Facility
import finder.model.FacilityCategory
import finder.repository.FacilityCategoryRepository
import finder.service.FacilityService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class FacilityController(val facilityService: FacilityService,
                         val facilityCategoryRepository: FacilityCategoryRepository) {

    val FACILITY_SEARCH_LIMIT = 10

    @GetMapping("/api/facilities")
    fun findAll(): Collection<Facility>
            = facilityService.findFacilities()


    @GetMapping("/api/{name}")
    fun findByName(@PathVariable("name") name: String): ResponseEntity<Facility> {
        val facilities = facilityService.findFacilities(name)
        val facility: Facility? = facilities?.get(0)
        return ResponseEntity.ok(facility)
    }

    @GetMapping(value = *arrayOf("/api/facility/list", "/api/v1/facility/list"))
    fun getFacilities(
            @RequestParam(value = "searchTerm", required = false) searchTerm: String?,
            @RequestParam(value = "facilityType", required = false) facilityType: String?,
            @RequestParam(value = "latitude", required = false) latitude: Double?,
            @RequestParam(value = "longitude", required = false) longitude: Double?)
            : ResponseEntity<List<Map<String, Any>>> {

        var facilities: List<Facility> = emptyList()

        if (!searchTerm.isNullOrEmpty() && facilityType.isNullOrEmpty()) {
            facilities = facilityService.findFacilities(searchTerm)
        }


        if (searchTerm.isNullOrEmpty() && !facilityType.isNullOrEmpty()) {
            facilities = facilityService.findFacilitiesByFacilityType(facilityType)
        }

        return getFacilities(facilities, latitude, longitude)
    }

    fun getFacilities(facilities: List<Facility>, latitude: Double?,
                      longitude: Double?): ResponseEntity<List<Map<String, Any>>> {

        var facilitiesWithDistanceFromUser: List<Facility> = emptyList()

        if (latitude != null && longitude != null) {
            val currentUserLocation = DistanceUtil.Coordinate.of(latitude, longitude)

            facilitiesWithDistanceFromUser = facilities.filter { it.hasCoordinate() }.toList()

            facilitiesWithDistanceFromUser.forEach {

                val lat: Double = ResponseMapper.parseDouble(it.latitude)
                val lng: Double = ResponseMapper.parseDouble(it.longitude)
                if (lat != null && lng != null) {

                    val facilityCoordinate = DistanceUtil.Coordinate.of(lat, lng)
                    val facilityDistanceFomUserLocation = DistanceUtil.getDistanceToLocation(facilityCoordinate, currentUserLocation)
                    it.distanceFromLocation = facilityDistanceFomUserLocation
                }
            }

            facilitiesWithDistanceFromUser.sorted().take(FACILITY_SEARCH_LIMIT)
        }

        // if no facilities can be found by distance e.g no current location, show any random matching ones
        if (facilitiesWithDistanceFromUser.isEmpty()) {

            Collections.shuffle(facilities)

            val limitedRandomFacilities = facilities.take(FACILITY_SEARCH_LIMIT)

            return ResponseMapper.toFacilitiesResponse(limitedRandomFacilities, true)

        } else {

            return ResponseMapper.toFacilitiesResponse(facilitiesWithDistanceFromUser)
        }


    }

    @GetMapping(value = *arrayOf("/api/facilityType/list", "/api/v1/facilityType/list"))
    fun findFacilityTypes(@RequestParam(value = "category", required = false) category: String): ResponseEntity<List<String>> {

        var facilityTypes: List<String>? = emptyList()
        if (!category.isNullOrEmpty()) {
            val facilityCategory: FacilityCategory = facilityCategoryRepository.findByName(category)
            facilityTypes = facilityCategory.facilityTypes?.toList()
        } else {
            facilityTypes = facilityService.findFacilityTypes()
        }

        return ResponseMapper.toStringResponse(facilityTypes)
    }
}