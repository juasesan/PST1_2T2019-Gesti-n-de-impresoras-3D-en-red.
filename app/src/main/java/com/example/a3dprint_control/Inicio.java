package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registrar(View view) {
        Intent i = new Intent(this, Registrar.class );
        startActivity(i);
    }

    public void ingresar(View view) {
        Intent i2 = new Intent(this, Ingresar.class );
        startActivity(i2);
    }
}
