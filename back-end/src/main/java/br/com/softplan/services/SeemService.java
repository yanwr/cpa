package br.com.softplan.services;

import br.com.softplan.models.Seem;
import java.util.List;

public interface SeemService {
    List<Seem> index();

    Seem show(Long id);

    Seem store(Seem seem);

    Seem update(Seem seem);

    void delete(Long id);

    List<Seem> storeAll(List<Seem> opinions);
}