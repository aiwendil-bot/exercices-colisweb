package com.colisweb.exercise

import com.colisweb.exercise.domain.{Path, Point}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

final class PathSpec extends AnyFlatSpec with Matchers {
  "empty path" should "have length 0" in {
    Path().length shouldBe 0.0
  }

  "1 point path" should "have length 0" in {
    Path(List(Point(10, 15))).length shouldBe 0.0
  }

  "2 point path" should "have length 10" in {
    Path(List(Point(10, 15), Point(20, 15))).length shouldBe 10.0 +- 0.0001
  }

  "general path" should "have length 115" in {
    Path(
      List(
        Point(10, 15),
        Point(20, 15),
        Point(20, 35),
        Point(40, 35),
        Point(40, 100)
      )
    ).length shouldBe 115.0 +- 0.0001
  }
}
