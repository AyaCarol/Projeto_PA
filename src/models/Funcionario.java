package models;
public class Funcionario extends Pessoa implements Tributavel {
    private double salario;
    private String cargo;
    private ContaBancaria contaSalario;
    

    public Funcionario(String nome, int idade, String cpf, String endereco, double salario, String cargo, ContaBancaria contaSalario){
        super(nome, idade, cpf, endereco);
        this.salario = salario;
        this.cargo = cargo;
        this.contaSalario = contaSalario;
    }

    public void setSalario(float salAlt){
        this.salario = salAlt;
    }

    public double getSalario(){
        return this.salario;
    }

    public double getSalario(double bonus){
        return this.salario + bonus;
    }

    public void setCargo(String cargoAlt){
        this.cargo = cargoAlt;
    }

    public String getCargo(){
        return this.cargo;
    }

    public ContaBancaria getContaSalario() {
        return contaSalario;
    }

    public void setContaSalario(ContaBancaria contaSalario) {
        this.contaSalario = contaSalario;
    }

    @Override
    public double calcularIR() {
        return salario * 0.11; // 11% de imposto de renda sobre o salário
    }

    public void exibirInfo(){
        System.out.println(
            "\nFUNCIONARIO\n"
            + "Nome: " + this.nome
            + "\nIdade: " 
            + this.idade
            + "\nCPF: "
            + this.getCpf()
            + "\nEndereco: "
            + this.getEndereco()
            + "\nCargo: " + this.cargo
            + "\nSalario: " + this.salario
            + "\nConta Salário: " + this.contaSalario.getConta()
        );
    }

}