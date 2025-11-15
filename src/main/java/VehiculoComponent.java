import com.almasb.fxgl.entity.component.Component;

public class VehiculoComponent extends Component {
    //Inputs iniciales del usuario
    private double masa;
    private double gravedad = 9.81;
    private double peso;
    /*Las variables fuerzaFrenadoMax y fuerzaMotorMax son definidas por el usuario y sirven como especificaciones del prototipo
    creado */
    private double fuerzaFrenadoMax;
    private double fuerzaMotorMax;
    private double coefF;
    private double velocidadMaxSegura; //Define el limite de falla del prototipo. Si la velocidad del simulador rebasa esta variable, el prototipo se rompe
    private double coefAero;
    private double areaFrontal;
    double desplazamientoFrame = 0;
    private double densidadAire = 1.225;

    //Variables para controles
    private boolean estaAcelerando = false;
    private boolean estaFrenando = false;

    //Variables de salida
    private double velocidad = 0.0;
    private double aceleracion = 0.0;
    private double potenciaActual = 0;
    private double trabajo = 0;
    private double trabajoAcumulado = 0;
    private double fuerzaNetaActual = 0;
    private double fuerzaFrenadoActual = 0;
    private double fuerzaMotorActual = 0;
    private double fuerzaResistencia =0;
    private double distanciaFrenado = 0;
    private boolean prototipoRoto = false;
    private double posicion = 0;

    private double fuerzaNormal = 0;
    private double fuerzaFriccion = 0;


    @Override
    public void onUpdate(double tpf) {
        peso = masa * gravedad;
        fuerzaNormal = peso;
        if(prototipoRoto){
            velocidad = 0.0;
            aceleracion = 0.0;
            return;
        }

            fuerzaMotorActual = 0.0;
            if(estaAcelerando){
                fuerzaMotorActual = fuerzaMotorMax;
            }
            fuerzaFrenadoActual = 0.0;
            if(estaFrenando){
                fuerzaFrenadoActual = -fuerzaFrenadoMax;
            }

        fuerzaResistencia = (velocidad * velocidad) * areaFrontal * coefAero * (densidadAire/2);
        fuerzaFriccion = -coefF*fuerzaNormal;
        fuerzaNetaActual = fuerzaFrenadoActual + fuerzaMotorActual - fuerzaFriccion- fuerzaResistencia;
        velocidad = velocidad + aceleracion * tpf;
        aceleracion = velocidad/tpf;

        posicion = posicion + (velocidad*tpf);
        desplazamientoFrame = velocidad * tpf;
        trabajo = fuerzaMotorActual * desplazamientoFrame;
        trabajoAcumulado = trabajo + trabajoAcumulado;
        potenciaActual = fuerzaMotorActual * velocidad;

        distanciaFrenado = (velocidad*velocidad) / (2*getDesaceleracionMaxima());
    if(Math.abs(velocidad) > velocidadMaxSegura){
        prototipoRoto = true;
    }

    }
    /* API para controles (getters y setters) */
    public void setAcelerando (boolean estaPresionado){
        this.estaAcelerando = estaPresionado;
    }
    public void setFrenando(boolean estaPresionado){
        this.estaFrenando = estaPresionado;
    }

    //Metodos para UI

    public double getFuerzaFriccion(){
        return this.fuerzaFriccion;
    }

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

    //Setters para configuración del prototipo
    public void setMasa (double masa){
        this.masa = masa;
    }
    public void setFuerzaMotorMaxima(double fuerzaMotorMax){
        this.fuerzaMotorMax = fuerzaMotorMax;
    }
    public void setFuerzaFrenadoMaxima(double fuerzaFrenadoMax){
        this.fuerzaFrenadoMax = fuerzaFrenadoMax;
    }
    public void setCoefF(double coefF){
        this.coefF = coefF;
    }
    public void setCoefAero(double coefAero){
        this.coefAero = coefAero;
    }
    public void setAreaFrontal (double areaFrontal){
        this.areaFrontal = areaFrontal;
    }
    public void setVelocidadMaxSegura(double velocidadMaxSegura){
        this.velocidadMaxSegura = velocidadMaxSegura;

    }

}
