package hsfweb.model.dto

import hsfweb.model.Address
import hsfweb.model.BaseEntity
import hsfweb.model.Contact
import java.util.ArrayList
import javax.persistence.ManyToOne

class FacilityData(val name: String? = null) {

    var hpaDistrict: DistrictDTO? = null;
    var latitude: String? = null
    var longitude: String? = null
    var facilityType: String? = null
    var hpaFacilityContacts: List<Contact>? = null
    var hpaFacilityAddresses: List<Address>? = null
    var hpaId: String? = null

}