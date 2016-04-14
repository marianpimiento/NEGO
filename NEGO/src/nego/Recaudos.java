
package nego;

import BaseDatos.ConexionPostgresql;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;
import org.jfree.chart.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Recaudos extends javax.swing.JFrame {
    
    //Definir un Table Model
    DefaultTableModel modelo;
    
    //Conexion con la base de datos
    ConexionPostgresql postgresql = new ConexionPostgresql();
    Connection cn = postgresql.Conectar();
    
    public Recaudos() {
        initComponents();
        CargarTablaRecaudos("","","","");
        inhabilitarRecaudos();
        
        //Calcula ganancias por inscripciones.
        String[] total = new String[3];
        total = TotalRecaudado("","","","");
        txtTotalRecaudos.setText("$ "+total[0]+"=");
        
        this.Pastel.setVisible(true);
        capas.setLayer(Pastel, 0,0);
    }
    
    void LimpiarRecaudos() {
        
        //Dejar vacios todos los campos de captura de datos
        txtFechaInicioRecaudos.setText("");
        txtFechaFinRecaudos.setText("");
        txtCodigoServicioRecaudos.setText("");
        txtCedulaClienteRecaudos.setText("");
        txtNombreClienteRecaudos.setText("");
        
    }
    void inhabilitarRecaudos() {
        
         //Inhabilitar todos los campos.
        txtNombreClienteRecaudos.setEnabled(false);
        txtTotalRecaudos.setEnabled(false);
                
    }
    
    void CargarTablaRecaudos(String fini, String ffin, String codservicio, String cedcliente)
    {
        //Titulos para el Table Model
        String[] titulos = {"Cod. Servicio","Servicio", "Tipo", "Cedula", "Nombre", "Sexo",  "Fecha de Pago", "Valor Inscripción"};
        //Arreglo para introducir los datos leidos de la BD
        String[] registro = new String[8];
        String sSQL = "";
        
        //Crear un Table Model
        modelo = new DefaultTableModel(null, titulos);
          
        //SQL : Leer datos de la tabla Instructores, cuando "valor" sea igual o este en la contenación de los datos del instructor
        
        if(!fini.equals("") && !ffin.equals("")){
            sSQL = "SELECT * FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND fechainscripcion >='"+fini+"' AND  fechainscripcion <='"+ffin+"'" 
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio "
               + " ORDER BY fechainscripcion";
        }
        else  if(!ffin.equals("")){
            sSQL = "SELECT * FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%'  AND  fechainscripcion <='"+ffin+"'" 
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio "
               + " ORDER BY fechainscripcion";
        }
        else if(!fini.equals("")){
            sSQL = "SELECT * FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND fechainscripcion >='"+fini+"' " 
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio "
               + " ORDER BY fechainscripcion";
        }
        else{
            sSQL = "SELECT * FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%'"
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio "
               + " ORDER BY fechainscripcion";
        }
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            String nombreCompleto = "";
            
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                registro[0] = rs.getString("codservicio");
                registro[1] = rs.getString("descripcion");
                registro[2] = rs.getString("tiposervicio");
                registro[3] = rs.getString("cedula");
                    nombreCompleto = rs.getString("nombre1").trim()+' '+rs.getString("apellido1").trim();
                registro[4] = nombreCompleto;
                registro[5] = rs.getString("sexo");
                registro[6] = rs.getString("fechainscripcion");
                registro[7] = rs.getString("valor");
                
                //Agregar "registro" al modelo (La tabla)
                modelo.addRow(registro);
            }
            
            //Mostrar el moledo en la Tabla de Consulta
            tblConsultaRecaudos.setModel(modelo);
            
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        } 
    }
    
    String[] TotalRecaudado(String fini, String ffin, String codservicio, String cedcliente)
    {
        //Arreglo para introducir los datos leidos de la BD
        String sSQL = "";
        String[] total = new String[3]; 
        String[] total2 = new String[3];
        
        //Calcula la suma de los campos inscripciones.valor.
        //SQL : Leer datos de la tabla Instructores, cuando "valor" sea igual o este en la contenación de los datos del instructor
        if(!fini.equals("") && !ffin.equals("")){
            sSQL = "SELECT SUM(inscripciones.valor) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' AND fechainscripcion >='"+fini+"' AND fechainscripcion <='"+ffin+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";
            total2[1] = "SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND fechainscripcion >='"+fini+"' AND fechainscripcion <='"+ffin+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";
            total2[2] = "SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND fechainscripcion >='"+fini+"' AND fechainscripcion <='"+ffin+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio "
               + " AND sexo = 'M'";
        }
        else  if(!ffin.equals("")){
            sSQL = "SELECT SUM(inscripciones.valor) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND  fechainscripcion <='"+ffin+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";
            total2[1] = "SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND  fechainscripcion <='"+ffin+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";
            total2[2] = "SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND  fechainscripcion <='"+ffin+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio AND sexo = 'M'";
        }
        else if(!fini.equals("")){
            sSQL = "SELECT SUM(inscripciones.valor) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND fechainscripcion >='"+fini+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";
            total2[1] ="SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND fechainscripcion >='"+fini+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";;
            total2[2] ="SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND fechainscripcion >='"+fini+"' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio AND sexo = 'M'";
        }
        else{
            sSQL = "SELECT SUM(inscripciones.valor) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";
            total2[1] ="SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio ";;
            total2[2] ="SELECT COUNT(*) FROM clientes,inscripciones,servicios "
               + " WHERE cedula LIKE '%"+cedcliente+"%' AND servicios.codservicio LIKE '%"+codservicio+"%' "
               + " AND cedula=cedcliente AND inscripciones.codservicio=servicios.codservicio AND sexo = 'M'";
            
        }
        
        try 
        {
            //Usar la conexion con la BD y crear un Result set
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while(rs.next())
            {
                //Guadar en el arreglo los datos de determinado registro
                total[0] = rs.getString("SUM");
            }
            rs = st.executeQuery(total2[1]);
            while(rs.next()){
                total[1] = rs.getString("COUNT");
            }
            rs = st.executeQuery(total2[2]);
            while(rs.next()){
                total[2] = rs.getString("COUNT");
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
        txtFechaInicioRecaudos = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFechaFinRecaudos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodigoServicioRecaudos = new javax.swing.JTextField();
        btnServiciosRecaudos = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtCedulaClienteRecaudos = new javax.swing.JTextField();
        btnClientesRecaudos = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtNombreClienteRecaudos = new javax.swing.JTextField();
        btnCalcularRecaudos = new javax.swing.JButton();
        btnSalirRecaudos = new javax.swing.JButton();
        btnNuevoRecaudos = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsultaRecaudos = new javax.swing.JTable();
        txtTotalRecaudos = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        capas = new javax.swing.JLayeredPane();
        Pastel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1290, 762));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Recaudo Por Inscripciones"));

        jLabel1.setText("Fecha Inicio:");

        txtFechaInicioRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaInicioRecaudosActionPerformed(evt);
            }
        });
        txtFechaInicioRecaudos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFechaInicioRecaudosFocusLost(evt);
            }
        });

        jLabel2.setText("Fecha Fin:");

        txtFechaFinRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaFinRecaudosActionPerformed(evt);
            }
        });
        txtFechaFinRecaudos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFechaFinRecaudosFocusLost(evt);
            }
        });

        jLabel3.setText("Código Del Servicio:");

        txtCodigoServicioRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoServicioRecaudosActionPerformed(evt);
            }
        });
        txtCodigoServicioRecaudos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoServicioRecaudosFocusLost(evt);
            }
        });

        btnServiciosRecaudos.setBackground(new java.awt.Color(255, 255, 255));
        btnServiciosRecaudos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnServiciosRecaudos.setText("Ver Servicios");
        btnServiciosRecaudos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnServiciosRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiciosRecaudosActionPerformed(evt);
            }
        });

        jLabel4.setText("Cédula Del Cliente:");

        txtCedulaClienteRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaClienteRecaudosActionPerformed(evt);
            }
        });
        txtCedulaClienteRecaudos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaClienteRecaudosFocusLost(evt);
            }
        });

        btnClientesRecaudos.setBackground(new java.awt.Color(255, 255, 255));
        btnClientesRecaudos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Ver.jpg"))); // NOI18N
        btnClientesRecaudos.setText("Ver Clientes");
        btnClientesRecaudos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnClientesRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesRecaudosActionPerformed(evt);
            }
        });

        jLabel5.setText("Nombre Del Cliente:");

        txtNombreClienteRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteRecaudosActionPerformed(evt);
            }
        });

        btnCalcularRecaudos.setBackground(new java.awt.Color(255, 255, 255));
        btnCalcularRecaudos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/Calcular.jpg"))); // NOI18N
        btnCalcularRecaudos.setText("Calcular");
        btnCalcularRecaudos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCalcularRecaudos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCalcularRecaudosMouseEntered(evt);
            }
        });
        btnCalcularRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularRecaudosActionPerformed(evt);
            }
        });

        btnSalirRecaudos.setBackground(new java.awt.Color(255, 255, 255));
        btnSalirRecaudos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/x2-25.jpg"))); // NOI18N
        btnSalirRecaudos.setText("Salir");
        btnSalirRecaudos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSalirRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirRecaudosActionPerformed(evt);
            }
        });

        btnNuevoRecaudos.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevoRecaudos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nego/check1-25.jpg"))); // NOI18N
        btnNuevoRecaudos.setText("Nuevo");
        btnNuevoRecaudos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNuevoRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoRecaudosActionPerformed(evt);
            }
        });

        tblConsultaRecaudos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblConsultaRecaudos);

        txtTotalRecaudos.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtTotalRecaudos.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalRecaudos.setDisabledTextColor(new java.awt.Color(255, 51, 51));
        txtTotalRecaudos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalRecaudosActionPerformed(evt);
            }
        });

        jLabel7.setText("Formato: aaaa-mm-dd");

        jLabel6.setBackground(new java.awt.Color(0, 102, 102));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 51, 51));
        jLabel6.setText("TOTAL:   ");

        capas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout PastelLayout = new javax.swing.GroupLayout(Pastel);
        Pastel.setLayout(PastelLayout);
        PastelLayout.setHorizontalGroup(
            PastelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );
        PastelLayout.setVerticalGroup(
            PastelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        capas.add(Pastel);
        Pastel.setBounds(10, 10, 430, 280);

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
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFechaInicioRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoServicioRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCedulaClienteRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnClientesRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnServiciosRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(btnCalcularRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jLabel7)
                                .addGap(67, 67, 67))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFechaFinRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreClienteRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(btnSalirRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTotalRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(capas, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtFechaInicioRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtFechaFinRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoServicioRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(btnServiciosRecaudos))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCedulaClienteRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(btnClientesRecaudos)
                            .addComponent(jLabel5)
                            .addComponent(txtNombreClienteRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCalcularRecaudos)
                            .addComponent(btnNuevoRecaudos)
                            .addComponent(btnSalirRecaudos))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalRecaudos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addComponent(capas, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                .addContainerGap())
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
                .addContainerGap(898, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreClienteRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteRecaudosActionPerformed

    }//GEN-LAST:event_txtNombreClienteRecaudosActionPerformed

    private void txtFechaInicioRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaInicioRecaudosActionPerformed
        txtFechaInicioRecaudos.transferFocus();
    }//GEN-LAST:event_txtFechaInicioRecaudosActionPerformed

    private void txtFechaFinRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaFinRecaudosActionPerformed
        txtFechaFinRecaudos.transferFocus();
    }//GEN-LAST:event_txtFechaFinRecaudosActionPerformed

    private void txtCodigoServicioRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoServicioRecaudosActionPerformed
        txtCodigoServicioRecaudos.transferFocus();
    }//GEN-LAST:event_txtCodigoServicioRecaudosActionPerformed

    private void txtCedulaClienteRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaClienteRecaudosActionPerformed
        txtCedulaClienteRecaudos.transferFocus();
    }//GEN-LAST:event_txtCedulaClienteRecaudosActionPerformed

    private void btnServiciosRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiciosRecaudosActionPerformed
        new Servicios().setVisible(true);
    }//GEN-LAST:event_btnServiciosRecaudosActionPerformed

    private void btnClientesRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesRecaudosActionPerformed
        new Clientes().setVisible(true);
    }//GEN-LAST:event_btnClientesRecaudosActionPerformed

    private void btnNuevoRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoRecaudosActionPerformed
        //Boton Nuevo : llama el método habilitar
        LimpiarRecaudos();
    }//GEN-LAST:event_btnNuevoRecaudosActionPerformed

    private void btnSalirRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirRecaudosActionPerformed
        //Botón Salir : Cierra la aplicación
        this.dispose();
    }//GEN-LAST:event_btnSalirRecaudosActionPerformed

    private void btnCalcularRecaudosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalcularRecaudosMouseEntered
        btnNuevoRecaudos.requestFocus();
    }//GEN-LAST:event_btnCalcularRecaudosMouseEntered

    private void btnCalcularRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularRecaudosActionPerformed
        //Calcula ganancias por inscripciones.
        String[] total;
        Pastel.removeAll();
        total = TotalRecaudado(txtFechaInicioRecaudos.getText(),txtFechaFinRecaudos.getText(),txtCodigoServicioRecaudos.getText(),txtCedulaClienteRecaudos.getText());
        CargarTablaRecaudos(txtFechaInicioRecaudos.getText(),txtFechaFinRecaudos.getText(),txtCodigoServicioRecaudos.getText(),txtCedulaClienteRecaudos.getText());
        txtTotalRecaudos.setText("$ "+total[0]+"=");
        
        //Graficar torta
        
        float M,H,T;
        T=Float.parseFloat(total[1]);
        H=Float.parseFloat(total[2]);
        M=T-H;
        H=H*100/T;
        M=M*100/T;
        ChartPanel panel;
        JFreeChart chart = null;
        
        DefaultPieDataset data = new DefaultPieDataset();
        data.setValue("Mujeres "+M+"%", M);
        data.setValue("Hombres "+H+"%", H);
        
        chart = ChartFactory.createPieChart3D("Gráfico Hombres y Mujeres",
                data, true, true, true);
        
        panel = new ChartPanel(chart);
        panel.setBounds(5, 5, 410, 300);
        
        Pastel.add(panel);
        Pastel.repaint();
    }//GEN-LAST:event_btnCalcularRecaudosActionPerformed

    private void txtTotalRecaudosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalRecaudosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalRecaudosActionPerformed

    private void txtFechaFinRecaudosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaFinRecaudosFocusLost
        // *** Validar formato de fecha
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String ffin = txtFechaFinRecaudos.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(ffin);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaFinRecaudos.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaFinRecaudos.setText("");
                txtFechaFinRecaudos.requestFocus();
                ver = 1;
            }
        }
        catch (ParseException ex)
        {
            if(!txtFechaFinRecaudos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Fin No Válido.");
                txtFechaFinRecaudos.setText("");
                txtFechaFinRecaudos.requestFocus();
                ver = 1;
            }
        }
        
        String fin=txtFechaFinRecaudos.getText();
	int mayor = fin.compareTo( txtFechaInicioRecaudos.getText() );
        
        if(ver == 0 && mayor < 0 && !txtFechaInicioRecaudos.getText().equals("") && !txtFechaFinRecaudos.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Fin es Anterior a la Fecha Inicio.");
            txtFechaFinRecaudos.setText("");
        }
    }//GEN-LAST:event_txtFechaFinRecaudosFocusLost

    private void txtFechaInicioRecaudosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaInicioRecaudosFocusLost
        //Valisar Fecha Inicio
        int ver = 0;
        
        //Guadar en las variables los datos registrados en los jTextField
        String finicio = txtFechaInicioRecaudos.getText();
                
        //Convertir la fecha de String a Calendar 
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date dateObj = null;
        
        try {
            dateObj = curFormater.parse(finicio);
            
            //Valida que la fecha esté en el formato.
            Pattern pat = Pattern.compile("([1,2][0-9][0-9][0-9])-(([0][1-9])|([1][0-2]))-(([0][1-9])|([1,2][0-9])|([3][0,1]))|");
            Matcher mat = pat.matcher( txtFechaInicioRecaudos.getText()); 
            if(!mat.matches()){
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioRecaudos.setText("");
                txtFechaInicioRecaudos.requestFocus();
                ver = 1;
            }
        } 
        catch (ParseException ex)
        {
            ver = 1;
            if(!txtFechaInicioRecaudos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Formato de Fecha Inicio No Válido.");
                txtFechaInicioRecaudos.setText("");
                txtFechaInicioRecaudos.requestFocus();
            }
        }
        
        String fin=txtFechaInicioRecaudos.getText();
	int mayor = fin.compareTo( txtFechaFinRecaudos.getText() );
        
        if(ver == 0 && mayor > 0  && !txtFechaInicioRecaudos.getText().equals("") && !txtFechaFinRecaudos.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"La Fecha Inicio es Posterior a la Fecha Fin.");
            txtFechaInicioRecaudos.setText("");
        }
    }//GEN-LAST:event_txtFechaInicioRecaudosFocusLost

    private void txtCodigoServicioRecaudosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoServicioRecaudosFocusLost
        //Accion cuando pierde el foco
        String sSQL = "";
        
        //SQL : Validar el servicio
        sSQL = "SELECT * FROM servicios "
               + "WHERE codservicio = '"+txtCodigoServicioRecaudos.getText()+"'";
        
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
            if(recordCount == 0 && !txtCodigoServicioRecaudos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Servicio No existe.");
                txtCodigoServicioRecaudos.setText("");
                txtCodigoServicioRecaudos.requestFocus();
            }  
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCodigoServicioRecaudosFocusLost

    private void txtCedulaClienteRecaudosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaClienteRecaudosFocusLost
        //Accion cuando pierde el foco
        String sSQL = "", nombre1 = "", apellido1 = "",  nombrecompleto = "";
        
        //SQL : Validar el cliente y nombre del cliente
        sSQL = "SELECT nombre1, apellido1 FROM clientes "
               + "WHERE cedula = '"+txtCedulaClienteRecaudos.getText()+"'";
        
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
            if(recordCount != 1 && !txtCedulaClienteRecaudos.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "El Cliente No Existe.");
                txtCedulaClienteRecaudos.setText("");
                txtCedulaClienteRecaudos.requestFocus();
            }
            else
            {
                //.trim() quita todos los espacios en blanco generados por almacenamiento en la BD.                
                nombrecompleto = nombre1.trim()+" "+apellido1.trim();
                
                txtNombreClienteRecaudos.setText(nombrecompleto);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_txtCedulaClienteRecaudosFocusLost

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
            java.util.logging.Logger.getLogger(Recaudos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Recaudos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Recaudos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Recaudos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Recaudos().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Pastel;
    private javax.swing.JButton btnCalcularRecaudos;
    private javax.swing.JButton btnClientesRecaudos;
    private javax.swing.JButton btnNuevoRecaudos;
    private javax.swing.JButton btnSalirRecaudos;
    private javax.swing.JButton btnServiciosRecaudos;
    private javax.swing.JLayeredPane capas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblConsultaRecaudos;
    private javax.swing.JTextField txtCedulaClienteRecaudos;
    private javax.swing.JTextField txtCodigoServicioRecaudos;
    private javax.swing.JTextField txtFechaFinRecaudos;
    private javax.swing.JTextField txtFechaInicioRecaudos;
    private javax.swing.JTextField txtNombreClienteRecaudos;
    private javax.swing.JTextField txtTotalRecaudos;
    // End of variables declaration//GEN-END:variables
}
