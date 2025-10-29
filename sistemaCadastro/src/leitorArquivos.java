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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class leitorArquivos {
    private String caminhoFormulario = "sistemaCadastro/src/formulario.txt"; //src/formulario.txt
    private BufferedReader leitor;
    private record ArquivoComConteudo(Path path, List<String> linhas) {}
    
    
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

    public void listarCadastrados(String filtro, String filtro2, String input1, String input2){
    
        Map<String, String> filtros = new HashMap<>();

    
        if (filtro != null && !filtro.isBlank() && input1 != null && !input1.isBlank()) {
            filtros.put(filtro, input1);
        }
        
    
        if (filtro2 != null && !filtro2.isBlank() && input2 != null && !input2.isBlank()) {
            filtros.put(filtro2, input2);
        }

        //chama com map o método mais complexo
        listarArquivosComFiltros(filtros);
    }

    public void listarCadastrados(String filtro, String input){
        // Reutiliza a lógica do método mais complexo, passando filtros vazios para o segundo par
        listarCadastrados(filtro, "", input, "");
    }

    
    private void listarArquivosComFiltros(Map<String, String> filtros) {
        Path pasta = Paths.get("sistemaCadastro/src/petsCadastrados");

        try (Stream<Path> streamArquivos = Files.list(pasta)) {
            
            List<ArquivoComConteudo> resultados = streamArquivos
                .filter(Files::isRegularFile)
                .map(path -> { //Lê o conteúdo e transforma em um pacote (acc)
                    try {
                        List<String> linhas = particionarArquivo(path.toString());
                        return new ArquivoComConteudo(path, linhas);
                    } catch (IOException e) {
                        System.err.println("Falha ao ler o arquivo: " + path);
                        return null;
                    }
                })
                .filter(acc -> acc != null) //remove os que falharam na leitura
                .filter(acc -> atendeTodosOsFiltros(acc.linhas(), filtros)) //O filtro MÁGICO
                .toList(); //coleta os resultados e vira lista

            //printa os resultados
            if (resultados.isEmpty()) {
                System.out.println("Nenhum arquivo encontrado com os filtros combinados.");
            } else {
                System.out.println("Arquivos encontrados:");
                resultados.forEach(acc -> {
                    System.out.println("=== Conteúdo do arquivo: " + acc.path() + " ===");
                    acc.linhas().forEach(System.out::println);
                    System.out.println();
                });
            }

        } catch (IOException e) {
            System.out.println("Erro ao listar arquivos na pasta: " + e.getMessage());
        }
    }


    private boolean atendeTodosOsFiltros(List<String> linhas, Map<String, String> filtros) {
    
    //lista tudo
    if (filtros.isEmpty()) {
        return true;
    }

    for (Map.Entry<String, String> filtro : filtros.entrySet()) {
        
        String chave = filtro.getKey();
        String valorDesejado = filtro.getValue();
        int indiceLinha = -1;
        boolean usarContains = false;
        
        switch (chave.toLowerCase()) {
            case "nome":
                indiceLinha = 0; // Linha 1
                usarContains = true;
                break;
            //tipo é pulado pq n é filtrado assim...
            case "sexo":
                indiceLinha = 2; 
                break;
            case "endereço":
                indiceLinha = 3; 
                usarContains = true;
                break;
            case "idade":
                indiceLinha = 4; 
                break;
            case "peso":
                indiceLinha = 5; 
                break;
            case "raça":
                indiceLinha = 6; 
                usarContains = true;
                break;
            default:
                System.err.println("Filtro desconhecido: " + chave);
                continue; 
        }

        if (indiceLinha == -1 || linhas.size() <= indiceLinha) {
            return false;
        }

        String linhaComPrefixo = linhas.get(indiceLinha);
        String valorReal;
        
        try {
            // pega o texto depois do " - "
            valorReal = linhaComPrefixo.substring(linhaComPrefixo.indexOf("-") + 1).trim();
        } catch (Exception e) {
            // Se a linha não tiver " - ", usa a linha inteira.
            valorReal = linhaComPrefixo;
        }

        // limpa "Idade" e "Peso"
        if (chave.equalsIgnoreCase("idade") || chave.equalsIgnoreCase("peso")) {
            // Pega a primeira parte antes do espaço (ex: "0.x" de "0.x anos")
            valorReal = valorReal.split(" ")[0];
        }
        
        boolean match;
        if (usarContains) {
            match = valorReal.toLowerCase().contains(valorDesejado.toLowerCase());
        } else {
            match = valorReal.equalsIgnoreCase(valorDesejado);
        }

        if (!match) {
            return false;
        }
    }
    return true;
    }
}
