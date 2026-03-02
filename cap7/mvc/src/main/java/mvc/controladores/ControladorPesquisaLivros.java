package mvc.controladores;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import mvc.modelo.*;
import mvc.visao.*;

public class ControladorPesquisaLivros {
  private ServicoPesquisaLivros pesq;
  private PaginaDadosLivro pagina; 

  public ControladorPesquisaLivros(ServicoPesquisaLivros pesq, PaginaDadosLivro pagina) {
    this.pesq = pesq;
    this.pagina = pagina;
  }
  
  public void start() {
    // Criação da instância do Javalin com arquivos estáticos
    Javalin app = Javalin.create(config -> {
        config.staticFiles.add("/static", Location.CLASSPATH);
    }).start(4567);
  
    // Rota Raiz
    app.get("/", ctx -> ctx.redirect("/index.html"));

    // Rota de Pesquisa
    app.get("/pesquisa", ctx -> { 
       String autor = ctx.queryParam("autor");
       
       Livro livro = pesq.pesquisaPorAutor(autor);
       
       if (livro != null) {
           String html = pagina.exibeLivro(livro.getTitulo(), livro.getAutor(), livro.getISBN());
           ctx.html(html); // Define o content-type como text/html automaticamente
       } else {
           ctx.status(404).result("Livro não encontrado para o autor: " + autor);
       }
    });

    System.out.println("Javalin rodando em http://localhost:4567");
  }
}
