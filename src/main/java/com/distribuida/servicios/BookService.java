package com.distribuida.servicios;

import com.distribuida.db.Book;

import java.util.List;

public interface BookService {

    Book buscarId(Integer id);
    List<Book> buscar();
    Boolean ingresar(Book book);
    Boolean eliminar(Integer id);
    Boolean actualizar(Book book);

}
