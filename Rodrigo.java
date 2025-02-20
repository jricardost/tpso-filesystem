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
        if (args.length < 2) {
            System.out.println("touch: missing file operand");
            return;
        }

        boolean updateModificationTime = false;
        boolean createFile = true;
        String filename = args[1];

        for (int i = 0; i < args.length; i++) {
            if ("-m".equals(args[i]) && i + 1 < args.length) {
                updateModificationTime = true;
                filename = args[i + 1];
                i++;
            } else if ("-c".equals(args[i])) {
                createFile = false;
            }
        }

        Inode file = vfs.read(filename);

        if (file == null && createFile) {
            Inode folder = vfs.read(app.currentDirectory);
            IFile newFile = new IFile(folder.absolutePath + '/' + filename);
            newFile.absolutePath = folder.absolutePath + '/' + filename;
            newFile.name = filename; 
            newFile.creationDate = Tools.timeStamp();
            newFile.modificationDate = newFile.creationDate;
            System.out.println(uac.getCurrentUser().id());
            newFile.owner = 0;
            newFile.permissions = 755;
            newFile.size = 0;

            ((IDirectory) folder).files.put(newFile.name, newFile);
        } else if (updateModificationTime && file != null) {
            file.modificationDate = Tools.timeStamp();
        }
    }
}