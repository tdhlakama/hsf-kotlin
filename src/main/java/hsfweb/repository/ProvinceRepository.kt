package hsfweb.repository

import hsfweb.model.Facility
import hsfweb.model.Province
import org.springframework.data.jpa.repository.JpaRepository

interface ProvinceRepository : JpaRepository<Province, Long> {

    fun findByName(name: String): Province

    fun findByHpaId(hpaId: String): Province

}