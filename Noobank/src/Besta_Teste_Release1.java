package Noobank_Features;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Besta_Teste_Release1 {

	private static final String DIRETORIO_ARQUIVO = "C:\\Users\\200029317\\Documents\\Guijas\\Noobank\\data";
	private static final String ARQUIVO_USUARIOS = DIRETORIO_ARQUIVO + "\\Usuarios.txt";
	private static Map<String, Usuario> usuarios = new HashMap<>();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final String EMAIL_SERVIDOR = "seu_email@gmail.com"; // Substitua com seu email
	private static final String SENHA_EMAIL = "sua_senha"; // Substitua com sua senha
	private static final String ASSUNTO_EMAIL = "Confirmação de Registro";

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
			enviarEmailConfirmacao(email);
			System.out.println("Registro concluído com sucesso! Um e-mail de confirmação foi enviado.");
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

	private static void enviarEmailConfirmacao(String emailDestinatario) {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com"); // Altere para o seu servidor SMTP
		properties.put("mail.smtp.port", "587"); // Altere para a porta do seu servidor SMTP

		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_SERVIDOR, SENHA_EMAIL);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(EMAIL_SERVIDOR));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
			message.setSubject(ASSUNTO_EMAIL);
			message.setText("Seu registro foi concluído com sucesso!");

			Transport.send(message);

			System.out.println("E-mail de confirmação enviado com sucesso.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
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