import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

public final class Tools implements Constants {
    
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
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        return simpleDateFormat.format(new Date());
    }
    
    public static String timeStamp(String stampFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stampFormat, new Locale("en", "US"));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
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
        
        String[] help = readApplicationFile("/usr/share/man/" + cmd);
        

        if (help == null){
            System.out.println("command not found");
            return;
        }

        System.out.println();

        for (String s : help){
            System.out.println(s);
        }

        System.out.println();
    }
    
    public static String[] readApplicationFile(String path){
        return readApplicationFile(path, true);
    }
    
    public static String[] readApplicationFile(String path, boolean keepComments){
        
        try {
            ArrayList<String> lines = new ArrayList<String>();
            
            File file = new File(new File("").getAbsolutePath() + "/data/root" + path);
            Scanner fscan = new Scanner(file);
            
            while (fscan.hasNextLine()) {
                
                String line = fscan.nextLine();
                if (!keepComments && line.charAt(0) == '#') continue;
                lines.add(line);
            }
            
            fscan.close();        
            
            return lines.toArray(new String[lines.size()]);
            
        } catch (Exception e) {
        } 
        
        return null;
    }
    
    public static void saveApplicationFile(String path, String[] content){
        
        
        File file = new File(new File("data/root").getAbsolutePath() + path);
        System.out.println("saving: " + file.getAbsolutePath());
        
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (content == null) return;
            for (String line : content) {
                writer.write(line);  // Write each line
                writer.newLine();  // Add a new line after each string
            }
            // System.out.println("File written successfully.");
        } catch (IOException e) {
            e.printStackTrace();  // Handle exception if file writing fails
        }
    }
    
    public static void motd(){
        String[] motd = readApplicationFile("/etc/motd", true);
        for (String s : motd) System.out.println(s);
    }
    
    public static String hashPassword(String passwd){
        try {
            //return MessageDigest.getInstance("SHA-256").digest(passwd.getBytes()).toString();
            return MessageDigest.getInstance("SHA-256").digest(passwd.getBytes(StandardCharsets.UTF_8)).toString();
        } catch (NoSuchAlgorithmException nsae){}
        
        return "";
    }

    public static String validatePath(String path) {
        String res = path;
        res = res.replace("///", "/");
        res = res.replace("//", "/");
        return res;
    }
}
