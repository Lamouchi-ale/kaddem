package tn.esprit.spring.kaddem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Integer credits;

    private String description;

    // Relation ManyToMany avec Etudiant
    @ManyToMany(mappedBy = "cours")
    private Set<Etudiant> etudiants;
}
