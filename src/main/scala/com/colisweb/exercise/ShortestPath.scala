package com.colisweb.exercise

import com.colisweb.exercise.domain.{Edge, _}

import annotation.tailrec
import scala.collection.immutable._
object ShortestPath {

  def shortestPath(pathProblem: PathProblem):Option[Path] ={

  val adjList: Map[Point, List[(Point, Double)]] = pathProblem.graph.groupBy(_.from).map {
    case (k, v) => (k, v.map { edge => (edge.to, edge.distance) })
  }

  @tailrec
  def dijkstra(adjList: Map[Point, List[(Point, Double)]], bestKnownPaths: List[(List[Point], Double)], visited: List[Point]): Option[Path] = bestKnownPaths match {
    case Nil => None
    case (path: List[Point], length: Double) :: bestKnownPathsRestant => path match {
      case point :: _ =>
        if (point == pathProblem.end) Some(Path(path.reverse))
        else {
          val newPaths: List[(List[Point], Double)] = {
            adjList.getOrElse(point, Nil).flatMap {
            case (point: Point, l: Double) => if (!visited.contains(point)) List((point :: path, length + l)) else Nil
          }
          }

          val bestKnownPathsSorted: List[(List[Point], Double)] = (newPaths ::: bestKnownPathsRestant).sortWith({ case ((_, d1: Double), (_, d2: Double)) => d1 < d2 })
          dijkstra(adjList, bestKnownPathsSorted, point::visited)

        }
    }
  }
  dijkstra(adjList, List((List(pathProblem.start), 0)), List[Point]())

}
}
