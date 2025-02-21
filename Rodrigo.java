import java.util.ArrayList;
import java.util.List;

public class Rodrigo {

    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;

    public List<String> commandHistory = new ArrayList<>();

    public Rodrigo(Application app, UserAccountController uac, VirtualFileSystem vfs) {
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }

    public void addHistory(String cmd) {
        commandHistory.add(cmd);
    }

    public void chown(String... args) {
        if (args.length != 3) {
            System.out.println("Usage: chown <owner> <file>");
            return;
        }

        String owner = args[1];
        String filePath = args[2];

        Inode inode = vfs.read(filePath);

        if (inode != null) {
            try {
                inode.owner = Integer.parseInt(owner);
            } catch (NumberFormatException e) {
                System.out.println("Invalid owner id: " + owner);
                return;
            }
            System.out.println("Owner for " + filePath + " changed to " + owner);
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    public void head(String... args) {
        int linecount = 0;

        switch (args.length) {
            case 2:
                linecount = 10;
                break;

            case 3:
                try {
                    linecount = Integer.parseInt(args[2]);
                    break;
                } catch (Exception e) {
                    System.out.println("head: invalid number of lines: " + args[2]);
                    Tools.help("head");
                    return;
                }

            default:
                Tools.help("head");
                return;
        }
        Inode node = vfs.read(args[1]);
        if (node == null || node instanceof IDirectory) {
            System.out.println("head: " + args[1] + " not found");
            return;
        }

        IFile file = (IFile) node;
        String[] lines = file.getContent();

        int end = Math.min(linecount, lines.length);
        for (int i = 0; i < end; i++) {
            System.out.println(lines[i]);
        }
    }

    public void history(String... args) {
        if (args.length > 1 && args[1].equals("-c")) {
            commandHistory.clear();
            return;
        }
        int index = 1;
        for (String cmd : commandHistory) {
            System.out.println(index + " " + cmd);
            index++;
        }
    }

    public void mv(String... args) {
    }

    public void touch(String... args) {
        boolean createFile = true;
        boolean updateModificationTime = false;

        String filename = args[1];
        Inode file;

        if (args.length < 2) {
            System.out.println("touch: missing file operand");
            return;
        }

        for (String str : args) {
            switch (str) {
                case "-m":
                    createFile = true;
                    updateModificationTime = true;
                    break;

                case "-c":
                    createFile = false;
                    updateModificationTime = true;
                    break;
            }
        }

        if (filename.lastIndexOf("/") == 0) {
            System.out.println("touch: cannot touch " + filename + ": Is a directory");
            return;
        }

        file = vfs.read(filename);

        if (file != null && updateModificationTime) { // se o arquivo existe!
            file.modificationDate = Tools.timeStamp();
            return;
        }

        if (!createFile)
            return;

        String[] split;

        if (filename.indexOf("/") == 0)
            filename = filename.substring(1);

        if (filename.lastIndexOf("/") > 1) {
            split = filename.split("/");
        } else {
            split = new String[] { filename.replace("/", "") };
        }

        // create directories
        IDirectory currentDir = (IDirectory) vfs.read(app.currentDirectory);

        if (currentDir == null) {
            System.out.println("touch failed in the current directory");
            return;
        }

        for (int i = 0; i < split.length - 1; i++) {
            IDirectory dir = new IDirectory(app.currentDirectory + "/" + split[i]);
            currentDir.files.put(dir.name, dir);
            currentDir = dir;
        }

        IFile ifile = new IFile(app.currentDirectory + "/" + filename);
        currentDir.files.put(ifile.name, ifile);
    }
}