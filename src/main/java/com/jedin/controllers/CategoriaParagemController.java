package com.jedin.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/categoria_paragem")
public class CategoriaParagemController{
    
    @GetMapping("/listar")
    public List<CategoriaParagem>(){
        return
    }
    
}