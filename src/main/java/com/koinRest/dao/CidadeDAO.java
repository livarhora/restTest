package com.koinRest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koinRest.bd.Conexao;
import com.koinRest.entity.Cidade;

public class CidadeDAO {
	public Cidade buscarCidade(int id) {
		Cidade user = new Cidade();
	        try {
	            ResultSet rs = Conexao.getInstance().createStatement().executeQuery("select * from cidades where id = " + id);
	            while (rs.next()) {
	                user.setId(rs.getInt("id"));
	                user.setNome(rs.getString("nome"));
	                user.setLatitude(rs.getString("latitude"));
	                user.setLongitude(rs.getString("longitude"));
	            }
	
	        } catch (SQLException ex) {
	            System.out.println("Erro retornando cidade");
	            return null;
	        }
	        return user;
	    }
	
	public List<Cidade> buscarTodas() {
        List<Cidade> lista = new ArrayList<Cidade>();
        try {
            ResultSet rs = Conexao.getInstance().createStatement().executeQuery("select * from cidades");
            while (rs.next()) {
            	Cidade user = new Cidade();
                user.setId(rs.getInt("id"));
                user.setNome(rs.getString("nome"));
                user.setLatitude(rs.getString("latitude"));
                user.setLongitude(rs.getString("longitude"));
                lista.add(user);
            }

        } catch (SQLException ex) {
            System.out.println("Erro retornando lista completa de cidades");
            return null;
        }
        return lista;
    }
	
}

