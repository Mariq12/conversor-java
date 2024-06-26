package com.alura;

/* Usa JsonParser y JsonObject */
import javax.swing.JOptionPane;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Conversor {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    private static final Map<String, String> monedas = new HashMap<>();

    static {
        monedas.put("ARS", "Peso argentino");
        monedas.put("BOB", "Boliviano boliviano");
        monedas.put("BRL", "Real brasileño");
        monedas.put("CLP", "Peso chileno");
        monedas.put("COP", "Peso colombiano");
        monedas.put("USD", "Dólar estadounidense");
    }

    public void startCurrencyConverter() {
        String monedaOrigen = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la moneda de origen:",
                "Seleccionar Moneda de Origen",
                JOptionPane.PLAIN_MESSAGE,
                null,
                monedas.values().toArray(),
                monedas.values().toArray()[0]);

        if (monedaOrigen == null) {
            return; // Si el usuario cancela la selección
        }

        String monedaDestino = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la moneda de destino:",
                "Seleccionar Moneda de Destino",
                JOptionPane.PLAIN_MESSAGE,
                null,
                monedas.values().toArray(),
                monedas.values().toArray()[0]);

        if (monedaDestino == null) {
            return; // Si el usuario cancela la selección
        }

        String codigoOrigen = obtenerCodigoPorNombre(monedaOrigen);
        String codigoDestino = obtenerCodigoPorNombre(monedaDestino);

        if (codigoOrigen != null && codigoDestino != null) {
            buscarConversion(codigoOrigen, codigoDestino);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: No se pudo encontrar el código para alguna de las monedas seleccionadas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerCodigoPorNombre(String nombre) {
        for (Map.Entry<String, String> entry : monedas.entrySet()) {
            if (entry.getValue().equals(nombre)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void buscarConversion(String codigoOrigen, String codigoDestino) {
        try {
            URL url = new URL(API_URL + "edfc1752376623b9854b8b39/latest/" + codigoOrigen);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

                if (jsonResponse.get("result").getAsString().equals("success")) {
                    double tasaConversion = jsonResponse.getAsJsonObject("conversion_rates").get(codigoDestino).getAsDouble();

                    String montoStr = JOptionPane.showInputDialog(
                            null,
                            "Ingrese el monto a convertir:",
                            "Convertir de " + codigoOrigen + " a " + codigoDestino,
                            JOptionPane.PLAIN_MESSAGE);

                    if (montoStr != null && !montoStr.isEmpty()) {
                        try {
                            double monto = Double.parseDouble(montoStr);
                            double resultado = monto * tasaConversion;

                            JOptionPane.showMessageDialog(
                                    null,
                                    String.format("%.2f %s = %.2f %s", monto, codigoOrigen, resultado, codigoDestino),
                                    "Resultado de la Conversión",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Error: Ingrese un monto válido.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error: No se pudo obtener las tasas de conversión.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Error: No se pudo conectar con la API de tasas de cambio.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de conexión: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Conversor().startCurrencyConverter();
    }
}