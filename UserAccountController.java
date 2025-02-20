import java.util.HashMap;
import java.util.Scanner;
import java.io.Console;
import java.security.AllPermission;

public class UserAccountController {
    
    // private int currentUser;
    User currentUser;
    HashMap<String, User> users;
    
    public UserAccountController(){
        
        users = new HashMap<String, User>();
        
        loadUserData();
    }
    
    private void loadUserData(){
        String[] usr = Tools.readApplicationFile("/etc/passwd", false);
        String[] pwd = Tools.readApplicationFile("/etc/shadow", false);
        
        HashMap<String, String> passwd = new HashMap<String, String>();
        
        for (String s : pwd){
            
            String[] split = s.split(":");
            if (split.length == 2){
                passwd.put(split[0], split[1]);
            }
        }
        
        for (String s : usr){
            String[] split  = s.split(":");
            users.put(split[0], new User(split[0], passwd.get(split[0]), Integer.parseInt(split[2]), split[3]));
        }
    }
    
    private boolean authenticate(String username, String password){

        User user = users.get(username);

        if (user != null && password.equals(user.password())){
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

    public User getUser(int id){

        for (User user : users.values()){
            if (user.id() == id) return user;
        }

        return null;
    }

    public User getCurrentUser(){
        return this.currentUser;
    }
}