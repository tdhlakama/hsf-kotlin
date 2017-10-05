package hsfweb.model

import javax.persistence.MappedSuperclass

/**
 * Created by tdhla on 10/4/2017.
 */
@MappedSuperclass
open class BaseEntity(val name: String? =null) : BaseEntityId() {

}