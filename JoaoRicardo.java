import java.util.Arrays;

public class JoaoRicardo {
    
    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;
    
    public JoaoRicardo(Application app, UserAccountController uac, VirtualFileSystem vfs) {
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }
    
    public void diff(String... args) {
        Inode file1;
        Inode file2;
        
        String[] contentA;
        String[] contentB;
        String lineA;
        String lineB;
        int length;
        
        if (args.length != 3) {
            Tools.help(args[0]);
            return;
        }
        
        file1 = vfs.read(args[1]);
        file2 = vfs.read(args[2]);
        
        if (file1 == null || file1 instanceof IDirectory) {
            System.out.println("diff: " + args[1] + " not found");
            return;
        }
        if (file2 == null || file2 instanceof IDirectory) {
            System.out.println("diff: " + args[2] + " not found");
            return;
        }
        
        contentA = ((IFile) file1).getContent();
        contentB = ((IFile) file2).getContent();
        
        length = contentA.length;
        if (contentB.length > length)
        length = contentB.length;
        
        for (int i = 0; i < length; i++) {
            lineA = "";
            lineB = "";
            
            if (i < contentA.length)
            lineA = contentA[i];
            if (i < contentB.length)
            lineB = contentB[i];
            
            if (!lineA.equals(lineB)) {
                System.out.println(String.format("line %d:\t%s\n\t%s", i, lineA, lineB));
            }
        }
        
    }
    
    public void echo(String... args) {

        if (args.length != 4) {
            Tools.help(args[0]);
            return;
        }
        
        String fileName = args[3];
        
        Inode node = vfs.read(fileName);
        
        if (node == null){
            app.execute("touch", fileName);
            node = vfs.read(fileName);
        }
        
        if (node instanceof IDirectory) {
            System.out.println("echo: " + fileName + " is a Directory");
            return;
        }
        
        IFile file = (IFile) node;
        
        switch(args[2]){
            case ">" :
            file.setContent(new String[] {args[1]});
            break;
            
            case ">>":
            String[] content = Arrays.copyOf(file.getContent(), file.getContent().length + 1);
            content[content.length - 1] = args[1];
            file.setContent(content);
            break;
            
            default: 
            for (String s : args) System.out.println(s + " ");
            break;
        }
    }
    
    public void ls(String... args) {
        IDirectory dir = (IDirectory) vfs.read(app.currentDirectory);
        for (String key : dir.files.keySet()) {
            Inode file = dir.files.get(key);
            System.out.print(Tools.getPermissionsString(file.type, file.permissions) + " - ");
            System.out.print((Application.uac.getUser(file.owner)).name() + " ");
            System.out.print(file.creationDate + " ");
            System.out.print(file.name + "\n");
        }
    }
    
    public void mkdir(String... args) {
        if (args.length != 2){
            Tools.help(args[0]);
            return;
        }

        String path = args[1];

        if (path.charAt(0) == '/') path = path.substring(1);

        String[] split = path.split("/");

        IDirectory currentDir = (IDirectory) vfs.read(app.currentDirectory);

        for (int i = 0; i < split.length; i++) {
            IDirectory dir = new IDirectory(app.currentDirectory + "/" + split[i]);
            currentDir.files.put(dir.name, dir);
            currentDir = dir;
        }
    }
    
    public void pwd(String... args) {
        System.out.println(app.currentDirectory);
    }
    
    public void tail(String... args) {
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
        
        int start = Math.max(0, lines.length - linecount);
        for (int i = start; i < lines.length; i++) {
            System.out.println(lines[i]);
        }
    }
}