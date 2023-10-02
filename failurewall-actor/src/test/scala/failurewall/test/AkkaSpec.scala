package failurewall.test

import failurewall.ActorCompat.actor.ActorSystem
import org.scalatest.BeforeAndAfterAll

trait AkkaSpec
  extends WallSpec
  with BeforeAndAfterAll {

  val system = ActorSystem("akka-circuit-breaker-failure-spec")

  override protected def afterAll(): Unit = TestHelper.await(system.terminate())
}
