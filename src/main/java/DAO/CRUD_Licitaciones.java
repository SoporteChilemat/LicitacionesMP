package DAO;

import Objects.Licitacion;
import static Procedure.Procedures.conex;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CRUD_Licitaciones {

    public static boolean registrarLicitacion(Licitacion lici, int estado, int flag) {
        String query = "";
        if (flag == 0) {
            query = "INSERT INTO dbo.licitaciones (licitacion,organismo,nombre,descripcion,descripcionProduct,fecha,ofertas,estado) VALUES ('"
                    + lici.getNumAdqui() + "', '"
                    + lici.getOrganismo() + "', '"
                    + lici.getNombreAdqui() + "', '"
                    + lici.getDescrip() + "', '"
                    + lici.getDescripProducto() + "', '"
                    + lici.getFechaCierre() + "',1, "
                    + estado + ")";
        } else {
            query = "INSERT INTO dbo.licitaciones (licitacion,organismo,nombre,descripcion,descripcionProduct,fecha,ofertas,estado) VALUES ('"
                    + lici.getNumAdqui() + "', '"
                    + lici.getOrganismo() + "', '"
                    + lici.getNombreAdqui() + "', '"
                    + lici.getDescrip() + "', 'AGIL', '"
                    + lici.getFechaCierre() + "',1, "
                    + estado + ")";
        }
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate(query);
            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean updateLicitacion(String licitacion, int estado) {
        boolean bool = false;
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("UPDATE dbo.licitaciones SET estado =  " + estado + " WHERE licitacion = '" + licitacion + "'");
            estatuto.close();
            bool = true;
        } catch (SQLException ex) {
            Logger.getLogger(CRUD_Licitaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }

    public static boolean updateDirecc(String dir, String oc) {
        boolean bool = false;
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("UPDATE dbo.licitaciones SET dir =  '" + dir.toUpperCase() + "' WHERE numOC = '" + oc + "'");
            estatuto.close();
            bool = true;
        } catch (SQLException ex) {
            Logger.getLogger(CRUD_Licitaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }

    public static boolean updateFechaTermino(String fecha, String oc) {
        boolean bool = false;
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("UPDATE dbo.licitaciones SET fechaTermino =  '" + fecha + "' WHERE numOC = '" + oc + "'");
            estatuto.close();
            bool = true;
        } catch (SQLException ex) {
            Logger.getLogger(CRUD_Licitaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }

    public static void agregarOC(String licitacion, String oc, String valor, String fechaOC, String direccion) {
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate("UPDATE dbo.licitaciones SET numOC =  '" + oc + "',"
                    + " valor = " + valor
                    + ", fechaOC = '" + fechaOC + "'"
                    + ", dir = '" + direccion + "'"
                    + " WHERE licitacion = '" + licitacion + "'");
            estatuto.close();
        } catch (SQLException ex) {
            Logger.getLogger(CRUD_Licitaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<Licitacion> getLicitaciones() {
        ArrayList<Licitacion> arrCotiaciones = new ArrayList<>();

        try ( Statement estatuto = conex.getConnection().createStatement()) {
            try ( ResultSet res = estatuto.executeQuery("SELECT * FROM dbo.licitaciones")) {
                while (res.next()) {
                    Licitacion lici = new Licitacion();
                    lici.setNumAdqui(res.getString("licitacion"));
                    lici.setOrganismo(res.getString("organismo"));
                    lici.setNombreAdqui(res.getString("nombre"));
                    lici.setDescrip(res.getString("descripcion"));
                    lici.setDescripProducto(res.getString("descripcionProduct"));
                    lici.setFechaCierre(res.getString("fecha"));
                    lici.setOfertas(res.getString("ofertas"));
                    lici.setEstado(res.getInt("estado"));
                    lici.setNumOC(res.getString("numOC"));
                    lici.setFechaOC(res.getString("fechaOC"));
                    lici.setFechaTermino(res.getString("fechaTermino"));
                    lici.setDireccion(res.getString("dir"));
                    lici.setValor(res.getInt("valor"));

                    arrCotiaciones.add(lici);
                }
            }
            estatuto.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arrCotiaciones;
    }

    public static boolean estaRegistrada(Licitacion lici) {

        String esta = "";
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            try ( ResultSet res = estatuto.executeQuery("SELECT * FROM dbo.licitaciones WHERE licitacion = '" + lici.getNumAdqui() + "'")) {
                if (res.next()) {
                    esta = res.getString("licitacion");
                }
            }
            estatuto.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return !esta.equals("");
    }

    public static boolean registrarCoti(Licitacion lici, String coti) {
        String query = "INSERT INTO dbo.cotizaciones (cotizacion,licitacion,estado) VALUES ('"
                + coti + "', '"
                + lici.getNumAdqui() + "', "
                + 0 + ")";
        try ( Statement estatuto = conex.getConnection().createStatement()) {
            estatuto.executeUpdate(query);
            estatuto.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
