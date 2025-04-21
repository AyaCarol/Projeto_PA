package dao;

import java.util.*;
import java.sql.*;
import models.*;
import factory.ConnectionFactory;

public class ContaPoupancaDAO {
    public void create(ContaPoupanca conta) throws SQLException {
        String sql = "INSERT INTO contas_bancarias (titular, agencia, conta, saldo, senha, tarifa, rendimento_mensal, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, 'POUPANCA')";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getAgencia());
            stmt.setString(3, conta.getConta());
            stmt.setDouble(4, conta.getSaldo());
            stmt.setString(5, conta.getSenha());
            stmt.setDouble(6, 0.0); // tarifa não aplicável para poupança
            stmt.setDouble(7, conta.getRendimentoMensal());

            stmt.executeUpdate();

            // Obtém o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                conta.setId(idGerado);
                System.out.println("ID gerado para a conta: " + idGerado);
            }

            System.out.println("Conta Poupança criada com Sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Propaga a exceção para tratamento adequado
        }
    }

    public List<ContaPoupanca> read() throws SQLException {
        String sql = "SELECT * FROM contas_bancarias WHERE tipo = 'POUPANCA'";
        List<ContaPoupanca> contas = new ArrayList<>();

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                ContaPoupanca conta = new ContaPoupanca(
                    rs.getString("titular"),
                    rs.getString("agencia"),
                    rs.getString("conta"),
                    rs.getDouble("saldo"),
                    rs.getString("senha"),
                    rs.getDouble("rendimento_mensal")
                );
                conta.setId(rs.getInt("id"));
                contas.add(conta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contas;
    }

    public void update(ContaPoupanca conta) throws SQLException {
        String sql = "UPDATE contas_bancarias SET titular=?, agencia=?, conta=?, saldo=?, senha=?, rendimento_mensal=? WHERE id=? AND tipo='POUPANCA'";
        
        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getAgencia());
            stmt.setString(3, conta.getConta());
            stmt.setDouble(4, conta.getSaldo());
            stmt.setString(5, conta.getSenha());
            stmt.setDouble(6, conta.getRendimentoMensal());
            stmt.setInt(7, conta.getId());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Conta Poupança não encontrada!");
                return;
            }

            System.out.println("Conta Poupança atualizada com Sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ContaPoupanca conta) throws SQLException {
        String sql = "DELETE FROM contas_bancarias WHERE id = ? AND tipo='POUPANCA'";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setInt(1, conta.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Conta Poupança não encontrada!");
                return;
            }

            System.out.println("Conta Poupança deletada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
