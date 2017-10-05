package hsfweb.repository

import hsfweb.model.Address
import hsfweb.model.Contact
import hsfweb.model.Facility
import hsfweb.model.dto.FacilityDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContactRepository : JpaRepository<Contact, Long> {

    fun findByFacility(facility: Facility): List<Contact>

    @Query("select c from Contact c where c.facility.id=:id")
    fun findContacts(@Param("id") id: Long): List<Contact>


}