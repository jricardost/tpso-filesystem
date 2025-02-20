import java.awt.desktop.AppForegroundListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Natan extends Constants {

    static final int MAX_TREE_DEPTH = 1024;

    public static void cat(String ... args){
        if (args.length == 1) {
            Tools.help(args[0]);
            return;
        }

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

    public static void tree(String ... args) {
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
    /*
     * @param depth: Profundidade de diretórios atual(começa em 0)
     *              Usado pra definir a identação da árvore
     * @param isLastEntry: Indica se o node atual é o último de seu diretório
     *                    Usado pra definir de vai ser usado o caracter "└" (true) ou "├" (false)
     * @param dirsDepths: Array que controla onde os caracteres verticais "│" são exibidos.
     *                   Cada índice representa uma profundidade na árvore:
     *                   - Se 'dirsDepths[3]' for true, um "│" será desenhado na
     *                     profundidade 3, indicando que há diretórios subsequentes
     *                     nesse nível
     *                   - Se 'dirsDepths[3]' for false, nenhum "│" será desenhado
     *                     na profundidade 3, pois não há mais diretórios ativos nela
     *   Profundidade:   0   1   2   3
     *                   └───root
     *                       ├───dir1
     *                       │       file1
     *                       │       file2
     *                       └───dir2
     *                               file1
     */
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
    
    public static void unzip(String ... args){
    }
}