package hsfweb.model

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

/**
 * Created by tdhla on 10/4/2017.
 */
@Entity
class FacilityCategory(name: String? = null) : BaseEntity(name) {


    @Transient val facilityTypes: Set<String>? = null
}