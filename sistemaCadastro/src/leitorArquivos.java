import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class leitorArquivos {
    private String caminhoFormulario = "sistemaCadastro/src/formulario.txt";
    private BufferedReader leitor;
    
    public String getCaminhoFormulario() {
        return caminhoFormulario;
    }

    public void setCaminhoFormulario(String caminhoFormulario) {
        this.caminhoFormulario = caminhoFormulario;
    }

    public leitorArquivos(){ //Agora é fácil de deixar flexível o leitor.
        this.leitor = new BufferedReader(new InputStreamReader(System.in));
    }

    public leitorArquivos (String caminhoArquivo) throws IOException{
        this.leitor = new BufferedReader(new FileReader(caminhoArquivo));
    }


    /*
     * A classe abaixo retorna a leitura de um arquivo x,
     * com erros caso ocorra um funcionamento fora do esperado
     * pela função File reader.
     * 
     * 
     */
    
    public String nomeCaminho(String nome){
        if (nome.equals("formulario")){
            return getCaminhoFormulario();    
        }
        return nome;
    }
    
    public float virgulaParseParaFloat(String entrada){
        float valorFloat = 0;

        if(entrada.contains(",")){
            try{
                NumberFormat formatoPadrao = NumberFormat.getInstance(Locale.getDefault());
                Number numeroPadrao = formatoPadrao.parse(entrada);
                valorFloat = numeroPadrao.floatValue();
            } catch (ParseException e){
                System.err.println("Erro dando parse no padrão local: " + e.getMessage());
            }
        }else{
            valorFloat = Float.parseFloat(entrada);
        }
        
        return valorFloat;
    }

    public String lerTodoArquivo(String caminho) throws Exception{
        String conteudo = "";

        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeCaminho(caminho)))) { 
            String linha;
            while((linha = leitor.readLine()) != null){
                conteudo += linha + "\n";
            }
        }
        return conteudo.trim();
    }

    public String lerInput() throws IOException{
        return leitor.readLine();
    }
    
    //Serve para ler por linhas cada arquivo (útil para splitar)
    public List<String> particionarArquivo(String caminhoArquivo) throws IOException{
        List<String> entrada = new ArrayList<>();
        caminhoArquivo = nomeCaminho(caminhoArquivo);
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo))) { 
            String linha = leitor.readLine();
            while(linha != null){
                entrada.add(linha.trim());
                linha = leitor.readLine();
            }
        } catch (Exception erro) {
            
            throw new IOException("Erro ao ler o arquivo: " + caminhoArquivo,erro); //Vai ser utilizado caso não exista o pet cadastrado.
        }
        
        return entrada;
    }

    public void listarCadastrados(String filtro, String input){
        Path pasta = Paths.get("sistemaCadastro/src/petsCadastrados");
        
        if((filtro.isBlank()) && (input.isBlank())){
            try(Stream<Path> arquivos = Files.list(pasta)){
                arquivos
                    .filter(Files::isRegularFile) //Se de fato é um arquivo
                    .forEach(System.out::println);

            }catch(IOException e){
                System.out.println("Não há cadastros.");
            }
        }

        
        
        if(filtro.isBlank()){
            try(Stream<Path> arquivos = Files.list(pasta)){
                arquivos
                    .filter(Files::isRegularFile) //Se de fato é um arquivo
                    .forEach(System.out::println);

            }catch(IOException e){
                System.out.println("Não há cadastros.");
            }
        }else{
            if(filtro.equals("Nome")){
                try(Stream<Path> arquivos = Files.list(pasta)){
                arquivos
                    .filter(Files::isRegularFile) //Se de fato é um arquivo
                    .filter(arquivo -> arquivo.getFileName().toString().toLowerCase().contains(filtro.toLowerCase()))
                    .forEach(System.out::println);
                }catch(IOException e){
                    System.out.println("Não há cadastros." + e.getMessage());
                }
            }else{
                
                try(Stream<Path> arquivos = Files.list(pasta)){
                arquivos
                    .filter(arquivo -> {
                        try {
                            List <String> linhas = particionarArquivo(arquivo.toString());

                            return linhas.stream().anyMatch(linha -> linha.contains(input));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .forEach(System.out::println); //Printa todos que atendem as expectativas
                }catch(IOException e){
                    System.out.println("Não há cadastros." + e.getMessage());
                }

            }
            
            
            
            


        }
    }
}
