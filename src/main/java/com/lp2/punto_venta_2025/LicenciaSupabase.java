package com.lp2.punto_venta_2025;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LicenciaSupabase {

    private static final String SUPABASE_URL = "https://rnhkiuwqhkvpmnhuaxdn.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuaGtpdXdxaGt2cG1uaHVheGRuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDkxMjY5NzEsImV4cCI6MjA2NDcwMjk3MX0.2hzB-IWeyw0al1H-7HWuTPk6WJTTE7kmOlT1ledvKmQ";
    private static final String CLIENTE_ID = "cliente001";

    public static boolean verificarLicencia() {
        try {
            String filtro = URLEncoder.encode("eq." + CLIENTE_ID, StandardCharsets.UTF_8.toString());
            String urlStr = SUPABASE_URL + "/rest/v1/clientespos?cliente_id=" + filtro + "&select=activo";

            System.out.println("🔗 URL Final: " + urlStr);

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("apikey", SUPABASE_API_KEY);
            con.setRequestProperty("Authorization", "Bearer " + SUPABASE_API_KEY);
            con.setRequestProperty("Accept", "text/csv");

            int codigoRespuesta = con.getResponseCode();
            if (codigoRespuesta != 200) {
                System.out.println("❌ Error de conexión: código " + codigoRespuesta);
                return false;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea;//linea de datos
            int lineaNro = 0;
            boolean licenciaActiva = false;
            boolean datosEncontrados = false;

            System.out.println(" Respuesta Supabase (CSV):");

            while ((linea = in.readLine()) != null) {
                System.out.println(" Línea " + lineaNro + ": " + linea);
                if (lineaNro == 1) {
                    datosEncontrados = true;
                    licenciaActiva = linea.equals("t") || linea.trim().equalsIgnoreCase("true");//Si la linea 1 es "t" la liecencia esta activa
                }
                lineaNro++;
            }
            in.close();

            if (!datosEncontrados) {
                System.out.println("⚠️ Cliente no encontrado en Supabase.");
                return false;
            }

            System.out.println(" Licencia activa: " + licenciaActiva);
            return licenciaActiva;

        } catch (Exception e) {
            System.out.println("⚠️ Error al verificar licencia: " + e.getMessage());
            return false;
        }
    }
}

