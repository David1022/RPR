package com.nipsa.rpr;

import android.graphics.Matrix;
import android.test.InstrumentationTestRunner;
import android.text.util.Linkify;

/**
 * Created by David.Mendano on 21/09/2017.
 */

public class InfoApoyoEndesa {

    private Double longitud, latitud;
    private String datum;

    public static final String INTERNATIONAL11924 = "international11924";
    public static final String WGS84 = "wgs84";

    private static final Double A_EQUATORIAL_RADIUS_ED50 = 6378388d; // Equatorial Radius for International (ED50)
    private static final Double B_EQUATORIAL_RADIUS_ED50 = 6356911.9; // Equatorial Radius for International (ED50)
    private static final Double A_EQUATORIAL_RADIUS_WGS84 = 6378137d; // Equatorial Radius for International (WGS84)
    private static final Double B_EQUATORIAL_RADIUS_WGS84 = 6356752.314245; // Equatorial Radius for International (WGS84)


    public InfoApoyoEndesa(){
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public static Double atn2 (Double y, Double x) {
        Double atn2;

        if (x > 0) {
            atn2 = Math.atan(y/x);
        } else if (x < 0) {
            atn2 = Math.signum(y) * (Math.PI - Math.atan(Math.abs(y/x)));
        } else if (y == 0){
            atn2 = 0d;
        } else {
            atn2 = Math.signum(y) * (Math.PI / 2);
        }

        return atn2;
    }

    /**
     * Conversor entre dos sistema de coordenadas (desde sistema "coord.datum" al sistema newDatum")
     * @param coord
     * @param newDatum
     * @param tranX
     * @param tranY
     * @param tranZ
     * @param escala
     * @param rotX
     * @param rotY
     * @param rotZ
     * @return
     */
    public static InfoApoyoEndesa transformarDatum (InfoApoyoEndesa coord, String newDatum, Integer tranX, Integer tranY,
                                            Integer tranZ, Integer escala, Double rotX, Double rotY, Double rotZ) {
        Double a_old, b_old, e2_old, x_old, y_old, z_old, phi_old, lambda_old;
        Double a_new, b_new, e2_new, x_new, y_new, z_new, phi_new, lambda_new;
        InfoApoyoEndesa result = new InfoApoyoEndesa();
        Double h = 0d; // Se considera altura 0, si no fuera así debería pasarse como parámetro

        a_old = 0d;
        e2_old = 0d;
        a_new = 0d;
        e2_new = 0d;
        // Se verifica que los sistema de coordenadas de las coordenadas que se pasan y donde queremos
        // llegar son diferentes, en caso contrario se sale
        if (coord.getDatum().equals(newDatum)) {
            return null;
        }

        // Se define el elipsoide del orígen
        if (coord.getDatum().equals(INTERNATIONAL11924)) {
            a_old = A_EQUATORIAL_RADIUS_ED50; // Equatorial Radius for International (ED50)
            b_old = B_EQUATORIAL_RADIUS_ED50; // Equatorial Radius for International (ED50)
            e2_old = (Math.pow(a_old, 2) - Math.pow(b_old, 2)) / Math.pow(a_old, 2); // Square of eccentricity for International (ED50)
        } else if (coord.getDatum().equals(WGS84)) {
            a_old = A_EQUATORIAL_RADIUS_WGS84; // Equatorial Radius for WGS84
            b_old = B_EQUATORIAL_RADIUS_WGS84; // Equatorial Radius for WGS84
            e2_old = ((Math.pow(a_old, 2) - Math.pow(b_old, 2)) / Math.pow(a_old, 2));
        }

        // Se obtienen el resto de variable
        phi_old = coord.getLatitud() * Math.PI / 180;
        lambda_old = coord.getLongitud() * Math.PI / 180;

        // Se convierten las coordenadas de latitud/longitud --> a cartesianas (x,y,z) [sistema de coordenadas viejo "coord.Datum"]
        Double v_old = a_old / Math.sqrt(1 - e2_old * (Math.pow(Math.sin(phi_old), 2)));
        x_old = (v_old + h) * Math.cos(phi_old) * Math.cos(lambda_old);
        y_old = (v_old + h) * Math.cos(phi_old) * Math.sin(lambda_old);
        z_old = ((1 - e2_old) * v_old + h) * Math.sin(phi_old);

        // Conversion a coordenadas cartesianas del nuevo sistema de coordenadas
        x_new = tranX + (x_old * (1 + escala)) - (y_old * rotZ) + (z_old * rotY);
        y_new = tranY + (x_old * rotZ) + (y_old * (1 + escala)) - (z_old * rotX);
        z_new = tranZ + (x_old * rotY) + (y_old * rotX) + (z_old * (1 + escala));

        //Se define el destino
        if (newDatum.equals(INTERNATIONAL11924)) {
            a_new = A_EQUATORIAL_RADIUS_ED50; // Equatorial Radius for International (ED50)
            b_new = B_EQUATORIAL_RADIUS_ED50; // Equatorial Radius for International (ED50)
            e2_new = (Math.pow(a_new, 2) - Math.pow(b_new, 2)) / Math.pow(a_new, 2); // Square of eccentricity for International (ED50)
        } else if ( newDatum.equals(WGS84)) {
            a_new = A_EQUATORIAL_RADIUS_WGS84; // Equatorial Radius for WGS84
            e2_new = 0.00669438; // Square of eccentricity for WGS84
        }

        // Se vuelve a pasar a cartesiana (x,y,z) --> latitud/longitud [sistema coordenadas nuevo "newDatum"]
        lambda_new = (atn2(y_new, x_new)) * 180 / Math.PI;
        Double p = Math.sqrt(Math.pow(x_new, 2) + Math.pow(y_new, 2));
        phi_new = Math.atan(z_new / (p * (1 - e2_new)));
        Double phi1 = 0d;
        Double v_new = 0d;
        do {
            phi1 = phi_new;
            v_new = a_new / Math.sqrt(1 - (e2_new * (Math.pow(Math.sin(phi_new), 2))));
            phi_new = Math.atan((z_new + (e2_new * v_new * Math.sin(phi_new))) / p);
        } while (Math.abs(phi_new - phi1) >= 0.00001);

        Double h_new = p / Math.cos(phi_new) - v_new;
        phi_new = phi_new * 180 / Math.PI;

        result.setLatitud(phi_new);
        result.setLongitud(lambda_new);
        result.setDatum(newDatum);

        return result;
    }

    /**
     *
     * @param coord84
     * @return
     */
    public static InfoApoyoEndesa converToGPS84toGPS50 (InfoApoyoEndesa coord84) {
        Integer tx, ty, tz, s;
        Double rx, ry, rz;

        tx = 87;
        ty = 98;
        tz = 121;
        s= 0;
        rx = 0 * Math.PI / 180;
        ry = 0 * Math.PI / 180;
        rz = 0 * Math.PI / 180;

        return transformarDatum(coord84, INTERNATIONAL11924, tx, ty, tz, s, rx, ry, rz);
    }

    public static InfoApoyoEndesaUTM convertToGPSToUTM (InfoApoyoEndesa coord50) {
        InfoApoyoEndesaUTM result = new InfoApoyoEndesaUTM();

        int r = (int) (((coord50.getLongitud() + 180) / 6) + 1);
        result.setHuso((double) r);

        // Definimos las variables
        Double escala = 0.9996;
        Double n0 = 0d;
        Double e0 = 500000d;
        Double phi0 = 0d;
        Double lambda0 = (result.getHuso() - 1) * 6 - 180 + 3;

        Double originalLat = phi0;
        Double originalLong = lambda0 * Math.PI / 180;

        Double lat = coord50.getLatitud() * Math.PI / 180;
        Double sinLat = Math.sin(lat);
        Double cosLat = Math.cos(lat);
        Double tanLat = Math.tan(lat);
        Double tanLatSq = Math.pow(tanLat, 2);

        Double lon = coord50.getLongitud() * Math.PI / 180;

        Double a = 0d;
        Double b = 0d;
        Double e2 = 0d;

        // Verificamos que coord50 sean ED50
        if (coord50.getDatum().equals(INTERNATIONAL11924)) {
            a = A_EQUATORIAL_RADIUS_ED50; // Equatorial Radius for International (ED50)
            b = B_EQUATORIAL_RADIUS_ED50; // Equatorial Radius for International (ED50)
            e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2); // Square eccentricity for International (ED50)
        }

        Double n = (a - b) / (a + b);
        Double nSq = Math.pow(n, 2);
        Double nCu = Math.pow(n, 3);

        Double v = a * escala * Math.pow((1 - e2 * Math.pow(sinLat, 2)), -0.5);
        Double p = a * escala * (1 - e2) * Math.pow((1 - e2 * Math.pow(sinLat, 2)), -1.5);
        Double hSq = (v / p) - 1;

        Double latPlusOrigin = lat + originalLat;
        Double latMinusOrigin = lat - originalLat;

        Double longMinusOrigin = lon - originalLong;

        Double m = b * escala * ((1 + n + 1.25 * (nSq + nCu)) * latMinusOrigin
                - (3 * (n + nSq) + 2.625 * nCu) * Math.sin(latMinusOrigin) * Math.cos(latPlusOrigin)
                + 1.875 * (nSq + nCu) * Math.sin(2 * latMinusOrigin) * Math.cos(2 * latPlusOrigin)
                - (35 / 24 * nCu * Math.sin(3 * latMinusOrigin) * Math.cos(3 * latPlusOrigin)));

        Double I = m + n0;
        Double II = v / 2 * sinLat * cosLat;
        Double III = v /24 * sinLat * Math.pow(cosLat, 3) * (5 - tanLatSq + Math.pow(tanLatSq, 2));
        Double IIIA = 0d;
        Double IV = v * cosLat;
        Double VV = v / 6 * Math.pow(cosLat,3) * (v / p - tanLatSq);
        Double VI = v / 120 * Math.pow(cosLat, 5) * (5 - 18 * tanLatSq + Math.pow(tanLatSq, 2) + 14 * hSq - 58
                * tanLatSq * hSq);

        Double utmX = e0 + IV * longMinusOrigin + VV * Math.pow(longMinusOrigin, 3) + VI * Math.pow(longMinusOrigin, 5);
        Double utmY = I + II * Math.pow(longMinusOrigin, 2) + III * Math.pow(longMinusOrigin, 4) + IIIA *
                Math.pow(longMinusOrigin, 6);
        result.setUTMx(utmX);
        result.setUTMy(utmY);

        return result;
    }
}
