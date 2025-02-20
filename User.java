public class User {
    private int UID;
    public String name;
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

    public int id(){
        return UID;
    }

    public String name(){
        return name;
    }

    public String password(){
        return passwd;
    }

    public String homeDir(){
        return homeDir;
    }
}