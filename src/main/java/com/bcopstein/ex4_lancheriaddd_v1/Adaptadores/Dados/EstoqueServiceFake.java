package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IEstoqueService;

@Service
@Profile("dev")
public class EstoqueServiceFake implements IEstoqueService {

    @Override
    public List<ItemPedido> verificarEstoque(List<ItemPedido> itens) {
        return List.of();
    }
}
