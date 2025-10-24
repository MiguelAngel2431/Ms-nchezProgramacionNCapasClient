package com.digis01.MsanchezProgramacionNCapas.Controller;

import com.digis01.MsanchezProgramacionNCapas.ML.Result;
import com.digis01.MsanchezProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class Login {

    //Mostrar el formulario
    @GetMapping("/login")
    public String login() {
        return "Login";
    }
    
    @PostMapping("/login")
public String procesarLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Model model) {

    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> request = Map.of("userName", username, "password", password);

    try {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            "http://localhost:8081/auth/login",
            new HttpEntity<>(request, new HttpHeaders()),
            Map.class
        );

        Map<String, Object> resp = response.getBody();
        if ("pending".equals(resp.get("status"))) {
            model.addAttribute("username", resp.get("username"));
            return "ValidarCorreo"; // vista con SweetAlert + spinner
        }

        return "redirect:/usuario";

    } catch (HttpClientErrorException e) {
        model.addAttribute("error", "Credenciales inválidas");
        return "Login";
    } catch (Exception e) {
        model.addAttribute("error", "Error al conectar con el servidor de autenticación");
        return "Login";
    }
}

//    @PostMapping("/login")
//    public String procesarLogin(@RequestParam("username") String username,
//            @RequestParam("password") String password,
//            HttpSession session,
//            Model model) {
//
//        try {
//
//            // Crear un JSON con los datos del usuario
//            Map<String, String> request = new HashMap<>();
//            request.put("userName", username);  // Importante: debe ser exactamente como lo espera el backend
//            request.put("password", password);
//
//            // Preparar headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            // Enviar la solicitud con RestTemplate
//            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:8081/auth/login", entity, Map.class);
//
//            // Recuperar el token y otros datos
//            Map<String, Object> responseBody = response.getBody();
//            String status = (String) responseBody.get("status"); // pendiente /success
//            String email = (String) responseBody.get("email");
//            String token = (String) responseBody.get("token");
//            String role = (String) responseBody.get("role");
//
//            if ("pending".equals(status)) {
//                // Usuario debe validar correo
//                model.addAttribute("email", email);
//                return "ValidarCorreo";  // Página con SweetAlert + spinner + polling
//            }
//
//            // Guardar el token en sesión
//            session.setAttribute("jwt", token);
//            session.setAttribute("rol", role);
//            session.setAttribute("username", username);
//
//            // Redirigir al index (o a donde quieras)
//            return "redirect:/usuario";
//
//        } catch (HttpClientErrorException e) {
//            model.addAttribute("error", "Credenciales inválidas");
//            return "Login";
//        } catch (Exception e) {
//            model.addAttribute("error", "Error al conectar con el servidor de autenticación");
//            return "Login";
//        }
//    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Limpia sesión en cliente (aunque no es estrictamente necesario si solo guardas el JWT)
        return "redirect:/login?logout";
    }

    @GetMapping("/resetPassword")
    public String mostrarEmailForm(Model model) {

        return "ResetPassword";

    }

    @PostMapping("/resetPassword")
    public String procesarEmailForm(@RequestParam("email") String email, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        String SERVER_URL = "http://localhost:8081/authPassword/forgotPassword";

        String url = SERVER_URL + "?email=" + email;

        try {

            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

            model.addAttribute("email", email);
            model.addAttribute("succcess", response.getBody());

            if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("mensaje", "Se ha enviado una liga a tu correo.");

            }

        } catch (Exception e) {

            model.addAttribute("error", "Hubo un error al procesar la solicitud");

        }

        return "ResetPassword";

    }

    @GetMapping("/restablecerContrasenia")
    public String resetPassword(@RequestParam("token") String token, Model model) {

        model.addAttribute("token", token);

        return "SolicitarPasswords";

    }

    @PostMapping("/restablecerContrasenia")
    public String procesarResetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String password,
            Model model) {

        RestTemplate restTemplate = new RestTemplate();

        // Endpoint del servidor REST
        String url = "http://localhost:8081/authPassword/resetPassword";

        // Datos a enviar
        Map<String, String> datos = new HashMap<>();
        datos.put("token", token);
        datos.put("password", password);

        try {
            ResponseEntity<Result> response = restTemplate.exchange(url,
                    HttpMethod.POST, new HttpEntity<>(datos),
                    Result.class);

            Result result = response.getBody();

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("mensaje", "Contraseña actualizada con exito. Inicia sesión.");

            }
        } catch (Exception e) {
            model.addAttribute("error", "No se puede actualizar la contraseña: " + e.getMessage());
        }

        return "Login";

    }
    
    @GetMapping("/validarCorreo")
    public String validarCorreo(@RequestParam("token") String token, HttpSession session, Model model) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        String url = "http://localhost:8081/auth/verifyLogin?token=" + token;
        
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();
            
            if ("success".equals(body.get("status"))) {
                
                //Guardar jwt y token en sesion
                session.setAttribute("jwt", body.get("jwt"));
                session.setAttribute("username", body.get("username"));
                session.setAttribute("rol", body.get("role"));
                
                return "redirect:/usuario";
                
            } else {
                return "Login";
            }
            
        } catch (HttpClientErrorException ex) {
            model.addAttribute("error", "Token invalido o expirado");
            return "Login";
        }
        
    }

}
