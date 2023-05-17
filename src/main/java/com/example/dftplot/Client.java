package com.example.dftplot;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Client {

    public static String GETrequest(String url) {
        try {
            var page = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            connection.disconnect();
            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }
    }

    public class Data{
        ArrayList<Sample> derecha;

        public ArrayList<Sample> getDerecha() {
            return derecha;
        }

        public void setDerecha(ArrayList<Sample> derecha) {
            this.derecha = derecha;
        }
    }

    public class Sample{
        private int t;
        private int x;
        private int y;
        private int z;



        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }

}
