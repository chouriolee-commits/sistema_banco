package vista;

import controlador.ColaBanco;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;

public class BancoSwing extends JFrame {

    private static final String ARCHIVO_DATOS = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "clientes.json").toString();

    private final ColaBanco banco;
    private final DefaultTableModel modeloTabla;
    private final JLabel labelTotal;
    private final JTextArea areaMensajes;

    public BancoSwing() {
        super("Sistema de Turnos - Banco");

        banco = new ColaBanco();
        banco.cargarCola(ARCHIVO_DATOS);

        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Cédula", "Tipo", "Hora llegada", "Prioridad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        labelTotal = new JLabel();
        areaMensajes = new JTextArea(4, 40);

        inicializarInterfaz();
        actualizarTabla();
    }

    public static void mostrarVentana() {
        BancoSwing ventana = new BancoSwing();
        ventana.setVisible(true);
    }

    private void inicializarInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JPanel panelAcciones = crearPanelAcciones();
        add(panelAcciones, BorderLayout.WEST);

        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                banco.guardarCola(ARCHIVO_DATOS);
            }
        });
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Acciones"));
        panel.setPreferredSize(new Dimension(210, 0));

        JButton btnAgregar = new JButton("Agregar Cliente");
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregar.addActionListener(e -> abrirDialogoAgregar());

        JButton btnAtender = new JButton("Atender Siguiente");
        btnAtender.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAtender.addActionListener(e -> atenderCliente());

        JButton btnVerProximo = new JButton("Ver Próximo");
        btnVerProximo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerProximo.addActionListener(e -> verProximoCliente());

        JButton btnMostrarFila = new JButton("Mostrar Cola");
        btnMostrarFila.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMostrarFila.addActionListener(e -> mostrarColaCompleta());

        JButton btnVaciar = new JButton("Vaciar Cola");
        btnVaciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVaciar.setForeground(Color.WHITE);
        btnVaciar.setBackground(new Color(220, 40, 40));
        btnVaciar.setOpaque(true);
        btnVaciar.addActionListener(e -> vaciarCola());

        JButton btnSalir = new JButton("Salir");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setBackground(new Color(55, 55, 55));
        btnSalir.setOpaque(true);
        btnSalir.addActionListener(e -> guardarYSalir());

        panel.add(Box.createVerticalStrut(16));
        panel.add(btnAgregar);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnAtender);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnVerProximo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnMostrarFila);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnVaciar);
        panel.add(Box.createVerticalGlue());
        panel.add(btnSalir);
        panel.add(Box.createVerticalStrut(14));

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        JTable tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clientes en espera"));
        return scrollPane;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Información"));

        labelTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelTotal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        areaMensajes.setBackground(new Color(245, 245, 245));
        areaMensajes.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(labelTotal, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaMensajes), BorderLayout.CENTER);
        return panel;
    }

    private void abrirDialogoAgregar() {
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfNombre = new JTextField(18);
        JTextField tfCedula = new JTextField(18);
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Deposito", "Retiro", "Consulta", "Pago"});
        JComboBox<String> cbPrioridad = new JComboBox<>(new String[]{"Normal", "Adulto mayor", "Discapacitado"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(tfNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1;
        formulario.add(tfCedula, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Tipo de transacción:"), gbc);
        gbc.gridx = 1;
        formulario.add(cbTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1;
        formulario.add(cbPrioridad, gbc);

        int result = JOptionPane.showConfirmDialog(this, formulario, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = tfNombre.getText().trim();
                String cedula = tfCedula.getText().trim();
                String tipo = (String) cbTipo.getSelectedItem();
                String prioridad = (String) cbPrioridad.getSelectedItem();

                Cliente cliente = new Cliente(nombre, cedula, tipo, LocalDateTime.now(), prioridad);
                banco.encolar(cliente);
                actualizarTabla();
                mostrarMensaje("Cliente agregado: " + nombre);
            } catch (IllegalArgumentException ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    private void atenderCliente() {
        try {
            Cliente atendido = banco.atenderSiguiente();
            actualizarTabla();
            mostrarMensaje("Atendiendo a: " + atendido.getNombre() + " (" + atendido.getTipoTransaccion() + ")");
        } catch (IllegalStateException e) {
            mostrarError("No hay clientes en espera.");
        }
    }

    private void verProximoCliente() {
        try {
            Cliente proximo = banco.verProximo();
            mostrarMensaje("Próximo cliente: " + proximo.getNombre() + " - " + proximo.getTipoTransaccion());
        } catch (IllegalStateException e) {
            mostrarError("No hay clientes en espera.");
        }
    }

    private void mostrarColaCompleta() {
        try {
            Queue<Cliente> cola = banco.obtenerCola();
            if (cola.isEmpty()) {
                mostrarMensaje("La cola está vacía.");
                return;
            }

            StringBuilder texto = new StringBuilder();
            for (Cliente cliente : cola) {
                texto.append(cliente.toString()).append("\n");
            }

            JTextArea area = new JTextArea(texto.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(700, 380));

            JOptionPane.showMessageDialog(this, scroll, "Clientes en la cola", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException e) {
            mostrarError("No hay clientes en espera.");
        }
    }

    private void vaciarCola() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea vaciar toda la cola?", "Confirmar acción", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            banco.vaciarCola();
            actualizarTabla();
            mostrarMensaje("Cola vaciada correctamente.");
        }
    }

    private void guardarYSalir() {
        banco.guardarCola(ARCHIVO_DATOS);
        dispose();
        System.exit(0);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        Queue<Cliente> cola = banco.obtenerCola();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Cliente cliente : cola) {
            modeloTabla.addRow(new Object[]{
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getTipoTransaccion(),
                cliente.getHoraLlegada().format(formatter),
                cliente.getPrioridad()
            });
        }

        labelTotal.setText("Total de clientes en espera: " + banco.tamano());
    }

    private void mostrarMensaje(String mensaje) {
        areaMensajes.append(mensaje + "\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
