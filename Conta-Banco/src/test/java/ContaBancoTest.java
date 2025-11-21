package org.contabanco.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * Testes unitários para a classe ContaBanco.
 * Usamos float para os valores, conforme a classe original.
 */
class ContaBancoTest {
    private ContaBanco contaTeste;
    private final float DELTA = 0.0001f; // Tolerância para comparação de floats

    // Configuração executada antes de cada método de teste
    @BeforeEach
    void setUp() {
        contaTeste = new ContaBanco();
        contaTeste.setDono("Tester");
        contaTeste.setNumconta(9999);
    }

    // -------------------------------------------------------------------------
    // TESTES DE ABERTURA E SALDO INICIAL
    // -------------------------------------------------------------------------

    @Test
    void testAbrirContaCC() {
        contaTeste.abrirConta("CC");
        assertTrue(contaTeste.isStatus());
        assertEquals(50.0f, contaTeste.getSaldo(), DELTA);
        assertEquals("CC", contaTeste.getTipo());
    }

    @Test
    void testAbrirContaCP() {
        contaTeste.abrirConta("CP");
        assertTrue(contaTeste.isStatus());
        assertEquals(150.0f, contaTeste.getSaldo(), DELTA);
        assertEquals("CP", contaTeste.getTipo());
    }

    // -------------------------------------------------------------------------
    // TESTES DE DEPÓSITO E SAQUE
    // -------------------------------------------------------------------------

    @Test
    void testDepositarContaAberta() {
        contaTeste.abrirConta("CC"); // Saldo inicial: 50.0f
        contaTeste.depositar(100.0f);
        assertEquals(150.0f, contaTeste.getSaldo(), DELTA);
    }

    @Test
    void testSacarComSaldoSuficiente() {
        contaTeste.abrirConta("CC"); // Saldo inicial: 50.0f
        contaTeste.depositar(100.0f); // Saldo: 150.0f
        contaTeste.sacar(50.0f);
        assertEquals(100.0f, contaTeste.getSaldo(), DELTA);
    }

    @Test
    void testSacarSemSaldo() {
        contaTeste.abrirConta("CP"); // Saldo inicial: 150.0f
        // Tenta sacar 200.0f
        contaTeste.sacar(200.0f);
        // O saldo deve permanecer o inicial
        assertEquals(150.0f, contaTeste.getSaldo(), DELTA);
    }

    // -------------------------------------------------------------------------
    // TESTES DE MENSALIDADE E FECHAMENTO
    // -------------------------------------------------------------------------

    @Test
    void testPagarMensalidadeCC() {
        contaTeste.abrirConta("CC"); // Saldo inicial: 50.0f
        contaTeste.pagarMensalidade(); // Desconta 12.0f
        assertEquals(38.0f, contaTeste.getSaldo(), DELTA);
    }

    @Test
    void testPagarMensalidadeCP() {
        contaTeste.abrirConta("CP"); // Saldo inicial: 150.0f
        contaTeste.pagarMensalidade(); // Desconta 20.0f
        assertEquals(130.0f, contaTeste.getSaldo(), DELTA);
    }

    @Test
    void testFechamentoComSaldoZero() {
        contaTeste.abrirConta("CC"); // Saldo: 50.0f
        contaTeste.sacar(50.0f); // Saldo: 0.0f
        contaTeste.fecharConta();
        assertFalse(contaTeste.isStatus()); // Deve estar fechada
    }

    @Test
    void testFechamentoComSaldoPositivo() {
        contaTeste.abrirConta("CP"); // Saldo: 150.0f
        contaTeste.fecharConta();
        assertTrue(contaTeste.isStatus()); // Deve permanecer aberta
    }
}
