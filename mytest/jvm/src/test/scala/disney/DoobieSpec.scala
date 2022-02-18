package disney

import doobie._
import doobie.implicits._

import cats._
import cats.effect._
import cats.implicits._

import cats.effect.testing.specs2.CatsEffect
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.testcontainers.containers.PostgreSQLContainer

// for some reason, only class works here; object will not be detected by sbt
class DoobieSpec extends Specification with CatsEffect with BeforeAfterAll {

  var postgres: PostgreSQLContainer[_] = null
  var xa: Transactor[IO] = null

  override def afterAll(): Unit = {
    // xa.close()
    postgres.stop()
  }
  override def beforeAll(): Unit = {
    // start container
    postgres =  new PostgreSQLContainer("postgres:13.6-alpine")
    postgres.start()
    // create transactor
    xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",    // driver classname
    postgres.getJdbcUrl(),      // connect URL (driver-specific)
    "test",                     // user
    "test"                      // password
  )
  }

  println("construction")

  "thetest" should {

    println("registration")

    "do the things" in {
      println("in do things, before IO")

      val program2 = sql"select 42".query[Int].unique
      program2.transact(xa).map { res =>
        println(res)
        true must beTrue
      }
    }
  }
}