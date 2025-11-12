import com.almasb.fxgl.entity.component.Component;

public class VehiculoComponent extends Component {
    double masa;
    double velocidad;
    double posicion;
    double desplazamiento;

    double  peso;
    double  tiempo;
    double aceleracion;
    double gravedad = 9.81;
public double peso (double masa, double gravedad){
    double peso = masa * gravedad;
    return peso;
}

}
