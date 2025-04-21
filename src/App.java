import models.*;
import dao.*;


public class App {
    public static void main(String[] args) {
        try {
            // Testes para ContaCorrente
            testarContaCorrente();
            
            // Testes para ContaPoupanca
            testarContaPoupanca();
            
            // Testes para Funcionario
            testarFuncionario();
            
            // Testes para Produto
            testarProduto();
            
        } catch (Exception e) {
            System.out.println("Erro durante os testes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testarContaCorrente() throws Exception {
        System.out.println("\n=== Testes ContaCorrente ===");
        ContaCorrenteDAO dao = new ContaCorrenteDAO();
        
        // Create
        ContaCorrente conta = new ContaCorrente("João Silva", "001", "1234", 1000.0, "senha123", 10.0);
        System.out.println("\nCriando conta corrente...");
        dao.create(conta);
        System.out.println("Conta criada com ID: " + conta.getId());

        // Read
        System.out.println("\nListando todas as contas correntes:");
        for (ContaCorrente c : dao.read()) {
            System.out.println("Conta: " + c.getConta() + " - Saldo: " + c.getSaldo());
        }

        // Update
        System.out.println("\nAtualizando conta corrente...");
        conta.setSaldo(2000.0);
        dao.update(conta);
        System.out.println("Conta atualizada");

        // Delete
        System.out.println("\nDeletando conta corrente...");
        dao.delete(conta);
        System.out.println("Conta deletada");
    }

    private static void testarContaPoupanca() throws Exception {
        System.out.println("\n=== Testes ContaPoupanca ===");
        ContaPoupancaDAO dao = new ContaPoupancaDAO();
        
        // Create
        ContaPoupanca conta = new ContaPoupanca("Maria Santos", "002", "5678", 5000.0, "senha456", 0.5);
        System.out.println("\nCriando conta poupança...");
        dao.create(conta);
        System.out.println("Conta criada com ID: " + conta.getId());

        // Read
        System.out.println("\nListando todas as contas poupança:");
        for (ContaPoupanca c : dao.read()) {
            System.out.println("Conta: " + c.getConta() + " - Saldo: " + c.getSaldo());
        }

        // Update
        System.out.println("\nAtualizando conta poupança...");
        conta.setSaldo(6000.0);
        dao.update(conta);
        System.out.println("Conta atualizada");

        // Delete
        System.out.println("\nDeletando conta poupança...");
        dao.delete(conta);
        System.out.println("Conta deletada");
    }

    private static void testarFuncionario() throws Exception {
        System.out.println("\n=== Testes Funcionario ===");
        FuncionarioDAO dao = new FuncionarioDAO();
        
        // Primeiro criar uma conta para o funcionário
        ContaCorrente conta = new ContaCorrente("Funcionário Teste", "003", "9012", 3000.0, "senha789", 15.0);
        ContaCorrenteDAO contaDAO = new ContaCorrenteDAO();
        contaDAO.create(conta);

        // Create
        Funcionario funcionario = new Funcionario(
            "Carlos Oliveira",
            30,
            "123.456.789-00",
            "Rua Teste, 123",
            5000.0,
            "Desenvolvedor",
            conta
        );
        System.out.println("\nCriando funcionário...");
        dao.create(funcionario);
        System.out.println("Funcionário criado com ID: " + funcionario.getId());

        // Read
        System.out.println("\nListando todos os funcionários:");
        for (Funcionario f : dao.read()) {
            System.out.println("Nome: " + f.nome + " - Cargo: " + f.getCargo());
        }

        // Update
        System.out.println("\nAtualizando funcionário...");
        funcionario.setSalario((float)6000.0);
        dao.update(funcionario);
        System.out.println("Funcionário atualizado");

        // Delete
        System.out.println("\nDeletando funcionário...");
        dao.delete(funcionario);
        System.out.println("Funcionário deletado");
        
        // Deletar a conta também
        contaDAO.delete(conta);
    }

    private static void testarProduto() throws Exception {
        System.out.println("\n=== Testes Produto ===");
        ProdutoDAO dao = new ProdutoDAO();
        
        // Create
        Produto produto = new Produto("PRD001", 5000.0, "Notebook", 10);
        System.out.println("\nCriando produto...");
        dao.create(produto);
        System.out.println("Produto criado com ID: " + produto.getId());

        // Read
        System.out.println("\nListando todos os produtos:");
        for (Produto p : dao.read()) {
            System.out.println("Nome: " + p.getNome() + " - Valor: " + p.getValor());
        }

        // Update
        System.out.println("\nAtualizando produto...");
        produto.setValor(5500.0);
        dao.update(produto);
        System.out.println("Produto atualizado");

        // Delete
        System.out.println("\nDeletando produto...");
        dao.delete(produto);
        System.out.println("Produto deletado");
    }
}
