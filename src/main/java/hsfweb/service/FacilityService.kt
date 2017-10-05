//package hsfweb.service
//
//import hsfweb.model.District
//import hsfweb.model.FacilityCategory
//import hsfweb.model.dto.FacilityDTO
//import hsfweb.repository.*
//import org.springframework.stereotype.Service
//import java.util.*
//
//@Service
//class FacilityService(val facilityRepository: FacilityRepository,
//                      val contactRepository: ContactRepository,
//                      val addressRepository: AddressRepository,
//                      val provinceRepository: ProvinceRepository,
//                      val districtRepository: DistrictRepository){
//
//
//    lateinit  entityManager: EntityManager
//
//    private fun findFacilitiesByFacilityType(facilityType: String): List<FacilityDTO> {
//        return entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.facilityType=:facilityType")
//                .setParameter("facilityType", facilityType)
//                .getResultList()
//    }
//
//    private fun findFacilitiesByFacilityTypeAndHpaDistrict(facilityType: String, hpaDistrict: HpaDistrict): List<FacilityDTO> {
//        return entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.facilityType=:facilityType and h.hpaDistrict=:hpaDistrict")
//                .setParameter("facilityType", facilityType)
//                .setParameter("hpaDistrict", hpaDistrict)
//                .getResultList()
//    }
//
//    private fun findFacilitiesByHpaDistrict(hpaDistrict: District): List<FacilityDTO> {
//        return entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.hpaDistrict=:hpaDistrict")
//                .setParameter("hpaDistrict", hpaDistrict)
//                .getResultList()
//    }
//
//    private fun findFacilitiesByFacilityCategory(facilityCategory: FacilityCategory): List<FacilityDTO> {
//        return entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.facilityCategory=:facilityCategory")
//                .setParameter("facilityCategory", facilityCategory)
//                .getResultList()
//    }
//
//    private fun findFacilitiesByFacilityCategoryAndHpaDistrict(facilityCategory: FacilityCategory, hpaDistrict: HpaDistrict): List<FacilityDTO> {
//        return entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.facilityCategory=:facilityCategory and h.hpaDistrict=:hpaDistrict")
//                .setParameter("facilityCategory", facilityCategory)
//                .setParameter("hpaDistrict", hpaDistrict)
//                .getResultList()
//    }
//
//
//    fun findFacilities(): List<FacilityDTO> {
//        return entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h")
//                .getResultList()
//    }
//
//    fun findFacilityTypes(): List<String> {
//        return entityManager.createQuery("select distinct (h.facilityType) from HpaFacility h")
//                .getResultList()
//    }
//
//    fun findFacilities(facilityType: Optional<String>, facilityCategory: Optional<FacilityCategory>, hpaDistrict: Optional<District>): List<FacilityDTO> {
//
//        if (!facilityCategory.isPresent() && !facilityType.isPresent && !hpaDistrict.isPresent()) {
//            //return findFacilities()
//        }
//
//        if (facilityCategory.isPresent() && !facilityType.isPresent) {
//            return findFacilitiesByFacilityCategory(facilityCategory.get())
//        }
//
//        if (!facilityCategory.isPresent() && facilityType.isPresent) {
//            return findFacilitiesByFacilityType(facilityType.get())
//        }
//
//        if (!facilityCategory.isPresent() && !facilityType.isPresent && hpaDistrict.isPresent()) {
//            return findFacilitiesByHpaDistrict(hpaDistrict.get())
//        }
//
//        if (facilityCategory.isPresent() && !facilityType.isPresent && hpaDistrict.isPresent()) {
//            return findFacilitiesByFacilityCategoryAndHpaDistrict(facilityCategory.get(), hpaDistrict.get())
//        }
//
//        return if (!facilityCategory.isPresent() && facilityType.isPresent && hpaDistrict.isPresent()) {
//            findFacilitiesByFacilityTypeAndHpaDistrict(facilityType.get(), hpaDistrict.get())
//        } else emptyList<T>()
//
//    }
//
//    fun findFacilities(searchTerm: Optional<String>, hpaDistrict: Optional<District>): List<FacilityDTO> {
//
//        return if (searchTerm.isPresent && !hpaDistrict.isPresent()) {
//            entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.name like :name")
//                    .setParameter("name", "%" + searchTerm.get() + "%")
//                    .getResultList()
//        } else if (searchTerm.isPresent && hpaDistrict.isPresent()) {
//            entityManager.createQuery("select new zw.com.hitrac.web.util.FacilityDTO(h.id,h.name,h.sector, h.facilityType,h.latitude, h.longitude) from HpaFacility h where h.name like :name and h.hpaDistrict=:hpaDistrict")
//                    .setParameter("name", "%" + searchTerm.get() + "%")
//                    .setParameter("hpaDistrict", hpaDistrict.get())
//                    .getResultList()
//        } else if (!searchTerm.isPresent && hpaDistrict.isPresent())
//            findFacilitiesByHpaDistrict(hpaDistrict.get())
//        else {
//            emptyList<T>()
//        }
//
//    }
//
////    fun findFacilityTypes(facilityCategory: FacilityCategory): List<String> {
////        return hpaFacilityRepository.findFacilityTypes(facilityCategory, HpaFacilitySource.HPA)
////    }
//
//}
//
//
//}