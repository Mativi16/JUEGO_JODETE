package Observer;

import Modelo.Evento;
import Modelo.Juego;

public interface Observador {
    void actualizar(Evento evento);
}
