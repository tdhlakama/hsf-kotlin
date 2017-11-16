package finder.model

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Product(name: String? = null) : BaseEntity(name), Comparable<Product> {

    var price: BigDecimal? = BigDecimal.ZERO
    @ManyToOne
    var facility: Facility? = null

    override fun compareTo(other: Product): Int {
        if (this === other) return 0
        if (other == null) return 1
        return name!!.compareTo(other.name!!, ignoreCase = true)
    }

}