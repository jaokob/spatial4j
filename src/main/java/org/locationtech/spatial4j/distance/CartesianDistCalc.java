/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.distance;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Calculates based on Euclidean / Cartesian 2d plane.
 */
public class CartesianDistCalc extends AbstractDistanceCalculator {

  public static final CartesianDistCalc INSTANCE = new CartesianDistCalc();
  public static final CartesianDistCalc INSTANCE_SQUARED = new CartesianDistCalc(true);

  private final boolean squared;

  public static boolean[] flags = new boolean[5];

  public CartesianDistCalc() {
    this.squared = false;
  }

  /**
   * @param squared Set to true to have {@link #distance(org.locationtech.spatial4j.shape.Point, org.locationtech.spatial4j.shape.Point)}
   *                return the square of the correct answer. This is a
   *                performance optimization used when sorting in which the
   *                actual distance doesn't matter so long as the sort order is
   *                consistent.
   */
  public CartesianDistCalc(boolean squared) {
    this.squared = squared;
  }

  @Override
  public double distance(Point from, double toX, double toY) {
    double xSquaredPlusYSquared = distanceSquared(from.getX(), from.getY(), toX, toY);
    if (squared)
      return xSquaredPlusYSquared;

    return Math.sqrt(xSquaredPlusYSquared);
  }

  private static double distanceSquared(double fromX, double fromY, double toX, double toY) {
    double deltaX = fromX - toX;
    double deltaY = fromY - toY;
    return deltaX*deltaX + deltaY*deltaY;
  }

  /**
   * Distance from point to a line segment formed between points 'v' and 'w'.
   * It respects the "squared" option.
   */
  // TODO add to generic DistanceCalculator and develop geo versions.
  public double distanceToLineSegment(Point point, double vX, double vY, double wX, double wY) {
    // Translated from: http://bl.ocks.org/mbostock/4218871

    double d = distanceSquared(vX, vY, wX, wY);
    double toX;
    double toY;
    if (d <= 0) { // DD2480: Previous tests never went down this branch.
      flags[0] = true;
      toX = vX;
      toY = vY;
    } else {
      flags[1] = true;
      // t = ((point[0] - v[0]) * (w[0] - v[0]) + (point[1] - v[1]) * (w[1] - v[1])) / d
      double t = ((point.getX() - vX) * (wX - vX) + (point.getY() - vY) * (wY - vY)) / d;
      if (t < 0) {
        flags[2] = true;
        toX = vX;
        toY = vY;
      } else if (t > 1) {
        flags[3] = true;
        toX = wX;
        toY = wY;
      } else {
        flags[4] = true;
        toX = vX + t * (wX - vX);
        toY = vY + t * (wY - vY);
      }
    }
    writeToFile(); //branch coverage print function.
    return distance(point, toX, toY);
  }

  private void writeToFile(){
    try
    {
      String filename= "DistanceToLineSegment.txt";
      FileWriter fw = new FileWriter(filename,false); //the true will append the new data
      fw.write("DistanceToLineSegment \n");
      int count = 0;
      for (boolean b :flags) {
        if (b) count ++;
        fw.write(b + " ");
      }
      fw.write("\nCoverage: " + (Double.toString((double) count/flags.length)) );
      fw.close();
    }
    catch(IOException ioe)
    {
      System.err.println("IOException: " + ioe.getMessage());
    }
  }

  @Override
  public boolean within(Point from, double toX, double toY, double distance) {
    double deltaX = from.getX() - toX;
    double deltaY = from.getY() - toY;
    return deltaX*deltaX + deltaY*deltaY <= distance*distance;
  }

  @Override
  public Point pointOnBearing(Point from, double distDEG, double bearingDEG, SpatialContext ctx, Point reuse) {
    if (distDEG == 0) {
      if (reuse == null)
        return from;
      reuse.reset(from.getX(), from.getY()); // DD2480: Previous tests never visited this line.
      return reuse;
    }
    double bearingRAD = DistanceUtils.toRadians(bearingDEG);
    double x = from.getX() + Math.sin(bearingRAD) * distDEG;
    double y = from.getY() + Math.cos(bearingRAD) * distDEG;
    if (reuse == null) {
      return ctx.makePoint(x, y);
    } else {  // DD2480: Previous tests never visited this branch.
      reuse.reset(x, y);
      return reuse;
    }
  }

  @Override
  public Rectangle calcBoxByDistFromPt(Point from, double distDEG, SpatialContext ctx, Rectangle reuse) {
    double minX = from.getX() - distDEG;
    double maxX = from.getX() + distDEG;
    double minY = from.getY() - distDEG;
    double maxY = from.getY() + distDEG;
    if (reuse == null) {
      return ctx.makeRectangle(minX, maxX, minY, maxY);
    } else {
      reuse.reset(minX, maxX, minY, maxY);
      return reuse;
    }
  }

  @Override
  public double calcBoxByDistFromPt_yHorizAxisDEG(Point from, double distDEG, SpatialContext ctx) {
    return from.getY();
  }

  @Override
  public double area(Rectangle rect) {
    return rect.getArea(null);
  }

  @Override
  public double area(Circle circle) {
    return circle.getArea(null);
  }

  private static boolean[] flags2 = new boolean[4];
  private void writeToFile2(){
    try
    {
      String filename= "equals.txt";
      FileWriter fw = new FileWriter(filename,false); //the true will append the new data
      fw.write("equals \n");
      int count = 0;
      for (boolean b :flags2) {
        if (b) count ++;
        fw.write(b + " ");
      }
      fw.write("\nCoverage: " + (Double.toString((double) count/flags2.length)) );
      fw.close();
    }
    catch(IOException ioe)
    {
      System.err.println("IOException: " + ioe.getMessage());
    }
  }

  @Override
  public boolean equals(Object o) {

    if (this == o){
      flags2[0] = true;
      writeToFile2();
      return true;
    }
    if (o == null || getClass() != o.getClass()){
      flags2[1] = true;
      writeToFile2();
      return false;
    }

    CartesianDistCalc that = (CartesianDistCalc) o;

    if (squared != that.squared){
      flags2[2] = true;
      writeToFile2();
      return false;
    }
    flags2[3] = true;
    writeToFile2();
    return true;
  }

  @Override
  public int hashCode() {
    return (squared ? 1 : 0);
  }
}
