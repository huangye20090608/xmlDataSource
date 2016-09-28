import java.sql.*;

/**
 * Created by zhangzhonghua on 2016/9/13.
 */
public class ConnectionUtils {
    private static String url = "jdbc:mysql://192.168.149.57:3306/ecc_b2b_meta?characterEncoding=utf8&connectTimeout=1000&autoReconnect=true";
    private static String username = "root";
    private static String password = "1qaz2wsx";

    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            con = null;
        }
    }

    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            stmt = null;
        }
    }

    public static void closeStatement(PreparedStatement pstmt) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            pstmt = null;
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rs = null;
        }
    }
}
