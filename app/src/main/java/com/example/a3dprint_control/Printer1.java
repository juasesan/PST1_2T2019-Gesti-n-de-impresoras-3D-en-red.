package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Printer1 extends Activity {

    private String serverIP = "remotemysql.com";
    private String port = "3306";
    private String userMySQL = "cD2kkGazJC";
    private String pwdMySQL = "34WoCvLgEW";
    private String database = "cD2kkGazJC";
    private String[] datosConexion = null;
    private TextView nombreIm;
    private TextView lugarIm;
    private Button btfile;
    private Date fecha;
    private int id_impresora;
    private String ruta;
    private String nombreArchi;
    private String correoUsu;
    private static int id_impresion=1;
    private static final int READ_REQUEST_CODE = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer1);
        nombreIm=(TextView) findViewById(R.id.modelo1);
        lugarIm=(TextView) findViewById(R.id.lugar1);
        btfile=(Button)findViewById(R.id.btn_file);
        String[] resultadoSQL = null;
        try{
            datosConexion = new String[]{
                    serverIP,
                    port,
                    database,
                    userMySQL,
                    pwdMySQL,
                    "SELECT * FROM Impresora WHERE Id_impresora=1;"
            };
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver).newInstance();
            resultadoSQL = new AsyncQuery().execute(datosConexion).get();
            String resultadoConsulta = resultadoSQL[0];
            String[] lista=resultadoConsulta.split(",");
            id_impresora=1;
            nombreIm.setText("Modelo: "+lista[1]);
            lugarIm.setText("Lugar: "+lista[2]);

        }catch(Exception ex)
        {
            Toast.makeText(this, "Error al obtener resultados de la consulta Transact-SQL: "
                    + ex.getMessage()+ ex.getClass(), Toast.LENGTH_LONG).show();
        }
    }
    public static String peticionHttpGet(String urlParaVisitar) throws Exception {
        // Esto es lo que vamos a devolver
        StringBuilder resultado = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(urlParaVisitar);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        conexion.setRequestProperty("X-Api-Key", "B680B923CCD84F77BFEF7F4B275D394B");
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
        String url = "http://192.168.2.219/api/files/local";
        String respuesta = "";
        try {
            respuesta = peticionHttpGet(url);
            JSONObject jsonresult = new JSONObject(respuesta);
            JSONArray array = (JSONArray) jsonresult.get("files");
            for(int i =0; i<array.length();i++){
                JSONObject jsonobj = (JSONObject) array.get(i);
                System.out.println(jsonobj);
                btfile.setText(jsonobj.toString());
            }
            Intent i = new Intent(this, Consulta.class );
            startActivity(i);
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }

    public void buscarArchivo(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                String driver = "com.mysql.jdbc.Driver";
                ruta=uri.getPath();
                String[] algo=ruta.split("/");
                int longitud= algo.length;
                nombreArchi=algo[longitud-1];
                if (nombreArchi.indexOf(".gcode")>-1) {
                    fecha = new Date();
                    Bundle datos = getIntent().getExtras();
                    correoUsu = datos.getString("Correo");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Class.forName(driver).newInstance();


                        String[] nuevaCita = new String[]{
                                Integer.toString(id_impresion),
                                formatoFecha.format(fecha),
                                nombreArchi,
                                correoUsu,
                                Integer.toString(id_impresora),
                                ruta,
                                serverIP,
                                port,
                                database,
                                userMySQL,
                                pwdMySQL,
                        };


                        new RegistrarImpresion().execute(nuevaCita);
                        id_impresion++;

                        Toast.makeText(this, "Subida de archivo realizada con exito", Toast.LENGTH_LONG).show();

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(this, "Tipo de archivo incorrecto, debe ser de tipo .gcode", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
