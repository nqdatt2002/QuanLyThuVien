package mainMenu;

import java.util.List;
import java.util.Scanner;

import dao.BookDAO;
import dao.CategoryDAO;
import entities.Book;
import entities.Category;

public class BookMenu {

    private static final BookDAO bookDAO = new BookDAO();
    private static final CategoryDAO categoryDAO = new CategoryDAO();

    public static void showMenu(Scanner sc) {
        while (true) {
            System.out.println("\n***************** QUẢN LÝ SÁCH *****************");
            System.out.println("1. Thêm mới");
            System.out.println("2. Hiển thị thông tin");
            System.out.println("3. Sắp xếp");
            System.out.println("4. Cập nhật thông tin");
            System.out.println("5. Quay lại");
            System.out.print("Lựa chọn của bạn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addBooks(sc);
                    break;
                case "2":
                    showInfoMenu(sc);
                    break;
                case "3":
                    sortMenu(sc);
                    break;
                case "4":
                    updateBook(sc);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Vui lòng chọn từ 1 đến 5!");
            }
        }
    }

    private static void addBooks(Scanner sc) {
        System.out.print("Bạn muốn thêm bao nhiêu sách? ");

        int n;
        try {
            n = Integer.parseInt(sc.nextLine().trim());
            if (n <= 0) {
                System.out.println("Số lượng phải lớn hơn 0!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Số lượng không hợp lệ!");
            return;
        }

        for (int i = 1; i <= n; i++) {
            System.out.println("\n--- Nhập sách thứ " + i + " ---");

            System.out.print("Mã sách (ví dụ C001): ");
            String id = sc.nextLine().trim();

            if (id.length() != 4 || !id.matches("C...")) {
                System.out.println("Mã sách phải gồm C và 3 ký tự!");
                continue;
            }

            if (bookDAO.getById(id) != null) {
                System.out.println("Mã sách đã tồn tại!");
                continue;
            }

            System.out.print("Tên sách: ");
            String title = sc.nextLine().trim();

            System.out.print("Trạng thái (1: hoạt động, 0: ngừng): ");
            boolean status;
            String statusInput = sc.nextLine().trim();

            if (statusInput.equals("1")) {
                status = true;
            } else if (statusInput.equals("0")) {
                status = false;
            } else {
                System.out.println("Trạng thái không hợp lệ!");
                continue;
            }

            System.out.print("Giá sách: ");
            double price;
            try {
                price = Double.parseDouble(sc.nextLine().trim());
                if (price <= 0 || Double.isNaN(price)
                        || Double.isInfinite(price)) {
                    System.out.println("Giá phải lớn hơn 0!");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Giá không hợp lệ!");
                continue;
            }

            System.out.print("Mô tả: ");
            String description = sc.nextLine().trim();

            System.out.print("Tác giả: ");
            String author = sc.nextLine().trim();

            System.out.println("\nDanh sách danh mục:");
            List<Category> categories = categoryDAO.getAll();

            if (categories == null || categories.isEmpty()) {
                System.out.println("Chưa có danh mục. Hãy thêm danh mục trước!");
                continue;
            }

            for (Category c : categories) {
                System.out.println(c.getId() + " - " + c.getName());
            }

            System.out.print("Nhập mã danh mục: ");
            String categoryId = sc.nextLine().trim();

            if (categoryDAO.getById(categoryId) == null) {
                System.out.println("Mã danh mục không tồn tại!");
                continue;
            }

            Book book = new Book(
                id, title, status, price,
                description, author, categoryId
            );

            if (bookDAO.insert(book)) {
                System.out.println("Thêm sách thành công!");
            } else {
                System.out.println("Thêm sách thất bại!");
            }
        }
    }

    private static void showInfoMenu(Scanner sc) {
        while (true) {
            System.out.println("\n***** HIỂN THỊ THÔNG TIN SÁCH *****");
            System.out.println("1. Danh sách sách theo danh mục");
            System.out.println("2. Tìm sách theo tác giả và giá tối thiểu");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Nhập mã danh mục: ");
                    String categoryId = sc.nextLine().trim();

                    if (categoryDAO.getById(categoryId) == null) {
                        System.out.println("Danh mục không tồn tại!");
                    } else {
                        List<Book> books = bookDAO.getByCategory(categoryId);
                        printBooks(books);
                    }
                    break;

                case "2":
                    System.out.print("Nhập tên tác giả cần tìm: ");
                    String author = sc.nextLine().trim();

                    System.out.print("Nhập giá tối thiểu: ");
                    try {
                        double minPrice =
                            Double.parseDouble(sc.nextLine().trim());

                        if (minPrice < 0 || Double.isNaN(minPrice)
                                || Double.isInfinite(minPrice)) {
                            System.out.println("Giá không hợp lệ!");
                            break;
                        }

                        List<Book> books =
                            bookDAO.searchByAuthorAndMinPrice(author, minPrice);
                        printBooks(books);
                    } catch (NumberFormatException e) {
                        System.out.println("Giá không hợp lệ!");
                    }
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Vui lòng chọn từ 1 đến 3!");
            }
        }
    }

    private static void sortMenu(Scanner sc) {
        while (true) {
            System.out.println("\n*********** SẮP XẾP SÁCH ***********");
            System.out.println("1. Sắp xếp theo giá tăng dần");
            System.out.println("2. Sắp xếp theo tên sách tăng dần");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": {
                    List<Book> books = bookDAO.getAll();
                    if (books != null) {
                        books.sort((a, b) ->
                            Double.compare(a.getPrice(), b.getPrice()));
                    }
                    printBooks(books);
                    break;
                }

                case "2": {
                    List<Book> books = bookDAO.getAll();
                    if (books != null) {
                        books.sort((a, b) ->
                            a.getTitle().compareToIgnoreCase(b.getTitle()));
                    }
                    printBooks(books);
                    break;
                }

                case "3":
                    return;

                default:
                    System.out.println("Vui lòng chọn từ 1 đến 3!");
            }
        }
    }

    private static void updateBook(Scanner sc) {
        System.out.print("Nhập mã sách cần cập nhật: ");
        String id = sc.nextLine().trim();

        Book oldBook = bookDAO.getById(id);

        if (oldBook == null) {
            System.out.println("Không tìm thấy sách!");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        printBook(oldBook);

        System.out.println("\nNhập thông tin mới:");

        System.out.print("Tên sách mới: ");
        String title = sc.nextLine().trim();

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

        System.out.print("Giá mới: ");
        double price;
        try {
            price = Double.parseDouble(sc.nextLine().trim());
            if (price <= 0 || Double.isNaN(price)
                    || Double.isInfinite(price)) {
                System.out.println("Giá phải lớn hơn 0!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Giá không hợp lệ!");
            return;
        }

        System.out.print("Mô tả mới: ");
        String description = sc.nextLine().trim();

        System.out.print("Tác giả mới: ");
        String author = sc.nextLine().trim();

        System.out.println("Danh sách danh mục:");
        List<Category> categories = categoryDAO.getAll();

        if (categories == null || categories.isEmpty()) {
            System.out.println("Chưa có danh mục!");
            return;
        }

        for (Category c : categories) {
            System.out.println(c.getId() + " - " + c.getName());
        }

        System.out.print("Mã danh mục mới: ");
        String categoryId = sc.nextLine().trim();

        if (categoryDAO.getById(categoryId) == null) {
            System.out.println("Danh mục không tồn tại!");
            return;
        }

        oldBook.setTitle(title);
        oldBook.setStatus(status);
        oldBook.setPrice(price);
        oldBook.setDescription(description);
        oldBook.setAuthor(author);
        oldBook.setCategoryId(categoryId);

        if (bookDAO.update(oldBook)) {
            System.out.println("Cập nhật sách thành công!");
        } else {
            System.out.println("Cập nhật sách thất bại!");
        }
    }

    private static void printBooks(List<Book> books) {
        if (books == null || books.isEmpty()) {
            System.out.println("Không có sách phù hợp!");
            return;
        }

        System.out.println("\n========== DANH SÁCH SÁCH ==========");
        for (Book book : books) {
            printBook(book);
            System.out.println("------------------------------------");
        }
    }

    private static void printBook(Book b) {
        System.out.println("Mã sách: " + b.getId());
        System.out.println("Tên sách: " + b.getTitle());
        System.out.println("Trạng thái: "
            + (b.isStatus() ? "Đang hoạt động" : "Ngừng hoạt động"));
        System.out.println("Giá: " + b.getPrice());
        System.out.println("Mô tả: " + b.getDescription());
        System.out.println("Tác giả: " + b.getAuthor());
        System.out.println("Mã danh mục: " + b.getCategoryId());
    }
}
