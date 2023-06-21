/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.fgbd;

/**
 *
 * @author Usuario
 */
public class ValoresConexaoBanco {
    public static String url;
    public static String database;
    public static String usuario;
    public static String senha;
    
    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ValoresConexaoBanco.url = url;
    }

    public static String getDatabase() {
        return database;
    }

    public static void setDatabase(String database) {
        ValoresConexaoBanco.database = database;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        ValoresConexaoBanco.usuario = usuario;
    }

    public static String getSenha() {
        return senha;
    }

    public static void setSenha(String senha) {
        ValoresConexaoBanco.senha = senha;
    }  
}
