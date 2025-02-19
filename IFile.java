public final class IFile extends Inode {
    
    private byte[] content;
    
    public IFile(String path){
        super(TYPE_FILE);
        super.absolutePath = path;
        super.name = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
    }
    
    private byte[] getContent(){
        return this.content;
    }
    
    public void setContent(byte[] content){
        this.content = content;
        this.modificationDate = Tools.timeStamp("MMM d HH:mm");
    }
}