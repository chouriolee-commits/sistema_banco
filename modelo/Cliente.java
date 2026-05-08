package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Cliente {

    private static final List<String> TIPOS_VALIDOS = List.of("Deposito", "Retiro", "Consulta", "Pago");

    private String nombre;
    private String identificacion;
    private String tipoTransaccion;
    private LocalDateTime horaLlegada;
    private String prioridad;

    public Cliente(String nombre, String identificacion, String tipoTransaccion,
                   LocalDateTime horaLlegada, String prioridad) {
        this.nombre = validarNombre(nombre);
        this.identificacion = validarIdentificacion(identificacion);
        this.tipoTransaccion = validarTipoTransaccion(tipoTransaccion);
        this.horaLlegada = validarHoraLlegada(horaLlegada);
        this.prioridad = validarPrioridad(prioridad);
    }

    public String getNombre()             { return nombre; }
    public String getIdentificacion()     { return identificacion; }
    public String getTipoTransaccion()    { return tipoTransaccion; }
    public LocalDateTime getHoraLlegada() { return horaLlegada; }
    public String getPrioridad()          { return prioridad; }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        return nombre.trim();
    }

    private String validarIdentificacion(String id) {
        if (id == null || !id.trim().matches("\\d+"))
            throw new IllegalArgumentException("Identificacion invalida: solo digitos");
        return id.trim();
    }

    private String validarTipoTransaccion(String tipo) {
        if (tipo == null || !TIPOS_VALIDOS.contains(tipo.trim()))
            throw new IllegalArgumentException("Tipo invalido. Use: Deposito, Retiro, Consulta o Pago");
        return tipo.trim();
    }

    private LocalDateTime validarHoraLlegada(LocalDateTime hora) {
        if (hora == null)
            throw new IllegalArgumentException("La hora de llegada no puede ser nula");
        return hora;
    }

    private String validarPrioridad(String prioridad) {
        if (prioridad == null || prioridad.trim().isEmpty())
            return "Normal";
        return prioridad.trim();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format(
            "| %-20s | %-12s | %-10s | %-16s | %-8s |",
            nombre, identificacion, tipoTransaccion,
            horaLlegada.format(fmt), prioridad
        );
    }
}