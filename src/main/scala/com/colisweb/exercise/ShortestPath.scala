package com.colisweb.exercise

import com.colisweb.exercise.domain.{Edge, _}

import annotation.tailrec
import scala.collection.immutable._

object ShortestPath {

  def shortestPath(pathProblem: PathProblem): Option[Path] = {

    val adjList: Map[Point, List[Point]] = pathProblem.graph.groupBy(_.from).map {
      case (k, v) => (k, v.map { edge => edge.to })
    }

    @tailrec
    def dijkstra(adjList: Map[Point, List[Point]], extremePoints: List[Point], bestKnownPaths: Map[Point, Path], visited: List[Point]): Option[Path] = extremePoints match {
      case Nil => None
      case point :: _ =>
        if (point == pathProblem.end) Some(Path(bestKnownPaths(point).points.reverse))
        else {
          def updateBestKnownPaths(neighbors: List[Point], bestKnownPaths: Map[Point, Path]): Map[Point, Path] = neighbors match {
            case Nil => bestKnownPaths
            case neighbor :: tail =>
              if (!visited.contains(neighbor)) {
                updateBestKnownPaths(tail, bestKnownPaths + (neighbor -> Path(neighbor :: bestKnownPaths(point).points)))
              } else
                updateBestKnownPaths(tail, bestKnownPaths)
          }

          val updatedBestKnownPaths = updateBestKnownPaths(adjList.getOrElse(point, Nil), bestKnownPaths)
          val sortedExtremePoints = ((extremePoints ::: adjList.getOrElse(point, Nil)) diff (List(point) ::: visited)).sortBy(point => updatedBestKnownPaths(point).length)

          dijkstra(adjList, sortedExtremePoints, updatedBestKnownPaths, point :: visited)

        }
    }

    dijkstra(adjList, List(pathProblem.start), Map[Point, Path](pathProblem.start -> Path(List(pathProblem.start))), List[Point]())
  }

}
