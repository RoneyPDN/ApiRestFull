package com.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.model.Usuario;
import com.api.rest.repository.UsuarioRepository;

@CrossOrigin
@RestController
@RequestMapping(value="/usuario")
public class IndexController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@CacheEvict(value="cacheuser", allEntries = true)
	@CachePut("cacheuser")
	@GetMapping(value="/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	@CacheEvict(value="cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario (){
		
		List <Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	@PostMapping(value="/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar (@RequestBody Usuario usuario) {
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@PutMapping(value="/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar (@RequestBody Usuario usuario) {
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{id}", produces = "application/text")
	public ResponseEntity delete (@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return (ResponseEntity) ResponseEntity.ok();
	}
	
}
