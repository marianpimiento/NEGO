
package BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class ConexionPostgresql {
    
    //public String db = "nego";
    public String url = "jdbc:postgresql://localhost/nego";
    //public String user = "root";
    public String pass = "juanes";
    
    public ConexionPostgresql() {
        //pass=JOptionPane.showInputDialog("Digite la Clave de la BD");
        
    }
    
    public Connection Conectar() {
                
        Connection link = null;
        
        try{
            //Acceso al Driver
            Class.forName("org.postgresql.Driver");
            //La conexión con los parámetros necesarios
            link = DriverManager.getConnection( url,"postgres",pass);
        }
        catch(Exception e)
        {
            //Por si ocurre un error
            JOptionPane.showMessageDialog(null, e);
        }
        return link;        
        
    }

}

/*
import java.sql.*;
public class Main{
public static void main(String[] args){
Variable para almacenar la URL de conexión a nuestra Base de Datos, si esta estuviera en otra máquina, necesitaríamos estar registrados en ella y contar con su IP
String url = "jdbc:postgresql://localhost/moo";
try{
//Acceso al Driver
Class.forName("org.postgresql.Driver");
//La conexión con los parámetros necesarios
Connection con = DriverManager.getConnection( url,"postgres","postgres");
//Abrimos la conexión y la iniciamos
Statement stmt = con.createStatement();
Un ResultSet es como en .NET un DataSet, un arreglo temporal donde se almacenará el resultado de la consulta SQL
ResultSet rs;
//Una variable String para almacenar la sentencia SQL
String query = "select id as ID from moo.usuarios";
//En el ResultSet guardamos el resultado de ejecutar la consulta
rs = stmt.executeQuery(query);
//En un ciclo while recorremos cada fila del resultado de nuestro
Select while ( rs.next()){
Aqui practicamente podemos hacer lo que deseemos con el resultado, en mi caso solo lo mande a imprimir
System.out.println(rs.getString("ID") + "\t" + rs.getString("ID"));
}
//Cerramos la conexión
stmt.execute("END");
stmt.close();
con.close();
}
catch( Exception e ){
//Por si ocurre un error
System.out.println(e.getMessage());
e.printStackTrace();
}
}

*/