package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.servicios.BookService;
import com.google.gson.Gson;
import io.helidon.http.Status;
import io.helidon.webserver.WebServer;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    private static ContainerLifecycle lifecycle = null;


    public static void main(String[] args)  {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        Gson gson=new Gson();
        BookService servicio= CDI.current().select(BookService.class).get();

        Book book=new Book();
        book.setIsbn("125df3");
        book.setAuthor("Kevin .A");
        book.setTitle("El secreto de la montaña");
        book.setPrice(new BigDecimal(15));

        servicio.ingresar(book);
        servicio.buscar().stream().map(Book::toString).forEach(System.out::println);

        WebServer server = WebServer.builder()
                .port(8080)
                .routing(builder -> builder
                        .get("/libros", (req, res) -> {
                            try {
                                List<Book> libros = servicio.buscar();
                                String response = new Gson().toJson(libros);
                                res.status(Status.ACCEPTED_202)
                                        .send(response);
                            }catch (RuntimeException e){
                                res.status(Status.BAD_REQUEST_400)
                                        .send("Error al obtener el resultado");
                            }
                        })
                        .get("/libros/{id}", (req, res) -> {
                            try {
                                Book libro = servicio
                                        .buscarId(Integer.valueOf(req.path().pathParameters().get("id")));
                                String response = new Gson().toJson(libro);
                                res.status(Status.ACCEPTED_202)
                                        .send(response);
                            }catch (RuntimeException e){
                                res.status(Status.BAD_REQUEST_400)
                                        .send("Error al obtener el resultado");
                            }
                        })
                        .post("/libros", (req, res) -> {
                            String body = req.content().as(String.class);
                            Book book1 = gson.fromJson(body, Book.class);
                            Boolean ingresado=servicio.ingresar(book1);
                            if(ingresado) {
                                res.status(Status.ACCEPTED_202)
                                        .send("Libro ingresado correctamente");
                            }else{
                                res.status(Status.BAD_REQUEST_400)
                                        .send("Error al ingresar el dato");
                            }
                        })
                        .put("/libros/{id}", (req, res) -> {
                            try{
                                Book books = servicio
                                        .buscarId(Integer.valueOf(req.path().pathParameters().get("id")));
                                String libroAnteS = new Gson().toJson(books);
                                Book libroAnte = gson.fromJson(libroAnteS, Book.class);

                                String body = req.content().as(String.class);
                                Book book1 = gson.fromJson(body, Book.class);
                                book1.setId(libroAnte.getId());

                                Boolean actualizado=servicio.actualizar(book1);
                                if(actualizado) {
                                    res.status(Status.ACCEPTED_202)
                                            .send("Libro actualizado correctamente");
                                }else{
                                    res.status(Status.BAD_REQUEST_400)
                                            .send("Error al actualizar el dato");
                                }
                            }catch (Exception e){
                                res.status(Status.BAD_REQUEST_400)
                                        .send("Error al actualizar el objeto por un valor");
                            }
                        })
                        .delete("/libros/{id}", (req, res) -> {
                            Integer id = Integer.valueOf(req.path().pathParameters().get("id"));

                            Boolean eliminar=servicio.eliminar(id);
                            if(eliminar) {
                                res.status(Status.ACCEPTED_202)
                                        .send("Libro eliminado correctamente");
                            }else{
                                res.status(Status.BAD_REQUEST_400)
                                        .send("Error al eliminar el dato");
                            }
                        })
                )
                .build();
        server.start();
        shutdown();

    }

    public static void shutdown()  {
        lifecycle.stopApplication(null);
    }
}