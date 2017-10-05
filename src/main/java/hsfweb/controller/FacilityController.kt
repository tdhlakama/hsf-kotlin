package hsfweb.controller

import hsfweb.model.District
import hsfweb.model.Facility
import hsfweb.model.Province
import hsfweb.repository.FacilityRepository
import org.springframework.web.bind.annotation.*

@RestController("/")
class FacilityController(val facilityRepository: FacilityRepository) {

    @GetMapping("facilities")
    fun findAll(): List<Facility>
            = facilityRepository.findAll()

    @GetMapping("{name}")
    fun findByName(@PathVariable("name") name: String): Facility
            = facilityRepository.findByName(name)

    @GetMapping("add")
    fun save() {
        facilityRepository.save(Facility("Takunda"))
        facilityRepository.save(Facility("Data"))
    }

    @PostMapping("province-push")
    fun saveProvinces(@RequestBody facilities: List<Province>): String {

        return ""
    }

    @PostMapping("district-push")
    fun saveDistricts(@RequestBody facilities: List<District>): String {

        return ""
    }


    @PostMapping("facilities-push")
    fun saveFacilities(@RequestBody facilities: List<Facility>): String {
        updateFacilities(facilities)
        return ""
    }

    fun updateFacilities(facilities: List<Facility>) {


    }
}