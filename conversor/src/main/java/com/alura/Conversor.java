package com.alura;

import javax.swing.JOptionPane;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Conversor {

    // https://v6.exchangerate-api.com/v6/edfc1752376623b9854b8b39/latest/USD

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    private static final Map<String, String> monedas = new HashMap<>();

    static {
        monedas.put("ARS", "Peso argentino (ARS)");
        monedas.put("BOB", "Boliviano boliviano (BOB)");
        monedas.put("BRL", "Real brasileño (BRL)");
        monedas.put("CLP", "Peso chileno (CLP)");
        monedas.put("COP", "Peso colombiano (COP)");
        monedas.put("USD", "Dólar estadounidense (USD)");
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
                JsonObject jsonResponse = new Gson().fromJson(reader, JsonObject.class);

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





/*
import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Conversor {

    private static final Map<String, String> monedas = new HashMap<>();

    static {
        // Definir los códigos de moneda y su descripción
        monedas.put("ARS", "Peso argentino");
        monedas.put("BOB", "Boliviano boliviano");
        monedas.put("BRL", "Real brasileño");
        monedas.put("CLP", "Peso chileno");
        monedas.put("COP", "Peso colombiano");
        monedas.put("USD", "Dólar estadounidense");
    }

    public void startCurrencyConverter() {
        String[] monedaNombres = monedas.values().toArray(new String[0]);

        String monedaNombreOrigen = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la moneda de origen:",
                "Seleccionar Moneda de Origen",
                JOptionPane.PLAIN_MESSAGE,
                null,
                monedaNombres,
                monedaNombres[0]);

        if (monedaNombreOrigen == null) {
            return; // Si el usuario cancela la selección
        }

        String monedaCodigoOrigen = buscarCodigoPorNombre(monedaNombreOrigen);

        String monedaNombreDestino = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la moneda de destino:",
                "Seleccionar Moneda de Destino",
                JOptionPane.PLAIN_MESSAGE,
                null,
                monedaNombres,
                monedaNombres[0]);

        if (monedaNombreDestino == null) {
            return; // Si el usuario cancela la selección
        }

        String monedaCodigoDestino = buscarCodigoPorNombre(monedaNombreDestino);

        if (monedaCodigoOrigen != null && monedaCodigoDestino != null) {
            buscarConversiones(monedaCodigoOrigen, monedaCodigoDestino);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "No se encontró el código para alguna de las monedas seleccionadas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String buscarCodigoPorNombre(String monedaNombre) {
        for (Map.Entry<String, String> entry : monedas.entrySet()) {
            if (entry.getValue().equals(monedaNombre)) {
                return entry.getKey();
            }
        }
        return null; // No se encontró el código para el nombre de la moneda
    }

    private void buscarConversiones(String monedaCodigoOrigen, String monedaCodigoDestino) {
        try {
            String apiUrl = "https://v6.exchangerate-api.com/v6/903b80dd483e81436df2b9c0/latest/" + monedaCodigoOrigen;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String jsonResponse = new String(connection.getInputStream().readAllBytes());

                // Aquí puedes parsear la respuesta JSON para obtener las tasas de conversión
                System.out.println(jsonResponse); // Mostrar la respuesta JSON en la consola (puedes manejarla como desees)

                // Puedes procesar la respuesta JSON aquí para obtener la tasa de conversión de moneda
                JOptionPane.showMessageDialog(
                        null,
                        "Consulta exitosa para convertir de " + monedaCodigoOrigen + " a " + monedaCodigoDestino,
                        "Consulta Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo obtener las tasas de conversión.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error al conectar con la API de tasas de cambio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
*/


/*import javax.swing.JOptionPane;

public class Conversor {

    private static final double USD_TO_EUR_RATE = 0.85;
    private static final double USD_TO_GBP_RATE = 0.73;

    public void startCurrencyConverter() {
        String[] options = {"USD to EUR", "USD to GBP", "Quit"};

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Select conversion option:",
                    "Currency Converter",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                convertUSDToEUR();
            } else if (choice == 1) {
                convertUSDToGBP();
            } else if (choice == 2) {
                break; // Quit the program
            }
        }
    }

    private void convertUSDToEUR() {
        String usdInput = JOptionPane.showInputDialog("Enter USD amount:");
        if (usdInput == null) return; // User clicked Cancel

        try {
            double usdAmount = Double.parseDouble(usdInput);
            double eurAmount = usdAmount * USD_TO_EUR_RATE;
            JOptionPane.showMessageDialog(
                    null,
                    String.format("%.2f USD = %.2f EUR", usdAmount, eurAmount),
                    "Conversion Result",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid input. Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertUSDToGBP() {
        String usdInput = JOptionPane.showInputDialog("Enter USD amount:");
        if (usdInput == null) return; // User clicked Cancel

        try {
            double usdAmount = Double.parseDouble(usdInput);
            double gbpAmount = usdAmount * USD_TO_GBP_RATE;
            JOptionPane.showMessageDialog(
                    null,
                    String.format("%.2f USD = %.2f GBP", usdAmount, gbpAmount),
                    "Conversion Result",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid input. Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

*/