import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application implements Constants, JoaoRicardo, Julia, Mariana, Natan, Rodrigo {
	
	private static boolean exit = false;
	private static boolean skipLogin = true;
	
	static String currentDirectory;
	static User currentUser;	
	
	public static Scanner input;
	public static VirtualFileSystem vfs;
	public static UserAccountController uac;
	
	public static void main(String[] args){
		
		/* Ambiente */
		String cmdline;
		String[] arguments;
		
		initialize();
		
		while(!exit) {
			
			if (skipLogin){
				currentUser = new User("root", "", 0, "/");
			}
			while (currentUser == null) {
				uac.login();
				currentUser = uac.getCurrentUser();
				
				if (currentUser != null){
					Tools.motd();
				}
			}
			
			try {
				System.out.print(String.format("%s@tpso:~$ ", currentUser.getName()));
				cmdline = input.nextLine();
				arguments = cmdline.split(" ");
				
				if (arguments.length == 2 && arguments[1].contains("--help")){
					Tools.help(arguments[0]);
				} else {
					execute(arguments);
				}
				
			}
			catch (NoSuchElementException e) { /* para sair ao pressionar CTRL + C */
				exit = true; 
				break;
			}
			
			if (exit) break;
			
		}
		
		input.close();
		
		clear();
		
		System.exit(0);
	}
	
	private static void initialize(){
		input = new Scanner(System.in);
		vfs = new VirtualFileSystem(4096);
		uac = new UserAccountController();
	}
	
	public static void execute(String[] arguments){
		try {
			
			Method method;
			
			if (arguments.length == 1){
				/* busca métodos sem argumentos */
				method = Application.class.getMethod(arguments[0]);
				method.invoke(Application.class);
			} else {
				/* busca métodos com argumentos */
				method = Application.class.getMethod(arguments[0], String[].class);
				method.invoke(Application.class, (Object) arguments);
			}
		} catch(NoSuchMethodException e){
			System.out.println("method not found");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void login(){
		uac.login();
	}
	
	public static void logout(){
		uac.logout();
	}
	
	public static void clear(){
		Tools.clearScreen();
	}
	
	public static void cls(){
		clear();
	}
	
	public static void test(String[] arguments){
		System.out.println(arguments[1]);
		System.out.println(Tools.hashPassword(arguments[1]));
	}
	
	public static void exit(){
		exit = true;
	}
}