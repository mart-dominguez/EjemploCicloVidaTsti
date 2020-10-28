package tsti.dam.ejemplociclovida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class Pantalla02Activity extends AppCompatActivity {

    Button btnAceptar, btnRandom;
    TextView tv1, tv2;
    Random r = new Random();

    Integer v1, v2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla02);
        tv1 = findViewById(R.id.texto1);
        tv2 = findViewById(R.id.texto2);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnRandom = findViewById(R.id.btnGenerarAleatorio);

        Bundle extrasParams = getIntent().getExtras();
        if(extrasParams != null){
            String param1 = extrasParams.getString("PARAM1","  - vacio -");
            Integer val2 = extrasParams.getInt("PARAM2", -99);
            tv1.setText(param1);
            tv2.setText(" --> "+val2);
        } else {
            tv1.setText("SIN EXTRAS ");
            tv2.setText("SIN EXTRAS ");
        }

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1 = r.nextInt();
                v2 = v1 * r.nextInt();
                tv1.setText("RANDOM GENERADO: ["+v1+" ; "+v2+"]");
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultado = new Intent();
                resultado.putExtra("rand1",v1);
                resultado.putExtra("rand2",v2);
                setResult(RESULT_OK,resultado);
                finish();
            }
        });
    }
}