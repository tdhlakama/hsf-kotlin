package hsfweb.repository

import hsfweb.model.Address
import hsfweb.model.Contact
import hsfweb.model.Facility
import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository : JpaRepository<Contact, Long> {

    fun findByFacility(facility: Facility): List<Contact>

    fun findByHpaId(hpaId: String): List<Contact>

}