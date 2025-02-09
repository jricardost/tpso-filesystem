class User {
    private int UID;
    private String name;
    private String passwd;
    private String homeDir;


    public User(String name, String passwd, int uid, String homeDir){
        this.UID = uid;
        this.name = name;
        this.passwd = passwd;
        this.homeDir = homeDir;
    }
}