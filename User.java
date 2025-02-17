public class User {
    private int UID;
    private String name;
    private String passwd;
    private String homeDir;

    public User(int uid){
        this.UID = uid;
    }

    public User(String name, String passwd, int uid, String homeDir){
        this.UID = uid;
        this.name = name;
        this.passwd = passwd;
        this.homeDir = homeDir;
    }

    public int getId(){
        return UID;
    }

    public String getName(){
        return name;
    }
}