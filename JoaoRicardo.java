public class JoaoRicardo {

    Application app;
    UserAccountController uac;
    VirtualFileSystem vfs;

    public JoaoRicardo(Application app, UserAccountController uac, VirtualFileSystem vfs){
        this.app = app;
        this.uac = uac;
        this.vfs = vfs;
    }
    
    public void diff(String ... args){
        Inode file1;
        Inode file2;

        String[] contentA;
        String[] contentB;
        String lineA;
        String lineB;
        int length;
        

        if (args.length !=  3) {
            Tools.help(args[0]);
            return;
        }
        
        file1 = vfs.read(args[1]);
        file2 = vfs.read(args[2]);

        if (file1 == null || file1 instanceof IDirectory) {
            System.out.println("diff: " + args[1] + " not found");
            return;
        }
        if (file2 == null || file2 instanceof IDirectory) {
            System.out.println("diff: " + args[2] + " not found");
            return;
        }

        contentA = ((IFile) file1).getContent();
        contentB = ((IFile) file2).getContent();

        length = contentA.length;
        if (contentB.length > length) length = contentB.length;

        for (int i = 0; i < length; i++){
            lineA = "";
            lineB = "";

            if (i < contentA.length) lineA = contentA[i];
            if (i < contentB.length) lineB = contentB[i];

            if (!lineA.equals(lineB)){
                System.out.println(String.format("line %d:\t%s\n\t%s", i, lineA, lineB));
            }
        }

    }
    
    public void echo(String ... args){
    }
    
    public void ls(String ... args){
    }
    
    public void mkdir(String ... args){
    }
    
    public void pwd(String ... args){
        System.out.println(app.currentDirectory);
    }
    
    public void tail(String ... args){

        int linecount = 0;

        switch (args.length) {
            case 2:
                linecount = 10;
                break;
        
            case 3:
                linecount = Integer.parseInt(args[2]);
                break;

            default:
                Tools.help("tail");
                return;
        }
        Inode dir = vfs.read(args[1]);
    }
}