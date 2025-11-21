package org.contabanco.dao;

import org.contabanco.model.ContaBanco;
import org.contabanco.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaDAO {
    /**
     * Salva uma nova conta no banco de dados.
     * @param conta Objeto ContaBanco a ser persistido.
     */
    public void salvar(ContaBanco conta) {
        // SQL para inserir uma nova conta
        String sql = "INSERT INTO conta_banco (numConta, tipo, dono, saldo, status) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.getConnection();
            stmt = conn.prepareStatement(sql);

            // Seta os valores nos placeholders (?)
            stmt.setInt(1, conta.getNumconta());
            stmt.setString(2, conta.getTipo());
            stmt.setString(3, conta.getDono());
            stmt.setFloat(4, conta.getSaldo());
            stmt.setBoolean(5, conta.isStatus());

            stmt.execute();
            System.out.println("DAO: Conta " + conta.getNumconta() + " salva com sucesso no BD.");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar conta: " + e.getMessage());
        } finally {
            // Garantir que a conexão e o statement sejam fechados
            Conexao.fecharConexao(conn);
        }
    }

    /**
     * Atualiza apenas o saldo da conta (usado para depositar, sacar, mensalidade).
     * @param conta Objeto ContaBanco com o novo saldo e o numConta.
     */
    public void atualizarSaldo(ContaBanco conta) {
        // SQL para atualizar apenas o saldo
        String sql = "UPDATE conta_banco SET saldo = ? WHERE numConta = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setFloat(1, conta.getSaldo());
            stmt.setInt(2, conta.getNumconta());

            stmt.executeUpdate();
            System.out.println("DAO: Saldo da Conta " + conta.getNumconta() + " atualizado.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar saldo: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn);
        }
    }

    /**
     * Busca uma conta no banco de dados pelo número.
     * @param numConta Número da conta a ser buscada.
     * @return Objeto ContaBanco preenchido, ou null se não for encontrado.
     */
    public ContaBanco buscarConta(int numConta) {
        String sql = "SELECT * FROM conta_banco WHERE numConta = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ContaBanco conta = null;

        try {
            conn = Conexao.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numConta);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Mapeia os dados do ResultSet para o objeto ContaBanco
                conta = new ContaBanco();
                conta.setNumconta(rs.getInt("numConta"));
                conta.setTipo(rs.getString("tipo"));
                conta.setDono(rs.getString("dono"));
                conta.setSaldo(rs.getFloat("saldo"));
                conta.setStatus(rs.getBoolean("status"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar conta: " + e.getMessage());
        } finally {
            // Fechar recursos (ResultSet, Statement, Connection)
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexao.fecharConexao(conn);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
        return conta;
    }
}
