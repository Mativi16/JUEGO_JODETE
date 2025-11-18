import Modelo.*;
import Vista.VistaConsola;
import Controlador.ControladorConsola;

import java.util.List;


public class MainConsola {
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        List<Jugador> jugadores = vista.pedirDatosJugadores();
        Juego juego = new Juego((jugadores));
        ControladorConsola controlador = new ControladorConsola(juego, vista);
        controlador.iniciarPartida();


    }

}


