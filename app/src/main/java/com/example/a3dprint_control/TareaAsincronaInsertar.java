package com.example.a3dprint_control;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TareaAsincronaInsertar extends AsyncTask<String[],Void,Void> {

    private Connection conexionMySQL = null;
    private Statement st = null;

    @SuppressLint("LongLogTag")
    protected Void doInBackground(String[]... datos) {

        String NOMBRE = datos[0][0];
        String CONTRASEÑA = datos[0][1];
        String CORREO = datos[0][2];
        String USUARIO_NOM = datos[0][3];
        String SERVIDOR = datos[0][4];
        String PUERTO = datos[0][5];
        String BD = datos[0][6];
        String USUARIO = datos[0][7];
        String PASSWORD = datos[0][8];

        try {
            conexionMySQL = DriverManager.getConnection("jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BD, USUARIO, PASSWORD);
            st = conexionMySQL.createStatement();
            st.executeUpdate("INSERT INTO `Usuario` (Nombre, Contraseña, Correo, User_name) VALUES " +
                    "('" + NOMBRE + "','" + CONTRASEÑA + "','" + CORREO + "','" + USUARIO_NOM + "')");
        } catch (SQLException ex) {
            Log.d("No ha sido posible insertar una nueva cita", ex.getMessage());
        } finally {
            try {
                st.close();
                conexionMySQL.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
