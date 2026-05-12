package vista;

import controlador.ColaBanco;
import modelo.Cliente;

import java.time.LocalDateTime;
import java.util.Scanner;

public class MainConsola {

    static ColaBanco banco = new ColaBanco();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        banco.cargarCola(ColaBanco.ARCHIVO_DATOS);

        int opcion;

        do {

            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            ejecutarOpcion(opcion);

        } while (opcion != 7);

        banco.guardarCola(ColaBanco.ARCHIVO_DATOS);

        sc.close();
    }

    private static void mostrarMenu() {

        System.out.println("\n===== BANCO =====");
        System.out.println("1. Agregar cliente");
        System.out.println("2. Atender siguiente");
        System.out.println("3. Ver próximo");
        System.out.println("4. Mostrar fila");
        System.out.println("5. Cantidad");
        System.out.println("6. Vaciar cola");
        System.out.println("7. Salir");
    }

    private static void ejecutarOpcion(int opcion) {

        switch (opcion) {

            case 1 -> agregarCliente();
            case 2 -> atenderCliente();
            case 3 -> verProximo();
            case 4 -> mostrarFila();
            case 5 -> System.out.println("Clientes: " + banco.tamano());
            case 6 -> banco.vaciarCola();
            case 7 -> System.out.println("Hasta luego");
            default -> System.out.println("Opción inválida");
        }
    }

    private static void agregarCliente() {

        try {

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Identificación: (Solo Números) ");
            String id = sc.nextLine();

            String tipo = leerTipoTransaccion();

            System.out.print("Prioridad: (Normal/Adulto Mayor/Discapacitado) \n");
            String prioridad = sc.nextLine();

            Cliente cliente = new Cliente(
                nombre,
                id,
                tipo,
                LocalDateTime.now(),
                prioridad
            );

            banco.encolar(cliente);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private static String leerTipoTransaccion() {
        System.out.println("Tipo de transacción:");
        System.out.println("1. Deposito");
        System.out.println("2. Retiro");
        System.out.println("3. Consulta");
        System.out.println("4. Pago");

        int opcion;
        do {
            opcion = leerEntero("Seleccione una opción (1-4): ");
            if (opcion < 1 || opcion > 4) {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion < 1 || opcion > 4);

        return switch (opcion) {
            case 1 -> "Deposito";
            case 2 -> "Retiro";
            case 3 -> "Consulta";
            default -> "Pago";
        };
    }

    private static void atenderCliente() {

        try {

            System.out.println(banco.atenderSiguiente());

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private static void verProximo() {

        try {

            System.out.println(banco.verProximo());

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private static void mostrarFila() {

        try {

            banco.mostrarFila();

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private static int leerEntero(String mensaje) {

        System.out.print(mensaje);

        while (!sc.hasNextInt()) {

            System.out.println("Número inválido");
            sc.next();
        }

        int n = sc.nextInt();
        sc.nextLine();

        return n;
    }
}