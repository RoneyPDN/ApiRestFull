package com.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.api.rest.ApplicationContextLoad;
import com.api.rest.model.Usuario;
import com.api.rest.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	private static final long EXPIRATION_TIME = 172800000;
	
	private static final String SECRET = "*SenhaExtremamenteSecreta";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*Gera Token e adiciona cabeçalho http*/
	
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		String JWT = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		/*Adicionar prefixo Bearer*/
		String token = TOKEN_PREFIX + " " + JWT;
		
		/*Adiciona cabeçalho http*/
		response.addHeader(HEADER_STRING, token);
		
		/*Liberando respostas para portas diferentes que usem a api ou no caso clientes web*/
		liberacaoCors(response);
		
		/*Escreve token como resposta do corpo http*/
		response.getWriter().write("{\"Authorization\": \""+ token +"\"}");
	}
	
	/*Retorna o usuário validado com token*/
	
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		/*Pega o token enviado no cabeçalho http*/
		
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody().getSubject();
			
			if (user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findUserByLogin(user);

				if (usuario != null) {
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
							usuario.getAuthorities());
				}
			}
		}
		liberacaoCors(response);
		return null;
	}

	private void liberacaoCors(HttpServletResponse response) {
		
		if(response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		if(response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		if(response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		if(response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
}
