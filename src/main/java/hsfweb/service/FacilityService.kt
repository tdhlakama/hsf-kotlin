package hsfweb.service

import hsfweb.model.Facility
import hsfweb.repository.*
import org.springframework.stereotype.Service

@Service
class FacilityService(val facilityRepository: FacilityRepository,
                      val contactRepository: ContactRepository,
                      val addressRepository: AddressRepository,
                      val provinceRepository: ProvinceRepository,
                      val districtRepository: DistrictRepository){


    fun saveFacilities(facilities: List<Facility>){

            facilityRepository.save(facilities)
    }

}