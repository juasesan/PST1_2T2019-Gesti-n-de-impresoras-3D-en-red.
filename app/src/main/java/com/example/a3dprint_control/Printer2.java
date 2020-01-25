package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Printer2 extends Activity {
    private TextView textosalida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer2);
        textosalida=(TextView)findViewById(R.id.textJason);

    }


    public static String peticionHttpGet(String urlParaVisitar) throws Exception {
        // Esto es lo que vamos a devolver
        StringBuilder resultado = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(urlParaVisitar);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        conexion.setRequestProperty("X-Api-Key", "DCB5DB6F053845BDAEDB213B7BDB56FF");
        //conexion.setRequestProperty("command", "select");
        //conexion.setRequestProperty("print", "true");
        // Búferes para leer
        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((linea = rd.readLine()) != null) {
            resultado.append(linea);
        }
        // Cerrar el BufferedReader
        rd.close();
        // Regresar resultado, pero como cadena, no como StringBuilder
        return resultado.toString();
    }

    public  void manejoJson(View view){
        String url = "http://192.168.0.16/api/files/local";
        String respuesta = "";
        try {
            respuesta = peticionHttpGet(url);
            JSONObject jsonresult = new JSONObject(respuesta);
            JSONArray array = (JSONArray) jsonresult.get("files");
            System.out.println("DATOS DE ARCHIVOS");
            for(int i =0; i<array.length();i++){
                JSONObject jsonobj = (JSONObject) array.get(i);
                textosalida.setText(jsonobj.toString());
            }
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }
}
