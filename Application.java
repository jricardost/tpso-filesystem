public class Application {

	public static void main(String[] args){

		VirtualFileSystem vfs = new VirtualFileSystem(4096);
		VirtualFileSystem.Inode node = vfs.new Inode(0, 0);

		node.setName("documento.txt");
		node.setAbsolutePath("/");
		node.setPermissions(777);
		node.dump();
	}

	}