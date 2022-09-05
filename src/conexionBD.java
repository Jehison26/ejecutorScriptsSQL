/**
 * Conexion a la BD Punto Virtual
 */


import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class conexionBD {
	private static Connection cn=null;

	/**
	 * Conexion a la base de datos
	 */
	public static Connection conectar() {
		try {
			Properties propiedad = new Properties();
			InputStream entrada = null;

			entrada = new FileInputStream("config.properties");
			propiedad.load(entrada);

			String Server = propiedad.getProperty("IPBD").trim();
			String Puerto = propiedad.getProperty("PuertoBD").trim();
			String DataBaseName = propiedad.getProperty("BaseDatos").trim();
			String Login = propiedad.getProperty("UsuarioBD").trim();
			String Clave = propiedad.getProperty("ClaveBD").trim();
			// System.out.println(Server+"-"+DataBaseName+"-"+Login+"-"+Clave);
			String url = String.format("jdbc:sqlserver://%s\\%s;databaseName=%s;user=%s;password=%s;", Server, Puerto,
					DataBaseName, Login, Clave);
//			System.out.println(url);
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			if (cn == null || cn.isClosed()) {
				cn = DriverManager.getConnection(url);
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return cn;
	}
	
	public Connection getConexion()
	{
		return cn;
	}

//cierra la conexion de la base de datos.
	/**
	 * cierra la conexion a la BD
	 */
	public static void CerrarconexionBD() {
		try {
			conectar().close();
			cn = null;
		} catch (Exception sqle) {
			JOptionPane.showMessageDialog(null, "Error al cerrar la conexion", "Error", JOptionPane.ERROR_MESSAGE);
			Logger.getLogger(conexionBD.class.getName()).log(Level.SEVERE, null, sqle);
		}

	}

//ejecuta los querys
	/**
	 * Ejecuta los querys que se le mandan
	 * @param q Querys de SQL
	 */
	public static void EjecutarQuery(String q) throws SQLException {

		try {
			if (cn == null) {
				conectar();
			}
			Statement st;
			st = cn.createStatement();
			st.executeUpdate(q);
			System.out.println("Query : \n"+q);
			System.out.println("Ejecutado con exito \n");

		} catch (SQLException ex) {
			cn = null;
			System.out.println(q);
			System.out.println("Error al ingresar datos");

		}
	}

// =================================consulta la base de datos de SQL===========================================================================
	/**
	 * consulta la BD a partir de un Query
	 * @param q Query a consultar
	 */
	public DefaultTableModel ConsultarQuery(String q) throws SQLException {

		try {
			if (cn == null) {
				conectar();
			}
			Statement st;
			st = cn.createStatement();
			ResultSet result = st.executeQuery(q);
			return this.ConvertToTableModel(result);

		} catch (SQLException ex) {
			System.out.println(q);
			System.out.println("Error al leer los datos");
			cn.close();
			return null;
		}
	}

// *************************************************************************************************************
	private static DefaultTableModel ConvertToTableModel(ResultSet rs) {
		try {
			
			DefaultTableModel table = new DefaultTableModel();
			ResultSetMetaData metaData = rs.getMetaData();			
			int totalColumn = metaData.getColumnCount();
			Object[] dataRow = new Object[ totalColumn];
			if (rs != null) {
				for (int i = 1; i <= totalColumn; i++) {
					table.addColumn(metaData.getColumnName(i));
				}
				while (rs.next()) {
					for (int i = 1; i <= totalColumn; i++) {
						dataRow[ (i - 1)] = rs.getObject(i);
					}
					table.addRow(dataRow);
				}
			}
			table.getColumnCount();
			return table;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}// fin del programa...
