package DAO;

import Objects.LicitacionAgil;
import static Procedure.Procedures.conex;
import java.sql.SQLException;
import java.sql.Statement;

public class CRUD_LicitacionAgil {

    public static boolean registrarLicitacionAgil(LicitacionAgil lici, int estado) {

        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("INSERT INTO dbo.licitaciones (licitacion,organismo,nombre,descripcion,descripcionProduct,fecha,ofertas,estado) VALUES ('"
                    + lici.getId() + "', '"
                    + lici.getUnidadCompra() + "', '"
                    + lici.getNombre() + "', 'AGIL', 'AGIL', '"
                    + lici.getFechaCierre() + "',1, "
                    + estado + ")");
            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
