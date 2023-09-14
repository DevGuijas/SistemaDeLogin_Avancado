package Noobank_Features;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Beta_Testes {

	static Scanner sc = new Scanner(System.in);
	static Date dt = new Date();

	public static void main(String[] args) {

		System.out.println("(1) Sobre a conta.");

		System.out.print("\nSua escolha:");
		int escolha = sc.nextInt();

		switch (escolha) {

		case 1:
			Sobre_acc();
			break;
		default:
			System.out.println("Opção inválida!");
			break;
		}

	}

	public static void Sobre_acc() {

		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataNascimento = LocalDate.parse(dt.dataNascimentoStr, formatoData);

		DateTimeFormatter formatoExtenso = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
		String dataFormatada = dataNascimento.format(formatoExtenso);
		
		
		
		DateTimeFormatter formatoData2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataDeCriacao = LocalDate.parse(dt.DataDeCriacao, formatoData);

		DateTimeFormatter formatoExtenso2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
		String dataFormatada2 = dataDeCriacao.format(formatoExtenso);
		
		System.out.println("------------------------------------------------------");
		System.out.println("|               Informações do titular               |");
		System.out.println("------------------------------------------------------");

		System.out.println("\nNome completo: " + dt.nome + ".");
		System.out.println("Idade: " + dt.idade + ".");
		System.out.println("Data de Nascimento: " + dataFormatada + ".");
		System.out.println("Data de criação da conta: " + dataFormatada2 + ".");
		System.out.println("Nome da mãe do titular: " + dt.nomeDaMae + ".");

		sc.close();

	}

}
