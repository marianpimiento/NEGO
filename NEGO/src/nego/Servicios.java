
package nego;

import BaseDatos.ConexionPostgresql;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Servicios extends javax.swing.JFrame {

    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Servicios() {
        initComponents();
        
        //cboTipoServicio.addItem("Mensualidad");
        //cboTipoServicio.addItem("Personalizado");
        
        CargarTablaServicios("");
        inhabilitarServicio();
    }
    
    void inhabilitarServicio() {
        
        //Inhabilitar todos los campos de captura de datos
        txtCodigoServicio.setEnabled(false);
        txtDescripcionServicio.setEnabled(false);
        cboTipoServicio.setEnabled(false);
        txtValor1Servicio.setEnabled(false);
        txtValorCursoServicio.setEnabled(false);
        txtValorHoraInstServicio.setEnabled(false);
                
        //Dejar vacios todos los campos de captura de datos
        txtCodigoServicio.setText("");
        txtDescripcionServicio.setText("");
        txtValor1Servicio.setText("");
        txtValorCursoServicio.setText("");
        txtValorHoraInstServicio.setText("");
                        
        //Inhabilitar los botones Guardar y Cancelar
        btnGuardarServicio.setEnabled(false);
        btnCancelarServicio.setEnabled(false);
        
    }
    
    void habilitarServicio() {
        
        //Habilitar todos los campos de captura de datos
        txtCodigoServicio.setEnabled(true);
        txtDescripcionServicio.setEnabled(true);
        cboTipoServicio.setEnabled(true);
        txtValor1Servicio.setEnabled(true);
        txtValorCursoServicio.setEnabled(true);
        txtValorHoraInstServicio.setEnabled(true);
                
        //Dejar vacios todos los campos de captura de datos
        txtCodigoServicio.setText("");
        txtDescripcionServicio.setText("");
        cboTipoServicio.setSelectedIndex(0);
        txtValor1Servicio.setText("");
        txtValorCursoServicio.setText("");
        txtValorHoraInstServicio.setText("");
                        
        //Habilitar los botones Guardar y Cancelar
        btnGuardarServicio.setEnabled(true);
        btnCancelarServicio.setEnabled(true);
        
          //Poner el foco en Cedula (Primer dato de la captura)
        txtCodigoServicio.requestFocus();
        
    }
    
    
    void CargarTablaServicios(String valor)
    {
        //Titulos para el Table Model
        String[] titulos = {"Código", "Descripción", "Tipo", "Valor Clase", "Valor Curso", "Valor Hora Instructor"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[6];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
                
        //SQL : Leer datos de la tabla servicios, cuando "valor" sea igual o este en la contenación de los datos del servicio
        sSQL = "SELECT * FROM servicios "
               + "WHERE CONCAT(codservicio, ' ', descripcion, ' ',tiposervicio, ' ',valor1, ' ', valorcurso, ' ', valorhorainst) LIKE '%"+valor+"%' "
               + "ORDER BY codservicio";
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("codservicio");
                registro[1] = rs.getString("descripcion");
                
                if(rs.getString("tiposervicio").trim().equals("M"))
                {
                    registro[2] = "Mensualidad";
                }
                else
                {
                    registro[2] = "Personalizado";
                }
                
                registro[3] = rs.getString("valor1");
                registro[4] = rs.getString("valorcurso");
                registro[5] = rs.getString("valorhorainst");
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el moledo en la Tabla de Consulta
            tblConsultaServicio.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }        
    
    
    }
            
    //Cod como variable global, para usar en diferentes metodos
    String cod_actualizar = "";
    
    void BuscarServiciosModificar(String cod)
    {
        String sSQL = "";
        String des = "", tipo = "", v1 = "", vcurso = "", vinst = "";
        
        //SQL : Leer datos de la tabla servicioc, cuando al cod corresponda al servicio que se esta buscando
        sSQL = "SELECT codservicio, descripcion, tiposervicio, valor1, valorcurso, valorhorainst FROM servicios "
               + "WHERE codservicio = '"+cod+"'";
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next())
            {
                //Guadar en cada variable los datos del registro encontrado
                //cod = rs.getString("codservicio");
                des = rs.getString("descripcion");
                tipo = rs.getString("tiposervicio");
                v1 = rs.getString("valor1");
                vcurso = rs.getString("valorcurso");
                vinst = rs.getString("valorhorainst");
            }
            
            //Asignar a los jTextField los datos leidos de la BD
            txtCodigoServicio.setText(cod.trim());
            txtDescripcionServicio.setText(des.trim());
            
            if(tipo.equals("M"))
            {
                cboTipoServicio.setSelectedIndex(0);
                txtValor1Servicio.setText(v1.trim());
            } 
            else if (tipo.equals("P"))
            {
                cboTipoServicio.setSelectedIndex(1);
            }
            
            txtValorCursoServicio.setText(vcurso.trim());
            txtValorHoraInstServicio.setText(vinst.trim());
            
            //Hacer la cod_actualziar (global), igual a la cod del registro buscado
            cod_actualizar = cod;
            
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
        jPanel2 = new javax.swing.JPanel();
        pnRegistroCliente1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtCodigoServicio = new javax.swing.JTextField();
        txtDescripcionServicio = new javax.swing.JTextField();
        txtValor1Servicio = new javax.swing.JTextField();
        txtValorCursoServicio = new javax.swing.JTextField();
        btnNuevoServicio = new javax.swing.JButton();
        btnGuardarServicio = new javax.swing.JButton();
        btnCancelarServicio = new javax.swing.JButton();
        btnSalirServicio = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtValorHoraInstServicio = new javax.swing.JTextField();
        cboTipoServicio = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        lbAterisco = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblConsultaServicio = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        txtBuscarServicio = new javax.swing.JTextField();
        btnBuscarServicio = new javax.swing.JButton();

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

        jPanel2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jPanel2AncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        pnRegistroCliente1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Servicio"));
        pnRegistroCliente1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                pnRegistroCliente1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel21.setText("* Código del Servicio:");

        jLabel22.setText("* Descripción:");

        jLabel23.setText("* Tipo del Servicio:");

        jLabel24.setText("Valor Clase (1):");

        jLabel25.setText("* Valor Curso:");

        txtCodigoServicio.setToolTipText("3 Dígitos");
        txtCodigoServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoServicioActionPerformed(evt);
            }
        });
        txtCodigoServicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodigoServicioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoServicioFocusLost(evt);
            }
        });

        txtDescripcionServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionServicioActionPerformed(evt);
            }
        });

        txtValor1Servicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValor1ServicioActionPerformed(evt);
            }
        });
        txtValor1Servicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValor1ServicioFocusLost(evt);
            }
        });

        txtValorCursoServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorCursoServicioActionPerformed(evt);
            }
        });
        txtValorCursoServicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorCursoServicioFocusLost(evt);
            }
        });

        btnNuevoServicio.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoServicio.setText("Nuevo");
        btnNuevoServicio.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoServicioActionPerformed(evt);
            }
        });

        btnGuardarServicio.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardarServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/guardar11-25.jpg"))); // NOI18N
        btnGuardarServicio.setText("Guardar");
        btnGuardarServicio.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardarServicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarServicioMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnGuardarServicioMousePressed(evt);
            }
        });
        btnGuardarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarServicioActionPerformed(evt);
            }
        });

        btnCancelarServicio.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelarServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x1-25.jpg"))); // NOI18N
        btnCancelarServicio.setText("Cancelar");
        btnCancelarServicio.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCancelarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarServicioActionPerformed(evt);
            }
        });

        btnSalirServicio.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirServicio.setText("Salir");
        btnSalirServicio.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalirServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirServicioActionPerformed(evt);
            }
        });

        jLabel1.setText("* Valor Clase Instructor:");

        txtValorHoraInstServicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorHoraInstServicioFocusLost(evt);
            }
        });

        cboTipoServicio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mensualidad", "Personalizado" }));
        cboTipoServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTipoServicioActionPerformed(evt);
            }
        });

        jLabel2.setText("* Campos Obligatorios");

        lbAterisco.setText("*");

        javax.swing.GroupLayout pnRegistroCliente1Layout = new javax.swing.GroupLayout(pnRegistroCliente1);
        pnRegistroCliente1.setLayout(pnRegistroCliente1Layout);
        pnRegistroCliente1Layout.setHorizontalGroup(
            pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroCliente1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente1Layout.createSequentialGroup()
                        .addComponent(lbAterisco, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24))
                    .addComponent(jLabel23)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistroCliente1Layout.createSequentialGroup()
                        .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cboTipoServicio, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtValor1Servicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addComponent(txtValorCursoServicio, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(pnRegistroCliente1Layout.createSequentialGroup()
                                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtValorHoraInstServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnRegistroCliente1Layout.createSequentialGroup()
                                        .addComponent(btnNuevoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addComponent(btnGuardarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(39, 39, 39)
                                        .addComponent(btnCancelarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(27, 27, 27)
                                .addComponent(btnSalirServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDescripcionServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addGroup(pnRegistroCliente1Layout.createSequentialGroup()
                        .addComponent(txtCodigoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnRegistroCliente1Layout.setVerticalGroup(
            pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistroCliente1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtDescripcionServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cboTipoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtValor1Servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAterisco))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtValorCursoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtValorHoraInstServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnRegistroCliente1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevoServicio)
                    .addComponent(btnGuardarServicio)
                    .addComponent(btnCancelarServicio)
                    .addComponent(btnSalirServicio))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRegistroCliente1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Servicios"));

        tblConsultaServicio.setModel(new javax.swing.table.DefaultTableModel(
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
        tblConsultaServicio.setComponentPopupMenu(jPopupMenu1);
        tblConsultaServicio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane3.setViewportView(tblConsultaServicio);

        jLabel30.setText("Buscar:");

        btnBuscarServicio.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/buscar1-25.jpg"))); // NOI18N
        btnBuscarServicio.setText("Buscar");
        btnBuscarServicio.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBuscarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarServicioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel30)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarServicio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnRegistroCliente1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnRegistroCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoServicioActionPerformed
        txtCodigoServicio.transferFocus();
    }//GEN-LAST:event_txtCodigoServicioActionPerformed

    private void txtValor1ServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValor1ServicioActionPerformed
        txtValor1Servicio.transferFocus();
    }//GEN-LAST:event_txtValor1ServicioActionPerformed

    private void btnSalirServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirServicioActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirServicioActionPerformed

    private void txtDescripcionServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionServicioActionPerformed
        txtDescripcionServicio.transferFocus();
    }//GEN-LAST:event_txtDescripcionServicioActionPerformed

    private void txtValorCursoServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorCursoServicioActionPerformed
        txtValorCursoServicio.transferFocus();
    }//GEN-LAST:event_txtValorCursoServicioActionPerformed

    private void btnNuevoServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoServicioActionPerformed
        habilitarServicio();
        
        //Accion en Insertar para "guardar" y validar
        accion  = "Insertar";
        
        //Toma por defecto el primer item del combobox
        cboTipoServicio.setSelectedIndex(0);
        
        //Inhabilita nuevo y habilita cancelar
        btnNuevoServicio.setEnabled(false);
        btnCancelarServicio.setEnabled(true);
    }//GEN-LAST:event_btnNuevoServicioActionPerformed

    private void btnCancelarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarServicioActionPerformed
        inhabilitarServicio();
        
        //Habilita nuevo e inhabilita cancelar
        btnNuevoServicio.setEnabled(true);
        btnCancelarServicio.setEnabled(false);
    }//GEN-LAST:event_btnCancelarServicioActionPerformed

    String accion  = "Insertar"; 
    String service;
    private void btnGuardarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarServicioActionPerformed
        
        btnNuevoServicio.setEnabled(true);
        
        String cod, des, tipo;
        int v1, vcurso, vinst, ver = 0;
        String sSQL = "", mensaje = "";
        
        
        
        if( !txtCodigoServicio.getText().equals("") && !txtDescripcionServicio.getText().equals("") && !txtValorCursoServicio.getText().equals("") && !txtValorHoraInstServicio.getText().equals(""))
        {
            if(cboTipoServicio.getSelectedItem().toString().equals("Mensualidad") && !txtValor1Servicio.getText().equals(""))
            {
                ver = 1;
            }
            if(cboTipoServicio.getSelectedItem().toString().equals("Personalizado"))
            {
                ver = 1;
            }
        }
        
        if( ver == 1)
        {
            //Guadar en las variables los datos registrados en los jTextField
            cod = txtCodigoServicio.getText();
            des = txtDescripcionServicio.getText();
            tipo = cboTipoServicio.getSelectedItem().toString();
            vcurso = Integer.parseInt(txtValorCursoServicio.getText()); 
            vinst = Integer.parseInt(txtValorHoraInstServicio.getText());
        
            if(tipo.equals("Mensualidad"))
            {
                v1 = Integer.parseInt(txtValor1Servicio.getText());
                
                if(accion.equals("Insertar"))
                {
                    //SQL : Insertar en la BD en la tabla servicios los '?'
                    sSQL = "INSERT INTO servicios (codservicio, descripcion, tiposervicio, valor1, valorcurso, valorhorainst)"
                            + "VALUES(?, ?, ?, ?, ?, ?)";
                    
                    mensaje = "Los datos se han Insertado";
                }
                else if(accion.equals("Modificar"))
                {
                    //SQL : Actualizar en la BD en la tabla servicios los '?', para el cod_actualizar
                    sSQL = "UPDATE servicios "
                            + "SET codservicio = ?,"
                            + " descripcion = ?,"
                            + " tiposervicio = ?,"
                            + " valor1 = ?,"
                            + " valorcurso = ?,"
                            + " valorhorainst = ? "
                            + "WHERE codservicio = '"+cod_actualizar+"'";
                
                    mensaje = "Los datos se han Modificado";
                }
            
                try
                {
                    //Usar la conexion con la BD, con un Prepared Statement
                    PreparedStatement pst = cn.prepareStatement(sSQL);
                
                    //Con pst asignar los valores de las variables, según el número de interrogante que corresponda.
                    pst.setString(1, cod);
                    pst.setString(2, des);
                    pst.setString(3, "M");
                    pst.setInt(4, v1);
                    pst.setInt(5, vcurso);
                    pst.setInt(6, vinst);
                
                    //Inhabilitar (Método)
                    inhabilitarServicio();
                
                    //Actualizar la BD
                    int n = pst.executeUpdate();
                
                    //Verificar que se haya actualizado la BD
                    if (n > 0)
                    {
                        JOptionPane.showMessageDialog(null, mensaje);
                        CargarTablaServicios("");
                    }
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
            
            else 
            {
                if(accion.equals("Insertar"))
                {
                    sSQL = "INSERT INTO servicios (codservicio, descripcion, tiposervicio, valorcurso, valorhorainst)"
                            + "VALUES(?, ?, ?, ?, ?)";
                    
                    mensaje = "Los datos se han Insertado";
                }
                else if(accion.equals("Modificar"))
                {
                    sSQL = "UPDATE servicios "
                            + "SET codservicio = ?,"
                            + " descripcion = ?,"
                            + " tiposervicio = ?,"
                            + " valor1 = 0,"
                            + " valorcurso = ?,"
                            + " valorhorainst = ? "
                            + "WHERE codservicio = '"+cod_actualizar+"'";
                    
                    mensaje = "Los datos se han Modificado";
                }
                
                try
                {
                    PreparedStatement pst = cn.prepareStatement(sSQL);
                    pst.setString(1, cod);   
                    pst.setString(2, des);
                    pst.setString(3, "P");
                    pst.setInt(4, vcurso);
                    pst.setInt(5, vinst);
                    
                    inhabilitarServicio();
                    
                    int n = pst.executeUpdate();
                    
                    if (n > 0)
                    {
                        JOptionPane.showMessageDialog(null, mensaje);
                        CargarTablaServicios("");
                    }
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Por favor llene todos los Campos Obligatorios!");
        }
        
    }//GEN-LAST:event_btnGuardarServicioActionPerformed

    private void btnBuscarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarServicioActionPerformed
        String valor = txtBuscarServicio.getText();
        CargarTablaServicios(valor);
    }//GEN-LAST:event_btnBuscarServicioActionPerformed

    private void mnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarActionPerformed
        //Menu desplegable "Modificar"
        
        int filasel;
        String cod;
        
        try
        {
            //Tomar el número de la fila (resgistro) seleccionada
            filasel = tblConsultaServicio.getSelectedRow();
            
            //Verificar si se selecciono alguna fila
            if(filasel == -1)
            {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila");
            }
            else
            {
                //Si seleccionó alguna fila, entonces hable la variable accion (global) = "Modificar"
                accion = "Modificar";
                
                //Leer el cod de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaServicio.getModel();
                cod = (String) modelo.getValueAt(filasel, 0);
                
                //Habilitar los campos jTextField y buscar el servicio, según el cod
                habilitarServicio();
                BuscarServiciosModificar(cod);
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
        String cod;
        
        //Tomar el número de la fila (resgistro) seleccionada
        filasel = tblConsultaServicio.getSelectedRow();
        
        //Verificar si se selecciono alguna fila
        if(filasel == -1)
        {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila");
        }
        else
        {
            //Pregunta : ¿Seguro que desea eliminar el servicio? Si o No
            if (JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea eliminar el Servicio?",
                "Eliminar Servicio", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                String sSQL = "";
                
                //Leer el cod de la fila seleccionada de la Tabla
                modelo = (DefaultTableModel) tblConsultaServicio.getModel();
                cod = (String) modelo.getValueAt(filasel, 0);
                
                //SQL : Eliminar en la BD en la tabla servicios, el registro identificado con el codigo cod
                sSQL = "DELETE FROM servicios "
                        + "WHERE codservicio = '"+cod+"'";
                
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
                CargarTablaServicios("");
                
            }
        }
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void cboTipoServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTipoServicioActionPerformed
        String tipo = cboTipoServicio.getSelectedItem().toString();
        
        if (tipo.equals("Personalizado"))
        {
            txtValor1Servicio.setEnabled(false);
            txtValor1Servicio.setText("");
            lbAterisco.setVisible(false);
        } 
        else if (tipo.equals("Mensualidad"))
        {
            txtValor1Servicio.setEnabled(true);
            txtValor1Servicio.setText("");
            lbAterisco.setVisible(true);
        }
    }//GEN-LAST:event_cboTipoServicioActionPerformed

    private void pnRegistroCliente1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_pnRegistroCliente1AncestorAdded
        
    }//GEN-LAST:event_pnRegistroCliente1AncestorAdded

    private void jPanel2AncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanel2AncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2AncestorMoved

    private void txtCodigoServicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoServicioFocusLost
        // TODO add your handling code here://Accion cuando pierde el foco
        //Valida que el valor no contenga letras y al menos un valor.

        String sSQL = "", servicio ="";
        
        //SQL : Validar el servicio y leer el tipo de servicio
        sSQL = "SELECT * FROM servicios "
               + "WHERE codservicio = '"+txtCodigoServicio.getText()+"'";
        
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
                servicio = rs.getString("codservicio");
            }
            
            //Validar si existe el Servicio  
            if(accion.equals("Insertar")){
                if(recordCount != 0)
                {
                    JOptionPane.showMessageDialog(null, "Ya existe el Servicio "+txtCodigoServicio.getText()+".");
                    txtCodigoServicio.setText("");
                    txtCodigoServicio.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("[0-9]{3}|");
                    Matcher mat = pat.matcher(txtCodigoServicio.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Valor No Válido.");
                        txtCodigoServicio.setText("");
                        txtCodigoServicio.requestFocus();
                    }
                }
            }//accion modificar así solo toma en cuenta los demás datos y no el propio
            else{
                if(recordCount != 0  && !service.equals(servicio.trim()))
                {
                    JOptionPane.showMessageDialog(null, "Ya existe el Servicio "+txtCodigoServicio.getText()+".");
                    txtCodigoServicio.setText("");
                    txtCodigoServicio.requestFocus();
                }
                else{
                    Pattern pat = Pattern.compile("[0-9]{3}|");
                    Matcher mat = pat.matcher(txtCodigoServicio.getText());
                    if(!mat.matches()){
                        JOptionPane.showMessageDialog(null, "Valor No Válido.");
                        txtCodigoServicio.setText("");
                        txtCodigoServicio.requestFocus();
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCodigoServicioFocusLost

    private void txtValor1ServicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValor1ServicioFocusLost
        // TODO add your handling code here://Accion cuando pierde el foco
        //Valida que el valor no contenga letras y al menos un valor.
        Pattern pat = Pattern.compile("[0-9]+|");
        Matcher mat = pat.matcher(txtValor1Servicio.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Valor No Válido.");
            txtValor1Servicio.setText("");
            txtValor1Servicio.requestFocus();
        }
    }//GEN-LAST:event_txtValor1ServicioFocusLost

    private void txtValorCursoServicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorCursoServicioFocusLost
        // TODO add your handling code here://Accion cuando pierde el foco
        //Valida que el valor no contenga letras y al menos un valor.
        Pattern pat = Pattern.compile("[0-9]+|");
        Matcher mat = pat.matcher(txtValorCursoServicio.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Valor No Válido.");
            txtValorCursoServicio.setText("");
            txtValorCursoServicio.requestFocus();
        }
    }//GEN-LAST:event_txtValorCursoServicioFocusLost

    private void txtValorHoraInstServicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorHoraInstServicioFocusLost
        // TODO add your handling code here://Accion cuando pierde el foco
        //Valida que el valor no contenga letras y al menos un valor.
        Pattern pat = Pattern.compile("[0-9]+|");
        Matcher mat = pat.matcher(txtValorHoraInstServicio.getText()); 
        if(!mat.matches()){
            JOptionPane.showMessageDialog(null, "Valor No Válido.");
            txtValorHoraInstServicio.setText("");
            txtValorHoraInstServicio.requestFocus();
        }
    }//GEN-LAST:event_txtValorHoraInstServicioFocusLost

    private void txtCodigoServicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoServicioFocusGained
        service=txtCodigoServicio.getText();
    }//GEN-LAST:event_txtCodigoServicioFocusGained

    private void btnGuardarServicioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarServicioMousePressed
        //txtValorHoraInstServicio.requestFocus();
    }//GEN-LAST:event_btnGuardarServicioMousePressed

    private void btnGuardarServicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarServicioMouseEntered
        btnCancelarServicio.requestFocus();
    }//GEN-LAST:event_btnGuardarServicioMouseEntered

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
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Servicios().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarServicio;
    private javax.swing.JButton btnCancelarServicio;
    private javax.swing.JButton btnGuardarServicio;
    private javax.swing.JButton btnNuevoServicio;
    private javax.swing.JButton btnSalirServicio;
    private javax.swing.JComboBox cboTipoServicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbAterisco;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JMenuItem mnModificar;
    private javax.swing.JPanel pnRegistroCliente1;
    private javax.swing.JTable tblConsultaServicio;
    private javax.swing.JTextField txtBuscarServicio;
    private javax.swing.JTextField txtCodigoServicio;
    private javax.swing.JTextField txtDescripcionServicio;
    private javax.swing.JTextField txtValor1Servicio;
    private javax.swing.JTextField txtValorCursoServicio;
    private javax.swing.JTextField txtValorHoraInstServicio;
    // End of variables declaration//GEN-END:variables
}
