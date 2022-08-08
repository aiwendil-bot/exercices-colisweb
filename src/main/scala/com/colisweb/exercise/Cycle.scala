package com.colisweb.exercise

import com.colisweb.exercise.domain.{Edge, Path, Point}

import scala.annotation.tailrec


object Cycle {

  // "Simple Constructive, Insertion, and Improvement Heuristics Based on the Girding Polygon for the Euclidean Traveling Salesman Problem"
  // Source : https://www.mdpi.com/1999-4893/13/1/5
  def shortCycle(points: Iterable[Point]): Path = {

    def sqr(d: Double): Double = d * d

    def distance(p1: Point, p2: Point): Double = math.sqrt(sqr(p2.x - p1.x) + sqr(p2.y - p1.y))

    // Phase 1

    val pointsList: List[Point] = points.toList

    def extremePoints():List[Point] = {

      @tailrec
      def extremeCoordonates(V:List[Point], xmin:Double, xmax:Double, ymin:Double, ymax:Double):(Double,Double,Double,Double) = V match {
        case Nil => (xmin,xmax,ymin,ymax)
        case p :: t =>
          val yma = if (p.y > ymax) p.y else ymax
          val ymi = if (p.y < ymin) p.y else ymin
          val xma = if (p.x > xmax) p.x else xmax
          val xmi = if (p.x < xmin) p.x else xmin
          extremeCoordonates(t,xmi,xma,ymi,yma)
      }

      val (xmin,xmax,ymin,ymax) = extremeCoordonates(pointsList,pointsList(1).x,pointsList(1).x,pointsList(1).y,pointsList(1).y)

      val T = pointsList.filter(p => p.y == ymax)
      val L = pointsList.filter(p => p.x == xmin)
      val B = pointsList.filter(p => p.y == ymin)
      val R = pointsList.filter(p => p.x == xmax)

      @tailrec
      def extremePointsHelper1(T: List[Point], v1:Point):Point = T match {
        case Nil => v1
        case p :: t =>
          val v = if (p.x > v1.x) p else v1
          extremePointsHelper1(t,v)
      }
      @tailrec
      def extremePointsHelper2(L: List[Point], v2: Point): Point = L match {
        case Nil => v2
        case p :: t =>
          val v = if (p.y > v2.y) p else v2
          extremePointsHelper2(t, v)
      }

      @tailrec
      def extremePointsHelper3(B: List[Point], v3: Point): Point = B match {
        case Nil => v3
        case p :: t =>
          val v = if (p.x < v3.x) p else v3
          extremePointsHelper3(t, v)
      }

      @tailrec
      def extremePointsHelper4(R: List[Point], v4: Point): Point = R match {
        case Nil => v4
        case p :: t =>
          val v = if (p.y < v4.y) p else v4
          extremePointsHelper4(t, v)
      }

      List(extremePointsHelper1(T,T.head),extremePointsHelper2(L,L.head),extremePointsHelper3(B,B.head),extremePointsHelper4(R,R.head))

    }

    val ExtremePointsList = extremePoints()

    val v1 = ExtremePointsList.head
    val v2 = ExtremePointsList(1)
    val v3 = ExtremePointsList(2)
    val v4 = ExtremePointsList(3)

    def teta(p1: Point, p2: Point): Double = {
      if (math.asin((p2.y - p1.y) / distance(p1, p2)) >= 0)
        math.acos((p2.x - p1.x) / distance(p1, p2))
      else
        -math.acos((p2.x - p1.x) / distance(p1, p2))
    }


    def polygonProcedure(pointsList: List[Point], v1: Point, v2: Point, v3: Point, v4: Point): List[Point] = {
      val P = List(v1)
      val k = v1

      def subsetEdges(k: Point, V: List[Point]): List[Edge] = V match {
        case Nil => Nil
        case p :: t => Edge(k, p) :: subsetEdges(k, t)
      }

      def setAngles(k: Point, E: List[Edge]):Map[(Point,Point),Double] = E match {
        case Nil => Map[(Point,Point),Double]()
        case e :: t => setAngles(k,t) + ((e.from,e.to) -> teta(e.from,e.to))
      }

      @tailrec
      def polygonHelp1(P: List[Point], k: Point): List[Point] = {
        if (k == v2) Nil
        else {
          val V: List[Point] = pointsList.filter(p => p.x < k.x && p.y >= v2.y)
          val E: List[Edge] = subsetEdges(k, V)
          val minVertices: (Point, Point) = setAngles(k, E).minBy(_._2)._1
          polygonHelp1(minVertices._2::P,minVertices._2)
        }
      }

      @tailrec
      def polygonHelp2(P: List[Point], k: Point): List[Point] = {
        if (k == v3) Nil
        else {
          val V: List[Point] = pointsList.filter(p => p.x <= v3.x && p.y < k.y)
          val E: List[Edge] = subsetEdges(k, V)
          val minVertices: (Point, Point) = setAngles(k, E).minBy(_._2)._1
          polygonHelp2(minVertices._2 :: P, minVertices._2)
        }
      }

      @tailrec
      def polygonHelp3(P: List[Point], k: Point): List[Point] = {
        if (k == v4) Nil
        else {
          val V: List[Point] = pointsList.filter(p => p.x > k.x && p.y <= v4.y)
          val E: List[Edge] = subsetEdges(k, V)
          val minVertices: (Point, Point) = setAngles(k, E).minBy(_._2)._1
          polygonHelp3(minVertices._2 :: P, minVertices._2)
        }
      }
      @tailrec
      def polygonHelp4(P: List[Point], k: Point): List[Point] = {
        if (k == v1) Nil
        else {
          val V: List[Point] = pointsList.filter(p => p.x >= v1.x && p.y > k.y)
          val E: List[Edge] = subsetEdges(k, V)
          val minVertices: (Point, Point) = setAngles(k, E).minBy(_._2)._1
          polygonHelp4(minVertices._2 :: P, minVertices._2)
        }
      }

      polygonHelp4(polygonHelp3(polygonHelp2(polygonHelp1(P,k),v2),v3),v4)

    }

    Path(polygonProcedure(pointsList,v1,v2,v3,v4))

    // Phase 2


  }
}