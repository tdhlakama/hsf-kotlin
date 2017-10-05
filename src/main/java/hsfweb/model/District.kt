package hsfweb.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * Created by tdhla on 10/4/2017.
 */
@Entity
class District(name: String? = null) : BaseEntity(name) {

    @ManyToOne var province :Province?=null
}