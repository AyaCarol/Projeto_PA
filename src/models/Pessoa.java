package models;


public class Pessoa{
    public String nome;
    public int idade;
    private String cpf;
    private String endereco;
    private int id;

    public Pessoa(String nome, int idade, String cpf, String endereco){
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    public Pessoa(String nome){
        this.nome = nome;
        this.endereco = "Não informado";
    }

    public void setCpf(String cpfAlt){
        this.cpf = cpfAlt;
    }

    public String getCpf(){
        return this.cpf;
    }

    public void setEndereco(String endAlt){
        this.endereco = endAlt;
    }

    public String getEndereco(){
        return this.endereco;
    }

    public void exibirInfo(){
        System.out.println(
            "PESSOA\n" 
            + "Nome: " 
            + this.nome 
            + "\nIdade: " 
            + this.idade
            + "\nCPF: "
            + this.cpf
            + "\nEndereco: "
            + this.endereco);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}