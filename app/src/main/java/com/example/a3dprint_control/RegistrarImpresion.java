package com.example.a3dprint_control;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrarImpresion extends AsyncTask<String[],Void,Void> {

    private Connection conexionMySQL = null;
    private Statement st = null;

    @SuppressLint("LongLogTag")
    protected Void doInBackground(String[]... datos) {

        String NUM_IMPRESION = datos[0][0];
        String FECHA = datos[0][1];
        String NOMBRE_ARCHIVO= datos[0][2];
        String CORREO_USUARIO = datos[0][3];
        String ID_IMPRESORA= datos[0][4];
        String RUTA = datos[0][5];
        String SERVIDOR = datos[0][6];
        String PUERTO = datos[0][7];
        String BD = datos[0][8];
        String USUARIO = datos[0][9];
        String PASSWORD = datos[0][10];

        try {
            conexionMySQL = DriverManager.getConnection("jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BD, USUARIO, PASSWORD);
            st = conexionMySQL.createStatement();
            st.executeUpdate("INSERT INTO `Impresion` (Num_Impresion,Fecha, Nom_archivo, Correo_usuario,Id_impresora,Ruta) VALUES " +
                    "('" + NUM_IMPRESION + "','" + FECHA + "','" + NOMBRE_ARCHIVO + "','" + CORREO_USUARIO + "','" +ID_IMPRESORA+ "','" +RUTA +"')");
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
