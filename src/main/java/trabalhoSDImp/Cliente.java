package trabalhoSDImp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import trabalhoSD.Aresta;
import trabalhoSD.GrafoInterface;
import trabalhoSD.Vertice;
import trabalhoSD.notFaulException;


/**
 * 
 * 
 * 
 * @author Tafarel, Augusto
 *
 */
public class Cliente {

	public static void main(String[] args) {
		try {

			Scanner scan = new Scanner(System.in);
			scan.useLocale(Locale.US);
			TTransport transporte;

			String ip = "";
			int porta = 9999;

			if (args.length == 0) {
				System.out.println("Passe o endereço IP do servidor:");
				ip = scan.nextLine();

				System.out.println("Informe o número da porta:");
				porta = scan.nextInt();
				scan.nextLine();
				transporte = new TSocket(ip, porta);
			} else
				transporte = new TSocket(args[0], Integer.parseInt(args[1]));

			transporte.open();

			TProtocol protocol = new TBinaryProtocol(transporte);
			GrafoInterface.Client client = new GrafoInterface.Client(protocol);

			int menu = 1;

			while (menu != 0) {

				System.out.println("1- Remove Aresta");
				System.out.println("2- Busca Aresta");
				System.out.println("3- Atualiza Aresta");
				System.out.println("4- Busca Vértice");
				System.out.println("5- Adicionar Aresta");
				System.out.println("6- Listar Arestas de um vértice");
				System.out.println("7- Adicionar Vértice");
				System.out.println("8- Lista Arestas");
				System.out.println("9- Atualiza Vértice");
				System.out.println("10- Remover Vértice");
				System.out.println("12- Lista os vizinhos de um vértice");
				System.out.println("11- Lista Vértices");
				System.out.println("0- Sair");

				menu = scan.nextInt();
				scan.nextLine();

				int identif;
				int identif1;
				int cor;
				String desc;
				int flag;
				double peso;
				Vertice v1;
				Vertice v2 = null;
				Aresta aresta;
				Vertice v;
				switch (menu) {
				case 1:
					try {
						System.out.print("#  Informe os identificadores da aresta: ");
						identif = scan.nextInt();
						identif1 = scan.nextInt();
						scan.nextLine();

						if (!client.removeAresta(identif, identif1)) {
							System.out.println("Erro Excluir Aresta");

						} else {
							System.out.println("Sucesso");

						}

					} catch (notFaulException nfe) {
						System.out.println("Não encontrado");
					} catch (InputMismatchException e) {
						System.out.println("Parâmetro errado");
					}
					break;
				case 2:
					try {
						System.out.print("#  Digite os identificadores");
						identif = scan.nextInt();
						identif1 = scan.nextInt();
						scan.nextLine();

						aresta = client.buscaAresta(identif, identif1);
						if (aresta != null) {
							System.out.println(aresta.toString());
						}
					} catch (notFaulException nfe) {
						System.out.println("Não encontrado");

					} catch (InputMismatchException e) {
						System.out.println("Parâmetro errado");

					}
					break;
				case 3:
					try {
						System.out.print("Digite os identificadores");
						identif = scan.nextInt();
						identif1 = scan.nextInt();
						scan.nextLine();

						aresta = client.buscaAresta(identif, identif1);

						if (aresta != null) {
							System.out.print("Digite o novo peso: ");
							peso = scan.nextDouble();
							scan.nextLine();

							System.out.print("#  Digite a nova descrição");
							desc = scan.nextLine();

							aresta.setDescricao(desc);
							aresta.setPeso(peso);

							if (client.atualizarAresta(aresta)) {
								System.out.println("Sucesso");

							} else {
								System.out.println("Falha");

							}
						}
					} catch (notFaulException ex) {
						System.out.println("Não encontrado");

					} catch (InputMismatchException e) {
						System.out.println("Parâmetro  errado");
					}
					break;

				case 4:
					try {
						System.out.print("#  Digite o identificador do vértice: ");
						identif = scan.nextInt();
						scan.nextLine();

						v = client.buscaVertice(identif);

						if (v != null) {
							System.out.println(v.toString());
						}
					} catch (notFaulException ex) {
						System.out.println("Nao encontrado");

					} catch (InputMismatchException e) {
						System.out.println("Parametro  errado");

					}
					break;
	
				case 5:
					int menuAresta = 0;
					while (menuAresta != 3) {
						System.out.println("1- Criar Aresta apartir de vértices existentes      ");
						System.out.println("2- Criar Aresta apartir de vértices novos           ");
						System.out.println("3- Sair                                             ");
						menuAresta = scan.nextInt();
						scan.nextLine();
						switch (menuAresta) {
						case 1:
							try {
								System.out.print("Digite o identificador do Primeiro Vertice");
								identif = scan.nextInt();
								scan.nextLine();
								v1 = client.buscaVertice(identif);
								System.out.print("Digite o identificador do segundo Vertice");
								identif1 = scan.nextInt();
								scan.nextLine();
								System.out.print("Digite o peso da Aresta");
								peso = scan.nextDouble();
								scan.nextLine();
								System.out.print("E direcionado  (1- Sim / 2- Não)");
								flag = scan.nextInt();
								scan.nextLine();
								System.out.print("Digite a descrição da aresta");
								desc = scan.nextLine();

								if (flag == 1) {
									aresta = new Aresta(v1, v2, peso, true, desc);

								} else {
									aresta = new Aresta(v1, v2, peso, false, desc);
								}
								if (client.addAresta(aresta)) {
									System.out.println("Sucesso ");

								} else {
									System.out.println("Erro ao criar aresta");

								}
								menuAresta = 3;
							} catch (notFaulException e) {
								System.out.println("Vértice não encontrado ");

							} catch (InputMismatchException e) {
								System.out.println("Parâmetro  errado");
							}
							System.out.println();
							break;

						case 2:
							try {
								System.out.println("Primeiro Vértice");
								System.out.print("Digite o identificador do primeiro vértice");
								identif = scan.nextInt();
								scan.nextLine();

								System.out.print("Digite a cor do primeiro vértice");
								cor = scan.nextInt();
								scan.nextLine();

								System.out.print("#  Digite a descrição do primeiro vértice");
								desc = scan.nextLine();

								System.out.print("#  Digite o peso do primeiro vértice");
								peso = scan.nextDouble();
								scan.nextLine();

								v1 = new Vertice(identif, cor, desc, peso);

								System.out.println("Segundo Vértice");
								System.out.print("#  Digite o identificador do segundo vértice: ");
								identif = scan.nextInt();
								scan.nextLine();

								System.out.print("#  Digite a cor do segundo vértice: ");
								cor = scan.nextInt();
								scan.nextLine();

								System.out.print("#  Digite a descrição do segundo vértice: ");
								desc = scan.nextLine();

								System.out.print("#  Digite o peso do segundo vértice: ");
								peso = scan.nextDouble();
								scan.nextLine();

								v2 = new Vertice(identif, cor, desc, peso);
								client.addVertice(v2);
								client.addVertice(v1);

								System.out.print("#  Digite o peso da Aresta: ");
								peso = scan.nextDouble();
								scan.nextLine();

								System.out.print("#  Aresta é direcionada? (1- Sim / 2- Não): ");
								flag = scan.nextInt();
								scan.nextLine();

								System.out.print("Digite a descrição da aresta: ");
								desc = scan.nextLine();
								if (flag == 1) {
									aresta = new Aresta(v1, v2, peso, true, desc);

								} else {
									aresta = new Aresta(v1, v2, peso, false, desc);
								}
								if (client.addAresta(aresta)) {
									System.out.println("Sucesso");

								} else {
									System.out.println("Falha");
								}
								menuAresta = 3;
							} catch (InputMismatchException e) {
								System.out.println("Parametro  errado");

							}
							break;
						case 3:
							break;
						default:
							break;
						}
					}
					break;
				case 6:
					System.out.print("Digite o identificador do vértice: ");
					identif = scan.nextInt();
					scan.nextLine();
					try {
						ArrayList<Aresta> arestasV = (ArrayList<Aresta>) client.arestasDoVertice(identif);
						for (Aresta ares : arestasV)
							System.out.println(ares.toString());

					} catch (notFaulException ex) {
						System.out.println("Não possui aresta que pertence a esse vértice");
					} catch (Exception e) {
						System.out.println("falha durante a operação");
					}
					break;

				case 7:
					try {
						System.out.print("Digite o identificador do vértice");
						identif = scan.nextInt();
						scan.nextLine();

						System.out.print("Digite a cor do vértice");
						cor = scan.nextInt();
						scan.nextLine();

						System.out.print("Digite o peso do vértice");
						peso = scan.nextDouble();
						scan.nextLine();

						System.out.print("Digite a descrição do vértice: ");
						desc = scan.nextLine();

						v = new Vertice(identif, cor, desc, peso);

						if (client.addVertice(v)) {
							System.out.println("Sucesso");

						} else {
							System.out.println("Falha ");
						}
						System.out.println();
					} catch (InputMismatchException e) {
						System.out.println("Parametro errado");
					}
					break;
				case 8:
					try {
						ArrayList<Aresta> arestas = (ArrayList<Aresta>) client.listaArestas();
						for (Aresta a : arestas)
							System.out.println(a.toString());

					} catch (notFaulException ex) {
						System.out.println("Não existem");

					}
					break;
				case 9:
					try {
						System.out.print("Digite o identificador do vértice");
						identif = scan.nextInt();
						scan.nextLine();

						v = client.buscaVertice(identif);

						if (v != null) {
							System.out.print("Digite a cor do vértice");
							cor = scan.nextInt();
							scan.nextLine();

							System.out.print("Digite o peso do vértice");
							peso = scan.nextDouble();
							scan.nextLine();

							System.out.print("Digite a nova descrição do vértice");
							desc = scan.nextLine();

							v.setCor(cor);
							v.setDescricao(desc);
							v.setPeso(peso);

							if (client.atualizarVertice(v)) {
								System.out.println("Sucesso");

							} else {
								System.out.println("Falha ao atualizar Vértice");
							}
						}
					} catch (notFaulException nfe) {
						System.out.println("Nao encontrado");

					} catch (InputMismatchException e) {
						System.out.println("Parametro  errado");

					}

					break;
				case 10:
					try {
						System.out.print("#  Digite o identificador do vértice: ");
						identif = scan.nextInt();
						scan.nextLine();

						if (!client.removeVertice(identif)) {
							System.out.println(" Não foi possível excluir");

						} else {
							System.out.println("Sucesso");

						}

					} catch (notFaulException nfe) {
						System.out.println("Não encontrado");

					} catch (InputMismatchException e) {
						System.out.println("Parâmetro  errado");

					}
					break;
				case 11:
					System.out.print("Digite o identificador do vértice");
					identif = scan.nextInt();
					scan.nextLine();

					try {
						ArrayList<Vertice> vts = (ArrayList<Vertice>) client.verticesVizinhos(identif);
						for (Vertice vs : vts)
							System.out.println(vs.toString());

						System.out.println();
					} catch (notFaulException nfe) {
						System.out.println("Vértice não possui vizinhos");
					} catch (Exception e) {
						System.out.println("Ocorreu alguma falha");

					}
					break;

				case 12:

					try {
						ArrayList<Vertice> vertices = (ArrayList<Vertice>) client.listaVertices();
						for (Vertice vert : vertices)
							System.out.println(vert.toString());

						System.out.println();
					} catch (notFaulException nfe) {
						System.out.println("Não existem vértices");

					} catch (InputMismatchException e) {
						System.out.println("Parâmetro  errado ");

					}
					break;

				case 0:
					break;
				default:
					break;

				}
			}
			scan.close();
			transporte.close();
		} catch (TException x) {
			x.printStackTrace();
		}
	}
}
