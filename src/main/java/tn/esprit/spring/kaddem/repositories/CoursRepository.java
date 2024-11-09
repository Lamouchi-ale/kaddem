package tn.esprit.spring.kaddem.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.spring.kaddem.entities.Cours;

public interface CoursRepository extends CrudRepository<Cours,Integer> {
}
