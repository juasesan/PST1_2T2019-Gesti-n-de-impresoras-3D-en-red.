package com.example.a3dprint_control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Registrar extends Activity {

    private String serverIP = "remotemysql.com";
    private String port = "3306";
    private String userMySQL = "cD2kkGazJC";
    private String pwdMySQL = "34WoCvLgEW";
    private String database = "cD2kkGazJC";
    private String[] datosConexion = null;
    private EditText nomb;
    private EditText cont;
    private EditText corre;
    private EditText usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        nomb = (EditText) findViewById(R.id.nombre);
        cont = (EditText) findViewById(R.id.contraseña3);
        corre = (EditText) findViewById(R.id.correo3);
        usu = (EditText) findViewById(R.id.usuario);
    }


    public void insertarUsuario(View view) {
        try {
            String[] resultadoSQL = null;
            datosConexion = new String[]{
                    serverIP,
                    port,
                    database,
                    userMySQL,
                    pwdMySQL,
                    "SELECT * FROM Usuario;"
            };
            String driver2 = "com.mysql.jdbc.Driver";
            Class.forName(driver2).newInstance();
            resultadoSQL = new AsyncQuery().execute(datosConexion).get();
            String resultadoConsulta2 = resultadoSQL[0];
            String[] lista=resultadoConsulta2.split("\n");
            lista[0]="Nombre,Contraseña,Correo,User_name";
            int contador=1;
            boolean veridico=true;
            for(String dato: lista){
                if(contador!=1){
                    String[] listaCompleta= dato.split(",");
                    if ((listaCompleta[2].equals(corre.getText().toString()))||((corre.getText().toString()).equals(""))){
                        veridico=false;
                    }
                }else{
                    contador++;
                }
            }

            if(veridico){
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver).newInstance();

                String[] nuevaCita = new String[]{
                        nomb.getText().toString(),
                        cont.getText().toString(),
                        corre.getText().toString(),
                        usu.getText().toString(),
                        serverIP,
                        port,
                        database,
                        userMySQL,
                        pwdMySQL,
                };


                new TareaAsincronaInsertar().execute(nuevaCita);

                Toast.makeText(this,"Cuenta creada con exito", Toast.LENGTH_LONG).show();
                nomb.setText("");
                cont.setText("");
                corre.setText("");
                usu.setText("");
                finish();

            }else{
                Toast.makeText(this,"Correo o Usuario ya en uso o no ingreso ningun correo", Toast.LENGTH_LONG).show();
            }

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
        }
    }
}
