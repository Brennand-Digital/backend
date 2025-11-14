package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.Aviso;
import com.brennandDigital.Projeto.Repositories.AvisoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AvisoServices {

    private final AvisoRepository avisoRepository;

    public AvisoServices(AvisoRepository avisoRepository){
        this.avisoRepository = avisoRepository;
    }

    public List<Aviso> getAllAvisos(){
        return avisoRepository.findAll();
    }

    public Aviso findAvisoById(String avisoId){
        Optional<Aviso> aviso = avisoRepository.findById(avisoId);
        return aviso.orElseThrow(() -> new ResourceNotFoundException(avisoId));
    }

    public Aviso createAviso(Aviso aviso){
        return avisoRepository.save(aviso);
    }

    public Aviso updateAviso(String avisoId, Aviso avisoDetails) throws Exception {
       Aviso aviso= avisoRepository
               .findById(avisoId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + avisoId));

            aviso.setDescricao(avisoDetails.getDescricao());
            aviso.setTitulo(avisoDetails.getTitulo());
            aviso.setDate(avisoDetails.getDate());
            return avisoRepository.save(aviso);
    }

    public void deleteAviso(String avisoId){
        try{
            avisoRepository.deleteById(avisoId);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
