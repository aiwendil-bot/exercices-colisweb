package com.colisweb.exercise.domain

import scala.annotation.tailrec

case class Path(points: List[Point] = Nil){

  lazy val length:Double = {
    def lengthCalc(points:List[Point]):Double = points match {
      case Nil => 0
      case p :: Nil => 0
      case p1 :: p2 :: tail => Edge(p1,p2).distance + lengthCalc(p2::tail)
    }
    lengthCalc(points)
  }
}
