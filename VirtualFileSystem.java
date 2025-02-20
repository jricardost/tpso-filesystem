import java.io.File;
import java.lang.String;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;

public class VirtualFileSystem implements Constants {
    
    final int MAX_SIZE;
    final String ROOT_FOLDER = "";
    
    private String DISK_FOLDER = "";
    private ArrayList<String> metadata;
    private UserAccountController uac;
    private Application app;
    
    static IDirectory root;
    static HashMap<String, Inode> files;
    
    public VirtualFileSystem(Application app, UserAccountController uac, int size){
        this.app = app;
        this.MAX_SIZE = size;
        this.uac = uac;
        initialize();
    }
    
    private void initialize(){
        
        DISK_FOLDER = new File("").getAbsolutePath() + "/data/root";
        files = new HashMap<String, Inode>();
        root = new IDirectory("");
        
        load(root, ROOT_FOLDER, DISK_FOLDER);
        
        applyMetadata();
    }
    
    private void load(IDirectory node, String vPath, String rPath){
        
        for (File child : new File(rPath).listFiles()){
            
            if (child.isDirectory()){      
                
                IDirectory dir = new IDirectory(node.absolutePath + "/" + child.getName());
                
                node.files.put(child.getName(), dir);
                
                load(dir, String.format("%s", vPath, child.getName()), String.format("%s/%s", rPath, child.getName()));
                
            } else {
                if (child.getName().charAt(0) != '.' && !child.getName().contains("readme.md")) {
                    IFile fil = new IFile(node.absolutePath + "/" + child.getName());
                    fil.setContent(Tools.readApplicationFile(node.absolutePath + "/" + child.getName()));
                    node.files.put(child.getName(), fil);
                }
            }
        } 
    }
    
    public final void save(){
        metadata = new ArrayList<String>();
        save(root);
        
        Tools.saveApplicationFile("/.metadata", metadata.toArray(new String[0]));
        
        System.out.println("files saved!");
    }
    
    private void save(IDirectory node){        
        for (Inode n : node.files.values()){
            
            metadata.add(String.format("%s:%s:%s", n.owner, n.permissions, n.absolutePath));
            
            if (n instanceof IDirectory){
                save((IDirectory) n);
            } else {
                Tools.saveApplicationFile(n.absolutePath, ((IFile) n).getContent());
            }
        }
    }
    
    private void applyMetadata(){
        String[] metadata = Tools.readApplicationFile("/.metadata");
        
        for (String s : metadata){
            
            String[] split = s.split(":");
            
            if (split.length != 3) continue;
            
            Inode node = get(split[2]);
            
            if (node != null){
                node.owner = Integer.parseInt(split[0]);
                node.permissions = Integer.parseInt(split[1]);
            }
        }
    }
    
    private String validatePath(String path){
        String res = path;
        res = res.replace("///", "/");
        res = res.replace("//", "/");
        return res;
    }
    
    private Inode get(String path){
        return get(path, uac.getUser("root"));
    }

    private Inode get(String path, User user){
        
        if (path != "/" && path.charAt(0) == '/') path = path.substring(1);
        
        String[] split = path.split("/");
        
        IDirectory dir = root;
        Inode temp = null;
        String name;
        
        for (int i = 0; i < split.length; i++){
            
            name = split[i];
            
            if (name == "") continue;
            
            temp = dir.files.get(name);
            
            if (temp == null) {
                return null;
            }
            
            if (temp instanceof IDirectory){
                dir = (IDirectory) temp;
                continue;
            }            
        }
        
        return temp;
    }
 
    public Inode read(String path){
        Inode node;
        String filePath = path;
        
        // System.out.println("fileRequest: " + validatePath(filePath));
        node = get(validatePath(filePath), Application.currentUser); 

        if (node == null){
            filePath = app.currentDirectory + "/" + path;

            // System.out.println("fileRequest: " + validatePath(filePath));
            node = get(validatePath(filePath), Application.currentUser); 
        }
        
        if (node != null) return node;
        
        // System.out.println("file not found");
        return null;
    }
    
    public void write(Inode file){
        return;
    }

    public void delete(Inode node){
        
    }
}
