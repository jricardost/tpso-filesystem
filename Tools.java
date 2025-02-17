import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

public final class Tools implements Constants {

    /*
        Procura por um arquivo/diretório a partir do diretório atual dado um caminho
        Retorna null se não encontrar
    */
    public static VirtualFileSystem.Inode findInode(String path) {
        String[] splitPath = path.split("/");
        return findInode(splitPath, Application.vfs.currentDirectory);
    }

    private static VirtualFileSystem.Inode findInode(String[] splitPath, VirtualFileSystem.Directory currentDir) {
        List<VirtualFileSystem.Inode> inodes = new ArrayList<>(currentDir.files.values());

        if (splitPath.length == 1) {
            for (VirtualFileSystem.Inode inode : inodes) {
                if (inode.name.equals(splitPath[0])) {
                    return inode;
                }
            }
            return null;
        }

        for (VirtualFileSystem.Inode inode : inodes) {
            if (inode.name.equals(splitPath[0])) {
                String[] newPath = Arrays.copyOfRange(splitPath, 1, splitPath.length);
                return findInode(newPath, (VirtualFileSystem.Directory) inode);
            }
        }
        return null;
    }

    public static void clearScreen(){
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static String timeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());
    }
    
    public static String timeStamp(String stampFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stampFormat, new Locale("en", "US"));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());
    }
    
    public static String getPermissionsString(int type, int permission){
        
        String result = "";
        int temp;
        
        while (permission > 0) {
            temp = permission % 10;
            
            result += ((temp >= 4) ? 'r' : '-');// + result;
            temp -= (temp >= 4) ? 4 : 0;
            result += ((temp >= 2) ? 'w' : '-');// + result;
            temp -= (temp >= 2) ? 2 : 0;
            result += ((temp >= 1) ? 'x' : '-');// + result;
            temp -= (temp >= 1) ? 1 : 0;
            
            permission /= 10;
        }
        result = (type == TYPE_DIRECTORY ? "d" : "-") + result;
        return result;
    }
    
    public static void help(String cmd){
        
        try {
            File helpFile = new File(new File("").getAbsolutePath() + "/help/" + cmd);
            Scanner fscan = new Scanner(helpFile);
            
            while (fscan.hasNextLine()) {
                System.out.println(fscan.nextLine());
            }
            
            fscan.close();        

        } catch (Exception e) {
               
        } 
    }
}
