/*
 * Copyright 2020 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.effect.testing.scalatest

import cats.effect.IO
import cats.effect.testing.RuntimePlatform
import cats.effect.unsafe.IORuntime

import org.scalactic.source.Position
import org.scalatest.AsyncTestSuite
import org.scalatest.enablers.Retrying
import org.scalatest.time.Span

trait AsyncIOSpec extends AssertingSyntax with EffectTestSupport with RuntimePlatform { asyncTestSuite: AsyncTestSuite =>

  implicit lazy val ioRuntime: IORuntime = createIORuntime(executionContext)

  implicit def ioRetrying[T]: Retrying[IO[T]] = new Retrying[IO[T]] {
    override def retry(timeout: Span, interval: Span, pos: Position)(fun: => IO[T]): IO[T] =
      IO.fromFuture(
        IO(Retrying.retryingNatureOfFutureT[T](executionContext).retry(timeout, interval, pos)(fun.unsafeToFuture())),
      )
  }
}
