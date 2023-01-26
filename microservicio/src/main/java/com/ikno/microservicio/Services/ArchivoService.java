package com.ikno.microservicio.Services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoService {

    private String folder = "cargas//";
    private static String folderSal = "salidas//";
    private final Logger logg = LoggerFactory.getLogger(ArchivoService.class);

    public String[] save(MultipartFile file) {
        String[] arrDocumentSave = new String[8];
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(folder + file.getOriginalFilename()); // folder+file
                Files.write(path, bytes);
                logg.info("LOG:: Archivo guardado");
                String nombreArhivo = folder + path.getFileName();
                arrDocumentSave = leerArchivo(nombreArhivo);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();

            }
        } else {
            return null;
        }
        return arrDocumentSave;
    }

    public static String[] leerArchivo(String arch) {
        File archivo = null;
        FileWriter fichero = null;
        PrintWriter pw = null;
        String[] arrDocument = new String[8];

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            // archivo = new File("cargas\\Fj.json");
            archivo = new File(arch);
            int cont = 0;
            int tipo = 0;
            String[] arrCampos = new String[7];
            String linea;

            JSONParser parser = new JSONParser();
            try {
                Object ob = parser.parse(new FileReader(archivo));

                cont++;
                JSONObject js = (JSONObject) ob;
                JSONArray arrObject = (JSONArray) js.get("documents");

                for (int i = 0; i < arrObject.size(); i++) {
                    if (tipo == 0) {
                        Date toDAY = new Date();
                        DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
                        String historial = hourdateFormat.format(toDAY);

                        String archSalida = folderSal + (i + 1) + "-DEPOSIT SLIP-table " + historial + ".csv";
                        arrDocument[i] = archSalida;

                        fichero = new FileWriter(archSalida);
                        pw = new PrintWriter(fichero);
                        salida(0, arrCampos, pw);
                        tipo = 1;
                    }
                    JSONObject objDoc = (JSONObject) arrObject.get(i);
                    String docId = objDoc.get("docId").toString(); // OK
                    JSONObject jsonTable = (JSONObject) objDoc.get("table");

                    JSONArray jsonTableRows = (JSONArray) jsonTable.get("tableRows");

                    // Obtiene elementos de array "Rows".
                    for (int j = 0; j < jsonTableRows.size(); j++) {
                        JSONObject objRows = (JSONObject) jsonTableRows.get(j);
                        JSONArray jsonColumnFields = (JSONArray) objRows.get("columnFields");

                        for (int k = 0; k < jsonColumnFields.size(); k++) {
                            JSONObject objColumn = (JSONObject) jsonColumnFields.get(k);
                            switch (objColumn.get("key").toString()) {
                                case "Date":
                                    arrCampos[0] = (String) objColumn.get("value");
                                    break;
                                case "BT":
                                    arrCampos[1] = (String) objColumn.get("value");
                                    break;
                                case "Prov":
                                    arrCampos[2] = (String) objColumn.get("value");
                                    break;
                                case "Name":
                                    arrCampos[3] = (String) objColumn.get("value");
                                    break;
                                case "Bank":
                                    arrCampos[4] = (String) objColumn.get("value");
                                    break;
                                case "Check#":
                                    arrCampos[5] = (String) objColumn.get("value");
                                    break;
                                case "Amount":
                                    arrCampos[6] = (String) objColumn.get("value");
                                    break;
                                default:
                                    break;
                            }
                        }
                        salida(tipo, arrCampos, pw);
                    }
                    tipo = 0;
                    if (null != fichero)
                        fichero.close();
                }

                System.out.println(" *** FIN *** " + cont); 
            } catch (Exception e) {
                System.out.println("Error controlado en controler rest" + archivo.getClass().getSimpleName() + archivo.length());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return arrDocument;
    }

    public static void salida(int tipo, String[] arrCampos, PrintWriter pw) {
        String lineaSal = "";
        try {
            if (tipo == 0) {
                pw.println("Date,\"BT\",\"Prov\",\"Name\",\"Bank\",\"Check\",\"Amount\" ");
            } else {
                for (int i = 0; i < arrCampos.length; i++) {
                    lineaSal = lineaSal + "\"" + arrCampos[i] + "\"" + ",";
                }
                pw.println(lineaSal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
