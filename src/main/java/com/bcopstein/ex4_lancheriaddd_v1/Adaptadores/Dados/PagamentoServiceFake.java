package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPagamentoService;

@Service
public class PagamentoServiceFake implements IPagamentoService {

    @Override
    public boolean processarPagamento(Pedido pedido) {
        return true;
    }
}
