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
            Tools.help(args[0]);
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
            Tools.help(args[0]);
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
        if (args.length != 3) {
            System.out.println("Usage: mv <source> <destination>");
            return;
        }
        
        Inode source = vfs.read(args[1]);
        Inode destination = vfs.read(args[2]);
        
        if (source == null || destination == null) {
            System.out.println("mv: cannot move '" + args[1] + "': No such file or directory");
            return;
        }
        
        if (destination instanceof IFile) {
            System.out.println("mv: cannot move ': " + args[2] + " is not a directory");
            return;
        }
        
        if (source instanceof IFile) {
            ((IDirectory) destination).files.put(source.name, source);
            app.execute("rm", args[1]);
        }
        
        // arquivo para diretorio
        // diretorio para diretorio
        
    }
    
    public void touch(String... args) {
        boolean createFile = true;
        boolean updateModificationTime = false;
        
        String filename = args[1];
        
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
        
        if (filename.lastIndexOf("/") != -1) {
            System.out.println("touch: cannot touch " + filename + " in another directory");
            return;
        }
        
        Inode file = vfs.read(filename);
        
        if (file != null && updateModificationTime) { // se o arquivo existe!
            file.modificationDate = Tools.timeStamp();
            return;
        }
        
        if (!createFile) {
            return;
        }
        
        IDirectory currentDir = (IDirectory) vfs.read(app.currentDirectory);
        
        IFile ifile = new IFile(app.currentDirectory + "/" + filename);
        currentDir.files.put(ifile.name, ifile);
    }
}