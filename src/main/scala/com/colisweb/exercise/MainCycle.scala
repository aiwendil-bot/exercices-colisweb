package com.colisweb.exercise

import com.colisweb.exercise.domain.{Path, Point}
import com.colisweb.exercise.parsing.ReadFromResources

import scala.util.Random

object MainCycle extends App {
  val seed = args.headOption.map(_.toLong)
  seed.foreach(Random.setSeed)

  def pointsInGrid(width: Int, height: Int): Seq[Point] =
    Seq
      .tabulate(width, height) {
        case (x, y) => Point(x, y)
      }
      .flatten

  Cycle.shortCycle(pointsInGrid(10, 8))

  println("80 points in a 8x10 grid")
  printResult(Cycle.shortCycle(Random.shuffle(pointsInGrid(10, 8))))

  println("200 random points")
  printResult(Cycle.shortCycle(Random.shuffle(Seq.fill(200)(Point(Random.nextInt(700), Random.nextInt(700))))))

  println("File 14 nodes")
  printResult(Cycle.shortCycle(ReadFromResources.cycle("14_nodes.txt")))

  println("File 52 nodes")
  printResult(Cycle.shortCycle(ReadFromResources.cycle("52_nodes.txt")))

  println("File 202 nodes")
  printResult(Cycle.shortCycle(ReadFromResources.cycle("202_nodes.txt")))

  println("File 1002 nodes")
  printResult(Cycle.shortCycle(ReadFromResources.cycle("1002_nodes.txt")))

  println("File 5915 nodes")
  printResult(Cycle.shortCycle(ReadFromResources.cycle("5915_nodes.txt")))

  def printResult(path: Path): Unit = {
    println(path.length)
    println(path.points.reverse.mkString(","))
    println()

  }
}
