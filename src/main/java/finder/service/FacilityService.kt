package finder.service

import finder.model.Facility
import finder.model.Product
import finder.repository.FacilityRepository
import finder.repository.ProductRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service

@Service
class FacilityService(val facilityRepository: FacilityRepository) {

    fun findFacilities(searchTerm: String?): List<Facility>
            = facilityRepository.findFacilitiesByName("%" + searchTerm + "%")

}