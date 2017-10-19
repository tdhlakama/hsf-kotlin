package finder.repository

import finder.model.Facility
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface FacilityRepository : JpaRepository<Facility, Long> {

    @Query("select f from Facility f")
    fun findFacilities(): List<Facility>

    @Query("select f from Facility f where f.facilityType=:facilityType")
    fun findFacilitiesByFacilityType(@Param("facilityType") facilityType: String): List<Facility>

    @Query("select f from Facility f where f.name like :name")
    fun findFacilitiesByName(@Param("name") name: String): List<Facility>

    @Query("select distinct (f.facilityType) from Facility f")
    fun findFacilityTypes(): List<String>

}