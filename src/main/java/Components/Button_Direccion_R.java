package Components;

import Procedure.Procedures;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Jason
 */
public class Button_Direccion_R extends JPanel implements TableCellRenderer {

    JLabel jLabel2;
    JButton jButton2;
    int tab;

    public Button_Direccion_R(int tab) {
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        this.tab = tab;

        jButton2.setText("Direccion");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel1Layout);
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
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String fechaOC = table.getValueAt(row, table.getColumn("Fecha OC").getModelIndex()).toString();

        Procedures.pintarCelda(fechaOC, this, row, table, tab);

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

        return this;
    }

}
