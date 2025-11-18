package Modelo;

import java.util.*;

public class Jugador {
    private final String nombre;
    private final List<Carta> mano;
    public Jugador(String nombre) {
        this.nombre = nombre;
        mano = new ArrayList<>();
    }
    public void agregarCarta(Carta unaCarta) {
        mano.add(unaCarta);
    }
    public Carta jugarCarta(int indice) {
        if (indice >= 0 && indice < mano.size()) {
            return mano.remove(indice);
        }
        return null;
    }
    public List<Carta> getMano() {
        return mano;
    }
    public boolean sinCartas() {
        return mano.isEmpty();
    }
    public String getNombre() {
        return nombre;
    }
}
