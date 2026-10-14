package dao;

import java.util.List;
import entities.Category;


public interface CategoryDAOInterface {

    List<Category> getAll();

    Category getById(String id);

    boolean insert(Category category);

    boolean update(Category category);

    boolean delete(String id);

    List<Category> searchByName(String name);
}

