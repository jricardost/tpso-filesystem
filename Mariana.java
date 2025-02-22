public class Mariana {
	
	Application app;
	UserAccountController uac;
	VirtualFileSystem vfs;
	
	public Mariana(Application app, UserAccountController uac, VirtualFileSystem vfs) {
		this.app = app;
		this.uac = uac;
		this.vfs = vfs;
	}
	
	public void chmod(String... args) {
		if (args.length != 3) {
			Tools.help(args[0]);
			return;
		}
		
		String permissionStr = args[1];
		String filePath = args[2];
		
		try {
			int permissions = Integer.parseInt(permissionStr);
			Inode inode = vfs.read(filePath);
			
			if (inode != null) {
				inode.permissions = permissions;
			} else {
				System.out.println("File not found: " + filePath);
			}
			
		} catch (NumberFormatException e) {
			System.out.println("Invalid permission format. Please use octal (e.g., 755).");
		}
	}
	
	public void cp(String... args) {
		if (args.length != 3) {
			Tools.help(args[0]);
			return;
		}
		
		String sourcePath = args[1];
		String destinationPath = args[2];
		
		Inode source = vfs.read(sourcePath);
		Inode destination = vfs.read(destinationPath);
		
		if (source == null) {
			System.out.println("failed: " + sourcePath + " not found");
			return;
		}
		
		if (destination == null && source instanceof IDirectory){
			System.out.println("failed: " + destinationPath + " not found");
			return;
		}
		
		if (source instanceof IDirectory && destination instanceof IFile) {
			System.out.println("failed : cannot copy a directory to a file");
			return;
		}
		
		
		if (source instanceof IDirectory){
			
			app.execute("mkdir", destinationPath);
			
			IDirectory src = (IDirectory) source;
			IDirectory dst = (IDirectory) destination;
			
			dst.files.putAll(src.files);
			return;
		}
		
		
		if (source instanceof IFile) {

			IFile src = (IFile) source;
			IFile dst;

			if (destinationPath.contains("/")){
				
				// String temp = destinationPath;
				app.execute("mkdir", destinationPath);
				IDirectory dir = (IDirectory) vfs.read(destinationPath);

				dir.files.put(src.name, src);

				return;
			}

			app.execute("touch", destinationPath);
			dst = (IFile) vfs.read(destinationPath);
			dst.setContent(src.getContent());
		}		
	}
	
	public void cd(String... args) {
		if (args.length != 2) {
			Tools.help(args[0]);
			return;
		}
		
		if (args[1].equals("/")){
			app.currentDirectory = "/";
			return;
		}
		
		if (args[1].equals("..")) {
			if (app.currentDirectory.lastIndexOf("/") != 0){
				app.currentDirectory = app.currentDirectory.substring(0, app.currentDirectory.lastIndexOf("/"));
			} else {
				app.currentDirectory = "/";
			}
			return;
		}
		
		String directoryPath = args[1];
		Inode directory = vfs.read(app.currentDirectory + "/" + directoryPath);
		
		if (directory == null || !(directory instanceof IDirectory)) {
			System.out.println("Directory not found: " + directoryPath);
			return;
		}
		
		app.currentDirectory = Tools.validatePath(app.currentDirectory + '/' + directoryPath);
	}
	
	public void rename(String... args) {
		if (args.length != 3) {
			Tools.help(args[0]);
			return;
		}
		
		String oldName = args[1];
		String newName = args[2];
		
		IDirectory folder = (IDirectory) vfs.read(app.currentDirectory);
		Inode inode = vfs.read(oldName);
		if (inode != null) {
			folder.files.remove(oldName);
			
			inode.name = newName;
			inode.absolutePath = inode.absolutePath.replace(oldName, newName);
			
			folder.files.put(newName, inode);
			
			System.out.println("Renamed " + oldName + " to " + newName);
			
		} else {
			System.out.println("File or directory not found: " + oldName);
		}
	}
	
	public void rm(String... args) {
		if (args.length != 2) {
			Tools.help(args[0]);
			return;
		}
		
		String filePath = args[1];
		Inode inode = vfs.read(filePath);
		
		if (inode != null) {
			IDirectory currentDirectory = (IDirectory) vfs.read(app.currentDirectory);
			currentDirectory.files.remove(filePath);
		} else {
			System.out.println("File or directory not found: " + filePath);
		}
	}
}