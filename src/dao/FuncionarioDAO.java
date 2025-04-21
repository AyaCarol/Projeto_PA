package dao;

import java.util.*;
import java.sql.*;
import models.*;
import factory.ConnectionFactory;

public class FuncionarioDAO {
    private ContaCorrenteDAO contaDAO;

    public FuncionarioDAO() {
        this.contaDAO = new ContaCorrenteDAO();
    }

    public void create(Funcionario funcionario) throws SQLException {
        // Primeiro salva a conta se ela não existir
        if (funcionario.getContaSalario().getId() == 0) {
            try {
                contaDAO.create((ContaCorrente) funcionario.getContaSalario());
                System.out.println("ID da conta após criação: " + funcionario.getContaSalario().getId());
            } catch (SQLException e) {
                System.out.println("Erro ao criar conta: " + e.getMessage());
                throw e;
            }
        }

        String sql = "INSERT INTO funcionarios (cpf, nome, idade, endereco, salario, cargo, id_conta) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            int idConta = funcionario.getContaSalario().getId();

            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.nome);
            stmt.setInt(3, funcionario.idade);
            stmt.setString(4, funcionario.getEndereco());
            stmt.setDouble(5, funcionario.getSalario());
            stmt.setString(6, funcionario.getCargo());
            stmt.setInt(7, idConta);

            stmt.executeUpdate();

            // Obtém o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                funcionario.setId(rs.getInt(1));
            }

            System.out.println("Funcionário criado com Sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar funcionário: " + e.getMessage());
            throw e;
        }
    }

    public List<Funcionario> read() throws SQLException {
        String sql = "SELECT f.*, c.titular, c.agencia, c.conta, c.saldo, c.senha, c.tarifa " +
                    "FROM funcionarios f " +
                    "JOIN contas_bancarias c ON f.id_conta = c.id";
        List<Funcionario> funcionarios = new ArrayList<>();

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
                conta.setId(rs.getInt("id_conta"));

                Funcionario f = new Funcionario(
                    rs.getString("nome"),
                    rs.getInt("idade"),
                    rs.getString("cpf"),
                    rs.getString("endereco"),
                    rs.getDouble("salario"),
                    rs.getString("cargo"),
                    conta
                );
                f.setId(rs.getInt("id"));
                funcionarios.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

    public void update(Funcionario funcionario) throws SQLException {
        // Atualiza a conta se necessário
        if (funcionario.getContaSalario().getId() == 0) {
            contaDAO.create((ContaCorrente) funcionario.getContaSalario());
        } else {
            contaDAO.update((ContaCorrente) funcionario.getContaSalario());
        }

        String sql = "UPDATE funcionarios SET cpf=?, nome=?, idade=?, endereco=?, salario=?, cargo=?, id_conta=? WHERE id=?";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.nome);
            stmt.setInt(3, funcionario.idade);
            stmt.setString(4, funcionario.getEndereco());
            stmt.setDouble(5, funcionario.getSalario());
            stmt.setString(6, funcionario.getCargo());
            stmt.setInt(7, funcionario.getContaSalario().getId());
            stmt.setInt(8, funcionario.getId());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Funcionário não encontrado!");
                return;
            }

            System.out.println("Funcionário atualizado com Sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Funcionario funcionario) throws SQLException {
        String sql = "DELETE FROM funcionarios WHERE id = ?";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setInt(1, funcionario.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Funcionário não encontrado!");
                return;
            }

            System.out.println("Funcionário deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
