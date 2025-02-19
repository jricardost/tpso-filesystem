import java.util.HashMap;

public final class IDirectory extends Inode {
    
    public HashMap<String, Inode> files;
    
    public IDirectory(String path){
        super(TYPE_DIRECTORY);
        super.absolutePath = path;
        super.name = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;

        files = new HashMap<String, Inode>();
    }
}