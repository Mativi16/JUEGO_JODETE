package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private final List<Carta> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        // 48 cartas + 2 comodines
        for (Palo p : Palo.values()) {
            if (p != Palo.COMODIN) {
                for (int i = 1; i <= 12; i++) {
                    cartas.add(new Carta(i, p));
                }
            }
        }
        cartas.add(new Carta(0, Palo.COMODIN));
        cartas.add(new Carta(0, Palo.COMODIN));
        Collections.shuffle(cartas);
    }
    public Carta tomarCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.remove(0);
    }
    public boolean estaVacio() {
        return cartas.isEmpty();
    }
    // Permite volver a cargar cartas en el mazo al reciclar
    public void agregarCarta(Carta carta) {
        if (carta != null) {
            cartas.add(carta);
        }
    }
}
