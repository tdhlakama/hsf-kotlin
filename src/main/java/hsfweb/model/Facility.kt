package hsfweb.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Facility(name :String? =null) : BaseEntity(name) {

    var hpaId: String? = null
    var district :District?=null;
    var latitude: String? = null
    var longitude: String? = null
    var facilityType: String? = null

}