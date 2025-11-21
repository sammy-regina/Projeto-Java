package org.contabanco.model;

public class ContaBanco {

    public int numconta;
    protected String tipo;
    private String dono;
    private float saldo;
    private boolean status;

    /**
     * Construtor da classe.
     * Define o status como 'fechada' e o saldo como zero por padrão.
     */
    public ContaBanco() {
        this.saldo = 0;
        this.status = false;
    }

    // Métodos getters e setters

    public int getNumconta() {
        return numconta;
    }

    public void setNumconta(int numconta) {
        this.numconta = numconta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    // Métodos personalizados

    /**
     * Abre a conta, definindo o tipo e concedendo um saldo inicial.
     * @param tipo Tipo da conta ('CC' ou 'CP').
     */
    public void abrirConta(String t) {
        this.setTipo(t);
        this.setStatus(true);
        if (t.equals("CC")) {
            this.setSaldo(50);
        } else if (t.equals("CP")) {
            this.setSaldo(150);
        }
        System.out.println("Conta aberta com sucesso!");
    }

    /**
     * Fecha a conta, se não houver saldo ou débito.
     */
    public void fecharConta() {
        if (this.getSaldo() > 0) {
            System.out.println("Conta com saldo positivo. Não é possível fechar.");
        } else if (this.getSaldo() < 0) {
            System.out.println("Conta com saldo negativo. Não é possível fechar.");
        } else {
            this.setStatus(false);
            System.out.println("Conta fechada com sucesso!");
        }
    }

    /**
     * Realiza um depósito na conta.
     * @param valor O valor a ser depositado.
     */
    public void depositar(float v) {
        if (this.isStatus()) {
            this.setSaldo(this.getSaldo() + v);
            System.out.println("Depósito realizado na conta de " + this.getDono());
        } else {
            System.out.println("Impossível depositar. Conta fechada.");
        }
    }
    /**
     * Realiza um saque na conta.
     * @param valor O valor a ser sacado.
     */
    public void sacar(float v) {
        if (this.isStatus()) {
            if (this.getSaldo() >= v) {
                this.setSaldo(this.getSaldo() - v);
                System.out.println("Saque realizado na conta de " + this.getDono());
            } else {
                System.out.println("Saldo insuficiente para saque.");
            }
        } else {
            System.out.println("Impossível sacar. Conta fechada.");
        }
    }

    /**
     * Paga a mensalidade da conta.
     */
    public void pagarMensalidade() {
        float v = 0;
        if (this.getTipo().equals("CC")) {
            v = 12;
        } else if (this.getTipo().equals("CP")) {
            v = 20;
        }
        if (this.isStatus()) {
            this.setSaldo(this.getSaldo() - v);
            System.out.println("Mensalidade paga por " + this.getDono());
        } else {
            System.out.println("Impossível pagar mensalidade. Conta fechada.");
        }
    }
}
