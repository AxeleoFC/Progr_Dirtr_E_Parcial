package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService {

    @Inject
    EntityManager em;

    @Override
    public Book buscarId(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public List<Book> buscar() {
        return em.createQuery("select b from Book b order by b.id asc", Book.class)
                .getResultList();
    }

    @Override
    public Boolean ingresar(Book book) {
        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    @Override
    public Boolean eliminar(Integer id) {
        try {
            em.getTransaction().begin();
            em.remove(buscarId(id));
            em.getTransaction().commit();
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    @Override
    public Boolean actualizar(Book book) {
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
            return true;
        }catch(Exception e) {
            return false;
        }
    }
}
