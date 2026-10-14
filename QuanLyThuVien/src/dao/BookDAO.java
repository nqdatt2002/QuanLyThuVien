package dao;

import db.DatabaseConnect;
import entities.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements BookDAOInterface {

    private Book mapResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();

        book.setId(rs.getString("id"));
        book.setTitle(rs.getString("Title"));
        book.setStatus(rs.getBoolean("status"));
        book.setPrice(rs.getDouble("price"));
        book.setDescription(rs.getString("description"));
        book.setAuthor(rs.getString("Author"));
        book.setCategoryId(rs.getString("category_id"));

        return book;
    }

    @Override
    public List<Book> getAll() {
        List<Book> list = new ArrayList<>();

        String sql = "SELECT * FROM book";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return list;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sách: " + e.getMessage());
        }

        return list;
    }

    @Override
    public Book getById(String id) {
        String sql = "SELECT * FROM book WHERE id = ?";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi tìm sách: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean insert(Book book) {
        String sql = "INSERT INTO book "
                + "(id, Title, status, price, description, Author, category_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, book.getId());
                ps.setString(2, book.getTitle());
                ps.setBoolean(3, book.isStatus());
                ps.setDouble(4, book.getPrice());
                ps.setString(5, book.getDescription());
                ps.setString(6, book.getAuthor());
                ps.setString(7, book.getCategoryId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi thêm sách: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE book SET "
                + "Title = ?, status = ?, price = ?, "
                + "description = ?, Author = ?, category_id = ? "
                + "WHERE id = ?";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, book.getTitle());
                ps.setBoolean(2, book.isStatus());
                ps.setDouble(3, book.getPrice());
                ps.setString(4, book.getDescription());
                ps.setString(5, book.getAuthor());
                ps.setString(6, book.getCategoryId());
                ps.setString(7, book.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sách: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM book WHERE id = ?";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi xóa sách: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Book> getByCategory(String categoryId) {
        List<Book> list = new ArrayList<>();

        String sql = "SELECT * FROM book WHERE category_id = ?";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return list;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, categoryId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSet(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy sách theo danh mục: "
                    + e.getMessage());
        }

        return list;
    }

    @Override
    public List<Book> searchByAuthorAndMinPrice(
            String author, double minPrice) {

        List<Book> list = new ArrayList<>();

        String sql = "SELECT * FROM book "
                + "WHERE Author LIKE ? AND price >= ?";

        try (Connection con = DatabaseConnect.getConnect()) {
            if (con == null) {
                return list;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + author + "%");
                ps.setDouble(2, minPrice);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSet(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm sách: " + e.getMessage());
        }

        return list;
    }
}