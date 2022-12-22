package DAO;

import static Procedure.Procedures.conex;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CRUD_Cotizaciones {

    public static boolean registrarCotizacion(String cotizacion, String licitacion, int estado) {

        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("INSERT INTO dbo.cotizaciones (cotizacion,licitacion,estado) VALUES ('"
                    + cotizacion + "', '"
                    + licitacion + "', "
                    + estado + ")");

            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static ArrayList<String> getCotizacion(String licitacion) {
        ArrayList<String> arrCotiaciones = new ArrayList<>();
        String cotizacion = "";
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            try ( ResultSet res = estatuto.executeQuery("SELECT cotizacion FROM dbo.cotizaciones WHERE licitacion = '" + licitacion + "'")) {
                while (res.next()) {
                    cotizacion = (res.getString("cotizacion"));

                    arrCotiaciones.add(cotizacion);
                }
            }
            estatuto.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arrCotiaciones;
    }
    public static boolean validateCotizacion(String licitacion) {
 
        String cotizacion = "";
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            try ( ResultSet res = estatuto.executeQuery("SELECT cotizacion FROM dbo.cotizaciones WHERE licitacion = '" + licitacion + "'")) {
                if (res.next()) {
                    cotizacion = (res.getString("cotizacion"));

                }
            }
            estatuto.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return !cotizacion.equals("");
    }

    public static boolean eliminaCotizacion(String cotizacion) {
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.execute("DELETE from dbo.cotizaciones WHERE cotizacion = '" + cotizacion + "'");
            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean updateCotizacion(String licitacion) {
        boolean bool = false;
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("UPDATE dbo.cotizaciones SET estado =  1 WHERE licitacion = '" + licitacion + "'");
            estatuto.close();
            bool = true;
        } catch (SQLException ex) {
            Logger.getLogger(CRUD_Cotizaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }
}
