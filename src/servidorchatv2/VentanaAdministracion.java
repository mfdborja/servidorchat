package servidorchatv2;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class VentanaAdministracion extends javax.swing.JFrame {

    //Modelos para escribir en una JTable
    DefaultTableModel erroresModel = new DefaultTableModel();
    DefaultTableModel mensajesModel = new DefaultTableModel();

    public VentanaAdministracion() {
        initComponents();

        //Modelos para las tablas
        erroresModel.addColumn("Errores");

        mensajesModel.addColumn("Usuario");
        mensajesModel.addColumn("Mensaje");

        //Asociamos los modelos a la tabla de la ventana
        jTablaErrores.setModel(erroresModel);
        jTablaConversacion.setModel(mensajesModel);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTablaErrores = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablaConversacion = new javax.swing.JTable();
        Borjaro2000 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor Chat V2");
        setResizable(false);

        jScrollPane1.setEnabled(false);

        jTablaErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTablaErrores);

        jScrollPane2.setEnabled(false);

        jTablaConversacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTablaConversacion);

        Borjaro2000.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        Borjaro2000.setForeground(new java.awt.Color(0, 0, 204));
        Borjaro2000.setText("www.borjaro2000.tk");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Borjaro2000)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Borjaro2000)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Borjaro2000;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablaConversacion;
    private javax.swing.JTable jTablaErrores;
    // End of variables declaration//GEN-END:variables

    //Escribe un error en la ventana
    public void incluirError(String error) {
        String[] temp = {""}; //Una columna en el array por columna en la Jtable
        temp[0] = error;
        erroresModel.addRow(temp); //Añadimos en nuevo mensaje al final
        if (erroresModel.getRowCount() > 100) { //Quitamos los más antiguos
            erroresModel.removeRow(0);
        }

        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Dimension dimta = jTablaErrores.getSize();
                        Point p = new Point(0, dimta.height);
                        jScrollPane1.getViewport().setViewPosition(p);
                    }
                });
    }

    //Escribe un mensaje de usuario en la ventana
    public void incluirMensaje(String mensaje) {
        String[] temp = {"", ""};
        temp[0] = mensaje.substring(0, mensaje.indexOf("|@|"));
        temp[1] = mensaje.substring(mensaje.indexOf("|@|") + 3);

        mensajesModel.addRow(temp);
        if (mensajesModel.getRowCount() > 100) {
            mensajesModel.removeRow(0);
        }

        //Avanza el scroll de la ventana hasta el último mensaje
        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Dimension dimta = jTablaConversacion.getSize();
                        Point p = new Point(0, dimta.height);
                        jScrollPane2.getViewport().setViewPosition(p);
                    }
                });
    }
}
