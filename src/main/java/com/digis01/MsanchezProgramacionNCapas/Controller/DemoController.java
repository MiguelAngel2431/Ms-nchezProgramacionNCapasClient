package com.digis01.MsanchezProgramacionNCapas.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //Define una clase como controlador
@RequestMapping("demo") //Define una ruta del controlador
public class DemoController {

    @GetMapping("HolaMundo")
    public String HolaMundo() {
        return "HolaMundo";
    }

    @GetMapping("/persona/{persona}")
    public String Persona(@PathVariable String persona, Model model) {
        model.addAttribute("persona", persona);

        return "HolaMundo";
    }

    @GetMapping("/buscar")
    public String buscarUser(@RequestParam String nombre, @RequestParam String edad, Model model) {
        model.addAttribute("nombre", nombre);
        model.addAttribute("edad", edad);
        return "HolaMundo";
    }

}
