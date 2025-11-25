package tp3sockets;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;

//Clase para la selección de archivos
public class FileUtils {
    
    public static File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo para enviar");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    
 
    public static boolean askForContinue() {
        int option = JOptionPane.showConfirmDialog(
            null, 
            "¿Desea enviar otro archivo?", 
            "Continuar transmisión", 
            JOptionPane.YES_NO_OPTION
        );
        
        return option == JOptionPane.YES_OPTION;
    }
    

    public static String getFileName(String filePath) {
        return new File(filePath).getName();
    }
    

    public static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}