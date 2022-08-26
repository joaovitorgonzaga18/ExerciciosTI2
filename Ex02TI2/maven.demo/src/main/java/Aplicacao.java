

import java.util.List;

public class Aplicacao {
	
	public static void main(String[] args) throws Exception {
		
		XDAO XDAO = new XDAO();
		
		System.out.println("\n\n==== Inserir usuário === ");
		X X = new X(11, "pablo", "pablo",'M');
		if(XDAO.insert(X) == true) {
			System.out.println("Inserção com sucesso -> " + X.toString());
		}
		
		System.out.println("\n\n==== Testando autenticação ===");
		System.out.println("Usuário (" + X.getLogin() + "): " + XDAO.autenticar("pablo", "pablo"));
			
		System.out.println("\n\n==== Mostrar usuários do sexo masculino === ");
		List<X> Xs = XDAO.getSexoMasculino();
		for (X u: Xs) {
			System.out.println(u.toString());
		}

		System.out.println("\n\n==== Atualizar senha (código (" + X.getCodigo() + ") === ");
		X.setSenha(DAO.toMD5("pablo"));
		XDAO.update(X);
		
		System.out.println("\n\n==== Testando autenticação ===");
		System.out.println("Usuário (" + X.getLogin() + "): " + XDAO.autenticar("pablo", DAO.toMD5("pablo")));		
		
		System.out.println("\n\n==== Invadir usando SQL Injection ===");
		System.out.println("Usuário (" + X.getLogin() + "): " + XDAO.autenticar("pablo", "x' OR 'x' LIKE 'x"));

		System.out.println("\n\n==== Mostrar usuários ordenados por código === ");
		Xs = XDAO.getOrderByCodigo();
		for (X u: Xs) {
			System.out.println(u.toString());
		}
		
		System.out.println("\n\n==== Excluir usuário (código " + X.getCodigo() + ") === ");
		XDAO.delete(X.getCodigo());
		
		System.out.println("\n\n==== Mostrar usuários ordenados por login === ");
		Xs = XDAO.getOrderByLogin();
		for (X u: Xs) {
			System.out.println(u.toString());
		}
	}
}