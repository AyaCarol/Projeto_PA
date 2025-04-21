package dao;

import java.util.*;
import java.sql.*;
import models.*;
import factory.ConnectionFactory;

public class ProdutoDAO {
    public void create(Produto produto) throws SQLException{
        String sql = "INSERT INTO produtos (codigo, valor, nome, quantidade) VALUES (?, ?, ?, ?)";

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            stmt.setString(1, produto.getCodigo());
            stmt.setDouble(2, produto.getValor());
            stmt.setString(3, produto.getNome());
            stmt.setInt(4, produto.getQuantidade());

            stmt.executeUpdate();

            // Obtém o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                produto.setId(rs.getInt(1));
            }

            System.out.println("Produto criado com Sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> read() throws SQLException{
        String sql = "SELECT * FROM produtos";

        List<Produto> produtos = new ArrayList<>();

        try(
            Connection conn = ConnectionFactory.getMySQLConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                System.out.println("----------------------------------");
                System.out.println("Codigo: "+rs.getString("codigo"));

                System.out.println("Valor: "+rs.getDouble("valor"));

                System.out.println("Nome: "+rs.getString("nome"));

                System.out.println("Quantidade: "+rs.getInt("quantidade"));
                System.out.println("----------------------------------");

                Produto p = new Produto(
                    rs.getString("codigo"),
                    rs.getDouble("valor"),
                    rs.getString("nome"),
                    rs.getInt("quantidade")
                );

                p.setId(rs.getInt("id"));
                produtos.add(p);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public void update(Produto produto) throws SQLException{
        String sql = "UPDATE produtos SET codigo=?, valor=?, nome=?, quantidade=? WHERE id=?";
        try (
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ){
        
            stmt.setString(1, produto.getCodigo());
            stmt.setDouble(2, produto.getValor());
            stmt.setString(3, produto.getNome());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getId());

            int update_instances = stmt.executeUpdate();

            if(update_instances == 0){
                System.out.println("Funcionario não encontrado!");
                return;
            }

            System.out.println("Funcionario Editado com Sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Produto produto){
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (
            Connection conn = ConnectionFactory.getMySQLConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setInt(1, produto.getId());
            
            int update_instances = stmt.executeUpdate();

            if(update_instances == 0){
                System.out.println("Produto não encontrado!");
                return;
            }

            System.out.println("Produto Deletado com sucesso! \nProdutos deletados: "+update_instances);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
