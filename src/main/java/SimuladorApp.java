import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.swing.*;

public class SimuladorApp {
    @FXML
    private Pane paneSimulador;

    @FXML
    private ImageView imgCoche;
    @FXML
    private TextField txtMasa;
    @FXML
    private TextField txtFuerzaMotor;
    @FXML
    private TextField txtFuerzaFrenado;
    @FXML
    private TextField txtCoefF;
    @FXML
    private TextField txtCoefAero;
    @FXML
    private TextField txtAreaFrontal;
    @FXML
    private TextField txtVelSegura;
    @FXML
    private TextField lblVelocidad;
    @FXML
    private TextField lblAceleracion;
    @FXML
    private TextField lblFuerzaNeta;
    @FXML
    private TextField lblEstado;
    @FXML
    private TextField lblPotencia;
    @FXML
    private TextField lblDistanciaFrenado;




    //Acceder a controlador simulador
    private VehiculoComponent vehiculo;


    //Acción para botón "INICIAR"

    @FXML
    private void onIniciarClick() {
        vehiculo = new VehiculoComponent();

        vehiculo.setMasa(Double.parseDouble(txtMasa.getText()));
        vehiculo.setFuerzaMotorMaxima(Double.parseDouble(txtFuerzaMotor.getText()));
        vehiculo.setFuerzaFrenadoMaxima(Double.parseDouble(txtFuerzaFrenado.getText()));
        vehiculo.setCoefF(Double.parseDouble(txtCoefF.getText()));
        vehiculo.setCoefAero(Double.parseDouble(txtCoefAero.getText()));
        vehiculo.setAreaFrontal(Double.parseDouble(txtAreaFrontal.getText()));
        vehiculo.setVelocidadMaxSegura(Double.parseDouble(txtVelSegura.getText()));

        imgCoche.setLayoutX(0);
        animacionCoche();



    }



    //Animacion del coche
    private Timeline timeline;
    private void animacionCoche() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.016), e -> {
                    imgCoche.setLayoutX(imgCoche.getLayoutX() + 2);
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }





    //Tasa de actualización de la imagen y de los resultados
    private void actualizarAnimacion() {




        double tpf = 0.016; //60 FPS
        vehiculo.onUpdate(tpf);

        imgCoche.setLayoutX(vehiculo.getPosicion());


        lblVelocidad.setText(String.format("%.2f m/s", vehiculo.getVelocidad()));
        lblAceleracion.setText(String.format("%.2f m/s²", vehiculo.getAceleracion()));
        lblFuerzaNeta.setText(String.format("%.2f N", vehiculo.getFuerzaNetaActual()));
        lblPotencia.setText(String.format("%.2f N", vehiculo.getFuerzaNetaActual()));
        lblDistanciaFrenado.setText(String.format("%.2f N", vehiculo.getDistanciaFrenado()));


        // Estado del prototipo (coche) roto
        if (vehiculo.isPrototipoRoto()) {
            timeline.stop();
            lblEstado.setText("¡Prototipo roto!");
        }
    }






}
