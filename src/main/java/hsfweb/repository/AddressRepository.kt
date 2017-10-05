package hsfweb.repository

import hsfweb.model.Address
import hsfweb.model.Contact
import hsfweb.model.Facility
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Long> {

    fun findByFacility(facility: Facility): List<Address>

    fun findByHpaId(hpaId: String): List<Address>

}