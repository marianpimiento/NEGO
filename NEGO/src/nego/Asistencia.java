
package nego;

import BaseDatos.ConexionPostgresql;
import java.awt.Color;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Asistencia extends javax.swing.JFrame {

    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Asistencia() {
        initComponents();
        CargarTablaAsistencias("","","","");
        inhabilitarAsistencia();
    }
    
    void inhabilitarAsistencia() {
        
         //Inhabilitar todos los campos de captura de datos
        txtIdClaseAsistencia.setEnabled(false);
        txtCodigoServicioAsistencia.setEnabled(false);
        txtDescripcionServicioAsistencia.setEnabled(false);
        txtFechaClaseAsistencia.setEnabled(false);
        txtHoraClaseAsistencia.setEnabled(false);
        txtCedulaClienteAsistencia.setEnabled(false);
        txtNombreClienteAsistencia.setEnabled(false);
       
        //Dejar vacios todos los campos de captura de datos
        txtIdClaseAsistencia.setText("");
        txtCodigoServicioAsistencia.setText("");
        txtDescripcionServicioAsistencia.setText("");
        txtFechaClaseAsistencia.setText("");
        txtHoraClaseAsistencia.setText("");
        txtCedulaClienteAsistencia.setText("");
        txtNombreClienteAsistencia.setText("");
        
        //Hhabilitar los botones Guardar y Cancelar
        btnGuardarAsistencia.setEnabled(false);
        btnCancelarAsistencia.setEnabled(false);
    }
    
    void habilitarAsistencia() {
        
         //Habilitar todos los campos de captura de datos
        txtIdClaseAsistencia.setEnabled(true);
        txtCedulaClienteAsistencia.setEnabled(true);
       
        //Dejar vacios todos los campos de captura de datos
        txtIdClaseAsistencia.setText("");
        txtCodigoServicioAsistencia.setText("");
        txtDescripcionServicioAsistencia.setText("");
        txtFechaClaseAsistencia.setText("");
        txtHoraClaseAsistencia.setText("");
        txtCedulaClienteAsistencia.setText("");
        txtNombreClienteAsistencia.setText("");
                      
        //Inhabilitar los botones Guardar y Cancelar
        btnGuardarAsistencia.setEnabled(true);
        btnCancelarAsistencia.setEnabled(true);
        
         //Poner el foco en el Id Clase (Primer dato de la captura)
        txtIdClaseAsistencia.requestFocus();
        
    }
    
    void CargarTablaAsistencias(String id, String fini, String ffin, String cli)
    {
        //Titulos para el Table Model
        String[] titulos = {"ID Asistencia", "ID Clase", "Cod. Servicio", "Fecha", "Hora",  "CC Cliente", "Nombre Cliente", };
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[7];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
        
        //SQL : Leer datos de las tablas asistencias, clases, clientes, según el parámetro de busqueda
        if(!fini.equals("") && !ffin.equals("")&& !id.equals("")){
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' AND clases.idclase = "+id+" "
               + " AND clases.fecha >='"+fini+"' AND  clases.fecha <='"+ffin+"' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }      
        else  if(!fini.equals("") && !ffin.equals("")&& id.equals("")){      
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' "
               + " AND clases.fecha >='"+fini+"' AND  clases.fecha <='"+ffin+"' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
        else if(!fini.equals("") && ffin.equals("")&& !id.equals("")){
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' AND clases.idclase = "+id+" "
               + " AND clases.fecha >='"+fini+"' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
        else if(fini.equals("") && !ffin.equals("")&& !id.equals("")){
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' AND clases.idclase = "+id+" "
               + " AND clases.fecha <='"+ffin+"' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
        else if(!fini.equals("") && ffin.equals("")&& id.equals("")){
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' "
               + " AND clases.fecha >='"+fini+"' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
        else if(fini.equals("") && !ffin.equals("")&& id.equals("")){
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' "
               + " AND clases.fecha <='"+ffin+"' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
        else if(fini.equals("") && ffin.equals("")&& !id.equals("")){
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' AND clases.idclase = "+id+" "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
        else
        {
            sSQL = "SELECT * FROM asistencias, clases, clientes "
               + " WHERE CONCAT(cedula, ' ', nombre1, ' ', apellido1) LIKE '%"+cli+"%' "
               + " AND clientes.cedula = asistencias.cedcliente AND asistencias.idclase = clases.idclase  "
               + " ORDER BY clases.fecha, asistencias.idclase, asistencias.idasistencia";
        }
                
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("idasistencia");
                registro[1] = rs.getString("idclase");
                registro[2] = rs.getString("codservicio");
                registro[3] = rs.getString("fecha");
                registro[4] = rs.getString("horainicio");
                registro[5] = rs.getString("cedcliente");
                registro[6] = rs.getString("nombre1").trim()+' '+rs.getString("apellido1").trim();
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el modelo en la Tabla de Consulta
            tblConsultaAsistencia.setModel(modelo);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }        
    }
    
    //Id como variable global, para usar en diferentes metodos
    String id_actualizar = "";
    
    void BuscarAsistenciasModificar(String id)
    {
        String sSQL = "";
        String idclase = "", codservicio = "", descripcion = "", fecha = "", hora = "", cedcliente = "", nombre = "";
                
        //SQL : Leer datos de la tabla asistencias, cuando el id corresponda
        sSQL = "SELECT asistencias.idclase, clases.codservicio, servicios.descripcion, clases.fecha, clases.horainicio, asistencias.cedcliente, clientes.nombre1, clientes.apellido1  FROM asistencias, clases, clientes, servicios "
               + "WHERE asistencias.idclase = clases.idclase AND asistencias.cedcliente = clientes.cedula AND clases.codservicio = servicios.codservicio AND asistencias.idasistencia = "+id;
        
        try
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en cada variable los datos del registro encontrado
                idclase = rs.getString("idclase");
                codservicio = rs.getString("codservicio");
                descripcion = rs.getString("descripcion");
                fecha = rs.getString("fecha");
                hora = rs.getString("horainicio");
                cedcliente = rs.getString("cedcliente");
                nombre = rs.getString("nombre1").trim()+' '+rs.getString("apellido1").trim();
            }
            
            //Asignar a los jTextField los datos leidos de la BD
            txtIdClaseAsistencia.setText(idclase.trim());
            txtCodigoServicioAsistencia.setText(codservicio.trim());
            txtDescripcionServicioAsistencia.setText(descripcion.trim());
            txtFechaClaseAsistencia.setText(fecha.trim());
            txtHoraClaseAsistencia.setText(hora.trim());
            txtCedulaClienteAsistencia.setText(cedcliente.trim());
            txtNombreClienteAsistencia.setText(nombre.trim());            
            
            //Hacer el id_actualizar (global), igual al id del registro buscado
            id_actualizar = id;
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }           
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        mnModificar = new javax.swing.JMenuItem();
        mnEliminar = new javax.swing.JMenuItem();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblConsultaAsistencia = new javax.swing.JTable();
        btnBuscarAsistencia = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtBuscarIdClaseAsistencia = new javax.swing.JTextField();
        txtBuscarFechaInicioAsistencia = new javax.swing.JTextField();
        txtBuscarFechaFinAsistencia = new javax.swing.JTextField();
        txtBuscarCedulaClienteAsistencia = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdClaseAsistencia = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoServicioAsistencia = new javax.swing.JTextField();
        btnClasesAsistencia = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFechaClaseAsistencia = new javax.swing.JTextField();
        txtHoraClaseAsistencia = new javax.swing.JTextField();
        txtCedulaClienteAsistencia = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDescripcionServicioAsistencia = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtNombreClienteAsistencia = new javax.swing.JTextField();
        btnNuevoAsistencia = new javax.swing.JButton();
        btnGuardarAsistencia = new javax.swing.JButton();
        btnCancelarAsistencia = new javax.swing.JButton();
        btnSalirAsistencia = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        mnModificar.setText("Modificar");
        mnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModificarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnModificar);

        mnEliminar.setText("Eliminar");
        mnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEliminarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnEliminar);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1290, 762));

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Asistencia"));

        tblConsultaAsistencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblConsultaAsistencia.setComponentPopupMenu(jPopupMenu1);
        tblConsultaAsistencia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane8.setViewportView(tblConsultaAsistencia);

        btnBuscarAsistencia.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/buscar1-25.jpg"))); // NOI18N
        btnBuscarAsistencia.setText("Buscar");
        btnBuscarAsistencia.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBuscarAsistencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarAsistenciaMouseEntered(evt);
            }
        });
        btnBuscarAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarAsistenciaActionPerformed(evt);
            }
        });

        jLabel9.setText("Fecha Inicio:");

        jLabel10.setText("Fecha Fin:");

        jLabel11.setText("Cliente (Cedula o  Nombre) :");

        jLabel12.setText("ID Clase:");

        txtBuscarIdClaseAsistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarIdClaseAsistenciaFocusLost(evt);
            }
        });

        txtBuscarFechaInicioAsistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarFechaInicioAsistenciaFocusLost(evt);
            }
        });

        txtBuscarFechaFinAsistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarFechaFinAsistenciaFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarIdClaseAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarFechaInicioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarFechaFinAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarCedulaClienteAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscarAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addComponent(jScrollPane8)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarAsistencia)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(txtBuscarIdClaseAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarFechaInicioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarFechaFinAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarCedulaClienteAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registo Asistencia"));

        jLabel1.setText("* ID Clase:");

        txtIdClaseAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdClaseAsistenciaActionPerformed(evt);
            }
        });
        txtIdClaseAsistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdClaseAsistenciaFocusLost(evt);
            }
        });

        jLabel2.setText("Código del Servicio:");

        btnClasesAsistencia.setBackground(new java.awt.Color(255, 255, 255));
        btnClasesAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnClasesAsistencia.setText("Ver Clases   ");
        btnClasesAsistencia.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnClasesAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClasesAsistenciaActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha:");

        jLabel4.setText("Hora:");

        jLabel5.setText("* Cédula del Cliente:");

        txtCedulaClienteAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaClienteAsistenciaActionPerformed(evt);
            }
        });
        txtCedulaClienteAsistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaClienteAsistenciaFocusLost(evt);
            }
        });

        jLabel6.setText("Descripción del Servicio:");

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        jButton2.setText("Ver Inscripciones");
        jButton2.setToolTipText("");
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setText("Nombre del Cliente:");

        btnNuevoAsistencia.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoAsistencia.setText("Nuevo");
        btnNuevoAsistencia.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoAsistenciaActionPerformed(evt);
            }
        });

        btnGuardarAsistencia.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardarAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/guardar11-25.jpg"))); // NOI18N
        btnGuardarAsistencia.setText("Guardar");
        btnGuardarAsistencia.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardarAsistencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarAsistenciaMouseEntered(evt);
            }
        });
        btnGuardarAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAsistenciaActionPerformed(evt);
            }
        });

        btnCancelarAsistencia.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelarAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x1-25.jpg"))); // NOI18N
        btnCancelarAsistencia.setText("Cancelar");
        btnCancelarAsistencia.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCancelarAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAsistenciaActionPerformed(evt);
            }
        });

        btnSalirAsistencia.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirAsistencia.setText("Salir");
        btnSalirAsistencia.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalirAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirAsistenciaActionPerformed(evt);
            }
        });

        jLabel8.setText("* Campos Obligatorios");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdClaseAsistencia)
                            .addComponent(txtFechaClaseAsistencia)
                            .addComponent(txtHoraClaseAsistencia)
                            .addComponent(txtCedulaClienteAsistencia)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCodigoServicioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcionServicioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtNombreClienteAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnClasesAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(btnNuevoAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnGuardarAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnCancelarAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnSalirAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 390, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdClaseAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClasesAsistencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodigoServicioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtDescripcionServicioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFechaClaseAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtHoraClaseAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCedulaClienteAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel7)
                    .addComponent(txtNombreClienteAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarAsistencia)
                    .addComponent(btnCancelarAsistencia)
                    .addComponent(btnNuevoAsistencia)
                    .addComponent(btnSalirAsistencia))
                .addGap(5, 5, 5)
                .addComponent(jLabel8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClasesAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClasesAsistenciaActionPerformed
        new Clases().setVisible(true);
    }//GEN-LAST:event_btnClasesAsistenciaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Inscripciones().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnNuevoAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoAsistenciaActionPerformed
        habilitarAsistencia();
        
        //Accion Insertar para guardar y validar
        accion  = "Insertar";
        
        //Inhabilita nuevo y habilita cancelar
        btnNuevoAsistencia.setEnabled(false);
        btnCancelarAsistencia.setEnabled(true);
        
    }//GEN-LAST:event_btnNuevoAsistenciaActionPerformed

    private void btnSalirAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirAsistenciaActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirAsistenciaActionPerformed

    private void btnCancelarAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAsistenciaActionPerformed
        inhabilitarAsistencia();
                
        //Habilita nuevo e inhabilita cancelar
        btnNuevoAsistencia.setEnabled(true);
        btnCancelarAsistencia.setEnabled(false);
    }//GEN-LAST:event_btnCancelarAsistenciaActionPerformed

    String accion ="Insertar";
    private void btnGuardarAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAsistenciaActionPerformed
         
         
         String cedcliente = "", sSQL = "",mensaje = "";
         int idclase = 0;
         
        //Validar que el cliente este inscrito en el servicio y la fecha sea valida
         
         // ver = 0  --> NO  ... ver = 1 --> SI
         int ver = 0;
         
         // *** Valida que se encuentre en el rango de fecha
         if( !txtIdClaseAsistencia.getText().equals("") && !txtCedulaClienteAsistencia.getText().equals(""))
         {
             ver = 1;
             
             //Toma la fecha de la clase          
             String fecha = txtFechaClaseAsistencia.getText();
             
             // *** Validar que exista la inscripcion, con el codservicio, la ced cliente y un rango de fechas válido
             String sSQL2 = "";
             
             //SQL2 : Validar la existencia de la inscripcion
             sSQL = "SELECT cedcliente FROM inscripciones "
                     + "WHERE cedcliente = '"+txtCedulaClienteAsistencia.getText()+"' AND codservicio = '"+txtCodigoServicioAsistencia.getText()+"' AND fechainicio <= '"+fecha+"' AND fechafin >= '"+fecha+"'";
             
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
                 }
                 
                 //Validar si existe el Servicio
                 if(recordCount == 0)
                 {
                     JOptionPane.showMessageDialog(null, "El Cliente No puede tomar la Clase.");
                     ver = 0;
                     
                     //Borrar cedula y nombre del cliente
                     txtCedulaClienteAsistencia.setText("");
                     txtNombreClienteAsistencia.setText("");
                     txtCedulaClienteAsistencia.requestFocus();
                 }
             }
             catch (SQLException ex)
             {
                 JOptionPane.showMessageDialog(null, ex);
             }
         }
         
         // *** Valida que no haya sido resgistrada esa misma asistencias
         if( ver == 1 )
         {
             String sSQL3 = "", cedula = "";
             int id = 0;
             
             //SQL : Validar que no exista el registro de asistencia
             sSQL = "SELECT idclase, cedcliente FROM asistencias "
                     + "WHERE idclase = "+txtIdClaseAsistencia.getText()+" AND cedcliente = '"+txtCedulaClienteAsistencia.getText()+"'";
             
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
                     id = rs.getInt("idclase");
                     cedula = rs.getString("cedcliente");
                 }
                 
                 //Validar si existe la Asistencia
                 if(accion.equals("Insertar"))
                 {
                     if(recordCount != 0)
                     {
                         JOptionPane.showMessageDialog(null, "Ya registró esta Asistencia");
                         txtCedulaClienteAsistencia.setText("");
                         txtNombreClienteAsistencia.setText("");
                         txtCedulaClienteAsistencia.requestFocus();
                         
                         ver = 0;
                     }
                 }//Accion modificar sí solo toma en cuenta los demás datos y no el propio
                 else
                 {
                     //************************************************************
                     if(recordCount != 0  && Integer.parseInt(txtIdClaseAsistencia.getText()) != id && !txtCedulaClienteAsistencia.getText().equals(cedula.trim()))
                     {
                         JOptionPane.showMessageDialog(null, "Ya registró esta Asistencia");
                         txtCedulaClienteAsistencia.setText("");
                         txtNombreClienteAsistencia.setText("");
                         txtCedulaClienteAsistencia.requestFocus();
                         
                         ver = 0;
                     }
                 }   
             }
             
             catch (SQLException ex)
             {
                 JOptionPane.showMessageDialog(null, ex);
             }
         }
         
         // *** Efecuta la accion de Insertar y Modificar
         if( ver == 1 )
         {
             //Guadar en las variables los datos registrados en los jTextField
             cedcliente = txtCedulaClienteAsistencia.getText();
             idclase = Integer.parseInt(txtIdClaseAsistencia.getText());
             
             if(accion.equals("Insertar"))
             {
                 //SQL : Insertar en la BD en la tabla asistencias los '?'
                 sSQL = "INSERT INTO asistencias(cedcliente,idclase)"
                         + "VALUES(?, ?)";
                 
                 mensaje = "Los datos se han Insertado";
             }
             else if(accion.equals("Modificar"))
             {
                 //SQL : Actualizar en la BD en la tabla asistencias los '?', para el id_actualizar
                 sSQL = "UPDATE asistencias "
                         + "SET cedcliente = ?,"
                         + " idclase = ? "
                         + "WHERE idasistencia = "+id_actualizar;
                 
                 mensaje = "Los datos se han Modificado";
             }        
             
             try
             {
                 //Usar la conexion con la BD, con un Prepared Statement
                 PreparedStatement pst = cn.prepareStatement(sSQL);
                 
                 //Con pst asignar los valores de las variables, según el número de intorrogante que corresponda.
                 pst.setString(1, cedcliente);
                 pst.setInt(2, idclase);
                 
                 if(accion.equals("Insertar"))
                 {
                     //Borrar cedula y nombre del cliente
                     txtCedulaClienteAsistencia.setText("");
                     txtNombreClienteAsistencia.setText("");
                     txtCedulaClienteAsistencia.requestFocus(); 
                 }
                 else if(accion.equals("Modificar"))
                 {
                     //Inhabilitar (Metodo)
                     inhabilitarAsistencia();
                 }       
                 
                 //Actualizar la BD
                 int n = pst.executeUpdate();
                 
                 //Verificar que se haya actualizado la BD
                 if (n > 0)
                 {
                     JOptionPane.showMessageDialog(null, mensaje);
                     CargarTablaAsistencias("","","","");
                 }
             }
             catch (SQLException ex)
             {
                 JOptionPane.showMessageDialog(null, ex);
             }
         }
         else
         {
             if(txtIdClaseAsistencia.getText().equals("") && txtCedulaClienteAsistencia.getText().equals("") )
             {
                 JOptionPane.showMessageDialog(null, "Por favor llene todos los Campos Obligatorios.");
             }
         }
    }//GEN-LAST:event_btnGuardarAsistenciaActionPerformed

    private void txtIdClaseAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdClaseAsistenciaActionPerformed
        txtIdClaseAsistencia.transferFocus();
    }//GEN-LAST:event_txtIdClaseAsistenciaActionPerformed

    private void txtCedulaClienteAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaClienteAsistenciaActionPerformed
        txtCedulaClienteAsistencia.transferFocus();
    }//GEN-LAST:event_txtCedulaClienteAsistenciaActionPerformed

    private void txtIdClaseAsistenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdClaseAsistenciaFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", codigo = "", descripcion = "", fecha = "",  hora = "";
        
        if(!txtIdClaseAsistencia.getText().equals(""))
        {
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT clases.codservicio, servicios.descripcion, clases.fecha, clases.horainicio FROM clases, servicios "
                + "WHERE clases.codservicio = servicios.codservicio and clases.idclase = "+txtIdClaseAsistencia.getText();
        
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
                codigo = rs.getString("codservicio");
                descripcion = rs.getString("descripcion");
                fecha = rs.getString("fecha");
                hora = rs.getString("horainicio");
            }
            
            //Validar si existe la Clase
            if(recordCount != 1)
            {
                JOptionPane.showMessageDialog(null, "La Clase No Existe.");
                txtIdClaseAsistencia.setText("");
                txtIdClaseAsistencia.requestFocus();
            }
            else
            {
                txtCodigoServicioAsistencia.setText(codigo);
                txtDescripcionServicioAsistencia.setText(descripcion);
                txtFechaClaseAsistencia.setText(fecha);
                txtHoraClaseAsistencia.setText(hora);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        }
    }//GEN-LAST:event_txtIdClaseAsistenciaFocusLost

    private void btnGuardarAsistenciaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarAsistenciaMouseEntered
        btnCancelarAsistencia.requestFocus();
    }//GEN-LAST:event_btnGuardarAsistenciaMouseEntered

    private void txtCedulaClienteAsistenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaClienteAsistenciaFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", nombre1 = "", apellido1 = "",  nombrecompleto = "";
        
        //SQL : Validar el cliente y nombre del cliente
        sSQL = "SELECT nombre1, apellido1 FROM clientes "
               + "WHERE cedula = '"+txtCedulaClienteAsistencia.getText()+"'";
        
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
            if(recordCount != 1 && !txtCedulaClienteAsistencia.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Cliente No Existe.");
                txtCedulaClienteAsistencia.setText("");
                txtCedulaClienteAsistencia.requestFocus();
            }
            else
            {
                //.trim() quita todos los espacios en blanco generados por almacenamiento en la BD.                
                nombrecompleto = nombre1.trim()+" "+apellido1.trim();
                
                txtNombreClienteAsistencia.setText(nombrecompleto);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCedulaClienteAsistenciaFocusLost

    private void btnBuscarAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarAsistenciaActionPerformed
        CargarTablaAsistencias(txtBuscarIdClaseAsistencia.getText(), txtBuscarFechaInicioAsistencia.getText(), txtBuscarFechaFinAsistencia.getText(), txtBuscarCedulaClienteAsistencia.getText());
        txtBuscarIdClaseAsistencia.requestFocus();
    }//GEN-LAST:event_btnBuscarAsistenciaActionPerformed

    private void mnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarActionPerformed
        //Menu desplegable "Modificar"
        
        int filasel;
        String id;
        
        try
        {
            //Tomar el número de la fila (resgistro) seleccionada
            filasel = tblConsultaAsistencia.getSelectedRow();
            
            //Verificar si se selecciono alguna fila
            if(filasel == -1)
            {
                JOptionPane.showMessageDialog(null, "No se ha Seleccionado Ninguna Fila.");
            }
            else
            {
                //Si seleccionó alguna fila, entonces haga la variable accion (global) = "Modificar"
                accion = "Modificar";
                
                //Leer el id de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaAsistencia.getModel();
                id = (String) modelo.getValueAt(filasel, 0);
                
                //Habilitar los campos jTextField y buscar la inscripcion, según el id
                habilitarAsistencia();
                BuscarAsistenciasModificar(id);
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }//GEN-LAST:event_mnModificarActionPerformed

    private void mnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEliminarActionPerformed
        //Menu desplegable "Eliminar"
        
        int filasel;
        String id;
        
        //Tomar el número de la fila (resgistro) seleccionada
        filasel = tblConsultaAsistencia.getSelectedRow();
        
        //Verificar si se selecciono alguna fila
        if(filasel == -1)
        {
            JOptionPane.showMessageDialog(null, "No se ha Seleccionado Ninguna Fila.");
        }
        else
        {
            //Pregunta : ¿Seguro que desea eliminar la Asistencia? Si o No
            if (JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea eliminar la Asistencia?",
                "Eliminar Asistencia", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                String sSQL = "";
                
                //Leer el id de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaAsistencia.getModel();
                id = (String) modelo.getValueAt(filasel, 0);
                
                //SQL : Eliminar en la BD en la tabla Asistencias, el registro identificado con el id
                sSQL = "DELETE FROM asistencias "
                        + "WHERE idasistencia = "+id;
                
                try 
                {
                    //Usar la conexion con la BD
                    PreparedStatement pst = cn.prepareStatement(sSQL);
                    //Acualizar la BD
                    pst.executeUpdate();
                } 
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
                
                //Cargar tabla (Método)
                CargarTablaAsistencias("","","","");
            }
        }
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void txtBuscarIdClaseAsistenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarIdClaseAsistenciaFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", codigo = "", descripcion = "", fecha = "",  hora = "";
        
        if(!txtBuscarIdClaseAsistencia.getText().equals(""))
        {
        //SQL : Validar el idclase
        sSQL = "SELECT * FROM clases "
                + "WHERE idclase = "+txtBuscarIdClaseAsistencia.getText();
        
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
            }
            
            //Validar si existe la Clase
            if(recordCount != 1)
            {
                JOptionPane.showMessageDialog(null, "La Clase No Existe.");
                txtBuscarIdClaseAsistencia.setText("");
                txtBuscarIdClaseAsistencia.requestFocus();
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        }
    }//GEN-LAST:event_txtBuscarIdClaseAsistenciaFocusLost

    private void txtBuscarFechaInicioAsistenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFechaInicioAsistenciaFocusLost
        //Valisar Fecha Inicio
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String finicio = txtBuscarFechaInicioAsistencia.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(finicio);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtBuscarFechaInicioAsistencia.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtBuscarFechaInicioAsistencia.setText("");                
                txtBuscarFechaInicioAsistencia.requestFocus();
                ver = 1;
            }
        } 
        catch (ParseException ex)
        {
            if(!txtBuscarFechaInicioAsistencia.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtBuscarFechaInicioAsistencia.setText("");
                txtBuscarFechaInicioAsistencia.requestFocus();
                ver = 1;
            }
        }
        
        String fin=txtBuscarFechaInicioAsistencia.getText();
	int mayor = fin.compareTo( txtBuscarFechaFinAsistencia.getText() );
        
        if(ver == 0 && mayor > 0 && !txtBuscarFechaInicioAsistencia.getText().equals("") && !txtBuscarFechaFinAsistencia.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Inicio es Posterior a la Fecha Fin.");
            txtBuscarFechaInicioAsistencia.setText("");
        }
    }//GEN-LAST:event_txtBuscarFechaInicioAsistenciaFocusLost

    private void txtBuscarFechaFinAsistenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFechaFinAsistenciaFocusLost
        // *** Validar formato de fecha
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String ffin = txtBuscarFechaFinAsistencia.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(ffin);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtBuscarFechaFinAsistencia.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtBuscarFechaFinAsistencia.setText("");
                txtBuscarFechaFinAsistencia.requestFocus();                
                ver = 1;
            }
        }
        catch (ParseException ex)
        {
            if(!txtBuscarFechaFinAsistencia.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtBuscarFechaFinAsistencia.setText("");
                txtBuscarFechaFinAsistencia.requestFocus();
                ver = 1;
            }
        }
        
        String fin=txtBuscarFechaFinAsistencia.getText();
	int mayor = fin.compareTo( txtBuscarFechaInicioAsistencia.getText() );
        
        if(ver == 0 && mayor < 0 && !txtBuscarFechaInicioAsistencia.getText().equals("") && !txtBuscarFechaFinAsistencia.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Fin es Anterior a la Fecha Inicio.");
            txtBuscarFechaFinAsistencia.setText("");
        }
    }//GEN-LAST:event_txtBuscarFechaFinAsistenciaFocusLost

    private void btnBuscarAsistenciaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarAsistenciaMouseEntered
        btnCancelarAsistencia.requestFocus();
    }//GEN-LAST:event_btnBuscarAsistenciaMouseEntered

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Asistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Asistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Asistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Asistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Asistencia().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarAsistencia;
    private javax.swing.JButton btnCancelarAsistencia;
    private javax.swing.JButton btnClasesAsistencia;
    private javax.swing.JButton btnGuardarAsistencia;
    private javax.swing.JButton btnNuevoAsistencia;
    private javax.swing.JButton btnSalirAsistencia;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JMenuItem mnModificar;
    private javax.swing.JTable tblConsultaAsistencia;
    private javax.swing.JTextField txtBuscarCedulaClienteAsistencia;
    private javax.swing.JTextField txtBuscarFechaFinAsistencia;
    private javax.swing.JTextField txtBuscarFechaInicioAsistencia;
    private javax.swing.JTextField txtBuscarIdClaseAsistencia;
    private javax.swing.JTextField txtCedulaClienteAsistencia;
    private javax.swing.JTextField txtCodigoServicioAsistencia;
    private javax.swing.JTextField txtDescripcionServicioAsistencia;
    private javax.swing.JTextField txtFechaClaseAsistencia;
    private javax.swing.JTextField txtHoraClaseAsistencia;
    private javax.swing.JTextField txtIdClaseAsistencia;
    private javax.swing.JTextField txtNombreClienteAsistencia;
    // End of variables declaration//GEN-END:variables
}
