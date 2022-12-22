package Procedure;

import Components.Button_Direccion_E;
import Components.Button_Direccion_R;
import Components.Button_Fecha_E;
import Components.Button_Fecha_R;
import Connect.DbConnection;
import DAO.CRUD_Cotizaciones;
import DAO.CRUD_Licitaciones;
import DAO.CRUD_PalabrasClaves;
import Objects.Licitacion;
import static Procedure.Functions.cruzarConBD;
import static Procedure.Functions.cruzarDescripciones;
import static Procedure.Functions.fecha;
import static Procedure.Functions.fechaMercadoPublico;
import View.View;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.awt.Color;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;
import org.openqa.selenium.Keys;
import javax.swing.table.TableRowSorter;

public class Procedures {

    public static String direccion = System.getProperty("user.dir");
    private static final int BUFFER_SIZE = 4096;
    public static DbConnection conex;

    public static void xlsToTXT() {

        File file = new File(direccion + "\\Licitaciones");
        String[] list = file.list();

        for (String name : list) {
            if (name.contains(".xls")) {
                File file3 = new File(file.getPath() + "\\" + name);
                File file2 = new File(file.getPath() + "\\" + name.replace(".xls", ".txt"));
                file3.renameTo(file2);
            }
        }
    }

    public static void descomprimirZIP() {

        ZipInputStream zipIn = null;
        try {
            String zipFilePath = direccion + "\\Licitaciones\\Licitacion_Publicada.zip";
            String destDirectory = direccion + "\\Licitaciones";
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zipIn.close();
            } catch (IOException ex) {
                Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void procedimiento(JTable jTable1, JTable jTable2) {
        try {
            File carpeta = new File(Procedures.direccion + "\\Licitaciones");
            String[] lstFolder = carpeta.list();

            int flagAgil = 0;
            int flagLici = 0;

            ArrayList<Licitacion> licitacionesBD = CRUD_Licitaciones.getLicitaciones();
            ArrayList<Licitacion> arrAgil = new ArrayList<>();
            ArrayList<Licitacion> arrAux = new ArrayList<>();
            ArrayList<Licitacion> arrAux2 = new ArrayList<>();

            for (String list1 : lstFolder) {

                if (list1.contains("BusquedaCAgil")) {
                    setArrAux(Functions.leerLicitacionesAgil(new File(carpeta.getPath() + "\\" + list1)), arrAux);
                    if (flagAgil > 0) {
                        arrAgil = cruzarConBD(arrAux, licitacionesBD, 1);
                        Procedures.llenarTabla(jTable2, arrAgil);
                    }
                    flagAgil++;
                }
                if (list1.contains("ListadoResultadoLicitacion")) {
                    setArrAux(Functions.addBorder(new File(carpeta.getPath() + "\\" + list1)), arrAux2);
                    if (flagLici > 0) {
//                        ArrayList<Licitacion> arr = cruzarDescripciones(arrAux2);
//                        arr = cruzarConBD(arr, licitacionesBD, 0);
                        Procedures.llenarTablaLicitaciones(jTable1, arrAux2);
                    }
                    flagLici++;
                }
            }

            filtro(jTable1);
            filtro(jTable2);
        } catch (Exception ex) {
            System.out.println("ex " + ex);
        }

    }

    public static void mostrarArreglo(ArrayList<Licitacion> arr) {
        System.out.println("");
        System.out.println("-------------------------------------------");

        System.out.println("size " + arr.size());
        arr.forEach((Licitacion lici) -> {
            System.out.println("" + lici.getNumAdqui());
        });
        System.out.println("-------------------------------------------");

    }

    private static void setArrAux(ArrayList<Licitacion> arrGuardar, ArrayList<Licitacion> arrAux) {

        arrGuardar.forEach((Licitacion lici) -> {
            arrAux.add(lici);
        });
    }

    public static void llenarTabla(JTable table, ArrayList<Licitacion> arrLicitaciones) {
        DecimalFormat df = new DecimalFormat("$###,###");
        DefaultTableModel modelo = Functions.modeloLicitacionesAgil();
        int columnas = modelo.getColumnCount();
        Object[] fila = new Object[columnas];

        for (int i = 0; i < arrLicitaciones.size(); i++) {

            Licitacion lici = arrLicitaciones.get(i);

            fila[0] = lici.getNumAdqui();
            fila[1] = lici.getDescrip();
            fila[2] = lici.getNombreAdqui();
            fila[3] = lici.getOrganismo();

            fila[4] = dateToString(stringToDateFechaTermino(lici.getFechaCierre()));

            fila[5] = lici.getOfertas();
            fila[6] = df.format(lici.getValor());

            if (lici.getFechaOC() != null) {
                fila[7] = lici.getFechaOC();
            } else {
                fila[7] = "";
            }
            if (lici.getFechaTermino() != null) {
                fila[8] = lici.getFechaTermino();
            } else {
                fila[8] = "";
            }
            if (lici.getDireccion() != null) {
                fila[9] = lici.getDireccion();
            } else {
                fila[9] = "";
            }
            if (lici.getNumOC() != null) {
                fila[10] = lici.getNumOC();
            } else {
                fila[10] = "";
            }
            fila[11] = "";
            if (lici.getEstado() != 0) {
                fila[12] = lici.getEstado() == 1;
            } else {
                fila[12] = false;
            }
            modelo.addRow(fila);
        }

        table.setModel(modelo);

        renderLicitacion(table, 1);

        table.getColumnModel().getColumn(table.getColumn("ID").getModelIndex()).setPreferredWidth(5);
        table.getColumnModel().getColumn(table.getColumn("Fecha Cierre").getModelIndex()).setPreferredWidth(5);
        table.getColumnModel().getColumn(table.getColumn("Aceptado").getModelIndex()).setPreferredWidth(3);

        table.getColumn("Cargar").setCellEditor(new EditorCargar(table));
        table.getColumn("Cargar").setCellRenderer(new RendererCargar());
//
        table.getColumn("Direccion").setCellRenderer(new Button_Direccion_R(1));
        table.getColumn("Direccion").setCellEditor(new Button_Direccion_E(table, 1));

        table.getColumn("Fecha Termino").setCellRenderer(new Button_Fecha_R(1));
        table.getColumn("Fecha Termino").setCellEditor(new Button_Fecha_E(table, 1));

        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);

        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        TableRowSorter trs = new TableRowSorter(modelo);
        trs.setComparator(4, new StringComparator2());
        table.setRowSorter(trs);

    }

    public static void llenarTablaLicitaciones(JTable table, ArrayList<Licitacion> arrLicitaciones) {

        DefaultTableModel modelo = Functions.modeloLicitaciones();
        int columnas = modelo.getColumnCount();
        Object[] fila = new Object[columnas];

        DecimalFormat df = new DecimalFormat("$###,###");

        for (int i = 1; i < arrLicitaciones.size(); i++) {
            Licitacion lici = arrLicitaciones.get(i);

            fila[0] = lici.getNumAdqui();
            fila[1] = lici.getOrganismo();
            fila[2] = lici.getNombreAdqui();
            fila[3] = lici.getDescrip();

            fila[4] = dateToString(stringToDateFechaTermino(lici.getFechaCierre()));

            fila[5] = lici.getOfertas();
            fila[6] = i;

            fila[7] = df.format(lici.getValor());
            if (lici.getFechaOC() != null) {
                fila[8] = lici.getFechaOC();
            } else {
                fila[8] = "";
            }
            if (lici.getFechaTermino() != null) {
                fila[9] = lici.getFechaTermino();
            } else {
                fila[9] = "";
            }
            if (lici.getDireccion() != null) {
                fila[10] = lici.getDireccion();
            } else {
                fila[10] = "";
            }
            if (lici.getNumOC() != null) {
                fila[11] = lici.getNumOC();
            } else {
                fila[11] = "";
            }
            fila[12] = "";
            if (lici.getEstado() != 0) {
                fila[13] = lici.getEstado() == 1;
            } else {
                fila[13] = false;
            }

            modelo.addRow(fila);
        }

        table.setModel(modelo);

        renderLicitacion(table, 0);
        table.getColumnModel().getColumn(table.getColumn("Numero").getModelIndex()).setPreferredWidth(5);
        table.getColumnModel().getColumn(table.getColumn("Fecha Cierre").getModelIndex()).setPreferredWidth(3);
        table.getColumnModel().getColumn(table.getColumn("Ofertas").getModelIndex()).setPreferredWidth(3);
        table.getColumnModel().getColumn(table.getColumn("Aceptado").getModelIndex()).setPreferredWidth(3);

        table.getColumn("Descripcion Producto").setCellEditor(new TablePopupEditor(arrLicitaciones));
        table.getColumn("Ofertas").setCellEditor(new TablePopupCotizacion(arrLicitaciones));

        table.getColumn("Cargar").setCellEditor(new EditorCargar(table));
        table.getColumn("Cargar").setCellRenderer(new RendererCargar());

        table.getColumn("Fecha Termino").setCellRenderer(new Button_Fecha_R(0));
        table.getColumn("Fecha Termino").setCellEditor(new Button_Fecha_E(table, 0));

        table.getColumn("Direccion").setCellRenderer(new Button_Direccion_R(0));
        table.getColumn("Direccion").setCellEditor(new Button_Direccion_E(table, 0));

        table.getColumnModel().getColumn(table.getColumn("pos").getModelIndex()).setMaxWidth(0);
        table.getColumnModel().getColumn(table.getColumn("pos").getModelIndex()).setMinWidth(0);
        table.getColumnModel().getColumn(table.getColumn("pos").getModelIndex()).setPreferredWidth(0);

        table.getColumnModel().getColumn(table.getColumn("Nombre").getModelIndex()).setMaxWidth(0);
        table.getColumnModel().getColumn(table.getColumn("Nombre").getModelIndex()).setMinWidth(0);
        table.getColumnModel().getColumn(table.getColumn("Nombre").getModelIndex()).setPreferredWidth(0);

        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        TableRowSorter trs = new TableRowSorter(modelo);
        trs.setComparator(4, new StringComparator2());
        table.setRowSorter(trs);

    }

    public static Date stringToDateFechaTermino(String fecha) {
        try {
            SimpleDateFormat formatter;
            Date startDate;
            try {
                formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                startDate = formatter.parse(fecha);
            } catch (Exception ex) {
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                startDate = formatter.parse(fecha);
            }

            return startDate;
        } catch (ParseException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }

    public static void llenarTablaCotizacion(String licitacion, JTable table) {

        DefaultTableModel model = Functions.modeloCoti();
        ArrayList<String> cotizacion = CRUD_Cotizaciones.getCotizacion(licitacion);
        Object[] fila = new Object[model.getColumnCount()];

        if (!cotizacion.isEmpty()) {
            for (int i = 0; i < cotizacion.size(); i++) {
                String coti = cotizacion.get(i);
                fila[0] = coti;
                model.addRow(fila);
            }
        }

        table.setModel(model);

        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        table.getColumn("Eliminar").setCellEditor(new CustomEditor(table, 0));
        table.getColumn("Eliminar").setCellRenderer(new CustomRenderer());

    }

    public static void llenarTablaPalabrasClaves(JTable table) {

        DefaultTableModel model = Functions.modelPalabrasClaves();
        ArrayList<String> palabrasClaves = CRUD_PalabrasClaves.getPalabrasClaves();
        Object[] fila = new Object[1];

        if (!palabrasClaves.isEmpty()) {
            for (int i = 0; i < palabrasClaves.size(); i++) {
                String coti = palabrasClaves.get(i);
                fila[0] = coti;
                model.addRow(fila);
            }
        }
        table.setModel(model);

        DefaultTableCellRenderer Renderer = new DefaultTableCellRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(Renderer);
        }
        table.getColumn("Eliminar").setCellEditor(new CustomEditor(table, 1));
        table.getColumn("Eliminar").setCellRenderer(new CustomRenderer());

        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
    }

    public static void renderCotizacion(JTable table) {

        DefaultTableCellRenderer Renderer = new DefaultTableCellRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        if (table.getRowCount() != 0) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(Renderer);
            }
        }
        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
    }

    public static void renderLicitacion(JTable table, int tab) {
        DefaultTableCellRenderer Renderer2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                Component cell = super.getTableCellRendererComponent(tabla, value, isSelected, hasFocus, row, col);

                String numOc = tabla.getValueAt(row, table.getColumn("NumOC").getModelIndex()).toString();

                if (numOc.equals("")) {
                    String descrip;
                    if (tab == 0) {
                        String toLowerCase = tabla.getValueAt(row, table.getColumn("Descripcion Producto").getModelIndex()).toString().toLowerCase();
                        if (toLowerCase != null) {
                            descrip = toLowerCase;
                        } else {
                            descrip = "";
                        }
                    } else {
                        descrip = tabla.getValueAt(row, table.getColumn("Descripcion").getModelIndex()).toString().toLowerCase();
                    }

                    ArrayList<String> palabrasClaves = View.arrPalabrasClaves;

                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);

                    palabrasClaves.forEach((String palabra) -> {

                        if (descrip.contains(palabra.toLowerCase())) {
                            cell.setBackground(new Color(155, 247, 155));
                            cell.setForeground(Color.BLACK);
                        }
                    });
                } else {

                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);
                    cell.setBackground(new Color(255, 102, 102)); //[255,102,102]
                    cell.setForeground(Color.BLACK);
                }
                return cell;
            }
        };
        Renderer2.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(Renderer2);
        }

        TableColumn column = table.getColumnModel().getColumn(table.getColumn("Aceptado").getModelIndex());
        column.setCellEditor(table.getDefaultEditor(Boolean.class));
        column.setCellRenderer(table.getDefaultRenderer(Boolean.class));

        if (tab != 0) {
            table.getColumn("Ofertas").setCellEditor(new TablePopupCotizacionAgil());
        }

    }

    public static void filtro(JTable table) {
        try {
            TableFilterHeader tableFilterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        } catch (Exception e) {
            System.out.println("Error : Exploto el filtro");
        }

    }

    public static void crearCarpeta(String carpeta) {

        File fileCarpeta = new File(direccion + "\\" + carpeta);
        if (!fileCarpeta.exists()) {
            new File(direccion + "\\" + carpeta).mkdir();
        }

    }

    public static void eliminarArchivos() {

        File carpeta = new File(direccion + "\\Licitaciones");
        String[] list = carpeta.list();
        for (int i = 0; i < list.length; i++) {
            File archivo = new File(carpeta.getPath() + "\\" + list[i]);
            if (archivo.exists()) {
                archivo.delete();
            }
        }
    }

    public static void esperar(int time) {
        try {

            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void descargar() {

        String rut = "82554947"; //82554947
        String psw = "Ftoso3402";
        String save = Procedures.direccion + "\\Licitaciones";

        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("plugins.always_open_pdf_externally", false);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", save);
        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--savebrowsing-disable-download-protection");
        options.addArguments("--host-resolver-rules=MAP www.google-analytics.com 127.0.0.1");

        options.setExperimentalOption("prefs", chromePrefs);
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.get("https://www.mercadopublico.cl/Home");

        try {
            System.out.println("BOTON INICIO");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div[2]/div[1]/div/input")));
            driver.findElement(By.xpath("/html/body/header/nav/div[2]/div[1]/div/input")).click();
        } catch (Exception ex) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div[2]/div[1]/div/button")));
            driver.findElement(By.xpath("/html/body/header/nav/div[2]/div[1]/div/button")).click();
        }

        //CLAVE UNICA
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("liRUT")));
        driver.findElement(By.id("liRUT")).click();

        //RUT
        try {
            System.out.println("RUT");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-rp")));
            driver.findElement(By.id("username-rp")).sendKeys(rut);
        } catch (Exception ex) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtRUT")));
            driver.findElement(By.id("txtRUT")).sendKeys("87255494");
        }

        //CLAVE  
        try {
            System.out.println("CLAVE");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-rp")));
            driver.findElement(By.id("password-rp")).sendKeys(psw);
        } catch (Exception ex) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtPass")));
            driver.findElement(By.id("txtPass")).sendKeys(psw);
        }

        //BOTON INGRESAR 
        try {
            esperar(2500);
            System.out.println("CLICK INGRESAR");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("kc-login-rp")));
            driver.findElement(By.id("kc-login-rp")).submit();
        } catch (Exception ex) {
            System.out.println("CLICK 2");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnAccesoRUT")));
            driver.findElement(By.id("btnAccesoRUT")).click();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div[2]/div[1]/div/div[2]/div/div/div/div[2]/div/table/tbody/tr/td/div[2]/label/span/input")));
        driver.findElement(By.xpath("/html/body/header/nav/div[2]/div[1]/div/div[2]/div/div/div/div[2]/div/table/tbody/tr/td/div[2]/label/span/input")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div[2]/div[1]/div/div[2]/div/div/div/div[3]/a")));
        driver.findElement(By.xpath("/html/body/header/nav/div[2]/div[1]/div/div[2]/div/div/div/div[3]/a")).click();

        wait = new WebDriverWait(driver, 20);

        descargarAgiles(save, driver, wait);
        descargarAgiles2(save, driver, wait);
        descargarLicitaciones(save, driver, wait);
        descargarLicitaciones2(save, driver, wait);

        driver.close();
        driver.quit();

    }

    public static void descargarLicitaciones(String save, WebDriver driver, WebDriverWait wait) {

        driver.get("https://www.mercadopublico.cl/BID/Modules/RFB/NEwSearchProcurement.aspx");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        }

        //REGION
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cboRegion")));
        Select select = new Select(driver.findElement(By.id("cboRegion")));
        select.selectByIndex(6);

        //BOTON INGRESAR
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("calFrom")));
        driver.findElement(By.id("calFrom")).clear();
        driver.findElement(By.id("calFrom")).sendKeys(fechaMercadoPublico(1));

        //BOTON INGRESAR
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSearch")));
        driver.findElement(By.id("btnSearch")).click();

        int cont = 0;
        while (cont != 15) {
            try {
                Thread.sleep(1000);
                cont++;
            } catch (InterruptedException ex) {
                Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //BOTON DESCARGAR lnkDowloadExcel
        executeScript(driver, "javascript:__doPostBack('lnkDowloadExcel','')");

        File fileAgil = new File(save + "\\ListadoResultadoLicitacion_" + fecha() + ".xls");

        while (fileAgil.exists() == false) {
            System.out.println("Esperando archivo Agil");
            esperar(1000);
        }
        System.out.println("DESCARGO EL archivo");
    }

    public static void descargarLicitaciones2(String save, WebDriver driver, WebDriverWait wait) {

        driver.get("https://www.mercadopublico.cl/BID/Modules/RFB/NEwSearchProcurement.aspx");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        }

        //REGION
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cboRegion")));
        Select select = new Select(driver.findElement(By.id("cboRegion")));
        select.selectByIndex(6);

        select = new Select(driver.findElement(By.id("cboState")));
        select.selectByIndex(3);

        //BOTON INGRESAR
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("calFrom")));
        driver.findElement(By.id("calFrom")).clear();
        driver.findElement(By.id("calFrom")).sendKeys(fechaMercadoPublico(1));

        //BOTON INGRESAR
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSearch")));
        driver.findElement(By.id("btnSearch")).click();

        //ESPERAR QUE CARGUE LA TABLA
        int cont = 0;
        while (cont != 15) {
            try {
                Thread.sleep(1000);
                cont++;
            } catch (InterruptedException ex) {
                Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        esperar(1000);

        //BOTON DESCARGAR lnkDowloadExcel
        executeScript(driver, "javascript:__doPostBack('lnkDowloadExcel','')");

        //BOTON TODAS LAS LICITACIONES
        executeScript(driver, "javascript:__doPostBack('lbExcel','')");

        File zip = new File(save + "\\Licitacion_Publicada.zip");

        while (zip.exists() == false) {
            try {
                System.out.println("Esperando que se descargue el zip");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void descargarAgiles(String save, WebDriver driver, WebDriverWait wait) {

        esperar(6000);
        try {
            //BOTON COMPRA AGIL
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mnuPrincipaln6\"]/table/tbody/tr/td/a")));
            driver.findElement(By.xpath("//*[@id=\"mnuPrincipaln6\"]/table/tbody/tr/td/a")).click();
        } catch (Exception ex) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mnuPrincipaln6\"]/table/tbody/tr/td")));
            driver.findElement(By.xpath("//*[@id=\"mnuPrincipaln6\"]/table/tbody/tr/td")).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.name("fraDetalle")));
        esperar(6000);
        driver.switchTo().frame("fraDetalle");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalStepper")));
        driver.findElement(By.id("modalStepper")).click();

        //REGION fraDetalle
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ddlRegion")));
        Select select = new Select(driver.findElement(By.id("ddlRegion")));
        select.selectByIndex(6);

        //MI RUBRO
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lblverMirubro")));
        driver.findElement(By.id("lblverMirubro")).click();

        //FECHA DESDE  
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DateFrom")));
        driver.findElement(By.id("DateFrom")).clear();
        driver.findElement(By.id("DateFrom")).sendKeys(fechaMercadoPublico(0));
        driver.findElement(By.id("DateFrom")).sendKeys(Keys.TAB);

        //FECHA HASTA
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DateTo")));
        driver.findElement(By.id("DateTo")).sendKeys(fechaMercadoPublico(2));

        esperar(3000);

        //BUSCAR 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSearchParameter")));
        driver.findElement(By.id("btnSearchParameter")).sendKeys(Keys.ENTER);

        esperar(3000);
        executeScript(driver, "javascript:__doPostBack('lnkDownloadExcel','')");
        File fileAgil = new File(save + "\\BusquedaCAgil_" + fecha() + ".xls");

        while (fileAgil.exists() == false) {
            System.out.println("Esperando archivo Agil");
            esperar(1000);
        }
    }

    private static void executeScript(WebDriver driver, String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
    }

    public static void descargarAgiles2(String save, WebDriver driver, WebDriverWait wait) {

        driver.navigate().refresh();

        System.out.println("pagina refrescada");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mnuPrincipaln6\"]/table/tbody/tr/td/a")));
        driver.findElement(By.xpath("//*[@id=\"mnuPrincipaln6\"]/table/tbody/tr/td/a")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.name("fraDetalle")));
        esperar(6000);
        driver.switchTo().frame("fraDetalle");

//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalStepper")));
//        driver.findElement(By.id("modalStepper")).click();
        //REGION fraDetalle
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ddlRegion")));
        Select select = new Select(driver.findElement(By.id("ddlRegion")));
        select.selectByIndex(6);

        System.out.println("region select");

        //ESTADO 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ddlState")));
        select = new Select(driver.findElement(By.id("ddlState")));
        select.selectByIndex(3);

        System.out.println("estado select");

        //MI RUBRO
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lblverMirubro")));
        driver.findElement(By.id("lblverMirubro")).click();

        System.out.println("click rubro");

        //FECHA DESDE  
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DateFrom")));
        driver.findElement(By.id("DateFrom")).clear();
        driver.findElement(By.id("DateFrom")).sendKeys(fechaMercadoPublico(0));
        driver.findElement(By.id("DateFrom")).sendKeys(Keys.TAB);

        //FECHA HASTA
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DateTo")));
        driver.findElement(By.id("DateTo")).sendKeys(fechaMercadoPublico(2));

        System.out.println("fechas listas");

        esperar(3000);

        //BUSCAR 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSearchParameter")));
        driver.findElement(By.id("btnSearchParameter")).sendKeys(Keys.ENTER);

        System.out.println("Buscar");

        executeScript(driver, "javascript:__doPostBack('lnkDownloadExcel','')");

        System.out.println("ejecutado el js");

        File fileAgil = new File(save + "\\BusquedaCAgil_" + fecha() + " (1).xls");

        while (fileAgil.exists() == false) {
            System.out.println("Esperando archivo Agil2");
            esperar(1000);
        }
    }

    public static void nuevaInstanciaBD() {

        try {
            conex = new DbConnection();
            Thread thread = new Thread(() -> {
                while (true) {

                    try {
                        if (conex.getConnection().isClosed()) {
                            conex = new DbConnection();
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("Desconectando...");
                        conex.desconectar();
                    } catch (SQLException ex) {
                        Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void pintarCelda(String fechaOc, JPanel panel, int row, JTable table, int tab) {
        if (!fechaOc.equals("")) {
            panel.setBackground(new Color(255, 102, 102));
        } else {
            String descrip;
            if (tab == 0) {
                String toLowerCase = table.getValueAt(row, table.getColumn("Descripcion Producto").getModelIndex()).toString().toLowerCase();
                if (toLowerCase != null) {
                    descrip = toLowerCase;
                } else {
                    descrip = "";
                }
            } else {
                descrip = table.getValueAt(row, table.getColumn("Descripcion").getModelIndex()).toString().toLowerCase();
            }

            ArrayList<String> palabrasClaves = View.arrPalabrasClaves;

            panel.setBackground(Color.WHITE);
            panel.setForeground(Color.BLACK);

            palabrasClaves.forEach((String palabra) -> {

                if (descrip.contains(palabra.toLowerCase())) {
                    panel.setBackground(new Color(155, 247, 155));
                    panel.setForeground(Color.BLACK);
                }
            });
        }
        panel.setForeground(Color.BLACK);
    }

}
