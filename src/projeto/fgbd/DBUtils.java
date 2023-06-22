/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.fgbd;
/**
 *
 * @author vinicius.brusamolin
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DBUtils {
    public static boolean verificaCriaTabela(String tabelaName) throws ClassNotFoundException, SQLException{
        return !verificaExisteTabela(tabelaName);
    }
    
    public static boolean verificaExisteTabela(String tabelaName) throws ClassNotFoundException, SQLException{
        boolean criarTabela = true;
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = N?");
        try(Connection conn = conexaoBancoDeDados()){
            try(PreparedStatement ps = conn.prepareStatement(sbSQL.toString())){
                ps.setString(1, tabelaName);
                
                ResultSet rs = ps.executeQuery();
                
                if(rs.next()){
                    System.out.println("Tabela: " + rs.getString("TABLE_NAME"));
                    criarTabela = false;
                }
            }
        }
        System.out.println("Criar Tabela: " + criarTabela);
        return criarTabela;
    }
    
    public static Connection conexaoBancoDeDados() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        if( ValoresConexaoBanco.database == null ||  ValoresConexaoBanco.url == null ||  
                    ValoresConexaoBanco.usuario == null ||  ValoresConexaoBanco.senha == null){
            return null;
        }else{
            Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", ValoresConexaoBanco.url, ValoresConexaoBanco.database), 
                ValoresConexaoBanco.usuario, ValoresConexaoBanco.senha);
            conn.setAutoCommit(true);
            return conn;
        }
        
    }
    
    public static void insereDadosTabela(String[] colunasNomes, String[] colunas, String nomeTabela) throws ClassNotFoundException, SQLException{

        StringBuilder query = montarInsertQuery(colunasNomes, nomeTabela);
        System.out.println("Query: " + query.toString());
        try(Connection conn = conexaoBancoDeDados()){
            try (PreparedStatement ps = conn.prepareStatement(query.toString())){
                int i = 1;
                for(String coluna : colunas){
                    System.out.println(coluna);
                    if(verificaIntObjeto(coluna)){
                         ps.setInt(i, Integer.valueOf(coluna));
                    }else{
                         ps.setString(i, coluna);
                    }
                    i++;
                }
                int rs = ps.executeUpdate();
                System.out.println(rs);
            }
        }
    }

    public static StringBuilder montarInsertQuery(String[] colunas, String nomeTabela){
        
        StringBuilder sbSQL = new StringBuilder();
        StringBuilder sbSQLValues = new StringBuilder();
        sbSQL.append(String.format("INSERT INTO %s(", nomeTabela));
        sbSQLValues.append(" values(");
        int i = 1;
        for(String coluna: colunas){
            sbSQL.append(String.format(" %s", coluna));
            sbSQLValues.append("?");
            if(i < colunas.length){
                sbSQL.append(",");
                sbSQLValues.append(",");
            }else{
                sbSQL.append(" )");
                sbSQLValues.append(");");
            }
            i++;
        }
        return sbSQL.append(sbSQLValues.toString());

    }
    
    public static void criaNovaTabelaBanco(String[] nomesColunas, String name, String[] colunas) throws ClassNotFoundException, SQLException{
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(String.format("CREATE TABLE %s ( ", name));
        int i = 1;
        for(String coluna : nomesColunas){
            sbSQL.append(String.format(" %s %s", coluna, verificaTipoObjeto(colunas[i-1])));
            if(i < colunas.length){
                sbSQL.append(",");
            }else{
                sbSQL.append(" );");
            }
            i++;
        }
        
        System.out.println(sbSQL.toString());
        
        try(Connection conn = conexaoBancoDeDados()){
            try (Statement stmt = conn.createStatement()) {
                int j = stmt.executeUpdate(sbSQL.toString());
                System.out.println(j);
            }catch(SQLException e){
                //TODO: logger
            }
        }
    }
    
    public static String verificaTipoObjeto(String coluna){
        boolean isNumeric = coluna.chars().allMatch( Character::isDigit );
        
        if(isNumeric){
            return "int";
        }
        if(coluna.length() <= 500){
            return "VARCHAR(500)";
        }else if(coluna.length() <= 1000){
            return "VARCHAR(1000)";
        }
       return "VARCHAR(5000)";
    }
    
    public static boolean verificaIntObjeto(String coluna){
       boolean isNumeric = coluna.chars().allMatch( Character::isDigit );
        
       return isNumeric;
    }
    
    public static StringBuilder efetivarBuscaConsulta(StringBuilder query) throws ClassNotFoundException, SQLException{
        StringBuilder retorno = new StringBuilder();
        boolean primeiraIteracao = true;
        try(Connection conn = conexaoBancoDeDados()){
             try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query.toString());
                while(rs.next()){
                    
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnSize = rsmd.getColumnCount();
                    if(primeiraIteracao){
                        for(int i = 1; i <= columnSize; i++){
                            retorno.append(String.format("---------%s--------",rsmd.getColumnName(i)));
                        }
                        primeiraIteracao = false;
                        retorno.append("\n\n");
                    }
                    for(int i = 1; i <= columnSize; i++){
                        retorno.append(String.format("---------%s--------",rs.getObject(i)));
                    }
                    retorno.append("\n\n");
                    
                }
            }
        }
        return retorno;
    }
}
