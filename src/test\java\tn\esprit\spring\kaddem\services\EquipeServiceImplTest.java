import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository; // Mock the repository

    @InjectMocks
    private EquipeServiceImpl equipeService; // Service under test

    private Equipe equipe;
    private Etudiant etudiant;
    private Contrat contrat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Initialize test data
        equipe = new Equipe();
        equipe.setIdEquipe(1);
        equipe.setNiveau(Niveau.JUNIOR);
        equipe.setEtudiants(new ArrayList<>());
        
        etudiant = new Etudiant();
        etudiant.setId(1);
        etudiant.setContrats(new HashSet<>());

        contrat = new Contrat();
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() - 10000000L)); // Date more than a year ago
        contrat.setArchive(false);
        etudiant.getContrats().add(contrat);
        equipe.getEtudiants().add(etudiant);
    }

    @Test
    void testRetrieveAllEquipes() {
        // Arrange
        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));

        // Act
        List<Equipe> equipes = equipeService.retrieveAllEquipes();

        // Assert
        assertNotNull(equipes);
        assertEquals(1, equipes.size());
        assertEquals(equipe, equipes.get(0));
    }

    @Test
    void testAddEquipe() {
        // Arrange
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // Act
        Equipe savedEquipe = equipeService.addEquipe(equipe);

        // Assert
        assertNotNull(savedEquipe);
        assertEquals(equipe.getIdEquipe(), savedEquipe.getIdEquipe());
    }

    @Test
    void testDeleteEquipe() {
        // Arrange
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        doNothing().when(equipeRepository).delete(any(Equipe.class));

        // Act
        equipeService.deleteEquipe(1);

        // Assert
        verify(equipeRepository, times(1)).delete(equipe); // Verify that delete was called once
    }

    @Test
    void testEvoluerEquipes() {
        // Arrange
        equipe.setNiveau(Niveau.JUNIOR);
        equipeRepository.save(equipe);
        List<Equipe> equipes = new ArrayList<>();
        equipes.add(equipe);
        
        when(equipeRepository.findAll()).thenReturn(equipes);
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // Act
        equipeService.evoluerEquipes();

        // Assert
        assertEquals(Niveau.SENIOR, equipe.getNiveau()); // The equipe should evolve to SENIOR
        verify(equipeRepository, times(1)).save(equipe); // Verify save method was called once
    }
}
