import java.lang.reflect.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application implements Constants, JoaoRicardo, Julia, Mariana, Natan, Rodrigo {
	
	private static boolean exit = false;
	private static boolean saveFiles = false;
	private static boolean skipLogin = true;
	private static boolean displayMOTD = false;
	
	public static String currentDirectory;
	public static User currentUser;	
	public static VirtualFileSystem vfs;

	public Scanner input;
	public UserAccountController uac;
	
	public void main(){
		
		/* Ambiente */
		String cmdline;
		String[] arguments;
		
		initialize();
		
		while(!exit) {
			
			
			while (currentUser == null) {
				
				if (skipLogin){
					currentUser = uac.getUser(0);
				} else {
					uac.login();
					currentUser = uac.getCurrentUser();
				}
				
				if (displayMOTD) Tools.motd();
				currentDirectory = currentUser.homeDir();
			}
			
			try {
				System.out.print(String.format("%s@tpso:%s $ ", currentUser.name(), (currentDirectory == currentUser.homeDir()) ? "~" : currentDirectory));
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
		
		if (saveFiles) vfs.save();

		System.exit(0);
	}
	
	private void initialize(){
		input = new Scanner(System.in);
		vfs = new VirtualFileSystem(4096);
		uac = new UserAccountController();
	}

	public void execute(String[] arguments){
		try {
			
			Method method;
			Class<?> owner;
			
			owner = findMethodOwner(arguments[0], String[].class);
			
			if (owner != null){
				method = owner.getDeclaredMethod(arguments[0], String[].class);

				if (Modifier.isStatic(method.getModifiers())){
					method.invoke(owner, (Object) arguments);
				} else {
					method.invoke(this, (Object) arguments);
				}

				
			} else {
				throw new NoSuchMethodException();
			}
			
		} catch(NoSuchMethodException e){
			System.out.println("method not found");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Class<?> findMethodOwner(String methodName, Class<?> ... args){
		
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
	
	public void login(String ... args){
		uac.login();
	}
	
	public void logout(String ... args){
		uac.logout();
		skipLogin = false;
	}
	
	public void clear(String ... args){
		Tools.clearScreen();
	}
	
	public void cls(String ... args){
		clear();
	}
	
	public void test(String ... arguments){
		
	}
	
	public void exit(String ... args){
		if (args.length > 1 && args[1].equals("save")) {
			saveFiles = true;
		}

		Application.exit = true;
	}
}