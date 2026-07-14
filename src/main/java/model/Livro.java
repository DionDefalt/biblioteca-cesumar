package model;

/**
 * Classe modelo que representa um Livro no acervo da biblioteca.
 * Aplica os princípios de encapsulamento da Orientação a Objetos.
 */
public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private String isbn;

    // Construtor padrão
    public Livro() {}

    // Construtor completo
    public Livro(int id, String titulo, String autor, int anoPublicacao, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.isbn = isbn;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Livro{id=" + id + ", titulo='" + titulo + "', autor='" + autor +
               "', ano=" + anoPublicacao + ", isbn='" + isbn + "'}";
    }
}
