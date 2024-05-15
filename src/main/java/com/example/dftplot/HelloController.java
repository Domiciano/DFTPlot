package com.example.dftplot;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    double mx, my;
    double[] signal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Señal artificial

        /*
        double fs = 1000;
        double duration = 2;
        signal = new double[(int)duration * (int) fs];
        double[] time = new double[(int)duration * (int) fs];
        for (int i = 0; i < duration * fs; i++) {
            time[i] = i / fs;
            signal[i] = 1*Math.sin(2 * Math.PI * 60 * time[i])+10*Math.sin(2 * Math.PI * 20 * time[i]);
        }
        signal = DFTUtils.normalize(signal);
        */


        //Señal real de acelerómetros
        int fs = 1000;
        signal = VoiceRecorder.record(fs);
        double duration = signal.length/(double) fs;
        double[] time = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            time[i] = i / (double) fs;
        }
        signal = DFTUtils.normalize(signal);


        System.out.println("DFTing...");
        double[] spectrum = DFTUtils.dftSpectrum(signal);
        double[] freqs = DFTUtils.dftFreq(signal, fs);
        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("Graficando...");
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseMoved(e -> {
            mx = e.getX();
            my = e.getY();
        });

        //DIBUJO
        new Thread(()->{
            while (true){
                Platform.runLater(()->{
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    double deltaX = canvas.getWidth()/signal.length;

                    gc.setStroke(Color.BLUE);
                    for(int i=0 ; i<time.length-1; i++){
                        gc.strokeLine(i*deltaX,0.25*canvas.getHeight()+0.4*canvas.getHeight()*DFTUtils.normalize(signal)[i],(i+1)*deltaX, 0.25*canvas.getHeight()+0.4*canvas.getHeight()*DFTUtils.normalize(signal)[i+1]);
                    }

                    deltaX = canvas.getWidth()/spectrum.length;
                    gc.setStroke(Color.RED);
                    for(int i=0 ; i<freqs.length-1; i++){
                        gc.strokeLine(i*deltaX,0.95*canvas.getHeight()-0.4*canvas.getHeight()*DFTUtils.normalize(spectrum)[i],(i+1)*deltaX, 0.95*canvas.getHeight()-0.4*canvas.getHeight()*DFTUtils.normalize(spectrum)[i+1]);
                    }


                    if(my<canvas.getHeight()/2){
                        gc.setFill(Color.BLACK);
                        double s = duration * (mx / canvas.getWidth());
                        gc.fillText(df.format(s) + " segundos", mx, my);
                    }else {
                        gc.setFill(Color.DARKVIOLET);
                        double HZ = 0.5 * fs * (mx / canvas.getWidth());
                        gc.fillText(df.format(HZ) + " Hz", mx, my);
                    }

                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }

    public double[] loadData(){
        String json = Client.GETrequest("https://parkinsonbluetooth.firebaseio.com/pruebas/XOrozcoMarchaNormal/pruebas/2018-9-3-17-28-42.json");
        Gson gson = new Gson();
        Client.Data data = gson.fromJson(json, Client.Data.class);

        int length = 600;
        double[] output = new double[length];
        for(int i=0 ; i<length ; i++){
            double d = (data.derecha.get(i).getY()-512)/1024f;
            output[i] = d;
        }
        return output;
    }



}