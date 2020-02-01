package com.example.a3dprint_control;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.mysql.fabric.Response;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Printer1 extends Activity {

    private String serverIP = "remotemysql.com";
    private String port = "3306";
    private String userMySQL = "cD2kkGazJC";
    private String pwdMySQL = "34WoCvLgEW";
    private String database = "cD2kkGazJC";
    private String[] datosConexion = null;
    private String[] datosConexion1 = null;
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
    private ListView listview;
    private ArrayList<String> archivos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer1);

        //INICIO DEL CODIGO PARA OBTENER LAS RUTAS DE LOS ARCHIVOS

        listview = (ListView) findViewById(R.id.vistaLista);
        String[] resultadoSQL1 = null;
        try{
            datosConexion1 = new String[]{
                    serverIP,
                    port,
                    database,
                    userMySQL,
                    pwdMySQL,
                    "SELECT * FROM Impresion;"
            };
            String driver1 = "com.mysql.jdbc.Driver";
            Class.forName(driver1).newInstance();
            resultadoSQL1 = new AsyncQuery().execute(datosConexion1).get();
            String resultadoConsulta1 = resultadoSQL1[0];
            String []lista1=resultadoConsulta1.split("\n");
            archivos=new ArrayList<String>();
            int contador=0;
            for(String dato: lista1){
                if(contador!=0){
                    String[] seccion=dato.split(",");
                    archivos.add(seccion[5]);
                    id_impresion=Integer.parseInt(seccion[0])+1;
                }else{
                    contador++;
                }

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, archivos);
            //listview.setAdapter(adapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
      

        //FIN DEL CODIGO PARA OBTENER LAS RUTAS DE LOS ARCHIVOS

        nombreIm=(TextView) findViewById(R.id.modelo2);
        lugarIm=(TextView) findViewById(R.id.lugar2);
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

    public void consulta(View v) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.43.100/api/files/local";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        manejarGet(response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Host", "192.168.43.100");
                params.put("X-Api-Key", "B680B923CCD84F77BFEF7F4B275D394B");

                return params;
            }
        };
        queue.add(getRequest);


    }

    public void manejarGet(String response){
        Intent i = new Intent(this,Consulta.class);
        i.putExtra("jsonresponse",response);
        startActivity(i);
    }
}
