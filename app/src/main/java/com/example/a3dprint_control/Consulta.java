package com.example.a3dprint_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Consulta extends AppCompatActivity implements View.OnClickListener {
    String nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        mostrarResult();


    }

    public void mostrarResult(){
        try {
            Bundle bl = getIntent().getExtras();
            String response = bl.getString("jsonresponse");
            JSONObject jsonresult = new JSONObject(response);
            JSONArray array = (JSONArray) jsonresult.get("files");
            final LinearLayout linearLayout = new LinearLayout(this);
            setContentView(linearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            for(int i =0; i<array.length();i++){
                final JSONObject jsonobj = (JSONObject) array.get(i);
                TextView textView = new TextView(this);
                final Button bt_ptint = new Button(this);
                bt_ptint.setText("Imprimir");
                bt_ptint.setOnClickListener(this);
                textView.setText(jsonobj.get("name").toString());
                linearLayout.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.addView(bt_ptint);
                        try{
                            nombre = jsonobj.get("name").toString();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,Impresion.class);
        i.putExtra("name",nombre);
        startActivity(i);
    }
}
