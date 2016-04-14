package nego;

import BaseDatos.ConexionPostgresql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Clases extends javax.swing.JFrame {

    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Clases() {
        initComponents();
        CargarTablaClases("");
        inhabilitarClase();
    }

    void inhabilitarClase()
    {
        //Inhabilitar todos los campos de captura de datos
        txtFechaClase.setEnabled(false);
        txtTipoServicioClase.setEnabled(false);
        txtCedulaInstructorClase.setEnabled(false);
        txtNombreInstructorClase.setEnabled(false);
        txtCodigoServicioClase.setEnabled(false);
        txtDescripcionServicioClase.setEnabled(false);
        txtHoraInicioClase.setEnabled(false);
        
         //Dejar vacios todos los campos de captura de datos
        txtFechaClase.setText("");
        txtCedulaInstructorClase.setText("");
        txtNombreInstructorClase.setText("");
        txtCodigoServicioClase.setText("");
        txtDescripcionServicioClase.setText("");
        txtHoraInicioClase.setText("");
                      
        //Inhabilitar los botones Guardar y Cancelar
        btnGuardarClase.setEnabled(false);
        btnCancelarClase.setEnabled(false);
    }
    
    void habilitarClase()
    {
        //Habilitar todos los campos de captura de datos
        txtCedulaInstructorClase.setEnabled(true);
        txtCodigoServicioClase.setEnabled(true);
        txtHoraInicioClase.setEnabled(true);
        
        //Dejar vacios todos los campos de captura de datos
        txtFechaClase.setText("");
        txtCedulaInstructorClase.setText("");
        txtNombreInstructorClase.setText("");
        txtCodigoServicioClase.setText("");
        txtDescripcionServicioClase.setText("");
        txtHoraInicioClase.setText("");
                      
        //Habilitar los botones Guardar y Cancelar
        btnGuardarClase.setEnabled(true);
        btnCancelarClase.setEnabled(true);
                
        //Poner el foco en Cedula (Primer dato de la captura)
        txtCedulaInstructorClase.requestFocus();
    }
    
    
    void CargarTablaClases(String valor)
    {
        //Titulos para el Table Model
        String[] titulos = {"ID", "Fecha", "CC Instructor", "Nombre Inst.","Cod. Servicio","Hora"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[6];
        String sSQL = "",nombreCompleto;
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
        
        //SQL : Leer datos de la tabla Inscripciones, cuando "valorbusqueda" sea igual o este en la contenación de los datos de la inscripción
        sSQL = "SELECT  clases.idclase,clases.fecha,instructores.cedula,clases.cedinstructor,instructores.nombre1,instructores.apellido1, clases.codservicio,clases.horainicio FROM clases,instructores "
               + "WHERE CONCAT(clases.idclase, ' ', clases.fecha, ' ',clases.cedinstructor,' ',instructores.nombre1,' ',instructores.apellido1, ' ',clases.codservicio, ' ', clases.horainicio) LIKE '%"+valor+"%' "
               + "and clases.cedinstructor = instructores.cedula ORDER BY idclase";
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("idclase");
                registro[1] = rs.getString("fecha");
                registro[2] = rs.getString("cedinstructor");
                nombreCompleto=rs.getString("nombre1").trim()+' '+rs.getString("apellido1").trim();
                registro[3] = nombreCompleto;
                registro[4] = rs.getString("codservicio");
                registro[5] = rs.getString("horainicio");
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el modelo en la Tabla de Consulta
            
            tblConsultaClase.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }        
    }
    
    //Id como variable global, para usar en diferentes metodos
    String id_actualizar = "";
    
    void BuscarClasesModificar(String id)
    {
        String sSQL = "";
        String fecha = "", ced = "", cod = "", hora = "";
                
        //SQL : Leer datos de la tabla clases, cuando el id corresponda
        sSQL = "SELECT fecha, cedinstructor, codservicio, horainicio  FROM clases "
               + "WHERE idclase = "+id;
        
        try
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en cada variable los datos del registro encontrado
                fecha = rs.getString("fecha");
                ced = rs.getString("cedinstructor");
                cod = rs.getString("codservicio");
                hora = rs.getString("horainicio");
            }
            
            //Asignar a los jTextField los datos leidos de la BD
            txtFechaClase.setText(fecha.trim());
            txtCedulaInstructorClase.setText(ced.trim());
            txtCodigoServicioClase.setText(cod.trim());
            txtHoraInicioClase.setText(hora.trim());
            
            //Hacer el id_actualizar (global), igual al id del registro buscado
            id_actualizar = id;
            
            // *** Mostrar el nombre del Instructor
            
            sSQL = "";
            String nombre1 = "", apellido1  = "", nombrecompleto = "";
            
            //SQL : Leer datos de la tabla Clientes, cuando la ced corresponda
            sSQL = "SELECT nombre1, apellido1  FROM instructores "
                    + "WHERE cedula = '"+ced+"'";
            
            try
            {
                //Usar la conexion con la BD y crear un Result set
                st = cn.createStatement();
                rs = st.executeQuery(sSQL);
                
                while(rs.next())
                {
                    //Guadar en cada variable los datos del registro encontrado
                    nombre1 = rs.getString("nombre1");
                    apellido1 = rs.getString("apellido1");
                }
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            //.trim() quita todos los espacios en blanco generados por almacenamiento en la BD.                
            nombrecompleto = nombre1.trim()+" "+apellido1.trim();
            
            txtNombreInstructorClase.setText(nombrecompleto);
            
            
            // *** Mostrar la descripcion y el tipo del servicio
            sSQL = "";
            String descripcion = "", tipo  = "";
            
            //SQL : Leer datos de la tabla Clientes, cuando la ced corresponda
            sSQL = "SELECT descripcion, tiposervicio  FROM servicios "
                    + "WHERE codservicio = '"+cod+"'";
            
            try
            {
                //Usar la conexion con la BD y crear un Result set
                st = cn.createStatement();
                rs = st.executeQuery(sSQL);
                
                while(rs.next())
                {
                    //Guadar en cada variable los datos del registro encontrado
                    descripcion = rs.getString("descripcion");
                    tipo = rs.getString("tiposervicio");
                }
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            txtDescripcionServicioClase.setText(descripcion);
            txtTipoServicioClase.setText(tipo);
            
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }           
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        mnModificar = new javax.swing.JMenuItem();
        mnEliminar = new javax.swing.JMenuItem();
        jPanel11 = new javax.swing.JPanel();
        pnRegistroCliente3 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtHoraInicioClase = new javax.swing.JTextField();
        btnNuevoClase = new javax.swing.JButton();
        btnGuardarClase = new javax.swing.JButton();
        btnCancelarClase = new javax.swing.JButton();
        btnSalirClase = new javax.swing.JButton();
        txtFechaClase = new javax.swing.JTextField();
        txtCedulaInstructorClase = new javax.swing.JTextField();
        txtCodigoServicioClase = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnInstructoresClase = new javax.swing.JButton();
        btnServiciosClase = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombreInstructorClase = new javax.swing.JTextField();
        txtDescripcionServicioClase = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTipoServicioClase = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarClase = new javax.swing.JTextField();
        btnBuscarClase = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConsultaClase = new javax.swing.JTable();

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

        pnRegistroCliente3.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Clase"));

        jLabel34.setText("Fecha:");

        jLabel35.setText("* Cédula del Instructor:");

        jLabel36.setText("* Código del Servicio:");

        jLabel37.setText("* Hora:");

        txtHoraInicioClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoraInicioClaseActionPerformed(evt);
            }
        });
        txtHoraInicioClase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHoraInicioClaseFocusLost(evt);
            }
        });

        btnNuevoClase.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoClase.setText("Nuevo");
        btnNuevoClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClaseActionPerformed(evt);
            }
        });

        btnGuardarClase.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardarClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/guardar11-25.jpg"))); // NOI18N
        btnGuardarClase.setText("Guardar");
        btnGuardarClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardarClase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarClaseMouseEntered(evt);
            }
        });
        btnGuardarClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClaseActionPerformed(evt);
            }
        });

        btnCancelarClase.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelarClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x1-25.jpg"))); // NOI18N
        btnCancelarClase.setText("Cancelar");
        btnCancelarClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCancelarClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarClaseActionPerformed(evt);
            }
        });

        btnSalirClase.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirClase.setText("Salir");
        btnSalirClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalirClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirClaseActionPerformed(evt);
            }
        });

        txtCedulaInstructorClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaInstructorClaseActionPerformed(evt);
            }
        });
        txtCedulaInstructorClase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaInstructorClaseFocusLost(evt);
            }
        });

        txtCodigoServicioClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoServicioClaseActionPerformed(evt);
            }
        });
        txtCodigoServicioClase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoServicioClaseFocusLost(evt);
            }
        });

        jLabel1.setText("* Campos Obligatorios");

        btnInstructoresClase.setBackground(new java.awt.Color(255, 255, 255));
        btnInstructoresClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnInstructoresClase.setText("Ver Instructores");
        btnInstructoresClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnInstructoresClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstructoresClaseActionPerformed(evt);
            }
        });

        btnServiciosClase.setBackground(new java.awt.Color(255, 255, 255));
        btnServiciosClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnServiciosClase.setText("Ver Servicios    ");
        btnServiciosClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnServiciosClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiciosClaseActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre del Instructor:");

        jLabel3.setText("Descripción del Servicio:");

        jLabel5.setText("Formato de hora: 23:59");

        jLabel6.setText("Tipo del Servicio:");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Clases"));

        jLabel4.setText("Buscar :");

        btnBuscarClase.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarClase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/buscar1-25.jpg"))); // NOI18N
        btnBuscarClase.setText("Buscar");
        btnBuscarClase.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBuscarClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClaseActionPerformed(evt);
            }
        });

        tblConsultaClase.setModel(new javax.swing.table.DefaultTableModel(
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
        tblConsultaClase.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tblConsultaClase);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarClase, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnBuscarClase, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtBuscarClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarClase))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnRegistroCliente3Layout = new javax.swing.GroupLayout(pnRegistroCliente3);
        pnRegistroCliente3.setLayout(pnRegistroCliente3Layout);
        pnRegistroCliente3Layout.setHorizontalGroup(
            pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFechaClase, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(txtCedulaInstructorClase)
                    .addComponent(txtCodigoServicioClase)
                    .addComponent(txtHoraInicioClase))
                .addGap(28, 28, 28)
                .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnServiciosClase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnInstructoresClase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreInstructorClase, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(txtDescripcionServicioClase))
                        .addGap(48, 48, 48)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtTipoServicioClase, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addGap(0, 114, Short.MAX_VALUE))
            .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(btnNuevoClase, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnGuardarClase, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnCancelarClase, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(btnSalirClase, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnRegistroCliente3Layout.setVerticalGroup(
            pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtFechaClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(txtCedulaInstructorClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInstructoresClase)
                            .addComponent(jLabel2)
                            .addComponent(txtNombreInstructorClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(txtCodigoServicioClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnServiciosClase)
                            .addComponent(jLabel3)
                            .addComponent(txtDescripcionServicioClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtTipoServicioClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(txtHoraInicioClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroCliente3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevoClase)
                            .addComponent(btnGuardarClase)
                            .addComponent(btnCancelarClase)
                            .addComponent(btnSalirClase))
                        .addGap(30, 30, 30))
                    .addGroup(pnRegistroCliente3Layout.createSequentialGroup()
                        .addGap(0, 282, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnRegistroCliente3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnRegistroCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(443, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtHoraInicioClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraInicioClaseActionPerformed
        txtHoraInicioClase.transferFocus();
    }//GEN-LAST:event_txtHoraInicioClaseActionPerformed

    private void btnSalirClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirClaseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirClaseActionPerformed

    private void btnInstructoresClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstructoresClaseActionPerformed
        new Instructores().setVisible(true);
    }//GEN-LAST:event_btnInstructoresClaseActionPerformed

    private void btnServiciosClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiciosClaseActionPerformed
        new Servicios().setVisible(true);
    }//GEN-LAST:event_btnServiciosClaseActionPerformed

    private void btnNuevoClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClaseActionPerformed
        habilitarClase();
        //Accion Insertar para guardar y validar
        accion  = "Insertar";
        
        //Inhabilita nuevo y habilita cancelar
        btnNuevoClase.setEnabled(false);
        btnCancelarClase.setEnabled(true);
        
        
        //Calcular fecha Hoy
        Calendar fechahoy = Calendar.getInstance();
        
        String aniof = String.valueOf(fechahoy.get(Calendar.YEAR));
        
        String mesf = String.valueOf((fechahoy.get(Calendar.MONTH) + 1));
        if ((fechahoy.get(Calendar.MONTH) + 1) < 10)
        {
            mesf = "0" + mesf;
        }
        
        String diaf = String.valueOf(fechahoy.get(Calendar.DATE));
        if (fechahoy.get(Calendar.DATE) < 10)
        {
            diaf = "0" + diaf;
        }
        
        String fin = aniof + "-" + mesf + "-" + diaf;
        
        txtFechaClase.setText(fin);
    }//GEN-LAST:event_btnNuevoClaseActionPerformed

    private void btnCancelarClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarClaseActionPerformed
        inhabilitarClase();
        
        //Habilita nuevo e inhabilita cancelar
        btnNuevoClase.setEnabled(true);
        btnCancelarClase.setEnabled(false);
    }//GEN-LAST:event_btnCancelarClaseActionPerformed

    String accion ="Insertar";
    private void btnGuardarClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClaseActionPerformed
        String ced = "", cod = "",fecha = "", hinicio = "";
        String sSQL = "",mensaje = "";
        
        btnNuevoClase.setEnabled(true);
        
        if( !txtCedulaInstructorClase.getText().equals("") && !txtCodigoServicioClase.getText().equals("") && !txtHoraInicioClase.getText().equals(""))
        {
            //Guadar en las variables los datos registrados en los jTextField
            fecha = txtFechaClase.getText();
            ced = txtCedulaInstructorClase.getText();
            cod = txtCodigoServicioClase.getText();
            hinicio = txtHoraInicioClase.getText();
            
            if(accion.equals("Insertar"))
            {
                //SQL : Insertar en la BD en la tabla clases los '?'
                sSQL = "INSERT INTO clases(fecha,cedinstructor,codservicio,horainicio)"
                        + "VALUES(?, ?, ?, ?)";
                
                mensaje = "Los datos se han Insertado";
            }
            else if(accion.equals("Modificar"))
            {
                //SQL : Actualizar en la BD en la tabla clases los '?', para el id_actualizar
                sSQL = "UPDATE clases "
                        + "SET fecha= ?,"
                        + " cedinstructor = ?,"
                        + " codservicio = ?,"
                        + " horainicio = ? "
                        + "WHERE idclase = "+id_actualizar;
                
                mensaje = "Los datos se han Modificado";
            }        
            
            try
            {
                //Usar la conexion con la BD, con un Prepared Statement
                PreparedStatement pst = cn.prepareStatement(sSQL);
                 
                //Con pst asignar los valores de las variables, según el número de intorrogante que corresponda.
                pst.setString(1, fecha);
                pst.setString(2, ced);
                pst.setString(3, cod);
                pst.setString(4, hinicio);
                
                //Inhabilitar (Método)
                inhabilitarClase();
                
                //Actualizar la BD
                int n = pst.executeUpdate();
                
                //Verificar que se haya actualizado la BD
                if (n > 0)
                {
                    JOptionPane.showMessageDialog(null, mensaje);
                    CargarTablaClases("");
                }
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Por favor llene todos los Campos Obligatorios.");
        }
    }//GEN-LAST:event_btnGuardarClaseActionPerformed

    private void btnBuscarClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClaseActionPerformed
        String valor = txtBuscarClase.getText();
        CargarTablaClases(valor);
    }//GEN-LAST:event_btnBuscarClaseActionPerformed

    private void txtCedulaInstructorClaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaInstructorClaseFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", nombre1 = "", apellido1 = "",  nombrecompleto = "";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM instructores "
               + "WHERE cedula = '"+txtCedulaInstructorClase.getText()+"'";
        
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
                nombre1 = rs.getString("nombre1");
                apellido1 = rs.getString("apellido1");
            }
            
            //Validar si existe la cédula.
            if(recordCount == 0 && !txtCedulaInstructorClase.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Instructor No existe.");
                txtCedulaInstructorClase.setText("");
                txtCedulaInstructorClase.requestFocus();
            }
            else
            {
                //.trim() quita todos los espacios en blanco generados por almacenamiento en la BD.                
                nombrecompleto = nombre1.trim()+" "+apellido1.trim();
                
                txtNombreInstructorClase.setText(nombrecompleto);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCedulaInstructorClaseFocusLost

    private void txtCodigoServicioClaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoServicioClaseFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", descripcion = "", tipo = "";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM servicios "
               + "WHERE codservicio = '"+txtCodigoServicioClase.getText()+"'";
        
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
                descripcion = rs.getString("descripcion");
                tipo = rs.getString("tiposervicio");
            }
            
            //Validar si existe el servicio.
            if(recordCount == 0 && !txtCodigoServicioClase.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Servicio No existe.");
                txtCodigoServicioClase.setText("");
                txtCodigoServicioClase.requestFocus();
            }  
            else
            {
                txtDescripcionServicioClase.setText(descripcion);
                
                if(tipo.equals("M"))
                {
                    //Si es tipo Mensualidad: Tipo = M
                    txtTipoServicioClase.setText("M");
                }
                else if(tipo.equals("P"))
                {
                    //Si es tipo Personalizado: Tipo = p
                    txtTipoServicioClase.setText("P");
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCodigoServicioClaseFocusLost

    private void txtHoraInicioClaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHoraInicioClaseFocusLost
        //Accion cuando pierde el foco
        //Valida que la Hora esté en el formato.
        Pattern pat = Pattern.compile("((([0,1][0-9])|([2][0-3])):([0-5][0-9]))|");
        Matcher mat = pat.matcher( txtHoraInicioClase.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Hora No Válida.");
             txtHoraInicioClase.setText("");
        }
    }//GEN-LAST:event_txtHoraInicioClaseFocusLost

    private void txtCedulaInstructorClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaInstructorClaseActionPerformed
        txtCedulaInstructorClase.transferFocus();
    }//GEN-LAST:event_txtCedulaInstructorClaseActionPerformed

    private void txtCodigoServicioClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoServicioClaseActionPerformed
        txtCodigoServicioClase.transferFocus();
    }//GEN-LAST:event_txtCodigoServicioClaseActionPerformed

    private void mnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarActionPerformed
        //Menu desplegable "Modificar"
        
        int filasel;
        String id;
        
        try
        {
            //Tomar el número de la fila (resgistro) seleccionada
            filasel = tblConsultaClase.getSelectedRow();
            
            //Verificar si se selecciono alguna fila
            if(filasel == -1)
            {
                JOptionPane.showMessageDialog(null, "No se ha Seleccionado Ninguna Fila.");
            }
            else
            {
                //Si seleccionó alguna fila, entonces hable la variable accion (global) = "Modificar"
                accion = "Modificar";
                
                //Leer el id de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaClase.getModel();
                id = (String) modelo.getValueAt(filasel, 0);
                
                //Habilitar los campos jTextField y buscar la inscripcion, según el id
                habilitarClase();
                BuscarClasesModificar(id);
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
        filasel = tblConsultaClase.getSelectedRow();
        
        //Verificar si se selecciono alguna fila
        if(filasel == -1)
        {
            JOptionPane.showMessageDialog(null, "No se ha Seleccionado Ninguna Fila.");
        }
        else
        {
            //Pregunta : ¿Seguro que desea eliminar la clase? Si o No
            if (JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea eliminar la Clase?",
                "Eliminar Clase", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                String sSQL = "";
                
                //Leer el id de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaClase.getModel();
                id = (String) modelo.getValueAt(filasel, 0);
                
                //SQL : Eliminar en la BD en la tabla clases, el registro identificado con el id
                sSQL = "DELETE FROM clases "
                        + "WHERE idclase = "+id;
                
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
                CargarTablaClases("");
            }
        }
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void btnGuardarClaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarClaseMouseEntered
        btnCancelarClase.requestFocus();
    }//GEN-LAST:event_btnGuardarClaseMouseEntered

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
            java.util.logging.Logger.getLogger(Clases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clases().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarClase;
    private javax.swing.JButton btnCancelarClase;
    private javax.swing.JButton btnGuardarClase;
    private javax.swing.JButton btnInstructoresClase;
    private javax.swing.JButton btnNuevoClase;
    private javax.swing.JButton btnSalirClase;
    private javax.swing.JButton btnServiciosClase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JMenuItem mnModificar;
    private javax.swing.JPanel pnRegistroCliente3;
    private javax.swing.JTable tblConsultaClase;
    private javax.swing.JTextField txtBuscarClase;
    private javax.swing.JTextField txtCedulaInstructorClase;
    private javax.swing.JTextField txtCodigoServicioClase;
    private javax.swing.JTextField txtDescripcionServicioClase;
    private javax.swing.JTextField txtFechaClase;
    private javax.swing.JTextField txtHoraInicioClase;
    private javax.swing.JTextField txtNombreInstructorClase;
    private javax.swing.JTextField txtTipoServicioClase;
    // End of variables declaration//GEN-END:variables
}
