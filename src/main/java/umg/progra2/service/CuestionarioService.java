package umg.progra2.service;

import umg.progra2.dao.CuestionarioDao;
import umg.progra2.db.DatabaseConnection;
import umg.progra2.db.TransactionManager;
import umg.progra2.model.Cuestionario;
import umg.progra2.model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class CuestionarioService {

    private CuestionarioDao cuestionarioDao = new CuestionarioDao();

    public void crearUsuario(Cuestionario cuestionario) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            TransactionManager tm = new TransactionManager(connection);
            tm.beginTransaction();
            try {
                cuestionarioDao.insertUser(cuestionario);
                tm.commit();
            } catch (SQLException e) {
                tm.rollback();
                throw e;
            }
        }
    }
}
