import java.io.File;
import java.lang.String;
import java.util.HashMap;

public class VirtualFileSystem implements Constants {
    
    final int MAX_SIZE;
    
    private  String DISK_FOLDER = "";
    final String ROOT_FOLDER = "";
    
    
    HashMap<String, Inode> files;
    
    public IDirectory root;
    
    public VirtualFileSystem(int size){
        this.MAX_SIZE = size;
        initialize();
    }
    
    private void initialize(){
        DISK_FOLDER = new File("").getAbsolutePath() + "/data/root";
        files = new HashMap<String, Inode>();
        
        root = new IDirectory("");
        
        load(root, ROOT_FOLDER, DISK_FOLDER);
    }
    
    private void load(IDirectory node, String vPath, String rPath){
        
        for (File child : new File(rPath).listFiles()){
            
            if (child.isDirectory()){      
                
                IDirectory dir = new IDirectory(node.absolutePath + "/" + child.getName());
                node.files.put(child.getName(), dir);
                
                load(dir, String.format("%s", vPath, child.getName()), String.format("%s/%s", rPath, child.getName()));
                
            } else {
                if (child.getName().charAt(0) != '.') {
                    IFile fil = new IFile(node.absolutePath + "/" + child.getName());
                    node.files.put(child.getName(), fil);
                }
            }
        } 
    }
    
    
    public void save(){
        
    }
    
    public static Inode read(String path){
        return null;
    }
    
    public static Inode write(String path){
        return null;
    }
}
