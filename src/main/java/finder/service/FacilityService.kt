package finder.service

import finder.model.Facility
import finder.repository.FacilityRepository
import org.springframework.stereotype.Service

@Service
class FacilityService(val facilityRepository: FacilityRepository) {

    fun findFacilities(): List<Facility> {
        return facilityRepository.findFacilities()
    }

    fun findFacilitiesByFacilityType(facilityType: String?): List<Facility> {
        return facilityRepository.findFacilitiesByFacilityType(facilityType.orEmpty())
    }

    fun findFacilityTypes(): List<String> {
        return facilityRepository.findFacilityTypes()
    }

    fun findFacilities(searchTerm: String?): List<Facility>
            = facilityRepository.findFacilitiesByName("%" + searchTerm + "%")

}