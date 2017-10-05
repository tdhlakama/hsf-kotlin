package hsfweb.repository

import hsfweb.model.FacilityCategory
import hsfweb.model.Province
import org.springframework.data.jpa.repository.JpaRepository

interface FacilityCategoryRepository : JpaRepository<FacilityCategory, Long> {

    fun findByName(name: String): FacilityCategory

}