package failurewall

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

private[failurewall] object ActorCompat {

  object actor {
    val ActorSystem = org.apache.pekko.actor.ActorSystem
    type Scheduler = org.apache.pekko.actor.Scheduler
  }

  object pattern {
    val CircuitBreaker = org.apache.pekko.pattern.CircuitBreaker
    type CircuitBreaker = org.apache.pekko.pattern.CircuitBreaker

    type CircuitBreakerOpenException = org.apache.pekko.pattern.CircuitBreakerOpenException

    def after[T](duration: FiniteDuration, using: org.apache.pekko.actor.Scheduler)(value: => Future[T])(implicit ec: ExecutionContext): Future[T] =
      org.apache.pekko.pattern.after(duration = duration, using = using)(value = value)(ec = ec)

  }

}
