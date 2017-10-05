package hsfweb.model

import javax.persistence.*

/**
 * Created by tdhla on 10/4/2017.
 */
@MappedSuperclass
open class BaseEntityId() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;

    @Column(unique = true)
    var hpaId: String? = null
}