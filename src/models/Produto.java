package models;

public class Produto {
    private String codigo;
    private double valor;
    private String nome;
    private int quantidade;
    private int id;

    public Produto(String codigo, double valor, String nome, int quantidade){
        this.codigo = codigo;
        this.valor = valor;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public String getCodigo(){
        return this.codigo;
    }

    public double getValor(){
        return this.valor;
    }   

    public String getNome(){
        return this.nome;
    }

    public int getQuantidade(){
        return this.quantidade;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public void setValor(double valor){
        this.valor = valor;
    }

    public void setNome(String nome){   
        this.nome = nome;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
    
    public void setId(int id){
        this.id = id;
    } 

    public int getId(){
        return this.id;
    }
    
}
