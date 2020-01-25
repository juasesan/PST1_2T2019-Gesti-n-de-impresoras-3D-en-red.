package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Printers extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printers);
    }

    public void printer1(View view) {
        Intent i = new Intent(this, Printer1.class );
        Bundle datos=getIntent().getExtras();
        String correo=datos.getString("Correo");
        i.putExtra("Correo",correo);
        startActivity(i);
    }

    public void printer2(View view) {
        Intent i = new Intent(this, Printer2.class );
        startActivity(i);
    }

}
