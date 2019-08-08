package math;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
 
/**
 * 判断一个点是否在一个多边形内
 * 提供了三种计算方式，孰好孰坏在实践中比较一下
 * @author xiongshiyan at 2019/1/11 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class GeometryUtil {
    private static final int POLYGON_MIN_SIZE = 3;
 
    public static boolean isPtInPolygon (Point2D.Double point , List<Point2D.Double> polygon) {
        assertParams(point, polygon);
 
        int iSum,iIndex;
        double dLon1 , dLon2 , dLat1 , dLat2 , dLon;
        int size = polygon.size();
        iSum = 0;
        for (iIndex = 0; iIndex<size; iIndex++) {
            if (iIndex == size - 1) {
                dLon1 = polygon.get(iIndex).getX();
                dLat1 = polygon.get(iIndex).getY();
                dLon2 = polygon.get(0).getX();
                dLat2 = polygon.get(0).getY();
            } else {
                dLon1 = polygon.get(iIndex).getX();
                dLat1 = polygon.get(iIndex).getY();
                dLon2 = polygon.get(iIndex + 1).getX();
                dLat2 = polygon.get(iIndex + 1).getY();
            }
            // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
            if (((point.y >= dLat1) && (point.y < dLat2))
                    || ((point.y >= dLat2) && (point.y < dLat1))) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    //得到 A点向左射线与边的交点的x坐标：
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - point.y) ) / (dLat1 - dLat2);
                    // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
                    if (dLon < point.x) {
                        iSum++;
                    }
                }
            }
        }
        return (iSum % 2) != 0;
    }
 
    /**
     * 返回一个点是否在一个多边形区域内， 如果点位于多边形的顶点或边上，不算做点在多边形内，返回false
     */
    public static boolean isPointInPoly(Point2D.Double point, List<Point2D.Double> polygon) {
        assertParams(point, polygon);
 
        java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x, first.y);
        int size = polygon.size();
        for (int i = 1; i < size; i++) {
            Point2D.Double pa = polygon.get(i);
            p.lineTo(pa.x, pa.y);
        }
        p.lineTo(first.x, first.y);
        p.closePath();
        return p.contains(point);
    }
 
    /**
     * 判断点是否在多边形内，如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
     * @param point 检测点
     * @param polygon   多边形的顶点
     * @return      点在多边形内返回true,否则返回false
     */
    public static boolean isPtInPoly(Point2D.Double point, List<Point2D.Double> polygon){
        assertParams(point, polygon);
 
        int N = polygon.size();
        //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        boolean boundOrVertex = true;
        //cross points count of x
        int intersectCount = 0;
        //浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;
        //neighbour bound vertices
        Point2D.Double p1, p2;
        //当前点
        Point2D.Double p = point;
 
        //left vertex
        p1 = polygon.get(0);
        //check all rays
        for(int i = 1; i <= N; ++i){
            if(p.equals(p1)){
                //p is an vertex
                return boundOrVertex;
            }
 
            //right vertex
            p2 = polygon.get(i % N);
            //ray is outside of our interests
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){
                p1 = p2;
                //next ray left point
                continue;
            }
 
            //ray is crossing over by the algorithm (common part of)
            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){
                //x is before of ray
                if(p.y <= Math.max(p1.y, p2.y)){
                    //overlies on a horizontal ray
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){
                        return boundOrVertex;
                    }
 
                    //ray is vertical
                    if(p1.y == p2.y){
                        //overlies on a vertical ray
                        if(p1.y == p.y){
                            return boundOrVertex;
                            //before ray
                        }else{
                            ++intersectCount;
                        }
                        //cross point on the left side
                    }else{
                        //cross point of y
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        //overlies on a ray
                        if(Math.abs(p.y - xinters) < precision){
                            return boundOrVertex;
                        }
 
                        //before ray
                        if(p.y < xinters){
                            ++intersectCount;
                        }
                    }
                }
                //special case when ray is crossing through the vertex
            }else{
                //p crossing over p2
                if(p.x == p2.x && p.y <= p2.y){
                    //next vertex
                    Point2D.Double p3 = polygon.get((i+1) % N);
                    //p.x lies between p1.x & p3.x
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){
                        ++intersectCount;
                    }else{
                        intersectCount += 2;
                    }
                }
            }
            //next ray left point
            p1 = p2;
        }
 
        //偶数在多边形外
        if(intersectCount % 2 == 0){
            return false;
            //奇数在多边形内
        } else {
            return true;
        }
    }
    private static void assertParams(Point2D.Double point, List<Point2D.Double> polygon) {
        if(null == point || null == polygon || polygon.size() < POLYGON_MIN_SIZE) {
            throw new IllegalArgumentException("参数不能为空，且多边形点数大于3");
        }
    }
    
    
    
    public static void main(String[] args) {
    	List<Point2D.Double> points =
                Arrays.asList(
                        new Point2D.Double(116.169465,39.932670),
                        new Point2D.Double(116.160260,39.924492),
                        new Point2D.Double(116.186138,39.879817),
                        new Point2D.Double(116.150625,39.710019),
                        new Point2D.Double(116.183198,39.709920),
                        new Point2D.Double(116.226950,39.777616),
                        new Point2D.Double(116.421078,39.810771),
                        new Point2D.Double(116.442621,39.799892),
                        new Point2D.Double(116.463478,39.790066),
                        new Point2D.Double(116.588276,39.809551),
                        new Point2D.Double(116.536091,39.808859),
                        new Point2D.Double(116.573856,39.839643),
                        new Point2D.Double(116.706380,39.916740),
                        new Point2D.Double(116.657285,39.934545),
                        new Point2D.Double(116.600293,39.937770),
                        new Point2D.Double(116.540039,39.937968),
                        new Point2D.Double(116.514805,39.982375),
                        new Point2D.Double(116.499935,40.013710),
                        new Point2D.Double(116.546520,40.030443),
                        new Point2D.Double(116.687668,40.129961),
                        new Point2D.Double(116.539697,40.080659),
                        new Point2D.Double(116.503390,40.058474),
                        new Point2D.Double(116.468800,40.052578));
 
        Point2D.Double pointNot = new Point2D.Double(116.566298, 40.014179);
        Point2D.Double pointYes = new Point2D.Double(116.529906,39.904706);
        Point2D.Double pointYes2 = new Point2D.Double(116.367171,39.968411);
 
        /*System.out.println(GeometryUtil.isPtInPoly(pointNot, points));
        System.out.println(GeometryUtil.isPtInPoly(pointYes, points));
        System.out.println(GeometryUtil.isPtInPoly(pointYes2, points));*/
 
        /* System.out.println(GeometryUtil.isPointInPoly(pointNot, points));
        System.out.println(GeometryUtil.isPointInPoly(pointYes, points));
        System.out.println(GeometryUtil.isPointInPoly(pointYes2, points));*/
 
        /*System.out.println(GeometryUtil.isPtInPolygon(pointNot, points));
        System.out.println(GeometryUtil.isPtInPolygon(pointYes, points));
        System.out.println(GeometryUtil.isPtInPolygon(pointYes2, points));*/
        
        int count = 0;
        long time1 = System.currentTimeMillis();
        System.out.println(time1);
        while (count < 50000000) {
        	GeometryUtil.isPtInPoly(pointNot, points);
        	GeometryUtil.isPtInPoly(pointYes, points);
        	GeometryUtil.isPtInPoly(pointYes2, points);
        	++count;
        }
        
        count = 0;
        long time2 = System.currentTimeMillis();
        
 
        while (count < 50000000) {
        	GeometryUtil.isPointInPoly(pointNot, points);
        	GeometryUtil.isPointInPoly(pointYes, points);
        	GeometryUtil.isPointInPoly(pointYes2, points);
        	++count;
        }
        long time3 = System.currentTimeMillis();
 
        count = 0;
        while (count < 50000000) {
        	GeometryUtil.isPtInPolygon(pointNot, points);
        	GeometryUtil.isPtInPolygon(pointYes, points);
        	GeometryUtil.isPtInPolygon(pointYes2, points);
        	++count;
        }
        long time4 = System.currentTimeMillis();
        
        System.out.println(time2 - time1);
        System.out.println(time3 - time2);
        System.out.println(time4 - time3);
        
        
    }
}
