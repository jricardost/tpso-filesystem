import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public interface UsersInterface {
    
    
    public static HashMap<Integer, User> getUsers(){
        try {
            HashMap<Integer, User> users = new HashMap<Integer, User>();
            
            File passwd = new File(new File("").getAbsolutePath() + "/data/users");
            Scanner fscan = new Scanner(passwd);
            String data;
            
            while (fscan.hasNextLine()) {
                data = fscan.nextLine();
                
                if (data.charAt(0) != '#'){
                    String[] split = data.split(" ");
                    System.out.println(data);
                } 
            }
            fscan.close();
            
            return users;
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } 
        
        return null;
    }
}