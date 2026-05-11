package src.java.controlador;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import src.java.modelo.Cliente;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ColaBanco {

    private Queue<Cliente> cola;
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

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

    // Métodos para persistencia con Gson
    public void guardarCola(String archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(cola, writer);
            System.out.println(" Cola guardada en " + archivo);
        } catch (IOException e) {
            System.err.println("Error al guardar la cola: " + e.getMessage());
        }
    }

    public void cargarCola(String archivo) {
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoCola = new TypeToken<Queue<Cliente>>(){}.getType();
            Queue<Cliente> colaCargada = gson.fromJson(reader, tipoCola);
            if (colaCargada != null) {
                cola = colaCargada;
                System.out.println(" Cola cargada desde " + archivo);
            } else {
                System.out.println(" Archivo vacío, iniciando cola nueva.");
            }
        } catch (IOException e) {
            System.out.println(" Archivo no encontrado, iniciando cola nueva.");
        }
    }

    private void validarVacio() {
        if (cola.isEmpty())
            throw new IllegalStateException(" La cola está vacía");
    }

    // Adaptador para LocalDateTime
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}
