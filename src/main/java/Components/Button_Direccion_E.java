/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Procedure.Procedures;
import View.Direccion_Dialog;
import View.View;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Jason
 */
public class Button_Direccion_E extends AbstractCellEditor implements ActionListener, TableCellEditor {

    JLabel jLabel2;
    JButton jButton2;
    JPanel jPanel5;
    JTable table;
    int tab;

    public Button_Direccion_E(JTable table, int tab) {
        this.table = table;
        this.tab = tab;

        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        jButton2.setText("Cambiar");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, 0))
        );
        jButton2.addActionListener(this);
    }

    @Override
    public Object getCellEditorValue() {
        return jLabel2.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        stopCellEditing();
        Direccion_Dialog fd = new Direccion_Dialog(View.frame, true, table);
        fd.setLocationRelativeTo(View.frame);
        fd.setVisible(true);
        jLabel2.setText(table.getValueAt(table.getSelectedRow(), table.getColumn("Direccion").getModelIndex()).toString());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String fechaOC = table.getValueAt(row, table.getColumn("Fecha OC").getModelIndex()).toString();

        table.getCellRenderer(row, column);

        Procedures.pintarCelda(fechaOC, jPanel5, row, table, tab);

        if (fechaOC.equals("")) {
            this.jButton2.setVisible(false);
            this.jLabel2.setVisible(true);
            jLabel2.setText("");
        } else {
            if (value.toString().equals("")) {
                this.jLabel2.setVisible(false);
                this.jButton2.setVisible(true);
                jLabel2.setText("");
            } else {
                this.jButton2.setVisible(false);
                this.jLabel2.setVisible(true);
                jLabel2.setText(value.toString());
            }
        }
        return jPanel5;
    }

}
