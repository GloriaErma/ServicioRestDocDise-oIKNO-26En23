# ServicioRestDocDise-oIKNO-26En23
Servicio REST, que recibe archivo JSON del cual generara varios archivos CSV.   Documento Diseño Técnico Sistema de Licencias


EJECUCION SERVICIO REST
El servicio Rest se ejecuta en Postman: 
•	POST   http://localhost:8080/api/cargar
•	En el Body, opción form-data se incluye como:
o	Key:  Archivo
o	Value: el nombre del archivo con extensión.

DECK DE PRUEBAS realizadas:
1-	Archivo vacío: vacio.json
2-	Archivo formato diferente a JSON y extensión diferente: scriptDatosPdf.txt
3-	Archivo JSON recibido OK, en el enunciado: B103078.json
4-	Revisión Archivos resultado de la ejecución, en carpeta salidas.
5-	Revisión del contenido de cada archivo.
***** En el nombre de los archivos de salida se incluyó la fecha y hora de ejecución.



RESULTADOS PRUEBAS:
1-	Archivo vacío: vacio.json

2-	Archivo formato diferente a JSON y extensión diferente: scriptDatosPdf.txt
 
3-	Archivo JSON recibido OK, en el enunciado: B103078.json
 
4-	Revisión Archivos resultado de la ejecución, En carpeta salidas:
 
5-	Revisión del contenido de cada archivo.
A la izquierda está el archivo modelo, a la derecha el archivo generado en el proceso.
 
 
 
 
 
 
 
 
 





