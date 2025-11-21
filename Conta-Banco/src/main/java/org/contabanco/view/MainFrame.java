package org.contabanco.view;

import org.contabanco.dao.ContaDAO;
import org.contabanco.model.ContaBanco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{
    // Componentes da Interface Gráfica
    private JTextField txtNumConta, txtDono, txtValor;
    private JComboBox<String> cbTipo;
    private JTextArea taSaida;
    private JButton btnAbrir, btnDepositar, btnSacar, btnPagarMensal, btnFechar, btnConsultar;

    // Instância do DAO para operações de banco de dados
    private ContaDAO contaDAO = new ContaDAO();

    // ----------------------------------------------------
    // CONSTRUTOR DA INTERFACE
    // ----------------------------------------------------
    public MainFrame() {
        super("Sistema Bancário POO (Swing/MySQL)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLayout(new BorderLayout(10, 10)); // Layout principal
        setLocationRelativeTo(null); // Centraliza a janela

        initComponents(); // Inicializa os componentes e layouts
        pack(); // Ajusta o tamanho da janela aos componentes
        setVisible(true);
    }

    // ----------------------------------------------------
    // INICIALIZAÇÃO E LAYOUT
    // ----------------------------------------------------
    private void initComponents() {
        // Painel de Entrada (Norte)
        JPanel pnlEntrada = new JPanel(new GridLayout(3, 2, 10, 5));

        txtNumConta = new JTextField(10);
        txtDono = new JTextField(10);
        txtValor = new JTextField(10);
        cbTipo = new JComboBox<>(new String[]{"CC", "CP"});

        pnlEntrada.add(new JLabel("Nº Conta:"));
        pnlEntrada.add(txtNumConta);
        pnlEntrada.add(new JLabel("Dono:"));
        pnlEntrada.add(txtDono);
        pnlEntrada.add(new JLabel("Tipo:"));
        pnlEntrada.add(cbTipo);
        pnlEntrada.add(new JLabel("Valor (Op):"));
        pnlEntrada.add(txtValor);

        add(pnlEntrada, BorderLayout.NORTH);

        // Painel de Botões (Centro)
        JPanel pnlBotoes = new JPanel(new GridLayout(2, 3, 10, 10));

        btnAbrir = new JButton("Abrir Conta");
        btnDepositar = new JButton("Depositar");
        btnSacar = new JButton("Sacar");
        btnPagarMensal = new JButton("Pagar Mensalidade");
        btnFechar = new JButton("Fechar Conta");
        btnConsultar = new JButton("Consultar Conta");

        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnDepositar);
        pnlBotoes.add(btnSacar);
        pnlBotoes.add(btnPagarMensal);
        pnlBotoes.add(btnFechar);
        pnlBotoes.add(btnConsultar);

        add(pnlBotoes, BorderLayout.CENTER);

        // Área de Saída (Sul)
        taSaida = new JTextArea(10, 60);
        taSaida.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taSaida);
        add(scrollPane, BorderLayout.SOUTH);

        // Adiciona Listeners aos botões
        btnAbrir.addActionListener(e -> abrirConta());
        btnDepositar.addActionListener(e -> operarConta("Depositar"));
        btnSacar.addActionListener(e -> operarConta("Sacar"));
        btnPagarMensal.addActionListener(e -> operarConta("Mensalidade"));
        btnFechar.addActionListener(e -> operarConta("Fechar"));
        btnConsultar.addActionListener(e -> consultarConta());


    }

    // ----------------------------------------------------
    // MÉTODOS DE AÇÃO (LIGAÇÃO COM MODELO E DAO)
    // ----------------------------------------------------

    /**
     * Tenta criar uma nova conta, chamando o método abrirConta() do Modelo
     * e o método salvar() do DAO.
     */
    private void abrirConta() {

        String numContaText = txtNumConta.getText();

        // 1. Verificação se o campo não está vazio
        if (numContaText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Nº Conta' não pode estar vazio.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return; // Sai do método
        }

        try {
            // 2. Tenta a conversão
            int numConta = Integer.parseInt(numContaText);
            String dono = txtDono.getText();
            String tipo = (String) cbTipo.getSelectedItem();

            if (dono.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o nome do Dono.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Lógica POO e Persistência
            ContaBanco novaConta = new ContaBanco();
            novaConta.setNumconta(numConta);
            novaConta.setDono(dono);
            novaConta.abrirConta(tipo);

            // Adicionar uma verificação de conta já existente (DAO) seria bom aqui!
            contaDAO.salvar(novaConta);

            taSaida.append("-> Conta " + numConta + " de " + dono + " aberta com sucesso.\n");

        } catch (NumberFormatException ex) {
            // Captura se houver letras ou caracteres inválidos (ex: "12a")
            JOptionPane.showMessageDialog(this, "O número da conta deve conter apenas dígitos.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa operações de depósito, saque, mensalidade e fechamento.
     * @param operacao O tipo de operação a ser realizada.
     */
    private void operarConta(String operacao) {
        try {
            int numConta = Integer.parseInt(txtNumConta.getText());
            ContaBanco conta = contaDAO.buscarConta(numConta);

            if (conta == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada no sistema.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Variável para armazenar o valor da operação (se aplicável)
            float valor = 0.0f;
            if (operacao.equals("Depositar") || operacao.equals("Sacar")) {
                valor = Float.parseFloat(txtValor.getText());
            }

            // Lógica POO e atualização no BD
            String resultado = "";
            switch (operacao) {
                case "Depositar":
                    if (conta.isStatus()) {
                        conta.depositar(valor); // Lógica POO
                        contaDAO.atualizarSaldo(conta); // Persistência
                        resultado = "Depósito de R$" + valor + " realizado.";
                    } else {
                        resultado = "Impossível depositar. Conta fechada.";
                    }
                    break;
                case "Sacar":
                    // A própria lógica do sacar verifica o saldo e status
                    conta.sacar(valor);
                    contaDAO.atualizarSaldo(conta);
                    resultado = "Tentativa de saque de R$" + valor + " processada.";
                    break;
                case "Mensalidade":
                    conta.pagarMensalidade(); // Lógica POO
                    contaDAO.atualizarSaldo(conta); // Persistência
                    resultado = "Mensalidade paga/processada.";
                    break;
                case "Fechar":
                    // A própria lógica do fecharConta verifica o saldo
                    conta.fecharConta();
                    // Se o status mudou para false, atualiza no BD (poderia ter um método dao.atualizarStatus)
                    contaDAO.atualizarSaldo(conta); // Neste caso, atualiza saldo (0) e status (se implementado no DAO)
                    resultado = "Tentativa de fechamento de conta processada.";
                    break;
            }

            taSaida.append("-> Conta " + numConta + " (" + operacao + "): " + resultado + " | Novo Saldo: R$" + conta.getSaldo() + "\n");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número da Conta ou Valor inválido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Consulta os dados atuais de uma conta no BD e exibe na saída.
     */
    private void consultarConta() {
        try {
            int numConta = Integer.parseInt(txtNumConta.getText());
            ContaBanco conta = contaDAO.buscarConta(numConta);

            if (conta == null) {
                taSaida.append("-> ERRO: Conta " + numConta + " não encontrada.\n");
                return;
            }

            String statusConta = conta.isStatus() ? "Aberta" : "Fechada";
            taSaida.append("--- CONSULTA CONTA " + conta.getNumconta() + " ---\n");
            taSaida.append("Dono: " + conta.getDono() + "\n");
            taSaida.append("Tipo: " + conta.getTipo() + "\n");
            taSaida.append("Saldo Atual: R$" + conta.getSaldo() + "\n");
            taSaida.append("Status: " + statusConta + "\n");
            taSaida.append("----------------------------\n");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número da Conta inválido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------------------------------------------
    // MÉTODO MAIN PARA INICIAR O SWING
    // ----------------------------------------------------
    public static void main(String[] args) {
        // 1. Tentar aplicar o Look and Feel Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se Nimbus não estiver disponível, usa o Look and Feel padrão.
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Não foi possível carregar o Look and Feel.");
            }
        }

        // Assegura que o Swing seja executado na thread de despacho de eventos
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
