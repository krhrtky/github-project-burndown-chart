package api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GithubProjectBurndownChartApiApplication

fun main(args: Array<String>) {
    runApplication<GithubProjectBurndownChartApiApplication>(*args)
}
