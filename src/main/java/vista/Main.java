package vista;

import javax.swing.SwingUtilities;

import controlador.ColaBanco;
import modelo.Cliente;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    static ColaBanco banco = new ColaBanco();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ejecutarInterfazSwing();
    }

    public static void ejecutarInterfazSwing() {
        SwingUtilities.invokeLater(BancoSwing::mostrarVentana);
    }

    private static void cargarDatosConsola() {
        banco.cargarCola("src/main/resources/clientes.json");
    }

    private static void guardarDatosConsola() {
        banco.guardarCola("src/main/resources/clientes.json");
    }

    private static void mostrarMenu() {
        System.out.println("\n==============================");
        System.out.println("   BANCO - Sistema de Turnos  ");
        System.out.println("==============================");
        System.out.println("1. Agregar cliente");
        System.out.println("2. Atender siguiente");
        System.out.println("3. Ver proximo en espera");
        System.out.println("4. Mostrar toda la fila");
        System.out.println("5. Clientes en espera");
        System.out.println("6. Vaciar cola (emergencia)");
        System.out.println("7. Salir");
        System.out.println("==============================");
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> agregarCliente();
            case 2 -> atenderCliente();
            case 3 -> verProximo();
            case 4 -> mostrarFila();
            case 5 -> System.out.println("Clientes en espera: " + banco.tamano());
            case 6 -> banco.vaciarCola();
            case 7 -> System.out.println("Hasta luego.");
            default -> System.out.println("Opcion invalida, intente de nuevo.");
        }
    }

    private static void agregarCliente() {
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Identificacion (solo numeros): ");
            String id = sc.nextLine();

            System.out.println("Tipo de transaccion:");
            System.out.println("  1. Deposito  2. Retiro  3. Consulta  4. Pago");
            int tipoNum = leerEntero("Opcion: ");
            String tipo = switch (tipoNum) {
                case 1 -> "Deposito";
                case 2 -> "Retiro";
                case 3 -> "Consulta";
                case 4 -> "Pago";
                default -> throw new IllegalArgumentException("Tipo invalido");
            };

            System.out.print("Prioridad (Normal / Adulto mayor / Discapacitado): ");
            String prioridad = sc.nextLine();

            Cliente c = new Cliente(nombre, id, tipo, LocalDateTime.now(), prioridad);
            banco.encolar(c);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void atenderCliente() {
        try {
            Cliente atendido = banco.atenderSiguiente();
            System.out.println("\nAtendiendo a:");
            System.out.println(atendido);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void verProximo() {
        try {
            System.out.println("\nProximo cliente:");
            System.out.println(banco.verProximo());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void mostrarFila() {
        try {
            banco.mostrarFila();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            System.out.println("Ingrese un numero valido.");
            sc.next();
            System.out.print(mensaje);
        }
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }
}
