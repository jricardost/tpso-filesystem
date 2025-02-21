import java.util.ArrayList;
import java.util.List;

public class Julia {

    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;

    public Julia(Application app, UserAccountController uac, VirtualFileSystem vfs) {
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }

    public void find(String... args) {
        if (args.length < 3) {
            System.out.println("Usage: find <path> <file>");
            return;
        }

        String path = args[1];
        String name = args[2];

        Inode node;

        if (path.equals("/"))
            node = vfs.root;
        else
            node = vfs.read(path);

        if (node == null || !(node instanceof IDirectory)) {
            System.out.println("find: " + path + ": Directory not found");
            return;
        }

        IDirectory dir = (IDirectory) node;

        for (Inode n : dir.files.values()) {

            System.out.println(n.absolutePath);

            if (n instanceof IDirectory) {
                find("", path + "/" + n.name, name);
                return;
            }

            if (n.name.equals(name)) {
                System.out.println(name);
                return;
            }
        }

        System.out.println("find: " + name + ": No such file or directory");
    }

    public void rmdir(String... args) {
        if (args.length < 1) {
            System.out.println("Usage: rmdir <path>");
            return;
        }

        String path = args[1];
        Inode node = vfs.read(app.currentDirectory(path));
        if (node == null) {
            System.out.println("rmdir: " + path + ": No such file or directory");
            return;
        }

        if (node instanceof IFile) {
            System.out.println("rmdir: " + path + ": Not a directory");
            return;
        }

        if (((IDirectory) node).files.isEmpty()) {
            IDirectory parent = (IDirectory) vfs
                    .read(node.absolutePath.substring(0, node.absolutePath.lastIndexOf("/")));
            parent.files.remove(node.name);
            return;
        }
        System.out.println("rmdir: " + path + ": Directory not empty");
    }

    public void stat(String... args) {
        if (args.length < 1) {
            System.out.println("Usage: stat <path>");
            return;
        }

        String path = args[1];
        Inode node = vfs.read(path);

        if (node != null) {
            System.out.println("Information about '" + path + "':");
            System.out.println("Type: " + (node.type == Constants.TYPE_FILE ? "File" : "Directory"));
            System.out.println("Size: " + node.size + " bytes");
            System.out.println("Creation: " + node.creationDate);
            System.out.println("Modification: " + node.modificationDate);
            return;
        }
        System.out.println("File or directory not found: " + path);
    }

    public void wc(String... args) {
        if (args.length < 1) {
            System.out.println("Usage: wc <file>");
            return;
        }

        String filePath = args[1];
        Inode node = vfs.read(filePath);

        if (node == null) {
            System.out.println("wc: " + filePath + " no such file or directory");
            return;
        }

        if (node instanceof IFile) {
            IFile file = (IFile) node;
            String[] content = file.getContent();
            if (content != null) {
                int lineCount = content.length;
                int wordCount = 0;
                int charCount = 0;

                for (String line : content) {
                    String[] words = line.split("\\s+");
                    wordCount += words.length;
                    charCount += line.length();
                }

                System.out.println(lineCount + " " + wordCount + " " + charCount + " " + filePath);
                return;
            }
            System.out.println("wc: " + filePath + " no such file or directory");
            return;
        }
        System.out.println("wc: " + filePath + " not a file");
    }

    public void zip(String... args) {
        if (args.length < 2) {
            System.out.println("Uso: zip <arquivo_saida> <arquivo1> <arquivo2> ...");
            return;
        }

        String output = args[1] + ".zip";
        List<String> content = new ArrayList<String>();
        List<String> files = new ArrayList<String>();

        for (int i = 2; i < args.length; i++) {
            files.add(args[i]);
        }

        for (String string : files) {
            Inode node = vfs.read(string);
            if (node == null) {
                System.out.println("zip: " + string + ": No such file or directory");
                return;
            }

            if (node instanceof IDirectory) {
                System.out.println("zip: " + string + ": Is a directory");
                return;
            }

            content.add("f: " + node.name);
            for (String line : ((IFile) node).getContent()) {
                content.add(line);
            }
        }

        app.execute("touch", output);
        ((IFile) vfs.read(app.currentDirectory(output))).setContent(content.toArray(new String[content.size()]));

    }
}