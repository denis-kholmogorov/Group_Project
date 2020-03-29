package project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.models.Dialog;
import project.repositories.DialogRepository;

@Slf4j
@Service
@AllArgsConstructor
public class DialogService {
    private DialogRepository dialogRepository;

    public Dialog findById(Integer id) {
        return dialogRepository.findById(id).orElse(null);
    }

}
