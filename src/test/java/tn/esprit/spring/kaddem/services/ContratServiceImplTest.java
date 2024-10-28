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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void retrieveAllContrats() {
        List<Contrat> contrats = Arrays.asList(new Contrat(), new Contrat());
        when(contratRepository.findAll()).thenReturn(contrats);

        List<Contrat> result = contratService.retrieveAllContrats();

        assertEquals(2, result.size());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void updateContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.updateContrat(contrat);

        assertNotNull(result);
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void addContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.addContrat(contrat);

        assertNotNull(result);
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void retrieveContrat() {
        int id = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(id)).thenReturn(Optional.of(contrat));

        Contrat result = contratService.retrieveContrat(id);

        assertNotNull(result);
        verify(contratRepository, times(1)).findById(id);
    }

    @Test
    void removeContrat() {
        int id = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(id)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(id);

        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void affectContratToEtudiant() {
        int idContrat = 1;
        String nomE = "John";
        String prenomE = "Doe";

        Etudiant etudiant = new Etudiant();
        Contrat contrat = new Contrat();

        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);

        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);

        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void nbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        int result = contratService.nbContratsValides(startDate, endDate);

        assertEquals(5, result);
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }

    @Test
    void retrieveAndUpdateStatusContrat() {
        Contrat contrat = new Contrat();
        contrat.setArchive(false);
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000)); // 15 jours passés

        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat));

        contratService.retrieveAndUpdateStatusContrat();

        assertTrue(contrat.getArchive());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void getChiffreAffaireEntreDeuxDates() {
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000); // 60 jours dans le futur
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);

        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat));

        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        assertTrue(result > 0);
    }
}