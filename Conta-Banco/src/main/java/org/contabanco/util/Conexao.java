package org.contabanco.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // Configurações do Banco de Dados
    private static final String URL = "jdbc:mysql://localhost:3306/bancoconta"; // Altere a URL do seu BD
    private static final String USUARIO = "root"; // Seu usuário do MySQL
    private static final String SENHA = "Sandra2325"; // Sua senha do MySQL

    /**
     * Retorna uma nova conexão ativa com o banco de dados MySQL.
     * @return Objeto Connection.
     * @throws SQLException Se ocorrer um erro de conexão.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Tenta registrar o driver JDBC (pode ser opcional dependendo da versão do Java/Driver)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Retorna a conexão
            return DriverManager.getConnection(URL, USUARIO, SENHA);

        } catch (ClassNotFoundException e) {
            // Caso o driver não seja encontrado (verifique o pom.xml)
            System.err.println("Erro: Driver JDBC não encontrado.");
            throw new SQLException("Driver JDBC não encontrado.", e);
        }
    }

    // Método para fechar a conexão, se necessário
    public static void fecharConexao(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
