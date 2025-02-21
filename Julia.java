import java.util.Map;
import java.util.HashMap;

public class Julia {
    
    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;
    
    public Julia(Application app, UserAccountController uac, VirtualFileSystem vfs){
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }
    
    public  void find(String ... args){
        if (args.length < 3) {
            System.out.println("Uso: find <caminho> <nome>");
            return;
        }
        
        String path = args[1];
        String name = args[2];
        
        Inode node;
        
        if (path.equals("/")) node = vfs.root;
        else node = vfs.read(path);
        
        if (!(node instanceof IDirectory) || node == null) {
            System.out.println("find: " + path + ": Directory not found");
            return;
        }
        
        IDirectory dir = (IDirectory) node;
        
        for (Inode n : dir.files.values()) {
            
            System.out.println(n.absolutePath);
            
            if (n instanceof IDirectory){    
                find("", path + "/" + n.name, name);
                return;
            }
            
            if (n.name.equals(name)){
                System.out.println(name);
                return;
            }
        }
        
        System.out.println("find: " + name + ": No such file or directory");
    }
    
    public  void rmdir(String ... args){
        if (args.length < 1) {
            System.out.println("Uso: rmdir <caminho>");
            return;
        }
        
        String path = args[0];
        IDirectory directory = new IDirectory(path);
        
        if (directory.type == Constants.TYPE_DIRECTORY) {
            if (directory.files.isEmpty()) {
                System.out.println("Diretório '" + path + "' removido com sucesso.");
            } else {
                System.out.println("Diretório '" + path + "' não está vazio. Não foi possível remover.");
            }
        } else {
            System.out.println("Diretório não encontrado ou não é um diretório: " + path);
        }
    }
    
    public  void stat(String ... args){
    }
    
    public  void wc(String ... args){
    }
    
    public  void zip(String ... args){
    }
}