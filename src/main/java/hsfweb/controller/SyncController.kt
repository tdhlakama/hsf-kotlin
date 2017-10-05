package hsfweb.controller

import hsfweb.model.*
import hsfweb.model.dto.DistrictDTO
import hsfweb.model.dto.FacilityData
import hsfweb.repository.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SyncController(val facilityRepository: FacilityRepository,
                     val provinceRepository: ProvinceRepository,
                     val districtRepository: DistrictRepository,
                     val addressRepository: AddressRepository,
                     val contactRepository: ContactRepository) {

    @PostMapping("/sync/provinces")
    fun saveProvinces(@RequestBody provinces: List<Province>): ResponseEntity<Void> {
        provinceRepository.save(provinces)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/sync/districts")
    fun saveDistricts(@RequestBody districts: List<DistrictDTO>): ResponseEntity<Void> {

        districts.forEach {
            val district = District(it.name)
            district.hpaId = it.hpaId
            district.province = provinceRepository.findByHpaId(it.hpaProvince.orEmpty())
            districtRepository.save(district)
        }

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/sync/facilities")
    fun saveFacilities(@RequestBody facilities: List<FacilityData>): ResponseEntity<Facility> {

        facilities.forEach {
            val addresses = it.hpaFacilityAddresses.orEmpty()
            val contacts = it.hpaFacilityContacts.orEmpty()

            val facility = Facility(it.name)

            facility.hpaId = it.hpaId
            facility.facilityType = it.facilityType
            facility.latitude = it.latitude
            facility.longitude = it.longitude

            it.hpaDistrict?.hpaId?.let { districtId ->
                facility.district = districtRepository.findByHpaId(districtId);
            }

            facilityRepository.save(facility)

            addresses.forEach {
                it.facility = facility
            }
            addressRepository.save(addresses)

            contacts.forEach {
                it.facility = facility
            }
            contactRepository.save(contacts)

        }
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}