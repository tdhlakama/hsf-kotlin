package hsfweb.controller

import hsfweb.model.Facility
import hsfweb.repository.FacilityRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class FacilityController(val facilityRepository: FacilityRepository) {

    @GetMapping("facilities")
    fun findAll(): List<Facility>
            = facilityRepository.findAll()

    @GetMapping("{name}")
    fun findByName(@PathVariable("name") name :String): Facility
            = facilityRepository.findByName(name)

    @GetMapping("add")
    fun save() {
        facilityRepository.save(Facility("Takunda"))
        facilityRepository.save(Facility("Data"))
    }
}