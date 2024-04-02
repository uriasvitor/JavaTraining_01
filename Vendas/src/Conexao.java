import java.sql.DriverManager;
import java.sql.*;
public class Conexao {
    private static  String servidor="jdbc:mysql://localhost:3306/mysqlvendas";
    private static  String usuario="root";
    private static  String senha="123456";
    private static String driver="com.mysql.cj.jdbc.Driver";


    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(servidor, usuario, senha);
    }

}
