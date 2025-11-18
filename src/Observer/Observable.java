package Observer;

import Modelo.Evento;

public interface Observable {
    void agregarObservador(Observador observador);
    void quitarObservador(Observador observador);
    void notificarObservadores(Evento evento);
}

