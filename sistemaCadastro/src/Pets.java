import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pets {
    leitorArquivos leitor = new leitorArquivos();
    private float peso,idade;
    private String nome, tipoAnimal, sexo, endereco, raca;
    
    //Constante do caminho do projeto.
    public static final String caminhoProjeto = "sistemaCadastro/src/petsCadastrados/";
    
    //Constante padrão para campos não informados:
    public static final String naoInformado = "Não informado";
    
    //Lista feita para manter os objetos pets acessíveis.
    public ArrayList<Pets> listaPets = new ArrayList<>();
    

    public Pets(String nome, String tipoAnimal, String sexo, String endereco, float idade, float peso, String raca){
        this.nome = nome;
        this.tipoAnimal = tipoAnimal;
        this.sexo = sexo;
        this.endereco = endereco;
        this.idade = idade;
        this.raca = raca;
    }

    public enum tipoPet{
        Gato,Cachorro;
    }

    public enum sexoPet{
        Masculino,Feminino;
    }


    public Pets(){
        //Inicializador vazio para leitura externa.
    }


    /*
     * A classe abaixo vai ler o formulario e criar os pets a partir das perguntas
     * *params: nenhum, pois vai ser criado dentro da classe
     * 
     * 
     */
    public void cadastrarPet() throws Exception{
        tempoCadastroPet tempo = new tempoCadastroPet();
        List<String> perguntasParticionadas = leitor.particionarArquivo("formulario");
        


        System.out.println("Nosso cadastro funciona com perguntas, para guardarmos com zelo seu amiguinho");
        System.out.println("Obs: Cada entrada deve ser em uma linha separada.");
        System.out.println("===========================================================");
        
        
        //Nome do pet
        System.out.println(perguntasParticionadas.get(0));
        setNome(leitor.lerInput());
        
        //Tipo do pet
        System.out.println(perguntasParticionadas.get(1));
        setTipoAnimal(leitor.lerInput());
        
        
        //Sexo do pet
        System.out.println(perguntasParticionadas.get(2));
        setSexo(leitor.lerInput());
        
        
        //Endereço
        System.out.println(perguntasParticionadas.get(3));
        String endereco = "";
        System.out.println("Rua:");
        endereco += leitor.lerInput() + ", ";
        System.out.println("Número:");
        String numeroEndereco = leitor.lerInput();
        
        if (numeroEndereco.isBlank()){
            numeroEndereco = naoInformado;
            endereco += numeroEndereco + ", ";
        }else{
            try {
                endereco += Integer.parseInt(numeroEndereco) + ", ";
            }catch (NumberFormatException e){
                throw new NumberFormatException(e.getMessage() + " -> Número informado não é um inteiro ou não é um número.");
            }
        }
        
        

        System.out.println("Cidade:");
        endereco += leitor.lerInput();
        setEndereco(endereco);
        
        
        //Idade
        System.out.println(perguntasParticionadas.get(4));
        String idadeString = leitor.lerInput();
        float idadeFormatada;
        if (idadeString.isBlank()){
            idadeFormatada = -1; //Erro de input, tratado no método SET
        }else{
            try {
                idadeFormatada = Float.parseFloat(idadeString);
            }catch (NumberFormatException e){
                throw new NumberFormatException(e.getMessage() + " -> Número informado não é um float ou não é um número.");
            }
        }
        setIdade(idadeFormatada);

        //Peso
        System.out.println(perguntasParticionadas.get(5));
        String pesoString = leitor.lerInput();
        float pesoFormatado;
        if(pesoString.isBlank()){
            pesoFormatado = -1; //Erro de input, tratado no método SET
        }else{
            try{
                pesoFormatado = Float.parseFloat(pesoString);
            }catch (NumberFormatException e){
                throw new NumberFormatException(e.getMessage() + " -> Número informado não é um float ou não é um número.");
            }
        }
        setPeso(pesoFormatado);

        //Raça
        System.out.println(perguntasParticionadas.get(6));
        setRaca(leitor.lerInput());

        

        //Formatação final das variáveis que são o corpo do arquivo do pet
        String nomeFinal = getNome();
        String tipoFinal = getTipoAnimal();
        String sexoFinal = getSexo();
        String enderecoFinal = getEndereco();
        String idadeFinal = formatarIdade();
        String pesoFinal = formatarPeso();
        String racaFinal = getRaca();
        
        
        //20231101T1234-FLORZINHADASILVA.TXT
        String caminhoNovoPet = criarCaminhoNovoPet(criarNomeArquivoPet(tempo, nomeFinal));
        
        //Preenchendo arquivo.
        String conteudoArquivoPet = formatarConteudoCadastro(nomeFinal,tipoFinal, sexoFinal, enderecoFinal, idadeFinal, pesoFinal, racaFinal);
        
        //Nome + conteudo
        criarArquivoPet(caminhoNovoPet, conteudoArquivoPet);

        
        
        //Adicionando o novo pet na lista para registrarmos e ficar mais simples de puxarmos.
        //Mantendo getIdade e getPeso pois cada um sofreu formatações na versão final.
        Pets novoPet = new Pets(nomeFinal, tipoFinal, sexoFinal, enderecoFinal, getIdade(), getPeso(), racaFinal);
        listaPets.add(novoPet);

    }


    //Método para formatar a criação do nome do arquivo.
    private String criarNomeArquivoPet(tempoCadastroPet tempoCriacao, String nome){
        return tempoCriacao.AnoMesDia() + "T" + tempoCriacao.Horario() + "-" + nome.toUpperCase().replaceAll("\\s+", "") + ".TXT";
    }


    private String criarCaminhoNovoPet(String nomeArquivo){
        return caminhoProjeto + nomeArquivo;
    }


    private String formatarConteudoCadastro(String nome, String tipo, String sexo, String endereco, String idade, String peso, String raca){
        return 
        "1 - " + nome 
        + "\n2 - " + tipo 
        + "\n3 - " + sexo 
        + "\n4 - " + endereco + 
        "\n5 - " + idade + 
        "\n6 - " + peso + 
        "\n7 - " + raca;
    }

    private void criarArquivoPet(String caminhoArquivo, String conteudoArquivo){
        Path path = Paths.get(caminhoArquivo);
        try{
            // Cria pastas através do caminho
            if(Files.notExists(path.getParent())){
                Files.createDirectories(path.getParent());
            }

            //Cria o arquivo SE ele não existir
            if(Files.notExists(path)){
                Files.createFile(path);
                System.out.println("Registro criado em: " + path);
            }
            
            //Finalmente escreve no arquivo :D
            Files.write(path, Collections.singletonList(conteudoArquivo), StandardOpenOption.APPEND);
            System.out.println("Texto gravado com sucesso!");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void BuscarCadastros(){
        leitor.listarCadastrados("","");
    }

    
    
    public void BuscarCadastrosComCriterios() throws Exception{
        String tipo;
        String opcao;
        List<String> escolhas = new ArrayList<>(Arrays.asList("Nome","Sexo","Idade","Peso","Raça","Endereço"));
        int iPosicao = 0;
        
        System.out.println("Antes de procurarmos, qual é o tipo do animal?(Gato,Cachorro)");
        tipo = leitor.lerInput();

        if(tipo.toLowerCase().equals("Gato".toLowerCase()) || tipo.toLowerCase().equals("Cachorro".toLowerCase())){
            System.out.println("Deseja pesquisar por 1 ou 2 critérios de busca?");
            opcao = leitor.lerInput();
            if (opcao.equals("1")){
                System.out.println("Escreva qual critério você quer utilizar, entre: ");
                for (; iPosicao < escolhas.size();iPosicao++) {
                    System.out.println((iPosicao+1) + ". " + escolhas.get(iPosicao)); //iposicao+1 para ficar '1.', '2.' ...
                }

                String criterio = leitor.lerInput();
                if(!escolhas.contains(criterio)){
                    throw new Exception(criterio+" não é válido dentro de escolhas");
                }
                
                System.out.print("Comparar " + criterio + " com: ");
                String input = leitor.lerInput();
                

                System.out.println();
                for (String filtro : escolhas) {
                    if(filtro.equals(criterio)){
                        leitor.listarCadastrados(criterio,input);
                        break;
                    }
                    iPosicao++;
                }

            }
            
            else if(opcao.equals("2")){
                System.out.println("Escreva qual critério você quer utilizar, entre: ");
                for (iPosicao = 0; iPosicao < escolhas.size();iPosicao++) {
                    System.out.println((iPosicao+1) + ". " + escolhas.get(iPosicao));
                }
                String filtroPrimario = leitor.lerInput();
                
                
                if(!escolhas.contains(filtroPrimario)){
                    throw new Exception("Critério não corresponde aos explicitados");
                }
                
                escolhas.remove(filtroPrimario);
                System.out.println("Agora, o outro critério: ");
                


                for (iPosicao = 0; escolhas.get(iPosicao) == null;iPosicao++) {
                    System.out.println((iPosicao+1) + ". " + escolhas.get(iPosicao));
                }
                
                String filtroSecundario = leitor.lerInput();
                
                if(!escolhas.contains(filtroSecundario)){
                    throw new Exception("Critério não corresponde aos explicitados");
                }

                System.out.println("Comparar " + filtroPrimario + " com: ");
                String entrada1 = leitor.lerInput();
                System.out.println("E comparar " + filtroSecundario + " com: ");
                String entrada2 = leitor.lerInput();

                leitor.listarCadastrados(filtroPrimario, filtroSecundario, entrada1, entrada2);
                


            }else{
                throw new Exception("Escolha um valor válido de critérios");
            }
            
        }
        else{
            throw new Exception("Tipo especificado não existe");
        }
    }

    /**
     * Método auxiliar para SOBRESCREVER um arquivo com o novo conteúdo.
     */
    private void sobrescreverArquivoPet(Path caminho, List<String> linhas) throws IOException {
        // TRUNCATE_EXISTING apaga o conteúdo antigo antes de escrever.
        Files.write(caminho, linhas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        System.out.println("Cadastro alterado com sucesso!");
    }



    public void alterarCadastro() throws Exception{
        
        System.out.println("Para alterar um cadastro, precisamos que você selecione o cadastro que quer alterar.");
        BuscarCadastros();
        
        System.out.println("Para selecionar um cadastro, digite o nome do pet exatamente como está escrito");
        String nomeBuscado = leitor.lerInput();

        try {
            List<String> resultadoBusca = leitor.BuscarArquivoEspecifico(nomeBuscado);

            // Verificação de segurança
            if (resultadoBusca == null || resultadoBusca.size() < 2) {
                System.err.println("Não foi possível encontrar o cadastro.");
                return;
            }

            String caminhoString = resultadoBusca.get(0);
            String conteudoCompleto = resultadoBusca.get(1);

            // Converte o caminho String para Path
            Path caminhoDoArquivo = Paths.get(caminhoString);
            
            // Converte o CONTEÚDO (String única) para uma LISTA DE LINHAS
            // \\r?\\n para funcionar no Windows e Linux/Mac.
            List<String> linhasDoArquivo = new ArrayList<>(Arrays.asList(conteudoCompleto.split("\\r?\\n")));

            System.out.println("Em que linha você quer alterar? (1-7)");
            int linhaEscolhida = Integer.parseInt(leitor.lerInput());

            if (linhaEscolhida < 1 || linhaEscolhida > 7) {
                System.err.println("Linha inválida. Deve ser entre 1 e 7.");
                return;
            }

            Pets validador = new Pets();
            String linhaFormatada = ""; // A nova string formatada para o arquivo
            
            switch (linhaEscolhida) {
                case 1: // Nome
                    System.out.println("Digite o novo Nome:");
                    String novoNome = leitor.lerInput();
                    validador.setNome(novoNome); // Validação
                    linhaFormatada = "1 - " + validador.getNome(); // Formatação
                    break;
                
                case 2: // Tipo
                    System.out.println("Digite o novo Tipo (Gato ou Cachorro):");
                    String novoTipo = leitor.lerInput();
                    validador.setTipoAnimal(novoTipo); 
                    linhaFormatada = "2 - " + validador.getTipoAnimal(); 
                    break;
                
                case 3: // Sexo
                    System.out.println("Digite o novo Sexo (Masculino ou Feminino):");
                    String novoSexo = leitor.lerInput();
                    validador.setSexo(novoSexo); 
                    linhaFormatada = "3 - " + validador.getSexo(); 
                    break;

                case 4: // Endereço
                    System.out.println("Digite o novo Endereço:");
                    String endereco = "";
                    System.out.println("Rua:");
                    endereco += leitor.lerInput() + ", ";
                    
                    System.out.println("Número:");
                    String numeroEndereco = leitor.lerInput();
                    
                    if (numeroEndereco.isBlank()){
                        endereco += naoInformado + ", ";
                    } else {
                        try {
                            endereco += Integer.parseInt(numeroEndereco) + ", ";
                        } catch (NumberFormatException e){
                            throw new NumberFormatException("Número informado não é um inteiro.");
                        }
                    }
                    System.out.println("Cidade:");
                    endereco += leitor.lerInput();
                    
                    validador.setEndereco(endereco); 
                    linhaFormatada = "4 - " + validador.getEndereco();
                    break;

                case 5: // Idade
                    System.out.println("Digite a nova Idade:");
                    String idadeString = leitor.lerInput();
                    float idadeFormatada;
                    if (idadeString.isBlank()){
                        idadeFormatada = -1;
                    } else {
                        try {
                            idadeFormatada = Float.parseFloat(idadeString);
                        } catch (NumberFormatException e){
                            throw new NumberFormatException("Idade informada não é um número.");
                        }
                    }
                    validador.setIdade(idadeFormatada); // Validação
                    linhaFormatada = "5 - " + validador.formatarIdade(); // Formatação
                    break;

                case 6: // Peso
                    System.out.println("Digite o novo Peso:");
                    String pesoString = leitor.lerInput();
                    float pesoFormatado;
                    if(pesoString.isBlank()){
                        pesoFormatado = -1;
                    } else {
                        try{
                            pesoFormatado = Float.parseFloat(pesoString);
                        } catch (NumberFormatException e){
                            throw new NumberFormatException("Peso informado não é um número.");
                        }
                    }
                    validador.setPeso(pesoFormatado); // Validação
                    linhaFormatada = "6 - " + validador.formatarPeso(); // Formatação
                    break;

                case 7: // Raça
                    System.out.println("Digite a nova Raça:");
                    String novaRaca = leitor.lerInput();
                    validador.setRaca(novaRaca); // Validação 
                    linhaFormatada = "7 - " + validador.getRaca(); // Formatação
                    break;
            }
            
            // Atualiza a lista de linhas
            linhasDoArquivo.set(linhaEscolhida - 1, linhaFormatada);

            // Sobrescreve o arquivo no disco
            sobrescreverArquivoPet(caminhoDoArquivo, linhasDoArquivo);

        } catch (Exception e) {
            // Captura falhas de validação
            System.err.println("Falha ao alterar o cadastro: " + e.getMessage());
        }
    }


    public float getIdade() {
        return idade;
    }



    public void setIdade(float idade) throws Exception{
        if(idade > 20){
            throw new Exception("Idade maior que 20kgs");
        }else if (idade < 1){
            idade = -2; //Idade é 0.x anos
        }
        
        
        
        this.idade = idade;
    }


    private String formatarIdade(){
        if(getIdade() == -1){ //Idade não informada no cadastro
            return naoInformado;
        }else if (getIdade() == -2){ //Idade menor que um ano
            return "0.x anos";
        }else{
            return getIdade() + " anos";
        }
    }

    public float getPeso() {
        return peso;
    }



    public void setPeso(float peso) throws Exception{
        //Tratando inputs máximos e min
        if (peso > 60){
            throw new Exception("Peso acima de 60kgs");
        }else if (peso < 0.5){
            throw new Exception("Peso abaixo de 0.5kgs");
        }

        this.peso = peso;
    }

    private String formatarPeso(){
        return (getPeso() == -1) ? naoInformado : String.valueOf(getPeso()) + " kgs";
    }

    public String getNome() {
        return nome;
    }



    public void setNome(String nome) throws Error{
        //Tratamento segundo as regras de nome, para não ter caracteres
        //especiais nem nome em branco.
        
        boolean temCharEspecial = !nome.matches("[A-Za-z ]+");

        if(nome.isBlank()){
            nome = naoInformado;
        }else if(temCharEspecial){
            throw new Error("Nome não pode ter caracteres especiais.");
        }
        
        
        this.nome = nome;
    }



    public String getTipoAnimal() {
        return tipoAnimal;
    }



    public void setTipoAnimal(String tipoAnimal) {
        try {
            tipoPet.valueOf(tipoAnimal);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "-> Tipo não corresponde com os cadastrados: " + tipoPet.values());
        }
        
        
        this.tipoAnimal = tipoAnimal;
    }



    public String getSexo() {
        return sexo;
    }



    public void setSexo(String sexo) {
        try {
            sexoPet.valueOf(sexo);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage() + " -> Sexo não corresponde com os cadastrados: " + sexoPet.values());
        }
        
        
        this.sexo = sexo;
    }



    public String getEndereco() {
        return endereco;
    }



    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }



    public String getRaca() {
        return raca;
    }



    public void setRaca(String raca) {
        boolean temCharEspecial = !raca.matches("[A-Za-z ]+");
        
        if(raca.isBlank()){
            raca = naoInformado;
        }else if(temCharEspecial){
            throw new Error("Raça não pode ter caracteres especiais");
        }
        
        this.raca = raca;
    } 

    
}
