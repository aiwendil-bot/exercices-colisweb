package com.colisweb.exercise

import com.colisweb.exercise.domain.{Edge, Path, Point}

import scala.annotation.tailrec
import scala.util.control._

object Cycle {

  def shortCycle(points: Iterable[Point]): Path = {

    // NN construction heuristic

    val pointsList: List[Point] = points.toList

    def NearestNeighbor(points: List[Point], currentPoint: Point, cycle: List[Edge]): List[Edge] = {
      if (points.isEmpty) Edge(cycle.last.to, currentPoint) :: cycle
      else {
        val nearest: Point = points.minBy(p => Edge(p, currentPoint).distance2)
        NearestNeighbor(points diff List(nearest), nearest, Edge(nearest, currentPoint) :: cycle)
      }
    }

    // 2-opt

    @tailrec
    def getNextElem[T](liste: List[T], elem: T): Option[T] = liste match {
      case Nil => None
      case `elem` :: t :: _ => Some(t)
      case _ :: tail => getNextElem(tail, elem)
    }

    @tailrec
    def getPreviousElem[T](liste: List[T], elem: T): Option[T] = liste match {
      case Nil => None
      case t :: `elem` :: _ => Some(t)
      case _ :: tail => getPreviousElem(tail, elem)
    }

    def reverseEdges(edges: List[Edge]): List[Edge] = {
      edges.map(e => Edge(e.to, e.from)).reverse
    }


    // edgeToSwap1 avant edgeToSwap2

    def twoOptSwap(cycle: List[Edge], edgeToSwap1: Edge, edgeToSwap2: Edge): List[Edge] = {
      val index_edges: List[Int] = cycle.zipWithIndex.collect { case (`edgeToSwap1`, i) => i case (`edgeToSwap2`, i) => i }

      cycle.takeWhile(edge => edge != edgeToSwap1) ::: List(Edge(edgeToSwap1.from, edgeToSwap2.from)) :::
        reverseEdges(cycle.slice(index_edges.head + 1, index_edges.last)) ::: List(Edge(edgeToSwap1.to, edgeToSwap2.to)) :::
        cycle.takeRight(cycle.length - index_edges.last - 1)

    }

    def twoOpt(cycle: List[Edge]): List[Edge] = {


      def twoOptHelper(cycle: List[Edge], amelioration: Boolean, res:List[List[Edge]]): List[Edge] = {
        if (amelioration) {

          for (edge1 <- cycle.dropRight(2)) {
            for (edge2 <- cycle.drop(cycle.indexOf(edge1) + 2)) {
              if (edge1.distance2 + edge2.distance2 > Edge(edge1.from, edge2.from).distance2 + Edge(edge1.to, edge2.to).distance2) {
                val newCycle = twoOptSwap(cycle, edge1, edge2)
                //println(Path(edgesToPoints(newCycle)).length)
                twoOptHelper(newCycle, true, newCycle :: res)
              }
            }
          }
          twoOptHelper(cycle, false, res)
        }
        else
          res.head

      }
  twoOptHelper(cycle, true, List(cycle))

}

def edgesToPoints (edges: List[Edge] ): List[Point] = edges match {
  case Nil => Nil
  case edge :: Nil => edge.from :: edge.to :: Nil
  case edge :: tail => edge.from :: edgesToPoints (tail)
  }
  // NearestNeighbor(pointsList.tail,pointsList.head,Nil)

  Path (edgesToPoints (twoOpt(List (Edge (Point (0, 0), Point (0, 1) ), Edge (Point (0, 1), Point (1, 0) ), Edge (Point (1, 0), Point (1, 1) ), Edge (Point (1, 1), Point (0, 0) ) ) ) ) )
  }
  }
