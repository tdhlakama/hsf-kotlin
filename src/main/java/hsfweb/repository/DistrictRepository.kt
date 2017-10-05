package hsfweb.repository

import hsfweb.model.District
import hsfweb.model.Province
import org.springframework.data.jpa.repository.JpaRepository

interface DistrictRepository : JpaRepository<District, Long> {

    fun findByName(name: String): District

    fun findByHpaId(hpaId: String): District

}