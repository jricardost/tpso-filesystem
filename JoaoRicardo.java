public interface JoaoRicardo {
    
    public static void diff(String ... args){
        Inode file1;
        Inode file2;
        
        if (args.length !=  3) {
            Tools.help(args[0]);
            return;
        }
        
        file1 = Application.vfs.read(args[1]);
        file2 = Application.vfs.read(args[2]);

        if (file1 == null || file1 instanceof IDirectory) {
            System.out.println("diff: " + args[1] + " not found");
            return;
        }
        if (file2 == null || file2 instanceof IDirectory) {
            System.out.println("diff: " + args[2] + " not found");
            return;
        }
    }
    
    public static void echo(String ... args){
    }
    
    public static void ls(String ... args){
    }
    
    public static void mkdir(String ... args){
    }
    
    public static void pwd(String ... args){
    }
    
    public static void tail(String ... args){
    }
}