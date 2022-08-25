package com.colisweb.exercise

import com.colisweb.exercise.domain.{Edge, Path, Point}

import scala.annotation.tailrec

object Cycle {

  def shortCycle(points: Iterable[Point]): Path = {

    // nearest neighbor construction heuristic

    val pointsList: List[Point] = points.toList

    @tailrec
    def nearestNeighbor(points: List[Point], currentPoint: Point, cycle: List[Edge]): List[Edge] = {
      if (points.isEmpty) Edge(cycle.last.to, currentPoint) :: cycle
      else {
        val nearest: Point = points.minBy(p => Edge(p, currentPoint).distance2)
        nearestNeighbor(points diff List(nearest), nearest, Edge(nearest, currentPoint) :: cycle)
      }
    }

    // 2-opt

    def reverseEdges(edges: List[Edge]): List[Edge] = {
      edges.map(e => Edge(e.to, e.from)).reverse
    }

    // edgeToSwap1 "avant" edgeToSwap2 dans cycle

    def twoOptSwap(cycle: List[Edge], edgeToSwap1: Edge, edgeToSwap2: Edge): List[Edge] = {
      val index_edges: List[Int] = cycle.zipWithIndex.collect({
        case (`edgeToSwap1`, i) => i
        case (`edgeToSwap2`, i) => i
      })

      cycle.takeWhile(edge => edge != edgeToSwap1) ::: List(Edge(edgeToSwap1.from, edgeToSwap2.from)) :::
        reverseEdges(cycle.slice(index_edges.head + 1, index_edges.last)) ::: List(
        Edge(edgeToSwap1.to, edgeToSwap2.to)
      ) :::
        cycle.takeRight(cycle.length - index_edges.last - 1)

    }

    @tailrec
    def twoOpt(cycle: List[Edge]): List[Edge] = {

      @tailrec
      def twoOptHelper(cycle: List[Edge], cycle2: List[Edge], cycleCopy: List[Edge]): List[Edge] = {

        cycle match {
          case Nil           => cycleCopy
          case edge1 :: tail =>
            cycle2 match {
              case Nil            => twoOptHelper(tail, cycleCopy.drop(cycle.indexOf(edge1) + 2), cycleCopy)
              case edge2 :: tail2 =>
                if (
                  edge1.distance2 + edge2.distance2 > Edge(edge1.from, edge2.from).distance2 + Edge(
                    edge1.to,
                    edge2.to
                  ).distance2
                )
                  twoOptSwap(cycleCopy, edge1, edge2)
                else
                  twoOptHelper(cycle, tail2, cycleCopy)

            }

        }
      }

      val updatedCycle = twoOptHelper(cycle.dropRight(2), cycle.drop(2), cycle)
      if (Path(edgesToPoints(updatedCycle)).length == Path(edgesToPoints(cycle)).length)
        updatedCycle
      else {
        twoOpt(updatedCycle)
      }

    }

    def edgesToPoints(edges: List[Edge]): List[Point] = {
      @tailrec
      def edgesToPointsHelper(edges: List[Edge], res: List[Point]): List[Point] = {
        edges match {
          case Nil          => res
          case edge :: Nil  => edgesToPointsHelper(Nil, edge.to :: edge.from :: res)
          case edge :: tail => edgesToPointsHelper(tail, edge.from :: res)
        }
      }
      edgesToPointsHelper(edges, Nil)
    }
    //petite instance de tests
    //Edge(Point(0,0),Point(0.5,2)),Edge(Point(0.5,2),Point(1,0)),Edge(Point(1,0),Point(1,1)),Edge(Point(1,1),Point(0,1)),Edge(Point(0,1),Point(0,0))

    Path(
      edgesToPoints(
        twoOpt(
          nearestNeighbor(pointsList.tail, pointsList.head, Nil)
        )
      )
    )

  }
}
