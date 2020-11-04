package tsti.dam.ejemplociclovida;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String TAG_APP = "APP_CICLO_VIDA";
    public static final Integer MAX_VAL =  20;
    Button btnResta;
    Button btnSuma;
    Button btnReiniciar;
    Button btnIrAct02;
    Button btnIniciarProceso, btnIniciarProceso2, btnIniciarProceso3;

    TextView resultado, tvInfoProceso;
    EditText valor;
    Integer valorActual;

    Handler miHandler;

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
        btnIniciarProceso = findViewById(R.id.btnIniciarProceso);
        btnIniciarProceso2= findViewById(R.id.btnIniciarProceso2);
        btnIniciarProceso3 = findViewById(R.id.btnIniciarProceso3);
        tvInfoProceso = findViewById(R.id.tvInfoProceso);
        valor.setText("1");
        resultado.setText(""+valorActual);

        miHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1: // marca el inicio
                        tvInfoProceso.setText("Proceso iniciado");
                        tvInfoProceso.setTextColor(Color.RED);
                        break;
                    case 2: // marca el avacnce que inicio el procesamiento de un elmeento
                        // en arg1 tengo el valor actual
                        // en arg2 tengo el indice del valor actual
                        // en obj tengo el total de elementos
                        int actual = msg.arg1;
                        int indiceActual = msg.arg2;
                        String totalELementos = (String) msg.obj;
                        tvInfoProceso.setText("Procesando elemento" +actual+" ( "+indiceActual+ " de "+ totalELementos+")");
                        tvInfoProceso.setTextColor(Color.BLUE);
                    case 3: // FIN
                        tvInfoProceso.setText("Proceso FINALIZADO");
                        tvInfoProceso.setTextColor(Color.GREEN);
                    case 4: // finalizo un elmeento
                        int actual2 = msg.arg1;
                        int indiceActual2 = msg.arg2;
                        String totalELementos2 = (String) msg.obj;
                        tvInfoProceso.setText("Termino de procesar elemento" +actual2+" ( "+indiceActual2+ " de "+ totalELementos2+ ")");
                        tvInfoProceso.setTextColor(Color.CYAN);
                }
            }
        };

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


        // sin usar handler
        btnIniciarProceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInfoProceso.setText("Arranca proceso....");
                int[] miArreglo = new int[100];
                Random r = new Random();
                for(int x =0;x<miArreglo.length;x++){
                    miArreglo[x] = r.nextInt();
                }
                int i= 0;

                for(long unValor: miArreglo){
                    tvInfoProceso.setText("procesando..."+unValor);
                    try {
                        Log.d(TAG_APP, "procesar: "+unValor+ " ( "+i+" de ");

                        Thread.currentThread().sleep(100);
                        tvInfoProceso.setText("termino de procesar..."+unValor+ " ( "+i+" de 100)");
                        Thread.currentThread().sleep(30);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tvInfoProceso.setText("FIN...");
            }
        });

        // USANDO HANDLER

        // sin usar handler
        btnIniciarProceso2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInfoProceso.setText("Preparando para arrancar");
                // crear el hilo secundario
                Runnable miHiloSecundario = new Runnable() {
                    @Override
                    public void run() {
                        int[] miArreglo = new int[100];
                        Random r = new Random();
                        for(int x =0;x<miArreglo.length;x++){
                            miArreglo[x] = r.nextInt();
                        }
                        int i= 0;
                        // enviamos un mensaje para actualizar la pantalla que estamos arrancando
                        Message miMensajeArranque = miHandler.obtainMessage();
                        miMensajeArranque.what = 1;
                        miHandler.sendMessage(miMensajeArranque);
                        for(int unValor: miArreglo){
                            // enviamos un mensaje para actualizar la pantalla que estamos arrancando
                            Message mensajeProcesamiento = miHandler.obtainMessage();
                            mensajeProcesamiento.what = 2;
                            mensajeProcesamiento.arg1 = unValor;
                            mensajeProcesamiento.arg2 = i;
                            mensajeProcesamiento.obj = "100";
                            miHandler.sendMessage(mensajeProcesamiento);
                            /// tvInfoProceso.setText("procesando..."+unValor);
                            try {
                                Log.d(TAG_APP, "procesar: "+unValor+ " ( "+i+" de ");

                                Thread.currentThread().sleep(100);
                                Message mensajeProcesamiento2 = miHandler.obtainMessage();
                                mensajeProcesamiento2.what = 4;
                                mensajeProcesamiento2.arg1 = unValor;
                                mensajeProcesamiento2.arg2 = i;
                                mensajeProcesamiento2.obj = "100";
                                miHandler.sendMessage(mensajeProcesamiento2);
                                //vInfoProceso.setText("termino de procesar..."+unValor+ " ( "+i+" de 100)");
                                Thread.currentThread().sleep(30);
                                i++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Message mensajeFin = miHandler.obtainMessage();
                        mensajeFin.what = 3;
                        miHandler.sendMessage(mensajeFin);
 //                       tvInfoProceso.setText("FIN...");
                    }
                };
                Thread miThreadSecundario = new Thread(miHiloSecundario);
                miThreadSecundario.start();
            }
        });

        btnIniciarProceso3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer[] miArreglo = new Integer[100];
                Random r = new Random();
                for (int x = 0; x < miArreglo.length; x++) {
                    miArreglo[x] = r.nextInt();
                };
                MiTareaAsincroncia miTareaAsincroncia = new MiTareaAsincroncia();
                miTareaAsincroncia.execute(miArreglo);
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

    class MiTareaAsincroncia extends AsyncTask<Integer,String,Integer> {
        @Override
        protected Integer doInBackground(Integer... valores) {

            int i = 0;
            for (int unValor : valores) {
                /// tvInfoProceso.setText("procesando..."+unValor);
                try {
                    Log.d(TAG_APP, "procesar: " + unValor + " ( " + i + " de ");
                    publishProgress(""+unValor,""+i,"100");

                    Thread.currentThread().sleep(100);
                    //vInfoProceso.setText("termino de procesar..."+unValor+ " ( "+i+" de 100)");
                    Thread.currentThread().sleep(30);
                    i++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return i;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfoProceso.setText("Proceso iniciado ASYNC");
            tvInfoProceso.setTextColor(Color.RED);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            tvInfoProceso.setText("Proceso FINALIZADO ASYNC");
            tvInfoProceso.setTextColor(Color.GREEN);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvInfoProceso.setText("Procesando ASYNC elemento" +values[0]+" ( "+values[1]+ " de "+ values[2]+")");
            tvInfoProceso.setTextColor(Color.BLUE);
        }
    };
}