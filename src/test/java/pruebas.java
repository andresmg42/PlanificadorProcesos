

public class pruebas {
    public Object FechaHoraActual() {
        // Capturar la fecha y hora actual
        java.util.Date utilDate = new java.util.Date();  // Captura la fecha y hora actual

        // Convertir a java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // Convierte a java.sql.Date

        // Convertir a java.sql.Time
        java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());  // Convierte a java.sql.Time

        // Imprimir resultados
        return new Object[]{sqlDate,sqlTime};
    }

    public static void main(String[] args) {
      String p="sleep 4";
        System.out.println(p.trim());// Llama a la función para mostrar la fecha y hora actuales
    }
}
