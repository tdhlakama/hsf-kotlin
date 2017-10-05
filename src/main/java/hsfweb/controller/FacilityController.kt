package hsfweb.controller

import hsfweb.model.District
import hsfweb.model.Facility
import hsfweb.model.Province
import hsfweb.repository.DistrictRepository
import hsfweb.repository.FacilityRepository
import hsfweb.repository.ProvinceRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("/")
class FacilityController(val facilityRepository: FacilityRepository,
                         val provinceRepository: ProvinceRepository,
                         val districtRepository: DistrictRepository) {

    @GetMapping("facilities")
    fun findAll(): List<Facility>
            = facilityRepository.findAll()

    @GetMapping("{name}")
    fun findByName(@PathVariable("name") name: String): ResponseEntity<Facility> {
        val facility = facilityRepository.findByName(name)
        return ResponseEntity.ok(facility)
    }

}