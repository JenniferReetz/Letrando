package reetz.letrando.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reetz.letrando.DTO.AuthRequest;
import reetz.letrando.DTO.AuthResponse;
import reetz.letrando.model.Usuario;
import reetz.letrando.repository.UsuarioRepository;
import reetz.letrando.security.JwtUtil;
import reetz.letrando.service.UsuarioService;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            UsuarioService usuarioService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(400).body("Nome de usuário já existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário criado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Tentando autenticar: " + authRequest.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()
                    )
            );

            System.out.println("Autenticado com sucesso!");

            UserDetails userDetails = usuarioService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtil.gerarToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
