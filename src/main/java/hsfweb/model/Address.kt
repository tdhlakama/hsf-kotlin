package hsfweb.model

import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * Created by tdhla on 10/4/2017.
 */
@Entity
class Address : BaseEntityId() {

    var addressType: String? = null
    var address1: String? = null
    var address2: String? = null
    var hpaId: String? = null
    @ManyToOne var facility: Facility? = null

}