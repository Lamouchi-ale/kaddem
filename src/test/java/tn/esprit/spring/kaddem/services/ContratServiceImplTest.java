package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContratServiceImplTest {

    @Mock
    ContratRepository contratRepository;

    @Mock
    EtudiantRepository etudiantRepository;

    @InjectMocks
    ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllContrats() {
        List<Contrat> contratList = Arrays.asList(new Contrat(), new Contrat());
        when(contratRepository.findAll()).thenReturn(contratList);

        List<Contrat> result = contratService.retrieveAllContrats();
        assertEquals(2, result.size(), "Should return 2 contrats");
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testAddContrat() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.addContrat(contrat);
        assertEquals(1, result.getIdContrat(), "Contract ID should be 1");
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testRetrieveContrat() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        Contrat result = contratService.retrieveContrat(1);
        assertNotNull(result, "Should retrieve contrat with ID 1");
        assertEquals(1, result.getIdContrat());
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateContrat() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.updateContrat(contrat);
        assertNotNull(result, "Updated contrat should not be null");
        assertEquals(1, result.getIdContrat());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testRemoveContrat() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(1);
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void testAffectContratToEtudiant() {
        int idContrat = 1;
        String nomE = "John";
        String prenomE = "Doe";

        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setContrats(new HashSet<>());

        Contrat contrat = new Contrat();
        contrat.setIdContrat(idContrat);
        contrat.setArchive(false);

        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);

        assertNotNull(result.getEtudiant(), "Contrat should be assigned to an Etudiant");
        assertEquals(etudiant, result.getEtudiant(), "Assigned student should match the expected Etudiant");
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testNbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        Integer result = contratService.nbContratsValides(startDate, endDate);
        assertEquals(5, result, "Should return 5 valid contrats between the dates");
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }

    @Test
    void testRetrieveAndUpdateStatusContrat() {
        Contrat contrat1 = new Contrat();
        contrat1.setIdContrat(1);
        contrat1.setDateFinContrat(new Date(System.currentTimeMillis() - 15L * 24 * 60 * 60 * 1000));
        contrat1.setArchive(false);

        Contrat contrat2 = new Contrat();
        contrat2.setIdContrat(2);
        contrat2.setDateFinContrat(new Date(System.currentTimeMillis()));
        contrat2.setArchive(false);

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        when(contratRepository.findAll()).thenReturn(contrats);

        contratService.retrieveAndUpdateStatusContrat();

        assertTrue(contrat1.getArchive(), "Contrat should be archived if it has expired");
        assertTrue(contrat2.getArchive(), "Contrat should be archived if it ends today");
        verify(contratRepository, times(2)).save(any(Contrat.class));
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDates() {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // +30 days

        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(Specialite.IA);

        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(Specialite.CLOUD);

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        when(contratRepository.findAll()).thenReturn(contrats);

        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Expected revenue: (300 for IA + 400 for CLOUD) for 1 month
        float expectedRevenue = (300 + 400);
        assertEquals(expectedRevenue, result, 0.01, "Chiffre d'affaire should match expected value");
    }
}
