package hsfweb.repository

import hsfweb.model.Contact
import hsfweb.model.District
import hsfweb.model.Facility
import org.springframework.data.jpa.repository.JpaRepository

interface FacilityRepository : JpaRepository<Facility, Long> {

    fun findByName(name: String): Facility

    fun findByHpaId(hpaId: String): Facility

    fun findByDistrict(district: District): List<Facility>

}