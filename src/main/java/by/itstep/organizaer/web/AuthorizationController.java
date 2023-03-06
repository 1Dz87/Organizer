package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.LoginRequest;
import by.itstep.organizaer.model.dto.RegistrationRequest;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.model.entity.Roles;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.UserMapper;
import by.itstep.organizaer.security.JwtUtil;
import by.itstep.organizaer.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authorization", description = "API авторизации")
public class AuthorizationController {

    UserService userService;

    AuthenticationManager authenticationManager;

    JwtUtil jwtUtil;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    public AuthorizationController(UserService userService,
                                   AuthenticationManager authenticationManager,
                                   JwtUtil jwtUtil,
                                   UserMapper userMapper,
                                   @Qualifier("major") PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // http://localhost:8080/auth/register
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.ROLE_USER));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/registerAdmin")
    public ResponseEntity<UserDto> registerAdmin(@RequestBody RegistrationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.ROLE_ADMIN));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    // http://localhost:8080/auth/login
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
        log.debug("Запрос входа с логином: {}", request.getLogin());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtUtil.generateToken(user))
                .body(userMapper.toDto(user));
    }

}
