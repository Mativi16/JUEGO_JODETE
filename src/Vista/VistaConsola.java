package Vista;

import Modelo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {
    private final Scanner sc;
    public VistaConsola() {
        this.sc = new Scanner(System.in);
    }
    public List<Jugador> pedirDatosJugadores() {
        mostrarMensaje("------ BIENVENIDO AL JUEGO 'JODETE' ------");

        int n = pedirCantidadJugadores();

        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            String nombre = pedirNombreJugador(i);
            jugadores.add(new Jugador(nombre));
        }
        return jugadores;
    }

    private int pedirCantidadJugadores() {
        mostrarMensaje("Ingrese cantidad de jugadores a participar(2-5): ");

        int n;
        while (true) {
            try {
                n = Integer.parseInt(sc.nextLine());
                if (n >= 2 && n <= 5) return n;
                mostrarMensaje("Debe ser entre 2 y 5 jugadores. Intente de nuevo: ");
            } catch (NumberFormatException e) {
                mostrarMensaje("Entrada inválida. Intente de nuevo: ");
            }
        }
    }

    private String pedirNombreJugador(int numeroJugador) {
        System.out.print("Nombre del jugador " + numeroJugador + ": ");
        String nombre = sc.nextLine().trim();
        while (nombre.isEmpty()) {
            System.out.print("El nombre no puede estar vacío. Ingrese nuevamente: ");
            nombre = sc.nextLine().trim();
        }
        return nombre;
    }
    public void mostrarCartaSuperior(Carta carta, Palo paloActivo) {
        String mensaje;
        if (carta == null) {
            mensaje = "Carta superior: NINGUNA";
        } else {
            mensaje = "Carta superior: " + carta;
        }
        if (paloActivo != null) {
            mensaje += " (Palo activo: " + paloActivo + ")";

        }
        System.out.println("\n" + mensaje);
    }


    public void mostrarMano(Jugador jugador) {
        System.out.println("\nTurno de " + jugador.getNombre() + ":");
        List<Carta> mano = jugador.getMano();
        for (int i = 0; i < mano.size(); i++) {
            System.out.println((i + 1) + ") " + mano.get(i));
        }
        System.out.println("0) Tomar carta");
    }

    public int pedirCartaAJugar(int cantidadCartas) {
        int opcion = -1;
        boolean valido = false;

        while (!valido) {
            System.out.print("Seleccione número de carta a jugar (0 para tomar): ");
            String entrada = sc.nextLine().trim();

            try {
                opcion = Integer.parseInt(entrada);
                if (opcion < 0 || opcion > cantidadCartas) {
                    System.out.println("Número fuera de rango. Intente nuevamente.");
                } else {
                    valido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
            }
        }
        return opcion;
    }

    public Palo pedirNuevoPalo() {
        System.out.println("Seleccione el nuevo palo:");
        int i = 1;
        Palo[] palosValidos = {Palo.OROS, Palo.COPAS, Palo.ESPADAS, Palo.BASTOS};

        for (Palo p : palosValidos) {
            System.out.println(i + ") " + p);
            i++;
        }

        while (true) {
            System.out.print("Ingrese número de palo: ");
            try {
                int opcion = Integer.parseInt(sc.nextLine());
                if (opcion >= 1 && opcion <= 4) {
                    return palosValidos[opcion - 1];
                }
            } catch (NumberFormatException ignored) {}
            mostrarMensaje("Opción inválida. Intente otra vez.");
        }
    }


    public boolean preguntarJugarCartaTomada(Carta carta, Carta cartaSuperior) {
        mostrarMensaje("Tomaste: " + carta);
        mostrarMensaje("Carta superior actual: " + cartaSuperior);
        return pedirConfirmacion("¿Desea jugarla ahora?");
    }
    public boolean pedirConfirmacion(String pregunta) {
        System.out.print(pregunta + " (s/n): ");
        String r = sc.nextLine().trim().toLowerCase();
        return r.equals("s") || r.equals("si");
    }
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}