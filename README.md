# 🐾 Sistema de Gerenciamento de Abrigo (CLI)

Este projeto é um sistema de gerenciamento para um abrigo de animais, permitindo o cadastro, busca, alteração e exclusão de pets via interface de linha de comando (CLI).

O sistema foi desenvolvido em Java puro, com foco na aplicação de conceitos de **Programação Orientada a Objetos (OO)**, **Manipulação de Arquivos (Java IO e NIO)** e **Tratamento de Exceções**.

---

## 🚀 Funcionalidades Principais

O sistema permite ao usuário (dono do abrigo) realizar as seguintes ações:

* **1. Cadastrar um novo pet:** Guia o usuário através de um formulário (baseado no `formulario.txt`) para registrar um novo animal.
* **2. Alterar dados do pet:** Permite buscar um pet pelo nome e sobrescrever informações específicas (como nome, idade, raça, etc.).
* **3. Deletar um pet cadastrado:** Remove permanentemente o registro de um animal, pedindo confirmação antes de agir.
* **4. Listar todos os pets cadastrados:** Exibe no console o conteúdo de todos os registros de pets.
* **5. Listar pets por critério:** Uma busca avançada que permite filtrar animais por um ou dois critérios combinados (ex: `Tipo = Gato` E `Idade = 2`).
* **6. Sair:** Encerra a aplicação.

---

## 🛠️ Tecnologias e Conceitos Aplicados

Este projeto foi construído para demonstrar o entendimento dos seguintes pilares do Java:

* **Programação Orientada a Objetos (OO):** O sistema é dividido em classes com responsabilidades claras (`Menu` para interface, `Pets` para lógica, `leitorArquivos` para I/O).
* **Java IO (Entrada e Saída):**
    * `BufferedReader` e `InputStreamReader` para capturar a entrada do usuário no console.
    * `FileReader` para ler arquivos de texto (`formulario.txt` e os cadastros dos pets).
* **Java NIO (New IO):**
    * Uso de `Path` e `Paths` para manipulação moderna de caminhos de arquivos.
    * Uso de `Files` para operações de sistema de arquivos como `Files.createDirectories()`, `Files.write()`, `Files.delete()` e `Files.list()`.
* **Java Streams (API):** A lógica de filtragem e busca (`leitorArquivos.java`) é implementada de forma eficiente usando `Stream`, `map`, `filter` e `toList` para processar a lista de arquivos.
* **API `java.time`:** Utilizada na classe `tempoCadastroPet` para gerar timestamps (`LocalDate`, `LocalTime`) que compõem os nomes dos arquivos.
* **Tratamento de Exceções:** Uso extensivo de blocos `try-catch` e lançamento de exceções (`throw new Exception()`) para garantir a robustez do programa contra entradas inválidas, arquivos não encontrados (`NoSuchFileException`) e erros de I/O.
* **Enum:** Usado na classe `Pets` para validar campos de entrada restritos (`tipoPet` e `sexoPet`).

---
## 🏛️ Arquitetura e Classes

O sistema é dividido em 5 classes principais, cada uma com uma responsabilidade única:

### 1. `App.java`
* **Responsabilidade:** Ponto de Entrada (Main).
* **Descrição:** É a classe que inicia o programa. Sua única função é instanciar a classe `Menu` e chamar o método `iniciar()`, dando partida ao loop da aplicação.

### 2. `Menu.java`
* **Responsabilidade:** Interface e Controle de Fluxo.
* **Descrição:** Apresenta o menu de opções ao usuário, captura a escolha e, através de um `switch`, delega a execução da tarefa para os métodos corretos na classe `Pets`.

### 3. `Pets.java`
* **Responsabilidade:** Lógica de Negócio, Modelo de Dados e Escrita.
* **Descrição:** É a classe central do sistema.
    * **Modelo:** Define os atributos de um pet (nome, idade, peso, etc.).
    * **Validação:** Contém todos os métodos `set...` (ex: `setIdade`, `setNome`) que validam os dados de entrada, lançando exceções se as regras de negócio não forem atendidas.
    * **Lógica (CRUD):** Contém os métodos principais (`cadastrarPet`, `alterarCadastro`, `deletarCadastro`) que executam as ações do menu.
    * **Escrita:** Formata e escreve fisicamente os dados no disco (`criarArquivoPet`, `sobrescreverArquivoPet`), usando `tempoCadastroPet` para gerar nomes de arquivo únicos.

### 4. `leitorArquivos.java`
* **Responsabilidade:** Leitura de I/O e Lógica de Busca.
* **Descrição:** Este é o "motor" de leitura e filtragem.
    * **Leitura de Console:** O método `lerInput()` é usado por todas as outras classes para ler a entrada do usuário.
    * **Leitura de Arquivo:** O método `particionarArquivo()` lê arquivos de texto (`formulario.txt` ou um pet) e os divide em uma lista de linhas.
    * **Busca e Filtragem:** Os métodos `listarCadastrados()`, `atendeTodosOsFiltros()` e `BuscarArquivoEspecifico()` contêm a lógica mais complexa. Eles usam Java Streams para varrer o diretório `petsCadastrados/`, ler o conteúdo de cada arquivo e retornar apenas aqueles que batem com os critérios de busca (com 1 ou 2 filtros).

### 5. `tempoCadastroPet.java`
* **Responsabilidade:** Utilitário de Timestamp.
* **Descrição:** Uma classe simples que fornece métodos (`AnoMesDia`, `Horario`) para gerar um carimbo de data e hora formatado, garantindo que cada pet cadastrado tenha um nome de arquivo único.

---

## 💾 Formato de Dados (Persistência)

O sistema não utiliza um banco de dados tradicional. Em vez disso, cada pet cadastrado é salvo como um arquivo `.txt` individual dentro da pasta `src/petsCadastrados/`.

* **Nome do Arquivo:** O nome do arquivo segue o padrão `[TIMESTAMP]-[NOME].TXT`.
    * *Exemplo:* `20251107T1701-FIDO.TXT`
* **Conteúdo do Arquivo:** O conteúdo do arquivo é baseado nas 7 perguntas do `formulario.txt`, salvo em um formato linha a linha para facilitar a leitura e o *parsing* na hora da busca.
    ```
    1 - Fido
    2 - Cachorro
    3 - Masculino
    4 - Rua dos Exemplos, 123, Bairro Java
    5 - 4.0 anos
    6 - 15.5 kgs
    7 - Vira-lata
    ```
Esta abordagem permite que a lógica de filtragem (`atendeTodosOsFiltros`) funcione de maneira eficaz, lendo linhas específicas de cada arquivo para aplicar os filtros.

---

## ▶️ Como Executar

**Pré-requisitos:**
* Ter o **JDK (Java Development Kit)** (versão 8 ou superior) instalado e configurado nas variáveis de ambiente.

**Passos para Compilar e Rodar:**

1.  Abra um terminal ou prompt de comando.
2.  Navegue até o diretório `src/` do projeto.
3.  Compile todos os arquivos `.java`:
    ```bash
    javac App.java Menu.java Pets.java leitorArquivos.java tempoCadastroPet.java
    ```
4.  Execute a classe principal `App`:
    ```bash
    java App
    ```
5.  O menu do sistema será exibido no console e estará pronto para uso.

A estrutura de pastas e arquivos do projeto é a seguinte: