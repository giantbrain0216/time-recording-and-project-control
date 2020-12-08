import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

public class Application {
    protected ConnectionSource connectionSource;

    public Application() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");
    }


}
