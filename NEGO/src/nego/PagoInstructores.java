
package nego;

import java.sql.*;
import BaseDatos.ConexionPostgresql;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PagoInstructores extends javax.swing.JFrame {
    
    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();

    public PagoInstructores() {
        initComponents();
        CargarTablaPagoInstructores("","","","");
        inhabilitarRecaudos();
        
        //Calcula los gastos en instructores.
        String total = TotalRecaudado("","","","");
        txtTotalPagos.setText("$ "+total+"=");
    }
    
    void LimpiarPagos() {
        
        //Dejar vacios todos los campos de captura de datos
        txtFechaInicioPagos.setText("");
        txtFechaFinPagos.setText("");
        txtCodigoServicioPagos.setText("");
        txtCedulaInstructorPagos.setText("");
        txtNombreInstructorPagos.setText("");
        
    }
    void inhabilitarRecaudos() {
        
         //Inhabilitar todos los campos.
        txtNombreInstructorPagos.setEnabled(false);
        txtTotalPagos.setEnabled(false);
    }
    
    
    void CargarTablaPagoInstructores(String fini, String ffin, String codservicio, String cedinstructor)
    {
        //Titulos para el Table Model
        //String[] titulos = {"Cedula", "P. Nombre", "Otros Nombres","P. Apellido", "S. Apellido",  "ID Clase", "Fecha Clase", "Servicio", "Valor Clase"};
        String[] titulos = {"CC Instructor", "Nombre Instructor",  "ID Clase", "Fecha Clase", "Cod. Servicio", "Servicio", "Valor Clase"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[7];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
          
        //SQL : Leer datos de la tabla Instructores, cuando "valor" sea igual o este en la contenación de los datos del instructor
        
        if(!fini.equals("") && !ffin.equals("")){
            sSQL = "SELECT * FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND clases.fecha >='"+fini+"' AND  clases.fecha <='"+ffin+"'" 
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio "
               + " ORDER BY fecha";
        }
        else  if(!ffin.equals("")){
            sSQL = "SELECT * FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%'  AND  clases.fecha <='"+ffin+"'" 
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio "
               + " ORDER BY fecha";
        }
        else if(!fini.equals("")){
            sSQL = "SELECT * FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND clases.fecha >='"+fini+"' " 
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio "
               + " ORDER BY fecha";
        }
        else{
            sSQL = "SELECT * FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%'"
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio "
               + " ORDER BY fecha";
        }
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("cedula");
                String nombreCompleto=rs.getString("nombre1").trim()+' '+rs.getString("apellido1").trim();
                registro[1] = nombreCompleto;
                registro[2] = rs.getString("idclase");
                registro[3] = rs.getString("fecha");
                registro[4] = rs.getString("codservicio");
                registro[5] = rs.getString("descripcion");
                registro[6] = rs.getString("valorhorainst");
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el moledo en la Tabla de Consulta
            tblConsultaPagos.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }       
    }
    
     String TotalRecaudado(String fini, String ffin, String codservicio, String cedinstructor)
    {
        //Arreglo para introducir los datos leidos de la BD
        String sSQL = "";
        String total="";
          
        //Calcula la suma de los campos inscripciones.valor.
        //SQL : Leer datos de la tabla Instructores, cuando "valor" sea igual o este en la contenación de los datos del instructor
        if(!fini.equals("") && !ffin.equals("")){
            sSQL = "SELECT SUM(valorhorainst) FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND clases.fecha >='"+fini+"' AND  clases.fecha <='"+ffin+"' "
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio ";
        }
        else  if(!ffin.equals("")){
            sSQL = "SELECT SUM(valorhorainst) FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%'  AND  clases.fecha <='"+ffin+"' "
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio ";
        }
        else if(!fini.equals("")){
            sSQL = "SELECT SUM(valorhorainst) FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND clases.fecha >='"+fini+"' "
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio ";
        }
        else{
            sSQL = "SELECT SUM(valorhorainst) FROM instructores,clases,servicios "
               + " WHERE cedula LIKE '%"+cedinstructor+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND cedula=cedinstructor AND clases.codservicio=servicios.codservicio ";
        }
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                total = rs.getString("SUM");
            }
            
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        return total;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFechaInicioPagos = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFechaFinPagos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodigoServicioPagos = new javax.swing.JTextField();
        btnServiciosPagos = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtCedulaInstructorPagos = new javax.swing.JTextField();
        btnInstructoresPagos = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtNombreInstructorPagos = new javax.swing.JTextField();
        btnCalcularPagos = new javax.swing.JButton();
        btnSalirPagos = new javax.swing.JButton();
        btnNuevoPagos = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsultaPagos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtTotalPagos = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1290, 762));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Gastos En Instructores"));

        jLabel1.setText("Fecha Inicio:");

        txtFechaInicioPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaInicioPagosActionPerformed(evt);
            }
        });
        txtFechaInicioPagos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFechaInicioPagosFocusLost(evt);
            }
        });

        jLabel2.setText("Fecha Fin:");

        txtFechaFinPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaFinPagosActionPerformed(evt);
            }
        });
        txtFechaFinPagos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFechaFinPagosFocusLost(evt);
            }
        });

        jLabel3.setText("Código Del Servicio:");

        txtCodigoServicioPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoServicioPagosActionPerformed(evt);
            }
        });
        txtCodigoServicioPagos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoServicioPagosFocusLost(evt);
            }
        });

        btnServiciosPagos.setBackground(new java.awt.Color(255, 255, 255));
        btnServiciosPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnServiciosPagos.setText("Ver Servicios     ");
        btnServiciosPagos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnServiciosPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiciosPagosActionPerformed(evt);
            }
        });

        jLabel4.setText("Cédula Del Instructor:");

        txtCedulaInstructorPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaInstructorPagosActionPerformed(evt);
            }
        });
        txtCedulaInstructorPagos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaInstructorPagosFocusLost(evt);
            }
        });

        btnInstructoresPagos.setBackground(new java.awt.Color(255, 255, 255));
        btnInstructoresPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnInstructoresPagos.setText("Ver Instructores");
        btnInstructoresPagos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnInstructoresPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstructoresPagosActionPerformed(evt);
            }
        });

        jLabel5.setText("Nombre Del Instructor:");

        txtNombreInstructorPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreInstructorPagosActionPerformed(evt);
            }
        });

        btnCalcularPagos.setBackground(new java.awt.Color(255, 255, 255));
        btnCalcularPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Calcular.jpg"))); // NOI18N
        btnCalcularPagos.setText("Calcular");
        btnCalcularPagos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCalcularPagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCalcularPagosMouseEntered(evt);
            }
        });
        btnCalcularPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularPagosActionPerformed(evt);
            }
        });

        btnSalirPagos.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirPagos.setText("Salir");
        btnSalirPagos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalirPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirPagosActionPerformed(evt);
            }
        });

        btnNuevoPagos.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoPagos.setText("Nuevo");
        btnNuevoPagos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoPagosActionPerformed(evt);
            }
        });

        tblConsultaPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblConsultaPagos);

        jLabel6.setBackground(new java.awt.Color(0, 102, 102));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 51, 51));
        jLabel6.setText("TOTAL:   ");

        txtTotalPagos.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtTotalPagos.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalPagos.setCaretColor(new java.awt.Color(255, 0, 51));
        txtTotalPagos.setDisabledTextColor(new java.awt.Color(255, 51, 51));

        jLabel7.setText("Formato: aaaa-mm-dd");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtCodigoServicioPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCedulaInstructorPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtFechaInicioPagos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNuevoPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnCalcularPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addComponent(btnSalirPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnServiciosPagos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnInstructoresPagos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtFechaFinPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(48, 48, 48)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNombreInstructorPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(393, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtFechaInicioPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFechaFinPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoServicioPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnServiciosPagos))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCedulaInstructorPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnInstructoresPagos)
                    .addComponent(jLabel5)
                    .addComponent(txtNombreInstructorPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalcularPagos)
                    .addComponent(btnNuevoPagos)
                    .addComponent(btnSalirPagos))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreInstructorPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreInstructorPagosActionPerformed
        
    }//GEN-LAST:event_txtNombreInstructorPagosActionPerformed

    private void txtFechaInicioPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaInicioPagosActionPerformed
        txtFechaInicioPagos.transferFocus();
    }//GEN-LAST:event_txtFechaInicioPagosActionPerformed

    private void txtFechaFinPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaFinPagosActionPerformed
        txtFechaFinPagos.transferFocus();
    }//GEN-LAST:event_txtFechaFinPagosActionPerformed

    private void txtCodigoServicioPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoServicioPagosActionPerformed
        txtCodigoServicioPagos.transferFocus();
    }//GEN-LAST:event_txtCodigoServicioPagosActionPerformed

    private void txtCedulaInstructorPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaInstructorPagosActionPerformed
        txtCedulaInstructorPagos.transferFocus();
    }//GEN-LAST:event_txtCedulaInstructorPagosActionPerformed

    private void btnCalcularPagosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalcularPagosMouseEntered
        btnNuevoPagos.requestFocus();
    }//GEN-LAST:event_btnCalcularPagosMouseEntered

    private void btnSalirPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirPagosActionPerformed
        //Botón Salir : Cierra la aplicación
        this.dispose();
    }//GEN-LAST:event_btnSalirPagosActionPerformed

    private void btnNuevoPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoPagosActionPerformed
        //Boton Nuevo : llama el método habilitar
        LimpiarPagos();
        
    }//GEN-LAST:event_btnNuevoPagosActionPerformed

    private void btnServiciosPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiciosPagosActionPerformed
        new Servicios().setVisible(true);
    }//GEN-LAST:event_btnServiciosPagosActionPerformed

    private void btnInstructoresPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstructoresPagosActionPerformed
        new Instructores().setVisible(true);
    }//GEN-LAST:event_btnInstructoresPagosActionPerformed

    private void btnCalcularPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularPagosActionPerformed
        //Calcula los gastos en instructores.
        String total = TotalRecaudado(txtFechaInicioPagos.getText(),txtFechaFinPagos.getText(),txtCodigoServicioPagos.getText(),txtCedulaInstructorPagos.getText());
        CargarTablaPagoInstructores(txtFechaInicioPagos.getText(),txtFechaFinPagos.getText(),txtCodigoServicioPagos.getText(),txtCedulaInstructorPagos.getText());
        txtTotalPagos.setText("$ "+total+"=");
    }//GEN-LAST:event_btnCalcularPagosActionPerformed

    private void txtFechaInicioPagosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaInicioPagosFocusLost
        //Valisar Fecha Inicio
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String finicio = txtFechaInicioPagos.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(finicio);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaInicioPagos.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioPagos.setText("");                
                txtFechaInicioPagos.requestFocus();
                ver = 1;
            }
        } 
        catch (ParseException ex)
        {
            if(!txtFechaInicioPagos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioPagos.setText("");
                txtFechaInicioPagos.requestFocus();
                ver = 1;
            }
        }
        
        String fin=txtFechaInicioPagos.getText();
	int mayor = fin.compareTo( txtFechaFinPagos.getText() );
        
        if(ver == 0 && mayor > 0 && !txtFechaInicioPagos.getText().equals("") && !txtFechaFinPagos.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Inicio es Posterior a la Fecha Fin.");
            txtFechaInicioPagos.setText("");
        }
    }//GEN-LAST:event_txtFechaInicioPagosFocusLost

    private void txtFechaFinPagosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaFinPagosFocusLost
        // *** Validar formato de fecha
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String ffin = txtFechaFinPagos.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(ffin);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaFinPagos.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtFechaFinPagos.setText("");
                txtFechaFinPagos.requestFocus();                
                ver = 1;
            }
        }
        catch (ParseException ex)
        {
            if(!txtFechaFinPagos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtFechaFinPagos.setText("");
                txtFechaFinPagos.requestFocus();
                ver = 1;
            }
        }
        
        String fin=txtFechaFinPagos.getText();
	int mayor = fin.compareTo( txtFechaInicioPagos.getText() );
        
        if(ver == 0 && mayor < 0 && !txtFechaInicioPagos.getText().equals("") && !txtFechaFinPagos.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Fin es Anterior a la Fecha Inicio.");
            txtFechaFinPagos.setText("");
        }
    
    }//GEN-LAST:event_txtFechaFinPagosFocusLost

    private void txtCodigoServicioPagosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoServicioPagosFocusLost
        //Accion cuando pierde el foco
        String sSQL = "";
        
        //SQL : Validar el servicio
        sSQL = "SELECT * FROM servicios "
               + "WHERE codservicio = '"+txtCodigoServicioPagos.getText()+"'";
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            //Contar el tamaño del ResultSet
            int recordCount =0;
            while(rs.next())
            {
                recordCount++;
            }
            
            //Validar si existe el servicio.
            if(recordCount == 0 && !txtCodigoServicioPagos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Servicio No existe.");
                txtCodigoServicioPagos.setText("");
                txtCodigoServicioPagos.requestFocus();
            }  
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCodigoServicioPagosFocusLost

    private void txtCedulaInstructorPagosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaInstructorPagosFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", nombre1 = "", apellido1 = "",  nombrecompleto = "";
        
        //SQL : Validar el Instructor y nombre del Instructor
        sSQL = "SELECT nombre1, apellido1 FROM instructores "
               + "WHERE cedula = '"+txtCedulaInstructorPagos.getText()+"'";
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            //Contar el tamaño del ResultSet
            int recordCount = 0;
            while(rs.next())
            {
                recordCount++;
                nombre1 = rs.getString("nombre1");
                apellido1 = rs.getString("apellido1");
            }
            
            //Validar si existe el Servicio
            if(recordCount != 1 && !txtCedulaInstructorPagos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Instructor No Existe.");
                txtCedulaInstructorPagos.setText("");
                txtCedulaInstructorPagos.requestFocus();
            }
            else
            {
                //.trim() quita todos los espacios en blanco generados por almacenamiento en la BD.                
                nombrecompleto = nombre1.trim()+" "+apellido1.trim();
                
                txtNombreInstructorPagos.setText(nombrecompleto);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCedulaInstructorPagosFocusLost

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PagoInstructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PagoInstructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PagoInstructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PagoInstructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PagoInstructores().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcularPagos;
    private javax.swing.JButton btnInstructoresPagos;
    private javax.swing.JButton btnNuevoPagos;
    private javax.swing.JButton btnSalirPagos;
    private javax.swing.JButton btnServiciosPagos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblConsultaPagos;
    private javax.swing.JTextField txtCedulaInstructorPagos;
    private javax.swing.JTextField txtCodigoServicioPagos;
    private javax.swing.JTextField txtFechaFinPagos;
    private javax.swing.JTextField txtFechaInicioPagos;
    private javax.swing.JTextField txtNombreInstructorPagos;
    private javax.swing.JTextField txtTotalPagos;
    // End of variables declaration//GEN-END:variables
}
