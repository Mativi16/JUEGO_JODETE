package Modelo;

public class Carta {
    private final int numero;
    private final Palo palo;
    public Carta(int numero, Palo palo) {
        this.numero = numero;
        this.palo = palo;
    }
    public Palo getPalo() {
        return palo;
    }

    public int getNumero() {
        return numero;
    }
    @Override
    public String toString() {
        if (palo == Palo.COMODIN) {
            return "Comodín";
        }
        return numero + " de " + palo;
    }
}
