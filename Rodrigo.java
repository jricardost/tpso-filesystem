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
    }
    
    public void head(String... args) {
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
        
        if (file != null && updateModificationTime){ //se o arquivo existe!
            file.modificationDate = Tools.timeStamp();
            return;
        }
        
        if (!createFile) return;
        
        
        String[] split;
        
        if (filename.indexOf("/") == 0) filename = filename.substring(1);

        if (filename.lastIndexOf("/") > 1){
            split = filename.split("/");
        } else {
            split = new String[] {filename.replace("/", "")};
        }
        
        //create directories
        IDirectory currentDir = (IDirectory) vfs.read(app.currentDirectory);

        if (currentDir == null){
            System.out.println("touch failed in the current directory");
            return;
        }
        
        for (int i = 0; i < split.length - 1; i++){
            IDirectory dir = new IDirectory(app.currentDirectory + "/" + split[i]);
            currentDir.files.put(dir.name, dir);
            currentDir = dir;
        }

        IFile ifile = new IFile(app.currentDirectory + "/" + filename);
        currentDir.files.put(ifile.name, ifile);
    }
}