package hsfweb.model

import javax.persistence.Entity

@Entity
class Facility(name: String? = null) : BaseEntity(name), Comparable<Facility> {

    var latitude: String? = null
    var longitude: String? = null
    var facilityType: String? = null
    var contactDetail: String? = null
    var addressDetail: String? = null
    @Transient
    var distanceFromLocation: Double? = null

    fun hasCoordinate(): Boolean {
        return (latitude == null && longitude == null)
    }

    override fun compareTo(other: Facility): Int {
        if (this === other) return 0
        if (other == null) return 1

        return if (distanceFromLocation != null && other.distanceFromLocation != null) {
            distanceFromLocation!!.compareTo(other.distanceFromLocation!!)

        } else if (distanceFromLocation != null && other.distanceFromLocation == null) {
            -1
        } else if (distanceFromLocation == null && other.distanceFromLocation != null) {
            1
        } else {
            name!!.compareTo(other.name!!, ignoreCase = true)
        }
    }
}