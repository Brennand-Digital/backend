package Services;

import Domain.Aviso.Aviso;
import Repositories.AvisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import Services.Exceptions.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;

@Service
public class AvisoServices {

    @Autowired
    private AvisoRepository avisoRepository;

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
        Optional<Aviso> avisoOpt = avisoRepository.findById(avisoId);

        if(avisoOpt.isPresent()){
            Aviso aviso = findAvisoById(avisoId);
            aviso.setAviso(avisoDetails.getAviso());
            return avisoRepository.save(aviso);
        }
        throw new Exception("Aviso não encontrado com o ID: " + avisoId);
    }

    public void deleteOrder(String avisoId){
        try{
            avisoRepository.deleteById(avisoId);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
