package hsfweb.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Facility(var name: String? =null) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id :Long? =null;

}