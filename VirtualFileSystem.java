import java.lang.String;
import java.util.HashMap;

public class VirtualFileSystem {
    
    final int START_ADDRESS = 0x0000;
    final int MAX_SIZE;

    final String ROOT_FOLDER = "/";

    
    HashMap<Integer, Inode> storage;
    
    /* Representa um nó genérico na árvore (Arquivo ou Diretório) */
    /* https://guialinux.uniriotec.br/inode/ */
    protected class Inode {  

        public static final int DIRECTORY = 0;
        public static final int FILE = 1;

        // mesmo esquema do linux -> https://www.redhat.com/en/blog/linux-file-permissions-explained
        protected int permissions; 
        protected int owner;
        
        //Tamanho (em bytes)
        protected int size;
        
        protected String name;
        protected String absolutePath; // caminho "completo" do arquivo
        protected String creationDate; // Tue, 15 Nov 1994 08:12:31 GMT
        protected String modificationDate; // Tue, 15 Nov 1994 08:12:31 GMT
        
        public final int address;
        public final int type;
        
        public Inode(int address, int type){
            this.address = address;
            this.type = type;
            this.creationDate = Utils.timeStamp("MMM d HH:mm");
        }
        
        public final String getName(){
            return this.name;
        }
        
        public final boolean setName(String name){
            if (name == null || name.isEmpty()) return false;
            this.name = name;
            return true;
        }
        
        public final String getAbsolutePath(){
            return this.absolutePath;
        }
        
        public final boolean setAbsolutePath(String path){
            if (path == null || path.isEmpty()) return false;
            this.absolutePath = path;
            return true;
        }
        
        public final int getPermissions(){
            return this.permissions;
        }

        public final void setPermissions(int permissions){
            if (permissions < 0 || permissions > 777) return;
            this.permissions = permissions;
        }
        
        public final String getPermissionsString(){

            String result = "";
            int permission = this.permissions;
            int temp;

            while (permission > 0) {
                temp = permission % 10;
                
                result += ((temp >= 4) ? 'r' : '-');// + result;
                temp -= (temp >= 4) ? 4 : 0;
                result += ((temp >= 2) ? 'w' : '-');// + result;
                temp -= (temp >= 2) ? 2 : 0;
                result += ((temp >= 1) ? 'x' : '-');// + result;
                temp -= (temp >= 1) ? 1 : 0;
                
                permission /= 10;
            }
            result = (type == DIRECTORY ? "d" : "-") + result;
            return result;
        }

        public final void dump(){
            System.out.println(String.format("%s\t%s\t%s", getPermissionsString(), getName(), creationDate));
        }
    }
    
    
    public final class Directory extends Inode {
        
        HashMap<Integer, Inode> files;
        
        public Directory(int address){
            super(address, Inode.DIRECTORY);
        }
        
    }
    
    
    public final class File extends Inode {
        
        protected byte[] content;
        
        public File(int address){
            super(address, Inode.FILE);
        }
    }
    
    
    public VirtualFileSystem(int size){
        this.MAX_SIZE = size;
    }
}
