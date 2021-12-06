package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeradorDePagamentoTest {
    private GeradorDePagamento gerador;



    @Mock
    private PagamentoDao pagamentoDao;

    //a anotação captor serve para simbolizar que essa variavel (obrigatoriamene tem que ser do tipo argumentCaptor)
    //será uma captura de um objeto dentro do método que está sendo testado e sendo esse um metodo de um mock
    @Captor
    private ArgumentCaptor<Pagamento> captor;


    @Mock
    private Clock clock;

    //inicia os mocks com o o objeto da classe que está send testada
    @BeforeEach
    private void beforeEach(){
        MockitoAnnotations.initMocks(this);
        this.gerador = new GeradorDePagamento(pagamentoDao,clock);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao(){



        LocalDate data = LocalDate.of(2020,12,7);




        Leilao leilao = lanceVencedor();
        leilao.setLanceVencedor(leilao.getLances().get(0));


        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        gerador.gerarPagamento(leilao.getLanceVencedor());
        // define para verficar dentro do metodo salvar  um objeto do tipo pagamento e captura ele para a variavel captor
        Mockito.verify(pagamentoDao).salvar(captor.capture());
        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now().plusDays(1),pagamento.getVencimento());
        assertFalse(pagamento.getPago());
        assertEquals(leilao.getLanceVencedor().getUsuario(),pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());



    }

    private Leilao lanceVencedor(){
        List<Leilao> lista = new ArrayList<>();
        Leilao leilao = new Leilao("Celular"
                ,new BigDecimal("500")
                ,new Usuario("Fulano"));


        Lance vencedor = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));


        leilao.propoe(vencedor);

        return leilao;
    }

}