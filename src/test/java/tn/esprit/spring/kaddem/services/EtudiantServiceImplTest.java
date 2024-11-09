package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Option;
import tn.esprit.spring.kaddem.entities.Cours;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.repositories.CoursRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Assurez-vous que l'extension Mockito est utilisée pour les tests
@ExtendWith(MockitoExtension.class)
public class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private CoursRepository coursRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    private Etudiant etudiant;
    private Contrat contrat;
    private Equipe equipe;
    private Cours cours;

    // Mocked Set for Etudiants in Equipe
    @Mock
    private Set<Etudiant> mockEtudiants;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");

        contrat = new Contrat();
        contrat.setIdContrat(1);

        equipe = new Equipe();
        equipe.setIdEquipe(1);

        cours = new Cours(); // Initialize cours

        // Mock the behavior of getEtudiants() to return a mocked Set
        when(equipe.getEtudiants()).thenReturn(mockEtudiants);
    }
    @Test
    void testAddAndAssignEtudiantToEquipeAndContract() {
        // Mock du repository pour retourner un contrat et une équipe spécifiques
        when(contratRepository.findById(1)).thenReturn(java.util.Optional.of(contrat));
        when(equipeRepository.findById(1)).thenReturn(java.util.Optional.of(equipe));

        // Appeler la méthode du service
        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, 1, 1);

        // Vérifier que l'étudiant n'est pas nul et que les informations sont correctes
        assertNotNull(result);
        assertEquals("John", result.getNomE());
        assertEquals("Doe", result.getPrenomE());

        // Vérifier que les méthodes du repository ont bien été appelées
        verify(contratRepository).findById(1);
        verify(equipeRepository).findById(1);
    }

    @Test
    void testRetrieveAllEtudiants() {
        Etudiant etudiant1 = new Etudiant("Alice", "Smith");
        Etudiant etudiant2 = new Etudiant("Bob", "Johnson");

        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant1, etudiant2));

        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();

        assertEquals(2, etudiants.size());
        assertEquals("Alice", etudiants.get(0).getNomE());
        assertEquals("Bob", etudiants.get(1).getNomE());
    }

    @Test
    void testRetrieveEtudiantsByCours() {
        // Simuler le comportement du repository Cours pour renvoyer un cours spécifique
        when(coursRepository.findById(1)).thenReturn(java.util.Optional.of(cours));

        // Simuler le comportement du repository pour renvoyer des étudiants inscrits au cours
        Etudiant etudiant1 = new Etudiant("Alice", "Smith");
        Etudiant etudiant2 = new Etudiant("Bob", "Johnson");

        when(etudiantRepository.findEtudiantsByCours(cours)).thenReturn(Arrays.asList(etudiant1, etudiant2));

        // Appeler la méthode du service
        List<Etudiant> etudiants = etudiantService.retrieveEtudiantsByCours(1);

        // Vérifier que la liste contient les bons étudiants
        assertEquals(2, etudiants.size());
        assertEquals("Alice", etudiants.get(0).getNomE());
        assertEquals("Bob", etudiants.get(1).getNomE());

        // Vérifier que les méthodes des repositories ont bien été appelées
        verify(coursRepository).findById(1);
        verify(etudiantRepository).findEtudiantsByCours(cours);
    }

    @Test
    void testRetrieveEtudiantsByAdvancedCriteria() {
        // Créer des étudiants avec des options
        Etudiant etudiant1 = new Etudiant("Alice", "Smith", Option.SE);
        Etudiant etudiant2 = new Etudiant("Bob", "Johnson", Option.GAMIX);

        // Mock la méthode de recherche selon l'option
        when(etudiantRepository.findEtudiantsByNomEAndPrenomEAndOp("Alice", "Smith", Option.SE))
                .thenReturn(Arrays.asList(etudiant1));

        // Appeler la méthode du service avec les critères correspondants
        List<Etudiant> etudiants = etudiantService.retrieveEtudiantsByAdvancedCriteria("Alice", "Smith", Option.SE);

        // Vérifier que la liste contient bien un étudiant
        assertEquals(1, etudiants.size());
        assertEquals("Alice", etudiants.get(0).getNomE());

        // Vérifier que la méthode du repository a été appelée avec les bons arguments
        verify(etudiantRepository).findEtudiantsByNomEAndPrenomEAndOp("Alice", "Smith", Option.SE);
    }
}
