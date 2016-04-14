
package nego;

import BaseDatos.ConexionPostgresql;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Instructores extends javax.swing.JFrame {

    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Instructores() {
        initComponents();
        CargarTablaInstructores("");
        inhabilitarInstructor();
    }
    
    void inhabilitarInstructor() {
        
        //Inhabilitar todos los campos de captura de datos
        txtCedulaInstructor.setEnabled(false);
        txtNombre1Instructor.setEnabled(false);
        txtNombre2Instructor.setEnabled(false);
        txtApellido1Instructor.setEnabled(false);
        txtApellido2Instructor.setEnabled(false);
        txtDireccionInstructor.setEnabled(false);
        txtTelefonoInstructor.setEnabled(false);
        txtCelularInstructor.setEnabled(false);
        txtEmailInstructor.setEnabled(false);
        cboSexoInstructor.setEnabled(false);
        
        //Dejar vacios todos los campos de captura de datos
        txtCedulaInstructor.setText("");
        txtNombre1Instructor.setText("");
        txtNombre2Instructor.setText("");
        txtApellido1Instructor.setText("");
        txtApellido2Instructor.setText("");
        txtDireccionInstructor.setText("");
        txtTelefonoInstructor.setText("");
        txtCelularInstructor.setText("");
        txtEmailInstructor.setText("");
                
        //Inhabilitar los botones Guardar y Cancelar
        btnGuardarInstructor.setEnabled(false);
        btnCancelarInstructor.setEnabled(false);
        
    }
    
    
    void habilitarInstructor() {
        
        //Habilitar todos los campos de captura de datos
        txtCedulaInstructor.setEnabled(true);
        txtNombre1Instructor.setEnabled(true);
        txtNombre2Instructor.setEnabled(true);
        txtApellido1Instructor.setEnabled(true);
        txtApellido2Instructor.setEnabled(true);
        txtDireccionInstructor.setEnabled(true);
        txtTelefonoInstructor.setEnabled(true);
        txtCelularInstructor.setEnabled(true);
        txtEmailInstructor.setEnabled(true);
        cboSexoInstructor.setEnabled(true);
        
        //Dejar vacios todos los campos de captura de datos
        txtCedulaInstructor.setText("");
        txtNombre1Instructor.setText("");
        txtNombre2Instructor.setText("");
        txtApellido1Instructor.setText("");
        txtApellido2Instructor.setText("");
        txtDireccionInstructor.setText("");
        txtTelefonoInstructor.setText("");
        txtCelularInstructor.setText("");
        txtEmailInstructor.setText("");
                
        //Habilitar los botones Guardar y Cancelar
        btnGuardarInstructor.setEnabled(true);
        btnCancelarInstructor.setEnabled(true);
        
        //Poner el foco en Cedula (Primer dato de la captura)
        txtCedulaInstructor.requestFocus();
        
    }
    
    void CargarTablaInstructores(String valor)
    {
        //Titulos para el Table Model
        String[] titulos = {"Cedula", "P. Nombre", "Otros Nombres","P. Apellido", "S. Apellido",  "Direccion", "Telefono", "Celular", "E-mail", "Sexo"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[10];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
          
        //SQL : Leer datos de la tabla Instructores, cuando "valor" sea igual o este en la contenación de los datos del instructor
        sSQL = "SELECT * FROM instructores "
               + "WHERE CONCAT(cedula, ' ', nombre1, ' ',nombre2, ' ',apellido1, ' ', apellido2, ' ', direccion,' ', telefono, ' ', celular, ' ',email, ' ',sexo) LIKE '%"+valor+"%'"
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
            tblConsultaInstructor.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }        
    }
    
    //Cedd como variable global, para usar en diferentes metodos
    String ced_actualizar = "";
    
    void BuscarInstructoresModificar(String ced)
    {
        String sSQL = "";
        String nom1 = "", nom2 = "", ap1 = "", ap2 = "", dir = "", tel = "", cel = "", email = "", sexo = "";
        
        //SQL : Leer datos de la tabla Instructores, cuando la ced corresponda al que se esta buscando
        sSQL = "SELECT cedula, nombre1, nombre2, apellido1, apellido2, direccion, telefono, celular, email, sexo  FROM instructores "
               + "WHERE cedula = '"+ced+"'";
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en cada variable los datos del registro encontrado
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
            txtCedulaInstructor.setText(ced.trim());
            txtNombre1Instructor.setText(nom1.trim());
            
            //Cuando nom2 o ap2 son null, no los muestra en el JTextField
            String nombre2 = "|"+nom2+"|";
            if(!nombre2.equals("|null|"))
            {
                txtNombre2Instructor.setText(nom2.trim());
            }
            
            txtApellido1Instructor.setText(ap1.trim());
            
            String apellido2 = "|"+ap2+"|";
            if(!apellido2.equals("|null|"))
            {
                txtApellido2Instructor.setText(ap2.trim());
            }
            
            txtDireccionInstructor.setText(dir.trim());
            txtTelefonoInstructor.setText(tel.trim());
            txtCelularInstructor.setText(cel.trim());
            txtEmailInstructor.setText(email.trim());
            
            if(sexo.trim().equals("M"))
            {
                cboSexoInstructor.setSelectedIndex(0);
            }
            else
            {
                cboSexoInstructor.setSelectedIndex(1);
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
        jPanel4 = new javax.swing.JPanel();
        pnRegistroInstructor = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtCedulaInstructor = new javax.swing.JTextField();
        txtNombre1Instructor = new javax.swing.JTextField();
        txtApellido1Instructor = new javax.swing.JTextField();
        txtNombre2Instructor = new javax.swing.JTextField();
        txtApellido2Instructor = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtDireccionInstructor = new javax.swing.JTextField();
        txtTelefonoInstructor = new javax.swing.JTextField();
        txtCelularInstructor = new javax.swing.JTextField();
        txtEmailInstructor = new javax.swing.JTextField();
        btnNuevoInstructor = new javax.swing.JButton();
        btnGuardarInstructor = new javax.swing.JButton();
        btnCancelarInstructor = new javax.swing.JButton();
        btnSalirInstructor = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboSexoInstructor = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsultaInstructor = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        txtBuscarInstructor = new javax.swing.JTextField();
        btnBuscarInstructor = new javax.swing.JButton();

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

        pnRegistroInstructor.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Instructor"));

        jLabel7.setText("* Cédula:");

        jLabel8.setText("* Primer Nombre:");

        jLabel9.setText("Otros Nombres:");

        jLabel14.setText("* Primer Apellido:");

        jLabel15.setText("Segundo Apellido:");

        txtCedulaInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaInstructorActionPerformed(evt);
            }
        });
        txtCedulaInstructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaInstructorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaInstructorFocusLost(evt);
            }
        });

        txtNombre1Instructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre1InstructorActionPerformed(evt);
            }
        });
        txtNombre1Instructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombre1InstructorFocusLost(evt);
            }
        });

        txtApellido1Instructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellido1InstructorActionPerformed(evt);
            }
        });
        txtApellido1Instructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellido1InstructorFocusLost(evt);
            }
        });

        txtNombre2Instructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre2InstructorActionPerformed(evt);
            }
        });
        txtNombre2Instructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombre2InstructorFocusLost(evt);
            }
        });

        txtApellido2Instructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellido2InstructorActionPerformed(evt);
            }
        });
        txtApellido2Instructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellido2InstructorFocusLost(evt);
            }
        });

        jLabel16.setText("* Dirección:");

        jLabel17.setText("* Teléfono:");

        jLabel18.setText("* Celular:");

        jLabel19.setText("* e-mail:");

        txtDireccionInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionInstructorActionPerformed(evt);
            }
        });

        txtTelefonoInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoInstructorActionPerformed(evt);
            }
        });
        txtTelefonoInstructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelefonoInstructorFocusLost(evt);
            }
        });

        txtCelularInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularInstructorActionPerformed(evt);
            }
        });
        txtCelularInstructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCelularInstructorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCelularInstructorFocusLost(evt);
            }
        });

        txtEmailInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailInstructorActionPerformed(evt);
            }
        });
        txtEmailInstructor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailInstructorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailInstructorFocusLost(evt);
            }
        });

        btnNuevoInstructor.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoInstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoInstructor.setText("Nuevo");
        btnNuevoInstructor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoInstructorActionPerformed(evt);
            }
        });

        btnGuardarInstructor.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardarInstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/guardar11-25.jpg"))); // NOI18N
        btnGuardarInstructor.setText("Guardar");
        btnGuardarInstructor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardarInstructor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarInstructorMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarInstructorMouseEntered(evt);
            }
        });
        btnGuardarInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarInstructorActionPerformed(evt);
            }
        });

        btnCancelarInstructor.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelarInstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x1-25.jpg"))); // NOI18N
        btnCancelarInstructor.setText("Cancelar");
        btnCancelarInstructor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCancelarInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarInstructorActionPerformed(evt);
            }
        });

        btnSalirInstructor.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirInstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirInstructor.setText("Salir");
        btnSalirInstructor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalirInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirInstructorActionPerformed(evt);
            }
        });

        jLabel1.setText("* Sexo:");

        jLabel2.setText("* Campos Obligatorios");

        cboSexoInstructor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Masculino", "Femenino" }));

        javax.swing.GroupLayout pnRegistroInstructorLayout = new javax.swing.GroupLayout(pnRegistroInstructor);
        pnRegistroInstructor.setLayout(pnRegistroInstructorLayout);
        pnRegistroInstructorLayout.setHorizontalGroup(
            pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtApellido1Instructor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                        .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCedulaInstructor, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtNombre1Instructor)))
                    .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(txtDireccionInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCelularInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardarInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                        .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)
                            .addComponent(jLabel9)
                            .addComponent(jLabel1))
                        .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboSexoInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtNombre2Instructor, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                        .addComponent(txtApellido2Instructor, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                        .addComponent(txtTelefonoInstructor, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnSalirInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmailInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(162, Short.MAX_VALUE))))
                    .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                        .addComponent(btnCancelarInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2))))
        );
        pnRegistroInstructorLayout.setVerticalGroup(
            pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroInstructorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCedulaInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cboSexoInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre1Instructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtNombre2Instructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellido1Instructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(txtApellido2Instructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(txtTelefonoInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCelularInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(txtEmailInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroInstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarInstructor)
                    .addComponent(btnNuevoInstructor)
                    .addComponent(btnCancelarInstructor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalirInstructor))
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroInstructorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Instructores"));

        tblConsultaInstructor.setModel(new javax.swing.table.DefaultTableModel(
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
        tblConsultaInstructor.setComponentPopupMenu(jPopupMenu1);
        tblConsultaInstructor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(tblConsultaInstructor);

        jLabel20.setText("Buscar:");

        btnBuscarInstructor.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarInstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/buscar1-25.jpg"))); // NOI18N
        btnBuscarInstructor.setText("Buscar");
        btnBuscarInstructor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBuscarInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarInstructorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnBuscarInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtBuscarInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarInstructor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnRegistroInstructor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnRegistroInstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCedulaInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaInstructorActionPerformed
        //txtCedula : Transfiere el foco --> De la misma forma para los demas campos
        txtCedulaInstructor.transferFocus();
    }//GEN-LAST:event_txtCedulaInstructorActionPerformed

    private void txtApellido1InstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellido1InstructorActionPerformed
        txtApellido1Instructor.transferFocus();
    }//GEN-LAST:event_txtApellido1InstructorActionPerformed

    private void btnCancelarInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarInstructorActionPerformed
        //Boton Cancular : llama el método inhabilitar
        inhabilitarInstructor();
        
        //Habilita nuevo e inhabilita cancelar
        btnNuevoInstructor.setEnabled(true);
        btnCancelarInstructor.setEnabled(false);
    }//GEN-LAST:event_btnCancelarInstructorActionPerformed

    //Variable global "accion", puede ser 'Insertar' o 'Modificar'.
    String accion  = "Insertar";
    //Variables globales para validar cuando accion es modificar
    String cedul, celul, emai;
    
    private void btnGuardarInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarInstructorActionPerformed
        
        btnNuevoInstructor.setEnabled(true);
        
        String ced, nom1, nom2, ap1, ap2, dir, tel, cel, email, sexo;
        String sSQL = "";
        String mensaje = "";
        
        if( !txtCedulaInstructor.getText().equals("") && !txtNombre1Instructor.getText().equals("") && !txtApellido1Instructor.getText().equals("") && !txtDireccionInstructor.getText().equals("") && !txtCelularInstructor.getText().equals("") && !txtTelefonoInstructor.getText().equals("") && !txtEmailInstructor.getText().equals(""))
        {
            //Guadar en las variables los datos registrados en los jTextField
            ced = txtCedulaInstructor.getText();
            nom1 = txtNombre1Instructor.getText();
            nom2 = txtNombre2Instructor.getText();
            ap1 = txtApellido1Instructor.getText();
            ap2 = txtApellido2Instructor.getText();
            dir = txtDireccionInstructor.getText();
            tel = txtTelefonoInstructor.getText();
            cel = txtCelularInstructor.getText();
            email = txtEmailInstructor.getText();
            
            if(cboSexoInstructor.getSelectedItem().toString().equals("Masculino"))
            {
                sexo = "M";
            }
            else
            {
                sexo = "F";
            }

            if(accion.equals("Insertar"))
            {
                //SQL : Insertar en la BD en la tabla Instructores los '?'
                sSQL = "INSERT INTO instructores(cedula, nombre1, nombre2, apellido1, apellido2, direccion, telefono, celular, email, sexo)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                mensaje = "Los datos se han Insertado";
            }
            else if(accion.equals("Modificar"))
            {
                //SQL : Actualizar en la BD en la tabla Instructores los '?', para la ced_actualizar
                sSQL = "UPDATE instructores "
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
            
                //Con pst asignar los valores de las variables, según el número de intorrogante que corresponda.
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
                inhabilitarInstructor();
                //Actualizar la BD
                int n = pst.executeUpdate();
                //Verificar que se haya actualizado la BD
                if (n > 0)
                {
                    JOptionPane.showMessageDialog(null, mensaje);
                    CargarTablaInstructores("");
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
    }//GEN-LAST:event_btnGuardarInstructorActionPerformed

    private void btnNuevoInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoInstructorActionPerformed
        //Boton Nuevo : llama el método habilitar
        habilitarInstructor();
        
        //Accion Insertar para guardar y validar
        accion  = "Insertar";
        
        //Asigar por defecto el primer item del combobox
        cboSexoInstructor.setSelectedIndex(0);
        
        //Inhabilita nuevo y habilita cancelar
        btnNuevoInstructor.setEnabled(false);
        btnCancelarInstructor.setEnabled(true);
    }//GEN-LAST:event_btnNuevoInstructorActionPerformed

    private void txtNombre1InstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre1InstructorActionPerformed
        txtNombre1Instructor.transferFocus();
    }//GEN-LAST:event_txtNombre1InstructorActionPerformed

    private void txtNombre2InstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre2InstructorActionPerformed
        txtNombre2Instructor.transferFocus();
    }//GEN-LAST:event_txtNombre2InstructorActionPerformed

    private void txtTelefonoInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoInstructorActionPerformed
        txtTelefonoInstructor.transferFocus();
    }//GEN-LAST:event_txtTelefonoInstructorActionPerformed

    private void txtDireccionInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionInstructorActionPerformed
        txtDireccionInstructor.transferFocus();
    }//GEN-LAST:event_txtDireccionInstructorActionPerformed

    private void txtApellido2InstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellido2InstructorActionPerformed
        txtApellido2Instructor.transferFocus();
    }//GEN-LAST:event_txtApellido2InstructorActionPerformed

    private void txtCelularInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularInstructorActionPerformed
        txtCelularInstructor.transferFocus();
    }//GEN-LAST:event_txtCelularInstructorActionPerformed

    private void txtEmailInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailInstructorActionPerformed
        txtEmailInstructor.transferFocus();
    }//GEN-LAST:event_txtEmailInstructorActionPerformed

    private void btnSalirInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirInstructorActionPerformed
        //Botón Salir : Cierra la aplicación
        this.dispose();
    }//GEN-LAST:event_btnSalirInstructorActionPerformed

    private void btnBuscarInstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarInstructorActionPerformed
        //Boton Buscar : llama el método Buscar y a Cargar Tabla
        String valor = txtBuscarInstructor.getText();
        CargarTablaInstructores(valor);
    }//GEN-LAST:event_btnBuscarInstructorActionPerformed

    private void mnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarActionPerformed
        //Menu desplegable "Modificar"
        
        int filasel;
        String ced;
        
        try
        {
            //Tomar el número de la fila (resgistro) seleccionada
            filasel = tblConsultaInstructor.getSelectedRow();
            
            //Verificar si se selecciono alguna fila
            if(filasel == -1)
            {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila");
            }
            else
            {
                //Si seleccionó alguna fila, entonces hable la variabl accion (global) = "Mdoficar"
                accion = "Modificar";
                
                //Leer la ced de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaInstructor.getModel();
                ced = (String) modelo.getValueAt(filasel, 0);
                
                //Habilitar los campos jTextField y buscar el cliente, según la ced
                habilitarInstructor();
                BuscarInstructoresModificar(ced);
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
        filasel = tblConsultaInstructor.getSelectedRow();
        
        //Verificar si se selecciono alguna fila
        if(filasel == -1)
        {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila");
        }
        else
        {
            //Pregunta : ¿Seguro que desea eliminar el Instructor? Si o No
            if (JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea eliminar el Instructor?",
                "Eliminar Instructor", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                String sSQL = "";
                
                //Leer la ced de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaInstructor.getModel();
                ced = (String) modelo.getValueAt(filasel, 0);
                
                //SQL : Eliminar en la BD en la tabla Instructores, el registro identificado con la ced
                sSQL = "DELETE FROM instructores "
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
                CargarTablaInstructores("");
                
            }
            
        }
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void txtCedulaInstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaInstructorFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", cedula ="";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT cedula FROM instructores "
               + "WHERE cedula = '"+txtCedulaInstructor.getText()+"'";
        
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
                JOptionPane.showMessageDialog(null, "Ya existe el Cliente "+txtCedulaInstructor.getText()+".");
                txtCedulaInstructor.setText("");
                txtCedulaInstructor.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9]+)[0-9]([0-9]+$)|");
                    Matcher mat = pat.matcher(txtCedulaInstructor.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Cédula No Válida.");
                        txtCedulaInstructor.setText("");
                        txtCedulaInstructor.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !cedul.equals(cedula.trim()))
                {
                JOptionPane.showMessageDialog(null, "Ya existe el Cliente "+txtCedulaInstructor.getText()+".");
                txtCedulaInstructor.setText("");
                txtCedulaInstructor.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9]+)[0-9]([0-9]+$)|");
                    Matcher mat = pat.matcher(txtCedulaInstructor.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Cédula No Válida.");
                        txtCedulaInstructor.setText("");
                        txtCedulaInstructor.requestFocus();
                    }
                }
            }   
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCedulaInstructorFocusLost

    private void txtNombre1InstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombre1InstructorFocusLost
        //Accion cuando pierde el foco
        //Valida que el nombre no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtNombre1Instructor.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Nombre No Válido.");
            txtNombre1Instructor.setText("");
            txtNombre1Instructor.requestFocus();
        }
    }//GEN-LAST:event_txtNombre1InstructorFocusLost

    private void txtEmailInstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailInstructorFocusLost
         //Accion cuando pierde el foco
        String sSQL = "", email ="";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM instructores "
               + "WHERE email = '"+txtEmailInstructor.getText()+"'";
        
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
                JOptionPane.showMessageDialog(null, "Ya existe el correo "+txtEmailInstructor.getText()+".");
                txtEmailInstructor.setText("");
                txtEmailInstructor.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[\\w-]+)(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*((\\.[A-Za-z]{2,})$)|");
                    Matcher mat = pat.matcher(txtEmailInstructor.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Correo No Válido.");
                        txtEmailInstructor.setText("");
                        txtEmailInstructor.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !emai.equals(email.trim()))
                {
                JOptionPane.showMessageDialog(null, "Ya existe el correo "+txtEmailInstructor.getText()+".");
                txtEmailInstructor.setText("");
                txtEmailInstructor.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[\\w-]+)(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*((\\.[A-Za-z]{2,})$)|");
                    Matcher mat = pat.matcher(txtEmailInstructor.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Correo No Válido.");
                        txtEmailInstructor.setText("");
                        txtEmailInstructor.requestFocus();
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        } 
    }//GEN-LAST:event_txtEmailInstructorFocusLost

    private void txtNombre2InstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombre2InstructorFocusLost
        //Accion cuando pierde el foco
        //Valida que el nombre no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtNombre2Instructor.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Nombre No Válido.");
            txtNombre2Instructor.setText("");
            txtNombre1Instructor.requestFocus();
        }
    }//GEN-LAST:event_txtNombre2InstructorFocusLost

    private void txtApellido1InstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellido1InstructorFocusLost
        //Accion cuando pierde el foco
        //Valida que el Apellido no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtApellido1Instructor.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Apellido No Válido.");
            txtApellido1Instructor.setText("");
            txtApellido1Instructor.requestFocus();
        }
    }//GEN-LAST:event_txtApellido1InstructorFocusLost

    private void txtApellido2InstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellido2InstructorFocusLost
        //Accion cuando pierde el foco
        //Valida que el Apellido no contenga números
        Pattern pat = Pattern.compile("[A-Z,a-z,ñ,á,é,í,ó,ú]+|");
        Matcher mat = pat.matcher(txtApellido2Instructor.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Apellido No Válido.");
            txtApellido2Instructor.setText("");
            txtApellido2Instructor.requestFocus();
        }
    }//GEN-LAST:event_txtApellido2InstructorFocusLost

    private void txtTelefonoInstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoInstructorFocusLost
        //Accion cuando pierde el foco
        //Valida que el teléfono no contenga letras
        Pattern pat = Pattern.compile("(^[0-9]+)[0-9]([0-9]+$)|");
        Matcher mat = pat.matcher(txtTelefonoInstructor.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Teléfono No Válido.");
            txtTelefonoInstructor.setText("");
            txtTelefonoInstructor.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoInstructorFocusLost

    private void txtCelularInstructorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularInstructorFocusLost
        //Accion cuando pierde el foco
        //Valida que el teléfono no contenga letras
        String sSQL = "", celular ="";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM instructores "
               + "WHERE celular = '"+txtCelularInstructor.getText()+"'";
        
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
                JOptionPane.showMessageDialog(null, "Ya existe el Celular "+txtCelularInstructor.getText()+".");
                txtCelularInstructor.setText("");
                txtCelularInstructor.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9])[0-9]{8}([0-9]$)|");
                    Matcher mat = pat.matcher(txtCelularInstructor.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Celular No Válido, 10 dígitos.");
                        txtCelularInstructor.setText("");
                        txtCelularInstructor.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !celul.equals(celular.trim()))
                {
                    JOptionPane.showMessageDialog(null, "Ya existe el Celular "+txtCelularInstructor.getText()+".");
                    txtCelularInstructor.setText("");
                    txtCelularInstructor.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("(^[0-9])[0-9]{8}([0-9]$)|");
                    Matcher mat = pat.matcher(txtCelularInstructor.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Celular No Válido, 10 dígitos.");
                        txtCelularInstructor.setText("");
                        txtCelularInstructor.requestFocus();
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCelularInstructorFocusLost

    private void txtCedulaInstructorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaInstructorFocusGained
        cedul=txtCedulaInstructor.getText();
    }//GEN-LAST:event_txtCedulaInstructorFocusGained

    private void txtCelularInstructorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularInstructorFocusGained
        celul=txtCelularInstructor.getText();
    }//GEN-LAST:event_txtCelularInstructorFocusGained

    private void txtEmailInstructorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailInstructorFocusGained
        emai=txtEmailInstructor.getText();
    }//GEN-LAST:event_txtEmailInstructorFocusGained

    private void btnGuardarInstructorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarInstructorMouseClicked
        txtCelularInstructor.requestFocus();
    }//GEN-LAST:event_btnGuardarInstructorMouseClicked

    private void btnGuardarInstructorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarInstructorMouseEntered
        btnCancelarInstructor.requestFocus();
    }//GEN-LAST:event_btnGuardarInstructorMouseEntered

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
            java.util.logging.Logger.getLogger(Instructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Instructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Instructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Instructores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Instructores().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarInstructor;
    private javax.swing.JButton btnCancelarInstructor;
    private javax.swing.JButton btnGuardarInstructor;
    private javax.swing.JButton btnNuevoInstructor;
    private javax.swing.JButton btnSalirInstructor;
    private javax.swing.JComboBox cboSexoInstructor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JMenuItem mnModificar;
    private javax.swing.JPanel pnRegistroInstructor;
    private javax.swing.JTable tblConsultaInstructor;
    private javax.swing.JTextField txtApellido1Instructor;
    private javax.swing.JTextField txtApellido2Instructor;
    private javax.swing.JTextField txtBuscarInstructor;
    private javax.swing.JTextField txtCedulaInstructor;
    private javax.swing.JTextField txtCelularInstructor;
    private javax.swing.JTextField txtDireccionInstructor;
    private javax.swing.JTextField txtEmailInstructor;
    private javax.swing.JTextField txtNombre1Instructor;
    private javax.swing.JTextField txtNombre2Instructor;
    private javax.swing.JTextField txtTelefonoInstructor;
    // End of variables declaration//GEN-END:variables
}
