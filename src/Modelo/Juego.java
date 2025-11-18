package Modelo;

import Observer.Observable;
import Observer.Observador;

import java.util.ArrayList;
import java.util.List;

public class Juego implements Observable {
    private final List<Jugador> jugadores;
    private final Mazo mazo;
    private final PilaDeDescarte pila;
    private final Reglas reglas;
    private final List<Observador> observadores;
    private int indiceJugadorActual;
    private boolean sentidoHorario = true;
    private boolean juegoTerminado = false;
    private Palo paloActivo;

    public Juego(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.mazo = new Mazo();
        this.pila = new PilaDeDescarte();
        this.reglas = new Reglas();
        this.observadores = new ArrayList<>();
        this.indiceJugadorActual = 0;
    }

    // ====================== INICIO DEL JUEGO =======================
    public void iniciarPartida() {

        for (int i = 0; i < 5; i++) {
            for (Jugador j : jugadores) {
                j.agregarCarta(mazo.tomarCarta());
            }
        }


        Carta primera = mazo.tomarCarta();

        while (primera != null && primera.getPalo() == Palo.COMODIN) {
            // lo devuelve y toma otra
            mazo.agregarCarta(primera);
            primera = mazo.tomarCarta();
        }

        if (primera != null) {
            pila.ponerCarta(primera);
            paloActivo = primera.getPalo();
        }
        notificarObservadores(Evento.INICIO_PARTIDA);
    }

    // ====================== LÓGICA DE JUGADA Y TURNO (MODELO) =======================
    public ResultadoJugada jugarCartaConResultado(Jugador jugador, int indiceCarta) {
        if (jugador != getJugadorActual() || juegoTerminado) return null;

        Carta cartaJug = jugador.jugarCarta(indiceCarta);
        if (cartaJug == null || !reglas.puedeJugar(cartaJug, pila.verUltimaCarta(), paloActivo)) {
            if (cartaJug != null) jugador.agregarCarta(cartaJug);
            return null;
        }

        pila.ponerCarta(cartaJug);
        if (!(cartaJug.getNumero() == 10 || cartaJug.getPalo() == Palo.COMODIN)) {
            this.paloActivo = cartaJug.getPalo();
        }
        ResultadoJugada efecto = reglas.aplicarEfecto(cartaJug);
        aplicarEfectoYFlujo(efecto, jugador);

        if (jugador.sinCartas()) {
            juegoTerminado = true;
        }
        if (juegoTerminado) {
            notificarObservadores(Evento.JUEGO_TERMINADO);
        } else {
            notificarObservadores(Evento.CARTA_JUGADA);
        }
        return efecto;
    }

    private void aplicarEfectoYFlujo(ResultadoJugada efecto, Jugador jugador) {
        if (efecto == null) return;

        if (efecto.CambiaSentido()) {
            sentidoHorario = !sentidoHorario;
        }

        Jugador jugadorSiguiente = getSiguienteJugador();
        efecto.setJugadorAfectado(jugadorSiguiente);

        if (efecto.getRobaCartas() > 0) {
            for (int i = 0; i < efecto.getRobaCartas(); i++) {
                reciclarMazoSiEsNecesario();
                Carta tomada = mazo.tomarCarta();
                if (tomada != null) jugadorSiguiente.agregarCarta(tomada);
            }
        }

        boolean juegaDeNuevo = efecto.JuegaDeNuevo();
        boolean cambioSentidoEnDosJugadores = efecto.CambiaSentido() && jugadores.size() == 2;

        if (jugador.sinCartas() || juegaDeNuevo || cambioSentidoEnDosJugadores) {
            // Aca lo dejo vacio intencionalmente ya q es para detener la ejecuccion
        }
        else if (efecto.SaltaTurno()) {
            avanzarTurnoInterno();
            avanzarTurnoInterno();
        }
        else {
            avanzarTurnoInterno();
        }
    }

    public void avanzarTurno() {
        avanzarTurnoInterno();
        notificarObservadores(Evento.AVANCE_TURNO);
    }

    private void avanzarTurnoInterno() {
        if (juegoTerminado){
            return;
        }
        if (sentidoHorario) {
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
        } else {
            indiceJugadorActual = (indiceJugadorActual - 1 + jugadores.size()) % jugadores.size();
        }
    }
    public void penalizarPorNoDecirJodete(Jugador jugador) {
        reciclarMazoSiEsNecesario();
        Carta carta1 = mazo.tomarCarta();
        if (mazo.estaVacio()) {
            reciclarMazoSiEsNecesario();
        }
        Carta carta2 = mazo.tomarCarta();
        if (carta1 != null) {
            jugador.agregarCarta(carta1);
        }
        if (carta2 != null) {
            jugador.agregarCarta(carta2);
        }
        notificarObservadores(Evento.PENALIZACION);
    }
    public void reciclarMazoSiEsNecesario() {
        if (mazo.estaVacio()) {
            Carta ultima = pila.verUltimaCarta();
            List<Carta> recicladas = pila.getCartasMenosUltima();
            for (Carta c : recicladas) {
                mazo.agregarCarta(c);
            }
            pila.reiniciarCon(ultima);
        }
    }

    public Carta tomarCartaReinciando(Jugador jugador) {
        reciclarMazoSiEsNecesario();
        Carta carta = mazo.tomarCarta();
        if (carta != null) jugador.agregarCarta(carta);
        return carta;
    }
    public void terminarJuego() {
        this.juegoTerminado = true;
    }


    // ====================== GETTERS Y LOS METODOS DE OBSERVER =======================

    public Jugador getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
    }
    public Jugador getSiguienteJugador() {
        int siguiente = sentidoHorario
                ? (indiceJugadorActual + 1) % jugadores.size()
                : (indiceJugadorActual - 1 + jugadores.size()) % jugadores.size();
        return jugadores.get(siguiente);
    }
    public List<Jugador> getJugadores() {
        return jugadores;
    }
    public Carta getCartaSuperior() {
        return pila.verUltimaCarta();
    }
    public boolean JuegoTerminado() {
        return juegoTerminado;
    }
    public Palo getPaloActivo() {
        return paloActivo;
    }
    public void setPaloActivo(Palo p) {
        this.paloActivo = p;
        notificarObservadores(Evento.CAMBIO_PALO);
    }
    public Reglas getReglas() {
        return reglas;
    }

    @Override
    public void agregarObservador(Observador o) { observadores.add(o); }
    @Override
    public void quitarObservador(Observador o) { observadores.remove(o); }
    @Override
    public void notificarObservadores(Evento evento) {
        for (Observador observador : observadores) {
            observador.actualizar(evento);
        }
    }
}