package Controlador;

import Modelo.*;
import Observer.Observador;
import Vista.VistaJodeteInterfaz;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ControladorJodeteInterfaz implements ActionListener, Observador {
    private final Juego juego;
    private final VistaJodeteInterfaz vista;
    private static final String CMD_TOMAR = "CMD_TOMAR";
    private static final String CMD_JODETE = "CMD_JODETE";

    public ControladorJodeteInterfaz(Juego juego, VistaJodeteInterfaz vista) {
        this.juego = juego;
        this.vista = vista;
        juego.agregarObservador(this);
        vista.btnTomar.addActionListener(this);
        vista.btnJodete.addActionListener(this);

        vista.btnTomar.setActionCommand(CMD_TOMAR);
        vista.btnJodete.setActionCommand(CMD_JODETE);
    }


    public void iniciar() {
        juego.iniciarPartida();
        vista.mostrar();
    }

    // =================================================================
    //                   PATRÓN OBSERVER (ACTUALIZAR VISTA)
    // =================================================================
    public void actualizar (Evento evento) {
        switch (evento) {
            case INICIO_PARTIDA:
                vista.mostrarMensaje("El juego ha sido inicializado. ¡Primer turno!");
                refrescarVista();
                break;

            case AVANCE_TURNO:
            case CARTA_JUGADA:
            case CAMBIO_PALO:
            case PENALIZACION:
            case CARTA_TOMADA:
                refrescarVista();
                break;

            case JUEGO_TERMINADO:
                refrescarVista();
                break;
        }
    }
    private void refrescarVista() {
        Jugador actual = juego.getJugadorActual();

        vista.mostrarMano(actual);
        adjuntarListenersACartas();
        vista.mostrarTurno(actual.getNombre());
        vista.mostrarCartaSuperior(juego.getCartaSuperior(), juego.getPaloActivo());

        if (juego.JuegoTerminado()) {
            vista.mostrarMensaje("¡Juego Terminado! Ganador: " + obtenerGanador().getNombre());

        }
    }
    private void adjuntarListenersACartas() {
        for (Component comp : vista.getPanelMano().getComponents()) {
            if (comp instanceof JButton b) {
                if (b.getActionListeners().length == 0) {
                    b.addActionListener(this);
                }
            }
        }
    }

    // =================================================================
   //               (Como maneja la Gui los eventos)
    // =================================================================

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case CMD_TOMAR:
                tomaCartaI();
                break;
            case CMD_JODETE:
                jodeteAmigo();
                break;
            default:
                try {
                    int cardIndex = Integer.parseInt(command);
                    jugarCartaI(cardIndex);
                } catch (NumberFormatException ex) {
                    vista.mostrarMensaje("Error: Comando de acción no reconocido: " + command);
                }
                break;
        }
    }

    // =================================================================
    //                    LÓGICA DEL JUEGO (MÉTODOS PRIVADOS)
    // =================================================================


    private void jugarCartaI(int cardIndex) {
        Jugador actual = juego.getJugadorActual();
        Carta cartaJug = actual.getMano().get(cardIndex);
        Palo paloElegido = null;
        if (cartaJug.getNumero() == 10 || cartaJug.getPalo() == Palo.COMODIN) {
            paloElegido = vista.pedirNuevoPalo();
        }
        ResultadoJugada efecto = juego.jugarCartaConResultado(actual, cardIndex);
        if (efecto == null) {
            vista.mostrarMensaje("No se puede jugar esa carta. Reglas violadas.");
            return;
        }
        vista.mostrarMensaje(actual.getNombre() + " jugó " + cartaJug);
        aplicarYMostrarEfectos(efecto);
        if (actual.getMano().size() == 1) {
            int resp = JOptionPane.showConfirmDialog(vista.getFrame(),
                    actual.getNombre() + ", te queda una sola carta. ¿Dijiste '¡Jodete!'?",
                    "¡Jodete! ", JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.NO_OPTION) {
                juego.penalizarPorNoDecirJodete(actual);

            }
        }
        if (actual.sinCartas()) {
            vista.mostrarMensaje(actual.getNombre() + " se quedó sin cartas y ganó el juego!");
            juego.terminarJuego();
        }
    }


    private void tomaCartaI() {
        Jugador actual = juego.getJugadorActual();
        Carta tomada = juego.tomarCartaReinciando(actual);
        if (tomada == null) {
            vista.mostrarMensaje("El mazo está vacío. No se puede tomar carta.");
            return;
        }
        vista.mostrarMensaje(actual.getNombre() + " tomó: " + tomada);
        if (juego.getReglas().puedeJugar(tomada, juego.getCartaSuperior(), juego.getPaloActivo())) {
            int resp = JOptionPane.showConfirmDialog(
                    vista.getFrame(),
                    "La carta tomada puede jugarse. ¿Desea jugarla ahora?",
                    "Jugar carta",
                    JOptionPane.YES_NO_OPTION
            );
            if (resp == JOptionPane.YES_OPTION) {
                jugarCartaI(actual.getMano().size() - 1); // Jugar la última carta
                return;
            }
        }
        vista.mostrarMensaje(actual.getNombre() + " decide no jugar la carta tomada.");
        juego.avanzarTurno();

    }

    // esto es cuando no dice "jodete", toma cartas por colgado.
    private void jodeteAmigo() {
        Jugador actual = juego.getJugadorActual();
        if (actual.getMano().size() == 1) {
            vista.mostrarMensaje(actual.getNombre() + " dijo ¡Jodete! (Correcto)");
        } else {
            vista.mostrarMensaje(actual.getNombre() + " dijo ¡Jodete! (Incorrecto, tiene " + actual.getMano().size() + " cartas)");
        }
    }
    private void aplicarYMostrarEfectos(ResultadoJugada efecto) {
        if (efecto.TieneEfecto()) {
            if (efecto.getRobaCartas() > 0 && efecto.getJugadorAfectado() != null) {
                vista.mostrarMensaje(efecto.getJugadorAfectado().getNombre()
                        + " debe tomar " + efecto.getRobaCartas() + " carta(s).");
            }
            if (efecto.CambiaSentido()) vista.mostrarMensaje("¡Cambio de sentido del juego!");
            if (efecto.CambiaPalo() || efecto.Comodin()) {
                vista.mostrarMensaje("Palo activo cambiado a: " + juego.getPaloActivo());
            }
        }
    }
    // el q se queda sin cartas gana
    private Jugador obtenerGanador() {
        return juego.getJugadores().stream()
                .filter(Jugador::sinCartas)
                .findFirst()
                .orElseGet(() -> juego.getJugadores().get(0));
    }



}