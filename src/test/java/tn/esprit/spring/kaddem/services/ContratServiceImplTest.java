package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void retrieveAllContrats() {
        List<Contrat> contrats = Arrays.asList(new Contrat(), new Contrat());
        when(contratRepository.findAll()).thenReturn(contrats);

        List<Contrat> result = contratService.retrieveAllContrats();

        assertEquals(2, result.size());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    @Order(2)
    void updateContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.updateContrat(contrat);

        assertNotNull(result, "Le contrat mis à jour ne doit pas être nul");
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    @Order(3)
    void addContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.addContrat(contrat);

        assertNotNull(result, "Le contrat ajouté ne doit pas être nul");
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    @Order(4)
    void retrieveContrat() {
        int id = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(id)).thenReturn(Optional.of(contrat));

        Contrat result = contratService.retrieveContrat(id);

        assertNotNull(result, "Le contrat récupéré ne doit pas être nul");
        verify(contratRepository, times(1)).findById(id);
    }

    @Test
    @Order(5)
    void removeContrat() {
        int id = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(id)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(id);

        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    @Order(6)
    void affectContratToEtudiant() {
        int idContrat = 1;
        String nomE = "John";
        String prenomE = "Doe";

        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(new HashSet<>()); // Initialiser les contrats pour éviter NullPointerException
        Contrat contrat = new Contrat();

        // Simuler le retour d'un Optional contenant un contrat pour éviter NullPointerException
        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findById(idContrat)).thenReturn(Optional.of(contrat));

        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);

        assertEquals(etudiant, result.getEtudiant(), "L'étudiant assigné doit être celui attendu");
        verify(contratRepository, times(1)).save(contrat);
    }


    @Test
    @Order(7)
    void nbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 86400000L * 30); // 30 jours dans le futur
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        int result = contratService.nbContratsValides(startDate, endDate);

        assertEquals(5, result, "Le nombre de contrats valides doit être de 5");
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }

    @Test
    @Order(8)

    void retrieveAndUpdateStatusContrat() {
        Contrat contrat = new Contrat();
        contrat.setArchive(false);
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000)); // Contrat expiré depuis 15 jours

        List<Contrat> contrats = new ArrayList<>(); // Initialiser la liste pour éviter NullPointerException
        contrats.add(contrat);

        when(contratRepository.findAll()).thenReturn(contrats);

        contratService.retrieveAndUpdateStatusContrat();

        assertTrue(contrat.getArchive(), "Le contrat doit être archivé après expiration");
        verify(contratRepository, times(1)).save(contrat);
    }


    @Test
    @Order(9)
    void getChiffreAffaireEntreDeuxDates() {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 86400000L * 60); // 60 jours dans le futur
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);

        when(contratRepository.findAll()).thenReturn(Collections.singletonList(contrat));

        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        assertTrue(result > 0, "Le chiffre d'affaires doit être positif pour la période donnée");
    }
}
