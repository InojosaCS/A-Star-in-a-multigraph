import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.IOException;
import java.lang.String;
import java.io.*;


public class CambiarTexto
{   
	/**
     * Cambia los nombres de las ciudades en el archivo por el entero que las mapeas
     * @param String filePath: El archivo a a cambiar
     * @param String oldString
     * @param String newString
     */
    static void modificarArchivo(String filePath, String oldString, String newString)
    {
        File fileToBeModified = new File(filePath);         
        String oldContenido = "";         
        BufferedReader reader = null;         
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();
             
            while (line != null) 
            {
                oldContenido = oldContenido + line + System.lineSeparator();       
                line = reader.readLine();
            }
            
            String newContenido = oldContenido.replaceAll(oldString, newString);
             
            writer = new FileWriter(filePath); 
            writer.write(newContenido);
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            { 
                reader.close();          
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Revierte el mapep de las ciudades en el archivo
     * @param String filePath
     * @param String[] ciudades
     */
    static void recuperarArchivo(String filePath, String[] ciudades)
    {
        File fileToBeModified = new File(filePath);         
        String oldContenido = "";         
        BufferedReader reader = null;         
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
			String line = reader.readLine();
            int k = 0;
            int n = 0;
            
            while (line != null) 
            {	
            	if(k==1) {
            		n = Integer.parseInt(line);
            	}
            	if(k>n+2) {
            		String[] auxiliar = line.split("\\s+");
            		line = auxiliar[0] + " " + auxiliar[1] + " " + ciudades[Integer.parseInt(auxiliar[2])] + " " + auxiliar[3];
            	}
                oldContenido = oldContenido + line + System.lineSeparator();
                 
                line = reader.readLine();
                k++;
            }
             
            String newContenido = oldContenido;
            writer = new FileWriter(filePath); 
            writer.write(newContenido);
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            { 
                reader.close();          
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
     
    public static void main(String[] args)
    {   
        //modificarArchivo(args[0], "Digrafo.java", "epale"); 
    }
}