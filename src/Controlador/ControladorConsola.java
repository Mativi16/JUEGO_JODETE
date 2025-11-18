package Controlador;

import Modelo.*;
import Observer.Observador;
import Vista.VistaConsola;

public class ControladorConsola implements Observador {
    private final Juego juego;
    private final VistaConsola vista;

    public ControladorConsola(Juego juego, VistaConsola vista) {
        this.juego = juego;
        this.vista = vista;
        juego.agregarObservador(this);
    }

    public void iniciarPartida() {
        juego.iniciarPartida();
        buclePrincipal();
    }

    private void buclePrincipal() {
        while (!juego.JuegoTerminado()) {
            Jugador actual = juego.getJugadorActual();

            vista.mostrarMensaje("\n-------------------------------------------");
            vista.mostrarCartaSuperior(juego.getCartaSuperior(), juego.getPaloActivo());
            vista.mostrarMano(actual);

            int opcion = vista.pedirCartaAJugar(actual.getMano().size());

            if (opcion == 0) {
                manejarTomarCarta(actual);
            } else {
                manejarJugada(actual, opcion - 1);
            }
        }
        vista.mostrarMensaje("\n-----------------------------------------------");
        vista.mostrarMensaje("¡El juego terminó! El ganador es: " + obtenerGanador().getNombre());
        vista.mostrarMensaje("\n-----------------------------------------------");
        vista.mostrarMensaje("\n------ ¡GRACIAS POR JUGAR AL JODETE! ------");
    }

    private void manejarJugada(Jugador jugador, int indiceCarta) {
        Carta cartaJug = jugador.getMano().get(indiceCarta);
        Palo paloElegido;
        ResultadoJugada efecto = juego.jugarCartaConResultado(jugador, indiceCarta);

        if (efecto == null) {
            vista.mostrarMensaje("Jugada inválida: no puedes jugar esa carta.");
            return;
        }

        if (efecto.CambiaPalo() || efecto.Comodin()) {
            paloElegido = vista.pedirNuevoPalo();
            juego.setPaloActivo(paloElegido);
        }

        vista.mostrarMensaje(jugador.getNombre() + " jugó " + cartaJug);
        aplicarYMostrarEfectos(efecto);

        if (jugador.getMano().size() == 1) {
            boolean dijo = vista.pedirConfirmacion(jugador.getNombre() + ", te queda una carta. ¿Dijiste '¡Jodete!'?");
            if (!dijo) {
                juego.penalizarPorNoDecirJodete(jugador);
                vista.mostrarMensaje("Penalización aplicada: +2 cartas para " + jugador.getNombre());
            }
        }
    }

    private void manejarTomarCarta(Jugador jugador) {
        Carta tomada = juego.tomarCartaReinciando(jugador);
        if (tomada == null) {
            vista.mostrarMensaje("El mazo está vacío. No se puede tomar carta.");
            return;
        }

        vista.mostrarMensaje(jugador.getNombre() + " tomó: " + tomada);

        Carta cartaSuperior = juego.getCartaSuperior();
        if (juego.getReglas().puedeJugar(tomada, cartaSuperior, juego.getPaloActivo())) {
            boolean desea = vista.preguntarJugarCartaTomada(tomada, juego.getCartaSuperior());
            if (desea) {
                int indice = jugador.getMano().size() - 1;
                manejarJugada(jugador, indice);
                return;
            }
        }

        vista.mostrarMensaje(jugador.getNombre() + " decide no jugar la carta tomada.");
        juego.avanzarTurno();
    }

    private void aplicarYMostrarEfectos(ResultadoJugada efecto) {
        if (efecto.TieneEfecto()) {
            if (efecto.getRobaCartas() > 0 && efecto.getJugadorAfectado() != null) {
                vista.mostrarMensaje(efecto.getJugadorAfectado().getNombre()
                        + " debe tomar " + efecto.getRobaCartas() + " carta(s).");
            }
            if (efecto.CambiaSentido()) {
                vista.mostrarMensaje("¡Cambio de sentido del juego!");
                if (juego.getJugadores().size() == 2) {
                    vista.mostrarMensaje("El juego vuelve a " + juego.getJugadorActual().getNombre() + " (Juega de nuevo)");
                }
            }
            if (efecto.SaltaTurno()) {
                vista.mostrarMensaje("¡Se salta el turno del siguiente jugador!");
            }
            if (efecto.JuegaDeNuevo()) {
                vista.mostrarMensaje(juego.getJugadorActual().getNombre() + " juega de nuevo por efecto de la carta.");
            }
            if (efecto.CambiaPalo() || efecto.Comodin()) {
                vista.mostrarMensaje("Palo activo cambiado a: " + juego.getPaloActivo());
            }
        }
    }

    private Jugador obtenerGanador() {
        return juego.getJugadores().stream()
                .filter(Jugador::sinCartas)
                .findFirst()
                .orElse(juego.getJugadorActual());
    }

    @Override
    public void actualizar(Modelo.Evento evento) {
        if (juego.JuegoTerminado()) return;

        if (evento == Modelo.Evento.PENALIZACION) {
            vista.mostrarMensaje("AVISO: El estado del juego fue actualizado por una penalización.");
        }
    }
}