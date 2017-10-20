package finder.service

import finder.model.Facility
import finder.repository.FacilityRepository
import org.springframework.stereotype.Service

@Service
class FacilityService(val facilityRepository: FacilityRepository) {

    fun findFacilitiesByFacilityType(facilityType: String?): List<Facility> {
        return facilityRepository.findFacilitiesByFacilityType(facilityType.orEmpty())
    }

    fun findFacilities(searchTerm: String?): List<Facility>
            = facilityRepository.findFacilitiesByName("%" + searchTerm + "%")

}