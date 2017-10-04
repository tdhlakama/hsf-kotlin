package hsfweb.repository

import hsfweb.model.Facility
import org.springframework.data.jpa.repository.JpaRepository

interface FacilityRepository : JpaRepository<Facility, Long> {

    fun findByName(name: String): Facility

}