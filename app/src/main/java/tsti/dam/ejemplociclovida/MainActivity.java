package tsti.dam.ejemplociclovida;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {
    public static final String TAG_APP = "APP_CICLO_VIDA";
    public static final Integer MAX_VAL =  20;
    Button btnResta;
    Button btnSuma;
    Button btnReiniciar;
    Button btnIrAct02;

    TextView resultado;
    EditText valor;
    Integer valorActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_APP, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        valorActual = 1;
        btnResta = findViewById(R.id.buttonResta);
        btnSuma = findViewById(R.id.buttonSuma);
        btnReiniciar = findViewById(R.id.buttonReiniciar);
        btnIrAct02 = findViewById(R.id.btnIrAct02);
        resultado = findViewById(R.id.resultado);
        valor = findViewById(R.id.ingresoValor);
        valor.setText("1");
        resultado.setText(""+valorActual);

        btnResta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer valorIngresado = Integer.valueOf(valor.getText().toString());
                if(valorActual - valorIngresado> (-1* MAX_VAL)){
                    valorActual = valorActual - valorIngresado;
                    resultado.setText(""+valorActual);
                } else {
                    btnResta.setEnabled(false);
                    Toast.makeText(MainActivity.this,"SE PASA DE -"+(-1* MAX_VAL),Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer valorIngresado = Integer.valueOf(valor.getText().toString());
                if(valorActual + valorIngresado<MAX_VAL){
                    valorActual = valorActual + valorIngresado;
                    resultado.setText(""+valorActual);
                } else {
                    btnSuma.setEnabled(false);
                    Toast.makeText(MainActivity.this,"SE PASA DE -"+MAX_VAL,Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSuma.setEnabled(true);
                btnResta.setEnabled(true);
                valorActual =1;
                valor.setText("1");
                resultado.setText(""+valorActual);
            }
        });

        btnIrAct02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this, Pantalla02Activity.class);
                i1.putExtra("PARAM2",valorActual);
                startActivityForResult(i1,1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1000){
            if(resultCode==RESULT_OK){
                Integer v1Res = data.getExtras().getInt("rand1");
                Integer v2Res = data.getExtras().getInt("rand2");
                resultado.setText("OBTUVE de ACT2: "+v1Res+  " : "+ v2Res);
            }
        }
    }

    @Override
    protected void onRestart() {
        Log.d(TAG_APP, "RESTART: ");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG_APP, "START : ");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG_APP, "PAUSA: ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG_APP, "RESUME: ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG_APP, "DESTRUIR: ");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG_APP, "STOP: ");
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG_APP, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
        valorActual = savedInstanceState.getInt("VALOR_CONTADOR");
        resultado.setText(""+valorActual);
        btnResta.setEnabled(savedInstanceState.getBoolean("BTN_RESTAR_HABILITADO"));
        btnSuma.setEnabled(savedInstanceState.getBoolean("BTN_SUMAR_HABILITADO"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG_APP, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putInt("VALOR_CONTADOR",valorActual);
        outState.putBoolean("BTN_SUMAR_HABILITADO",btnSuma.isEnabled());
        outState.putBoolean("BTN_RESTAR_HABILITADO",btnResta.isEnabled());
    }
}