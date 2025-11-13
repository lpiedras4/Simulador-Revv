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
    public VehiculoComponent(double masa, double fuerzaFrenado, double fuerzaMotor, double coefF, double coefAero, double areaFrontal){
        this.masa = masa;
        this.fuerzaFrenadoMax = fuerzaFrenadoMax;
        this.fuerzaMotorMax = fuerzaMotorMax;
        this.coefF = coefF;
       this.coefAero = coefAero;
       this.areaFrontal = areaFrontal;
    }
public double peso (double masa, double gravedad){
    double peso = masa * gravedad;
    return peso;
}

}
