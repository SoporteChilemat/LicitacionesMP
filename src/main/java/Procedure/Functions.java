package Procedure;

import DAO.CRUD_Cotizaciones;
import DAO.CRUD_Licitaciones;
import DAO.CRUD_PalabrasClaves;
import Objects.Licitacion;
import View.DropOc;
import View.View;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class Functions {

    public static DefaultTableModel modeloLicitacionesAgil() {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Descripcion");
        model.addColumn("Nombre");
        model.addColumn("Organismo");
        model.addColumn("Fecha Cierre");
        model.addColumn("Ofertas");
        model.addColumn("Monto");
        model.addColumn("Fecha OC");
        model.addColumn("Fecha Termino");
        model.addColumn("Direccion");
        model.addColumn("NumOC");
        model.addColumn("Cargar");
        model.addColumn("Aceptado");

        return model;
    }

    public static DefaultTableModel modeloLicitaciones() {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Numero");
        model.addColumn("Organismo");
        model.addColumn("Nombre");
        model.addColumn("Descripcion Producto");
        model.addColumn("Fecha Cierre");
        model.addColumn("Ofertas");
        model.addColumn("pos");
        model.addColumn("Monto");
        model.addColumn("Fecha OC");
        model.addColumn("Fecha Termino");
        model.addColumn("Direccion");
        model.addColumn("NumOC");
        model.addColumn("Cargar");
        model.addColumn("Aceptado");

        return model;
    }

    public static DefaultTableModel modeloCoti() {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Cotizacion");
        model.addColumn("Eliminar");
        return model;
    }

    public static DefaultTableModel modelPalabrasClaves() {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Palabra clave");
        model.addColumn("Eliminar");
        return model;
    }

    public static ArrayList<Licitacion> leerLicitacionesAgil(File file) {

        ArrayList<Licitacion> arrLicitaciones = new ArrayList<>();

        BufferedReader br = null;
        String st = "";
        String html = "";
        String comienzo = "<td>";
        String termino = "</td>";

        try {
            br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null) {
                html += st + "\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] split = html.split("\\r?\\n");
        List<String> list = Arrays.asList(split);

        for (int i = 0; i < list.size(); i++) {
            String linea = list.get(i);

            if (!linea.contains("style") && !linea.contains("table>") && !linea.contains("tr>")) {
//                
                Licitacion liciAgil = new Licitacion();

                int indexOf = linea.indexOf(comienzo);
                linea = linea.substring(indexOf + comienzo.length());

                //ID
                indexOf = linea.indexOf(termino);
                liciAgil.setNumAdqui(linea.substring(0, indexOf));
                if (!liciAgil.getNumAdqui().equals("ID")) {

                    linea = linea.substring(indexOf + termino.length());

                    //DESSCRIP
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    liciAgil.setDescrip(linea.substring(0, indexOf));
                    linea = linea.substring(indexOf + termino.length());

                    //NOMBRE
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    liciAgil.setNombreAdqui(linea.substring(0, indexOf));
                    linea = linea.substring(indexOf + termino.length());

                    //FECHA DE PUBLICACION
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    linea = linea.substring(indexOf + termino.length());

                    //FECHA DE DE CIERRE
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    liciAgil.setFechaCierre(linea.substring(0, indexOf));
                    linea = linea.substring(indexOf + termino.length());

                    //FECHA ESTADO
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    linea = linea.substring(indexOf + termino.length());

                    //COTIZACIONES ENVIADAS
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    liciAgil.setOfertas(linea.substring(0, indexOf));
                    linea = linea.substring(indexOf + termino.length());

                    //INSTITUCION
                    indexOf = linea.indexOf(comienzo);
                    linea = linea.substring(indexOf + comienzo.length());
                    indexOf = linea.indexOf(termino);
                    liciAgil.setOrganismo(linea.substring(0, indexOf));
                    linea = linea.substring(indexOf + termino.length());

                    arrLicitaciones.add(liciAgil);
                }
            }
        }
        return arrLicitaciones;
    }

    public static ArrayList<Licitacion> leerLicitaciones2(File file) {

        if (file.getName().contains("xlsx")) {
            return licitaciones2XLSX(file);
        }

        ArrayList<Licitacion> arrLicitaciones = new ArrayList<>();
        InputStream ExcelFileToRead = null;
        try {
            ExcelFileToRead = new FileInputStream(file.getAbsolutePath());
            HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            Iterator rows = sheet.rowIterator();
            rows.next();
            rows.next();

            int row1 = 1;
            int ultimaLinea = 0;
            boolean bool = false;

            while (rows.hasNext()) {
                int cont = 0;

                row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = (HSSFCell) cells.next();
                    switch (cont) {
                        case 0: 
                            try {
                            String buscar = cell.getStringCellValue();
                            row1++;
                            if (buscar.contains("Numero")) {
                                ultimaLinea = sheet.getLastRowNum();
                                bool = true;
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("e " + e);
                        }
                        break;
                    }
                    if (bool) {
                        break;
                    }
                    cont++;
                }
                if (bool) {
                    break;
                }
            }
            ultimaLinea = ultimaLinea - 8;

            while (row1 != ultimaLinea) {
                Licitacion lici = new Licitacion();
                int cont = 0;

                row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                while (cells.hasNext()) {
                    cell = (HSSFCell) cells.next();
                    switch (cont) {
                        case 0: //ID
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setNumAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setNumAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setNumAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setNumAdqui(String.valueOf(valor));
                                }
                            }
                            break;
                        case 1: //TIPO ADQUISICION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setTipoAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setTipoAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setTipoAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setTipoAdqui(String.valueOf(valor));
                                }
                            }
                        case 3: //NOMBRE ADQUISICION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setNombreAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setNombreAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {

                                    lici.setNombreAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setNombreAdqui(String.valueOf(valor));
                                }
                            }

                            break;
                        case 4: //DESCRIPCION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setDescrip(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setDescrip(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setDescrip(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setDescrip(String.valueOf(valor));
                                }
                            }

                            break;
                        case 5: //ORGANISMO
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setOrganismo(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setOrganismo(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setOrganismo(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setOrganismo(String.valueOf(valor));
                                }
                            }

                            break;
                        case 6: //REGION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setRegion(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setRegion(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setRegion(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setRegion(String.valueOf(valor));
                                }
                            }

                            break;
                        case 8: //FECHA PUBLICACION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setFechaPubli(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    Date javaDate = DateUtil.getJavaDate((double) cell.getNumericCellValue());
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setFechaPubli(fecha);
                                }
                            } else {
                                try {
                                    lici.setFechaPubli(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setFechaPubli(String.valueOf(valor));
                                }
                            }
                            break;
                        case 9: //FECHA CIERRE
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setFechaCierre(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setFechaCierre(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setFechaCierre(cell.getStringCellValue());
                                } catch (Exception e) {
                                    Date javaDate = DateUtil.getJavaDate((double) cell.getNumericCellValue());
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setFechaCierre(fecha);
                                }
                            }
                            break;
                        case 10: //DESCRIPCION DE PRODUCTO
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setDescripProducto(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setDescripProducto(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setDescripProducto(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setDescripProducto(String.valueOf(valor));
                                }
                            }
                            break;
                    }
                    cont++;
                }
                row1++;
                arrLicitaciones.add(lici);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ExcelFileToRead.close();
            } catch (IOException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return arrLicitaciones;
    }

    public static ArrayList<Licitacion> licitaciones2XLSX(File file) {
        ArrayList<Licitacion> arrLicitaciones = new ArrayList<>();
        InputStream ExcelFileToRead = null;
        try {
            ExcelFileToRead = new FileInputStream(file.getAbsolutePath());
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            Iterator rows = sheet.rowIterator();
            rows.next();
            rows.next();

            int row1 = 1;
            int ultimaLinea = 0;
            boolean bool = false;

            while (rows.hasNext()) {
                int cont = 0;

                row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = (XSSFCell) cells.next();
                    switch (cont) {
                        case 0: 
                            try {
                            String buscar = cell.getStringCellValue();
                            row1++;
                            if (buscar.contains("Numero")) {
                                ultimaLinea = sheet.getLastRowNum();
                                bool = true;
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("e " + e);
                        }
                        break;
                    }
                    if (bool) {
                        break;
                    }
                    cont++;
                }
                if (bool) {
                    break;
                }
            }
            ultimaLinea = ultimaLinea - 8;

            while (row1 != ultimaLinea) {
                Licitacion lici = new Licitacion();
                int cont = 0;

                row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                while (cells.hasNext()) {
                    cell = (XSSFCell) cells.next();
                    switch (cont) {
                        case 0: //ID
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setNumAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setNumAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setNumAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setNumAdqui(String.valueOf(valor));
                                }
                            }
                            break;
                        case 1: //TIPO ADQUISICION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setTipoAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setTipoAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setTipoAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setTipoAdqui(String.valueOf(valor));
                                }
                            }
                        case 3: //NOMBRE ADQUISICION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setNombreAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setNombreAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {

                                    lici.setNombreAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setNombreAdqui(String.valueOf(valor));
                                }
                            }

                            break;
                        case 4: //DESCRIPCION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setDescrip(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setDescrip(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setDescrip(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setDescrip(String.valueOf(valor));
                                }
                            }

                            break;
                        case 5: //ORGANISMO
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setOrganismo(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setOrganismo(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setOrganismo(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setOrganismo(String.valueOf(valor));
                                }
                            }

                            break;
                        case 6: //REGION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setRegion(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setRegion(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setRegion(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setRegion(String.valueOf(valor));
                                }
                            }

                            break;
                        case 8: //FECHA PUBLICACION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setFechaPubli(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    Date javaDate = DateUtil.getJavaDate((double) cell.getNumericCellValue());
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setFechaPubli(fecha);
                                }
                            } else {
                                try {
                                    lici.setFechaPubli(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setFechaPubli(String.valueOf(valor));
                                }
                            }
                            break;
                        case 9: //FECHA CIERRE
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setFechaCierre(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setFechaCierre(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setFechaCierre(cell.getStringCellValue());
                                } catch (Exception e) {
                                    Date javaDate = DateUtil.getJavaDate((double) cell.getNumericCellValue());
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setFechaCierre(fecha);
                                }
                            }
                            break;
                        case 10: //DESCRIPCION DE PRODUCTO
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setDescripProducto(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setDescripProducto(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setDescripProducto(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setDescripProducto(String.valueOf(valor));
                                }
                            }
                            break;
                    }
                    cont++;
                }
                row1++;
                arrLicitaciones.add(lici);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ExcelFileToRead.close();
            } catch (IOException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return arrLicitaciones;
    }

    public static String agregarSaltos(String descrip) {
        if (descrip != null && descrip.contains("\n")) {
            descrip = descrip.replaceAll("\n", "");
        }

        String texto = "";
        if (descrip != null) {
            int cont = 0;
            for (int k = 0; k < descrip.length(); k++) {

                if (cont <= 30) {
                    if (cont >= 25 && cont <= 30) {
                        if (descrip.charAt(k) == ' ') {
                            texto += descrip.charAt(k) + "\n";
                            cont = 0;
                        } else {
                            texto += descrip.charAt(k);
                        }
                    } else {
                        texto += descrip.charAt(k);
                    }
                } else {
                    texto += descrip.charAt(k) + "-\n";
                    cont = 0;
                }
                cont++;
            }
        }

        return texto;
    }

    public static ArrayList<Licitacion> addBorder(File fileTxt) {

        ArrayList<Licitacion> arrLicitaciones = new ArrayList<>();

        BufferedReader br = null;
        String st = "";
        String txt = "";

        try {
            br = new BufferedReader(new FileReader(fileTxt));
            while ((st = br.readLine()) != null) {
                txt += st + "\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] split = txt.split("\\r?\\n");
        List<String> list = Arrays.asList(split);

        int cont = 0; // cont = 8 
        int indexOf = 0;
        Licitacion lici = null;

        // Variables de licitaciones
        String numeroLici = "";
        String nombreLici = "";
        String descripcion = "";
        String organismo = "";
        String fechaPubli = "";
        String fechaCierra = "";
        String estado = "";
        String ofertas = "";

        for (int i = 0; i < list.size(); i++) {

            String linea = list.get(i).trim();
            if (!linea.contains("style") && !linea.contains("table>")) {
                if (!linea.equals("") && !linea.contains("Mis Ofertas")) {

                    linea = linea.replaceAll("\n", "");
                    linea = linea.replaceAll(";", ",");
                    linea = linea.replaceAll("<tr>", "");
                    linea = linea.replaceAll("</tr>", "");
                    linea = linea.replaceAll("</tr>", "");
                    linea = linea.replaceAll("<td>", "");
                    linea = linea.replaceAll("</td>", ";");

                    //NUMERO DE LICITAICON 
                    if (cont == 0) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            numeroLici = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            numeroLici += " " + linea;
                        }
                    }

                    //NOMBRE DE LICITACION
                    if (cont == 1) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            nombreLici = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            nombreLici += " " + linea;
                        }
                    }

                    //DESCRIPCION
                    if (cont == 2) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            if (indexOf == 0) {
                                linea = linea.substring(indexOf + 1);
                                cont++;
                            } else {
                                descripcion = linea.substring(0, indexOf);
                                linea = linea.substring(indexOf + 1);
                                cont++;
                            }
                        } else {
                            descripcion += " " + linea;
                        }
                    }

                    //ORGANISMO / DEMANDANTE
                    if (cont == 3) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            organismo = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            organismo += " " + linea;
                        }
                    }

                    //FECHA DE PUBLICACION
                    if (cont == 4) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            fechaPubli = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            fechaPubli += " " + linea;
                        }
                    }

                    //FECHA DE CIERRE
                    if (cont == 5) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            fechaCierra = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            fechaCierra += " " + linea;
                        }
                    }

                    //ESTADO
                    if (cont == 6) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            estado = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            estado += " " + linea;
                        }
                    }

                    //OFERTAS
                    if (cont == 7) {
                        indexOf = linea.indexOf(";");
                        if (indexOf != -1) {
                            ofertas = linea.substring(0, indexOf);
                            linea = linea.substring(indexOf + 1);
                            cont++;
                        } else {
                            ofertas += " " + linea;
                        }
                    }
                }
            }

            if (cont == 8) {

                lici = new Licitacion();

                lici.setNumAdqui(numeroLici);
                lici.setNombreAdqui(nombreLici);
                lici.setDescrip(descripcion);
                lici.setOrganismo(organismo);
                lici.setFechaPubli(fechaPubli);
                lici.setFechaCierre(fechaCierra);
                lici.setOfertas(ofertas);

                arrLicitaciones.add(lici);

                cont = 0;
            }

        }
        return arrLicitaciones;
    }

    public static ArrayList<Licitacion> leerXLS(File file) {
        InputStream ExcelFileToRead = null;
        ArrayList<Licitacion> arrLicitaciones = new ArrayList<>();
        try {
            ExcelFileToRead = new FileInputStream(file.getAbsolutePath());
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            Iterator rows = sheet.rowIterator();
            rows.next();
            rows.next();
            while (rows.hasNext()) {
                Licitacion lici = new Licitacion();
                int cont = 0;
                row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = (XSSFCell) cells.next();

                    switch (cont) {
                        case 0: //ID
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setNumAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setNumAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setNumAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setNumAdqui(String.valueOf(valor));
                                }
                            }
                            break;
                        case 1: //NOMBRE
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setNombreAdqui(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setNombreAdqui(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setNombreAdqui(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setNombreAdqui(String.valueOf(valor));
                                }
                            }
                        case 2: //UNIDAD DE COMPRA
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setDescrip(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setDescrip(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setDescrip(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setDescrip(String.valueOf(valor));
                                }
                            }

                            break;
                        case 3: //FECHA DE CIERRE
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setOrganismo(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setOrganismo(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setOrganismo(cell.getStringCellValue());
                                } catch (Exception e) {
                                    String valueOf = String.valueOf(cell.getNumericCellValue());
                                    if (valueOf.contains("@")) {
                                        valueOf = valueOf.replace("@", "-");
                                    }
                                    Date javaDate = DateUtil.getJavaDate(Double.valueOf(valueOf));
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setOrganismo(fecha);
                                }
                            }

                            break;
                        case 4: //COTIZACIONES ENVIADAS
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setFechaPubli(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setFechaPubli(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setFechaPubli(cell.getStringCellValue());
                                } catch (Exception e) {
                                    String valueOf = String.valueOf(cell.getNumericCellValue());
                                    if (valueOf.contains("@")) {
                                        valueOf = valueOf.replace("@", "-");
                                    }
                                    Date javaDate = DateUtil.getJavaDate(Double.valueOf(valueOf));
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setFechaPubli(fecha);
                                }
                            }

                            break;
                        case 5: //INSTITUCION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setFechaCierre(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setFechaCierre(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setFechaCierre(cell.getStringCellValue());
                                } catch (Exception e) {
                                    String valueOf = String.valueOf(cell.getNumericCellValue());
                                    if (valueOf.contains("@")) {
                                        valueOf = valueOf.replace("@", "-");
                                    }
                                    Date javaDate = DateUtil.getJavaDate(Double.valueOf(valueOf));
                                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
                                    lici.setFechaCierre(fecha);
                                }
                            }
                            break;
                        case 7: //INSTITUCION
                            if (cell.getCellType() == CellType.FORMULA) {
                                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                                    lici.setOfertas(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                                    lici.setOfertas(cell.getStringCellValue());
                                }
                            } else {
                                try {
                                    lici.setOfertas(cell.getStringCellValue());
                                } catch (Exception e) {
                                    int valor = (int) cell.getNumericCellValue();
                                    lici.setOfertas(String.valueOf(valor));
                                }
                            }
                            break;
                    }
                    cont++;

                }
                arrLicitaciones.add(lici);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ExcelFileToRead.close();
            } catch (IOException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arrLicitaciones;
    }

    public static ArrayList<Licitacion> cruzarDescripciones(ArrayList<Licitacion> arrLici) {

        ArrayList<Licitacion> arrSinDuplicarConDescripcion = arrSinDuplicados();

        arrSinDuplicarConDescripcion.forEach((Licitacion liciGeneral) -> {
            arrLici.forEach((Licitacion lici) -> {
                if (liciGeneral.getNumAdqui().equals(lici.getNumAdqui())) {
                    String descripProducto = "";
                    if (lici.getDescripProducto() != null) {
                        descripProducto = lici.getDescripProducto();
                    }
                    lici.setDescripProducto(descripProducto + liciGeneral.getDescripProducto() + "\n");
                }
            });
        });
        return arrLici;
    }

    private static void mostrar(ArrayList<Licitacion> arrSinDuplicarConDescripcion, ArrayList<Licitacion> arrLici) {

//        arrSinDuplicarConDescripcion.parallelStream().forEach((Licitacion lici) -> {
//            System.out.println(lici.getNumAdqui());
//        });
        arrLici.parallelStream().forEach((Licitacion lici) -> {
            System.out.println(lici.getNumAdqui());
        });

    }

    private static ArrayList<Licitacion> arrSinDuplicados() {
        ArrayList<Licitacion> leerLicitaciones2 = leerLicitaciones2(new File(Procedures.direccion + "\\Licitaciones\\Licitacion_Publicada.xlsx"));

        ArrayList<String> arrAux = new ArrayList<>();
        ArrayList<Licitacion> arrNuevo = new ArrayList<>();
        ArrayList<Licitacion> arrAuxiliar = new ArrayList<>();

        leerLicitaciones2.forEach((Licitacion lici) -> {
            arrAux.add(lici.getNumAdqui());
        });

        List<String> collect = arrAux.stream().distinct().collect(Collectors.toList());

        collect.forEach((String numAdqui) -> {

            for (Licitacion lici : leerLicitaciones2) {
                if ((lici.getNumAdqui().equals(numAdqui))) {
                    arrNuevo.add(lici);
                    break;
                }
            }
        });

        return arrNuevo;
    }

    public static ArrayList<Licitacion> cruzarConBD(ArrayList<Licitacion> arr, ArrayList<Licitacion> licitacionesBD, int flag) {

        ArrayList<Licitacion> arrNuevo = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            arrNuevo.add(arr.get(i));
        }
        arr.forEach((Licitacion lici) -> {
            licitacionesBD.forEach((Licitacion liciBD) -> {
                if (lici.getNumAdqui().equals(liciBD.getNumAdqui())) {
                    arrNuevo.remove(lici);
                }
            });
        });

        if (flag == 0) {
            licitacionesBD.forEach((Licitacion lici) -> {
                if (!lici.getDescripProducto().equals("AGIL")) {
                    arrNuevo.add(lici);
                }
            });
        } else {
            licitacionesBD.forEach((Licitacion lici) -> {
                if (lici.getDescripProducto().equals("AGIL")) {
                    arrNuevo.add(lici);
                }
            });
        }

        return arrNuevo;
    }

    public static String fecha() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String getFecha() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String fechaMercadoPublico(int flag) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String fechaHoy = dtf.format(now);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = format.parse(fechaHoy);
        } catch (ParseException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }

        Calendar calendario = new GregorianCalendar();
        //hacemos calculos sobre el calendario
        calendario.setTime(fecha);
        switch (flag) {
            case 0:
                //movemos el ccalendario
                calendario.add(Calendar.MONTH, -1);
                break;
            case 2:
                //movemos el ccalendario
                calendario.add(Calendar.MONTH, +1);
                break;
            default:
                //movemos el ccalendario
                calendario.add(Calendar.MONTH, -2);
                break;
        }

        return format.format(calendario.getTime());
    }

    public static String[] procedimientoOC(File file) {
        String text = leerPDF2(file);
        String areglo[] = new String[]{"", "", "", ""};

        String[] split = text.split("\\r?\\n");
        List<String> list = Arrays.asList(split);

        for (int i = 0; i < list.size(); i++) {
            String linea = list.get(i);
            System.out.println(linea);

            if (linea.contains("ORDEN DE COMPRA N")) {
                int indexOf = linea.indexOf(":");
                areglo[0] = linea.substring(indexOf + 1);
            }
            if (linea.contains("Fecha Envio OC.  :")) {
                int indexOf = linea.indexOf(":");
                areglo[2] = linea.substring(indexOf + 1).trim();
            }
            if (linea.contains("DIRECCION DE DESPACHO :")) {
                int indexOf = linea.indexOf(":");
                areglo[3] = linea.substring(indexOf + 1).trim();
            }
            if (linea.contains("Total        ")) {
                areglo[1] = list.get(i - 1).replace("$", "").replace(".", "").trim();
            }
        }

        return areglo;
    }

    public static String leerPDF2(File file) {
        String text = "";
        try {
            PdfReader reader = new PdfReader(file.getPath());

            int numberOfPages = reader.getNumberOfPages();
            System.out.println(numberOfPages);

            for (int i = 1; i < numberOfPages + 1; i++) {
                text += PdfTextExtractor.getTextFromPage(reader, i);
            }
            reader.close();
        } catch (IOException e) {
        }
        return text;
    }

    public static Object[] agregarLicitacionAgil(Licitacion l) {
        return new Object[]{l.getNumAdqui(), l.getDescrip(), l.getOrganismo(), l.getOrganismo(), l.getFechaCierre(), 0, "$0", "", "", "", "", null, false};
    }

    public static Object[] agregarLicitacion(Licitacion l) {
        return new Object[]{l.getNumAdqui(), l.getOrganismo(), l.getOrganismo(), l.getDescripProducto(), l.getFechaCierre(), 0, 0, "$0", "", "", "", "", null, false};
    }

}

class TablePopupEditor extends DefaultCellEditor implements TableCellEditor {

    private String currentText = "";
    ArrayList<Licitacion> arrLicitaciones;

    public TablePopupEditor(ArrayList<Licitacion> arrLicitaciones) {
        super(new JTextField());
        this.arrLicitaciones = arrLicitaciones;
        setClickCountToStart(0);

        editorComponent = new JButton();
        editorComponent.setBackground(Color.white);
        editorComponent.setFocusable(false);
    }

    public Object getCellEditorValue() {
        return currentText;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    PopupDialog popup = new PopupDialog();
                    Point p = editorComponent.getLocationOnScreen();
                    popup.setLocation(p.x, p.y + editorComponent.getSize().height);

                    Dimension dm = new Dimension(290, 341);
                    popup.setSize(dm);
                    Integer rowPos = Integer.valueOf(table.getValueAt(row, 6).toString());
                    String des = Functions.agregarSaltos(arrLicitaciones.get(rowPos).getDescripProducto());
                    popup.jTextArea1.setText(des);
                    popup.setResizable(false);
                    popup.setVisible(true);
                    fireEditingStopped();
                } catch (IOException ex) {
                    Logger.getLogger(TablePopupEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(TablePopupEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        currentText = value.toString();
//        editorComponent.setText(currentText);
        return editorComponent;
    }

    class PopupDialog extends JDialog {

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JPanel jPanel1 = new javax.swing.JPanel();
        JPanel jPanel2 = new javax.swing.JPanel();
        JLabel jLabel2 = new javax.swing.JLabel();

        JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        JTextArea jTextArea1 = new javax.swing.JTextArea();

        public PopupDialog() throws IOException, SQLException {
            super((Frame) null, "", true);

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            this.addWindowFocusListener(new WindowFocusListener() {
                public void windowGainedFocus(WindowEvent e) {
                }

                public void windowLostFocus(WindowEvent e) {
                    dispose();
                }
            });

            jTextArea1.setColumns(20);
            jTextArea1.setRows(5);
            jScrollPane2.setViewportView(jTextArea1);

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane2)
                                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                    .addContainerGap())
            );

            jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel2.setText("Descripción");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel2)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())
            );

        }

    }
}

///////////////////////// CLASE LICITACIONES //////////////////////////////////
class CustomRenderer implements TableCellRenderer {

    JPanel panel;
    JButton b;

    public CustomRenderer() {
        panel = new javax.swing.JPanel();
        b = new javax.swing.JButton();

        b.setText("Eliminar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

////////////////////////////////////////////////////////////////////////////////
class CustomEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    JPanel panel;
    JButton b;
    JTable jTable;
    int num;

    @Override
    public Object getCellEditorValue() {
        return panel;
    }

    public CustomEditor(JTable jTable, int num) {
        this.jTable = jTable;
        this.num = num;
        panel = new javax.swing.JPanel();
        b = new javax.swing.JButton();

        b.setText("Eliminar");
        b.addActionListener(this);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            String eliminar = jTable.getValueAt(jTable.getSelectedRow(), 0).toString();
            ((DefaultTableModel) jTable.getModel()).removeRow(jTable.getSelectedRow());

            if (this.num == 0) {
                if (CRUD_Cotizaciones.eliminaCotizacion(eliminar)) {
                    JOptionPane.showMessageDialog(null, "Se elimino la cotizacion N " + eliminar + " exitosamente",
                            "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se puedo eliminar la cotizacion N " + eliminar,
                            "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
                }
            } else {

                if (CRUD_PalabrasClaves.eliminaPalabraClave(eliminar)) {
                    JOptionPane.showMessageDialog(null, "Se elimino " + eliminar + " exitosamente",
                            "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, eliminar + " no se puedo eliminar",
                            "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
                }

            }
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            jTable.setModel(model);

        }
    }
}

class TablePopupCotizacionAgil extends DefaultCellEditor implements TableCellEditor {
    //private PopupDialog popup;

    private String currentText = "";
    private JButton editorComponent;
    private JTable jTable1 = new javax.swing.JTable();
    private JTextField jTextField1 = new javax.swing.JTextField();

    public TablePopupCotizacionAgil() {
        super(new JTextField());
        setClickCountToStart(0);
        editorComponent = new JButton();
        editorComponent.setBackground(Color.white);
        editorComponent.setBorderPainted(false);
        editorComponent.setContentAreaFilled(false);
        editorComponent.setFocusable(false);
    }

    public Object getCellEditorValue() {
        return currentText;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        SwingUtilities.invokeLater(() -> {
            try {
                PopupDialog popup = new PopupDialog();
                Point p = editorComponent.getLocationOnScreen();
                popup.setLocation(p.x, p.y + editorComponent.getSize().height);

                Dimension dm = new Dimension(230, 290);
                popup.setSize(dm);
                popup.setResizable(false);
                popup.setVisible(true);
                fireEditingStopped();

            } catch (IOException ex) {
                Logger.getLogger(TablePopupEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TablePopupEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        currentText = value.toString();
        editorComponent.setText(currentText);
        return editorComponent;
    }

    class PopupDialog extends JDialog {

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JPanel jPanel1 = new javax.swing.JPanel();
        JPanel jPanel2 = new javax.swing.JPanel();
        JPanel jPanel3 = new javax.swing.JPanel();
        JLabel jLabel1 = new javax.swing.JLabel();
        JButton jButton1 = new javax.swing.JButton();

        JScrollPane jScrollPane2 = new javax.swing.JScrollPane();

        public PopupDialog() throws IOException, SQLException {
            super((Frame) null, "", true);

            setTitle("Agregar Cotizacion");

            jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Cotizacion");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            //LLENAR TABLA
            int row = View.jTable2.getSelectedRow();
            String licitacion = View.jTable2.getValueAt(row, 0).toString();
            Procedures.llenarTablaCotizacion(licitacion, jTable1);
            jScrollPane1.setViewportView(jTable1);

            jButton1.setText("Agregar");
            jButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        jButton1ActionPerformed(e);
                    } catch (IOException ex) {
                        Logger.getLogger(TablePopupCotizacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            jTextField1 = new javax.swing.JTextField(6);
            jTextField1.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (jTextField1.getText().trim().length() == 6 || ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                        e.consume();
                    }
                }
            });

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jButton1)))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel3Layout.setVerticalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton1)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
        }

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        String cotizacion = jTextField1.getText();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{cotizacion, true});
        Licitacion lici = null;

        //Registrar Licitacion
        int row = View.jTable2.getSelectedRow();
        lici = new Licitacion();

        lici.setNumAdqui(View.jTable2.getValueAt(row, 0).toString());
        lici.setDescrip(View.jTable2.getValueAt(row, 1).toString());
        lici.setNombreAdqui(View.jTable2.getValueAt(row, 2).toString());
        lici.setOrganismo(View.jTable2.getValueAt(row, 3).toString());
        String fechaCierrre = View.jTable2.getValueAt(row, 4).toString();
        int indexOf = fechaCierrre.indexOf(" ");
        fechaCierrre = fechaCierrre.substring(0, indexOf).trim();
        lici.setFechaCierre(fechaCierrre);
        lici.setOfertas(View.jTable2.getValueAt(row, 5).toString());

        if (CRUD_Licitaciones.estaRegistrada(lici)) {
            if (CRUD_Licitaciones.registrarCoti(lici, cotizacion)) {
                View.jTable2.setValueAt(1, View.jTable2.getSelectedRow(), View.jTable2.getColumn("Ofertas").getModelIndex());
                stopCellEditing();
                JOptionPane.showMessageDialog(null, "Se registro correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error : No se puedo registrar esta licitacion",
                        "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (CRUD_Licitaciones.registrarLicitacion(lici, 0, 1)) {
                if (CRUD_Cotizaciones.registrarCotizacion(cotizacion, lici.getNumAdqui(), 1)) {
                    JOptionPane.showMessageDialog(null, "Se registro correctamente");
                    View.jTable2.setValueAt(1, View.jTable2.getSelectedRow(), View.jTable2.getColumn("Ofertas").getModelIndex());
                } else {
                    JOptionPane.showMessageDialog(null, "Error : No se puedo registrar esta licitacion",
                            "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error : No se puedo registrar esta cotizacion",
                        "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        }

        View.jTable2.revalidate();
        View.jTable2.repaint();
        jTextField1.setText("");
    }
}

class TablePopupCotizacion extends DefaultCellEditor implements TableCellEditor {

    //private PopupDialog popup;
    private String currentText = "";
    private JButton editorComponent;
    private JTable jTable1 = new javax.swing.JTable();
    private JTextField jTextField1 = new javax.swing.JTextField();
    ArrayList<Licitacion> arrLicitaciones;

    public TablePopupCotizacion(ArrayList<Licitacion> arrLicitaciones) {
        super(new JTextField());
        setClickCountToStart(0);
        this.arrLicitaciones = arrLicitaciones;
        editorComponent = new JButton();
        editorComponent.setBackground(Color.white);
        editorComponent.setBorderPainted(false);
        editorComponent.setContentAreaFilled(false);

        editorComponent.setFocusable(false);
    }

    public Object getCellEditorValue() {
        return currentText;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    PopupDialog popup = new PopupDialog();
                    Point p = editorComponent.getLocationOnScreen();
                    popup.setLocation(p.x, p.y + editorComponent.getSize().height);

                    Dimension dm = new Dimension(230, 290);
                    popup.setSize(dm);
                    popup.setResizable(false);
                    popup.setVisible(true);
                    fireEditingStopped();
                } catch (IOException ex) {
                    Logger.getLogger(TablePopupEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(TablePopupEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        currentText = value.toString();
        editorComponent.setText(currentText);
        return editorComponent;
    }

    class PopupDialog extends JDialog {

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JPanel jPanel1 = new javax.swing.JPanel();
        JPanel jPanel2 = new javax.swing.JPanel();
        JPanel jPanel3 = new javax.swing.JPanel();
        JLabel jLabel1 = new javax.swing.JLabel();
        JButton jButton1 = new javax.swing.JButton();

        JScrollPane jScrollPane2 = new javax.swing.JScrollPane();

        public PopupDialog() throws IOException, SQLException {
            super((Frame) null, "", true);

            setTitle("Agregar Cotizacion");

            jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Cotizacion");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            //LLENAR TABLA
            int row = View.jTable1.getSelectedRow();
            String licitacion = View.jTable1.getValueAt(row, 0).toString();
            Procedures.llenarTablaCotizacion(licitacion, jTable1);

            jScrollPane1.setViewportView(jTable1);

            jButton1.setText("Agregar");
            jButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        jButton1ActionPerformed(e);
                    } catch (IOException ex) {
                        Logger.getLogger(TablePopupCotizacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            jTextField1 = new javax.swing.JTextField(6);
            jTextField1.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (jTextField1.getText().trim().length() == 6 || ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                        e.consume();
                    }
                }
            });

            jScrollPane1.setViewportView(jTable1);

            jButton1.setText("Agregar");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jButton1)))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel3Layout.setVerticalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton1)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
        }

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        String cotizacion = jTextField1.getText();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{cotizacion, true});
        Licitacion lici = null;
        Integer pos = Integer.valueOf(View.jTable1.getValueAt(View.jTable1.getSelectedRow(), 6).toString());
        lici = arrLicitaciones.get(pos);

        String fechaCierre = lici.getFechaCierre();
        int indexOf = lici.getFechaCierre().indexOf(" ");
        fechaCierre = fechaCierre.substring(0, indexOf).trim();
        lici.setFechaCierre(fechaCierre);

        if (CRUD_Licitaciones.estaRegistrada(lici)) {
            if (CRUD_Licitaciones.registrarCoti(lici, cotizacion)) {
                View.jTable1.setValueAt(1, View.jTable1.getSelectedRow(), View.jTable1.getColumn("Ofertas").getModelIndex());
                JOptionPane.showMessageDialog(null, "Se registro correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error : No se puedo registrar esta licitacion",
                        "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (CRUD_Licitaciones.registrarLicitacion(lici, 0, 0)) {
                if (CRUD_Cotizaciones.registrarCotizacion(cotizacion, lici.getNumAdqui(), 0)) {
                    View.jTable1.setValueAt(1, View.jTable1.getSelectedRow(), View.jTable1.getColumn("Ofertas").getModelIndex());
                    JOptionPane.showMessageDialog(null, "Se registro correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Error : No se puedo registrar esta licitacion",
                            "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error : No se puedo registrar esta cotizacion",
                        "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        }
        jTable1.revalidate();
        jTable1.repaint();
        jTextField1.setText("");

    }
}

class RendererGuardar implements TableCellRenderer {

    JPanel panel;
    JButton b;

    public RendererGuardar() {
        panel = new javax.swing.JPanel();
        b = new javax.swing.JButton();

        b.setText("Guardar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

////////////////////////////////////////////////////////////////////////////////
class EditorGuardar extends AbstractCellEditor implements TableCellEditor, ActionListener {

    JPanel panel;
    JButton b;
    JTable jTable;

    @Override
    public Object getCellEditorValue() {
        return panel;
    }

    public EditorGuardar(JTable jTable) {
        this.jTable = jTable;
        panel = new javax.swing.JPanel();
        b = new javax.swing.JButton();

        b.setText("Guardar");
        b.addActionListener(this);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {

            int seleccion = jTable.getColumn("Aceptado").getModelIndex();
            int row = jTable.getSelectedRow();
            int estado = 0;

            if (jTable.getValueAt(row, seleccion).toString().equals("true")) {
                estado = 1;
            } else {
                estado = 0;
            }
            CRUD_Licitaciones.updateLicitacion(jTable.getValueAt(row, 0).toString(), estado);
            JOptionPane.showMessageDialog(null, "Guardado exitosamente", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);

        }
    }
}

class StringComparator2 implements Comparator {

    public int compare(Object o1, Object o2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        String fechaString = o1.toString();
        String fechaString2 = o2.toString();
        try {
            date1 = sdf.parse(fechaString);
            date2 = sdf.parse(fechaString2);

        } catch (ParseException ex) {

            Logger.getLogger(StringComparator2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date1.compareTo(date2);
    }

    public boolean equals(Object o2) {
        return this.equals(o2);
    }
}
// --------------------------------------------------------------------------------------------------------------------------------------

class RendererCargar implements TableCellRenderer {

    JPanel panel;
    JButton b;

    public RendererCargar() {
        panel = new javax.swing.JPanel();
        b = new javax.swing.JButton();

        b.setText("Cargar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        boolean aceptado = table.getValueAt(row, table.getColumn("Aceptado").getModelIndex()).toString().equals("true");
        String numOc = table.getValueAt(row, table.getColumn("NumOC").getModelIndex()).toString();
        if (aceptado) {
            if (!numOc.equals("")) {
                b.setEnabled(false);
            } else {
                b.setEnabled(true);
            }
        } else {
            b.setEnabled(false);
        }

        return panel;
    }
}

////////////////////////////////////////////////////////////////////////////////
class EditorCargar extends AbstractCellEditor implements TableCellEditor, ActionListener {

    JPanel panel;
    JButton b;
    JTable jTable;

    @Override
    public Object getCellEditorValue() {
        return panel;
    }

    public EditorCargar(JTable jTable) {
        this.jTable = jTable;
        panel = new javax.swing.JPanel();
        b = new javax.swing.JButton();

        b.setText("Cargar");
        b.addActionListener(this);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(b, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        boolean aceptado = table.getValueAt(row, table.getColumn("Aceptado").getModelIndex()).toString().equals("true");
        String numOc = table.getValueAt(row, table.getColumn("NumOC").getModelIndex()).toString();

        if (aceptado) {
            if (!numOc.equals("")) {
                b.setEnabled(false);
            } else {
                b.setEnabled(true);
            }
        } else {
            b.setEnabled(false);
        }
        return panel;
    }

    public void actionPerformed(ActionEvent e) {

        int row = jTable.getSelectedRow();

        String licitacion = jTable.getValueAt(row, 0).toString();
        if (CRUD_Cotizaciones.validateCotizacion(licitacion)) {
            if (e.getSource() instanceof JButton) {
                DropOc drop = new DropOc(View.frame, true, jTable);
                drop.setResizable(false);
                drop.setLocationRelativeTo(View.frame);
                drop.setVisible(true);
                fireEditingStopped();
            }
        } else {
            JOptionPane.showMessageDialog(View.frame, "Error : No se puedo registrar esta licitacion sin una COTIZACION PREVIA\n\n Favor AGREGAR COTIZACION", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);

        }

    }
}
