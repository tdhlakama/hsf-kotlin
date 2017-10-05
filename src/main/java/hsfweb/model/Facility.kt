package hsfweb.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Facility(name: String? = null) : BaseEntity(name) {

    @ManyToOne
    var district: District? = null;
    var latitude: String? = null
    var longitude: String? = null
    var facilityType: String? = null
}