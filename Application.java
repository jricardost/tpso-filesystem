import java.lang.reflect.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application implements Constants {
	
	private static final boolean autoRootLogin = false;
	private static boolean exit = false;

	static User currentUser;	
	static UserAccountController uac;
	static Scanner input;
	static VirtualFileSystem vfs;
	
	static String currentDirectory;
	
	public static void main(String[] args){
		
		/* Ambiente */
		String cmdline;
		String[] arguments;
		
		
		/* Inicialização */
		input = new Scanner(System.in);
		vfs = new VirtualFileSystem(4096);
		uac = new UserAccountController();
		
		while(!exit) {


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

	public static void login(){
		uac.login();
	}

	public static void logout(){
		uac.logout();
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
	
	public static void test(){

	}

	/* --- João R --- */
	
	public static void clear(){
		Tools.clearScreen();
	}
	
	public static void cls(){
		clear();
	}
	
	public static void diff(String ... args){
	}
	
	public static void echo(String ... args){
	}
	
	public static void exit(){
		exit = true;
	}
	
	public static void ls(String ... args){

	}
	
	public static void mkdir(String ... args){
	}
	
	public static void pwd(){
	}
	
	public static void tail(String ... args){
	}
	
	/* --- Julia --- */
	public static void find(String ... args){
	}
	
	public static void rmdir(String ... args){
	}
	
	public static void stat(String ... args){
	}
	
	public static void wc(String ... args){
	}
	
	public static void zip(String ... args){
	}
	
	/* --- Mariana --- */
	public static void chmod(String ... args){
	}
	
	public static void cp(String ... args){
	}
	
	public static void cd(String ... args){
	}
	
	public static void rename(String ... args){
	}
	
	public static void rm(String ... args){
	}
	
	/* --- Nathan --- */
	public static void cat(String ... args){
	}
	
	public static void du(String ... args){
	}
	
	public static void grep(String ... args){
	}
	
	public static void tree(){
	}
	
	public static void unzip(String ... args){
	}
	
	/* --- Rodrigo --- */
	public static void chown(String ... args){
	}
	
	public static void head(String ... args){
	}
	
	public static void history(){
	}
	
	public static void mv(String ... args){
	}
	
	public static void touch(String ... args){
	}
}