package Modelo;

public class ResultadoJugada {

    private boolean saltaTurno;
    private boolean cambiaSentido;
    private int robaCartas;
    private boolean juegaDeNuevo;
    private boolean cambiaPalo;
    private boolean comodin;
    private Jugador jugadorAfectado;
    public boolean SaltaTurno() {
        return saltaTurno;
    }
    public void setSaltaTurno(boolean saltaTurno) {
        this.saltaTurno = saltaTurno;
    }
    public boolean CambiaSentido() {
        return cambiaSentido;
    }
    public void setCambiaSentido(boolean cambiaSentido) {
        this.cambiaSentido = cambiaSentido;
    }
    public int getRobaCartas() {
        return robaCartas;
    }
    public void setRobaCartas(int robaCartas) {
        this.robaCartas = robaCartas;
    }
    public boolean JuegaDeNuevo() {
        return juegaDeNuevo;
    }
    public void setJuegaDeNuevo(boolean juegaDeNuevo) {
        this.juegaDeNuevo = juegaDeNuevo;
    }
    public boolean CambiaPalo() {
        return cambiaPalo;
    }
    public void setCambiaPalo(boolean cambiaPalo) {
        this.cambiaPalo = cambiaPalo;
    }
    public boolean Comodin() {
        return comodin;
    }
    public void setComodin(boolean comodin) {
        this.comodin = comodin;
    }
    public Jugador getJugadorAfectado() {
        return jugadorAfectado;
    }
    public void setJugadorAfectado(Jugador jugadorAfectado) {
        this.jugadorAfectado = jugadorAfectado;
    }
    public boolean TieneEfecto() {
        return saltaTurno || cambiaSentido || robaCartas > 0 || juegaDeNuevo || cambiaPalo || comodin;
    }
}

