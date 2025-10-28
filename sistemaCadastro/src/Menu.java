import java.io.IOException;


public class Menu {
    leitorArquivos leitor = new leitorArquivos();
    public void iniciar(){
        int opcao = 0;
        Pets pets = new Pets();
        do {
            
            System.out.println("======================================");
            System.out.println("1. Cadastrar um novo pet");
            System.out.println("2. Alterar os dados do pet cadastrado");
            System.out.println("3. Deletar um pet cadastrado");
            System.out.println("4. Listar todos os pets cadastrados");
            System.out.println("5. Listar pets por algum critério (idade, nome, raça)");
            System.out.println("6. Sair");
            System.out.println("======================================");
            
            try{
                opcao = Integer.parseInt(leitor.lerInput());
            }catch(IOException e){
                System.err.println(e);
            }

            switch (opcao) {
                case 1:
                    try {
                        pets.cadastrarPet();
                    } catch (Exception e) {
                        System.out.println("Impossível cadastrar o pet");
                    }


                    break;
                case 2:
                    //Alterar um dado do pet
                    try{
                        //pets.alterarCadastro();
                    }catch (Exception e){
                        System.out.println("Impossível alterar o cadastro do pet");
                    }

                    break;
                case 3:
                    //Deletar um pet da lista de cadastrados
                    

                    break;
                case 4:
                    //Listar todos os pets
                    try{
                        pets.BuscarCadastros();
                    }catch(Exception e){
                        System.out.println("Não há pets cadastrados");
                    }
                    break;
                case 5:
                    //Listar por critérios.
                    try{
                        pets.BuscarCadastros();
                    }catch(Exception e){
                        System.out.println("Não há pets cadastrados/Não há pets que atinjam os critérios.");
                    }

                    break;
                case 6:
                    //Terminar o loop do while
                    System.out.println("Encerrando...");
                    break;
            
                default:
                    System.out.println("Número inválido. Digite um número de 1 a 6.");


                    break;
            }


            
        } while (opcao != 6);

    }
}
