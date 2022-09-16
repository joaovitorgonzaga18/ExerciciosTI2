package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.MedicamentoDAO;
import model.Medicamento;
import spark.Request;
import spark.Response;


public class MedicamentoService {

	private MedicamentoDAO MedicamentoDAO = new MedicamentoDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public MedicamentoService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Medicamento(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Medicamento(), orderBy);
	}

	
	public void makeForm(int tipo, Medicamento Medicamento, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umMedicamento = "";
		if(tipo != FORM_INSERT) {
			umMedicamento += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/Medicamento/list/1\">Novo Medicamento</a></b></font></td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t</table>";
			umMedicamento += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/Medicamento/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Medicamento";
				descricao = "leite, pão, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + Medicamento.getID();
				name = "Atualizar Medicamento (ID " + Medicamento.getID() + ")";
				descricao = Medicamento.getDescricao();
				buttonLabel = "Atualizar";
			}
			umMedicamento += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umMedicamento += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ descricao +"\"></td>";
			umMedicamento += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ Medicamento.getPreco() +"\"></td>";
			umMedicamento += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ Medicamento.getQuantidade() +"\"></td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ Medicamento.getDataFabricacao().toString() + "\"></td>";
			umMedicamento += "\t\t\t<td>Data de validade: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\""+ Medicamento.getDataValidade().toString() + "\"></td>";
			umMedicamento += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t</table>";
			umMedicamento += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umMedicamento += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Medicamento (ID " + Medicamento.getID() + ")</b></font></td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td>&nbsp;Descrição: "+ Medicamento.getDescricao() +"</td>";
			umMedicamento += "\t\t\t<td>Preco: "+ Medicamento.getPreco() +"</td>";
			umMedicamento += "\t\t\t<td>Quantidade: "+ Medicamento.getQuantidade() +"</td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t\t<tr>";
			umMedicamento += "\t\t\t<td>&nbsp;Data de fabricação: "+ Medicamento.getDataFabricacao().toString() + "</td>";
			umMedicamento += "\t\t\t<td>Data de validade: "+ Medicamento.getDataValidade().toString() + "</td>";
			umMedicamento += "\t\t\t<td>&nbsp;</td>";
			umMedicamento += "\t\t</tr>";
			umMedicamento += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-Medicamento>", umMedicamento);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Medicamentos</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/Medicamento/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/Medicamento/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/Medicamento/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Medicamento> Medicamentos;
		if (orderBy == FORM_ORDERBY_ID) {                 	Medicamentos = MedicamentoDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		Medicamentos = MedicamentoDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			Medicamentos = MedicamentoDAO.getOrderByPreco();
		} else {											Medicamentos = MedicamentoDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Medicamento p : Medicamentos) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getDescricao() + "</td>\n" +
            		  "\t<td>" + p.getPreco() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/Medicamento/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/Medicamento/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteMedicamento('" + p.getID() + "', '" + p.getDescricao() + "', '" + p.getPreco() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-Medicamento>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
		LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));
		
		String resp = "";
		
		Medicamento Medicamento = new Medicamento(-1, descricao, preco, quantidade, dataFabricacao, dataValidade);
		
		if(MedicamentoDAO.insert(Medicamento) == true) {
            resp = "Medicamento (" + descricao + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Medicamento (" + descricao + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Medicamento Medicamento = (Medicamento) MedicamentoDAO.get(id);
		
		if (Medicamento != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, Medicamento, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Medicamento " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Medicamento Medicamento = (Medicamento) MedicamentoDAO.get(id);
		
		if (Medicamento != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, Medicamento, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Medicamento " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Medicamento Medicamento = MedicamentoDAO.get(id);
        String resp = "";       

        if (Medicamento != null) {
        	Medicamento.setDescricao(request.queryParams("descricao"));
        	Medicamento.setPreco(Float.parseFloat(request.queryParams("preco")));
        	Medicamento.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	Medicamento.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
        	Medicamento.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));
        	MedicamentoDAO.update(Medicamento);
        	response.status(200); // success
            resp = "Medicamento (ID " + Medicamento.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Medicamento (ID \" + Medicamento.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Medicamento Medicamento = MedicamentoDAO.get(id);
        String resp = "";       

        if (Medicamento != null) {
            MedicamentoDAO.delete(id);
            response.status(200); // success
            resp = "Medicamento (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Medicamento (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}