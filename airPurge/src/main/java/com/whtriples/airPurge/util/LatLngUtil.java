package com.whtriples.airPurge.util;


public class LatLngUtil {

    private static final double EARTH_RADIUS = 6378137;//赤道半径(单位m)
    private static final double PI = 3.14159265;

    /**
     * 转化为弧度(rad)
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat3 第二点的纬度
     * @return 返回的距离，单位km
     */
    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * @param lat    纬度
     * @param lon    经度
     * @param raidus raidus 单位米  距离
     * @return
     */
    public static double[] getAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        //System.out.println(&quot;[&quot;+minLat+&quot;,&quot;+minLng+&quot;,&quot;+maxLat+&quot;,&quot;+maxLng+&quot;]&quot;);   
        return new double[]{minLat, minLng, maxLat, maxLng};
    }

    public static void main(String[] args) {
        double[] d = new double[]{33.54972648610388, 32.65120251389612, 112.20558750134002, 111.13299749865998};
        //d[2]d[1] d[2]d[0] d[3]d[0] d[3]d[1]
        System.out.println(GetDistance(d[2], d[1], d[3], d[1]));
        System.out.println(GetDistance(d[2], d[1], d[2], d[0]));
        System.out.println(GetDistance(d[3], d[0], d[3], d[1]));
        System.out.println(GetDistance(d[2], d[0], d[3], d[0]));
    }
}
