
package com.viajafacil.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generarPlanViaje(String consultaUsuario) {
        System.out.println("DEBUG: Iniciando solicitud a Groq (modelo 70B). Clave configurada: " + (groqApiKey != null && !groqApiKey.trim().isEmpty() ? "SÍ" : "NO"));

        if (groqApiKey == null || groqApiKey.trim().isEmpty()) {
            return "{\"error\": \"Clave de API de Groq no configurada en application.properties.\"}";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey.trim());

        // Prompt mejorado para incluir moneda local, USD y PEN
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Eres un experto en planificación de viajes para familias multigeneracionales, incluyendo niños, adultos y adultos mayores (abuelos). Responde EXCLUSIVAMENTE con un JSON válido y completo en español, sin texto extra ni markdown. La estructura debe ser:\n" +
                "{\n" +
                "  \"destino\": \"nombre del destino\",\n" +
                "  \"duracion\": \"X días\",\n" +
                "  \"presupuesto\": \"bajo/medio/alto\",\n" +
                "  \"planes\": [\n" +
                "    {\n" +
                "      \"dia\": 1,\n" +
                "      \"actividades\": [\"Actividades inclusivas y adecuadas para niños, adultos y adultos mayores\"],\n" +
                "      \"alojamiento\": \"alojamiento cómodo y accesible para familias multigeneracionales, con facilidades para niños y adultos mayores\",\n" +
                "      \"comida\": [\"platos típicos del destino con opciones variadas para niños, adultos y adultos mayores\"],\n" +
                "      \"transporte\": \"medios de transporte cómodos y accesibles para todas las edades\",\n" +
                "      \"costoEstimado\": {\n" +
                "        \"monedaDestino\": \"100 EUR\",\n" +
                "        \"USD\": \"110 USD\",\n" +
                "        \"PEN\": \"420 PEN\"\n" +
                "      }\n" +
                "    },\n" +
                "    ...\n" +
                "  ],\n" +
                "  \"totalCosto\": {\n" +
                "    \"monedaDestino\": \"suma aproximada en moneda local\",\n" +
                "    \"USD\": \"suma aproximada en USD\",\n" +
                "    \"PEN\": \"suma aproximada en PEN\"\n" +
                "  }\n" +
                "}\n" +
                "Usa la moneda local del destino para 'monedaDestino' (ejemplo: EUR para París, USD para Nueva York, GBP para Londres). Usa estos tipos de cambio aproximados para convertir a USD y PEN:\n" +
                "- 1 EUR = 1.1 USD\n" +
                "- 1 EUR = 4.2 PEN\n" +
                "- 1 USD = 4.2 PEN\n" +
                "- Si la moneda local es USD, solo convierte a PEN y usa USD igual.\n" +
                "- Si la moneda local no es EUR ni USD, convierte primero a USD (ejemplo: 1 GBP=1.25 USD) y luego a PEN.\n" +
                "Usa SOLO comida típica y tradicional del destino, con opciones variadas y adecuadas para niños, adultos y adultos mayores. Por ejemplo, para Londres usa: \"Fish and Chips\", \"Sunday Roast\", \"Full English Breakfast\" y opciones suaves para adultos mayores y niños. Para París usa: \"Croissants\", \"Quiche\", \"Coq au vin\" y platos familiares variados. Para Bogotá usa: \"Bandeja paisa\", \"Arepas\", \"Ajiaco\" y comidas fáciles de digerir para todas las edades. No incluyas comidas que no correspondan al destino.\n" +
                "Varía el alojamiento por día si es posible, priorizando opciones cómodas y accesibles para familias multigeneracionales. Usa comillas dobles y formato JSON estricto. Si no puedes, responde solo '{\"error\": \"formato inválido\"}'.");
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", consultaUsuario);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");
        requestBody.put("messages", Arrays.asList(systemMessage, userMessage));
        requestBody.put("max_tokens", 1200);
        requestBody.put("temperature", 0.5);

        System.out.println("DEBUG: Enviando request a Groq (70B). Mensaje: " + consultaUsuario);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GROQ_URL, entity, String.class);

            System.out.println("DEBUG: Status Code de Groq: " + response.getStatusCode());
            String fullResponse = response.getBody();
            System.out.println("DEBUG: Respuesta raw (primeros 300 chars): " + fullResponse.substring(0, Math.min(300, fullResponse.length())) + "...");

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(fullResponse);
                JsonNode choicesNode = rootNode.path("choices");
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode messageNode = choicesNode.get(0).path("message");
                    JsonNode contentNode = messageNode.path("content");
                    if (!contentNode.isMissingNode() && !contentNode.asText().trim().isEmpty()) {
                        String content = contentNode.asText().trim();
                        System.out.println("DEBUG: Contenido extraído completo de Groq (longitud: " + content.length() + "): " + content.substring(0, Math.min(200, content.length())) + "...");
                        content = content.replaceAll("\\\\\"", "\"").replaceAll("\\\\n", " ").trim();
                        return content;
                    } else {
                        System.out.println("DEBUG: No se encontró 'content' en la respuesta.");
                        return "{\"error\": \"Respuesta de Groq vacía o sin contenido válido.\"}";
                    }
                } else {
                    System.out.println("DEBUG: No hay 'choices' en la respuesta de Groq.");
                    return "{\"error\": \"Formato de respuesta de Groq inválido. Raw: " + fullResponse.substring(0, 100) + "...\"}";
                }
            } else {
                return "{\"error\": \"Groq respondió con status " + response.getStatusCode() + ": " + fullResponse + "\"}";
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Error en Groq (incluyendo parsing): " + e.getMessage());
            e.printStackTrace();
            return "{\"error\": \"Error de conexión o parsing en Groq: " + e.getMessage() + "\"}";
        }
    }
}