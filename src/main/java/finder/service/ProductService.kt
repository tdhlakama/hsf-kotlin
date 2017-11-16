package finder.service

import finder.model.Facility
import finder.model.Product
import finder.repository.FacilityRepository
import finder.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(val productRepository: ProductRepository) {

    fun findProductsByName(searchTerm: String?): List<Product> {
        return productRepository.findProductsByName("%" + searchTerm + "%")
    }

    fun findProductsByNameAndFacility(searchTerm: String?, facility: Facility): List<Product> {
        return productRepository.findProductsByNameAndFacility("%" + searchTerm + "%", facility)
    }

}