package DAO;

import static Procedure.Procedures.conex;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CRUD_PalabrasClaves {

    public static boolean registrarPalabraClave(String palabraClave) {

        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("INSERT INTO dbo.palabrasClaves (palabra) VALUES ('" + palabraClave + "')");
            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static ArrayList<String> getPalabrasClaves() {
        ArrayList<String> arrPalabrasClaves = new ArrayList<>();
        String cotizacion = "";
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            try ( ResultSet res = estatuto.executeQuery("SELECT palabra FROM dbo.palabrasClaves")) {
                while (res.next()) {
                    cotizacion = (res.getString("palabra"));

                    arrPalabrasClaves.add(cotizacion);
                }
            }
            estatuto.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arrPalabrasClaves;
    }

    public static boolean eliminaPalabraClave(String palabra) {
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.execute("DELETE from dbo.palabrasClaves WHERE palabra = '" + palabra + "'");
            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
