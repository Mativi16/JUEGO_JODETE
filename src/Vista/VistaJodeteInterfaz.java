package Vista;

import Modelo.*;
import Observer.Observador;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VistaJodeteInterfaz {
    public JButton btnTomar, btnJodete;
    private JFrame frame;
    private JLabel lblCartaSuperior;
    private JPanel panelMano;
    private JTextArea log;
    private JLabel lblTurnoActual;
    private Object controlador;


    public VistaJodeteInterfaz(Object controlador) {
        this.controlador = controlador;
        frame = new JFrame("Jodete - GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        JPanel panelNorte = new JPanel(new GridLayout(2, 1));
        lblTurnoActual = new JLabel("Esperando jugadores...", SwingConstants.CENTER);
        lblTurnoActual.setFont(lblTurnoActual.getFont().deriveFont(Font.BOLD, 22f));
        lblTurnoActual.setForeground(new Color(30, 144, 255));
        panelNorte.add(lblTurnoActual);
        lblCartaSuperior = new JLabel("Carta superior: NINGUNA", SwingConstants.CENTER);
        lblCartaSuperior.setFont(lblCartaSuperior.getFont().deriveFont(Font.PLAIN, 16f));
        panelNorte.add(lblCartaSuperior);
        frame.add(panelNorte, BorderLayout.NORTH);
        panelMano = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.add(new JScrollPane(panelMano), BorderLayout.CENTER);
        JPanel panelSur = new JPanel();
        btnTomar = new JButton("Tomar carta");
        btnJodete = new JButton("¡Jodete!");
        panelSur.add(btnTomar);
        panelSur.add(btnJodete);
        frame.add(panelSur, BorderLayout.SOUTH);
        log = new JTextArea(12, 25);
        log.setEditable(false);
        frame.add(new JScrollPane(log), BorderLayout.EAST);
    }


    // =======================================================
    //          GETTERS Y SETTERS
    // =======================================================

    public JFrame getFrame() {
        return frame;
    }
    public JPanel getPanelMano() {
        return panelMano;
    }
    public void setControlador(Object controlador) {
        this.controlador = controlador;
    }


    // =======================================================
    //          MÉTODOS DE PRESENTACIÓN
    // =======================================================
    public void mostrarTurno(String nombreJugador) {
        lblTurnoActual.setText("Turno de: " + nombreJugador.toUpperCase());
    }
    public void mostrarMano(Jugador jugador) {
        panelMano.removeAll();
        List<Carta> mano = jugador.getMano();
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            JButton b = new JButton(carta.toString());
            b.setPreferredSize(new Dimension(140, 180));
            b.setFont(b.getFont().deriveFont(Font.BOLD, 14f));
            b.setActionCommand(String.valueOf(i));
            panelMano.add(b);
        }
        panelMano.revalidate();
        panelMano.repaint();
    }
    public void mostrar() {
        frame.setVisible(true);
    }

    public void mostrarCartaSuperior(Carta carta, Palo paloActivo) {
        if (carta == null) {
            lblCartaSuperior.setText("Carta superior: NINGUNA");
        } else {
            lblCartaSuperior.setText("Carta superior: " + carta.toString() +
                    "   (Palo activo: " + paloActivo + ")");
        }
    }

    public void mostrarMensaje(String msg) {
        log.append(msg + "\n");
    }


    public Palo pedirNuevoPalo() {
        Palo[] opciones = {Palo.OROS, Palo.COPAS, Palo.ESPADAS, Palo.BASTOS};
        String[] labels = {"OROS","COPAS","ESPADAS","BASTOS"};
        int seleccion = JOptionPane.showOptionDialog(
                frame,
                "Seleccione el nuevo palo:",
                "Cambio de palo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                labels,
                labels[0]
        );
        if (seleccion >=0 && seleccion < opciones.length) return opciones[seleccion];
        return opciones[0];
    }



}