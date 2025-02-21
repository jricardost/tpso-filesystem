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
			System.out.println("Usage: chmod <permissions> <file>");
			return;
		}

		String permissionStr = args[1];
		String filePath = args[2];

		try {
			int permissions = Integer.parseInt(permissionStr, 8);
			Inode inode = vfs.read(filePath);

			if (inode != null) {
				inode.permissions = permissions;
				System.out.println("Permissions for " + filePath + " changed to " + permissionStr);
			} else {
				System.out.println("File not found: " + filePath);
			}

		} catch (NumberFormatException e) {
			System.out.println("Invalid permission format. Please use octal (e.g., 755).");
		}
	}

	public void cp(String... args) {
		if (args.length != 3) {
			System.out.println("Usage: cp <source> <destination>");
			return;
		}

		String sourcePath = args[1];
		String destinationPath = args[2];

		Inode destination = vfs.read(destinationPath);
		Inode source = vfs.read(sourcePath);

		if (source == null) {
			System.out.println("Source not found: " + sourcePath);
			return;
		}

		if (destination != null && source.getClass() != destination.getClass()) {
			System.out.println("Source and destination must be of the same type (file or directory).");
			return;
		}

		if (vfs.read(destinationPath.substring(0, destinationPath.lastIndexOf("/"))) == null) {
			System.out.println("Destination directory not found: " + destinationPath);
		}

		app.execute("touch", destinationPath);

		IFile sourceFile = (IFile) source;
		IFile newFile = (IFile) vfs.read(destinationPath);
		newFile.setContent(sourceFile.getContent());
	}

	public void cd(String... args) {
		if (args.length != 2) {
			System.out.println("Usage: cd <directory>");
			return;
		}

		if (args.length == 1) {
			app.currentDirectory = "/";
			return;
		}

		if (args[1].equals("..")) {
			app.currentDirectory = app.currentDirectory.substring(0, app.currentDirectory.lastIndexOf("/"));
			return;
		}

		String directoryPath = args[1];
		Inode directory = vfs.read(app.currentDirectory + "/" + directoryPath);

		if (directory == null || !(directory instanceof IDirectory)) {
			System.out.println("Directory not found: " + directoryPath);
			return;
		}

		app.currentDirectory = app.currentDirectory + '/' + directoryPath;
	}

	public void rename(String... args) {
		if (args.length != 3) {
			System.out.println("Usage: rename <old_name> <new_name>");
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
			System.out.println("Usage: rm <file>");
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