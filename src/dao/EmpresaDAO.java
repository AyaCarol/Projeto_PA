package dao;

import java.util.*;
import java.sql.*;
import services.*;
import models.*;
import factory.ConnectionFactory;

public class EmpresaDAO {
    private FuncionarioDAO funcionarioDAO;
    private ProdutoDAO produtoDAO;

    public EmpresaDAO() {
        this.funcionarioDAO = new FuncionarioDAO();
        this.produtoDAO = new ProdutoDAO();
    }

    public void create(Empresa empresa) throws SQLException {
        String sqlEmpresa = "INSERT INTO empresas (nome, cnpj) VALUES (?, ?)";
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getMySQLConnection();
            conn.setAutoCommit(false);  // Inicia transação

            // Insere a empresa
            PreparedStatement stmtEmpresa = conn.prepareStatement(sqlEmpresa, Statement.RETURN_GENERATED_KEYS);
            stmtEmpresa.setString(1, empresa.getNome());
            stmtEmpresa.setString(2, empresa.getCnpj());
            stmtEmpresa.executeUpdate();

            // Obtém o ID gerado para a empresa
            ResultSet rs = stmtEmpresa.getGeneratedKeys();
            if (rs.next()) {
                empresa.setId(rs.getInt(1));
            }

            // Insere os emails
            String sqlEmail = "INSERT INTO empresa_emails (empresa_id, email) VALUES (?, ?)";
            PreparedStatement stmtEmail = conn.prepareStatement(sqlEmail);
            for (String email : empresa.getEmails()) {
                stmtEmail.setInt(1, empresa.getId());
                stmtEmail.setString(2, email);
                stmtEmail.executeUpdate();
            }

            // Insere os relacionamentos com funcionários
            String sqlFuncionario = "INSERT INTO empresa_funcionarios (empresa_id, funcionario_id) VALUES (?, ?)";
            PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
            for (Funcionario funcionario : empresa.getFuncionarios()) {
                // Primeiro salva o funcionário se ele não existir
                if (funcionario.getId() == 0) {
                    funcionarioDAO.create(funcionario);
                }
                stmtFuncionario.setInt(1, empresa.getId());
                stmtFuncionario.setInt(2, funcionario.getId());
                stmtFuncionario.executeUpdate();
            }

            // Insere os relacionamentos com produtos
            String sqlProduto = "INSERT INTO empresa_produtos (empresa_id, produto_id) VALUES (?, ?)";
            PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
            for (Produto produto : empresa.getProdutos().values()) {
                // Primeiro salva o produto se ele não existir
                if (produto.getId() == 0) {
                    produtoDAO.create(produto);
                }
                stmtProduto.setInt(1, empresa.getId());
                stmtProduto.setInt(2, produto.getId());
                stmtProduto.executeUpdate();
            }

            conn.commit();  // Confirma a transação
            System.out.println("Empresa criada com sucesso!");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);  // Restaura autocommit
                conn.close();
            }
        }
    }

    public List<Empresa> read() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresas";

        try (
            Connection conn = ConnectionFactory.getMySQLConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                int empresaId = rs.getInt("id");
                Empresa empresa = new Empresa(
                    rs.getString("nome"),
                    rs.getString("cnpj")
                );
                empresa.setId(empresaId);

                // Carrega emails
                String sqlEmails = "SELECT email FROM empresa_emails WHERE empresa_id = ?";
                PreparedStatement stmtEmails = conn.prepareStatement(sqlEmails);
                stmtEmails.setInt(1, empresaId);
                ResultSet rsEmails = stmtEmails.executeQuery();
                while (rsEmails.next()) {
                    empresa.adicionarEmail(rsEmails.getString("email"));
                }

                // Carrega funcionários
                String sqlFuncionarios = "SELECT f.* FROM funcionarios f " +
                                       "JOIN empresa_funcionarios ef ON f.id = ef.funcionario_id " +
                                       "WHERE ef.empresa_id = ?";
                PreparedStatement stmtFuncionarios = conn.prepareStatement(sqlFuncionarios);
                stmtFuncionarios.setInt(1, empresaId);
                ResultSet rsFuncionarios = stmtFuncionarios.executeQuery();
                while (rsFuncionarios.next()) {
                    Funcionario funcionario = criarFuncionarioDoResultSet(rsFuncionarios);
                    empresa.adicionarFuncionario(funcionario);
                }

                // Carrega produtos
                String sqlProdutos = "SELECT p.* FROM produtos p " +
                                   "JOIN empresa_produtos ep ON p.id = ep.produto_id " +
                                   "WHERE ep.empresa_id = ?";
                PreparedStatement stmtProdutos = conn.prepareStatement(sqlProdutos);
                stmtProdutos.setInt(1, empresaId);
                ResultSet rsProdutos = stmtProdutos.executeQuery();
                while (rsProdutos.next()) {
                    Produto produto = criarProdutoDoResultSet(rsProdutos);
                    empresa.adicionarProduto(produto);
                }

                empresas.add(empresa);
            }
        }
        return empresas;
    }

    public void update(Empresa empresa) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getMySQLConnection();
            conn.setAutoCommit(false);

            // Atualiza dados básicos da empresa
            String sqlEmpresa = "UPDATE empresas SET nome = ?, cnpj = ? WHERE id = ?";
            PreparedStatement stmtEmpresa = conn.prepareStatement(sqlEmpresa);
            stmtEmpresa.setString(1, empresa.getNome());
            stmtEmpresa.setString(2, empresa.getCnpj());
            stmtEmpresa.setInt(3, empresa.getId());
            stmtEmpresa.executeUpdate();

            // Atualiza emails (remove todos e insere novamente)
            String sqlDeleteEmails = "DELETE FROM empresa_emails WHERE empresa_id = ?";
            PreparedStatement stmtDeleteEmails = conn.prepareStatement(sqlDeleteEmails);
            stmtDeleteEmails.setInt(1, empresa.getId());
            stmtDeleteEmails.executeUpdate();

            String sqlEmail = "INSERT INTO empresa_emails (empresa_id, email) VALUES (?, ?)";
            PreparedStatement stmtEmail = conn.prepareStatement(sqlEmail);
            for (String email : empresa.getEmails()) {
                stmtEmail.setInt(1, empresa.getId());
                stmtEmail.setString(2, email);
                stmtEmail.executeUpdate();
            }

            // Atualiza relacionamentos com funcionários
            String sqlDeleteFuncionarios = "DELETE FROM empresa_funcionarios WHERE empresa_id = ?";
            PreparedStatement stmtDeleteFuncionarios = conn.prepareStatement(sqlDeleteFuncionarios);
            stmtDeleteFuncionarios.setInt(1, empresa.getId());
            stmtDeleteFuncionarios.executeUpdate();

            String sqlFuncionario = "INSERT INTO empresa_funcionarios (empresa_id, funcionario_id) VALUES (?, ?)";
            PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
            for (Funcionario funcionario : empresa.getFuncionarios()) {
                if (funcionario.getId() == 0) {
                    funcionarioDAO.create(funcionario);
                }
                stmtFuncionario.setInt(1, empresa.getId());
                stmtFuncionario.setInt(2, funcionario.getId());
                stmtFuncionario.executeUpdate();
            }

            // Atualiza relacionamentos com produtos
            String sqlDeleteProdutos = "DELETE FROM empresa_produtos WHERE empresa_id = ?";
            PreparedStatement stmtDeleteProdutos = conn.prepareStatement(sqlDeleteProdutos);
            stmtDeleteProdutos.setInt(1, empresa.getId());
            stmtDeleteProdutos.executeUpdate();

            String sqlProduto = "INSERT INTO empresa_produtos (empresa_id, produto_id) VALUES (?, ?)";
            PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
            for (Produto produto : empresa.getProdutos().values()) {
                if (produto.getId() == 0) {
                    produtoDAO.create(produto);
                }
                stmtProduto.setInt(1, empresa.getId());
                stmtProduto.setInt(2, produto.getId());
                stmtProduto.executeUpdate();
            }

            conn.commit();
            System.out.println("Empresa atualizada com sucesso!");

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void delete(Empresa empresa) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getMySQLConnection();
            conn.setAutoCommit(false);

            // Remove relacionamentos
            String sqlDeleteEmails = "DELETE FROM empresa_emails WHERE empresa_id = ?";
            PreparedStatement stmtDeleteEmails = conn.prepareStatement(sqlDeleteEmails);
            stmtDeleteEmails.setInt(1, empresa.getId());
            stmtDeleteEmails.executeUpdate();

            String sqlDeleteFuncionarios = "DELETE FROM empresa_funcionarios WHERE empresa_id = ?";
            PreparedStatement stmtDeleteFuncionarios = conn.prepareStatement(sqlDeleteFuncionarios);
            stmtDeleteFuncionarios.setInt(1, empresa.getId());
            stmtDeleteFuncionarios.executeUpdate();

            String sqlDeleteProdutos = "DELETE FROM empresa_produtos WHERE empresa_id = ?";
            PreparedStatement stmtDeleteProdutos = conn.prepareStatement(sqlDeleteProdutos);
            stmtDeleteProdutos.setInt(1, empresa.getId());
            stmtDeleteProdutos.executeUpdate();

            // Remove a empresa
            String sqlDeleteEmpresa = "DELETE FROM empresas WHERE id = ?";
            PreparedStatement stmtDeleteEmpresa = conn.prepareStatement(sqlDeleteEmpresa);
            stmtDeleteEmpresa.setInt(1, empresa.getId());
            int rowsAffected = stmtDeleteEmpresa.executeUpdate();

            conn.commit();

            if (rowsAffected > 0) {
                System.out.println("Empresa deletada com sucesso!");
            } else {
                System.out.println("Empresa não encontrada!");
            }

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private Funcionario criarFuncionarioDoResultSet(ResultSet rs) throws SQLException {
        ContaBancaria contaSalario = new ContaCorrente(
            rs.getString("nome"),
            "001",  // agência padrão
            rs.getString("conta_salario"),
            0.0,    // saldo inicial
            "senha", // senha padrão
            10.0    // tarifa padrão
        );

        Funcionario funcionario = new Funcionario(
            rs.getString("nome"),
            rs.getInt("idade"),
            rs.getString("cpf"),
            rs.getString("endereco"),
            rs.getDouble("salario"),
            rs.getString("cargo"),
            contaSalario
        );
        funcionario.setId(rs.getInt("id"));
        return funcionario;
    }

    private Produto criarProdutoDoResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto(
            rs.getString("codigo"),
            rs.getDouble("valor"),
            rs.getString("nome"),
            rs.getInt("quantidade")
        );
        produto.setId(rs.getInt("id"));
        return produto;
    }
}
