public final class IFile extends Inode {
    
    private String[] content;
    
    public IFile(String path){
        super(TYPE_FILE);
        super.absolutePath = path;
        super.name = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
    }

    
    public String[] getContent(){
        return this.content;
    }
    
    public void setContent(String[] content){
        this.content = content;
        this.modificationDate = Tools.timeStamp("MMM d HH:mm");
        // Atualiza tamanho do arquivo
        int _size = 0;
        for (String line : content) {
            _size += line.length();
        }
        this.size = _size;
    }
}