import Controlador.ControladorJodeteInterfaz;
import Modelo.Juego;
import Modelo.Jugador;
import Vista.VistaJodeteInterfaz;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class MainInterfaz {
    public static void main(String[] args) {
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador("Matias (GUI)"));
        jugadores.add(new Jugador("Diego (GUI)"));
        jugadores.add(new Jugador("Martin (GUI)"));
        SwingUtilities.invokeLater(() -> {
            Juego modelo = new Juego(jugadores);
            VistaJodeteInterfaz vista = new VistaJodeteInterfaz(null);
            ControladorJodeteInterfaz controlador = new ControladorJodeteInterfaz(modelo, vista);
            vista.setControlador(controlador);
            controlador.iniciar();
        });
    }
}