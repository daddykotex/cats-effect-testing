package disney

import cats.effect.IO
import cats.effect.testing.specs2.CatsEffect
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

// for some reason, only class works here; object will not be detected by sbt
class ExampleSpec extends Specification with CatsEffect with BeforeAfterAll {

  override def afterAll(): Unit = {
    println("Afterall")
  }
  override def beforeAll(): Unit = {
    println("beforeall")
  }

  println("construction")

  "thetest" should {

    println("registration")

    "do the things" in {
      println("in do things, before IO")

      IO {
        println("in do things, in IO")
        true must beTrue
      }
    }
  }
}