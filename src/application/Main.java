package application;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import entities.Product;

public class Main {
	public static void main(String[] args) {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("Trabalho-BD");
		MongoCollection<Document> collection = database.getCollection("Product");

		Scanner scan = new Scanner(System.in);
		int option = 0;

		while (option != 5) {
			System.out.println();
			System.out.println("------ ESTOQUE DE PRODUTOS ------");
			System.out.println("1. Adicionar produto (CREATE)");
			System.out.println("2. Ver produtos (READ)");
			System.out.println("3. Atualizar produto (UPDATE)");
			System.out.println("4. Excluir produto (DELETE)");
			System.out.println("5. Encerrar programa");
			System.out.println();

			System.out.print("Digite a opção desejada: ");
			option = scan.nextInt();
			System.out.println();

			switch (option) {
			case 1:
				scan.nextLine();
				System.out.print("Nome do produto: ");
				String name = scan.nextLine();
				System.out.print("Descrição: ");
				String description = scan.nextLine();
				System.out.print("Quantidade: ");
				int quantity = scan.nextInt();
				System.out.print("Preço: ");
				float price = scan.nextFloat();

				Document doc = new Document("nome", name).append("descrição", description)
						.append("quantidade", quantity).append("preco", price);
				collection.insertOne(doc);
				System.out.println("Produto adicionado com sucesso!");
				System.out.println();
				break;

			case 2:
				for (Document produto : collection.find()) {
					Product product = new Product(produto.getString("nome"), produto.getString("descrição"),
							produto.getInteger("quantidade"), produto.getDouble("preco").floatValue());
					System.out.println(product);
					System.out.println();
				}
				break;

			case 3:
				scan.nextLine();
				System.out.print("Nome do produto a ser atualizado: ");
				String updateName = scan.nextLine();

				Document foundProduct = collection.find(new Document("nome", updateName)).first();
				if (foundProduct != null) {
					System.out.print("Nova descrição: ");
					String newDescription = scan.nextLine();
					System.out.print("Nova quantidade: ");
					int newQuantity = scan.nextInt();
					System.out.print("Novo preço: ");
					float newPrice = scan.nextFloat();

					Document updateFilter = new Document("nome", updateName);
					Document updateOperation = new Document("$set", new Document("descrição", newDescription)
							.append("quantidade", newQuantity).append("preco", newPrice));
					collection.updateOne(updateFilter, updateOperation);
					System.out.println("Produto atualizado com sucesso!");
				} else {
					System.out.println("Produto não encontrado!");
				}
				break;

			case 4:
				scan.nextLine();
				System.out.print("Nome do produto a ser excluído: ");
				String deleteName = scan.nextLine();

				foundProduct = collection.find(new Document("nome", deleteName)).first();
				if (foundProduct != null) {
					Document deleteFilter = new Document("nome", deleteName);
					collection.deleteOne(deleteFilter);
					System.out.println("Produto excluído com sucesso!");
				} else {
					System.out.println("Produto não encontrado!");
				}
				break;

			case 5:
				System.out.println("Encerrando o programa...");
				break;

			default:
				System.out.println("Opção inválida! Tente novamente.");
			}
			System.out.println();
		}
		scan.close();
		mongoClient.close();
	}
}