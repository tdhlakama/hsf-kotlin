package hsfweb.service

import hsfweb.model.District
import hsfweb.model.dto.FacilityDTO
import hsfweb.repository.FacilityRepository
import org.springframework.stereotype.Service

@Service
class FacilityService(val facilityRepository: FacilityRepository) {

    fun findFacilities(): List<FacilityDTO> {
        return facilityRepository.findFacilityDTOs()
    }

    fun findFacilitiesByFacilityType(facilityType: String): List<FacilityDTO> {
        return facilityRepository.findFacilitiesByFacilityType(facilityType)
    }

    fun findFacilitiesByFacilityTypeAndHpaDistrict(facilityType: String, district: District): List<FacilityDTO> {
        return facilityRepository.findFacilitiesByFacilityTypeAndHpaDistrict(facilityType, district)
    }

    fun findFacilitiesByHpaDistrict(district: District): List<FacilityDTO> {
        return facilityRepository.findFacilitiesByHpaDistrict(district)
    }

    fun findFacilityTypes(): List<String> {
        return facilityRepository.findFacilityTypes()
    }

    fun findFacilities(searchTerm: String?, district: District?): List<FacilityDTO> {

        if (!searchTerm.isNullOrEmpty() && district == null) {
           return findFacilities(searchTerm)
        } else if (!searchTerm.isNullOrEmpty() && district != null) {
            return facilityRepository.findFacilitiesByNameAndDistrict("%" + searchTerm + "%", district)
        } else if (searchTerm.isNullOrEmpty() && district != null) {
            return facilityRepository.findFacilitiesByHpaDistrict(district)
        } else {
            return emptyList();
        }
    }

    fun findFacilities(searchTerm: String?): List<FacilityDTO>
       = facilityRepository.findFacilitiesByName("%" + searchTerm + "%")


}