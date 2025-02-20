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

		if (vfs.read(destinationPath.substring(0, destinationPath.lastIndexOf("/") - 1)) == null) {
			System.out.println("Destination directory not found: " + destinationPath);

		}

		app.execute("touch", destinationPath);

		// IFile sourceFile = (IFile) source;
		// IFile newFile = new IFile(destinationPath);
		// newFile.setContent(sourceFile.getContent()); // Copia aqui

		// // Adiciona
		// IDirectory currentDirectory = new IDirectory(app.currentDirectory);
		// currentDirectory.files.put(destinationPath, newFile);

		// System.out.println("File copied to " + destinationPath);

	}

	public void cd(String... args) {
	}

	public void rename(String... args) {
	}

	public void rm(String... args) {
	}
}