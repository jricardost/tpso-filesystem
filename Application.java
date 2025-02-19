import java.lang.reflect.*;
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
			Class<?> owner;

			if (arguments.length == 1){
				/* busca métodos sem argumentos */
				owner = findMethodOwnerNoArgs(arguments[0]);
				if (owner != null){
					method = owner.getDeclaredMethod(arguments[0]);
					method.invoke(owner);
				}
			} else {
				/* busca métodos com argumentos */
				owner = findMethodOwner(arguments[0], String[].class);
				if (owner != null){
					method = owner.getDeclaredMethod(arguments[0], String[].class);
					method.invoke(owner, (Object) arguments);
				}

				// method = Application.class.getMethod(arguments[0], String[].class);
				// method.invoke(Application.class, (Object) arguments);
			}
		} catch(NoSuchMethodException e){
			System.out.println("method not found");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

		private static Class<?> findMethodOwnerNoArgs(String methodName){

		Method method;
		Class<?>[] interfaces = Application.class.getInterfaces();

		try{
			method = Application.class.getDeclaredMethod(methodName);
			if (method != null) return Application.class;
		} catch (Exception e){

		}

		for(Class<?> intfs : interfaces){

			try {
				method = intfs.getMethod(methodName);
				if (method != null) return intfs;
			} catch (Exception e) {

			}
		}
		
		return null;
	}
		private static Class<?> findMethodOwner(String methodName, Class<?> ... args){

		Method method;
		Class<?>[] interfaces = Application.class.getInterfaces();

		try{
			method = Application.class.getDeclaredMethod(methodName, args);
			if (method != null) return Application.class;
		} catch (Exception e){

		}

		for(Class<?> intfs : interfaces){

			try {
				method = intfs.getMethod(methodName, args);
				if (method != null) return intfs;
			} catch (Exception e) {

			}
		}
		
		return null;
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