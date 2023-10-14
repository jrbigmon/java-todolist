package br.com.vagnersiqueira.todolist.filters;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.vagnersiqueira.todolist.users.IUserRepository;
import br.com.vagnersiqueira.todolist.users.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  private UUID getUserId(String username, String password) {
    UserModel user = this.userRepository.findByUsername(username);

    if (user == null)
      return null;

    Result verified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

    return verified.verified ? user.getId() : null;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getServletPath().startsWith("/tasks/")) {
      String authorization = request.getHeader("Authorization");

      byte[] decoded = Base64.getDecoder().decode(authorization.substring("basic".length()).trim());

      String[] credentials = new String(decoded).split(":");

      String username = credentials[0];
      String password = credentials[1];

      UUID userId = this.getUserId(username, password);
      if (userId == null) {
        response.sendError(401);
      } else {
        request.setAttribute("userId", userId);
        filterChain.doFilter(request, response);
      }
    } else {
      filterChain.doFilter(request, response);
    }

  }

}
