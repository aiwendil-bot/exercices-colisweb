package com.colisweb.exercise.domain

import scala.annotation.tailrec

case class Path(points: List[Point] = Nil) {

  lazy val length: Double = {
    def lengthCalc(points: List[Point]): Double = {
      @tailrec
      def lengthCalcHelp(points: List[Point], sum: Double): Double = points match {
        case Nil => sum
        case p :: Nil => sum
        case p1 :: p2 :: tail => lengthCalcHelp(p2 :: tail, sum + Edge(p1, p2).distance)
      }

      lengthCalcHelp(points, 0)
    }

    lengthCalc(points)
  }
}