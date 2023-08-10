package com.campusflow.campusflow;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


public class AttendenceController {
    private volatile boolean stopThread = false;
    private Webcam webcam;
    @FXML
    private Label text;
    @FXML
    private SwingNode swingNode;

    void setText(String string){
        text.setText(string);
    }

    @FXML
    void startAttendence(ActionEvent event) {
        webcam = Webcam.getWebcams().get(1);
        Dimension size = WebcamResolution.QVGA.getSize();
        webcam.setViewSize(size);
        SwingUtilities.invokeLater(() -> {
            WebcamPanel webcamPanel = new WebcamPanel(webcam);
            webcamPanel.setPreferredSize(size);
            webcamPanel.setFPSDisplayed(true);

            swingNode.setContent(webcamPanel);
        });

        Task<String> checkQR = new Task<String>() {
            @Override
            protected String call() throws Exception {
                do {
                    Result result = null;
                    BufferedImage image = null;
                    if (webcam.isOpen()) {
                        if ((image = webcam.getImage()) == null) {
                            continue;
                        }
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        try {
                            result = new MultiFormatReader().decode(bitmap);
                            System.out.println(result.getText());
                            if(result !=null){
                                updateValue(result.getText());
                            }
                        } catch (NotFoundException e) {
                            //e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100); // Sleep for 100 milliseconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                } while (!stopThread);
                return null;
            }
        };

        text.textProperty().bind(checkQR.valueProperty().asString("%s"));
        Thread QRThread = new Thread(checkQR);
        QRThread.setDaemon(true);
        QRThread.start();

    }
}