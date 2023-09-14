package Noobank_Features;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Besta_Teste_Release1 {

	private static final String DIRETORIO_ARQUIVO = "C:\\Users\\200029317\\Documents\\Guijas\\Noobank\\data";
	private static final String ARQUIVO_USUARIOS = DIRETORIO_ARQUIVO + "\\Usuarios.txt";
	private static Map<String, Usuario> usuarios = new HashMap<>();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static void main(String[] args) {
		criarDiretorioSeNaoExistir();
		carregarUsuarios();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Bem-vindo ao sistema de login!");
			System.out.print("Escolha uma opção: (1) Login, (2) Registro, (3) Sair: ");
			int escolha = scanner.nextInt();
			scanner.nextLine(); // Consumir a nova linha

			switch (escolha) {
			case 1:
				fazerLogin(scanner);
				break;
			case 2:
				fazerRegistro(scanner);
				break;
			case 3:
				salvarUsuarios();
				System.out.println("Saindo do sistema. Até logo!");
				System.exit(0);
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
			}
		}
	}

	private static void criarDiretorioSeNaoExistir() {
		File diretorio = new File(DIRETORIO_ARQUIVO);
		if (!diretorio.exists()) {
			if (diretorio.mkdirs()) {
				System.out.println("Diretório criado com sucesso.");
			} else {
				System.err.println("Não foi possível criar o diretório.");
			}
		}
	}

	private static void carregarUsuarios() {
		try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				String[] partes = linha.split(":");
				if (partes.length == 4) {
					String usuario = partes[0];
					String senha = partes[1];
					String email = partes[2];
					String dataNascimento = partes[3];
					Usuario novoUsuario = new Usuario(usuario, senha, email, dataNascimento);
					usuarios.put(usuario, novoUsuario);
				}
			}
		} catch (FileNotFoundException e) {
			// Arquivo não existe, nada a fazer
		} catch (IOException e) {
			System.err.println("Erro ao carregar usuários: " + e.getMessage());
		}
	}

	private static void salvarUsuarios() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
			for (Usuario usuario : usuarios.values()) {
				writer.println(usuario.getUsuario() + ":" + usuario.getSenha() + ":" + usuario.getEmail() + ":"
						+ usuario.getDataNascimento());
			}
		} catch (IOException e) {
			System.err.println("Erro ao salvar usuários: " + e.getMessage());
		}
	}

	private static void fazerLogin(Scanner scanner) {
		System.out.print("Digite o nome de usuário: ");
		String usuario = scanner.nextLine();
		System.out.print("Digite a senha: ");
		String senha = scanner.nextLine();

		if (usuarios.containsKey(usuario) && usuarios.get(usuario).getSenha().equals(senha)) {
			System.out.println("Login bem-sucedido!");
		} else {
			System.out.println("Usuário ou senha incorretos. Tente novamente.");
		}
	}

	private static void fazerRegistro(Scanner scanner) {
		System.out.print("Digite o nome de usuário desejado: ");
		String usuario = scanner.nextLine();
		System.out.print("Digite a senha desejada: ");
		String senha = scanner.nextLine();
		System.out.print("Digite o e-mail: ");
		String email = scanner.nextLine();

		// Verificar se o e-mail segue o formato especificado
		if (!validarFormatoEmail(email)) {
			System.out.println("Formato de e-mail inválido. O e-mail deve seguir o formato exemplo@ecom.br.");
			return;
		}

		System.out.print("Digite a data de nascimento (dd/MM/yyyy): ");
		String dataNascimento = scanner.nextLine();

		// Verificar se a data de nascimento segue o formato especificado
		if (!validarFormatoDataNascimento(dataNascimento)) {
			System.out.println("Formato de data de nascimento inválido. A data deve seguir o formato dd/MM/yyyy.");
			return;
		}

		if (usuarios.containsKey(usuario)) {
			System.out.println("Este nome de usuário já existe. Escolha outro.");
		} else {
			Usuario novoUsuario = new Usuario(usuario, senha, email, dataNascimento);
			usuarios.put(usuario, novoUsuario);
			System.out.println("Registro concluído com sucesso!");
		}
	}

	private static boolean validarFormatoEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private static boolean validarFormatoDataNascimento(String dataNascimento) {
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(dataNascimento);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}

class Usuario {
	private String usuario;
	private String senha;
	private String email;
	private String dataNascimento;

	public Usuario(String usuario, String senha, String email, String dataNascimento) {
		this.usuario = usuario;
		this.senha = senha;
		this.email = email;
		this.dataNascimento = dataNascimento;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getSenha() {
		return senha;
	}

	public String getEmail() {
		return email;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}
}