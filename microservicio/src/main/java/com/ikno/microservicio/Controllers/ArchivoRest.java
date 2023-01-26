package com.ikno.microservicio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ikno.microservicio.Services.ArchivoService;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.*;

@RestController
@RequestMapping(path = "api", produces = "aplicacion/json")
public class ArchivoRest {

    @Autowired
    private ArchivoService service;

    @GetMapping("/hola")
    ResponseEntity<String> holaMundo() {
        return ResponseEntity.ok().body("Hola Mundo desde una respuesta HTTP!");
    }


    @PostMapping("/cargar")
    public ResponseEntity<?> carga(@RequestParam("Archivo") MultipartFile archivo) {
        String msg = " ";
        int status = 0;
        List<String> list = new ArrayList<>();
        
        if (null == archivo.getOriginalFilename() || archivo.isEmpty() || archivo.getSize() == 0) {
            msg = "Error! " + archivo.getName() + ": " + archivo.getOriginalFilename()
                    + ".  Esta vacio NO se puede para cargar!!!";
            status = 111;
        } else {
            String[] arrCarga = service.save(archivo);
            if (arrCarga != null) {
                for (String i : arrCarga) {
                    if (i == null) {
                        msg = "Error! " + archivo.getName() + ": " + archivo.getOriginalFilename()
                                + ".  Esta cargado!!! Pero NO se puede procesar.";
                        status = 333;
                    }
                    System.out.println(i);
                    list.add(i);
                }
            } else {
                msg = "Error! " + archivo.getName() + ": " + archivo.getOriginalFilename()
                        + ".  Esta cargado!!! Pero NO se puede procesar.";
                status = 222;
            }
        }

        if (msg==" " ) {
            msg = "Transacción exitosa!!!";
        }
        Date toDAY = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fecha = hourdateFormat.format(toDAY);
        HashMap<String, Object> detalle = new HashMap<String, Object>();
        detalle.put("Codigo Estado", status);
        detalle.put("Mensaje", msg);
        detalle.put("Resultado", list);
        detalle.put("Fecha", fecha);
        detalle.entrySet().stream().sorted(Map.Entry.comparingByKey());

        JSONObject resp = new JSONObject(detalle);

        return new ResponseEntity<String>(resp.toString(), HttpStatus.CREATED);

    }

}
