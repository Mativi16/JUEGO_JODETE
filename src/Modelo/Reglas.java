package Modelo;

public class Reglas {
    public boolean puedeJugar(Carta cartaAJugar, Carta cartaSuperior, Palo paloActivo) {
        if (cartaAJugar == null || cartaSuperior == null) return false;
        if (cartaAJugar.getPalo() == Palo.COMODIN) return true;
        if (paloActivo != null && paloActivo != cartaSuperior.getPalo()) {
            return cartaAJugar.getPalo() == paloActivo || cartaAJugar.getNumero() == cartaSuperior.getNumero();
        }
        return cartaAJugar.getPalo() == cartaSuperior.getPalo()
                || cartaAJugar.getNumero() == cartaSuperior.getNumero();
    }
    public ResultadoJugada aplicarEfecto(Carta carta) {
        ResultadoJugada efecto = new ResultadoJugada();
        switch (carta.getNumero()) {
            case 2:
                efecto.setRobaCartas(2);
                break;
            case 4:
            case 7:
                efecto.setJuegaDeNuevo(true);
                break;
            case 10:
                efecto.setCambiaPalo(true);
                break;
            case 11:
                efecto.setSaltaTurno(true);
                break;
            case 12:
                efecto.setCambiaSentido(true);
                break;
            case 0: // cuando es comodin
                efecto.setComodin(true);
                efecto.setRobaCartas(5);
                break;
        }

        return efecto;
    }
}
