package br.com.codificando.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.codificando.model.Usuario;
import br.com.codificando.repository.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/usuario/list")
	public String listUsuarios(Model model) {
		model.addAttribute("usuarios", usuarioRepository.findAll(Sort.by("nome")));
		
		return "usuario/list";
	}
	
	@GetMapping("/usuario/add")
	public String addUsuarios(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "usuario/add";
	}
	
	@PostMapping("/usuario/save")
	public String saveUsuario(Usuario usuario) {
		System.out.println(usuario);
		try {
			if (usuario != null) {
				String senha = new BCryptPasswordEncoder().encode(usuario.getSenha());
				usuario.setSenha(senha);
				usuarioRepository.save(usuario);
			}
		} catch (Exception e) {
			System.out.println("Erro ao salvar: "+ e.getMessage());
		}
		
		return "redirect:/usuario/view/" + usuario.getId() + "/" + true;
		
	}
	
	@GetMapping("/usuario/view/{id}/{salvo}")
	public String viewUsuario(@PathVariable long id, @PathVariable boolean salvo, Model model) {
		model.addAttribute("usuario", usuarioRepository.findById(id));
		model.addAttribute("Salvo", salvo);
		return "usuario/view";
	}
	
	@GetMapping("/usuario/edit/{id}")
	public String editUsuario(@PathVariable long id, Model model) {
		
		model.addAttribute("usuario", usuarioRepository.findById(id));
		return "usuario/edit";
	}
	
	@GetMapping("/usuario/delete/{id}")
	public String deleteUsuario(@PathVariable long id) {
		try {
			usuarioRepository.deleteById(id);
		} catch (Exception e) {
			System.out.println("Erro: "+ e.getMessage());
		}
		
		return "redirect:/usuario/list";
	}
}
