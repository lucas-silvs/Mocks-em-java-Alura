package leilao;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class HelloWorldMockito {

    @Test
    void hello(){
        LeilaoDao leilaoDao = Mockito.mock(LeilaoDao.class);
        List<Leilao> todos = leilaoDao.buscarTodos();
        Assert.assertTrue(todos.isEmpty());
    }
}
