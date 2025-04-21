package models;

public class ContaCorrente extends ContaBancaria implements Tributavel {
    private static double tarifa;

    public ContaCorrente(String titular, String agencia, String conta, double saldoInicial, String senha, double tarifa) {
        super(titular, agencia, conta, saldoInicial, senha);
        ContaCorrente.tarifa = tarifa;
    }

    @Override
    public void sacar(double valor) {
        double valorTotal = valor - tarifa;
        if (saldo >= valorTotal) {
            saldo -= valorTotal;
            System.out.println("Saque de R$ " + valor + " realizado. Tarifa de R$ " + tarifa + " aplicada. Novo saldo: R$ " + saldo);
        } else {
            System.out.println("Saldo insuficiente para realizar o saque.");
        }
    }

    @Override
    public double calcularIR() {
        return saldo * 0.015; // Exemplo: 1.5% de imposto sobre o saldo
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        ContaCorrente.tarifa = tarifa;
    }

}
