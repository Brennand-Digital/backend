package com.brennandDigital.Projeto.Controllers;

import com.brennandDigital.Projeto.Domain.Aviso.Aviso;
import com.brennandDigital.Projeto.Services.AvisoServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/avisos")
@Slf4j
public class AvisoController {

    private final AvisoServices avisoServices;

    public AvisoController(AvisoServices avisoServices){
        this.avisoServices = avisoServices;
    }

    @GetMapping
    public ResponseEntity<List<Aviso>> listAvisos(){
        List<Aviso> avisos = avisoServices.getAllAvisos();
        if(avisos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(avisos, HttpStatus.OK);
    }

    @GetMapping(value = "/{avisoId}")
    public ResponseEntity<Aviso> findAvisoById(@PathVariable String avisoId){
        Aviso aviso = avisoServices.findAvisoById(avisoId);
        return new ResponseEntity<>(aviso, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{avisoId}")
    public ResponseEntity<Aviso> deleteAvisoById(@PathVariable String avisoId){
        avisoServices.deleteAviso(avisoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{avisoId}")
    public ResponseEntity<Aviso> updateAviso(@PathVariable String avisoId, @RequestBody Aviso avisoDetails){
        try{
            avisoDetails = avisoServices.updateAviso(avisoId, avisoDetails);
            return new ResponseEntity<>(avisoDetails, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Aviso> addAviso (@RequestBody Aviso aviso){
        aviso = avisoServices.createAviso(aviso);
        return new ResponseEntity<>(aviso, HttpStatus.CREATED);
    }


}
