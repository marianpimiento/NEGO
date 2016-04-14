
package nego;

import BaseDatos.ConexionPostgresql;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;


public class Inscripciones extends javax.swing.JFrame {

    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Inscripciones() {
        initComponents();
        CargarTablaInscripciones("");
        inhabilitarInscripcion();
    }
    
    void inhabilitarInscripcion() {
        
         //Inhabilitar todos los campos de captura de datos
        txtFechaInscripcion.setEnabled(false);
        txtCedulaClienteInscripcion.setEnabled(false);
        txtNombreClienteInscripcion.setEnabled(false);
        txtCodigoServicioInscripcion.setEnabled(false);
        txtDescripcionServicioInscripcion.setEnabled(false);
        txtTipoServicioInscripcion.setEnabled(false);
        cboDiasServicioInscripcion.setEnabled(false);
        txtFechaInicioInscripcion.setEnabled(false);
        txtFechaFinInscripcion.setEnabled(false);
        txtValorInscripcion.setEnabled(false);
       
        //Dejar vacios todos los campos de captura de datos
        txtFechaInscripcion.setText("");
        txtCedulaClienteInscripcion.setText("");
        txtNombreClienteInscripcion.setText("");
        txtDescripcionServicioInscripcion.setText("");
        txtTipoServicioInscripcion.setText("");
        txtCodigoServicioInscripcion.setText("");
        txtFechaInicioInscripcion.setText("");
        txtFechaFinInscripcion.setText("");
        txtValorInscripcion.setText("");
                      
        //Inhabilitar los botones Guardar y Cancelar
        btnGuardarInscripcion.setEnabled(false);
        btnCancelarInscripcion.setEnabled(false);
        
    }
    
    void habilitarInscripcion() {
        
        //txtFechaInscripcion.setEnabled(true);
        txtCedulaClienteInscripcion.setEnabled(true);
        txtCodigoServicioInscripcion.setEnabled(true);
        txtFechaInicioInscripcion.setEnabled(true);
       
        //Dejar vacios todos los campos de captura de datos
        txtFechaInscripcion.setText("");
        txtCedulaClienteInscripcion.setText("");
        txtNombreClienteInscripcion.setText("");
        txtDescripcionServicioInscripcion.setText("");
        txtTipoServicioInscripcion.setText("");
        txtCodigoServicioInscripcion.setText("");
        cboDiasServicioInscripcion.setSelectedIndex(0);
        txtFechaInicioInscripcion.setText("");
        txtFechaFinInscripcion.setText("");
        txtValorInscripcion.setText("");
                      
        //Habilitar los botones Guardar y Cancelar
        btnGuardarInscripcion.setEnabled(true);
        btnCancelarInscripcion.setEnabled(true);
        
        //Poner el foco en Cedula (Primer dato de la captura)
        txtCedulaClienteInscripcion.requestFocus();
        
    }
    
    ///*
    
    void CargarTablaInscripciones(String valor)
    {
        //Titulos para el Table Model
        String[] titulos = {"ID", "Fecha", "CC Cliente", "Nombre Cliente", "Cod. Servicio","Fecha Inicio", "Fecha Fin",  "Valor"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[8];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
                
        //SQL : Leer datos de la tabla Inscripciones, cuando "valorbusqueda" sea igual o este en la contenación de los datos de la inscripción
        sSQL = "SELECT * FROM inscripciones, clientes "
               + "WHERE inscripciones.cedcliente = clientes.cedula and CONCAT(idinscripcion, ' ', fechainscripcion, ' ',cedcliente, ' ', nombre1, ' ', apellido1, ' ',codservicio, ' ', fechainicio, ' ', fechafin, ' ', valor) LIKE '%"+valor+"%' "
               + "ORDER BY idinscripcion";
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("idinscripcion");
                registro[1] = rs.getString("fechainscripcion");
                registro[2] = rs.getString("cedcliente");
                registro[3] = rs.getString("nombre1").trim()+' '+rs.getString("apellido1").trim();
                registro[4] = rs.getString("codservicio");
                registro[5] = rs.getString("fechainicio");
                registro[6] = rs.getString("fechafin");
                registro[7] = rs.getString("valor");
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el modelo en la Tabla de Consulta
            tblConsultaInscripcion2.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }        
    }

    //Id como variable global, para usar en diferentes metodos
    String id_actualizar = "";
    
    void BuscarInscripcionesModificar(String id)
    {
        String sSQL = "";
        String fecha = "", ced = "", cod = "", finicio = "", ffin = "", valor ="";
                
        //SQL : Leer datos de la tabla Inscripciones, cuando el id corresponda a l inscripcion que se esta buscando
        sSQL = "SELECT fechainscripcion, cedcliente, codservicio, fechainicio, fechafin, valor  FROM inscripciones "
               + "WHERE idinscripcion = "+id;
        
        try
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en cada variable los datos del registro encontrado
                fecha = rs.getString("fechainscripcion");
                ced = rs.getString("cedcliente");
                cod = rs.getString("codservicio");
                finicio = rs.getString("fechainicio");
                ffin = rs.getString("fechafin");
                valor = rs.getString("valor");
            }
            
            //Asignar a los jTextField los datos leidos de la BD
            txtFechaInscripcion.setText(fecha.trim());
            txtCedulaClienteInscripcion.setText(ced.trim());
            txtCodigoServicioInscripcion.setText(cod.trim());
            txtFechaInicioInscripcion.setText(finicio.trim());
            txtFechaFinInscripcion.setText(ffin.trim());
            txtValorInscripcion.setText(valor.trim());
            
            //Hacer el id_actualizar (global), igual a el id del registro buscado
            id_actualizar = id;
            
            // *** Mostrar el nombre del Cliente
            sSQL = "";
            String nombre1 = "", apellido1  = "", nombrecompleto = "";
            
            //SQL : Leer datos de la tabla Clientes, cuando la ced corresponda
            sSQL = "SELECT nombre1, apellido1  FROM clientes "
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
            
            txtNombreClienteInscripcion.setText(nombrecompleto);
            
            
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
            
            txtDescripcionServicioInscripcion.setText(descripcion);
            txtTipoServicioInscripcion.setText(tipo);
            
            //Según el tipo del servicio habilita o inhabilita fecha fin y combobos de dias
            if(tipo.equals("P"))
            {
                txtFechaFinInscripcion.setEnabled(true);
                cboDiasServicioInscripcion.setEnabled(false);
            }
            else
            {
                cboDiasServicioInscripcion.setEnabled(true);
                txtFechaFinInscripcion.setEnabled(false);
                
                if (finicio.equals(ffin))
                {
                    cboDiasServicioInscripcion.setSelectedIndex(1);
                }
                else
                {
                    cboDiasServicioInscripcion.setSelectedIndex(0);
                }
            }
            
            
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
        pnRegistroCliente2 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtCedulaClienteInscripcion = new javax.swing.JTextField();
        txtCodigoServicioInscripcion = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtValorInscripcion = new javax.swing.JTextField();
        btnNuevoInscripcion = new javax.swing.JButton();
        btnGuardarInscripcion = new javax.swing.JButton();
        btnCancelarInscripcion = new javax.swing.JButton();
        btnSalirInscripcion = new javax.swing.JButton();
        btnServiciosInscripcion = new javax.swing.JButton();
        btnClientesInscripcion = new javax.swing.JButton();
        txtFechaInscripcion = new javax.swing.JTextField();
        txtFechaInicioInscripcion = new javax.swing.JTextField();
        txtFechaFinInscripcion = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cboDiasServicioInscripcion = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtTipoServicioInscripcion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombreClienteInscripcion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtDescripcionServicioInscripcion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtBuscarInscripcion2 = new javax.swing.JTextField();
        btnBuscarInscripcion2 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblConsultaInscripcion2 = new javax.swing.JTable();

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

        pnRegistroCliente2.setBorder(javax.swing.BorderFactory.createTitledBorder("Inscripción"));

        jLabel26.setText("Fecha:");

        jLabel27.setText("* Cédula del Cliente:");

        jLabel28.setText("* Código del Servicio:");

        jLabel29.setText("* Fecha de Inicio:");

        jLabel31.setText("* Fecha Fin:");

        txtCedulaClienteInscripcion.setToolTipText("");
        txtCedulaClienteInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaClienteInscripcionActionPerformed(evt);
            }
        });
        txtCedulaClienteInscripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaClienteInscripcionFocusLost(evt);
            }
        });

        txtCodigoServicioInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoServicioInscripcionActionPerformed(evt);
            }
        });
        txtCodigoServicioInscripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoServicioInscripcionFocusLost(evt);
            }
        });

        jLabel32.setText("Valor:");

        btnNuevoInscripcion.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoInscripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoInscripcion.setText("Nuevo");
        btnNuevoInscripcion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoInscripcionActionPerformed(evt);
            }
        });

        btnGuardarInscripcion.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardarInscripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/guardar11-25.jpg"))); // NOI18N
        btnGuardarInscripcion.setText("Guardar");
        btnGuardarInscripcion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardarInscripcion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarInscripcionMouseEntered(evt);
            }
        });
        btnGuardarInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarInscripcionActionPerformed(evt);
            }
        });

        btnCancelarInscripcion.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelarInscripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x1-25.jpg"))); // NOI18N
        btnCancelarInscripcion.setText("Cancelar");
        btnCancelarInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarInscripcionActionPerformed(evt);
            }
        });

        btnSalirInscripcion.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirInscripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirInscripcion.setText("Salir");
        btnSalirInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirInscripcionActionPerformed(evt);
            }
        });

        btnServiciosInscripcion.setBackground(new java.awt.Color(255, 255, 255));
        btnServiciosInscripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnServiciosInscripcion.setText("Ver Servicios");
        btnServiciosInscripcion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnServiciosInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiciosInscripcionActionPerformed(evt);
            }
        });

        btnClientesInscripcion.setBackground(new java.awt.Color(255, 255, 255));
        btnClientesInscripcion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnClientesInscripcion.setText("Ver Clientes ");
        btnClientesInscripcion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnClientesInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesInscripcionActionPerformed(evt);
            }
        });

        txtFechaInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaInscripcionActionPerformed(evt);
            }
        });

        txtFechaInicioInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaInicioInscripcionActionPerformed(evt);
            }
        });
        txtFechaInicioInscripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFechaInicioInscripcionFocusLost(evt);
            }
        });

        txtFechaFinInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaFinInscripcionActionPerformed(evt);
            }
        });
        txtFechaFinInscripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFechaFinInscripcionFocusLost(evt);
            }
        });

        jLabel1.setText("Dias del Servicio:");

        cboDiasServicioInscripcion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Curso (30 días)", "Clase (1 día)" }));
        cboDiasServicioInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDiasServicioInscripcionActionPerformed(evt);
            }
        });

        jLabel2.setText("Tipo del Servicio:");

        txtTipoServicioInscripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipoServicioInscripcionActionPerformed(evt);
            }
        });

        jLabel4.setText("Formato de fecha: aaaa-mm-dd");

        jLabel3.setText("* Campos Obligatorios");

        jLabel5.setText("Nombre del Cliente:");

        jLabel7.setText("Descripción del Servicio:");

        javax.swing.GroupLayout pnRegistroCliente2Layout = new javax.swing.GroupLayout(pnRegistroCliente2);
        pnRegistroCliente2.setLayout(pnRegistroCliente2Layout);
        pnRegistroCliente2Layout.setHorizontalGroup(
            pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                        .addComponent(btnNuevoInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(btnGuardarInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnCancelarInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btnSalirInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtFechaFinInscripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                            .addComponent(txtFechaInicioInscripcion, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel4)
                                        .addGap(135, 135, 135))
                                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtCedulaClienteInscripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                            .addComponent(txtCodigoServicioInscripcion, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtValorInscripcion, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(32, 32, 32)
                                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnClientesInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnServiciosInscripcion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente2Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(12, 12, 12))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente2Layout.createSequentialGroup()
                                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel1)
                                                    .addComponent(jLabel7))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))))
                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboDiasServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                                        .addComponent(txtDescripcionServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTipoServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtNombreClienteInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtFechaInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        pnRegistroCliente2Layout.setVerticalGroup(
            pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtFechaInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel27)
                                    .addComponent(txtCedulaClienteInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnClientesInscripcion)
                                    .addComponent(jLabel5)
                                    .addComponent(txtNombreClienteInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel28)
                                    .addComponent(txtCodigoServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnServiciosInscripcion)
                                    .addComponent(jLabel2)
                                    .addComponent(txtTipoServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtDescripcionServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel29)
                                    .addComponent(txtFechaInicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(cboDiasServicioInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel31)
                                    .addComponent(txtFechaFinInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtValorInscripcion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(pnRegistroCliente2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevoInscripcion)
                            .addComponent(btnGuardarInscripcion)
                            .addComponent(btnCancelarInscripcion)
                            .addComponent(btnSalirInscripcion)))
                    .addGroup(pnRegistroCliente2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Inscripciones"));

        jLabel6.setText("Buscar:");

        btnBuscarInscripcion2.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarInscripcion2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/buscar1-25.jpg"))); // NOI18N
        btnBuscarInscripcion2.setText("Buscar");
        btnBuscarInscripcion2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBuscarInscripcion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarInscripcion2ActionPerformed(evt);
            }
        });

        tblConsultaInscripcion2.setModel(new javax.swing.table.DefaultTableModel(
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
        tblConsultaInscripcion2.setComponentPopupMenu(jPopupMenu1);
        tblConsultaInscripcion2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane6.setViewportView(tblConsultaInscripcion2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarInscripcion2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnBuscarInscripcion2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1224, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtBuscarInscripcion2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarInscripcion2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnRegistroCliente2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnRegistroCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoServicioInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoServicioInscripcionActionPerformed
        txtCodigoServicioInscripcion.transferFocus();
    }//GEN-LAST:event_txtCodigoServicioInscripcionActionPerformed

    private void btnSalirInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirInscripcionActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirInscripcionActionPerformed

    private void btnNuevoInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoInscripcionActionPerformed
        habilitarInscripcion();
        
        //Accion Insertar para guardar y validar
        accion  = "Insertar";
        
        //Inhabilita nuevo y habilita cancelar
        btnNuevoInscripcion.setEnabled(false);
        btnCancelarInscripcion.setEnabled(true);
        
        //Toma por defecto el primer item del combobox
        cboDiasServicioInscripcion.setSelectedIndex(0);
        
        //Toma la fecha del dia
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
        
        txtFechaInscripcion.setText(fin);
        txtFechaInicioInscripcion.setText(fin);
        txtFechaFinInscripcion.setText(fin);
        
    }//GEN-LAST:event_btnNuevoInscripcionActionPerformed

    private void btnCancelarInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarInscripcionActionPerformed
        inhabilitarInscripcion();
        
        //Habilita nuevo e inhabilita cancelar
        btnNuevoInscripcion.setEnabled(true);
        btnCancelarInscripcion.setEnabled(false);
    }//GEN-LAST:event_btnCancelarInscripcionActionPerformed

    private void txtCedulaClienteInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaClienteInscripcionActionPerformed
        txtCedulaClienteInscripcion.transferFocus();
    }//GEN-LAST:event_txtCedulaClienteInscripcionActionPerformed

    private void btnServiciosInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiciosInscripcionActionPerformed
        new Servicios().setVisible(true);
        
        btnServiciosInscripcion.transferFocus();
    }//GEN-LAST:event_btnServiciosInscripcionActionPerformed

    
    String accion ="Insertar";
    
    private void btnGuardarInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarInscripcionActionPerformed
        String ced = "", cod = "",fecha = "", finicio = "", ffin = "";
        int valor = 0;
        String sSQL = "",mensaje = "";
        btnNuevoInscripcion.setEnabled(true);
        if( !txtFechaInscripcion.getText().equals("") && !txtCedulaClienteInscripcion.getText().equals("") && !txtCodigoServicioInscripcion.getText().equals("") && !txtFechaInicioInscripcion.getText().equals("") && !txtFechaFinInscripcion.getText().equals(""))
        {
            //Guadar en las variables los datos registrados en los jTextField
            fecha = txtFechaInscripcion.getText();
            ced = txtCedulaClienteInscripcion.getText();
            cod = txtCodigoServicioInscripcion.getText();
            finicio = txtFechaInicioInscripcion.getText();
            ffin = txtFechaFinInscripcion.getText();
            valor = Integer.parseInt(txtValorInscripcion.getText());
            
            if(accion.equals("Insertar"))
            {
                //SQL : Insertar en la BD en la tabla Inscripciones los '?'
                sSQL = "INSERT INTO inscripciones(fechainscripcion,cedcliente,codservicio,fechainicio,fechafin,valor)"
                        + "VALUES(?, ?, ?, ?, ?, ?)";
                
                mensaje = "Los datos se han Insertado";
            }
            else if(accion.equals("Modificar"))
            {
                //SQL : Actualizar en la BD en la tabla Inscripciones los '?', para el id_actualizar
                sSQL = "UPDATE inscripciones "
                        + "SET fechainscripcion= ?,"
                        + " cedcliente = ?,"
                        + " codservicio = ?,"
                        + " fechainicio = ?,"
                        + " fechafin = ?, "
                        + " valor = ? "
                        + "WHERE idinscripcion = "+id_actualizar;
                
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
                pst.setString(4, finicio);
                pst.setString(5, ffin);
                pst.setInt(6, valor);
                
                //Inhabilitar (Método)
                inhabilitarInscripcion();
                
                //Actualizar la BD
                int n = pst.executeUpdate();
                
                //Verificar que se haya actualizado la BD
                if (n > 0)
                {
                    JOptionPane.showMessageDialog(null, mensaje);
                    CargarTablaInscripciones("");
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
        
    }//GEN-LAST:event_btnGuardarInscripcionActionPerformed

    private void btnClientesInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesInscripcionActionPerformed
        new Clientes().setVisible(true);
    }//GEN-LAST:event_btnClientesInscripcionActionPerformed

    
    private void btnBuscarInscripcion2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarInscripcion2ActionPerformed
        String valor = txtBuscarInscripcion2.getText();
        CargarTablaInscripciones(valor);
    }//GEN-LAST:event_btnBuscarInscripcion2ActionPerformed

    private void txtFechaInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaInscripcionActionPerformed
        txtFechaInscripcion.transferFocus();
    }//GEN-LAST:event_txtFechaInscripcionActionPerformed

    private void txtCodigoServicioInscripcionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoServicioInscripcionFocusLost
        //Accion cuando pierde el foco
        
        //*** Valida que exista el Servicio, asiga el tipo y el valor
        String sSQL = "", descripcion = "", tipo = "", valor1 = "", valorcurso = "";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT descripcion, tiposervicio, valor1, valorcurso FROM servicios "
               + "WHERE codservicio = '"+txtCodigoServicioInscripcion.getText()+"'";
        
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
                descripcion = rs.getString("descripcion");
                tipo = rs.getString("tiposervicio");
                valor1 = rs.getString("valor1");
                valorcurso = rs.getString("valorcurso");
            }
            
            //Validar si existe el Servicio
            if(recordCount != 1)
            {
                Pattern pat = Pattern.compile(""); 
                Matcher mat = pat.matcher(txtCodigoServicioInscripcion.getText());
                if(!mat.matches())
                {
                    JOptionPane.showMessageDialog(null, "El Servicio No Existe.");
                    txtCodigoServicioInscripcion.setText("");
                    txtTipoServicioInscripcion.setText("");
                }
            }
            else
            {
                txtDescripcionServicioInscripcion.setText(descripcion);
                
                if(tipo.equals("M"))
                {
                    //Si es tipo Mensualidad: Tipo = M, habilita combobox, inhabilita Fecha Fin y deja en blanco el valor
                    cboDiasServicioInscripcion.setEnabled(true);
                    txtFechaFinInscripcion.setEnabled(false);
                    txtTipoServicioInscripcion.setText("M");
                    txtValorInscripcion.setText("");
                    
                    if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Curso (30 días)"))
                    {
                        //Asigna valorcurso a valor Inscripcion
                        txtValorInscripcion.setText(valorcurso.trim());
                    }
                    else if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Clase (1 día)"))
                    {
                        //Asigna valor1 a valor Inscripcion
                        txtValorInscripcion.setText(valor1.trim());
                    }
                }
                else if(tipo.equals("P"))
                {
                    //Si es tipo Personalizado: Tipo = p, inhabilita combobox, habilita Fecha Fin y asigna el valor
                    txtFechaFinInscripcion.setEnabled(true);
                    cboDiasServicioInscripcion.setEnabled(false);
                    txtTipoServicioInscripcion.setText("P");
                    txtValorInscripcion.setText(valorcurso);
                    
                    //Tomar la fecha del dia (Hoy)
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
                    
                    txtFechaFinInscripcion.setText(fin);
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        
        //***** Calcula la Fecha Fin
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String finicio = txtFechaInicioInscripcion.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(finicio);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaInicioInscripcion.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioInscripcion.setText("");
                txtFechaInicioInscripcion.requestFocus();
                ver = 1;
            }
        }
        catch (ParseException ex)
        {
            ver = 1;
            
            Pattern pat = Pattern.compile(""); 
            Matcher mat = pat.matcher(txtCodigoServicioInscripcion.getText());
            if(!mat.matches())
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioInscripcion.setText("");
                txtFechaInicioInscripcion.requestFocus();
            }
            
        }
        
        String fin=txtFechaInicioInscripcion.getText();
	int mayor = fin.compareTo( txtFechaFinInscripcion.getText() );
        
        if(ver == 0 && mayor > 0 && !txtFechaInicioInscripcion.getText().equals("") && !txtFechaFinInscripcion.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Inicio es Posterior a la Fecha Fin.");
            txtFechaInicioInscripcion.setText("");
        }
                
        //Si la Fecha inicio está bien y es tipo Mensualidad
        if (ver == 0 && txtTipoServicioInscripcion.getText().equals("M"))
        {
            Calendar fechainicio = Calendar.getInstance();
            fechainicio.setTime(dateObj);
            
            Calendar fechafin = fechainicio;
            
            //Si es tipo Curso, entonces suma 30 días
            if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Curso (30 días)"))
            {
                fechafin.add(Calendar.MONTH, 1);
            }

            //Toma Año, Mes y Día del Calendar
             String aniof = String.valueOf(fechafin.get(Calendar.YEAR));
             
             String mesf = String.valueOf((fechafin.get(Calendar.MONTH) + 1));
             if ((fechafin.get(Calendar.MONTH) + 1) < 10)
             {
                 mesf = "0" + mesf;
             }
                 
             String diaf = String.valueOf(fechafin.get(Calendar.DATE));
             if (fechafin.get(Calendar.DATE) < 10)
             {
                 diaf = "0" + diaf;
             }
             
             //Concatena Año, Mes y Día para formato de fechas
             String fecha = aniof + "-" + mesf + "-" + diaf;
             
             //Asigna la Fecha Fin
             txtFechaFinInscripcion.setText(fecha);
        }
        
        
        // *** Si no tiene nada (en blanco) inhabilita fecha fin y dias servicio(combobox)
        if(txtCodigoServicioInscripcion.getText().equals(""))
        {
            txtFechaFinInscripcion.setEnabled(false);
            cboDiasServicioInscripcion.setEditable(false);
        }
        
    }//GEN-LAST:event_txtCodigoServicioInscripcionFocusLost

    private void txtFechaInicioInscripcionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaInicioInscripcionFocusLost
        //Calculo de Fecha Fin
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String finicio = txtFechaInicioInscripcion.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(finicio);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaInicioInscripcion.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioInscripcion.setText("");
                txtFechaInicioInscripcion.requestFocus();
                ver = 1;
            }
                        
        } catch (ParseException ex) {
            ver = 1;
            
            Pattern pat = Pattern.compile(""); 
            Matcher mat = pat.matcher(txtFechaInicioInscripcion.getText());
            if(!mat.matches())
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioInscripcion.setText("");                
                txtFechaInicioInscripcion.requestFocus();
            }
        }
        
        String fin=txtFechaInicioInscripcion.getText();
	int mayor = fin.compareTo( txtFechaFinInscripcion.getText() );
        
        if(ver == 0 && mayor > 0 && !txtFechaInicioInscripcion.getText().equals("") && !txtFechaFinInscripcion.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Inicio es Posterior a la Fecha Fin.");
            txtFechaInicioInscripcion.setText("");
        }
        
        
                
        //Si la Fecha inicio está bien y es tipo Mensualidad
        if (ver == 0 && txtTipoServicioInscripcion.getText().equals("M"))
        {
            Calendar fechainicio = Calendar.getInstance();
            fechainicio.setTime(dateObj);
            
            Calendar fechafin = fechainicio;
            
            //Si es tipo Curso, entonces suma 30 días
            if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Curso (30 días)"))
            {
                fechafin.add(Calendar.MONTH, 1);
            }

            //Toma Año, Mes y Día del Calendar
             String aniof = String.valueOf(fechafin.get(Calendar.YEAR));
             
             String mesf = String.valueOf((fechafin.get(Calendar.MONTH) + 1));
             if ((fechafin.get(Calendar.MONTH) + 1) < 10)
             {
                 mesf = "0" + mesf;
             }
                 
             String diaf = String.valueOf(fechafin.get(Calendar.DATE));
             if (fechafin.get(Calendar.DATE) < 10)
             {
                 diaf = "0" + diaf;
             }
             
             //Concatena Año, Mes y Día para formato de fechas
             String fecha = aniof + "-" + mesf + "-" + diaf;
             
             //Asigna la Fecha Fin
             txtFechaFinInscripcion.setText(fecha);
        }
        
    }//GEN-LAST:event_txtFechaInicioInscripcionFocusLost

    private void txtFechaFinInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaFinInscripcionActionPerformed
        txtFechaFinInscripcion.transferFocus();
    }//GEN-LAST:event_txtFechaFinInscripcionActionPerformed

    private void txtFechaInicioInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaInicioInscripcionActionPerformed
        txtFechaInicioInscripcion.transferFocus();
    }//GEN-LAST:event_txtFechaInicioInscripcionActionPerformed

    private void txtTipoServicioInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipoServicioInscripcionActionPerformed
        txtTipoServicioInscripcion.transferFocus();
    }//GEN-LAST:event_txtTipoServicioInscripcionActionPerformed

    private void cboDiasServicioInscripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDiasServicioInscripcionActionPerformed
        //Captura Item del combobox
        String tipo = cboDiasServicioInscripcion.getSelectedItem().toString();
        
        //Accion cuando pierde el foco
        String sSQL = "", valor1 = "", valorcurso = "";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT valor1, valorcurso FROM servicios "
               + "WHERE codservicio = '"+txtCodigoServicioInscripcion.getText()+"'";
        
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
                valor1 = rs.getString("valor1");
                valorcurso = rs.getString("valorcurso");
            }
            
            //Validar si existe el Servicio
            if(recordCount != 1 && !txtCodigoServicioInscripcion.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Servicio No Existe.");
                txtTipoServicioInscripcion.setText("");
            }
            else
            {
                if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Curso (30 días)"))
                {
                    //Asigna valorcurso a valor Inscripcion
                    txtValorInscripcion.setText(valorcurso);
                }
                else if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Clase (1 día)"))
                {
                    //Asigna valor1 a valor Inscripcion
                    txtValorInscripcion.setText(valor1);
                }
            }            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        
        //Si existe fecha en Fecha Inicio
        if(!txtFechaInicioInscripcion.getText().equals(""))
        {
            int ver = 0;
            
            //Guadar en las variables los datos registrados en los jTextField
            String finicio = txtFechaInicioInscripcion.getText();
            
            //Convertir la fecha de String a Calendar 
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
            Date dateObj = null;
            
            try {
                dateObj = curFormater.parse(finicio);
                
                //Valida que la fecha esté en el formato.
               Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
               Matcher mat = pat.matcher( txtFechaInicioInscripcion.getText()); 
               if(!mat.matches()){
                   JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                   txtFechaInicioInscripcion.setText("");
                   txtFechaInicioInscripcion.requestFocus();
                   ver = 1;
               }
                             
            } catch (ParseException ex) {
                ver = 1;
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioInscripcion.setText("");
                txtFechaInicioInscripcion.requestFocus();
            }
            
            String fin=txtFechaInicioInscripcion.getText();
            int mayor = fin.compareTo( txtFechaFinInscripcion.getText() );
            
            if(ver == 0 && mayor > 0 && !txtFechaInicioInscripcion.getText().equals("") && !txtFechaFinInscripcion.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"La Fecha Inicio es Posterior a la Fecha Fin.");
                txtFechaInicioInscripcion.setText("");
            }
            
            //Si la Fecha inicio está bien y es tipo Mensualidad
            if (ver == 0 && txtTipoServicioInscripcion.getText().equals("M"))
            {
                Calendar fechainicio = Calendar.getInstance();
                fechainicio.setTime(dateObj);
                
                Calendar fechafin = fechainicio;
                
                //Si es tipo Curso, entonces suma 30 días
                if(cboDiasServicioInscripcion.getSelectedItem().toString().equals("Curso (30 días)"))
                {
                    fechafin.add(Calendar.MONTH, 1);
                }
                
                String aniof = String.valueOf(fechafin.get(Calendar.YEAR));
                
                String mesf = String.valueOf((fechafin.get(Calendar.MONTH) + 1));
                if ((fechafin.get(Calendar.MONTH) + 1) < 10)
                {
                    mesf = "0" + mesf;
                }
                
                String diaf = String.valueOf(fechafin.get(Calendar.DATE));
                if (fechafin.get(Calendar.DATE) < 10)             {
                    diaf = "0" + diaf;
                }
                
                String fecha = aniof + "-" + mesf + "-" + diaf;
                
                txtFechaFinInscripcion.setText(fecha);
            }
            cboDiasServicioInscripcion.transferFocus();
        }  
        
    }//GEN-LAST:event_cboDiasServicioInscripcionActionPerformed

    private void txtCedulaClienteInscripcionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaClienteInscripcionFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", nombre1 = "", apellido1 = "",  nombrecompleto = "";
        
        //SQL : Validar el cliente y nombre del cliente
        sSQL = "SELECT nombre1, apellido1 FROM clientes "
               + "WHERE cedula = '"+txtCedulaClienteInscripcion.getText()+"'";
        
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
            if(recordCount != 1)
            {
                Pattern pat = Pattern.compile("");
                Matcher mat = pat.matcher(txtCedulaClienteInscripcion.getText());
                if(!mat.matches())
                {
                    JOptionPane.showMessageDialog(null, "El Cliente No Existe.");
                    txtCedulaClienteInscripcion.setText("");
                    txtCedulaClienteInscripcion.requestFocus();
                }
            }
            else
            {
                //.trim() quita todos los espacios en blanco generados por almacenamiento en la BD.                
                nombrecompleto = nombre1.trim()+" "+apellido1.trim();
                
                txtNombreClienteInscripcion.setText(nombrecompleto);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }  
        
    }//GEN-LAST:event_txtCedulaClienteInscripcionFocusLost

    private void txtFechaFinInscripcionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaFinInscripcionFocusLost
        //Validar formato de fecha
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String ffin = txtFechaFinInscripcion.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(ffin);
            
            //Validar que la fecha tenga el formato correcto
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaFinInscripcion.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtFechaFinInscripcion.setText("");
                txtFechaFinInscripcion.requestFocus();
                ver = 1;
            }
            
            
        } catch (ParseException ex) {
            ver = 1;
            Pattern pat = Pattern.compile(""); 
            Matcher mat = pat.matcher(txtFechaFinInscripcion.getText());
            if(!mat.matches())
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtFechaFinInscripcion.setText("");
                txtFechaFinInscripcion.requestFocus();
            }
        }
        
        String fin=txtFechaFinInscripcion.getText();
	int mayor = fin.compareTo( txtFechaInicioInscripcion.getText() );
        
        if(ver == 0 && mayor < 0 && !txtFechaInicioInscripcion.getText().equals("") && !txtFechaFinInscripcion.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Fin es Anterior a la Fecha Inicio.");
            txtFechaFinInscripcion.setText("");
        }
        
    }//GEN-LAST:event_txtFechaFinInscripcionFocusLost

    private void mnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarActionPerformed
        //Menu desplegable "Modificar"
        
        int filasel;
        String id;
        
        try
        {
            //Tomar el número de la fila (resgistro) seleccionada
            filasel = tblConsultaInscripcion2.getSelectedRow();
            
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
                modelo = (DefaultTableModel) tblConsultaInscripcion2.getModel();
                id = (String) modelo.getValueAt(filasel, 0);
                
                //Habilitar los campos jTextField y buscar la inscripcion, según el id
                habilitarInscripcion();
                BuscarInscripcionesModificar(id);
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
        filasel = tblConsultaInscripcion2.getSelectedRow();
        
        //Verificar si se selecciono alguna fila
        if(filasel == -1)
        {
            JOptionPane.showMessageDialog(null, "No se ha Seleccionado Ninguna Fila.");
        }
        else
        {
            //Pregunta : ¿Seguro que desea eliminar la inscripcion? Si o No
            if (JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea eliminar la Inscripción?",
                "Eliminar Inscripción", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                String sSQL = "";
                
                //Leer el id de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaInscripcion2.getModel();
                id = (String) modelo.getValueAt(filasel, 0);
                
                //SQL : Eliminar en la BD en la tabla inscripciones, el registro identificado con el id
                sSQL = "DELETE FROM inscripciones "
                        + "WHERE idinscripcion = "+id;
                
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
                CargarTablaInscripciones("");
                
            }
        }
        
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void btnGuardarInscripcionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarInscripcionMouseEntered
        btnCancelarInscripcion.requestFocus();
    }//GEN-LAST:event_btnGuardarInscripcionMouseEntered

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
            java.util.logging.Logger.getLogger(Inscripciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inscripciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inscripciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inscripciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inscripciones().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarInscripcion2;
    private javax.swing.JButton btnCancelarInscripcion;
    private javax.swing.JButton btnClientesInscripcion;
    private javax.swing.JButton btnGuardarInscripcion;
    private javax.swing.JButton btnNuevoInscripcion;
    private javax.swing.JButton btnSalirInscripcion;
    private javax.swing.JButton btnServiciosInscripcion;
    private javax.swing.JComboBox cboDiasServicioInscripcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JMenuItem mnModificar;
    private javax.swing.JPanel pnRegistroCliente2;
    private javax.swing.JTable tblConsultaInscripcion2;
    private javax.swing.JTextField txtBuscarInscripcion2;
    private javax.swing.JTextField txtCedulaClienteInscripcion;
    private javax.swing.JTextField txtCodigoServicioInscripcion;
    private javax.swing.JTextField txtDescripcionServicioInscripcion;
    private javax.swing.JTextField txtFechaFinInscripcion;
    private javax.swing.JTextField txtFechaInicioInscripcion;
    private javax.swing.JTextField txtFechaInscripcion;
    private javax.swing.JTextField txtNombreClienteInscripcion;
    private javax.swing.JTextField txtTipoServicioInscripcion;
    private javax.swing.JTextField txtValorInscripcion;
    // End of variables declaration//GEN-END:variables
}
