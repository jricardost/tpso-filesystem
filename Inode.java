/* Representa um nó genérico na árvore (Arquivo ou Diretório) */
/* https://guialinux.uniriotec.br/inode/ */
public class Inode implements Constants {  
    
    // mesmo esquema do linux -> https://www.redhat.com/en/blog/linux-file-permissions-explained
    public int permissions; 
    public int owner;
    
    public int size; /*(em bytes)*/
    public final int type; /* 0 = file | 1 = folder */
    
    public String name;
    public String absolutePath; // caminho "completo" do arquivo
    public String creationDate; // Tue, 15 Nov 1994 08:12:31 GMT
    public String modificationDate; // Tue, 15 Nov 1994 08:12:31 GMT
    
    public Inode(int type){
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