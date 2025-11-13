import com.almasb.fxgl.entity.component.Component;

public class VehiculoComponent extends Component {
    //Inputs iniciales del usuario
    private double masa;
    private double gravedad = 9.81;
    private double peso = masa * gravedad;
    /*Las variables fuerzaFrenadoMax y fuerzaMotorMax son definidas por el usuario y sirven como especificaciones del prototipo
    creado */
    private double fuerzaFrenadoMax;
    private double fuerzaMotorMax;
    private double coefF;
    private double velocidadMaxSegura; //Define el limite de falla del prototipo. Si la velocidad del simulador rebasa esta variable, el prototipo se rompe
    private double coefAero;
    private double areaFrontal;

    //Variables para controles
    private boolean estaAcelerando = false;
    private boolean estaFrenando = false;

    //Variables de salida
    private double velocidad = 0.0;
    private double aceleracion = 0.0;
    private double potenciaActual = 0;
    private double trabajoAcumulado = 0;
    private double fuerzaNetaActual = 0;
    private double distanciaFrenado = 0;
    private boolean prototipoRoto = false;
    private double posicion = 0;

    /* API para controles (getters y setters) */



    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if(prototipoRoto){
            velocidad = 0.0;
            aceleracion = 0.0;
            return;
        }

        double fuerzaMotorAct = 0;


    }

    public void setAcelerando (boolean estaPresionado){
        this.estaAcelerando = estaPresionado;
    }
    public void setFrenando(boolean estaPresionado){
        this.estaFrenando = estaPresionado;
    }

    //Metodos para UI
    public double getVelocidad(){
        return this.velocidad;
    }

    public double getAceleracion(){
        return this.aceleracion;
    }
    public double getPotenciaActual(){
        return this.potenciaActual;
    }
    public double getTrabajoAcumulado(){
        return this.trabajoAcumulado;
    }
    public double getFuerzaNetaActual(){
        return this.fuerzaNetaActual;
    }

    public double getDistanciaFrenado(){
        return this.distanciaFrenado;
    }
    public boolean isPrototipoRoto(){
        return this.prototipoRoto;
    }
    public double getDesaceleracionMaxima(){
        if(masa==0){
            return 0;
        }
        return this.fuerzaFrenadoMax / this.masa;
    }

}
