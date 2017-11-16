package finder.repository

import finder.model.Facility
import finder.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<Product, Long> {

    @Query("select p from Product p order by p.name")
    fun findProducts(): List<Product>

    fun findByFacility(facility: Facility): List<Product>

    @Query("select p.name from Product p where p.name like :name")
    fun findProductsByName(@Param("name") name: String): List<Product>

    @Query("select p.name from Product p where p.name like :name and p.facility=:facility")
    fun findProductsByNameAndFacility(@Param("name") name: String, @Param("facility") facility: Facility): List<Product>

}