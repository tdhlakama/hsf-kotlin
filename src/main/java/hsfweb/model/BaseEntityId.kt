package hsfweb.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by tdhla on 10/4/2017.
 */
@Entity
open class BaseEntityId() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;
}