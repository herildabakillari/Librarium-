public class TestDB {
    public static void main(String[] args) {
        try (java.sql.Connection conn = DBConnection.getConnection()) {
            System.out.println("✅ Lidhja me databazën u krye me sukses!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
