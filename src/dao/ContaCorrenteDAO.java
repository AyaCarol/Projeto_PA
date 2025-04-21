package dao;

import java.util.*;
import java.sql.*;
import models.*;
import factory.ConnectionFactory;

public class ContaCorrenteDAO {
    public void create(ContaCorrente conta) throws SQLException {
        // Primeiro verifica se a conta já existe
        String checkSql = "SELECT id FROM contas_bancarias WHERE conta = ?";
        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        ){
            checkStmt.setString(1, conta.getConta());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                throw new SQLException("Já existe uma conta com o número " + conta.getConta());
            }
        }

        String sql = "INSERT INTO contas_bancarias (titular, agencia, conta, saldo, senha, tarifa, rendimento_mensal, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, 'CORRENTE')";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getAgencia());
            stmt.setString(3, conta.getConta());
            stmt.setDouble(4, conta.getSaldo());
            stmt.setString(5, conta.getSenha());
            stmt.setDouble(6, conta.getTarifa());
            stmt.setDouble(7, 0.0); // rendimento não aplicável para corrente

            stmt.executeUpdate();

            // Obtém o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                conta.setId(idGerado);
            }

            System.out.println("Conta Corrente criada com Sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar conta corrente: " + e.getMessage());
            throw e;
        }
    }

    public List<ContaCorrente> read() throws SQLException {
        String sql = "SELECT * FROM contas_bancarias WHERE tipo = 'CORRENTE'";
        List<ContaCorrente> contas = new ArrayList<>();

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                ContaCorrente conta = new ContaCorrente(
                    rs.getString("titular"),
                    rs.getString("agencia"),
                    rs.getString("conta"),
                    rs.getDouble("saldo"),
                    rs.getString("senha"),
                    rs.getDouble("tarifa")
                );
                conta.setId(rs.getInt("id"));
                contas.add(conta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contas;
    }

    public void update(ContaCorrente conta) throws SQLException {
        String sql = "UPDATE contas_bancarias SET titular=?, agencia=?, conta=?, saldo=?, senha=?, tarifa=? WHERE id=? AND tipo='CORRENTE'";
        
        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ){
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getAgencia());
            stmt.setString(3, conta.getConta());
            stmt.setDouble(4, conta.getSaldo());
            stmt.setString(5, conta.getSenha());
            stmt.setDouble(6, conta.getTarifa());
            stmt.setInt(7, conta.getId());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Conta Corrente não encontrada!");
                return;
            }

            System.out.println("Conta Corrente atualizada com Sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ContaCorrente conta) throws SQLException {
        String sql = "DELETE FROM contas_bancarias WHERE id = ? AND tipo='CORRENTE'";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setInt(1, conta.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Conta Corrente não encontrada!");
                return;
            }

            System.out.println("Conta Corrente deletada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
