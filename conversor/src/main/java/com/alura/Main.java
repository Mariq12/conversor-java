package com.alura;

public class Main {

    public static void main(String[] args) {
        Conversor conversor = new Conversor();
        conversor.startCurrencyConverter();
    }
}

// USD a ARS = 864.7500 ARS

/*/
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/903b80dd483e81436df2b9c0/latest/USD";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Mostrar el menú de opciones al usuario
        while (true) {
            System.out.println("=== Conversor de Monedas ===");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");

            System.out.print("Ingrese su elección: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    convertCurrency();
                    break;
                case 2:
                    System.out.println("Gracias por usar el Conversor de Monedas. ¡Adiós!");
                    return;
                default:
                    System.out.println("Opción inválida. Inténtelo de nuevo.");
            }
        }
    }

    private static void convertCurrency() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el monto a convertir:");
        double amount = scanner.nextDouble();

        System.out.println("Ingrese la moneda de destino (ejemplo: EUR):");
        String targetCurrency = scanner.next().toUpperCase();

        try {
            // Obtener las tasas de cambio de la API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parsear la respuesta JSON
                JsonObject ratesJson = gson.fromJson(response.body(), JsonObject.class);
                JsonObject rates = ratesJson.getAsJsonObject("conversion_rates");

                // Verificar si la moneda de destino está presente en las tasas
                if (rates.has(targetCurrency)) {
                    double conversionRate = rates.get(targetCurrency).getAsDouble();

                    // Calcular y mostrar el resultado de la conversión
                    double convertedAmount = amount * conversionRate;
                    System.out.printf("%.2f USD equivale a %.2f %s\n", amount, convertedAmount, targetCurrency);
                } else {
                    System.out.println("La moneda especificada no es válida.");
                }
            } else {
                System.out.println("Error al obtener las tasas de cambio. Inténtelo de nuevo más tarde.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
*/