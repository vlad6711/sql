import java.sql.*;
import java.util.Scanner;

public class Main {
    // ввод с клавиатуры
    protected final static Scanner sc = new Scanner(System.in);

    // вывод принта
    static {
        System.out.println("Введите название таблицы: ");
    }

    // наименование таблицы
    protected final static String tableName = sc.nextLine();

    // url для подключения к бд
    protected final static String mysqlUrl = "jdbc:mysql://localhost:3306/My_cats";
    // класс для создания потока подключения к БД (инициализация подключения)
    protected static Connection conn;

    // подключение к БД через конструктор исключений
    static {
        try {
            conn = DriverManager.getConnection(mysqlUrl, "root", "root");
            System.out.println("Подключение прошло успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД:" + e);
        }
    }

    // Метод вывода таблиц из БД
    public static void viewTables() throws SQLException {
        String sql = "show tables"; // sql запрос
        PreparedStatement ps = conn.prepareStatement(sql); // класс для отправки sql-запроса
        /*
        executeQuery() - метод класса PreparedStatement для вывода значений из БД
        executeUpdate() - метод класса PreparedStatement для отправки значений из БД
         */
        ResultSet rs = ps.executeQuery();
        System.out.println("Название таблицы в БД:");
        int count = 1;
        // цикл с условием. Выполняется до тех пор пока есть строка (мктод next())
        while (rs.next()) {
            String tableName = rs.getString(1); // getString - вывод значения таблицы из БД по индексу
            System.out.println(count++ + ". " + tableName);
        }
    }

    public static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (ID int PRIMARY KEY AUTO_INCREMENT, types varchar(255) NOT NULL)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        System.out.println("Таблица `" + tableName + "` успешно создана!");
    }

    public static void insertIntoTable() throws SQLException {
        sc.nextLine();
        System.out.println("Введите тип: ");
        String type = sc.nextLine();
//        System.out.println("Введите фамилию: ");
//        String lastName = sc.nextLine();
        String sql = "INSERT INTO `" + tableName + "` (types) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, type);
        ps.executeUpdate();
//        System.out.println("Студент с именем `" + firstName + "` и фамилией `" + lastName + "` успешно добавлен!");
    }

    public static void viewsDataTable() throws SQLException {
        String sql = "SELECT * FROM `" + tableName + "`";
        System.out.printf("%1s | %-10s |\n", "ID", "types");
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs  = ps.executeQuery();
        while (rs.next()) {
            int ID = rs.getInt("ID");
            String types = rs.getString("types");
            System.out.printf("%1d. | %-10s |\n", ID, types);
        }
    }

    public static void updateDataTable() throws SQLException {
        System.out.println("Введите ID строки, которую хотите изменить: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.println("Введите новый тип: ");
        String type = sc.nextLine();
//        System.out.println("Введите новую фамилию: ");
//        String lastName = sc.nextLine();
        String sql = "UPDATE `" + tableName + "` SET types = ? WHERE ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, type);
        ps.setInt(2, id);
        ps.executeUpdate();
        System.out.println("Мы успешно обновили строку с ID `" + id + "`.");
    }

    public static void deleteDataTable() throws SQLException {
        System.out.println("Введите ID строки, которую хотите удалить: ");
        int id = sc.nextInt();
        sc.nextLine();
        String sql = "DELETE FROM `" + tableName + "` WHERE ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Строка с ID `" + id + "` успешно удалена!");

    }

    public static void main(String[] args) throws SQLException {
        int x = 0;
        String s = "";
        while (!"7".equals(s)) {
            System.out.println("---------------------------\n" +
                    "1. Вывести таблицы из БД\n" +
                    "2. Создать таблицу в БД\n" +
                    "3. Добавить данные в таблицу\n" +
                    "4. Вывести данные из таблицы\n" +
                    "5. Обновить данные в БД\n" +
                    "6. Удалить данные из БД\n" +
                    "7. Выход из БД");
            s = sc.next();

            try {
                x = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода");
            }

            switch (x) {
                case 1 -> Main.viewTables();
                case 2 -> Main.createTable();
                case 3 -> Main.insertIntoTable();
                case 4 -> Main.viewsDataTable();
                case 5 -> Main.updateDataTable();
                case 6 -> Main.deleteDataTable();
            }

        }
    }
}
