package dao;

import model.Medicamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class MedicamentoDAO extends DAO {	
	public MedicamentoDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Medicamento Medicamento) {
		boolean status = false;
		try {
			String sql = "INSERT INTO Medicamento (descricao, preco, quantidade, datafabricacao, datavalidade) "
		               + "VALUES ('" + Medicamento.getDescricao() + "', "
		               + Medicamento.getPreco() + ", " + Medicamento.getQuantidade() + ", ?, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(Medicamento.getDataFabricacao()));
			st.setDate(2, Date.valueOf(Medicamento.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Medicamento get(int id) {
		Medicamento Medicamento = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM Medicamento WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 Medicamento = new Medicamento(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	                				   rs.getInt("quantidade"), 
	        			               rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			               rs.getDate("datavalidade").toLocalDate());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Medicamento;
	}
	
	
	public List<Medicamento> get() {
		return get("");
	}

	
	public List<Medicamento> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Medicamento> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Medicamento> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Medicamento> get(String orderBy) {
		List<Medicamento> Medicamentos = new ArrayList<Medicamento>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM Medicamento" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Medicamento p = new Medicamento(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	        			                rs.getInt("quantidade"),
	        			                rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			                rs.getDate("datavalidade").toLocalDate());
	            Medicamentos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Medicamentos;
	}
	
	
	public boolean update(Medicamento Medicamento) {
		boolean status = false;
		try {  
			String sql = "UPDATE Medicamento SET descricao = '" + Medicamento.getDescricao() + "', "
					   + "preco = " + Medicamento.getPreco() + ", " 
					   + "quantidade = " + Medicamento.getQuantidade() + ","
					   + "datafabricacao = ?, " 
					   + "datavalidade = ? WHERE id = " + Medicamento.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(Medicamento.getDataFabricacao()));
			st.setDate(2, Date.valueOf(Medicamento.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM Medicamento WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}