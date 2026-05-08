package controlador;

import java.util.LinkedList;
import java.util.Queue;
import modelo.Cliente;

public class ColaBanco {

    private Queue<Cliente> cola;

    public ColaBanco() {
        this.cola = new LinkedList<>();
    }

    public void encolar(Cliente cliente) {
        if (cliente == null)
            throw new IllegalArgumentException("No se puede encolar un cliente nulo");
        cola.add(cliente);
        System.out.println(" Cliente agregado: " + cliente.getNombre());
    }

    public Cliente atenderSiguiente() {
        validarVacio();
        return cola.poll();
    }

    public Cliente verProximo() {
        validarVacio();
        return cola.peek();
    }

    public void mostrarFila() {
        validarVacio();
        System.out.println("\n--- Clientes en espera ---");
        System.out.printf("| %-20s | %-12s | %-10s | %-16s | %-8s |%n",
            "Nombre", "Cédula", "Tipo", "Hora llegada", "Prioridad");
        System.out.println("-".repeat(80));
        for (Cliente c : cola) {
            System.out.println(c);
        }
        System.out.println("-".repeat(80));
    }

    public int tamano() {
        return cola.size();
    }

    public void vaciarCola() {
        cola.clear();
        System.out.println(" Cola vaciada por emergencia.");
    }

    private void validarVacio() {
        if (cola.isEmpty())
            throw new IllegalStateException(" La cola está vacía");
    }
}
