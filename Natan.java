import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Natan {
    
    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;
    
    static final int MAX_TREE_DEPTH = 1024;
    
    public Natan(Application app, UserAccountController uac, VirtualFileSystem vfs) {
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }
    
    public void cat(String... args) {
        Inode inode = vfs.read(args[1]);
        if (inode == null) {
            return;
        }
        
        if (inode.type == Constants.TYPE_DIRECTORY) {
            System.out.println("path specified is a directory");
            return;
        }
        
        IFile file = (IFile) inode;
        System.out.println(String.join("\n", file.getContent()));
    }
    
    public void du(String... args) {
        String path = "";
        
        switch (args.length) {
            case 1:
                path = app.currentDirectory;
            break;
            
            case 2:
                path = args[1];
            break;

            default:
                Tools.help(args[0]);
            return;
        }
        
        Inode inode = vfs.read(path);
        if (inode == null) {
            System.out.println("file or folder not found");
            return;
        }
        du(inode);
    }
    
    public void grep(String... args) {
        if (args.length != 3) {
            Tools.help(args[0]);
            return;
        }
        
        String pattern = args[1];
        String path = args[2];
        
        Inode inode = vfs.read(path);
        if (inode == null) {
            return;
        }
        
        if (inode.type == Constants.TYPE_DIRECTORY) {
            System.out.println("path specified is a directory");
            return;
        }
        
        IFile file = (IFile) inode;
        for (String line : file.getContent()) {
            if (line.contains(pattern)) {
                System.out.println(line);
            }
        }
    }
    
    public void tree(String... args) {
        String path = "";
        
        switch(args.length){
            case 1:
            path = app.currentDirectory;
            break;
            
            case 2: 
            path = args[1];
            break;
        }
        
        
        System.out.println("path: " + path);
        
        Inode directory = vfs.read(path);
        
        if (directory == null) System.out.println("null");
        
        if (directory != null && directory.type == Constants.TYPE_DIRECTORY) {
            // String[] split = (app.currentDirectory(path)).split("/");
            // System.out.println(split[split.length - 1]);
            
            printTree(directory, 0, false, new boolean[MAX_TREE_DEPTH]);
        }
    }
    
    public void unzip(String... args) {
        if (args.length == 1) {
            Tools.help(args[0]);
            return;
        }
        
        String path = args[1];
        Inode inode = vfs.read(path);
        
        if (inode == null)
        return;
        
        if (inode.type == Constants.TYPE_DIRECTORY) {
            System.out.println("path specified is a directory");
            return;
        }
        
        if (!inode.name.endsWith(".zip")) {
            System.out.println("file is not zipped");
            return;
        }
        
        IFile file = (IFile) inode;
        String[] content = file.getContent();
        ArrayList<String> contentLines = new ArrayList<>();
        for (String line : content) {
            if (line.startsWith("f: ")) {
                if (!contentLines.isEmpty()) {
                    file.setContent(contentLines.toArray(new String[contentLines.size()]));
                    contentLines.clear();
                }
                String[] split = line.split(" ");
                String filename = split[1];
                app.execute("touch", filename);
                file = (IFile) vfs.read(filename);
            } else {
                contentLines.add(line);
            }
        }
        if (!contentLines.isEmpty()) {
            file.setContent(contentLines.toArray(new String[contentLines.size()]));
        }
        
    }
    
    private void printTree(Inode node, int depth, boolean isLastEntry, boolean[] dirsDepths) {
        if (node == null)
        return;
        
        if (depth >= MAX_TREE_DEPTH) {
            System.out.println("max tree depth of " + MAX_TREE_DEPTH + " reached");
            return;
        }
        
        for (int i = 1; i < depth; i++) {
            if (dirsDepths[i]) {
                System.out.print("│   ");
            } else {
                System.out.print("    ");
            }
        }
        
        // Diretório
        if (node.type == Constants.TYPE_DIRECTORY) {
            if (depth != 0) {
                if (isLastEntry) {
                    System.out.print("└");
                    dirsDepths[depth] = false;
                } else {
                    System.out.print("├");
                }
                System.out.printf("───%s\n", node.name);
            }
            IDirectory dir = (IDirectory) node;
            for (Inode inode : dir.files.values()) {
                if (inode.type == Constants.TYPE_DIRECTORY && depth + 1 < MAX_TREE_DEPTH) {
                    dirsDepths[depth + 1] = true;
                    break;
                }
            }
            
            List<Map.Entry<String, Inode>> entries = new ArrayList<>(dir.files.entrySet());
            for (int i = 0; i < entries.size(); i++) {
                Map.Entry<String, Inode> entry = entries.get(i);
                printTree(entry.getValue(), depth + 1, (i == entries.size() - 1), dirsDepths);
            }
        }
        // Arquivo
        else {
            if (dirsDepths[depth]) {
                System.out.print("│   ");
            } else {
                System.out.print("    ");
            }
            System.out.println(node.name);
        }
    }
    
    private void du(Inode rootNode) {
        // Arquivo
        if (rootNode.type == Constants.TYPE_FILE) {
            System.out.printf("%-8d %s%n", rootNode.size, rootNode.absolutePath);
            return;
        }
        // Diretório
        IDirectory directory = (IDirectory) rootNode;
        for (Inode inode : directory.files.values()) {
            du(inode);
        }
    }
}
