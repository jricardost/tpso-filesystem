public class Natan {
    
    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;
    
    public Natan(Application app, UserAccountController uac, VirtualFileSystem vfs){
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }
    
    public static void cat(String ... args){
        Inode inode = VirtualFileSystem.read(args[1]);
        if (inode == null) {
            return;
        }
        
        if (inode.type == TYPE_DIRECTORY) {
            System.out.println("path specified is a directory");
            return;
        }
        
        IFile file = (IFile) inode;
        System.out.println(String.join("\n", file.getContent()));
        
    }
    
    public static void du(String ... args){
        String path = "";
        if (args.length == 2) {
            path = args[1];
        }
        
        Inode inode = VirtualFileSystem.read(path);
        if (inode == null) {
            System.out.println("file or folder not found");
            return;
        }
        du(inode);
    }
    
    public static void grep(String ... args){
        if (args.length != 3) {
            Tools.help(args[0]);
            return;
        }
        
        String pattern = args[1];
        String path = args[2];
        
        Inode inode = VirtualFileSystem.read(path);
        if (inode == null) {
            return;
        }
        
        if (inode.type == TYPE_DIRECTORY) {
            System.out.println("path specified is a directory");
            return;
        }
        
        IFile file = (IFile) inode;
        for(String line : file.getContent()) {
            if (line.contains(pattern)) {
                System.out.println(line);
            }
        }
    }
    
    public static void tree(String ... args){
        String path = "";
        if (args.length == 2) {
            path = args[1];
        }
        
        Inode directory = VirtualFileSystem.read(path);
        if (directory != null && directory.type == TYPE_DIRECTORY) {
            String[] split = (Application.currentDirectory + "/" + path).split("/");
            System.out.println(split[split.length - 1]);
            
            printTree(directory, 0, true, new boolean[MAX_TREE_DEPTH]);
        }
    }
    
    public static void unzip(String ... args){
        if (args.length == 1) {
            Tools.help(args[0]);
            return;
        }
        
        String path = args[1];
        Inode inode = VirtualFileSystem.read(path);
        
        if (inode == null) return;
        
        if (inode.type == TYPE_DIRECTORY) {
            System.out.println("path specified is a directory");
            return;
        }
        
        if (!inode.name.endsWith(".zip")) {
            System.out.println("file is not zipped");
            return;
        }
        
        // Tira .zip do nome do arquivo
        inode.name = inode.name.substring(0, inode.name.length() - 4);
        inode.absolutePath = inode.absolutePath.substring(0, inode.absolutePath.length() - 4);
        
        // TODO: descomprimir conteúdo do arquivo se necessário(depende da implementação do comando 'zip')
        
    }
    
    private static void printTree(Inode node, int depth, boolean isLastEntry, boolean[] dirsDepths) {
        if (node == null) return;
        
        if (depth >= MAX_TREE_DEPTH) {
            System.out.println("max tree depth of " + MAX_TREE_DEPTH + " reached");
            return;
        }
        
        for (int i = 1; i < depth; i++) {
            if (dirsDepths[i]) {
                System.out.print("│   ");
            }
            else {
                System.out.print("    ");
            }
        }
        
        // Diretório
        if (node.type == TYPE_DIRECTORY) {
            if (depth != 0) {
                if (isLastEntry) {
                    System.out.print("└");
                    dirsDepths[depth] = false;
                }
                else {
                    System.out.print("├");
                }
                System.out.printf("───%s\n", node.name);
            }
            IDirectory dir = (IDirectory) node;
            for(Inode inode : dir.files.values()) {
                if (inode.type == TYPE_DIRECTORY && depth + 1 < MAX_TREE_DEPTH) {
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
            }
            else {
                System.out.print("    ");
            }
            System.out.println(node.name);
        }
    }

    private static void du(Inode rootNode) {
        // Arquivo
        if (rootNode.type == TYPE_FILE) {
            System.out.printf("%-8d %s%n", rootNode.size, rootNode.absolutePath);
            return;
        }
        // Diretório
        IDirectory directory = (IDirectory) rootNode;
        for(Inode inode : directory.files.values()) {
            du(inode);
        }
    }
}
