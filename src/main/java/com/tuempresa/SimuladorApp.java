package com.tuempresa;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class SimuladorApp {


    @FXML private Pane paneSimulador;
    @FXML private ImageView imgCoche;
    @FXML private ImageView imgCamino;


    @FXML private Button btnAcelerar;
    @FXML private Button btnFrenar;
    @FXML private Button btnIniciar;
    @FXML private Button btnReiniciar;

    @FXML private TextField txtMasa;
    @FXML private TextField txtFuerzaMotor;
    @FXML private TextField txtFuerzaFrenado;
    @FXML private TextField txtCoefF;
    @FXML private TextField txtCoefAero;
    @FXML private TextField txtAreaFrontal;
    @FXML private TextField txtVelSegura;

    // Campos de Texto (Salidas / Etiquetas)
    @FXML private Label lblVelocidad;
    @FXML private Label lblAceleracion;
    @FXML private Label lblFuerzaNeta;
    @FXML private Label lblEstado;
    @FXML private Label lblPotencia;
    @FXML private Label lblDistanciaFrenado;

    // --- LÓGICA ---
    private VehiculoComponent vehiculo;
    private AnimationTimer gameLoop;

    // Método que se ejecuta automáticamente al cargar el FXML
    @FXML
    public void initialize() {

        try {
            mostrarImagenEstatica();
        } catch (Exception e) {
            System.out.println("No se encontró el GIF. Usando imagen estática.");
        }

        // Configurar los botones para que funcionen como pedales
        // (Presionar = Activar, Soltar = Desactivar)
        configurarBotones();
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.widthProperty().bind(paneSimulador.widthProperty());
        clip.heightProperty().bind(paneSimulador.heightProperty());
        paneSimulador.setClip(clip);
        lblVelocidad.setText("0.00 m/s");
        lblAceleracion.setText("0.00 m/s²");
        lblDistanciaFrenado.setText("0.00 m");
        lblPotencia.setText("0.00 HP");
        lblFuerzaNeta.setText("0.00 N");
    }

    @FXML
    private void onIniciarClick() {
        // 1. Validar que haya datos escritos
        if (faltanDatos()) {
            mostrarAlerta("Faltan Datos", "Por favor llena todos los campos de texto.");
            return;
        }

        // 2. Intentar crear el vehículo. SI FALLA, NOS DETENEMOS AQUÍ.
        if (!inicializarVehiculo()) {
            return;
        }

        // 3. Arrancar el Bucle (Solo si el paso 2 fue exitoso)
        if (gameLoop != null) {
            gameLoop.stop();
        }

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizarFisicaYPantalla();
            }
        };

        btnIniciar.setVisible(false);
        btnAcelerar.setVisible(true);
        btnFrenar.setVisible(true);
        btnReiniciar.setVisible(true);
        gameLoop.start();
        lblEstado.setText("Simulación corriendo...");
    }

    private void actualizarFisicaYPantalla() {
        double anchoPantalla = paneSimulador.getWidth();
        if(anchoPantalla<=0) anchoPantalla = 800;
        double posicionVisual = (vehiculo.getPosicion() * 5.0) % anchoPantalla;
        // Calcular Física (Paso de tiempo fijo aprox 0.016s)
        double tpf = 0.016;
        vehiculo.onUpdate(tpf);

       // mover coche visualmente
        imgCoche.setLayoutX(posicionVisual);

        // Actualizar Etiquetas (Salidas)
        lblVelocidad.setText(String.format("%.2f m/s", vehiculo.getVelocidad()));
        lblAceleracion.setText(String.format("%.2f m/s²", vehiculo.getAceleracion()));
        lblFuerzaNeta.setText(String.format("%.2f N", vehiculo.getFuerzaNetaActual()));
        lblPotencia.setText(String.format("%.2f HP", vehiculo.getPotenciaActual()));
        lblDistanciaFrenado.setText(String.format("%.2f m", vehiculo.getDistanciaFrenado()));

        //Checar si se rompió
        if (vehiculo.isPrototipoRoto()) {
            gameLoop.stop(); // Detener simulación
            mostrarImagenEstatica();
            lblEstado.setText("¡PROTOTIPO ROTO!");
            mostrarAlerta("Falla Crítica", "El vehículo excedió la velocidad segura.");
        }
    }

    private boolean inicializarVehiculo() {
        vehiculo = new VehiculoComponent();
        try {
            mostrarImagenMovimiento();
            vehiculo.setMasa(Double.parseDouble(txtMasa.getText()));
            vehiculo.setFuerzaMotorMaxima(Double.parseDouble(txtFuerzaMotor.getText()));
            vehiculo.setFuerzaFrenadoMaxima(Double.parseDouble(txtFuerzaFrenado.getText()));
            vehiculo.setCoefF(Double.parseDouble(txtCoefF.getText()));
            vehiculo.setCoefAero(Double.parseDouble(txtCoefAero.getText()));
            vehiculo.setAreaFrontal(Double.parseDouble(txtAreaFrontal.getText()));
            vehiculo.setVelocidadMaxSegura(Double.parseDouble(txtVelSegura.getText()));
            return true;
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Números", "Asegúrate de poner solo números válidos (sin letras).");
            return false;
        }
    }

    private void configurarBotones() {

        if (btnAcelerar != null) {
            btnAcelerar.setOnMousePressed(e -> {
                if (vehiculo != null) vehiculo.setAcelerando(true);
                btnAcelerar.setFocusTraversable(false);
            });
            btnAcelerar.setOnMouseReleased(e -> {
                if (vehiculo != null) vehiculo.setAcelerando(false);
                btnFrenar.setFocusTraversable(false);
            });
        }
        if (btnFrenar != null) {
            btnFrenar.setOnMousePressed(e -> {
                if (vehiculo != null) vehiculo.setFrenando(true);
            });
            btnFrenar.setOnMouseReleased(e -> {
                if (vehiculo != null) vehiculo.setFrenando(false);
            });
        }
    }

    @FXML
    private void onReiniciarClick() {
        if (gameLoop != null) gameLoop.stop();
        imgCoche.setLayoutX(0);
        lblVelocidad.setText("0.00 m/s");
        lblAceleracion.setText("0.00 m/s²");
        lblDistanciaFrenado.setText("0.00 m");
        lblPotencia.setText("0.00 HP");
        lblFuerzaNeta.setText("0.00 N");
        lblEstado.setText("Reiniciado");
        btnIniciar.setVisible(true);
        btnAcelerar.setVisible(false);
        btnFrenar.setVisible(false);
        btnReiniciar.setVisible(false);
        mostrarImagenEstatica();
        // Limpiar el vehículo
        vehiculo = null;
    }

    // --- UTILIDADES ---
    private boolean faltanDatos() {
        return txtVelSegura.getText().isBlank() || txtCoefF.getText().isBlank() ||
                txtCoefAero.getText().isBlank() || txtFuerzaMotor.getText().isBlank() ||
                txtFuerzaFrenado.getText().isBlank() || txtAreaFrontal.getText().isBlank() ||
                txtMasa.getText().isBlank();
    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public void mostrarImagenEstatica(){
        Image camino = new Image(getClass().getResourceAsStream("/camino.png"));
        imgCamino.setImage(camino);
    }
    public void mostrarImagenMovimiento(){
        Image gifCamino = new Image(getClass().getResourceAsStream("/camino_loop.gif"));
        imgCamino.setImage(gifCamino);
    }
}