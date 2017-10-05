package hsfweb.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * Created by tdhla on 10/4/2017.
 */
@Entity
class Contact : BaseEntityId() {

    var type: String? = null
    var contactDetail: String? = null
    @ManyToOne var facility: Facility? = null

}