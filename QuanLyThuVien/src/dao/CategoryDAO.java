
package dao;

import db.DatabaseConnect;
import entities.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements CategoryDAOInterface {

    private Category mapResultSetToCategory(ResultSet rs)
            throws SQLException {

        Category category = new Category();

        category.setId(rs.getString("id"));
        category.setName(rs.getString("name"));
        category.setStatus(rs.getBoolean("status"));
        category.setParentId(rs.getString("parentId"));

        return category;
    }

    @Override
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();

        String sql = "SELECT * FROM category";

        try (Connection conn = DatabaseConnect.getConnect()) {
            if (conn == null) {
                return list;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapResultSetToCategory(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Category getById(String id) {
        String sql = "SELECT * FROM category WHERE id = ?";

        try (Connection conn = DatabaseConnect.getConnect()) {
            if (conn == null) {
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToCategory(rs);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean insert(Category category) {
        String sql = "INSERT INTO category "
                + "(id, name, status, parentId) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnect.getConnect()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, category.getId());
                ps.setString(2, category.getName());
                ps.setBoolean(3, category.isStatus());
                ps.setString(4, category.getParentId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Không thể thêm danh mục: "
                    + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE category "
                + "SET name = ?, status = ?, parentId = ? "
                + "WHERE id = ?";

        try (Connection conn = DatabaseConnect.getConnect()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, category.getName());
                ps.setBoolean(2, category.isStatus());
                ps.setString(3, category.getParentId());
                ps.setString(4, category.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Không thể cập nhật danh mục: "
                    + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM category WHERE id = ?";

        try (Connection conn = DatabaseConnect.getConnect()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Không thể xóa danh mục. "
                    + "Danh mục có thể đang được sách sử dụng. "
                    + e.getMessage());
        }

        return false;
    }

@Override
public List<Category> searchByName(String name) {
    List<Category> list = new ArrayList<>();

    String sql = "SELECT * FROM category WHERE name LIKE ?";

    try (Connection conn = DatabaseConnect.getConnect()) {
        if (conn == null) {
            return list;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCategory(rs));
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

}
