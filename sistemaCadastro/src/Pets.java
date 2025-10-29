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
    //Constante padrão para campos não informados:
    public static final String naoInformado = "Não informado";
    private float peso,idade;
    private String nome, tipoAnimal, sexo, endereco, raca;
    
    

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
        String conteudoArquivoPet = "";
        String nomeFinal = getNome();
        String tipoFinal = getTipoAnimal();
        String sexoFinal = getSexo();
        String enderecoFinal = getEndereco();
        String idadeFinal;

        if(getIdade() == -1){ //Idade não informada no cadastro
            idadeFinal = naoInformado;
        }else if (getIdade() == -2){ //Idade menor que um ano
            idadeFinal = "0.x anos";
        }else{
            idadeFinal = getIdade() + " anos";
        }


        String pesoFinal = (getPeso() == -1) ? naoInformado : String.valueOf(getPeso()) + " kgs";
        String racaFinal = getRaca();

        conteudoArquivoPet += "1 - " + nomeFinal + "\n2 - ";
        conteudoArquivoPet += tipoFinal + "\n3 - ";
        conteudoArquivoPet += sexoFinal + "\n4 - ";
        conteudoArquivoPet += enderecoFinal + "\n5 - ";
        conteudoArquivoPet += idadeFinal + "\n6 - ";
        conteudoArquivoPet += pesoFinal + "\n7 - ";
        conteudoArquivoPet += racaFinal;
        
        
        
        //`20231101T1234-FLORZINHADASILVA.TXT
        String nomeArquivoPet = tempo.AnoMesDia() + "T" + tempo.Horario() + "-" + getNome().toUpperCase().replaceAll("\\s+", "") + ".TXT";
        String caminhoNovoPet = "sistemaCadastro/src/petsCadastrados/" + nomeArquivoPet;

        Path path = Paths.get(caminhoNovoPet);
        try{
            // Cria pastas através do caminh
            if(Files.notExists(path.getParent())){
                Files.createDirectories(path.getParent());
            }

            //Cria o arquivo SE ele não existir
            if(Files.notExists(path)){
                Files.createFile(path);
                System.out.println("Registro criado em: " + path);
            }
            
            //Finalmente escreve no arquivo :D
            Files.write(path, Collections.singletonList(conteudoArquivoPet), StandardOpenOption.APPEND);
            System.out.println("Texto gravado com sucesso!");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void BuscarCadastros() throws Exception{
        leitor.listarCadastrados("",""); //Retorna todos.
    }
    
    
    
    
    public void BuscarCadastrosComCritério() throws Exception{
        System.out.println("Deseja pesquisar por 1 ou 2 critérios de busca?");
        String opcao = leitor.lerInput();
        if (opcao.equals("1")){
            List<String> escolhas = new ArrayList<>(Arrays.asList("Nome","Sexo","Idade","Peso","Raça","Endereço"));
            

            System.out.println("Escreva qual critério você quer utilizar, entre: ");
            System.out.println(escolhas);
            int iPosicao = 0;
            for (; escolhas.get(iPosicao) == null;iPosicao++) {
                System.out.println(iPosicao + ". " + escolhas.get(iPosicao));
            }

            String criterio = leitor.lerInput();
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

        }else if(opcao.equals("2")){

        }else{
            throw new Exception("Escolha um valor válido de critérios");
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
        boolean temCharEspecial = !nome.matches("[A-Za-z ]+");
        
        if(raca.isBlank()){
            raca = naoInformado;
        }else if(temCharEspecial){
            throw new Error("Raça não pode ter caracteres especiais");
        }
        
        this.raca = raca;
    } 

    
}
