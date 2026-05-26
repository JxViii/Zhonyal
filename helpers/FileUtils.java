package helpers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JFileChooser;

public class FileUtils {
  
  public static void selectFile( Runnable onRefresh ){

    try{

      JFileChooser fileChooser = new JFileChooser();

      int res = fileChooser.showSaveDialog(null); // Select file to open

      if( res == JFileChooser.APPROVE_OPTION ){

        // Create the files dir if it doesnt exist
        // The idea is to add them to .zhonyal in case the user moves the app or something like that

        File filesDir = new File(
          System.getProperty("user.home") + "/.zhonyal/files/"
        );
        filesDir.mkdirs();

        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

        if( !file.getName().endsWith("pdf") ) {
          System.out.println("We currently only support .pdf files");
          return;
        }

        // Copy file to filesDir

        File dest = new File( filesDir + "/" + file.getName() );
        Files.copy( file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING );

        printDir(filesDir);

        onRefresh.run();

      }


    }catch( Exception e){
      e.printStackTrace();
    }

  }

  public static File[] loadFiles( String path ){

    File filePath = new File( path );
    return filePath.listFiles();

  }

  public static void printDir( File dir ) {

    if (dir.listFiles().length == 0) System.out.println( dir + " is empty");

    for ( File s : dir.listFiles() ){

      System.out.println(s.getName() + " : " + formatSize(s.length()));

    }


  }

  public static void deleteAllFiles() {
    File dir = new File(System.getProperty("user.home") + "/.zhonyal/files");
    File[] files = dir.listFiles();
    if (files != null) {
      for (File f : files) f.delete();
    }
  }

  public static String formatSize(long bytes) {
      if (bytes < 1024) return bytes + " B";
      if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
      return String.format("%.1f MB", bytes / (1024.0 * 1024));
  }

// file.length() → "41.1 KB"
}
