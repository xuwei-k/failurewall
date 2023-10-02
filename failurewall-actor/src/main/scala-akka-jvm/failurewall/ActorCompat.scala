package failurewall

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

private[failurewall] object ActorCompat {

  object actor {
    val ActorSystem = akka.actor.ActorSystem
    type Scheduler = akka.actor.Scheduler
  }

  object pattern {
    val CircuitBreaker = akka.pattern.CircuitBreaker
    type CircuitBreaker = akka.pattern.CircuitBreaker

    type CircuitBreakerOpenException = akka.pattern.CircuitBreakerOpenException

    def after[T](duration: FiniteDuration, using: akka.actor.Scheduler)(value: => Future[T])(implicit ec: ExecutionContext): Future[T] =
      akka.pattern.after(duration = duration, using = using)(value = value)(ec = ec)
  }

}
