
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class App 
{
  public static void main(String[] args) 
  {

    //Lee quere archivos hay en la carpeta contenedora
    String sCarpAct = System.getProperty("user.dir");
    String[] filenames = new java.io.File(sCarpAct).list();

    //A cada archivo le hace su respectiva operacion
    for(String filename : filenames)
    {      
      if(filename.contains(".sql"))
      {
        System.out.println("archivo : "+filename);
        ejecutarArchivoSQL(filename);
      }
    
    }


    System.out.println("Hello, World!");
  }

  public static void ejecutarArchivoSQL(String fileName)
  {
      File archivo = null;
      FileReader fr = null;
      FileInputStream in;
      BufferedReader br = null;


      try 
      {  
         archivo = new File (fileName);
         fr = new FileReader (archivo);
         in = new FileInputStream(archivo);
         br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

         // Lectura del fichero
         String contenido = "";
         String linea;
         while((linea=br.readLine())!=null)
         {
            contenido += linea+"\n";
         }
         
         conexionBD.EjecutarQuery(contenido);
         conexionBD.CerrarconexionBD();
      }
      
      catch(Exception e){
         e.printStackTrace();
      }finally
      {
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
         
      }
  }
}
