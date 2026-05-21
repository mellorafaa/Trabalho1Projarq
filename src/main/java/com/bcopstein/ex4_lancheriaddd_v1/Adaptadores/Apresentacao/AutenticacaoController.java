package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.TokenPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.FazerLoginUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.TokenResponse;

@RestController
@RequestMapping("/autenticacao")
@CrossOrigin("*")
public class AutenticacaoController {
    
    private final FazerLoginUC fazerLoginUC;
    
    public AutenticacaoController(FazerLoginUC fazerLoginUC) {
        this.fazerLoginUC = fazerLoginUC;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar entrada
            if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getSenha() == null) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"erro\": \"Email e senha são obrigatórios\"}");
            }
            
            // Executar use case
            TokenResponse tokenResponse = fazerLoginUC.executar(loginRequest);
            
            // Converter para presenter
            TokenPresenter presenter = new TokenPresenter(tokenResponse);
            
            return ResponseEntity.ok(presenter);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"erro\": \"Erro ao processar login\"}");
        }
    }
}
