package hsfweb.repository

import hsfweb.model.FacilityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface FacilityCategoryRepository : JpaRepository<FacilityCategory, Long> {

    fun findByName(name: String): FacilityCategory

}