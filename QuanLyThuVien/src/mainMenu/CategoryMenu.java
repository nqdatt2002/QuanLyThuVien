package mainMenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import dao.CategoryDAO;
import entities.Category;

public class CategoryMenu {

    private static final CategoryDAO dao = new CategoryDAO();

    public static void showMenu(Scanner sc) {
        while (true) {
            System.out.println("\n******** QUẢN LÝ DANH MỤC ********");
            System.out.println("1. Danh sách danh mục");
            System.out.println("2. Thêm danh mục");
            System.out.println("3. Xóa danh mục");
            System.out.println("4. Tìm kiếm danh mục");
            System.out.println("5. Quay lại");
            System.out.print("Lựa chọn của bạn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    listMenu(sc);
                    break;
                case "2":
                    addCategory(sc);
                    break;
                case "3":
                    deleteCategory(sc);
                    break;
                case "4":
                    searchCategory(sc);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Vui lòng chọn từ 1 đến 5!");
            }
        }
    }

    private static void listMenu(Scanner sc) {
        while (true) {
            System.out.println("\n******** DANH SÁCH DANH MỤC ********");
            System.out.println("1. Danh sách cây danh mục");
            System.out.println("2. Thông tin chi tiết danh mục");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    showCategoryTree();
                    break;
                case "2":
                    showCategoryDetail(sc);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Vui lòng chọn từ 1 đến 3!");
            }
        }
    }

    private static void showCategoryTree() {
        List<Category> categories = dao.getAll();

        if (categories == null || categories.isEmpty()) {
            System.out.println("Chưa có danh mục nào!");
            return;
        }

        categories.sort((a, b) -> a.getId().compareTo(b.getId()));

        Set<String> visited = new HashSet<>();
        int rootNumber = 0;

        System.out.println("\n******** CÂY DANH MỤC ********");

        for (Category c : categories) {
            if (isRoot(c)) {
                rootNumber++;
                printTree(
                    c,
                    categories,
                    String.valueOf(rootNumber),
                    0,
                    visited
                );
            }
        }

        for (Category c : categories) {
            if (!visited.contains(c.getId())) {
                System.out.println("[Không nối được vào cây] "
                        + c.getId() + " - " + c.getName());
            }
        }
    }

    private static boolean isRoot(Category c) {
        String parentId = c.getParentId();
        return parentId == null
                || parentId.trim().isEmpty()
                || parentId.trim().equals("0");
    }

    private static void printTree(
            Category current,
            List<Category> categories,
            String number,
            int level,
            Set<String> visited) {

        if (visited.contains(current.getId())) {
            return;
        }

        visited.add(current.getId());

        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "    ";
        }

        System.out.println(indent + number + "   \""
                + current.getName() + "\"");

        List<Category> children = new ArrayList<>();

        for (Category c : categories) {
            if (current.getId().equals(c.getParentId())) {
                children.add(c);
            }
        }

        children.sort((a, b) -> a.getId().compareTo(b.getId()));

        for (int i = 0; i < children.size(); i++) {
            printTree(
                children.get(i),
                categories,
                number + "." + (i + 1),
                level + 1,
                visited
            );
        }
    }

    private static void showCategoryDetail(Scanner sc) {
        System.out.print("Nhập mã danh mục cần xem: ");
        String id = sc.nextLine().trim();

        Category c = dao.getById(id);

        if (c == null) {
            System.out.println("Không tìm thấy danh mục!");
            return;
        }

        System.out.println("\n***** CHI TIẾT DANH MỤC *****");
        System.out.println("Mã danh mục: " + c.getId());
        System.out.println("Tên danh mục: " + c.getName());
        System.out.println("Trạng thái: "
                + (c.isStatus() ? "Hoạt động" : "Ngừng hoạt động"));
        System.out.println("Mã danh mục cha: " + c.getParentId());
    }

    private static void addCategory(Scanner sc) {
        System.out.println("\n***** THÊM DANH MỤC *****");

        System.out.print("Nhập mã danh mục: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Mã danh mục không được để trống!");
            return;
        }

        if (dao.getById(id) != null) {
            System.out.println("Mã danh mục đã tồn tại!");
            return;
        }

        System.out.print("Nhập tên danh mục (6-30 ký tự): ");
        String name = sc.nextLine().trim();

        if (name.length() < 6 || name.length() > 30) {
            System.out.println("Tên danh mục phải từ 6 đến 30 ký tự!");
            return;
        }

        System.out.print("Trạng thái (1: hoạt động, 0: ngừng): ");
        String statusInput = sc.nextLine().trim();

        boolean status;
        if (statusInput.equals("1")) {
            status = true;
        } else if (statusInput.equals("0")) {
            status = false;
        } else {
            System.out.println("Trạng thái không hợp lệ!");
            return;
        }

        System.out.print("Nhập mã danh mục cha (0 nếu là danh mục gốc): ");
        String parentId = sc.nextLine().trim();

        if (parentId.isEmpty()) {
            parentId = "0";
        }

        if (!parentId.equals("0")) {
            if (!parentId.matches("[0-9]+")) {
                System.out.println(
                    "Mã danh mục cha phải là số theo ràng buộc SQL hiện tại!");
                return;
            }

            if (parentId.equals(id)) {
                System.out.println("Danh mục không thể là cha của chính nó!");
                return;
            }

            if (dao.getById(parentId) == null) {
                System.out.println("Danh mục cha không tồn tại!");
                return;
            }
        }

        Category c = new Category(id, name, status, parentId);

        if (dao.insert(c)) {
            System.out.println("Thêm danh mục thành công!");
        } else {
            System.out.println("Thêm danh mục thất bại!");
        }
    }

    private static void deleteCategory(Scanner sc) {
        System.out.println("\n***** XÓA DANH MỤC *****");

        System.out.print("Nhập mã danh mục cần xóa: ");
        String id = sc.nextLine().trim();

        Category c = dao.getById(id);

        if (c == null) {
            System.out.println("Không tìm thấy danh mục!");
            return;
        }

        System.out.println("Danh mục: " + c.getName());
        System.out.print("Bạn có chắc muốn xóa? (Y/N): ");

        String confirm = sc.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            if (dao.delete(id)) {
                System.out.println("Xóa danh mục thành công!");
            } else {
                System.out.println(
                    "Xóa thất bại! Có thể danh mục đang được sách sử dụng.");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    private static void searchCategory(Scanner sc) {
        System.out.println("\n***** TÌM KIẾM DANH MỤC *****");

        System.out.print("Nhập tên danh mục cần tìm: ");
        String name = sc.nextLine().trim();

        List<Category> categories = dao.searchByName(name);

        if (categories == null || categories.isEmpty()) {
            System.out.println("Không tìm thấy danh mục phù hợp!");
            return;
        }

        System.out.println("\nKết quả tìm kiếm:");

        for (Category c : categories) {
            System.out.println("-------------------------");
            System.out.println("Mã: " + c.getId());
            System.out.println("Tên: " + c.getName());
            System.out.println("Trạng thái: "
                    + (c.isStatus() ? "Hoạt động" : "Ngừng hoạt động"));
            System.out.println("Mã danh mục cha: " + c.getParentId());
        }
    }
}
