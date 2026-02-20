package ro.cristiansterie.vote.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.Paging;
import ro.cristiansterie.vote.util.TestConstants;
import ro.cristiansterie.vote.util.UserGenderEnum;
import ro.cristiansterie.vote.util.UserNationalityEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @Test
    void getAll_returnsExpectedValue() {
        // Arrange
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setUsername("user1");

        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("user2");

        when(repository.findAll()).thenReturn(List.of(service.convert(user1), service.convert(user2)));

        // Act
        List<UserDTO> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void getAllFaceImagesBase64_returnsExpectedValue() {
        // Arrange
        UserDAO user1 = new UserDAO();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setFaceImage(TestConstants.IMAGE_TEST);

        UserDAO user2 = new UserDAO();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setFaceImage(TestConstants.IMAGE_TEST);

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        Map<Long, byte[]> result = service.getAllFaceImagesBase64();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TestConstants.IMAGE_TEST, result.get(1L));

        verify(repository).findAll();
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void getFiltered_returnsExpectedValue() {
        // Arrange
        UserDAO user1 = new UserDAO();
        user1.setId(1L);
        user1.setUsername("user1");

        UserDAO user2 = new UserDAO();
        user2.setId(2L);
        user2.setUsername("user2");

        UserFilterDTO filter = new UserFilterDTO();
        filter.setObject(service.convert(user1));
        filter.setPaging(new Paging());

        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user1)));

        // Act
        List<UserDTO> result = service.getFiltered(filter);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.size());
        assertNotNull(result.get(0));
        assertEquals(1L, result.get(0).getId().longValue());

        verify(repository).findAll(any(Example.class), any(Pageable.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void countFiltered_returnExpectedValue() {
        // Arrange
        UserDAO user1 = new UserDAO();
        user1.setId(1L);
        user1.setUsername("user1");

        UserDAO user2 = new UserDAO();
        user2.setId(2L);
        user2.setUsername("user2");

        UserFilterDTO filter = new UserFilterDTO();
        filter.setObject(service.convert(user1));

        when(repository.count(any(Example.class))).thenReturn(1L);

        // Act
        Long count = service.countFiltered(filter);

        // Assert
        assertNotNull(count);
        assertEquals(1L, count.longValue());

        verify(repository).count(any());
    }

    @Test
    void get_returnExpectedValue() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        UserDTO result = service.get(1L);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId().longValue());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getVoter_returnExpectedValue() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        UserVoterDTO result = service.getVoter(1L);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId().longValue());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getProfile_returnExpectedValue() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        UserVoterDTO result = service.getProfile(1L);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId().longValue());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void save_persistsEntity() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setRole(UserRoleEnum.VOTANT);
        user.setHasVoted(false);
        user.setCnp(1234567890123456L);
        user.setFaceImage(TestConstants.IMAGE_TEST);
        user.setFirstname("firstname");
        user.setGender(UserGenderEnum.OTHER);
        user.setIdImage(TestConstants.IMAGE_TEST);
        user.setIdNumber(1);
        user.setIdSeries("series");
        user.setLastname("lastname");
        user.setNationality(UserNationalityEnum.FOREIGNER);
        user.setResidenceAddress("address");
        user.setValidityEndDate(1L);
        user.setValidityStartDate(0L);

        when(repository.findByUsername(anyString())).thenReturn(user);
        when(repository.save(any(UserDAO.class))).thenReturn(user);

        // Act
        UserDTO saved = service.save(service.convert(user));

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId().longValue());

        verify(repository, times(1)).findByUsername(anyString());
        verify(repository, times(1)).save(any(UserDAO.class));
    }

    @Test
    void saveProfile_persistsEntity() {
        // Arrange
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("test");
        user.setRole(UserRoleEnum.ADMIN);

        when(repository.findByUsername(anyString())).thenReturn(service.convert(user));
        when(repository.save(any(UserDAO.class))).thenReturn(service.convert(user));

        // Act
        UserVoterDTO saved = service.saveProfile(service.convertToVoter(service.convert(user)));

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId().longValue());

        verify(repository, times(1)).findByUsername(anyString());
        verify(repository, times(1)).save(any(UserDAO.class));
    }

    @Test
    void registerProfile_persistsEntity() {
        // Arrange
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("test");
        user.setRole(UserRoleEnum.VOTANT);
        user.setHasVoted(false);

        UserVoterDTO voter = service.convertToVoter(service.convert(user));
        voter.setCnp(1234567890123456L);

        when(repository.findByCnp(anyLong())).thenReturn(null);
        when(repository.save(any(UserDAO.class))).thenReturn(service.convert(user));

        // Act
        UserVoterDTO saved = service.registerProfile(voter);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId().longValue());

        verify(repository, times(1)).findByCnp(anyLong());
        verify(repository, times(1)).save(any(UserDAO.class));
    }

    @Test
    void delete_removesEntity() {
        // Arrange
        UserDTO user = new UserDTO();
        user.setId(1L);

        // Act
        boolean result = service.delete(1L);

        // Assert
        assertTrue(result);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void hasVotedByUsername_checkIfUserHasVoted() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");
        user.setHasVoted(true);

        when(repository.findByUsername(anyString())).thenReturn(user);

        // Act
        boolean result = service.hasVotedByUsername("test");

        // Assert
        assertTrue(result);

        verify(repository, times(1)).findByUsername(anyString());
    }

    @Test
    void setHasVotedTrue_manipulateVotingStatusForUser() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");

        when((repository.findByUsername(anyString()))).thenReturn(user);

        // Act
        boolean result = service.setHasVotedTrue("test");

        // Assert
        assertTrue(result);

        verify(repository, times(1)).findByUsername(anyString());
    }

    @Test
    void getByUsername_returnExpectedValue() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("test");

        when((repository.findByUsername(anyString()))).thenReturn(user);

        // Act
        UserDTO result = service.getByUsername("test");

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId().longValue());

        verify(repository, times(1)).findByUsername(anyString());
    }

    @Test
    void loadUserByUsername_returnExpectedValue() {
        // Arrange
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setUsername("1");
        user.setPassword("password");
        user.setRole(UserRoleEnum.VOTANT);

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        // when(repository.findByUsername(anyString())).thenReturn(user);

        // Act
        UserDetails details = service.loadUserByUsername("1");

        // Assert
        assertNotNull(details);
        assertNotNull(details.getUsername());

        verify(repository, times(1)).findById(anyLong());
    }
}
