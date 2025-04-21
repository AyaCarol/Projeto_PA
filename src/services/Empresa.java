package services;
import java.util.*;

import models.ContaBancaria;
import models.Funcionario;
import models.Produto;


public class Empresa {
    private String nome;
    private String cnpj;
    private Set<String> emails;
    private ArrayList<Funcionario> funcionarios;
    private Map<String, Produto> produtos;
    private int id;

    public Empresa(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.emails = new HashSet<>();
        this.funcionarios = new ArrayList<>();
        this.produtos = new HashMap<>();
    }

    public boolean adicionarEmail(String email) {
        return emails.add(email);
    }

    public boolean removerEmail(String email) {
        return emails.remove(email);
    }

    public void exibirEmails() {
        System.out.println("E-mails da empresa:");
        for (String email : emails) {
            System.out.println(email);
        }
    }

    public void adicionarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    public boolean removerFuncionario(String nomeFuncionario) {
        return funcionarios.removeIf(f -> f.nome.equalsIgnoreCase(nomeFuncionario));
    }

    public void exibirFuncionarios() {
        System.out.println("Funcionários da empresa:");
        for (Funcionario f : funcionarios) {
            System.out.println(f.nome);
        }
    }

    public void adicionarProduto(Produto produto) {
        produtos.put(produto.getCodigo(), produto);
    }
    
    public boolean removerProduto(String codigo){
        return produtos.remove(codigo) != null;
    }

    public void exibirProdutos(){
        System.out.println("Produtos da empresa:");
        for (Produto p : produtos.values()) {
            System.out.println(p.getNome());
        }
    }

    public void filtrarProdutosPorNome(String nome){
        produtos.values().stream().filter(prod -> prod.getNome().toLowerCase().
        contains(nome.toLowerCase())).
        forEach(System.out::println);
    }

    public void filtrarProdutosPorPreco(double preco){
        produtos.values().stream().
        filter(prod -> prod.getValor() <= preco).
        forEach(System.out::println);
    }

    public void filtrarProdutosPorCodigo(String codigo){
        produtos.values().stream().
        filter(prod -> prod.getCodigo().equals(codigo)).
        forEach(System.out::println);
    }

    public void pagarFuncionarios() {
        System.out.println("Realizando pagamento dos funcionários:");
        for (Funcionario funcionario : funcionarios) {
            ContaBancaria conta = funcionario.getContaSalario();
            double salario = funcionario.getSalario();
            conta.depositar(salario);
            System.out.println("Depositado R$ " + salario + " na conta de " + funcionario.nome);
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public ArrayList<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(ArrayList<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Map<String, Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Map<String, Produto> produtos) {
        this.produtos = produtos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}