package hsfweb

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class HsfWebApplication

fun main(args: Array<String>) {
    SpringApplication.run(HsfWebApplication::class.java, *args)
}