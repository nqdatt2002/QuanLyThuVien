package mainMenu;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n***************** MENU ***************");
            System.out.println("1. Quản lý danh mục");
            System.out.println("2. Quản lý sách");
            System.out.println("3. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    CategoryMenu.showMenu(sc);
                    break;

                case "2":
                    BookMenu.showMenu(sc);
                    break;

                case "3":
                    System.out.println("Đã thoát chương trình!");
                    sc.close();
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}
