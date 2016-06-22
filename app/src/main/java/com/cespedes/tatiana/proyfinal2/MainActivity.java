package com.cespedes.tatiana.proyfinal2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {



    ImageView Protoboard, componentes, arriba;
    ImageView leds11, leds12, leds13, leds14, leds15, leds21, leds22, leds23, leds24, leds25, leds31, leds32, leds33, leds34, leds35;
    TextView textView;
    Button boton, bOnOff, bCableComp;
    LinearLayout linerLa;

 //   ScaleGestureDetector SGD;


    matrices[][] ABCDE, FGHIJ, GND, VCC;
    matrices[] leds, sieteseg1, vcc1, gnd1 ;
    matrices[][] switchs, relojs;

    matrices[][] mcompuerta;

    cables cables[];
    int nCables=0;

    int borrar=0, cablearOcompuertas=0, OnOff=0;

    double x_compuerta=0;

    Bitmap bitmap;
    Canvas canvas;
    ArrayList<matrices> recorrido;
    Paint paint;
    StringBuilder stringBuilder = new StringBuilder();

    float downx = 0, downy = 0;
    float upx = 0, upy = 0;
    double minx = 1200, miny = 1200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Protoboard = (ImageView)findViewById(R.id.protoboard);
        componentes = (ImageView)findViewById(R.id.Icompuerta);
        arriba = (ImageView)findViewById(R.id.Iarriba);
        linerLa = (LinearLayout) findViewById(R.id.Linear1);

        boton = (Button) findViewById(R.id.boton);
        bOnOff = (Button) findViewById(R.id.bOnOff);
        bCableComp = (Button) findViewById(R.id.bCabComp);
        textView = (TextView)findViewById(R.id.texto);
        textView.setText("X: ,Y: ");
        ABCDE = new matrices[63][5];
        FGHIJ = new matrices[63][5];
        VCC = new matrices[50][2];
        GND = new matrices[50][2];
        leds = new matrices[15];
        sieteseg1 = new  matrices[21];
        vcc1 = new  matrices[5];
        gnd1 = new  matrices[5];
        switchs = new matrices[15][3];
        relojs = new matrices[2][3];

        cables = new cables[1000];
        recorrido = new ArrayList<>();

        inicializar_matrices();
        bitmap = Bitmap.createBitmap(1050, 435, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6.0F);

        Protoboard.setBackgroundResource(R.drawable.protoboard);
        Protoboard.setImageBitmap(this.bitmap);

        mcompuerta = new matrices[7][2];

       // SGD = new ScaleGestureDetector(this, new ScaleListener());

        for(int i=0; i<7; i++){
            mcompuerta[i][0] = new matrices();
            mcompuerta[i][1] = new matrices();
        }
        leds11= (ImageView) findViewById(R.id.led11);
        leds12= (ImageView) findViewById(R.id.led12);
        leds13= (ImageView) findViewById(R.id.led13);
        leds14= (ImageView) findViewById(R.id.led14);
        leds15= (ImageView) findViewById(R.id.led15);
        leds21= (ImageView) findViewById(R.id.led21);
        leds22= (ImageView) findViewById(R.id.led22);
        leds23= (ImageView) findViewById(R.id.led23);
        leds24= (ImageView) findViewById(R.id.led24);
        leds25= (ImageView) findViewById(R.id.led25);
        leds31= (ImageView) findViewById(R.id.led31);
        leds32= (ImageView) findViewById(R.id.led32);
        leds33= (ImageView) findViewById(R.id.led33);
        leds34= (ImageView) findViewById(R.id.led34);
        leds35= (ImageView) findViewById(R.id.led35);



        final CustomHorizontalScrollView SVhorizontal = (CustomHorizontalScrollView) findViewById(R.id.SVhorizontal);
        final CustomScrollView SVvertical = (CustomScrollView) findViewById(R.id.SVvertical);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        ActionBar.TabListener tabListener= new ActionBar.TabListener(){

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        ActionBar.Tab tab= actionBar.newTab().setTabListener(tabListener).setText("Archivo");
        actionBar.addTab(tab);

        tab= actionBar.newTab().setTabListener(tabListener).setText("Cable");
        actionBar.addTab(tab);

        tab = actionBar.newTab().setTabListener(tabListener).setText("Circuitos");
        actionBar.addTab(tab);


        Protoboard.setOnTouchListener(new View.OnTouchListener() {
            Path path = new Path();
            long tiempo, tiempo2, tiempo3;
            matrices punto1, punto2;
            int nCable2 = 0;
            matrices comp1, comp2;

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

             //   SGD.onTouchEvent(arg1);
                stringBuilder.setLength(0);

                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {

                    tiempo = arg1.getEventTime();
                    downx = arg1.getX();
                    downy = arg1.getY();
                    path.moveTo(downx, downy);
                    punto1 = new matrices();
                    punto1 = distancia_minima(downx, downy);
                    Toast.makeText(getApplicationContext(), "Conexiones:  " + punto1.getConexion(), Toast.LENGTH_SHORT).show();

                    if (punto1.estado == 1 && borrar == 1) {
                        borrarCable(punto1.nCable);
                        punto2 = new matrices();
                        punto2 = buscarOtroExtremo(punto1);
                        punto1.estado = 0;
                        punto1.nCable = 10000;
                        cambiarestado(punto1);
                        punto2.estado = 0;
                        punto2.nCable = 10000;
                        cambiarestado(punto2);
                    }


                }

                if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    tiempo2 = (arg1.getEventTime() - tiempo);
                    tiempo3 = (arg1.getEventTime() - tiempo);
                    if (tiempo2 > 1000) {

                        upx = arg1.getX();
                        upy = arg1.getY();
                        if (cablearOcompuertas == 0) {
                            path.lineTo(upx, upy);
                            canvas.drawPath(path, paint);
                            punto2 = new matrices();
                            punto2.setCoordenada_x(upx);
                            punto2.setCoordenada_y(upy);
                            recorrido.add(punto2);
                            Protoboard.invalidate();

                        }
                        SVhorizontal.setEnableScrolling(false);
                        SVvertical.setEnableScrolling(false);
                    }
                    if (tiempo3 > 200 && cablearOcompuertas == 1) {
                        componentes.setTranslationX(upx - 990);
                        componentes.setTranslationY(upy - 400);
                        Protoboard.invalidate();
                        SVhorizontal.setEnableScrolling(false);
                        SVvertical.setEnableScrolling(false);

                    }

                    stringBuilder.append("Moviendo, X:" + arg1.getX() + ", Y:" + arg1.getY());

                } else {
                    stringBuilder.append("Detenido, X:" + arg1.getX() + ", Y:" + arg1.getY());
                }
                if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    SVhorizontal.setEnableScrolling(true);
                    SVvertical.setEnableScrolling(true);
                    path = new Path();
                    if (tiempo2 > 1000 && cablearOcompuertas == 0) {
                        cables[nCables] = new cables(recorrido);
                        recorrido.clear();
                        cables[nCables].pi = distancia_minima(downx, downy);
                        canvas.drawLine((float) cables[nCables].pi.getCoordenada_x(), (float) cables[nCables].pi.getCoordenada_y(), downx, downy, paint);
                        cables[nCables].pf = distancia_minima(upx, upy);
                        canvas.drawLine((float) cables[nCables].pf.getCoordenada_x(), (float) cables[nCables].pf.getCoordenada_y(), upx, upy, paint);
                        cables[nCables].numero = nCables;

                        if (cables[nCables].pi.estado == 0 && cables[nCables].pf.estado == 0) {
                            cables[nCables].pi.estado = 1;
                            cables[nCables].pf.estado = 1;
                            cables[nCables].pi.nCable = nCables;
                            cables[nCables].pf.nCable = nCables;
                            cambiarestado(cables[nCables].pi);
                            cambiarestado(cables[nCables].pf);

                        } else {
                            borrarCable(nCables);
                            Toast.makeText(getApplicationContext(), "Ya está en uso", Toast.LENGTH_SHORT).show();
                            nCable2 = buscarCable(cables[nCables].pi);
                            redibujar(nCable2);

                        }
                        Protoboard.invalidate();  //Refresca el ImageView
                        nCables++;
                    }


                    if (tiempo3 > 200 && cablearOcompuertas == 1) {
                        componentes.setTranslationY(-203);
                        Protoboard.invalidate();
                        x_compuerta = upx;
                        comp1 = new matrices();
                        comp2 = new matrices();
                        for (int i = 0; i < 7; i++) {
                            mcompuerta[i][0] = new matrices();
                            mcompuerta[i][1] = new matrices();
                            mcompuerta[i][0].setCoordenada_x(x_compuerta - 56);
                            mcompuerta[i][0].setCoordenada_y(180);
                            mcompuerta[i][1].setCoordenada_x(x_compuerta - 56);
                            mcompuerta[i][1].setCoordenada_y(227);

                            comp1 = distancia_minima(mcompuerta[i][0].getCoordenada_x(), mcompuerta[i][0].getCoordenada_y());
                            comp2 = distancia_minima(mcompuerta[i][1].getCoordenada_x(), mcompuerta[i][1].getCoordenada_y());
                            comp1.setEstado(1);
                            comp2.setEstado(1);
                            cambiarestado(comp1);
                            cambiarestado(comp2);
                            mcompuerta[i][0].setConexion(comp1.getConexion());
                            mcompuerta[i][1].setConexion(comp2.getConexion());
                            //     canvas.drawPoint((float) x_compuerta - 56, 180, paint);
                            //    canvas.drawPoint((float) x_compuerta -56, 227, paint);
                            x_compuerta = x_compuerta + 16.17;
                        }
                    }

                    tiempo2 = 0;
                }
                //Se muestra en pantalla las coordenadas
                textView.setText(stringBuilder.toString());
                return true;
            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (borrar == 0) {
                    borrar = 1;
                    boton.setText("Desactivar Borrar");
                } else {
                    borrar = 0;
                    boton.setText("Activar Borrar");
                }
            }
        });

        bOnOff.setOnClickListener(new View.OnClickListener(){

            matrices newmatrices = new matrices();

            @Override
            public void onClick(View v) {


                if(OnOff==0){
                    OnOff=1;
                    bOnOff.setText("Off");
                    cambiarconexiones();
                    if(mcompuerta[0][0].getConexion()==1 && mcompuerta[6][1].getConexion()==0) {

                        if(mcompuerta[1][0].getConexion() ==1  && mcompuerta[2][0].getConexion()==1){

                            mcompuerta[3][0].setConexion(1);
                            newmatrices = distancia_minima(mcompuerta[3][0].getCoordenada_x(), mcompuerta[3][0].getCoordenada_y());

                            Toast.makeText(getApplicationContext(), "Conex  " + newmatrices.getConexion(), Toast.LENGTH_SHORT).show();
                            newmatrices.setConexion(1);
                            Toast.makeText(getApplicationContext(), "Conex2  " + newmatrices.getConexion()  , Toast.LENGTH_SHORT).show();
                            cambiarestado(newmatrices);
                        }
                        if(mcompuerta[4][0].getConexion() ==1  && mcompuerta[5][0].getConexion()==1){
                            mcompuerta[6][0].setConexion(1);
                            newmatrices = distancia_minima(mcompuerta[6][0].getCoordenada_x(), mcompuerta[6][0].getCoordenada_y());
                            newmatrices.setConexion(1);
                            cambiarestado(newmatrices);

                        }
                        if(mcompuerta[0][1].getConexion() ==1  && mcompuerta[1][1].getConexion()==1){
                            mcompuerta[2][1].setConexion(1);
                            newmatrices = distancia_minima(mcompuerta[2][1].getCoordenada_x(), mcompuerta[2][1].getCoordenada_y());
                            newmatrices.setConexion(1);
                            cambiarestado(newmatrices);

                        }
                        if(mcompuerta[3][1].getConexion() ==1  && mcompuerta[4][1].getConexion()==1){
                            mcompuerta[5][1].setConexion(1);
                            newmatrices = distancia_minima(mcompuerta[5][1].getCoordenada_x(), mcompuerta[5][1].getCoordenada_y());
                            newmatrices.setConexion(1);
                            cambiarestado(newmatrices);

                        }

                        cambiarconexionespropias();
                        cambiarconexiones();
                    }else{
                        Toast.makeText(getApplicationContext(), "Compuerta no polarizada  " , Toast.LENGTH_SHORT).show();
                    }


                    if(leds[0].getConexion()==1){
                        leds11.setVisibility(View.VISIBLE);
                    }else{
                        leds11.setVisibility(View.INVISIBLE);
                    }

                    if(leds[1].getConexion()==1){
                        leds12.setVisibility(View.VISIBLE);
                    }else{
                        leds12.setVisibility(View.INVISIBLE);
                    }
                    if(leds[2].getConexion()==1){
                        leds13.setVisibility(View.VISIBLE);
                    }else{
                        leds13.setVisibility(View.INVISIBLE);

                    }
                    if(leds[3].getConexion()==1){
                        leds14.setVisibility(View.VISIBLE);
                    }else{
                        leds14.setVisibility(View.INVISIBLE);

                    }
                    if(leds[4].getConexion()==1){
                        leds15.setVisibility(View.VISIBLE);
                    }else{
                        leds15.setVisibility(View.INVISIBLE);

                    }
                    if(leds[5].getConexion()==1){
                        leds21.setVisibility(View.VISIBLE);
                    }else{
                        leds21.setVisibility(View.INVISIBLE);

                    }
                    if(leds[6].getConexion()==1){
                        leds22.setVisibility(View.VISIBLE);
                    }else{
                        leds22.setVisibility(View.INVISIBLE);

                    }
                    if(leds[7].getConexion()==1){
                        leds23.setVisibility(View.VISIBLE);
                    }else{
                        leds23.setVisibility(View.INVISIBLE);

                    }
                    if(leds[8].getConexion()==1){
                        leds24.setVisibility(View.VISIBLE);
                    }else{
                        leds24.setVisibility(View.INVISIBLE);

                    }
                    if(leds[9].getConexion()==1){
                        leds25.setVisibility(View.VISIBLE);
                    }else{
                        leds25.setVisibility(View.INVISIBLE);

                    }
                    if(leds[10].getConexion()==1){
                        leds31.setVisibility(View.VISIBLE);
                    }else{
                        leds31.setVisibility(View.INVISIBLE);

                    }
                    if(leds[11].getConexion()==1){
                        leds32.setVisibility(View.VISIBLE);
                    }else{
                        leds32.setVisibility(View.INVISIBLE);

                    }
                    if(leds[12].getConexion()==1){
                        leds33.setVisibility(View.VISIBLE);
                    }else{
                        leds33.setVisibility(View.INVISIBLE);

                    }
                    if(leds[13].getConexion()==1){
                        leds34.setVisibility(View.VISIBLE);
                    }else{
                        leds34.setVisibility(View.INVISIBLE);

                    }
                    if(leds[14].getConexion()==1){
                        leds35.setVisibility(View.VISIBLE);
                    }else{
                        leds35.setVisibility(View.INVISIBLE);

                    }
                    Protoboard.invalidate();

                }else{

                    leds11.setVisibility(View.INVISIBLE);
                    leds12.setVisibility(View.INVISIBLE);
                    leds13.setVisibility(View.INVISIBLE);
                    leds14.setVisibility(View.INVISIBLE);
                    leds15.setVisibility(View.INVISIBLE);
                    leds21.setVisibility(View.INVISIBLE);
                    leds22.setVisibility(View.INVISIBLE);
                    leds23.setVisibility(View.INVISIBLE);
                    leds24.setVisibility(View.INVISIBLE);
                    leds25.setVisibility(View.INVISIBLE);
                    leds31.setVisibility(View.INVISIBLE);
                    leds32.setVisibility(View.INVISIBLE);
                    leds33.setVisibility(View.INVISIBLE);
                    leds34.setVisibility(View.INVISIBLE);
                    leds35.setVisibility(View.INVISIBLE);

                    OnOff=0;
                    bOnOff.setText("On");
                    Protoboard.invalidate();
                }
                Protoboard.invalidate();
            }
        });

        bCableComp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(cablearOcompuertas==0){
                    cablearOcompuertas=1;
                    bCableComp.setText("Cablear");
                    Protoboard.invalidate();

                }else{
                    cablearOcompuertas=0;
                    bOnOff.setText("Compuertas");
                }

            }
        });

    }



    public void borrarCable(int n){
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        clearPaint.setStyle(Paint.Style.STROKE);
        clearPaint.setStrokeWidth(10.0F);

        Path path1 = new Path();
        int l = cables[n].recorrido1.length;
        path1.moveTo((float) cables[n].recorrido1[0].getCoordenada_x(), (float) cables[n].recorrido1[0].getCoordenada_y());
        for(int i= 1; i<l; i++) {
            path1.lineTo((float) cables[n].recorrido1[i].getCoordenada_x(), (float) cables[n].recorrido1[i].getCoordenada_y());
            canvas.drawPath(path1, clearPaint);
            Protoboard.invalidate();
        }
        canvas.drawLine((float)cables[n].pi.getCoordenada_x(), (float)cables[n].pi.getCoordenada_y(),(float) cables[n].recorrido1[0].getCoordenada_x(), (float) cables[n].recorrido1[0].getCoordenada_y(), clearPaint);
        canvas.drawLine((float)cables[n].recorrido1[l-1].getCoordenada_x(), (float)cables[n].recorrido1[l-1].getCoordenada_y(), (float)cables[n].pf.getCoordenada_x(), (float)cables[n].pf.getCoordenada_y(), clearPaint);
        Protoboard.invalidate();
    }

    public void redibujar(int n){
        Path path1 = new Path();
        int l = cables[n].recorrido1.length;
        path1.moveTo((float) cables[n].recorrido1[0].getCoordenada_x(), (float) cables[n].recorrido1[0].getCoordenada_y());
        for(int i= 1; i<l; i++) {
            path1.lineTo((float) cables[n].recorrido1[i].getCoordenada_x(), (float) cables[n].recorrido1[i].getCoordenada_y());
            canvas.drawPath(path1, paint);
            Protoboard.invalidate();
        }
        canvas.drawLine((float)cables[n].pi.getCoordenada_x(), (float)cables[n].pi.getCoordenada_y(),(float) cables[n].recorrido1[0].getCoordenada_x(), (float) cables[n].recorrido1[0].getCoordenada_y(), paint);
        canvas.drawLine((float)cables[n].recorrido1[l-1].getCoordenada_x(), (float)cables[n].recorrido1[l-1].getCoordenada_y(), (float)cables[n].pf.getCoordenada_x(), (float)cables[n].pf.getCoordenada_y(), paint);
        Protoboard.invalidate();
    }

    public void inicializar_matrices() {
        double distancia= 16.17, distanciay=300;//294.5;
        double disx = 23.97, disy1=229 , disy2=112;//194.9 80.9;
        double disy3 = disx + (distancia*2);
        double disy4= 44.5;
        double disy5 = disy4 + distancia;
        double suma = disy3;

        double disledsy =5, dissietex = 702;

        for (int i = 0; i < 63; i++) {
            for (int m = 0; m < 5; m++) {
                ABCDE[i][m] = new matrices();
                ABCDE[i][m].setCoordenada_x(disx + (distancia * i));
                ABCDE[i][m].setCoordenada_y(disy1 + (distancia * m));
                ABCDE[i][m].setConexion(i + 2);
                ABCDE[i][m].setConexionant(i);
                ABCDE[i][m].setnHuecos(5);
                FGHIJ[i][m] = new matrices();
                FGHIJ[i][m].setCoordenada_x(disx + (distancia * i));
                FGHIJ[i][m].setCoordenada_y(disy2 + (distancia * m));
                FGHIJ[i][m].setConexion(i + 65);
                FGHIJ[i][m].setConexionant(i + 65);
                FGHIJ[i][m].setnHuecos(5);
            }
        }
        for(int j = 0; j < 50; j++) {
            for (int k = 0; k < 2; k++) {
                VCC[j][k] = new matrices();
                VCC[j][k].setCoordenada_x(suma);
                VCC[j][k].setCoordenada_y(disy5 + distanciay * k);
                VCC[j][k].setConexion(k + 128);
                VCC[j][k].setConexionant(k + 128);
                VCC[j][k].setnHuecos(50);
                GND[j][k] = new matrices();
                GND[j][k].setCoordenada_x(suma);
                GND[j][k].setCoordenada_y(disy4 + distanciay * k);
                GND[j][k].setConexion(k + 130);
                GND[j][k].setConexionant(k + 130);
                GND[j][k].setnHuecos(50);
            }
            if ((j + 1) % 5 == 0) {
                suma += 2 * distancia;
            } else {
                suma += distancia;
            }
        }

        suma = disy3;

        for(int i=0; i<15; i++){
            leds[i] = new matrices();
            leds[i].setCoordenada_x(suma);
            leds[i].setCoordenada_y(disledsy);
            leds[i].setConexion(i + 132);
            leds[i].setnHuecos(1);
            if ((i + 1) % 5 == 0) {
                suma += 4 * distancia;
            } else {
                suma += 2* distancia;
            }

            for(int j=0; j<3; j++){
                switchs[i][j] = new matrices();
                switchs[i][j].setCoordenada_x(disx + (distancia * i * 2) + (distancia * 14));
                switchs[i][j].setCoordenada_y(390 + (distancia * j));
                switchs[i][j].setConexion(i+147);
                switchs[i][j].setnHuecos(3);
                if(i<2){
                    relojs[i][j] = new matrices();
                    relojs[i][j].setCoordenada_x(disx + (distancia*i*3)+ (distancia*50));
                    relojs[i][j].setCoordenada_y(390 + (distancia * j));
                    relojs[i][j].setConexion(i + 162);
                    relojs[i][j].setnHuecos(3);
                }
            }
        }

        for(int i=0; i<21; i++){
            sieteseg1[i] = new matrices();
            sieteseg1[i].setCoordenada_x(dissietex + (distancia * i));
            sieteseg1[i].setCoordenada_y(disledsy);
            sieteseg1[i].setConexion(i + 164);
            sieteseg1[i].setnHuecos(1);
            if(i<5){
                vcc1[i] = new matrices();
                vcc1[i].setCoordenada_x(disx + (distancia * i));
                vcc1[i].setCoordenada_y(395);
                vcc1[i].setConexion(1);
                vcc1[i].setnHuecos(5);
                gnd1[i] = new matrices();
                gnd1[i].setCoordenada_x(117 + (distancia * i));
                gnd1[i].setCoordenada_y(395);
                gnd1[i].setConexion(0);
                gnd1[i].setnHuecos(5);
            }
        }


    }

    public matrices distancia_minima(double x1, double y1) {
        minx=1200;
        miny=1200;
        double minx1=1200, miny1=1200, miny2=1200, miny3=1200, miny4=1200,minx3=1200, minx4=1200;
        double minyled=1200, minxled=1200, minx1led=1200;
        double minysiete1=1200, minxsiete=1200, minxsiete4=1200;
        double minyvcc=1200, minxgnd=1200, minygnd=1200, minxgnd2=1200;
        double minxswt=1200, minyswt=1200, minxswt1=1200;
        double minxreloj=1200, minyreloj=1200, minxreloj1=1200;


        int i = 0, j = 0, k = 0, l=0, l1=0, s=0, g=0, w=0,r=0;
        int m, n;
        matrices actual;


        for (m = 0; m < 63; m++) {
            for (n = 0; n < 5; n++) {
                minx1 = Math.abs(ABCDE[m][n].getCoordenada_x() - x1);
                miny1 = Math.abs(ABCDE[m][n].getCoordenada_y() - y1);
                miny2 = Math.abs(FGHIJ[m][n].getCoordenada_y() - y1);
                if(m<50 && n<2) {
                    miny3 = Math.abs(VCC[m][n].getCoordenada_y() - y1);
                    miny4 = Math.abs(GND[m][n].getCoordenada_y() - y1);
                    minx3 = Math.abs(VCC[m][n].getCoordenada_x() - x1);
                    if(minx3 < minx4){
                        minx4= minx3;
                        l = m;
                    }

                    if(m<21 && n<1){
                        minysiete1 = Math.abs(sieteseg1[m].getCoordenada_y() - y1);
                        minxsiete  = Math.abs(sieteseg1[m].getCoordenada_x() - x1);
                        if(minxsiete < minxsiete4){
                            minxsiete4=minxsiete;
                            if(minx1led>minxsiete4) {
                                s = m;
                            }
                        }
                    }

                    if(m<15 && n<1){
                        minyled = Math.abs(leds[m].getCoordenada_y() - y1);
                        minxled = Math.abs(leds[m].getCoordenada_x() - x1);
                        if(minxled < minx1led){
                            minx1led = minxled;
                            l1=m;
                        }
                        if(m<5){
                            minyvcc = Math.abs(vcc1[m].getCoordenada_y()-y1);
                            minygnd = Math.abs(gnd1[m].getCoordenada_y()-y1);
                            minxgnd = Math.abs(gnd1[m].getCoordenada_x()-x1);
                            if(minxgnd<minxgnd2){
                                minxgnd2=minxgnd;
                                g=m;
                            }
                        }
                    }
                }
                if(m<15 && n<3){
                    minyswt = Math.abs(switchs[m][n].getCoordenada_y()-y1);
                    minxswt = Math.abs(switchs[m][n].getCoordenada_x()-x1);
                    if(minxswt<minxswt1){
                        minxswt1=minxswt;
                        w=m;
                    }
                    if(m<2){
                        minyreloj = Math.abs(relojs[m][n].getCoordenada_y()-y1);
                        minxreloj = Math.abs(relojs[m][n].getCoordenada_x()-x1);
                        if(minxreloj<minxreloj1){
                            minxreloj1= minxreloj;
                            r=m;
                        }
                    }
                }

                if (minx1 < minx) {
                    minx = minx1;
                    j = m;
                }
                if (miny1 < miny) {
                    miny = miny1;
                    i = n;
                    k = 1;
                }
                if (miny2 < miny) {
                    miny = miny2;
                    i = n;
                    k = 2;
                }
                if (miny3 < miny){
                    miny = miny3;
                    i = n;
                    k = 3;
                }
                if (miny4 < miny){
                    miny = miny4;
                    i = n;
                    k = 4;
                }
                if(x1<635) {

                    if (minyled < miny) {
                        miny = minyled;
                        k = 5;
                    }
                }else {
                    if (minysiete1 < miny) {
                        miny = minysiete1;
                        k = 6;
                    }
                }
                if(x1<95) {
                    if (minyvcc < miny) {
                        miny = minyvcc;
                        k = 7;
                    }
                }else if (x1 < 210) {
                    if (minygnd < miny) {
                        miny = minygnd;
                        k = 8;
                    }
                }else if (x1 < 760){
                    if(minyswt < miny){
                        miny = minyswt;
                        k=9;
                        i=n;
                    }
                }else if (x1 < 920){

                    if(minyreloj< miny) {
                        miny = minyreloj;
                        k=10;
                        i=n;
                    }
                }

            }
        }


        switch (k){
            case 1:
                actual = ABCDE[j][i];
                minx = ABCDE[j][i].getCoordenada_x();
                miny = ABCDE[j][i].getCoordenada_y();
                break;
            case 2:
                actual = FGHIJ[j][i];
                minx = FGHIJ[j][i].getCoordenada_x();
                miny = FGHIJ[j][i].getCoordenada_y();
                break;
            case 3:
                actual = VCC[l][i];
                miny = VCC[l][i].getCoordenada_y();
                minx = VCC[l][i].getCoordenada_x();
                break;
            case 4:
                actual = GND[l][i];
                miny = GND[l][i].getCoordenada_y();
                minx = GND[l][i].getCoordenada_x();
                break;
            case 5:
                actual = leds[l1];
                miny = leds[l1].getCoordenada_y();
                minx = leds[l1].getCoordenada_x();
                break;
            case 6:
                actual = sieteseg1[s];
                miny = sieteseg1[s].getCoordenada_y();
                minx = sieteseg1[s].getCoordenada_x();
                break;
            case 7:
                actual = vcc1[j];
                miny = vcc1[j].getCoordenada_y();
                minx = vcc1[j].getCoordenada_x();
                break;
            case 8:
                actual = gnd1[g];
                miny = gnd1[g].getCoordenada_y();
                minx = gnd1[g].getCoordenada_x();
                break;
            case 9:
                actual = switchs[w][i];
                miny = switchs[w][i].getCoordenada_y();
                minx = switchs[w][i].getCoordenada_x();
                break;
            case 10:
                actual = relojs[r][i];
                miny = switchs[r][i].getCoordenada_y();
                minx = switchs[r][i].getCoordenada_x();
                break;
            default:
                actual = FGHIJ[0][0];
                miny = FGHIJ[0][0].getCoordenada_y();
                minx = FGHIJ[0][0].getCoordenada_y();
                break;
        }
        return actual;
    }

    public void cambiarestado(matrices actual){

        double x2 = actual.getCoordenada_x();
        double y2 = actual.getCoordenada_y();

        //   Toast.makeText(getApplicationContext(), "llega  "+ actual.getConexion(), Toast.LENGTH_SHORT).show();
        for (int m = 0; m < 63; m++) {
            for (int n = 0; n < 5; n++) {
                if(ABCDE[m][n].getCoordenada_x()==x2  && ABCDE[m][n].getCoordenada_y() == y2){
                    ABCDE[m][n]=actual;

                    // Toast.makeText(getApplicationContext(), "ABCDE  "+ ABCDE[m][n].getnCable(), Toast.LENGTH_SHORT).show();
                }
                if(FGHIJ[m][n].getCoordenada_x()==x2  && FGHIJ[m][n].getCoordenada_y() == y2){
                    FGHIJ[m][n]=actual;
                    Toast.makeText(getApplicationContext(), "FGHIJ " + FGHIJ[m][n].getConexion(), Toast.LENGTH_SHORT).show();
                }

                if(m<50 && n<2) {

                    if(VCC[m][n].getCoordenada_x()==x2  && VCC[m][n].getCoordenada_y() == y2){
                        VCC[m][n]=actual;
                        //       Toast.makeText(getApplicationContext(), "VCC", Toast.LENGTH_SHORT).show();
                    }
                    if(GND[m][n].getCoordenada_x()==x2  && GND[m][n].getCoordenada_y() == y2){
                        GND[m][n]=actual;
                        //     Toast.makeText(getApplicationContext(), "GND", Toast.LENGTH_SHORT).show();
                    }

                    if(m<21 && n<1){

                        if(sieteseg1[m].getCoordenada_x()==x2  && sieteseg1[m].getCoordenada_y() == y2){
                            sieteseg1[m]=actual;
                            //       Toast.makeText(getApplicationContext(), "siete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(m<15 && n<1){
                        if(leds[m].getCoordenada_x()==x2  && leds[m].getCoordenada_y() == y2){
                            leds[m]=actual;
                            //     Toast.makeText(getApplicationContext(), "leds", Toast.LENGTH_SHORT).show();
                        }
                        if(m<5){
                            if(vcc1[m].getCoordenada_x()==x2  && vcc1[m].getCoordenada_y() == y2){
                                vcc1[m]=actual;
                                //       Toast.makeText(getApplicationContext(), "vcc1", Toast.LENGTH_SHORT).show();
                            }
                            if(gnd1[m].getCoordenada_x()==x2  && gnd1[m].getCoordenada_y() == y2){
                                gnd1[m]=actual;
                                //       Toast.makeText(getApplicationContext(), "gnd1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if(m<15 && n<3){
                    if(switchs[m][n].getCoordenada_x()==x2  && switchs[m][n].getCoordenada_y() == y2){
                        switchs[m][n]=actual;
                        //Toast.makeText(getApplicationContext(), "switchs", Toast.LENGTH_SHORT).show();
                    }
                    if(m<2){
                        if(relojs[m][n].getCoordenada_x()==x2  && relojs[m][n].getCoordenada_y() == y2){
                            relojs[m][n]=actual;
                            //  Toast.makeText(getApplicationContext(), "relojs", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }
        }
    }

    public int buscarCable(matrices actual){

        double x2 = actual.getCoordenada_x();
        double y2 = actual.getCoordenada_y();
        int nCabl=0;

        for (int m = 0; m < 63; m++) {
            for (int n = 0; n < 5; n++) {
                if(ABCDE[m][n].getCoordenada_x()==x2  && ABCDE[m][n].getCoordenada_y() == y2){
                    nCabl= ABCDE[m][n].nCable;
                    //              Toast.makeText(getApplicationContext(), "ABCDE", Toast.LENGTH_SHORT).show();
                }
                if(FGHIJ[m][n].getCoordenada_x()==x2  && FGHIJ[m][n].getCoordenada_y() == y2){
                    nCabl= FGHIJ[m][n].nCable;
                    //                Toast.makeText(getApplicationContext(), "FGHIJ", Toast.LENGTH_SHORT).show();
                }

                if(m<50 && n<2) {

                    if(VCC[m][n].getCoordenada_x()==x2  && VCC[m][n].getCoordenada_y() == y2){
                        nCabl= VCC[m][n].nCable;
                        //                      Toast.makeText(getApplicationContext(), "VCC", Toast.LENGTH_SHORT).show();
                    }
                    if(GND[m][n].getCoordenada_x()==x2  && GND[m][n].getCoordenada_y() == y2){
                        nCabl= GND[m][n].nCable;
//                        Toast.makeText(getApplicationContext(), "GND", Toast.LENGTH_SHORT).show();
                    }

                    if(m<21 && n<1){

                        if(sieteseg1[m].getCoordenada_x()==x2  && sieteseg1[m].getCoordenada_y() == y2){
                            nCabl= sieteseg1[m].nCable;
                            //       Toast.makeText(getApplicationContext(), "siete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(m<15 && n<1){
                        if(leds[m].getCoordenada_x()==x2  && leds[m].getCoordenada_y() == y2){
                            nCabl= leds[m].nCable;
                            //         Toast.makeText(getApplicationContext(), "leds", Toast.LENGTH_SHORT).show();
                        }
                        if(m<5){
                            if(vcc1[m].getCoordenada_x()==x2  && vcc1[m].getCoordenada_y() == y2){
                                nCabl= vcc1[m].nCable;
                                //               Toast.makeText(getApplicationContext(), "vcc1", Toast.LENGTH_SHORT).show();
                            }
                            if(gnd1[m].getCoordenada_x()==x2  && gnd1[m].getCoordenada_y() == y2){
                                nCabl= gnd1[m].nCable;
                                //                 Toast.makeText(getApplicationContext(), "gnd1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if(m<15 && n<3){
                    if(switchs[m][n].getCoordenada_x()==x2  && switchs[m][n].getCoordenada_y() == y2){
                        nCabl= switchs[m][n].nCable;
                        //           Toast.makeText(getApplicationContext(), "switchs", Toast.LENGTH_SHORT).show();
                    }
                    if(m<2){
                        if(relojs[m][n].getCoordenada_x()==x2  && relojs[m][n].getCoordenada_y() == y2){
                            nCabl= relojs[m][n].nCable;
                            //            Toast.makeText(getApplicationContext(), "relojs", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        return nCabl;
    }

    public matrices buscarOtroExtremo( matrices punto1){
        matrices actual = new matrices();

        double x2= punto1.getCoordenada_x();
        double y2= punto1.getCoordenada_y();
        int nCabl= punto1.getnCable();


        for (int m = 0; m < 63; m++) {
            for (int n = 0; n < 5; n++) {
                if (ABCDE[m][n].getnCable() == nCabl) {
                    if (ABCDE[m][n].getCoordenada_x() != x2 || ABCDE[m][n].getCoordenada_y() != y2) {
                        actual = ABCDE[m][n];
                        //                    Toast.makeText(getApplicationContext(), "ABCDE1  " + nCabl, Toast.LENGTH_SHORT).show();
                    }
                }
                if (FGHIJ[m][n].getnCable() == nCabl) {
                    if (FGHIJ[m][n].getCoordenada_x() != x2 || FGHIJ[m][n].getCoordenada_y() != y2) {
                        actual = FGHIJ[m][n];
                        //                  Toast.makeText(getApplicationContext(), "FGHIJ1   "  + nCabl, Toast.LENGTH_SHORT).show();
                    }
                }

                if(m<50 && n<2) {
                    if(VCC[m][n].getnCable()== nCabl) {
                        if (VCC[m][n].getCoordenada_x() != x2 || VCC[m][n].getCoordenada_y() != y2) {
                            actual = VCC[m][n];
                            //                    Toast.makeText(getApplicationContext(), "VCC1  "  + nCabl, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(GND[m][n].getnCable()== nCabl){
                        if (GND[m][n].getCoordenada_x() != x2 || GND[m][n].getCoordenada_y() == y2) {
                            actual = GND[m][n];
                            //                  Toast.makeText(getApplicationContext(), "GND1"  + nCabl, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(m<21 && n<1) {
                        if (sieteseg1[m].getnCable() == nCabl){
                            if (sieteseg1[m].getCoordenada_x() != x2 || sieteseg1[m].getCoordenada_y() != y2) {
                                actual = sieteseg1[m];
                                //                    Toast.makeText(getApplicationContext(), "siete1"  + nCabl, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if(m<15 && n<1){
                        if(leds[m].getnCable()==nCabl) {
                            if (leds[m].getCoordenada_x() != x2 || leds[m].getCoordenada_y() != y2) {
                                actual= leds[m];
//                                Toast.makeText(getApplicationContext(), "leds1", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(m<5){
                            if(vcc1[m].getnCable()== nCabl) {
                                if (vcc1[m].getCoordenada_x() != x2 || vcc1[m].getCoordenada_y() != y2) {
                                    actual=vcc1[m];
                                    //                                  Toast.makeText(getApplicationContext(), "vcc11", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(gnd1[m].getnCable()== nCabl) {
                                if (gnd1[m].getCoordenada_x() != x2 || gnd1[m].getCoordenada_y() != y2) {
                                    actual=gnd1[m];
                                    //                                Toast.makeText(getApplicationContext(), "gnd11", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
                if(m<15 && n<3){
                    if(switchs[m][n].getnCable() == nCabl) {
                        if (switchs[m][n].getCoordenada_x() != x2 || switchs[m][n].getCoordenada_y() != y2) {
                            actual= switchs[m][n];
                            //                      Toast.makeText(getApplicationContext(), "switchs1", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(m<2){
                        if(relojs[m][n].getnCable() == nCabl) {
                            if (relojs[m][n].getCoordenada_x() != x2 || relojs[m][n].getCoordenada_y() != y2) {
                                actual= relojs[m][n];
                                //                        Toast.makeText(getApplicationContext(), "relojs1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

            }

        }
        return actual;
    }

    public matrices buscarConexion(int nConx){

        matrices actual = new matrices();

        for (int m = 0; m < 63; m++) {
            for (int n = 0; n < 5; n++) {
                if (ABCDE[m][n].getConexion() == nConx) {
                    actual = ABCDE[m][n];
                    //                    Toast.makeText(getApplicationContext(), "ABCDE1  " + nCabl, Toast.LENGTH_SHORT).show();
                }
                if (FGHIJ[m][n].getConexion() == nConx) {
                    actual = FGHIJ[m][n];
                    //                  Toast.makeText(getApplicationContext(), "FGHIJ1   "  + nCabl, Toast.LENGTH_SHORT).show();
                }

                if(m<50 && n<2) {
                    if(VCC[m][n].getConexion() == nConx) {
                        actual = VCC[m][n];
                        //                    Toast.makeText(getApplicationContext(), "VCC1  "  + nCabl, Toast.LENGTH_SHORT).show();
                    }
                    if(GND[m][n].getConexion() == nConx){
                        actual = GND[m][n];
                        //                  Toast.makeText(getApplicationContext(), "GND1"  + nCabl, Toast.LENGTH_SHORT).show();
                    }
                    if(m<21 && n<1) {
                        if (sieteseg1[m].getConexion() == nConx){
                            actual = sieteseg1[m];
                            //                    Toast.makeText(getApplicationContext(), "siete1"  + nCabl, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(m<15 && n<1){
                        if(leds[m].getConexion() == nConx) {
                            actual= leds[m];
//                                Toast.makeText(getApplicationContext(), "leds1", Toast.LENGTH_SHORT).show();
                        }
                        if(m<5){
                            if(vcc1[m].getConexion() == nConx) {
                                actual=vcc1[m];
                                //                                  Toast.makeText(getApplicationContext(), "vcc11", Toast.LENGTH_SHORT).show();
                            }
                            if(gnd1[m].getConexion() == nConx) {
                                actual=gnd1[m];
                                //                                Toast.makeText(getApplicationContext(), "gnd11", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if(m<15 && n<3){
                    if(switchs[m][n].getConexion() == nConx) {
                        actual= switchs[m][n];
                        //                      Toast.makeText(getApplicationContext(), "switchs1", Toast.LENGTH_SHORT).show();
                    }
                    if(m<2){
                        if(relojs[m][n].getConexion() == nConx) {
                            actual= relojs[m][n];
                            //                        Toast.makeText(getApplicationContext(), "relojs1", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

        }
        return actual;
    }

    public void cambiarconexiones() {

        matrices punto1 = new matrices();
        int maxi = 10000, ant = 0;
        for (int z = 0; z < nCables; z++) {

            for (int m = 0; m < 5; m++) {
                if (gnd1[m].getnCable() != maxi) {
                    punto1 = buscarOtroExtremo(gnd1[m]);
                    ant = punto1.getConexion();
                    for(int i=0; i<punto1.getnHuecos(); i++) {
                        punto1 = buscarConexion(ant);
                        if(gnd1[m].getConexion()< punto1.getConexion()) {
                            punto1.setConexion(gnd1[m].getConexion());
                            cambiarestado(punto1);
                        }
                    }
                }
            }

            for (int m = 0; m < 5; m++) {
                if (vcc1[m].getnCable() != maxi) {
                    punto1 = buscarOtroExtremo(vcc1[m]);
                    ant = punto1.getConexion();
                    for(int i=0; i<punto1.getnHuecos(); i++) {
                        punto1 = buscarConexion(ant);
                        if( vcc1[m].getConexion() < punto1.getConexion()) {
                            punto1.setConexion(vcc1[m].getConexion());
                            cambiarestado(punto1);
                        }
                    }
                }
            }

            for (int m = 0; m < 63; m++) {
                for (int n = 0; n < 5; n++) {
                    if (ABCDE[m][n].getnCable() != maxi) {
                        punto1 = buscarOtroExtremo(ABCDE[m][n]);
                        ant = punto1.getConexion();
                        for (int i = 0; i < punto1.getnHuecos(); i++) {
                            punto1 = buscarConexion(ant);
                            if (ABCDE[m][n].getConexion() < punto1.getConexion()) {
                                punto1.setConexion(ABCDE[m][n].getConexion());
                                cambiarestado(punto1);
                            }
                        }
                    }
                }
            }

            for (int m = 0; m < 63; m++) {
                for (int n = 0; n < 5; n++) {
                    if (FGHIJ[m][n].getnCable() != maxi) {
                        punto1 = buscarOtroExtremo(FGHIJ[m][n]);
                        ant = punto1.getConexion();
                        for (int i = 0; i < punto1.getnHuecos(); i++) {
                            punto1 = buscarConexion(ant);
                            if (FGHIJ[m][n].getConexion() < punto1.getConexion()) {
                                punto1.setConexion(FGHIJ[m][n].getConexion());
                                cambiarestado(punto1);
                            }
                        }
                    }
                }
            }

            for (int m = 0; m < 50; m++) {
                for (int n = 0; n < 2; n++) {
                    if (VCC[m][n].getnCable() != maxi) {
                        punto1 = buscarOtroExtremo(VCC[m][n]);
                        ant = punto1.getConexion();
                        for (int i = 0; i < punto1.getnHuecos(); i++) {
                            punto1 = buscarConexion(ant);
                            if (VCC[m][n].getConexion() < punto1.getConexion()) {
                                punto1.setConexion(VCC[m][n].getConexion());
                                cambiarestado(punto1);
                            }
                        }
                    }
                }
            }

            for (int m = 0; m < 50; m++) {
                for (int n = 0; n < 2; n++) {
                    if (GND[m][n].getnCable() != maxi) {
                        punto1 = buscarOtroExtremo(GND[m][n]);
                        ant = punto1.getConexion();
                        for (int i = 0; i < punto1.getnHuecos(); i++) {
                            punto1 = buscarConexion(ant);
                            if (GND[m][n].getConexion() < punto1.getConexion()) {
                                punto1.setConexion(GND[m][n].getConexion());
                                cambiarestado(punto1);
                            }
                        }
                    }
                }
            }

            for (int m = 0; m < 15; m++) {
                if (leds[m].getnCable() != maxi) {
                    punto1 = buscarOtroExtremo(leds[m]);
                    ant = punto1.getConexion();
                    for(int i=0; i<punto1.getnHuecos(); i++) {
                        punto1 = buscarConexion(ant);
                        if (leds[m].getConexion() < punto1.getConexion()) {
                            punto1.setConexion(leds[m].getConexion());
                            cambiarestado(punto1);
                        }
                    }
                }
            }

            for (int m = 0; m < 15; m++) {
                for (int n = 0; n < 3; n++) {
                    if (switchs[m][n].getnCable() != maxi) {
                        punto1 = buscarOtroExtremo(switchs[m][n]);
                        ant= punto1.getConexion();
                        for(int i=0; i<punto1.getnHuecos(); i++) {
                            punto1 = buscarConexion(ant);
                            if(switchs[m][n].getConexion() < punto1.getConexion()) {
                                punto1.setConexion(switchs[m][n].getConexion());
                                cambiarestado(punto1);
                            }
                        }
                    }
                }
            }
            for (int m = 0; m < 2; m++) {
                for (int n = 0; n < 3; n++) {
                    if (relojs[m][n].getnCable() != maxi) {
                        punto1 = buscarOtroExtremo(relojs[m][n]);
                        ant = punto1.getConexion();
                        for(int i=0; i<punto1.getnHuecos(); i++){
                            punto1 = buscarConexion(ant);
                            if(relojs[m][n].getConexion()< punto1.getConexion()) {
                                punto1.setConexion(relojs[m][n].getConexion());
                                cambiarestado(punto1);
                            }
                        }
                    }
                }
            }

            for (int m = 0; m < 21; m++) {
                if (sieteseg1[m].getnCable() != maxi) {
                    punto1 = buscarOtroExtremo(sieteseg1[m]);
                    ant = punto1.getConexion();
                    for(int i=0; i<punto1.getnHuecos(); i++) {
                        punto1 = buscarConexion(ant);
                        if(sieteseg1[m].getConexion()<punto1.getConexion()) {
                            punto1.setConexion(sieteseg1[m].getConexion());
                            cambiarestado(punto1);
                        }
                    }
                }
            }

            for (int m = 0; m < 7; m++) {
                for (int n = 0; n < 2; n++) {
                    punto1= distancia_minima(mcompuerta[m][n].getCoordenada_x(), mcompuerta[m][n].getCoordenada_y());
                    mcompuerta[m][n].setConexion(punto1.getConexion());
                }
            }

        }
    }

    public void cambiarconexionespropias() {

        for (int m = 0; m < 63; m++) {
            for(int z= 0; z<5; z++) {
                for (int n = 0; n < 5; n++) {
                    if (ABCDE[m][z].getConexion() > ABCDE[m][n].getConexion()) {
                        ABCDE[m][z].setConexion(ABCDE[m][n].getConexion());
                    }
                }
            }
        }

        for (int m = 0; m < 63; m++) {
            for(int z= 0; z<5; z++) {
                for (int n = 0; n < 5; n++) {
                    if (FGHIJ[m][z].getConexion() > FGHIJ[m][n].getConexion()) {
                        FGHIJ[m][z].setConexion(FGHIJ[m][n].getConexion());
                    }
                }
            }
        }

    }

     /*  public void onWindowFocusChanged(boolean hasFocus){
        float width= Protoboard.getWidth();
        float height= Protoboard.getHeight();

        Log.e("heightttt", "" + height);
        Log.e("Widthhhh",""+width);
        Toast.makeText(getApplicationContext(), "1:  "+ height + "w: " + width, Toast.LENGTH_SHORT).show();

    }*/

    /* public int numconexiones(int num1, int num2){
        int cont1=0,cont2=0;
        for (int i = 0; i < 63; i++) {
            for (int m = 0; m < 5; m++) {
                if(num1==ABCDE[i][m].getConexion()){
                    cont1++;
                }
                if(num1==FGHIJ[i][m].getConexion()){
                    cont1++;
                }
                if(num2==ABCDE[i][m].getConexion()){
                    cont2++;
                }
                if(num2==FGHIJ[i][m].getConexion()){
                    cont2++;
                }
            }
        }
        for(int j = 0; j < 50; j++) {

            for (int k = 0; k < 2; k++) {
                if(num1==VCC[j][k].getConexion()){
                    cont1++;
                }
                if(num1==GND[j][k].getConexion()){
                    cont1++;
                }
                if(num2==VCC[j][k].getConexion()){
                    cont2++;
                }
                if(num2==GND[j][k].getConexion()){
                    cont2++;
                }
            }
        }

        if (cont1>cont2) {
            return num1;
        }else{
            return num2;
        }

    }*/


     /*  public class ScaleListener extends  ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override

        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(1f, Math.min(scale, 15f));
            Protoboard.setScaleX(scale);
            Protoboard.setScaleY(scale);
            arriba.setScaleX(scale);
            arriba.setScaleY(scale);
            leds11.setScaleX(scale);
            leds11.setScaleY(scale);
            leds12.setScaleX(scale);
            leds12.setScaleY(scale);
            leds13.setScaleX(scale);
            leds13.setScaleY(scale);
            leds14.setScaleX(scale);
            leds14.setScaleY(scale);
            leds15.setScaleX(scale);
            leds15.setScaleY(scale);
            leds21.setScaleX(scale);
            leds21.setScaleY(scale);
            leds22.setScaleX(scale);
            leds22.setScaleY(scale);
            leds23.setScaleX(scale);
            leds23.setScaleY(scale);
            leds24.setScaleX(scale);
            leds24.setScaleY(scale);
            leds25.setScaleX(scale);
            leds25.setScaleY(scale);
            leds31.setScaleX(scale);
            leds31.setScaleY(scale);
            leds32.setScaleX(scale);
            leds32.setScaleY(scale);
            leds33.setScaleX(scale);
            leds33.setScaleY(scale);
            leds34.setScaleX(scale);
            leds34.setScaleY(scale);
            leds35.setScaleX(scale);
            leds35.setScaleY(scale);
            componentes.setScaleX(scale);
            componentes.setScaleY(scale);


            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        SGD.onTouchEvent(event);
        return true;
    }

*/
}
