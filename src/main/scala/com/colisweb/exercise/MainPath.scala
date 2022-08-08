package com.colisweb.exercise

import com.colisweb.exercise.ShortestPath.shortestPath
import com.colisweb.exercise.domain._
import com.colisweb.exercise.parsing.ReadFromResources._

import scala.util.Random._

object MainPath extends App {

  val seed = args.headOption.map(_.toLong)
  seed.foreach(setSeed)

  def gridNorthEast(width: Int, height: Int): List[Edge] =
    List
      .tabulate(width, height) {
        case (x, y) =>
          List(
            Edge(from = Point(x, y), to = Point(x + 1, y)),
            domain.Edge(from = Point(x, y), to = Point(x, y + 1)),
            domain.Edge(from = Point(x, y), to = Point(x + 1, y + 1))
          )
      }
      .flatten
      .flatten

  def gridFull(width: Int, height: Int): List[Edge] =
    List
      .tabulate(width, height) {
        case (x, y) =>
          List(
            domain.Edge(from = Point(x, y), to = Point(x + 1, y)),
            domain.Edge(from = Point(x, y), to = Point(x, y + 1)),
            domain.Edge(from = Point(x, y), to = Point(x, y - 1)),
            domain.Edge(from = Point(x, y), to = Point(x - 1, y))
          )
      }
      .flatten
      .flatten

  printResult("grid", path("grid.txt"))
  printResult("spiral", path("spiral.txt"))
  printResult("bunker", path("bunker.txt"))
  printResult("line", path("line.txt"))
  printResult("Small hole", path("small_hole.txt"))
  printResult("Stick man", path("stickman.txt"))
  printResult("Simple graph 5x4 grid", PathProblem(shuffle(gridNorthEast(5, 4)), Point(0, 0), Point(3, 3)))
  printResult("Large graph 50x40 grid", PathProblem(shuffle(gridNorthEast(50, 40)), Point(0, 0), Point(30, 30)))
  printResult("Large graph  with cycle 50x40 grid", PathProblem(shuffle(gridFull(50, 40)), Point(0, 0), Point(30, 30)))
  printResult("Huge graph 200x300 grid", PathProblem(shuffle(gridNorthEast(200, 300)), Point(0, 0), Point(30, 30)))

  def printResult(title: String, problem: PathProblem): Unit = {
    println(title)
    println(s"from ${problem.start} to ${problem.end}")
    shortestPath(problem) match {
      case Some(result) =>
        println(result.length)
        printPath(problem, result)
      case None         => println("no path found")
    }
    println()

  }

  def printPath(problem: PathProblem, path: Path): Unit = {
    val points = problem.graph
      .flatMap(edge => List(Point(edge.to.x.toInt, edge.to.y.toInt), Point(edge.from.x.toInt, edge.to.y.toInt)))
      .distinct

    val xs = path.points.map(_.x.toInt)
    val ys = path.points.map(_.y.toInt)

    for {
      i <- xs.min - 1 to xs.max + 1
      j <- ys.min - 1 to ys.max + 1
    } {
      if (problem.start == Point(i, j)) print("S")
      else if (problem.end == Point(i, j)) print("E")
      else if (path.points.contains(Point(i, j))) print(".")
      else if (points.contains(Point(i, j))) print(" ")
      else print("#")
      if (j == ys.max + 1) println()
    }
  }

}
