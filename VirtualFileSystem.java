import java.lang.String;
import java.util.HashMap;

public class VirtualFileSystem implements Constants {

    final int MAX_SIZE;
    final String ROOT_FOLDER = "/";

    HashMap<Integer, Inode> files;
    
    /* Representa um nó genérico na árvore (Arquivo ou Diretório) */
    /* https://guialinux.uniriotec.br/inode/ */
    public class Inode {  

        // mesmo esquema do linux -> https://www.redhat.com/en/blog/linux-file-permissions-explained
        public int permissions; 
        public int owner;
        
        //Tamanho (em bytes)
        public int size;
        
        public String name;
        public String absolutePath; // caminho "completo" do arquivo
        public String creationDate; // Tue, 15 Nov 1994 08:12:31 GMT
        public String modificationDate; // Tue, 15 Nov 1994 08:12:31 GMT
        
        public final int address;
        public final int type;
        
        public Inode(int address, int type){
            
            this.address = address;
            this.type = type;
            this.creationDate = Tools.timeStamp("MMM d HH:mm");
            this.modificationDate = creationDate;
        }

        public int getPermissions(){
            return permissions;
        }

        public void setPermissions(){
            
        }
    }   
    
    public final class Directory extends Inode {
        
        HashMap<Integer, Inode> files;
        
        public Directory(int address){
            super(address, TYPE_DIRECTORY);
        }
    }  
    
    public final class File extends Inode {
        
        private byte[] content;
        
        public File(int address){
            super(address, TYPE_FILE);
        }


        public void setContent(byte[] content){
            this.content = content;
            this.modificationDate = Tools.timeStamp("MMM d HH:mm");
        }
    }
    
    public VirtualFileSystem(int size){
        this.MAX_SIZE = size;
    }

    public void initialize(){
        System.out.println(Application.currentUser.getId());
    }

    private void loadFromFile(){

    }
}
