package com.example.a3dprint_control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registrar extends Activity {

    private String serverIP = "remotemysql.com";
    private String port = "3306";
    private String userMySQL = "cD2kkGazJC";
    private String pwdMySQL = "34WoCvLgEW";
    private String database = "cD2kkGazJC";
    private EditText nomb;
    private EditText cont;
    private EditText corr;
    private EditText usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        nomb = (EditText) findViewById(R.id.nombre);
        cont = (EditText) findViewById(R.id.contraseña3);
        corr = (EditText) findViewById(R.id.correo3);
        usu = (EditText) findViewById(R.id.usuario);
    }

    public void siguiente(View view) {
        Intent i = new Intent(this, Printers.class);
        startActivity(i);
    }

    public void insertarUsuario(View view) {
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver).newInstance();

            String[] nuevaCita = new String[]{
                    nomb.getText().toString(),
                    cont.getText().toString(),
                    corr.getText().toString(),
                    usu.getText().toString(),
                    serverIP,
                    port,
                    database,
                    userMySQL,
                    pwdMySQL,
            };


            new TareaAsincronaInsertar().execute(nuevaCita);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
