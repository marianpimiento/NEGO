
package nego;

import BaseDatos.ConexionPostgresql;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Clientes extends javax.swing.JFrame {
    
    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Clientes() {
        initComponents();
        CargarTablaClientes("");
        inhabilitarCliente();
    }
    
    
    void inhabilitarCliente() {
        
        //Inhabilitar todos los campos de captura de datos
        txtCedulaCliente.setEnabled(false);
        txtNombre1Cliente.setEnabled(false);
        txtNombre2Cliente.setEnabled(false);
        txtApellido1Cliente.setEnabled(false);
        txtApellido2Cliente.setEnabled(false);
        txtDireccionCliente.setEnabled(false);
        txtTelefonoCliente.setEnabled(false);
        txtCelularCliente.setEnabled(false);
        txtEmailCliente.setEnabled(false);
        cboSexoCliente.setEnabled(false);
        
        //Dejar vacios todos los campos de captura de datos
        txtCedulaCliente.setText("");
        txtNombre1Cliente.setText("");
        txtNombre2Cliente.setText("");
        txtApellido1Cliente.setText("");
        txtApellido2Cliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtCelularCliente.setText("");
        txtEmailCliente.setText("");
        
        //Inhabilitar los botones Guardar y Cancelar
        btnGuardarCliente.setEnabled(false);
        btnCancelarCliente.setEnabled(false);
        
    }
    
    void habilitarCliente() {
        
        //Habilitar todos los campos de captura de datos
        txtCedulaCliente.setEnabled(true);
        txtNombre1Cliente.setEnabled(true);
        txtNombre2Cliente.setEnabled(true);
        txtApellido1Cliente.setEnabled(true);
        txtApellido2Cliente.setEnabled(true);
        txtDireccionCliente.setEnabled(true);
        txtTelefonoCliente.setEnabled(true);
        txtCelularCliente.setEnabled(true);
        txtEmailCliente.setEnabled(true);
        cboSexoCliente.setEnabled(true);
        
        //Dejar vacios todos los campos de captura de datos
        txtCedulaCliente.setText("");
        txtNombre1Cliente.setText("");
        txtNombre2Cliente.setText("");
        txtApellido1Cliente.setText("");
        txtApellido2Cliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtCelularCliente.setText("");
        txtEmailCliente.setText("");
        //cboSexoCliente.setSelectedIndex('M');
                
        //Habilitar los botones Guardar y Cancelar
        btnGuardarCliente.setEnabled(true);
        btnCancelarCliente.setEnabled(true);
        
        //Poner el foco en Cedula (Primer dato de la captura)
        txtCedulaCliente.requestFocus();
        
    }
    
    void CargarTablaClientes(String valor)
    {
        //Titulos para el Table Model
        String[] titulos = {"Cédula", "P. Nombre", "Otros Nombres","P. Apellido", "S. Apellido",  "Direccion", "Telefono", "Celular", "E-mail", "Sexo"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[10];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
        
        //SQL : Leer datos de la tabla Clientes, cuando "valor" sea igual o este en la contenación de los datos del cliente
        sSQL = "SELECT * FROM clientes "
               + "WHERE CONCAT(cedula, ' ', nombre1, ' ',nombre2, ' ',apellido1, ' ', apellido2, ' ', direccion,' ', telefono, ' ', celular, ' ',email, ' ',sexo) LIKE '%"+valor+"%' "
               + "ORDER BY apellido1, apellido2, nombre1";
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("cedula");
                registro[1] = rs.getString("nombre1");
                registro[2] = rs.getString("nombre2");
                registro[3] = rs.getString("apellido1");
                registro[4] = rs.getString("apellido2");
                registro[5] = rs.getString("direccion");
                registro[6] = rs.getString("telefono");
                registro[7] = rs.getString("celular");
                registro[8] = rs.getString("email");
                
                if(rs.getString("sexo").trim().equals("M"))
                {
                    registro[9] = "Masculino";
                }
                else
                {
                    registro[9] = "Femenino";
                }
                
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el moledo en la Tabla de Consulta
            tblConsultaCliente.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }        
    }
    
    //Ced como variable global, para usar en diferentes metodos
    String ced_actualizar = "";
    
    void BuscarClientesModificar(String ced)
    {
        String sSQL = "";
        String nom1 = "", nom2 = "", ap1 = "", ap2 = "", dir = "", tel = "", cel = "", email = "", sexo = "";
                
        //SQL : Leer datos de la tabla Clientes, cuando la ced corresponda al cliente que se esta buscando
        sSQL = "SELECT nombre1, nombre2, apellido1, apellido2, direccion, telefono, celular, email, sexo  FROM clientes "
               + "WHERE cedula = '"+ced+"'";
        
        try
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            
            while(rs.next())
            {
                //Guadar en cada variable los datos del registro encontrado
                //ced = rs.getString("cedula");
                nom1 = rs.getString("nombre1");
                nom2 = rs.getString("nombre2");
                ap1 = rs.getString("apellido1");
                ap2 = rs.getString("apellido2");
                dir = rs.getString("direccion");
                tel = rs.getString("telefono");
                cel = rs.getString("celular");
                email = rs.getString("email");
                sexo = rs.getString("sexo");
            }
            
            //Asignar a los jTextField los datos leidos de la BD
            txtCedulaCliente.setText(ced.trim());
            txtNombre1Cliente.setText(nom1.trim());
            
            //Cuando nom2 o ap2 son null, no los muestra en el JTextField
            String nombre2 = "|"+nom2+"|";
            if(!nombre2.equals("|null|"))
            {
                txtNombre2Cliente.setText(nom2.trim());
            }
            
            txtApellido1Cliente.setText(ap1.trim());
            
            String apellido2 = "|"+ap2+"|";
            if(!apellido2.equals("|null|"))
            {
                txtApellido2Cliente.setText(ap2.trim());
            }
            
            txtDireccionCliente.setText(dir.trim());
            txtTelefonoCliente.setText(tel.trim());
            txtCelularCliente.setText(cel.trim());
            txtEmailCliente.setText(email.trim());
            
            if(sexo.trim().equals("M"))
            {
                cboSexoCliente.setSelectedIndex(0);
            }
            else
            {
                cboSexoCliente.setSelectedIndex(1);
            }
            
            //Hacer la ced_actualziar (global), igual a la ced del registro buscado
            ced_actualizar = ced;
            
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
        jPanel1 = new javax.swing.JPanel();
        pnRegistroCliente = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCedulaCliente = new javax.swing.JTextField();
        txtNombre1Cliente = new javax.swing.JTextField();
        txtNombre2Cliente = new javax.swing.JTextField();
        txtApellido1Cliente = new javax.swing.JTextField();
        txtApellido2Cliente = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDireccionCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtCelularCliente = new javax.swing.JTextField();
        txtEmailCliente = new javax.swing.JTextField();
        btnNuevoCliente = new javax.swing.JButton();
        btnGuardarCliente = new javax.swing.JButton();
        btnCancelarCliente = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cboSexoCliente = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConsultaCliente = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtBuscarCliente = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();

        mnModificar.setText("Modificar");
        mnModificar.setComponentPopupMenu(jPopupMenu1);
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
        setTitle("Información de Clientes");
        setPreferredSize(new java.awt.Dimension(1290, 762));

        pnRegistroCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Cliente"));
        pnRegistroCliente.setComponentPopupMenu(jPopupMenu1);

        jLabel1.setText("* Cédula:");

        jLabel2.setText("* Primer Nombre:");

        jLabel3.setText("Otros Nombres:");

        jLabel4.setText("* Primer Apellido:");

        jLabel5.setText("Segundo Apellido:");

        txtCedulaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaClienteActionPerformed(evt);
            }
        });
        txtCedulaCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaClienteFocusLost(evt);
            }
        });

        txtNombre1Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre1ClienteActionPerformed(evt);
            }
        });
        txtNombre1Cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombre1ClienteFocusLost(evt);
            }
        });

        txtNombre2Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre2ClienteActionPerformed(evt);
            }
        });
        txtNombre2Cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombre2ClienteFocusLost(evt);
            }
        });

        txtApellido1Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellido1ClienteActionPerformed(evt);
            }
        });
        txtApellido1Cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellido1ClienteFocusLost(evt);
            }
        });

        txtApellido2Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellido2ClienteActionPerformed(evt);
            }
        });
        txtApellido2Cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellido2ClienteFocusLost(evt);
            }
        });

        jLabel10.setText("* Dirección:");

        jLabel11.setText("* Teléfono:");

        jLabel12.setText("* Celular:");

        jLabel13.setText("* e-mail:");

        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });

        txtTelefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoClienteActionPerformed(evt);
            }
        });
        txtTelefonoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelefonoClienteFocusLost(evt);
            }
        });

        txtCelularCliente.setToolTipText("10 dígitos");
        txtCelularCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularClienteActionPerformed(evt);
            }
        });
        txtCelularCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCelularClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCelularClienteFocusLost(evt);
            }
        });

        txtEmailCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtEmailClienteMousePressed(evt);
            }
        });
        txtEmailCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailClienteActionPerformed(evt);
            }
        });
        txtEmailCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailClienteFocusLost(evt);
            }
        });

        btnNuevoCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoCliente.setText("Nuevo");
        btnNuevoCliente.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        btnGuardarCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/guardar11-25.jpg"))); // NOI18N
        btnGuardarCliente.setText("Guardar");
        btnGuardarCliente.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarClienteMouseEntered(evt);
            }
        });
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });

        btnCancelarCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x1-25.jpg"))); // NOI18N
        btnCancelarCliente.setText("Cancelar");
        btnCancelarCliente.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCancelarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarClienteActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel7.setText("* Sexo:");

        cboSexoCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Masculino", "Femenino" }));

        jLabel8.setText("* Campos Obligatorios");

        javax.swing.GroupLayout pnRegistroClienteLayout = new javax.swing.GroupLayout(pnRegistroCliente);
        pnRegistroCliente.setLayout(pnRegistroClienteLayout);
        pnRegistroClienteLayout.setHorizontalGroup(
            pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre1Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido1Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                        .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnGuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43)
                .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                        .addComponent(btnCancelarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel5)
                            .addComponent(jLabel13)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApellido2Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtNombre2Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtEmailCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(cboSexoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 36, Short.MAX_VALUE)
                .addComponent(jLabel8))
        );
        pnRegistroClienteLayout.setVerticalGroup(
            pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevoCliente)
                            .addComponent(btnGuardarCliente)
                            .addComponent(btnCancelarCliente)
                            .addComponent(btnSalir))
                        .addContainerGap())
                    .addGroup(pnRegistroClienteLayout.createSequentialGroup()
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(cboSexoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre1Cliente)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(txtNombre2Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtApellido1Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtApellido2Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(txtEmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroClienteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Clientes"));

        tblConsultaCliente.setModel(new javax.swing.table.DefaultTableModel(
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
        tblConsultaCliente.setComponentPopupMenu(jPopupMenu1);
        tblConsultaCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(tblConsultaCliente);

        jLabel6.setText("Buscar:");

        btnBuscarCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/buscar1-25.jpg"))); // NOI18N
        btnBuscarCliente.setText("Buscar");
        btnBuscarCliente.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnRegistroCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnRegistroCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarClienteActionPerformed
        //Boton Cancular : llama el método inhabilitar
        inhabilitarCliente();
        
        //Habilita nuevo e inhabilita cancelar
        btnNuevoCliente.setEnabled(true);
        btnCancelarCliente.setEnabled(false);
    }//GEN-LAST:event_btnCancelarClienteActionPerformed

    //Variable global "accion", puede ser 'Insertar' o 'Modificar'.
    String accion  = "Insertar";
    //Variables globales para validar cuando accion es modificar
    String cedul, celul, emai;
    
    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed

        btnNuevoCliente.setEnabled(true);
        
        String ced, nom1, nom2, ap1, ap2, dir, tel, cel, email, sexo;
        String sSQL = "", mensaje = "";
        
        if( !txtCedulaCliente.getText().equals("") && !txtNombre1Cliente.getText().equals("") && !txtApellido1Cliente.getText().equals("") && !txtDireccionCliente.getText().equals("") && !txtCelularCliente.getText().equals("") && !txtTelefonoCliente.getText().equals("") && !txtEmailCliente.getText().equals(""))
        {
            //Guadar en las variables los datos registrados en los jTextField
            ced = txtCedulaCliente.getText();
            nom1 = txtNombre1Cliente.getText();
            nom2 = txtNombre2Cliente.getText();
            ap1 = txtApellido1Cliente.getText();
            ap2 = txtApellido2Cliente.getText();
            dir = txtDireccionCliente.getText();
            tel = txtTelefonoCliente.getText();
            cel = txtCelularCliente.getText();
            email = txtEmailCliente.getText();
            if(cboSexoCliente.getSelectedItem().toString().equals("Masculino"))
            {
                sexo = "M";
            }
            else
            {
                sexo = "F";
            }
            

            if(accion.equals("Insertar"))
            {
                //SQL : Insertar en la BD en la tabla Clientes los '?'
                sSQL = "INSERT INTO clientes(cedula, nombre1, nombre2, apellido1, apellido2, direccion, telefono, celular, email, sexo)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                mensaje = "Los datos se han Insertado";
            }
            else if(accion.equals("Modificar"))
            {
                //SQL : Actualizar en la BD en la tabla Clientes los '?', para el ced_actualizar
                sSQL = "UPDATE clientes "
                        + "SET cedula = ?,"
                        + " nombre1 = ?,"
                        + " nombre2 = ?,"
                        + " apellido1 = ?,"
                        + " apellido2 = ?,"
                        + " direccion = ?,"
                        + " telefono = ?,"
                        + " celular = ?,"
                        + " email = ?, "
                        + " sexo = ? "
                        + "WHERE cedula = '"+ced_actualizar+"'";
            
                mensaje = "Los datos se han Modificado";
            }

            try
            {
                //Usar la conexion con la BD, con un Prepared Statement
                PreparedStatement pst = cn.prepareStatement(sSQL);
            
                //Con pst asignar los valores de las variables, según el número de interrogante que corresponda.
                pst.setString(1, ced);
                pst.setString(2, nom1);
                pst.setString(3, nom2);
                pst.setString(4, ap1);
                pst.setString(5, ap2);
                pst.setString(6, dir);
                pst.setString(7, tel);
                pst.setString(8, cel);
                pst.setString(9, email);
                pst.setString(10, sexo);

                //Inhabilitar (Método)
                inhabilitarCliente();

                //Actualizar la BD
                int n = pst.executeUpdate();

                //Verificar que se haya actualizado la BD
                if (n > 0)
                {
                    JOptionPane.showMessageDialog(null, mensaje);
                    CargarTablaClientes("");
                }

            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Por favor llene todos los Campos Obligatorios!");
        }
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClienteActionPerformed
        //Boton Nuevo : llama el método habilitar
        habilitarCliente();
        
        //Accion en insertar para guardar y validar
        accion  = "Insertar";
        
        //Asigar por defecto el primer item del combobox
        cboSexoCliente.setSelectedIndex(0);
        
        //Inhabilita nuevo y habilita cancelar
        btnNuevoCliente.setEnabled(false);
        btnCancelarCliente.setEnabled(true);
    }//GEN-LAST:event_btnNuevoClienteActionPerformed

    private void txtEmailClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailClienteActionPerformed
        
    }//GEN-LAST:event_txtEmailClienteActionPerformed

    private void txtCelularClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularClienteActionPerformed
        txtCelularCliente.transferFocus();
    }//GEN-LAST:event_txtCelularClienteActionPerformed

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        txtTelefonoCliente.transferFocus();
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        txtDireccionCliente.transferFocus();
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void txtApellido2ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellido2ClienteActionPerformed
        txtApellido2Cliente.transferFocus();
    }//GEN-LAST:event_txtApellido2ClienteActionPerformed

    private void txtApellido1ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellido1ClienteActionPerformed
        txtApellido1Cliente.transferFocus();
    }//GEN-LAST:event_txtApellido1ClienteActionPerformed

    private void txtNombre2ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre2ClienteActionPerformed
        txtNombre2Cliente.transferFocus();
    }//GEN-LAST:event_txtNombre2ClienteActionPerformed

    private void txtNombre1ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre1ClienteActionPerformed
        txtNombre1Cliente.transferFocus();
    }//GEN-LAST:event_txtNombre1ClienteActionPerformed

    private void txtCedulaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaClienteActionPerformed
        //txtCedula : Transfiere el foco --> De la misma forma para los demas campos
        txtCedulaCliente.transferFocus();
    }//GEN-LAST:event_txtCedulaClienteActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        //Botón Salir : Cierra la aplicación
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        //Boton Buscar : llama el método Buscar y a Cargar Tabla
        String valor = txtBuscarCliente.getText();
        CargarTablaClientes(valor);
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void mnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarActionPerformed
        //Menu desplegable "Modificar"
        
        int filasel;
        String ced;
        
        try
        {
            //Tomar el número de la fila (resgistro) seleccionada
            filasel = tblConsultaCliente.getSelectedRow();
            
            //Verificar si se selecciono alguna fila
            if(filasel == -1)
            {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila");
            }
            else
            {
                //Si seleccionó alguna fila, entonces hable la variable accion (global) = "Modificar"
                accion = "Modificar";
                                
                //Leer la ced de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaCliente.getModel();
                ced = (String) modelo.getValueAt(filasel, 0);
                                
                //Habilitar los campos jTextField y buscar el cliente, según la ced
                habilitarCliente();
                BuscarClientesModificar(ced);
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
        String ced = "";
        
        //Tomar el número de la fila (resgistro) seleccionada
        filasel = tblConsultaCliente.getSelectedRow();
        
        //Verificar si se selecciono alguna fila
        if(filasel == -1)
        {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila");
        }
        else
        {
            //Pregunta : ¿Seguro que desea eliminar el Cliente? Si o No
            if (JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea eliminar el Cliente?",
                "Eliminar Cliente", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                String sSQL = "";
                
                //Leer la ced de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaCliente.getModel();
                ced = (String) modelo.getValueAt(filasel, 0);
                
                //SQL : Eliminar en la BD en la tabla Clientes, el registro identificado con la cedula ced
                sSQL = "DELETE FROM clientes "
                        + "WHERE cedula = '"+ced+"'";
                
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
                CargarTablaClientes("");
                
            }
            
            
        }
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void txtCedulaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaClienteFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", cedula ="";
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT cedula FROM clientes "
             + "WHERE cedula = '"+txtCedulaCliente.getText()+"'";
        
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
                cedula = rs.getString("cedula");
            }
        
            //Validar si existe el Servicio
            if(accion.equals("Insertar")){
                if(recordCount != 0)
                {
                JOptionPane.showMessageDialog(null, "Ya existe el Cliente "+txtCedulaCliente.getText()+".");
                txtCedulaCliente.setText("");
                txtCedulaCliente.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9]+)[0-9]([0-9]+$)|");
                    Matcher mat = pat.matcher(txtCedulaCliente.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Cédula No Válida.");
                        txtCedulaCliente.setText("");
                        txtCedulaCliente.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !cedul.equals(cedula.trim()))
                {
                JOptionPane.showMessageDialog(null, "Ya existe el Cliente "+txtCedulaCliente.getText()+".");
                txtCedulaCliente.setText("");
                txtCedulaCliente.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9]+)[0-9]([0-9]+$)|");
                    Matcher mat = pat.matcher(txtCedulaCliente.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Cédula No Válida.");
                        txtCedulaCliente.setText("");
                        txtCedulaCliente.requestFocus();
                    }
                }
            }   
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }  
    }//GEN-LAST:event_txtCedulaClienteFocusLost

    private void txtEmailClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailClienteFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", email ="";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM clientes "
               + "WHERE email = '"+txtEmailCliente.getText()+"'";
        
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
                email = rs.getString("email");
            }
            
            //Validar si existe el Servicio
            if(accion.equals("Insertar")){
                if(recordCount != 0)
                {
                JOptionPane.showMessageDialog(null, "Ya existe el correo "+txtEmailCliente.getText()+".");
                txtEmailCliente.setText("");
                txtEmailCliente.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[\\w-]+)(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*((\\.[A-Za-z]{2,})$)|");
                    Matcher mat = pat.matcher(txtEmailCliente.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Correo No Válido.");
                        txtEmailCliente.setText("");
                        txtEmailCliente.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !emai.equals(email.trim()))
                {
                JOptionPane.showMessageDialog(null, "Ya existe el correo "+txtEmailCliente.getText()+".");
                txtEmailCliente.setText("");
                txtEmailCliente.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[\\w-]+)(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*((\\.[A-Za-z]{2,})$)|");
                    Matcher mat = pat.matcher(txtEmailCliente.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Correo No Válido.");
                        txtEmailCliente.setText("");
                        txtEmailCliente.requestFocus();
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        } 
    }//GEN-LAST:event_txtEmailClienteFocusLost

    private void txtNombre1ClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombre1ClienteFocusLost
        //Accion cuando pierde el foco
        //Valida que el nombre no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtNombre1Cliente.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Nombre No Válido.");
            txtNombre1Cliente.setText("");
            txtNombre1Cliente.requestFocus();
        }

    }//GEN-LAST:event_txtNombre1ClienteFocusLost

    private void txtNombre2ClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombre2ClienteFocusLost
        //Accion cuando pierde el foco
        //Valida que el nombre no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtNombre2Cliente.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Nombre No Válido.");
            txtNombre2Cliente.setText("");
            txtNombre2Cliente.requestFocus();
        }
    }//GEN-LAST:event_txtNombre2ClienteFocusLost

    private void txtApellido1ClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellido1ClienteFocusLost
        //Accion cuando pierde el foco
        //Valida que el Apellido no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtApellido1Cliente.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Apellido No Válido.");
            txtApellido1Cliente.setText("");
            txtApellido1Cliente.requestFocus();
        }
    }//GEN-LAST:event_txtApellido1ClienteFocusLost

    private void txtApellido2ClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellido2ClienteFocusLost
        //Accion cuando pierde el foco
        //Valida que el Apellido no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtApellido2Cliente.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Apellido No Válido.");
            txtApellido2Cliente.setText("");
            txtApellido2Cliente.requestFocus();
        }
    }//GEN-LAST:event_txtApellido2ClienteFocusLost

    private void txtTelefonoClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoClienteFocusLost
        //Accion cuando pierde el foco
        //Valida que el teléfono no contenga letras
        Pattern pat = Pattern.compile("(^[0-9]+)[0-9]([0-9]+$)|");
        Matcher mat = pat.matcher(txtTelefonoCliente.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Teléfono No Válido.");
            txtTelefonoCliente.setText("");
            txtTelefonoCliente.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoClienteFocusLost

    private void txtCelularClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularClienteFocusLost
        //Accion cuando pierde el foco
        //Valida que el teléfono no contenga letras
        String sSQL = "", celular ="";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM clientes "
               + "WHERE celular = '"+txtCelularCliente.getText()+"'";
        
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
                celular = rs.getString("celular");
            }
            
            //Validar si existe el Servicio
            if(accion.equals("Insertar")){
                if(recordCount != 0)
                {
                JOptionPane.showMessageDialog(null, "Ya existe el Celular "+txtCelularCliente.getText()+".");
                txtCelularCliente.setText("");
                txtCelularCliente.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9])[0-9]{8}([0-9]$)|");
                    Matcher mat = pat.matcher(txtCelularCliente.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Celular No Válido, 10 dígitos.");
                        txtCelularCliente.setText("");
                        txtCelularCliente.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !celul.equals(celular.trim()))
                {
                JOptionPane.showMessageDialog(null, "Ya existe el Celular "+txtCelularCliente.getText()+".");
                txtCelularCliente.setText("");
                txtCedulaCliente.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9])[0-9]{8}([0-9]$)|");
                    Matcher mat = pat.matcher(txtCelularCliente.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Celular No Válido, 10 dígitos.");
                        txtCelularCliente.setText("");
                        txtCelularCliente.requestFocus();
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCelularClienteFocusLost

    private void txtCedulaClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaClienteFocusGained
        cedul=txtCedulaCliente.getText();
    }//GEN-LAST:event_txtCedulaClienteFocusGained

    private void txtCelularClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularClienteFocusGained
        celul=txtCelularCliente.getText();
    }//GEN-LAST:event_txtCelularClienteFocusGained

    private void txtEmailClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailClienteFocusGained
        emai=txtEmailCliente.getText();
    }//GEN-LAST:event_txtEmailClienteFocusGained

    private void txtEmailClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmailClienteMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailClienteMousePressed

    private void btnGuardarClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarClienteMouseEntered
        btnCancelarCliente.requestFocus();
    }//GEN-LAST:event_btnGuardarClienteMouseEntered

    
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
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clientes().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnCancelarCliente;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cboSexoCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JMenuItem mnModificar;
    private javax.swing.JPanel pnRegistroCliente;
    private javax.swing.JTable tblConsultaCliente;
    private javax.swing.JTextField txtApellido1Cliente;
    private javax.swing.JTextField txtApellido2Cliente;
    private javax.swing.JTextField txtBuscarCliente;
    private javax.swing.JTextField txtCedulaCliente;
    private javax.swing.JTextField txtCelularCliente;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtEmailCliente;
    private javax.swing.JTextField txtNombre1Cliente;
    private javax.swing.JTextField txtNombre2Cliente;
    private javax.swing.JTextField txtTelefonoCliente;
    // End of variables declaration//GEN-END:variables
}