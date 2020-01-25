package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consulta extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        String url = "http://192.168.2.219/api/files/local/xyzCalibration_cube.gcode";
        String respuesta = "";
        try {
            peticionHttpGet(url);
            System.out.println("La respuesta es:\n" + respuesta);
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }
    public static void peticionHttpGet(String urlParaVisitar) throws Exception {
        // Esto es lo que vamos a devolver
        URL url = new URL(urlParaVisitar);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        //con.connect();
        con.setRequestProperty("X-Api-Key", "B680B923CCD84F77BFEF7F4B275D394B");
        con.setRequestProperty("Content-Type", "application/json");

        //con.getOutputStream().write( ("command=select"+"&print="+true).getBytes());

        String jsonInputString = "{\"print\": true, \"command\": \"select\"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        //conexion.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary_string);
        // Búferes para leer
        /**InputStream is = con.getInputStream();
         byte[] b = new byte[1024];
         while(is.read(b)!= (-1)){
         StringBuilder buffer = new StringBuilder();
         buffer.append(new String(b));
         }**/
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
        String response=in.readLine();
        System.out.print(response);
        con.disconnect();

    }


    public void print(View view) {

    }
}
