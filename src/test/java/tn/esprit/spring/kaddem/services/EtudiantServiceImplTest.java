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

        equipe = mock(Equipe.class); // Mocking Equipe explicitly

        cours = new Cours(); // Initialize cours

        // Mock the behavior of getEtudiants() to return a mocked Set
        when(equipe.getEtudiants()).thenReturn(mockEtudiants); // Mocking the method correctly
    }

    @Test
    void testAddAndAssignEtudiantToEquipeAndContract() {
        // Mock the repository to return a specific contract and team
        when(contratRepository.findById(1)).thenReturn(java.util.Optional.of(contrat));
        when(equipeRepository.findById(1)).thenReturn(java.util.Optional.of(equipe));

        // Call the service method
        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, 1, 1);

        // Verify the result and the interactions
        assertNotNull(result);
        assertEquals("John", result.getNomE());
        assertEquals("Doe", result.getPrenomE());

        // Verify repository methods were called
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
        // Mock Cours repository to return a specific course
        when(coursRepository.findById(1)).thenReturn(java.util.Optional.of(cours));

        Etudiant etudiant1 = new Etudiant("Alice", "Smith");
        Etudiant etudiant2 = new Etudiant("Bob", "Johnson");

        when(etudiantRepository.findEtudiantsByCours(cours)).thenReturn(Arrays.asList(etudiant1, etudiant2));

        // Call service method
        List<Etudiant> etudiants = etudiantService.retrieveEtudiantsByCours(1);

        // Verify results
        assertEquals(2, etudiants.size());
        assertEquals("Alice", etudiants.get(0).getNomE());
        assertEquals("Bob", etudiants.get(1).getNomE());

        // Verify repository methods
        verify(coursRepository).findById(1);
        verify(etudiantRepository).findEtudiantsByCours(cours);
    }

    @Test
    void testRetrieveEtudiantsByAdvancedCriteria() {
        Etudiant etudiant1 = new Etudiant("Alice", "Smith", Option.SE);
        Etudiant etudiant2 = new Etudiant("Bob", "Johnson", Option.GAMIX);

        when(etudiantRepository.findEtudiantsByNomEAndPrenomEAndOp("Alice", "Smith", Option.SE))
                .thenReturn(Arrays.asList(etudiant1));

        List<Etudiant> etudiants = etudiantService.retrieveEtudiantsByAdvancedCriteria("Alice", "Smith", Option.SE);

        assertEquals(1, etudiants.size());
        assertEquals("Alice", etudiants.get(0).getNomE());

        verify(etudiantRepository).findEtudiantsByNomEAndPrenomEAndOp("Alice", "Smith", Option.SE);
    }
}


