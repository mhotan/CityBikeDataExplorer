package se.kth.csc.moderndb.cbexplorer.core.domain;

import java.util.ArrayList;
import org.postgis.Point;

/**
 * POJO Area class.
 *
 * Created by Jeannine on 07.05.14.
 */
public class Area {

    /**
     * Points of the Polygon that borders the Area
     */
    private ArrayList<Point> polygonPoints;

    /**
     * Empty Constructor for POJO reasons
     */
    public Area() {
    }

    /**
     * Creates an POJO Area object
     *
     * @param polygonPoints points restricting the Area
     */
    public Area(ArrayList<Point> polygonPoints) {
        this.polygonPoints = polygonPoints;
    }

    public ArrayList<Point> getPolygonPoints() {
        return polygonPoints;
    }
}
