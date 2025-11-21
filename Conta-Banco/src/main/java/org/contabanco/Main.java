package org.contabanco;

import org.contabanco.model.ContaBanco;

public class Main {
    public static void main(String[] args) {

        // 1. Instanciando duas contas
        ContaBanco conta1 = new ContaBanco();
        conta1.setNumconta(1001);
        conta1.setDono("Jubileu da Silva");

        ContaBanco conta2 = new ContaBanco();
        conta2.setNumconta(1002);
        conta2.setDono("Creuza Rodrigues");

        System.out.println("--- INICIANDO OPERAÇÕES ---");

        // 2. Operações na Conta 1 (Conta Corrente)
        conta1.abrirConta("CC"); // Abre a conta e recebe R$ 50,00
        conta1.depositar(300.0f); // Deposita R$ 300,00
        conta1.sacar(100.0f); // Saca R$ 100,00

        // 3. Operações na Conta 2 (Conta Poupança)
        conta2.abrirConta("CP"); // Abre a conta e recebe R$ 150,00
        conta2.depositar(500.0f); // Deposita R$ 500,00
        conta2.pagarMensalidade(); // Paga R$ 20,00 (Mensalidade CP)

        // 4. Tentativa de fechar a conta 1 com saldo positivo
        System.out.println("\n--- TENTATIVA DE FECHAMENTO ---");
        conta1.fecharConta(); // Deve falhar, saldo > 0

        // 5. Saque final e Fechamento
        System.out.println("\n--- AJUSTE E FECHAMENTO DA CONTA 1 ---");
        // Saldo da conta 1 deve ser: 50 + 300 - 100 = 250
        conta1.sacar(250.0f); // Zera o saldo
        conta1.fecharConta(); // Deve ter sucesso

        System.out.println("\n--- STATUS FINAIS ---");
        System.out.println("Conta " + conta1.getNumconta() + ": Saldo final = R$ " + conta1.getSaldo() + " | Status = " + (conta1.isStatus() ? "Aberta" : "Fechada"));
        System.out.println("Conta " + conta2.getNumconta() + ": Saldo final = R$ " + conta2.getSaldo() + " | Status = " + (conta2.isStatus() ? "Aberta" : "Fechada"));
    }
}