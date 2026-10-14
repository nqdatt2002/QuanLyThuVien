package dao;

import java.util.List;
import entities.Book;

public interface BookDAOInterface {
    List<Book> getAll();
    Book getById(String id);
    boolean insert(Book book);
    boolean update(Book book);
    boolean delete(String id);
    List<Book> getByCategory(String categoryId);
    List<Book> searchByAuthorAndMinPrice(
            String author, double minPrice);
}