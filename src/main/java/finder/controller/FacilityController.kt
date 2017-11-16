package finder.controller

import finder.controller.endpoints.ResponseMapper
import finder.model.Facility
import finder.model.Product
import finder.repository.FacilityRepository
import finder.repository.ProductRepository
import finder.service.FacilityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@CrossOrigin
@RestController
class FacilityController(val finderService: FacilityService,
                         val facilityRepository: FacilityRepository,
                         val productRepository: ProductRepository) {

    val FACILITY_SEARCH_LIMIT = 10

    @GetMapping("/api/facilities")
    fun findFacilities(): Collection<Facility>
            = facilityRepository.findFacilities()

    @GetMapping("/api/products")
    fun findProducts(): Collection<Product>
            = productRepository.findProducts()

    @GetMapping("/api/facility/get/{id}")
    fun findFacilityByName(@PathVariable("id") id: Long): ResponseEntity<Facility> {
        val facility = facilityRepository.getOne(id)
        return ResponseEntity.ok(facility)
    }

    @GetMapping("/api/product/get/{id}")
    fun findProductByName(@PathVariable("id") id: Long): ResponseEntity<Product> {
        val product = productRepository.getOne(id)
        return ResponseEntity.ok(product)
    }

    @GetMapping("/api/product/facility/{id}")
    fun findByFacility(@PathVariable("id") id: Long): ResponseEntity<List<Product>> {
        val facility = facilityRepository.getOne(id)
        return ResponseEntity.ok(productRepository.findByFacility(facility))
    }

    @PostMapping("/api/facility/add")
    fun saveFacility(@RequestBody facility: Facility): ResponseEntity<Facility> {
        return ResponseEntity.ok(facilityRepository.save(facility))
    }

    @PostMapping("/api/product/add")
    fun saveProduct(@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity.ok(productRepository.save(product))
    }

    @PutMapping("/api/facility/update")
    fun updateFacility(@RequestBody facility: Facility): ResponseEntity<Facility> {
        return ResponseEntity<Facility>(facilityRepository.save(facility), HttpStatus.OK)
    }

    @PutMapping("/api/product/update")
    fun updateFacility(@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity<Product>(productRepository.save(product), HttpStatus.OK)
    }

    @PatchMapping("/api/facility/patch")
    fun patchFacility(@RequestBody facility: Facility): ResponseEntity<Facility> {
        return ResponseEntity<Facility>(facilityRepository.save(facility), HttpStatus.OK)
    }

    @PatchMapping("/api/product/patch")
    fun patchFacility(@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity<Product>(productRepository.save(product), HttpStatus.OK)
    }

    @DeleteMapping("/api/facility/delete/{id}")
    fun deleteFacility(@PathVariable id: Long): ResponseEntity<Boolean> {
        facilityRepository.delete(id)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @DeleteMapping("/api/product/delete/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Boolean> {
        productRepository.delete(id)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @GetMapping(value = *arrayOf("/api/facility/list"))
    fun getFacilities(
            @RequestParam(value = "searchTerm", required = false) searchTerm: String?,
            @RequestParam(value = "latitude", required = false) latitude: Double?,
            @RequestParam(value = "longitude", required = false) longitude: Double?)
            : ResponseEntity<List<Map<String, Any>>> {

        var facilities: List<Facility> = emptyList()

        if (!searchTerm.isNullOrEmpty()) {
            facilities = finderService.findFacilities(searchTerm)
        }

        return getFacilities(facilities, latitude, longitude)
    }

    fun getFacilities(facilities: List<Facility>, latitude: Double?,
                      longitude: Double?): ResponseEntity<List<Map<String, Any>>> {

        var facilitiesWithDistanceFromUser: List<Facility> = emptyList()

        if (latitude != null && longitude != null) {
            val currentUserLocation = DistanceUtil.Coordinate.of(latitude, longitude)

            facilitiesWithDistanceFromUser = facilities.filter { it.hasCoordinate() }.toList()

            facilitiesWithDistanceFromUser.forEach {

                val lat: Double = ResponseMapper.parseDouble(it.latitude)
                val lng: Double = ResponseMapper.parseDouble(it.longitude)
                if (lat != null && lng != null) {

                    val facilityCoordinate = DistanceUtil.Coordinate.of(lat, lng)
                    val facilityDistanceFomUserLocation = DistanceUtil.getDistanceToLocation(facilityCoordinate, currentUserLocation)
                    it.distanceFromLocation = facilityDistanceFomUserLocation
                }
            }

            facilitiesWithDistanceFromUser.sorted().take(FACILITY_SEARCH_LIMIT)
        }

        // if no facilities can be found by distance e.g no current location, show any random matching ones
        if (facilitiesWithDistanceFromUser.isEmpty()) {

            Collections.shuffle(facilities)

            val limitedRandomFacilities = facilities.take(FACILITY_SEARCH_LIMIT)

            return ResponseMapper.toFacilitiesResponse(limitedRandomFacilities, true)

        } else {

            return ResponseMapper.toFacilitiesResponse(facilitiesWithDistanceFromUser)
        }


    }

}