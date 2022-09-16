package app;

import static spark.Spark.*;
import service.MedicamentoService;


public class Aplicacao {
	
	private static MedicamentoService MedicamentoService = new MedicamentoService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/Medicamento/insert", (request, response) -> MedicamentoService.insert(request, response));

        get("/Medicamento/:id", (request, response) -> MedicamentoService.get(request, response));
        
        get("/Medicamento/list/:orderby", (request, response) -> MedicamentoService.getAll(request, response));

        get("/Medicamento/update/:id", (request, response) -> MedicamentoService.getToUpdate(request, response));
        
        post("/Medicamento/update/:id", (request, response) -> MedicamentoService.update(request, response));
           
        get("/Medicamento/delete/:id", (request, response) -> MedicamentoService.delete(request, response));

             
    }
}