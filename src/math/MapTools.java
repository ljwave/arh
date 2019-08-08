package math;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Created by wanglei on 17-2-14.
 */
public class MapTools {
 
    public static List genPointList() {
        List list = new ArrayList();
        
        Point2D.Double p1 = new Point2D.Double(0.0,0.0);
        Point2D.Double p2 = new Point2D.Double(0.0,0.5);
        Point2D.Double p3 = new Point2D.Double(0.0,1.0);
        Point2D.Double p4 = new Point2D.Double(1.0,0.5);
        Point2D.Double p5 = new Point2D.Double(1.0,1.0);
        Point2D.Double p6 = new Point2D.Double(1.0,0.5);
        Point2D.Double p7 = new Point2D.Double(1.0,0.0);
        Point2D.Double p8 = new Point2D.Double(0.5,0.0);
        
        
 
        Point2D.Double[] points = {p1,p2,p3,p4,p5,p6,p7,p8};
        for (int i = 0; i < points.length; i++) {
            list.add(points[i]);
        }
 
        return list;
    }
 
    public static boolean checkWithPath(Point2D.Double point, List<Point2D.Double> polygon) {
        GeneralPath p = new GeneralPath();
        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x,first.y);
        for(Point2D.Double d: polygon) {
            p.lineTo(d.x,d.y);
        }
        p.lineTo(first.x,first.y);
        p.closePath();
        return p.contains(point);
    }
 
    public static void main(String[] args) {
        List list = genPointList();
        Point2D.Double p1 = new Point2D.Double(1.5,1.5);
        Point2D.Double p2 = new Point2D.Double(0.2,0.3);
        Point2D.Double p3 = new Point2D.Double(1.5,0.1);
        Point2D.Double p4 = new Point2D.Double(0.8,0.0);
        Point2D.Double p5 = new Point2D.Double(0.08,0.01);
        List<Point2D.Double> testList = new ArrayList();
        testList.add(p1);
        testList.add(p2);
        testList.add(p3);
        testList.add(p4);
        testList.add(p5);
        for(Point2D.Double each:testList) {
            boolean flag = checkWithPath(each,list);
            System.out.println(each.toString() + " is in polygon: " + flag);
        }
    }
}
