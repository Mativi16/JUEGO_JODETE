package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PilaDeDescarte {
    private final Stack<Carta> cartas;
    public PilaDeDescarte() {
        cartas = new Stack<>();
    }
    public void ponerCarta(Carta carta) {
        cartas.push(carta);
    }
    public Carta verUltimaCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.peek();
    }
    public List<Carta> getCartasMenosUltima() {
        List<Carta> lista = new ArrayList<>();
        if (cartas.isEmpty()) return lista;
        // Copiamos todo y sacamos la última
        lista.addAll(cartas);
        Carta ultima = cartas.peek();
        lista.remove(ultima);
        return lista;
    }
    public void reiniciarCon(Carta ultima) {
        cartas.clear();
        if (ultima != null) {
            cartas.push(ultima);
        }
    }
}
