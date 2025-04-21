package models;

public abstract class ContaBancaria {
    protected double saldo;
    protected String titular;
    protected String agencia;
    protected String conta;
    protected String senha;
    private int id_conta;

    public ContaBancaria(String titular, String agencia, String conta, double saldoInicial, String senha) {
        this.titular = titular;
        this.agencia = agencia;
        this.conta = conta;
        this.saldo = saldoInicial;
        this.senha = senha;
    }

    public void depositar(double valor) {
        saldo += valor;
        System.out.println("Depósito de R$ " + valor + " realizado. Novo saldo: R$ " + saldo);
    }

    public abstract void sacar(double valor);

    public void exibirSaldo() {
        System.out.println("Saldo atual de " + titular + " (Agência: " + agencia + ", Conta: " + conta + "): R$ " + saldo);
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public int getId() {
        return id_conta;
    }

    public void setId(int id_conta) {
        this.id_conta = id_conta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getSenha() {
        return senha;
    }   

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
}