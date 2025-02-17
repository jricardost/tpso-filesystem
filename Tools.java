import java.io.File;
import java.io.IOException;
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

        String[] help = readApplicationFile("/help/" + cmd);
        
        for (String s : help){
            System.out.println(s);
        }
    }
    
    
    public static String[] readApplicationFile(String path){
        
        try {
            ArrayList<String> lines = new ArrayList<String>();
            File file = new File(new File("").getAbsolutePath() + "/data/root" + path);
            Scanner fscan = new Scanner(file);
            
            while (fscan.hasNextLine()) {
                lines.add(fscan.nextLine());
            }
            
            fscan.close();        
            
            return lines.toArray(new String[lines.size()]);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } 
        
        return null;
    }
}
