package hsfweb.repository

import hsfweb.model.District
import hsfweb.model.Facility
import hsfweb.model.dto.FacilityDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface FacilityRepository : JpaRepository<Facility, Long> {

    fun findByName(name: String): Facility

    fun findByHpaId(hpaId: String): Facility

    @Query("select new hsfweb.model.dto.FacilityDTO(f.id, f.name, f.facilityType, f.latitude, f.longitude) from Facility f")
    fun findFacilityDTOs(): List<FacilityDTO>

    @Query("select new hsfweb.model.dto.FacilityDTO(f.id, f.name, f.facilityType, f.latitude, f.longitude) from Facility f where f.facilityType=:facilityType")
    fun findFacilitiesByFacilityType(@Param("facilityType") facilityType: String): List<FacilityDTO>

    @Query("select new hsfweb.model.dto.FacilityDTO(f.id, f.name, f.facilityType, f.latitude, f.longitude) from Facility f where f.facilityType=:facilityType and f.district=:district")
    fun findFacilitiesByFacilityTypeAndHpaDistrict(@Param("facilityType") facilityType: String, @Param("district") district: District): List<FacilityDTO>

    @Query("select new hsfweb.model.dto.FacilityDTO(f.id,f.name, f.facilityType, f.latitude, f.longitude) from Facility f where f.district=:district")
    fun findFacilitiesByHpaDistrict(@Param("district") district: District): List<FacilityDTO>

    @Query("select new hsfweb.model.dto.FacilityDTO(f.id,f.name, f.facilityType, f.latitude, f.longitude) from Facility f where f.name like :name")
    fun findFacilitiesByName(@Param("name") name: String): List<FacilityDTO>

    @Query("select new hsfweb.model.dto.FacilityDTO(f.id,f.name, f.facilityType, f.latitude, f.longitude) from Facility f where f.name like :name and f.district=:district")
    fun findFacilitiesByNameAndDistrict(@Param("name") name: String, @Param("district") district: District): List<FacilityDTO>

    @Query("select distinct (f.facilityType) from Facility f")
    fun findFacilityTypes(): List<String>

}