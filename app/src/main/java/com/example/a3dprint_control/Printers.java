package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Printers extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printers);
    }

    public void printer1(View view) {
        Intent i = new Intent(this, Printer1.class );
        startActivity(i);
    }

    public void printer2(View view) {
        Intent i = new Intent(this, Printer2.class );
        startActivity(i);
    }

    public void consultar(View view) {
        Intent i = new Intent(this, Consulta.class );
        startActivity(i);
    }

}
