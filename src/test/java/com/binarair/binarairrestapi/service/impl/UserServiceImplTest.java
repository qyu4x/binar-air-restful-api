package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.request.UserUpdateRequest;
import com.binarair.binarairrestapi.model.response.UserProfileResponse;
import com.binarair.binarairrestapi.model.response.UserRegisterResponse;
import com.binarair.binarairrestapi.model.response.UserResponse;
import com.binarair.binarairrestapi.model.response.UserUpdateResponse;
import com.binarair.binarairrestapi.repository.CityRepository;
import com.binarair.binarairrestapi.repository.RoleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.time.LocalDate;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private FirebaseStorageFileService firebaseStorageFileService;

    @Mock
    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void testRegisterUserSuccess() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("qq.khoiri@gmail.com")
                .fullName("Kaguya Shinomiya")
                .password("Secret")
                .build();
        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName(userRegisterRequest.getFullName())
                .email(userRegisterRequest.getEmail())
                .password(userRegisterRequest.getPassword())
                .build();

        Mockito.when(roleRepository.findById(RoleType.BUYER))
                .thenReturn(Optional.of(buyer));

        Mockito.when(userRepository.findByEmail(userRegisterRequest.getEmail()))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoderConfiguration.passwordEncoder())
                .thenReturn(new BCryptPasswordEncoder());

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        UserRegisterResponse userResponse = userService.save(userRegisterRequest);

        Assertions.assertEquals(userRegisterRequest.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(userRegisterRequest.getFullName(), userResponse.getFullName());

        Mockito.verify(roleRepository).findById(RoleType.BUYER);
        Mockito.verify(userRepository).findByEmail(userRegisterRequest.getEmail());
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void testRegisterUserRoleNotFound() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("qq.khoiri@gmail.com")
                .fullName("Kaguya Shinomiya")
                .password("Secret")
                .build();

        Mockito.when(roleRepository.findById(RoleType.BUYER))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.save(userRegisterRequest);
        });

        Mockito.verify(roleRepository).findById(RoleType.BUYER);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("qq.khoiri@gmail.com")
                .fullName("Kaguya Shinomiya")
                .password("Secret")
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName(userRegisterRequest.getFullName())
                .email(userRegisterRequest.getEmail())
                .password(userRegisterRequest.getPassword())
                .build();

        Mockito.when(roleRepository.findById(RoleType.BUYER))
                .thenReturn(Optional.of(buyer));

        Mockito.when(userRepository.findByEmail(userRegisterRequest.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            userService.save(userRegisterRequest);
        });

        Mockito.verify(roleRepository).findById(RoleType.BUYER);
        Mockito.verify(userRepository).findByEmail(userRegisterRequest.getEmail());

    }

    @Test
    void testUpdateUserAccountSuccess() {
        String userId = "kaguya";
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .fullName("Kaguya shinomiya")
                .gender("Woman")
                .cityId("JP")
                .birthDate(LocalDate.now().minusYears(20))
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .build();

        City city = City.builder()
                .name("Japan")
                .codeId("JP")
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(cityRepository.findById(userUpdateRequest.getCityId()))
                .thenReturn(Optional.of(city));

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        UserUpdateResponse updateResponse = userService.update(userUpdateRequest, userId);

        Assertions.assertEquals(userUpdateRequest.getGender(), updateResponse.getGender());
        Assertions.assertEquals(userUpdateRequest.getFullName(), updateResponse.getFullName());
        Assertions.assertEquals(userUpdateRequest.getBirthDate(), updateResponse.getBirthdate());

        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(cityRepository).findById(userUpdateRequest.getCityId());
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));

    }

    @Test
    void testUpdateUserAccountUserNotFound() {
        String userId = "kaguya";
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .fullName("Kaguya shinomiya")
                .gender("Woman")
                .cityId("JP")
                .birthDate(LocalDate.now().minusYears(20))
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.update(userUpdateRequest, userId);
        });

        Mockito.verify(userRepository).findById(userId);

    }

    @Test
    void testUpdateUserAccountCityNotFound() {
        String userId = "kaguya";
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .fullName("Kaguya shinomiya")
                .gender("Woman")
                .cityId("JP")
                .birthDate(LocalDate.now().minusYears(20))
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(cityRepository.findById(userUpdateRequest.getCityId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.update(userUpdateRequest, userId);
        });

        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(cityRepository).findById(userUpdateRequest.getCityId());

    }

    @Test
    void testDeleteUserAccountSuccess() {
        String userId = "kaguya";

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);
        Boolean status = userService.delete(userId);
        Assertions.assertTrue(status);

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository).deleteById(userId);

    }

    @Test
    void testDeleteUserAccountNotExists() {
        String userId = "kaguya";

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.delete(userId);
        });

        Mockito.verify(userRepository).existsById(userId);

    }

    @Test
    void testFindUserByIdSuccess() {
        String userId = "kaguya";

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserResponse userResponse = userService.findById(userId);

        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getFullName(), userResponse.getFullName());
        Assertions.assertEquals(user.getId(), userResponse.getId());

        Mockito.verify(userRepository).findById(userId);

    }

    @Test
    void testFindUserByIdNotFound() {
        String userId = "kaguya";
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.findById(userId);
        });

        Mockito.verify(userRepository).findById(userId);

    }

    @Test
    void testUpdateProfileSuccess() {
        String userId = "kaguya";

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        MockMultipartFile file = new MockMultipartFile("data", "image.jpg", "application/octet-stream", "some image".getBytes());

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(firebaseStorageFileService.doUploadFile(file))
                        .thenReturn("https://someimage.firebase.com");
        UserProfileResponse userProfileResponse = userService.updateProfile(userId, file);
        Assertions.assertEquals("https://someimage.firebase.com", userProfileResponse.getImageURL());

        Mockito.verify(firebaseStorageFileService).doUploadFile(file);
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(userRepository).save(user);

    }

    @Test
    void testUpdateProfileUserNotFound() {
        String userId = "kaguya";
        MockMultipartFile file = new MockMultipartFile("data", "image.jpg", "application/octet-stream", "some image".getBytes());

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.updateProfile(userId, file);
        });

        Mockito.verify(userRepository).findById(userId);

    }

    @Test
    void testUpdateProfileMultiparFileEmpty() {
        String userId = "kaguya";
        MockMultipartFile file = new MockMultipartFile(" ", "", "application/octet-stream", "".getBytes());

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.updateProfile(userId, file);
        });

        Mockito.verify(userRepository).findById(userId);

    }
}