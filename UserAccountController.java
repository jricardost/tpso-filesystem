import java.util.HashMap;
import java.util.Scanner;
import java.io.Console;
import java.security.AllPermission;

public class UserAccountController {
    
    // private int currentUser;
    User currentUser;
    HashMap<Integer, User> users;
    
    public UserAccountController(){

        users = new HashMap<Integer, User>();

        loadUserData();
        loadPasswordData();
    }
    
    private void loadUserData(){
        String[] data = Tools.readApplicationFile("/etc/passwd");
        
        // System.out.println("/data/users");
        
        // for (String s : data){
        //     System.out.println(s);
        // }
    }
    
    private void loadPasswordData(){
        String[] data = Tools.readApplicationFile("/etc/shadow");
        
        // System.out.println("/data/shadow");
        // for (String s : data){
        //     System.out.println(s);
        // }
    }
    
    private boolean authenticate(String username, String password){
        if (username.equals("user") && password.equals("user")){
            this.currentUser = new User(username, password, 0, "/");
            return true;
        }
        
        return false;
    }
    
    
    public void login(){
        
        int tries = 3;
        String username = "";
        String password = "";

        Console console = System.console();
        Scanner scan = new Scanner(System.in);
        
        System.out.print("login: ");
        username = scan.nextLine();
        
        do {
            
            System.out.print("password: ");
            
            if (console != null){
                password = new String(console.readPassword());
            } else {
                password = scan.nextLine();
            }
            
            if (authenticate(username, password)) break;
            else System.out.println("Access denied.");
            
            tries--;
            
        } while(tries > 0);
        
    }
    
    public void logout(){
        Application.currentUser = null;
    }
    
    public User getCurrentUser(){
        return this.currentUser;
    }
}