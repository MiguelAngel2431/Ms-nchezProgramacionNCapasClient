package com.digis01.MsanchezProgramacionNCapas.Controller;

import com.digis01.MsanchezProgramacionNCapas.ML.Colonia;
import com.digis01.MsanchezProgramacionNCapas.ML.Direccion;
import com.digis01.MsanchezProgramacionNCapas.ML.ErrorCM;
import com.digis01.MsanchezProgramacionNCapas.ML.Estado;
import com.digis01.MsanchezProgramacionNCapas.ML.Municipio;
import com.digis01.MsanchezProgramacionNCapas.ML.Pais;
import com.digis01.MsanchezProgramacionNCapas.ML.Result;
import com.digis01.MsanchezProgramacionNCapas.ML.Rol;
import com.digis01.MsanchezProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    //Metodo para retornar todos los usarios a la vista
    @GetMapping
    public String Index(Model model) {
        //Result result = usuarioDAOImplementation.GetAll(new Usuario("", "", "", 0));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange("http://localhost:8081/usuarioapi",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            model.addAttribute("usuarioBusqueda", new Usuario());

            Result result = responseEntity.getBody();

            if (result.correct) {
                model.addAttribute("usuarios", result.object);
                //model.addAttribute("roles", rolJPADAOImplementation.GetAll().objects);
            } else {
                model.addAttribute("usuarios", null);
            }

        }

        return "UsuarioIndex"; //Retornamos hacia la vista UsuarioIndex.html

    }

    //Buscador
//    @PostMapping
//    public String Index(@ModelAttribute("usuarioBusqueda") Usuario usuarioBusqueda,
//            Model model) {
//
//        Result result = usuarioDAOImplementation.GetAll(usuarioBusqueda);
//
//        if (result.correct) {
//            model.addAttribute("usuarios", result.objects);
//            model.addAttribute("roles", rolJPADAOImplementation.GetAll().objects);
//        } else {
//            model.addAttribute("usuarios", null);
//        }
//
//        return "UsuarioIndex"; //Retornamos hacia la vista UsuarioIndex.html
//
//    }
//   
    //Manda al fomulario (para agregar un usuario) o a la vista de usuario details
    @GetMapping("/action/{IdUsuario}")
    public String Add(Model model, @PathVariable("IdUsuario") int idUsuario) {

        if (idUsuario == 0) { //Manda al formulario completo (en blanco)

            RestTemplate restTemplate = new RestTemplate();

            //Rol
            ResponseEntity<Result<List<Rol>>> responseEntity = restTemplate.exchange("http://localhost:8081/rolapi",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
            });

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("rol", new Rol());
                Result result = responseEntity.getBody();

                //Pais
                ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange("http://localhost:8081/paisapi",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<Pais>>>() {
                });

                if (responseEntityPais.getStatusCode() == HttpStatusCode.valueOf(200)) {

                    model.addAttribute("pais", new Pais());

                    Result resultPais = responseEntityPais.getBody();

                    Usuario usuario = new Usuario();
                    usuario.Direcciones = new ArrayList<>();
                    usuario.Direcciones.add(new Direccion());
                    model.addAttribute("roles", result.object);
                    model.addAttribute("paises", resultPais.object);
                    model.addAttribute("Usuario", usuario);

                    //Poner por default un radiobutton seleccionado
                    usuario.setSexo("M ");

                }
            }

            return "UsuarioForm";

        } else { //Manda a la vista del detalle del Usuario
            //Result result = usuarioDAOImplementation.GetDetail(idUsuario);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange("http://localhost:8081/usuarioapi/details/" + idUsuario,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Usuario>>() {
            });

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("usuario", new Usuario());

                Result result = responseEntity.getBody();

                if (result.correct) {
                    model.addAttribute("usuario", result.object);
                } else {
                    model.addAttribute("usuario", null);
                }
            }

            return "UsuarioDetail"; //Retornamos hacia la vista UsuarioDetail.html
        }

    }

    //Metodo para retornar datos al formulario (GET)
    @GetMapping("formEditable")
    public String formEditable(
            @RequestParam int IdUsuario,
            @RequestParam(required = false) Integer IdDireccion,
            Model model) {

        /**
         * ***************************** EDITAR USUARIO
         * ****************************************
         */
        if (IdDireccion == null) { //Editar usuario

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange("http://localhost:8081/usuarioapi/details/" + IdUsuario,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Usuario>>() {
            });

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("usuario", new Usuario());

                Result result = responseEntity.getBody();

                //Rol
                ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange("http://localhost:8081/rolapi",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<Rol>>>() {
                });

                if (responseEntityRol.getStatusCode() == HttpStatusCode.valueOf(200)) {
                    model.addAttribute("rol", new Rol());
                    Result resultRol = responseEntityRol.getBody();

                    Usuario usuario = (Usuario) result.object;
                    usuario.Direcciones = new ArrayList<>();
                    usuario.Direcciones.add(new Direccion(-1));
                    if (result.correct) {
                        model.addAttribute("Usuario", result.object);
                        model.addAttribute("roles", resultRol.object);

                    } else {
                        model.addAttribute("Usuario", null);
                    }
                }
            }

            /**
             * ***************************** AGREGAR DIRECCION
             * ****************************************
             */
        } else if (IdDireccion == 0) { //Agregar direccion

            RestTemplate restTemplate = new RestTemplate();

            //Pais
            ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange("http://localhost:8081/paisapi",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
            });

            if (responseEntityPais.getStatusCode() == HttpStatusCode.valueOf(200)) {

                model.addAttribute("pais", new Pais());

                Result resultPais = responseEntityPais.getBody();

                if (resultPais.correct) {

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(IdUsuario);
                    usuario.Direcciones = new ArrayList<>();
                    usuario.Direcciones.add(new Direccion());

                    model.addAttribute("Usuario", usuario);
                    model.addAttribute("paises", resultPais.object);

                } else {
                    model.addAttribute("Usuario", null);
                }

            }

            /**
             * ***************************** EDITAR DIRECCION
             * ****************************************
             */
        } else {// Editar direccion

            //Result result = usuarioDAOImplementation.DireccionGetByIdDireccion(IdDireccion);
            RestTemplate restTemplate = new RestTemplate();

            //Direccion
            ResponseEntity<Result<Direccion>> responseEntity = restTemplate.exchange("http://localhost:8081/direccionapi/" + IdDireccion,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Direccion>>() {
            });

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {

                model.addAttribute("usuario", new Usuario());

                Result result = responseEntity.getBody();

                //Pais
                ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange("http://localhost:8081/paisapi",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<Pais>>>() {
                });

                if (responseEntityPais.getStatusCode() == HttpStatusCode.valueOf(200)) {

                    model.addAttribute("pais", new Pais());

                    Result resultPais = responseEntityPais.getBody();

                    Usuario usuarioEstado = new Usuario();
                    usuarioEstado.setIdUsuario(IdUsuario);
                    usuarioEstado.setDirecciones(new ArrayList<>());
                    //usuario.getDirecciones().add((Direccion) result.object);

                    Direccion direccionEstado = (Direccion) result.object;

                    usuarioEstado.Direcciones.add(direccionEstado);

                    //Estado
                    ResponseEntity<Result<List<Estado>>> responseEntityEstado = restTemplate.exchange("http://localhost:8081/estadoapi/Pais/" + usuarioEstado.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais(),
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<Result<List<Estado>>>() {
                    });

                    if (responseEntityEstado.getStatusCode() == HttpStatusCode.valueOf(200)) {

                        model.addAttribute("estado", new Pais());

                        Result resultEstado = responseEntityEstado.getBody();

                        //Municipio
                        ResponseEntity<Result<List<Municipio>>> responseEntityMunicipio = restTemplate.exchange("http://localhost:8081/municipioapi/Estado/" + usuarioEstado.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado(),
                                HttpMethod.GET,
                                HttpEntity.EMPTY,
                                new ParameterizedTypeReference<Result<List<Municipio>>>() {
                        });

                        if (responseEntityMunicipio.getStatusCode() == HttpStatusCode.valueOf(200)) {

                            model.addAttribute("municipio", new Municipio());

                            Result resultMunicipio = responseEntityMunicipio.getBody();

                            //Colonia
                            ResponseEntity<Result<List<Colonia>>> responseEntityColonia = restTemplate.exchange("http://localhost:8081/coloniaapi/Municipio/" + usuarioEstado.Direcciones.get(0).Colonia.Municipio.getIdMunicipio(),
                                    HttpMethod.GET,
                                    HttpEntity.EMPTY,
                                    new ParameterizedTypeReference<Result<List<Colonia>>>() {
                            });

                            if (responseEntityColonia.getStatusCode() == HttpStatusCode.valueOf(200)) {

                                model.addAttribute("colonia", new Colonia());

                                Result resultColonia = responseEntityColonia.getBody();

                                if (result.correct) {

                                    Usuario usuario = new Usuario();
                                    usuario.setIdUsuario(IdUsuario);
                                    usuario.setDirecciones(new ArrayList<>());

                                    Direccion direccion = (Direccion) result.object;

                                    usuario.Direcciones.add(direccion);

                                    model.addAttribute("Usuario", usuario);
                                    model.addAttribute("paises", resultPais.object);
                                    model.addAttribute("estados", resultEstado.object);
                                    model.addAttribute("municipios", resultMunicipio.object);
                                    model.addAttribute("colonias", resultColonia.object);

                                } else {
                                    model.addAttribute("Usuario", null);
                                }
                            }
                        }
                    }
                }
            }
        }

        return "UsuarioForm";

    }

    //Proceso de agregado (POST)
    @PostMapping("add")
    public String Add(
            @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult,
            Model model,
            @RequestParam(name = "imagenFile", required = false) MultipartFile imagen) {

        /**
         * ***************************** AGREGAR USUARIO
         * ****************************************
         */
        if (usuario.getIdUsuario() == 0) { //Agregar usuario

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:8081/usuarioapi",
                    HttpMethod.POST,
                    new HttpEntity<>(usuario),
                    new ParameterizedTypeReference<Result>() {
            });

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("usuario", new Usuario());

                Result result = responseEntity.getBody();

                //Si bindingResult tiene errores...
                if (bindingResult.hasErrors()) {
                    model.addAttribute("Usuario", usuario);

                    return "UsuarioForm";

                } else {

                    //Imagen
                    if (imagen != null && imagen.getOriginalFilename() != null) {
                        String nombre = imagen.getOriginalFilename();
                        //archivo.jpg
                        //[archivo,jpg]
                        String extension = nombre.split("\\.")[1];
                        if (extension.equals("jpg")) {
                            try {
                                byte[] bytes = imagen.getBytes();
                                String base64Image = Base64.getEncoder().encodeToString(bytes);
                                usuario.setImagen(base64Image);
                            } catch (Exception ex) {
                                System.out.println("Error");
                            }

                        }
                    }

                    //Autoinferencia
                    //Result result = usuarioDAOImplementation.Add(usuario);
                    //Result result = usuarioJPADAOImplementation.Add(usuario);
                    return "redirect:/usuario";
                }
            }
        }

        if (usuario.getIdUsuario() > 0) {

            /**
             * ***************************** EDITAR USUARIO
             * ****************************************
             */
            if (usuario.Direcciones.get(0).getIdDireccion() == -1) { //Editar usuario

                if (imagen != null) {
                    String nombre = imagen.getOriginalFilename();
                    //archivo.jpg
                    //[archivo,jpg]
                    String extension = nombre.split("\\.")[1];
                    if (extension.equals("jpg")) {
                        try {
                            byte[] bytes = imagen.getBytes();
                            String base64Image = Base64.getEncoder().encodeToString(bytes);
                            usuario.setImagen(base64Image);
                        } catch (Exception ex) {
                            System.out.println("Error");
                        }

                    }

                    RestTemplate restTemplate = new RestTemplate();

                    ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:8081/usuarioapi/" + usuario.getIdUsuario(),
                            HttpMethod.PUT,
                            new HttpEntity<>(usuario),
                            new ParameterizedTypeReference<Result>() {
                    });

                    if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                        model.addAttribute("usuario", new Usuario());

                        Result result = responseEntity.getBody();

                        model.addAttribute("Usuario", usuario);

                    }
                }

                return "redirect:/usuario/action/" + usuario.getIdUsuario();

                /**
                 * ***************************** AGREGAR DIRECCION
                 * ****************************************
                 */
            } else if (usuario.Direcciones.get(0).getIdDireccion() == 0) { //Agregar direccion

                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Usuario> entity = new HttpEntity<>(usuario, headers);

                ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange("http://localhost:8081/direccionapi",
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<Result<Usuario>>() {
                });

                if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                    model.addAttribute("usuario", new Usuario());

                    Result result = responseEntity.getBody();

                    model.addAttribute("Usuario", usuario);

                    usuario.Direcciones.get(0).IdUsuario = usuario.getIdUsuario();
                    //Result result = direccionJPADAOImplementation.Add(usuario);
                }
                return "redirect:/usuario/action/" + usuario.getIdUsuario();

                /**
                 * ***************************** EDITAR DIRECCION
                 * ****************************************
                 */
            } else { //Editar direccion

                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:8081/direccionapi/" + usuario.Direcciones.get(0).getIdDireccion(),
                        HttpMethod.PUT,
                        new HttpEntity<>(usuario),
                        new ParameterizedTypeReference<Result>() {
                });

                if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                    model.addAttribute("usuario", new Usuario());

                    //usuario.Direcciones.get(0).IdUsuario = usuario.getIdUsuario();
                    Result result = responseEntity.getBody();

                    model.addAttribute("Usuario", usuario);

                }

                return "redirect:/usuario/action/" + usuario.getIdUsuario();

            }
        }

        //Si bindingResult tiene errores...
        if (bindingResult.hasErrors()) {
//            model.addAttribute("Usuario", usuario);
//
//            //Volver a pintar la lista de roles
//            model.addAttribute("roles", rolJPADAOImplementation.GetAll().objects);

            return "UsuarioForm";
        } else {
//            //Autoinferencia
//            Result result = usuarioDAOImplementation.Add(usuario);

            return "redirect:/usuario";
        }
    }

    //Proceso de eliminado de direccion
    @GetMapping("/delete/{IdDireccion}")
    public String Delete(Model model,
            @PathVariable("IdDireccion") int idDireccion,
            @ModelAttribute("Usuario") Usuario usuario) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:8081/direccionapi/" + idDireccion,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result>() {
        });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            model.addAttribute("usuario", new Usuario());

            Result result = responseEntity.getBody();

            model.addAttribute("Usuario", usuario);

        }

        return "redirect:/usuario";
    }

    //Proceso de eliminado de usuario
    @GetMapping("/EliminarUsuario/{IdUsuario}")
    public String EliminarUsuario(Model model,
            @PathVariable("IdUsuario") int idUsuario
    ) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:8081/usuarioapi/" + idUsuario,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result>() {
        });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            model.addAttribute("usuario", new Usuario());

            //usuario.Direcciones.get(0).IdUsuario = usuario.getIdUsuario();
            Result result = responseEntity.getBody();

            //model.addAttribute("Usuario", usuario);
        }

        return "redirect:/usuario";
    }

    /*
    //DropdownList Pais
    @GetMapping("getEstadosByIdPais/{IdPais}")
    @ResponseBody //Retorna un dato estructurado - JSON
    public Result EstadoByIdPais(@PathVariable int IdPais, Model model) {

        //EstadoByIdPais
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Estado>>> responseEntityEstado = restTemplate.exchange("http://localhost:8081/direccionapi/EstadoByIdPais/" + IdPais,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Estado>>>() {
        });

        //if (responseEntityEstado.getStatusCode() == HttpStatusCode.valueOf(200)) {
        model.addAttribute("estado", new Estado());

        Result resultEstado = responseEntityEstado.getBody();

        model.addAttribute("estados", resultEstado.object);
        //}

        return resultEstado;
    }

    //DropdownList Estado
    @GetMapping("getMunicipiosByIdEstado/{IdEstado}")
    @ResponseBody //Retorna un dato estructurado - JSON
    public Result MunicipioByIdEstado(@PathVariable int IdEstado, Model model) {

        //EstadoByIdPais
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Municipio>>> responseEntityMunicipio = restTemplate.exchange("http://localhost:8081/direccionapi/MunicipioByIdEstado/" + IdEstado,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Municipio>>>() {
        });

        //if (responseEntityEstado.getStatusCode() == HttpStatusCode.valueOf(200)) {
        model.addAttribute("municipio", new Estado());

        Result resultEstado = responseEntityMunicipio.getBody();
        model.addAttribute("municipios", resultEstado.object);
        //}

        return resultEstado;
    }

    //DropdownList Municipio
    @GetMapping("getColoniasByIdMunicipio/{IdMunicipio}")
    @ResponseBody //Retorna un dato estructurado - JSON
    public Result ColoniaByIdMunicipio(@PathVariable int IdMunicipio, Model model) {

        //EstadoByIdPais
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Colonia>>> responseEntityColonia = restTemplate.exchange("http://localhost:8081/direccionapi/ColoniaByIdMunicipio/" + IdMunicipio,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Colonia>>>() {
        });

        //if (responseEntityEstado.getStatusCode() == HttpStatusCode.valueOf(200)) {
        model.addAttribute("colonia", new Estado());

        Result resultEstado = responseEntityColonia.getBody();
        model.addAttribute("colonias", resultEstado.object);
        //}

        return resultEstado;
    }
     */
    //Mostrar la vista de Carga masiva
    @GetMapping("cargamasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model, HttpSession session) {
        
        try {
            
            //Crear headers para Multipart
            HttpHeaders headers =  new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            //Creamos el cuerpo del archivo
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            //Llamar al servicio REST
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    "http://localhost:8081/usuarioapi/cargamasiva",
                    HttpMethod.POST,
                    requestEntity,
                    Result.class
            );
            
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {

            Result result = responseEntity.getBody();
            model.addAttribute("resultado", result);

            //model.addAttribute("Usuario", usuario);
        }
            
        } catch (Exception ex) {
        }
        
        return "redirect:/usuario";
    }

//    @PostMapping("cargamasiva")
//    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model, HttpSession session) {
//
//        String root = System.getProperty("user.dir");
//        String rutaArchivo = "/src/main/resources/archivos/";
//        String fechaSubida = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//        String rutaFinal = root + rutaArchivo + fechaSubida + file.getOriginalFilename();
//
//        try {
//            file.transferTo(new File(rutaFinal));
//        } catch (Exception ex) {
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        //Txt
//        if (file.getOriginalFilename().split("\\.")[1].equals("txt")) {
//            List<Usuario> usuarios = ProcesarTXT(new File(rutaFinal));
//            List<ErrorCM> errores = ValidarDatos(usuarios);
//
//            if (errores.isEmpty()) {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", true);
//                session.setAttribute("path", rutaFinal);
//            } else {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", false);
//            }
//
//            //Excel
//        } else {
//            List<Usuario> usuarios = ProcesarExcel(new File(rutaFinal));
//            List<ErrorCM> errores = ValidarDatos(usuarios);
//
//            if (errores.isEmpty()) {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", true);
//                session.setAttribute("path", rutaFinal);
//            } else {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", false);
//            }
//        }
//
//        return "CargaMasiva";
//
//    }
//
//    @GetMapping("cargamasiva/procesar")
//    public String CargaMasiva(HttpSession session) {
//        try {
//            String ruta = session.getAttribute("path").toString();
//
//            List<Usuario> usuarios;
//
//            if (ruta.split("\\.")[1].equals("txt")) {
//                usuarios = ProcesarTXT(new File(ruta));
//            } else {
//                usuarios = ProcesarExcel(new File(ruta));
//            }
//
//            for (Usuario usuario : usuarios) {
//                usuarioDAOImplementation.Add(usuario);
//            }
//
//            session.removeAttribute("path");
//
//        } catch (Exception ex) {
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        return "redirect:/usuario";
//    }
//
//    private List<Usuario> ProcesarTXT(File file) {
//        try {
//
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//
//            String linea = "";
//            List<Usuario> usuarios = new ArrayList<>();
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//            //Mientras haya usuarios... 
//            while ((linea = bufferedReader.readLine()) != null) {
//                String[] campos = linea.split("\\|");
//                Usuario usuario = new Usuario();
//                usuario.setUserName(campos[0]);
//                usuario.setNombre(campos[1]);
//                usuario.setApellidoPaterno(campos[2]);
//                usuario.setApellidoMaterno(campos[3]);
//                usuario.setEmail(campos[4]);
//                usuario.setPassword(campos[5]);
//
//                Date fecha = campos[6] == "" ? null : simpleDateFormat.parse(campos[6]);
//                usuario.setFechaNacimiento(fecha);
//                //usuario.setFechaNacimiento(campos[6] != "" ? simpleDateFormat.parse(campos[6]) : simpleDateFormat.parse(campos[6]));
//
//                usuario.setSexo(campos[7]);
//                usuario.setTelefono(campos[8]);
//                usuario.setCelular(campos[9]);
//                usuario.setCurp(campos[10]);
//
//                usuario.Rol = new Rol();
//                usuario.Rol.setIdRol(campos[11] != "" ? Integer.parseInt(campos[11]) : 0);
//                
//
//                usuario.Direcciones = new ArrayList<>();
//                Direccion direccion = new Direccion();
//
//                direccion.setCalle(campos[12]);
//                direccion.setNumeroInterior(campos[13]);
//                direccion.setNumeroExterior(campos[14]);
//
//                direccion.Colonia = new Colonia();
//                direccion.Colonia.setIdColonia(campos[15] != "" ? Integer.parseInt(campos[15]) : 0);
//
//                usuarios.add(usuario);
//                usuario.Direcciones.add(direccion);
//
//            }
//
//            return usuarios;
//
//        } catch (Exception ex) {
//            System.out.println("Error");
//            return null;
//        }
//    }
//
//    private List<Usuario> ProcesarExcel(File file) {
//        List<Usuario> usuarios = new ArrayList<>();
//
//        //Abrimos el excel de forma implicita
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//            for (Row row : sheet) {
//                Usuario usuario = new Usuario();
//
//                usuario.setUserName(row.getCell(0) != null ? row.getCell(0).toString() : "");
//                usuario.setNombre(row.getCell(1) != null ? row.getCell(1).toString() : "");
//                usuario.setApellidoPaterno(row.getCell(2) != null ? row.getCell(2).toString() : "");
//                usuario.setApellidoMaterno(row.getCell(3) != null ? row.getCell(3).toString() : "");
//                usuario.setEmail(row.getCell(4) != null ? row.getCell(4).toString() : "");
//                usuario.setPassword(row.getCell(5) != null ? row.getCell(5).toString() : "");
//
//                if (row.getCell(6) != null) {
//                    if (row.getCell(6).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(6))) {
//                        usuario.setFechaNacimiento(row.getCell(6).getDateCellValue());
//                    } else {
//                        usuario.setFechaNacimiento(simpleDateFormat.parse(row.getCell(6).toString()));
//                    }
//                }
//                //usuario.setFechaNacimiento(row.getCell(6) != null ? row.getCell(6) : "");
//                usuario.setSexo(row.getCell(7) != null ? row.getCell(7).toString() : "");
//
//                //String numero = row.getCell(8).toString();
//                DataFormatter dataFormatter = new DataFormatter();
//                usuario.setTelefono(row.getCell(8) != null ? dataFormatter.formatCellValue(row.getCell(8)) : "");
//                usuario.setCelular(row.getCell(9) != null ? dataFormatter.formatCellValue(row.getCell(9)) : "");
//                usuario.setCurp(row.getCell(10) != null ? row.getCell(10).toString() : "");
//
//                usuario.Rol = new Rol();
//                usuario.Rol.setIdRol(row.getCell(11) != null ? (int) row.getCell(11).getNumericCellValue() : 0);
//
//                usuario.Direcciones = new ArrayList<>();
//                Direccion direccion = new Direccion();
//
//                direccion.setCalle(row.getCell(12) != null ? row.getCell(12).toString() : "");
//                direccion.setNumeroInterior(row.getCell(13) != null ? dataFormatter.formatCellValue(row.getCell(13)) : "");
//                direccion.setNumeroExterior(row.getCell(14) != null ? dataFormatter.formatCellValue(row.getCell(14)) : "");
//
//                direccion.Colonia = new Colonia();
//                direccion.Colonia.setIdColonia(row.getCell(15) != null ? (int) row.getCell(15).getNumericCellValue() : 0);
//
//                usuarios.add(usuario);
//                usuario.Direcciones.add(direccion);
//            }
//
//            return usuarios;
//
//        } catch (Exception ex) {
//            return null;
//        }
//
//    }
//    
//    //Lógica para validar datos de procesar excel / txt
//    private List<ErrorCM> ValidarDatos(List<Usuario> usuarios) {
//        List<ErrorCM> errores = new ArrayList<>();
//        int linea = 1;
//        for (Usuario usuario : usuarios) {
//            if (usuario.getUserName() == null || usuario.getUserName() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getUserName(), "Username es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getNombre() == null || usuario.getNombre() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getNombre(), "Nombre es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getApellidoPaterno(), "Apellido paterno es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getApellidoMaterno() == null || usuario.getApellidoMaterno() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getApellidoMaterno(), "Apellido materno es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getEmail() == null || usuario.getEmail() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getEmail(), "Email es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getPassword() == null || usuario.getPassword() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getPassword(), "Password es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            
//            if (usuario.getSexo() == null || usuario.getSexo() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getSexo(), "Sexo es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getTelefono() == null || usuario.getTelefono() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getTelefono(), "Telefono es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getCelular() == null || usuario.getCelular() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getCelular(), "Campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.getCurp() == null || usuario.getCurp() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getCurp(), "Celular es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.Rol.getIdRol() == 0) {
//                ErrorCM errorCM = new ErrorCM(linea, String.valueOf(usuario.getIdRol()), "Id Rol es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.Direcciones.get(0).getCalle() == null || usuario.Direcciones.get(0).getCalle() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.Direcciones.get(0).getCalle(), "Direccion es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.Direcciones.get(0).getNumeroInterior() == null || usuario.Direcciones.get(0).getNumeroInterior() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.Direcciones.get(0).getCalle(), "Numero interior es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.Direcciones.get(0).getNumeroExterior() == null || usuario.Direcciones.get(0).getNumeroExterior() == "") {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.Direcciones.get(0).getCalle(), "Numero exterior es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            if (usuario.Direcciones.get(0).Colonia.getIdColonia() == 0) {
//                ErrorCM errorCM = new ErrorCM(linea, String.valueOf(usuario.Direcciones.get(0).Colonia.getIdColonia()), "Id Colonia es un campo obligatorio");
//                errores.add(errorCM);
//
//            }
//
//            //Expresiones Regex
//            String SoloLetras = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]*$"; // Nombre, ApellidoPaterno, ApellidoMaterno
//            String SoloNumeros = "^[0-9]*$";
//            String SoloLetrasNumeros = "^[a-zA-Z0-9\\s]*$"; //Curp
//            String ValidarUserName = "^[^_|0-9|\\s]+[a-zA-Z0-9]+$"; //UserName
//            String ValidarEmail = "^[a-zA-Z-0-9]+[^-|\\s]+@[a-zA-Z]+\\.[a-z]+"; //Email
//            String ValidarCalle = "^[^@$!%*?&]+$"; //Calle
//
//            if (usuario.getUserName().matches(ValidarUserName)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getUserName(), "Username inválido");
//                errores.add(errorCM);
//            }
//            if (usuario.getNombre().matches(SoloLetras)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getNombre(), "No se permiten números");
//                errores.add(errorCM);
//            }
//            if (usuario.getApellidoPaterno().matches(SoloLetras)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getApellidoPaterno(), "No se permiten números");
//                errores.add(errorCM);
//            }
//            if (usuario.getApellidoMaterno().matches(SoloLetras)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getApellidoMaterno(), "No se permiten números");
//                errores.add(errorCM);
//            }
//            if (usuario.getEmail().matches(ValidarEmail)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getEmail(), "Formato de correo inválido");
//                errores.add(errorCM);
//            }
//            if (usuario.getSexo().matches(SoloLetras)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getSexo(), "Solo se permiten letras");
//                errores.add(errorCM);
//            }
//            if (usuario.getTelefono().matches(SoloNumeros)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getTelefono(), "Solo se permiten numeros");
//                errores.add(errorCM);
//            }
//            if (usuario.getCelular().matches(SoloNumeros)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getCelular(), "Solo se permiten numeros");
//                errores.add(errorCM);
//            }
//            if (usuario.getCurp().matches(SoloLetrasNumeros)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.getCurp(), "Formato de CURP inválido");
//                errores.add(errorCM);
//            }
//            /*if (String.valueOf(usuario.Rol.getIdRol()).matches(SoloNumeros)) {   
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, String.valueOf(usuario.Rol.getIdRol()), "Solo los numeros estan permitidos");
//                errores.add(errorCM);
//            }*/
//            if (usuario.Direcciones.get(0).getCalle().matches(ValidarCalle)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.Direcciones.get(0).getCalle(), "No se mermiten caracteres especiales");
//                errores.add(errorCM);
//            }
//            if (usuario.Direcciones.get(0).getNumeroInterior().matches(SoloNumeros)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.Direcciones.get(0).getNumeroInterior(), "Solo los numeros están permitidos");
//                errores.add(errorCM);
//            }
//            if (usuario.Direcciones.get(0).getNumeroExterior().matches(SoloNumeros)) {
//            } else {
//                ErrorCM errorCM = new ErrorCM(linea, usuario.Direcciones.get(0).getNumeroExterior(), "Solo los numeros están permitidos");
//                errores.add(errorCM);
//            }
//
//            linea++;
//
//        }
//
//        return errores;
//    }
//    
//    //Lógica para cambiar el status del usuario
//    @GetMapping("/cambiarEstado/{IdUsuario}")
//    @ResponseBody
//    public Result cambiarEstado(@PathVariable int IdUsuario) {
//        
//        return usuarioJPADAOImplementation.BajaLogica(IdUsuario);
//    } 
}
