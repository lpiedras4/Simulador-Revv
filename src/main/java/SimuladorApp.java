import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class SimuladorApp {
    @FXML
    private Pane paneSimulador;

    @FXML
    private ImageView imgCoche;
    @FXML
    private ImageView imgCamino;
    @FXML
    private TextField txtMasa;
    @FXML
    private TextField Datos;
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
    private List<javafx.scene.image.Image> frames;


    //Acción para botón "INICIAR"

    @FXML
    private void onIniciarClick() throws IOException {
        vehiculo = new VehiculoComponent();
        if(!txtVelSegura.getText().isBlank()&&!txtCoefF.getText().isBlank()&&!txtCoefAero.getText().isBlank()&&!txtFuerzaMotor.getText().isBlank()
        &&!txtFuerzaFrenado.getText().isBlank()&&!txtAreaFrontal.getText().isBlank()&&!txtMasa.getText().isBlank()){
            vehiculo.setMasa(Double.parseDouble(txtMasa.getText()));
            vehiculo.setFuerzaMotorMaxima(Double.parseDouble(txtFuerzaMotor.getText()));
            vehiculo.setFuerzaFrenadoMaxima(Double.parseDouble(txtFuerzaFrenado.getText()));
            vehiculo.setCoefF(Double.parseDouble(txtCoefF.getText()));
            vehiculo.setCoefAero(Double.parseDouble(txtCoefAero.getText()));
            vehiculo.setAreaFrontal(Double.parseDouble(txtAreaFrontal.getText()));
            vehiculo.setVelocidadMaxSegura(Double.parseDouble(txtVelSegura.getText()));

            imgCoche.setLayoutX(0);
            animacionCoche();
            actualizarAnimacion();
        } else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Falta de Datos");
            alert.setHeaderText(null);
            alert.setContentText("Faltan de poner informacion en la seccion de datos");
            alert.showAndWait();
        }
    }


    //Animacion del coche
    private Timeline timeline;

    private void animacionCoche() throws IOException {
        if (frames == null) {
            frames = new ArrayList<>();

            File gifFile = new File("src/main/resources/camino_loop.gif");
            ImageInputStream stream = ImageIO.createImageInputStream(gifFile);
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
            ImageReader reader = readers.next();
            reader.setInput(stream);

            int numFrames = reader.getNumImages(true);
            for (int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                Image fxImage = SwingFXUtils.toFXImage(frame, null);
                frames.add(fxImage);
            }
        }

        final int[] currentFrame = {0};

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), e -> {
                    imgCamino.setImage(frames.get(currentFrame[0]));
                    currentFrame[0] = (currentFrame[0] + 1) % frames.size();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void onAcelerarClick() {
        timeline.setRate(timeline.getRate() + 0.5); // acelera
    }

    @FXML
    private void onFrenarClick() {
        timeline.setRate(Math.max(0, timeline.getRate() - 0.5));
    }

    @FXML
    private void onReiniciarClick(){

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
