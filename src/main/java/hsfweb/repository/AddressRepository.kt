package hsfweb.repository

import hsfweb.model.Address
import hsfweb.model.Contact
import hsfweb.model.Facility
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AddressRepository : JpaRepository<Address, Long> {

    fun findByFacility(facility: Facility): List<Address>

    @Query("select a from Address a where a.facility.id=:id")
    fun findAddresses(@Param("id") id: Long): List<Address>

}