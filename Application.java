import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application implements Constants{
	
	private static boolean exit = false;
	
	static User currentUser;	
	static Scanner input;
	static VirtualFileSystem vfs;
	
	
	public static void main(String[] args){
		
		/* Ambiente */
		String envroot = "> ";
		String cmdline;
		String[] arguments;
		
		
		/* Inicialização */
		input = new Scanner(System.in);
		vfs = new VirtualFileSystem(4096);

		while(!exit) {
			
			try {
				System.out.print(envroot);
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
		
		System.exit(0);
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
	
	/* --- Natan --- */
	public static void cat(String ... args){
		String filePath = args[1];
		VirtualFileSystem.Inode inode = Tools.findInode(filePath);

		if (inode == null || inode.type == TYPE_DIRECTORY) {
			System.out.println("file not found");
			return;
		}

		VirtualFileSystem.File file = (VirtualFileSystem.File) inode;

		// Converte conteúdo do arquivo de bytes para texto
		String text = new String(file.getContent(), StandardCharsets.UTF_8);

		System.out.println(text);
	}

	public static void du() {
        du(vfs.currentDirectory);
	}

	public static void du(String ... args) {
		String path = args[1];
		VirtualFileSystem.Inode inode = Tools.findInode(path);
		if (inode == null) {
			System.out.println("file or folder not found");
			return;
		}
		du(inode);
	}

	private static void du(VirtualFileSystem.Inode rootNode) {
		if (rootNode.type == TYPE_FILE) {
			System.out.printf("%-8d %s%n", rootNode.size, rootNode.absolutePath);
			return;
		}
		VirtualFileSystem.Directory directory = (VirtualFileSystem.Directory) rootNode;
		for(VirtualFileSystem.Inode node : directory.files.values()) {
			du(node);
		}
	}

	public static void grep(String ... args){
		if (args.length < 3) {
			System.out.println("missing args");
			System.out.println("use 'grep --help' for more info");
			return;
		}

		String pattern = args[1];
		String filePath = args[2];

		VirtualFileSystem.Inode inode = Tools.findInode(filePath);
		if (inode == null || inode.type == TYPE_DIRECTORY) {
			System.out.println("file not found");
			return;
		}

		VirtualFileSystem.File file = (VirtualFileSystem.File) inode;
		String content = new String(file.getContent(), StandardCharsets.UTF_8);
		String[] lines = content.split("\n");
		for(String line : lines) {
			if (line.contains(pattern)) {
				System.out.println(line);
			}
		}
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