package hsfweb.model

import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * Created by tdhla on 10/4/2017.
 */
@MappedSuperclass
open class BaseEntity(val name: String? =null) : BaseEntityId() {

    @Column(unique = true)
    var hpaId: String? = null
}