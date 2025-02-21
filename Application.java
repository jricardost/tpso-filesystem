import java.lang.reflect.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application implements Constants {

	private static boolean exit = false;
	private static boolean saveFiles = false;
	private static boolean skipLogin = true;
	private static boolean displayMOTD = false;

	public String currentDirectory;
	public static User currentUser;
	public static VirtualFileSystem vfs;
	public static UserAccountController uac;

	public JoaoRicardo joaoRicardo;
	public Julia julia;
	public Mariana mariana;
	public Natan natan;
	public Rodrigo rodrigo;

	public Scanner input;

	public Application() {
		initialize();
	}

	public void main() {

		/* Ambiente */
		String cmdline;
		String[] arguments;

		while (!exit) {

			while (currentUser == null) {

				if (skipLogin) {
					currentUser = uac.getUser(0);
				} else {
					uac.login();
					currentUser = uac.getCurrentUser();
				}

				if (displayMOTD)
					Tools.motd();
				currentDirectory = currentUser.homeDir();
			}

			try {
				System.out.print(String.format("%s@tpso:%s $ ", currentUser.name(),
						(currentDirectory == currentUser.homeDir()) ? "~" : currentDirectory));
				cmdline = input.nextLine();
				arguments = cmdline.split(" ");

				if (arguments.length == 2 && arguments[1].contains("--help")) {
					Tools.help(arguments[0]);
				} else {
					execute(arguments);
				}

			} catch (NoSuchElementException e) { /* para sair ao pressionar CTRL + C */
				exit = true;
				break;
			}

			if (exit)
				break;

		}

		input.close();

		clear();

		if (saveFiles)
			vfs.save();

		System.exit(0);
	}

	private void initialize() {
		input = new Scanner(System.in);
		uac = new UserAccountController();
		vfs = new VirtualFileSystem(this, uac, 4096);

		joaoRicardo = new JoaoRicardo(this, uac, vfs);
		julia = new Julia(this, uac, vfs);
		mariana = new Mariana(this, uac, vfs);
		natan = new Natan(this, uac, vfs);
		rodrigo = new Rodrigo(this, uac, vfs);
	}

	public void execute(String... arguments) {
		Method method;
		Object obj;
		Class<?> owner;

		try {
			obj = findObjectOwner(arguments[0], String[].class);
			if (obj != null) {
				method = obj.getClass().getDeclaredMethod(arguments[0], String[].class);
				method.invoke(obj, (Object) arguments);
				return;
			} else {
				System.out.println("object == null");
			}

		} catch (NoSuchMethodException e) {
			System.out.println("method not found (obj)");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			owner = findMethodOwner(arguments[0], String[].class);

			if (owner != null) {
				method = owner.getDeclaredMethod(arguments[0], String[].class);

				if (Modifier.isStatic(method.getModifiers())) {
					method.invoke(owner, (Object) arguments);
				} else {
					method.invoke(this, (Object) arguments);
				}

			} else {
				throw new NoSuchMethodException();
			}

		} catch (NoSuchMethodException e) {
			System.out.println("method not found");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object findObjectOwner(String methodName, Class<?>... args) {

		Method method;
		Object[] users = { joaoRicardo, julia, mariana, natan, rodrigo };

		for (Object obj : users) {
			try {
				method = obj.getClass().getMethod(methodName, args);
				if (method != null)
					return obj;
			} catch (Exception e) {

			}
		}

		return null;
	}

	private Class<?> findMethodOwner(String methodName, Class<?>... args) {

		Method method;
		Class<?>[] interfaces = Application.class.getInterfaces();

		try {
			method = Application.class.getDeclaredMethod(methodName, args);
			if (method != null)
				return Application.class;
		} catch (Exception e) {

		}

		for (Class<?> intfs : interfaces) {

			try {
				method = intfs.getMethod(methodName, args);
				if (method != null)
					return intfs;
			} catch (Exception e) {

			}
		}

		return null;
	}

	public void login(String... args) {
		uac.login();
	}

	public void logout(String... args) {
		uac.logout();
		skipLogin = false;
	}

	public void clear(String... args) {
		Tools.clearScreen();
	}

	public void cls(String... args) {
		clear();
	}

	public void test(String... args) {

	}

	public String currentDirectory(String args) {
		return currentDirectory + "/" + args;
	}

	public void exit(String... args) {
		if (args.length > 1 && args[1].equals("save")) {
			saveFiles = true;
		}

		Application.exit = true;
	}
}