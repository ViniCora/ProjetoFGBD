/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.fgbd;

/**
 *
 * @author vinicius.brusamolin
 */
public class DBUtils {
    public static boolean verificaCriaTabela(String tabelaName){
        return true;
    }
    
    public static boolean verificaExisteTabela(){
        return false;
    }
    
    public static void conexaoBancoDeDados(){
        
    }
    
    public static void insereDadosTabela(){
        conexaoBancoDeDados();
    }
    
    public static void criaNovaTabelaBanco(String[] nomesColunas, String name, String[] colunas){
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(String.format("CREATE TABLE %s ( ", name.split(".csv")[0]));
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
        
        conexaoBancoDeDados();
        
    }
    
    public static String verificaTipoObjeto(String coluna){
        boolean isNumeric = coluna.chars().allMatch( Character::isDigit );
        
        if(isNumeric){
            return "int";
        }
        if(coluna.length() <= 50){
            return "VARCHAR(50)";
        }else if(coluna.length() <= 100){
            return "VARCHAR(100)";
        }
       return "VARCHAR(255)";
    }
}
